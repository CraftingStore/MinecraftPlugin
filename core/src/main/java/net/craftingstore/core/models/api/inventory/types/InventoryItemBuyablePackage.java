package net.craftingstore.core.models.api.inventory.types;

import net.craftingstore.core.models.api.inventory.InventoryItem;

public class InventoryItemBuyablePackage extends InventoryItem {
    private int price;
    private int packageId;
    private String[] messages;
    private String successMessage;

    public int getPrice() {
        return price;
    }

    public int getPackageId() {
        return packageId;
    }

    public String[] getMessages() {
        return messages;
    }

    public String getSuccessMessage() {
        return successMessage;
    }
}
