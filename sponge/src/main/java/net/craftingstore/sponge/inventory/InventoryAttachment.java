package net.craftingstore.sponge.inventory;

import net.craftingstore.core.models.api.inventory.CraftingStoreInventory;

public class InventoryAttachment {

    private CraftingStoreInventory csInventory;
    private InventoryAttachment parent;


    public InventoryAttachment(CraftingStoreInventory csInventory, InventoryAttachment parent) {
        this.csInventory = csInventory;
        this.parent = parent;
    }

    public CraftingStoreInventory getCsInventory() {
        return csInventory;
    }

    public InventoryAttachment getParent() {
        return parent;
    }
}
