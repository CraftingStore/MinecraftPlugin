package net.craftingstore.core.scheduler;

import net.craftingstore.core.CraftingStore;

public class InformationUpdater implements Runnable {

    private final CraftingStore instance;

    public InformationUpdater(CraftingStore instance) {
        this.instance = instance;
    }

    public void run() {
        instance.getLogger().debug("Reloading plugin");
        instance.reload();
    }
}
