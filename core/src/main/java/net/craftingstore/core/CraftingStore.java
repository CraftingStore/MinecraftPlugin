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
import net.craftingstore.core.runner.DonationRunner;
import net.craftingstore.core.scheduler.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CraftingStore {

    private CraftingStorePlugin plugin;
    private CraftingStoreAPI api;
    private ProviderSelector selector;
    private CraftingStoreInformation information;
    private DonationRunner donationRunner;
    private boolean enabled = false;
    public final String ADMIN_PERMISSION = "craftingstore.admin";
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Map<Integer, Donation> pendingDonations;

    public CraftingStore(CraftingStorePlugin implementation) {
        this.plugin = implementation;
        this.pendingDonations = new HashMap<>();
        this.api = new CraftingStoreCachedAPI(this);
        this.selector = new ProviderSelector(this);
        this.donationRunner = new DonationRunner(this);
        this.getImplementation().runAsyncTask(() -> {
            try {
                if (this.reload().get()) {
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
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
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

    public Future<Boolean> reload() {
        this.getApi().setToken(this.getImplementation().getToken());
        return executor.submit(() -> {
            try {
                if (this.getApi().token == null || this.getApi().token.isEmpty()) {
                    getLogger().error(String.format(
                            "API key not set in the config. You need to set the correct api key using /%s key <key>.",
                            this.getImplementation().getConfiguration().getMainCommands()[0]
                    ));
                    setEnabled(false);
                    return false;
                }
                Root keyResult = this.getApi().checkKey().get();
                if (!keyResult.isSuccess()) {
                    getLogger().error("API key is invalid. The plugin will not work.");
                    setEnabled(false);
                    return false;
                }
                information = this.getApi().getInformation().get();

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
                getLogger().debug("Startup complete");
            } catch (CraftingStoreApiException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        });
    }

    public void executeQueue() {
        this.donationRunner.runDonations();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (!enabled) {
            getProviderSelector().disconnect();
        }
    }

    public CraftingStoreInformation getInformation() {
        return information;
    }

    public Map<Integer, Donation> getPendingDonations() {
        return pendingDonations;
    }
}
