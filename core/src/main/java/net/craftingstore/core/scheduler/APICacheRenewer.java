package net.craftingstore.core.scheduler;

import net.craftingstore.core.CraftingStore;
import net.craftingstore.core.http.CraftingStoreCachedAPI;
import net.craftingstore.core.exceptions.CraftingStoreApiException;

import java.util.concurrent.ExecutionException;

public class APICacheRenewer implements Runnable {

    private CraftingStore instance;

    public APICacheRenewer(CraftingStore instance) {
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
        this.instance.getLogger().debug("Refreshing API cache.");
        try {
            api.refreshPaymentsCache();
            api.refreshTopDonatorsCache();
        } catch (CraftingStoreApiException | ExecutionException | InterruptedException e) {
            if (this.instance.getLogger().isDebugging()) {
                e.printStackTrace();
            } else {
                instance.getLogger().error("Failed to renew API cache. If this issue persists, please contact support at https://craftingstore.net.");
            }
        }
    }
}
