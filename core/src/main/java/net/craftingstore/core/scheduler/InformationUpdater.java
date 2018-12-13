package net.craftingstore.core.scheduler;

import net.craftingstore.core.CraftingStore;

public class InformationUpdater implements Runnable {

    private CraftingStore instance;

    public InformationUpdater(CraftingStore instance) {
        this.instance = instance;
    }

    public void run() {
        this.instance.getLogger().debug("Reloading plugin");
        instance.reload();
    }
}
