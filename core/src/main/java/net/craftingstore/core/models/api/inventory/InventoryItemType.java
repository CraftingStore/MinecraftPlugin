package net.craftingstore.core.models.api.inventory;

import net.craftingstore.core.models.api.inventory.types.*;

public enum InventoryItemType {
    NULL(null),
    CATEGORY(InventoryItemCategory.class),
    MESSAGE(InventoryItemMessage.class),
    BACK(InventoryItemBackButton.class),
    CLOSE(InventoryItemCloseButton.class),
    BUYABLE_PACKAGE(InventoryItemBuyablePackage.class),
    BUY_DETAILS(InventoryItemBuyDetails.class),
    BUY_BUTTON(InventoryItemBuyButton.class);

    private final Class<? extends InventoryItem> c;

    InventoryItemType(Class<? extends InventoryItem> c) {
        this.c = c;
    }

    public Class<? extends InventoryItem> getActualClass() {
        return c;
    }
}
