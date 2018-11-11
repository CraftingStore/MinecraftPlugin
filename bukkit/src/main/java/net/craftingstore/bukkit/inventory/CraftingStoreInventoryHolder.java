package net.craftingstore.bukkit.inventory;

import net.craftingstore.core.models.api.inventory.CraftingStoreInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class CraftingStoreInventoryHolder implements InventoryHolder {

    private CraftingStoreInventory csInventory;
    private CraftingStoreInventoryHolder parentInventory;

    public CraftingStoreInventoryHolder(CraftingStoreInventory csInventory, CraftingStoreInventoryHolder parentInventory) {
        this.csInventory = csInventory;
        this.parentInventory = parentInventory;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }

    public CraftingStoreInventory getCsInventory() {
        return csInventory;
    }

    public CraftingStoreInventoryHolder getParentInventory() {
        return parentInventory;
    }
}
