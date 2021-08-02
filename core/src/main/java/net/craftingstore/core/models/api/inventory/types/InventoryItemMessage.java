package net.craftingstore.core.models.api.inventory.types;

import net.craftingstore.core.models.api.inventory.InventoryItem;
import net.craftingstore.core.models.api.inventory.InventoryItemIcon;
import net.craftingstore.core.models.api.inventory.InventoryItemType;

public class InventoryItemMessage extends InventoryItem {
    private String[] messages;
    private boolean close;

    public InventoryItemMessage() {
    }

    public InventoryItemMessage(
            String name,
            String[] description,
            InventoryItemIcon icon,
            int index,
            String[] messages,
            boolean close
    ) {
        super(name, description, InventoryItemType.MESSAGE, icon, index);
        this.messages = messages;
        this.close = close;
    }

    public String[] getMessages() {
        return messages;
    }

    public boolean shouldClose() {
        return close;
    }
}
