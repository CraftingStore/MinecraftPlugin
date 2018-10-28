package net.craftingstore.core.models.api.inventory;

public enum InventoryItemType {
    CATEGORY(InventoryItemCategory.class), LINK(InventoryItemLink.class);

    private Class c;

    InventoryItemType(Class c) {
        this.c = c;
    }

    public Class getActualClass() {
        return c;
    }
}
