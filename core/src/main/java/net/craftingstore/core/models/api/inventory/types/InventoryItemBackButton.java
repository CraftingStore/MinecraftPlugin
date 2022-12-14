package net.craftingstore.core.models.api.inventory.types;

import net.craftingstore.core.models.api.inventory.InventoryItem;
import net.craftingstore.core.models.api.inventory.InventoryItemIcon;
import net.craftingstore.core.models.api.inventory.InventoryItemType;

public class InventoryItemBackButton extends InventoryItem {
    public InventoryItemBackButton(String name, String[] description, InventoryItemIcon icon, int index) {
        super(name, description, InventoryItemType.BACK, icon, index);
    }
}