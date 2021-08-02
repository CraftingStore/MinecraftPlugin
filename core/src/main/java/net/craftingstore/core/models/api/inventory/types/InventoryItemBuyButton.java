package net.craftingstore.core.models.api.inventory.types;

import net.craftingstore.core.models.api.inventory.InventoryItem;
import net.craftingstore.core.models.api.inventory.InventoryItemIcon;
import net.craftingstore.core.models.api.inventory.InventoryItemType;

public class InventoryItemBuyButton extends InventoryItem {
    private final InventoryItemBuyButtonAction action;

    public InventoryItemBuyButton(
            String name,
            String[] description,
            InventoryItemIcon icon,
            int index,
            InventoryItemBuyButtonAction action
    ) {
        super(name, description, InventoryItemType.BUY_BUTTON, icon, index);
        this.action = action;
    }

    public InventoryItemBuyButtonAction getAction() {
        return action;
    }

    public enum InventoryItemBuyButtonAction {
        IN_GAME,
        ONLINE,
    }
}
