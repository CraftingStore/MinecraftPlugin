package net.craftingstore.nukkit.inventory;

import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.InventoryHolder;
import net.craftingstore.core.models.api.inventory.CraftingStoreInventory;

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

    public CraftingStoreInventory getCSInventory() {
        return csInventory;
    }

    public CraftingStoreInventoryHolder getParentInventory() {
        return parentInventory;
    }
}
