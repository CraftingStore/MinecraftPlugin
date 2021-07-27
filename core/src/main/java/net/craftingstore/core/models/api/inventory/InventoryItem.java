package net.craftingstore.core.models.api.inventory;

public class InventoryItem {
    protected String name;
    protected String[] description;
    protected InventoryItemType type;
    protected InventoryItemIcon icon;
    protected int index;

    public InventoryItem() {

    }

    public InventoryItem(String name, String[] description, InventoryItemType type, InventoryItemIcon icon, int index) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.icon = icon;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public String[] getDescription() {
        return description;
    }

    public InventoryItemType getType() {
        return type;
    }

    public InventoryItemIcon getIcon() {
        return icon;
    }

    public int getIndex() {
        return index;
    }
}
