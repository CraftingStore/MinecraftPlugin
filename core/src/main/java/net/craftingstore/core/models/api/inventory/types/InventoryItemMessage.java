package net.craftingstore.core.models.api.inventory.types;

import net.craftingstore.core.models.api.inventory.InventoryItem;

public class InventoryItemMessage extends InventoryItem {
    private String[] messages;
    private boolean close;

    public String[] getMessages() {
        return messages;
    }

    public boolean shouldClose() {
        return close;
    }
}
