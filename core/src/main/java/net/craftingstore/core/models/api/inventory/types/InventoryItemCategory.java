package net.craftingstore.core.models.api.inventory.types;

import net.craftingstore.core.models.api.inventory.InventoryItem;

public class InventoryItemCategory extends InventoryItem {
    private String title;
    private InventoryItem[] content;
    private int size;

    public String getTitle() {
        return title;
    }

    public InventoryItem[] getContent() {
        return content;
    }

    public int getSize() {
        return size;
    }
}
