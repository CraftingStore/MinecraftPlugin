package net.craftingstore.core.models.api.inventory;

public class InventoryItemIcon {
    private final String material;
    private final Integer amount;
    private final int customModelData;
    private final InventoryItemEnhancement[] enhancements;

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
