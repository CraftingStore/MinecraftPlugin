package net.craftingstore.core.scheduler;

import net.craftingstore.core.CraftingStore;

public class ProviderChecker implements Runnable {

    private CraftingStore instance;

    public ProviderChecker(CraftingStore instance) {
        this.instance = instance;
    }

    @Override
    public void run() {
        if (!instance.isEnabled()) {
            return;
        }
        if (instance.getProviderSelector().getCurrentProvider() == null ||
                !instance.getProviderSelector().getCurrentProvider().isConnected()) {
            instance.getLogger().debug("Reconnecting to a provider.");
            instance.getProviderSelector().selectProvider();
        }
    }
}
