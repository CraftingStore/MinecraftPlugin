package net.craftingstore.core.models.api.inventory;

public class InventoryItemIcon {
    private String material;
    private int amount;
    private InventoryItemEnhancement[] enhancements;

    public String getMaterial() {
        return material;
    }

    public int getAmount() {
        return amount;
    }

    public InventoryItemEnhancement[] getEnhancements() {
        return enhancements;
    }
}
