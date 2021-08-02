package net.craftingstore.core.models.api;

import net.craftingstore.core.models.api.inventory.InventoryItem;

public class ApiInventory {
    private String title;
    private InventoryItem[] content;
    private int size;
    private ApiInventory buyMenu;

    public String getTitle() {
        return title;
    }

    public InventoryItem[] getContent() {
        return content;
    }

    public int getSize() {
        return size;
    }

    public ApiInventory getBuyMenu() {
        return buyMenu;
    }
}
