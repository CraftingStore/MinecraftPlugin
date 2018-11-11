package net.craftingstore.core;

import net.craftingstore.core.api.CraftingStoreAPIImpl;
import net.craftingstore.core.exceptions.CraftingStoreApiException;
import net.craftingstore.core.models.api.Root;
import net.craftingstore.core.models.api.misc.CraftingStoreInformation;
import net.craftingstore.core.models.api.misc.UpdateInformation;
import net.craftingstore.core.provider.ProviderSelector;
import net.craftingstore.core.scheduler.DonationChecker;
import net.craftingstore.core.scheduler.ProviderChecker;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CraftingStore {

    private CraftingStorePlugin plugin;
    private CraftingStoreAPI api;
    private ProviderSelector selector;

    public CraftingStore(CraftingStorePlugin implementation) {
        this.plugin = implementation;
        this.api = new CraftingStoreAPIImpl(this);
        this.selector = new ProviderSelector(this);
        if (this.reload()) {
            this.plugin.registerRunnable(new DonationChecker(this), 10, 120);
            this.plugin.registerRunnable(new ProviderChecker(this), 10, 60);
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

    public Logger getLogger() {
        return getImplementation().getLogger();
    }

    public boolean reload() {
        this.getApi().setToken(this.getImplementation().getToken());
        try {
            Root keyResult = this.getApi().checkKey();
            if (!keyResult.isSuccess()) {
                getLogger().log(Level.SEVERE, "API key is invalid. Plugin will be disabled.");
                this.getImplementation().disable();
                return false;
            }
            CraftingStoreInformation information = this.getApi().getInformation();

            if (information.getUpdateInformation() != null) {
                UpdateInformation update = information.getUpdateInformation();
                getLogger().info(update.getMessage());
                if (update.shouldDisable()) {
                    getLogger().log(Level.SEVERE, "Plugin will be disabled until you install the latest update.");
                    this.getImplementation().disable();
                    return false;
                }
            }

            selector.setProviders(information.getProviders());
            selector.selectProvider();
        } catch (CraftingStoreApiException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void executeQueue() {
        try {
            Arrays.stream(this.getApi().getDonationQueue())
                    .forEach(donation -> this.getImplementation().executeDonation(donation));
        } catch (CraftingStoreApiException e) {
            e.printStackTrace();
        }
    }
}
