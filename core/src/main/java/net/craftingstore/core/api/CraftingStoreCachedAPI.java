package net.craftingstore.core.api;

import net.craftingstore.core.CraftingStore;
import net.craftingstore.core.exceptions.CraftingStoreApiException;
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

    public void refreshGUICache() throws CraftingStoreApiException {
        cache.put("plugin/inventory", super.getGUI());
    }
}
