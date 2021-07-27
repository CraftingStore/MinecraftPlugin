package net.craftingstore.core.models.api.inventory;

public class InventoryItemIcon {
    private String material;
    private Integer amount;
    private int customModelData;
    private InventoryItemEnhancement[] enhancements;

    public InventoryItemIcon() {
    }

    public InventoryItemIcon(String material, Integer amount, int customModelData, InventoryItemEnhancement[] enhancements) {
        this.material = material;
        this.amount = amount;
        this.customModelData = customModelData;
        this.enhancements = enhancements;
    }

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
