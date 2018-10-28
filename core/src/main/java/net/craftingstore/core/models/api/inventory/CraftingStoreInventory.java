package net.craftingstore.core.models.api.inventory;

public class CraftingStoreInventory {
    private String title;
    private InventoryItem[] content;

    public String getTitle() {
        return title;
    }

    public InventoryItem[] getContent() {
        return content;
    }
}
