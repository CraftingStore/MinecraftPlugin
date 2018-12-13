package net.craftingstore.core;

import net.craftingstore.core.http.CraftingStoreCachedAPI;
import net.craftingstore.core.exceptions.CraftingStoreApiException;
import net.craftingstore.core.jobs.ExecuteDonationsJob;
import net.craftingstore.core.logging.CraftingStoreLogger;
import net.craftingstore.core.models.api.Root;
import net.craftingstore.core.models.api.misc.CraftingStoreInformation;
import net.craftingstore.core.models.api.misc.UpdateInformation;
import net.craftingstore.core.models.donation.Donation;
import net.craftingstore.core.provider.ProviderSelector;
import net.craftingstore.core.scheduler.*;

public class CraftingStore {

    private CraftingStorePlugin plugin;
    private CraftingStoreAPI api;
    private ProviderSelector selector;
    private CraftingStoreInformation information;
    private boolean enabled = false;
    public final String ADMIN_PERMISSION = "craftingstore.admin";

    public CraftingStore(CraftingStorePlugin implementation) {
        this.plugin = implementation;
        this.api = new CraftingStoreCachedAPI(this);
        this.selector = new ProviderSelector(this);
        if (this.reload()) {
            // Every 5 minutes
            this.plugin.registerRunnable(new DonationChecker(this), 10, 5 * 60);
            // Every minute
            this.plugin.registerRunnable(new ProviderChecker(this), 60, 60);
            // Every 20 minutes
            this.plugin.registerRunnable(new InventoryRenewer(this), 20 * 60, 20 * 60);
            // Every 25 minutes
            this.plugin.registerRunnable(new APICacheRenewer(this), 10, 60 * 25);
            // Every 24 hours
            this.plugin.registerRunnable(new InformationUpdater(this), 24 * 60 * 60, 24 * 60 * 60);
        }
    }

    public CraftingStorePlugin getImplementation() {
        return plugin;
    }

    public CraftingStoreAPI getApi() {
        return api;
    }

    public ProviderSelector getProviderSelector() {
        return this.selector;
    }

    public CraftingStoreLogger getLogger() {
        return getImplementation().getLogger();
    }

    public boolean reload() {
        this.getApi().setToken(this.getImplementation().getToken());
        try {
            Root keyResult = this.getApi().checkKey();
            if (!keyResult.isSuccess()) {
                getLogger().error("API key is invalid. The plugin will not work.");
                setEnabled(false);
                return false;
            }
            information = this.getApi().getInformation();

            if (information.getUpdateInformation() != null) {
                UpdateInformation update = information.getUpdateInformation();
                getLogger().info(update.getMessage());
                if (update.shouldDisable()) {
                    getLogger().error("Plugin will be disabled until you install the latest update.");
                    setEnabled(false);
                    return false;
                }
            }
            setEnabled(true);

            selector.setProviders(information.getProviders());
            selector.selectProvider();

            // Renew the inventory on reload / start
            new InventoryRenewer(this).run();
        } catch (CraftingStoreApiException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void executeQueue() {
        try {
            Donation[] donationQueue = this.getApi().getDonationQueue();
            new ExecuteDonationsJob(this, donationQueue);
        } catch (CraftingStoreApiException e) {
            e.printStackTrace();
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public CraftingStoreInformation getInformation() {
        return information;
    }

    public void setInformation(CraftingStoreInformation information) {
        this.information = information;
    }
}
