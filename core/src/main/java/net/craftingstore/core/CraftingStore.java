package net.craftingstore.core;

import net.craftingstore.core.api.CraftingStoreAPIImpl;
import net.craftingstore.core.exceptions.CraftingStoreApiException;
import net.craftingstore.core.models.api.Root;
import net.craftingstore.core.scheduler.DonationChecker;

import java.util.Arrays;
import java.util.logging.Level;

public class CraftingStore {

    private CraftingStorePlugin plugin;
    private CraftingStoreAPI api;

    public CraftingStore(CraftingStorePlugin implementation) {
        this.plugin = implementation;
        this.api = new CraftingStoreAPIImpl(this);
        this.plugin.registerRunnable(new DonationChecker(this), 10, 60);
        this.reload();
    }

    public CraftingStorePlugin getImplementation() {
        return plugin;
    }

    public CraftingStoreAPI getApi() {
        return api;
    }

    public boolean reload() {
        this.getApi().setToken(this.getImplementation().getToken());
        try {
            Root keyResult = this.getApi().checkKey();
            if (!keyResult.isSuccess()) {
                this.getImplementation().getLogger().log(Level.SEVERE, "API key is invalid. Plugin will be disabled.");
                this.getImplementation().disable();
                return false;
            }

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
