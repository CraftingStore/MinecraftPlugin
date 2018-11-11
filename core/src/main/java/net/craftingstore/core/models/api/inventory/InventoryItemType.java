package net.craftingstore.core.models.api.inventory;

import net.craftingstore.core.models.api.inventory.types.InventoryItemBackButton;
import net.craftingstore.core.models.api.inventory.types.InventoryItemCategory;
import net.craftingstore.core.models.api.inventory.types.InventoryItemCloseButton;
import net.craftingstore.core.models.api.inventory.types.InventoryItemMessage;

public enum InventoryItemType {
    CATEGORY(InventoryItemCategory.class),
    MESSAGE(InventoryItemMessage.class),
    BACK(InventoryItemBackButton.class),
    CLOSE(InventoryItemCloseButton.class);

    private Class c;

    InventoryItemType(Class c) {
        this.c = c;
    }

    public Class getActualClass() {
        return c;
    }
}
