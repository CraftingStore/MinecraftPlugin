package net.craftingstore.core.http;

import net.craftingstore.core.CraftingStore;
import net.craftingstore.core.exceptions.CraftingStoreApiException;
import net.craftingstore.core.models.api.ApiPayment;
import net.craftingstore.core.models.api.ApiTopDonator;
import net.craftingstore.core.models.api.inventory.CraftingStoreInventory;

import java.util.HashMap;

public class CraftingStoreCachedAPI extends CraftingStoreAPIImpl {

    private HashMap<String, Object> cache = new HashMap<>();

    public CraftingStoreCachedAPI(CraftingStore instance) {
        super(instance);
    }

    @Override
    public CraftingStoreInventory getGUI() throws CraftingStoreApiException {
        String key = "plugin/inventory";
        if (!cache.containsKey(key)) {
            cache.put(key, super.getGUI());
        }
        return (CraftingStoreInventory) cache.get(key);
    }

    @Override
    public ApiTopDonator[] getTopDonators() throws CraftingStoreApiException {
        String key = "buyers/top";
        if (!cache.containsKey(key)) {
            cache.put(key, super.getTopDonators());
        }
        return (ApiTopDonator[]) cache.get(key);
    }

    @Override
    public ApiPayment[] getPayments() throws CraftingStoreApiException {
        String key = "buyers/recent";
        if (!cache.containsKey(key)) {
            cache.put(key, super.getPayments());
        }
        return (ApiPayment[]) cache.get(key);
    }

    public void refreshGUICache() throws CraftingStoreApiException {
        cache.put("plugin/inventory", super.getGUI());
    }

    public void refreshTopDonatorsCache() throws CraftingStoreApiException {
        cache.put("buyers/top", super.getTopDonators());
    }

    public void refreshPaymentsCache() throws CraftingStoreApiException {
        cache.put("buyers/recent", super.getPayments());
    }
}
