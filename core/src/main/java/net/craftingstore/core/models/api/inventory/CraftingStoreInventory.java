package net.craftingstore.core.models.api.inventory;

import java.util.Arrays;

public class CraftingStoreInventory {
    private String title;
    private InventoryItem[] content;
    private int size;

    public CraftingStoreInventory() {
    }

    public CraftingStoreInventory(String title, InventoryItem[] content, int size) {
        this.title = title;
        this.content = content;
        this.size = size;
    }

    public String getTitle() {
        return title;
    }

    public InventoryItem[] getContent() {
        return content;
    }

    public int getSize() {
        return size;
    }

    public InventoryItem getByIndex(int index) {
        return Arrays.stream(content).filter(i -> i.getIndex() == index).findFirst().orElse(null);
    }
}
