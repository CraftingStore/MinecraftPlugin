package net.craftingstore.core.models.api.inventory;

public class InventoryItemIcon {
    private String material;
    private Integer amount;
    private int customModelData;
    private InventoryItemEnhancement[] enhancements;

    public String getMaterial() {
        return material;
    }

    public int getAmount() {
        return amount;
    }

    public Integer getCustomModelData() {
        return customModelData;
    }

    public InventoryItemEnhancement[] getEnhancements() {
        return enhancements;
    }
}
