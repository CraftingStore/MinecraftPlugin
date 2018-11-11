package net.craftingstore.core.scheduler;

import net.craftingstore.core.CraftingStore;
import net.craftingstore.core.api.CraftingStoreCachedAPI;
import net.craftingstore.core.exceptions.CraftingStoreApiException;

public class InventoryRenewer implements Runnable {

    private CraftingStore instance;

    public InventoryRenewer(CraftingStore instance) {
        this.instance = instance;
    }

    @Override
    public void run() {
        if (!instance.isEnabled()) {
            return;
        }
        if (!(instance.getApi() instanceof CraftingStoreCachedAPI)) {
            return;
        }
        instance.getLogger().info("Refreshing GUI cache");
        CraftingStoreCachedAPI api = (CraftingStoreCachedAPI) instance.getApi();
        try {
            api.refreshGUICache();
        } catch (CraftingStoreApiException e) {
            e.printStackTrace();
        }
    }
}
