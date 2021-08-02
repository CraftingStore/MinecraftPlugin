package net.craftingstore.bukkit.inventory;

import net.craftingstore.core.models.api.inventory.CraftingStoreInventory;
import net.craftingstore.core.models.api.inventory.types.InventoryItemBuyablePackage;

public class BuyMenuInventoryHolder extends CraftingStoreInventoryHolder {

    private final InventoryItemBuyablePackage itemBuyablePackage;

    public BuyMenuInventoryHolder(
            CraftingStoreInventory csInventory,
            CraftingStoreInventoryHolder parentInventory,
            InventoryItemBuyablePackage itemBuyablePackage
    ) {
        super(csInventory, parentInventory);
        this.itemBuyablePackage = itemBuyablePackage;
    }

    public InventoryItemBuyablePackage getItemBuyablePackage() {
        return itemBuyablePackage;
    }
}
