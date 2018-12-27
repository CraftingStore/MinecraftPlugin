package net.craftingstore.core.scheduler;

import net.craftingstore.core.CraftingStore;
import net.craftingstore.core.http.CraftingStoreCachedAPI;
import net.craftingstore.core.exceptions.CraftingStoreApiException;

import java.util.concurrent.ExecutionException;

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
        CraftingStoreCachedAPI api = (CraftingStoreCachedAPI) instance.getApi();
        instance.getLogger().debug("Refreshing GUI cache.");
        try {
            api.refreshGUICache();
        } catch (CraftingStoreApiException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
