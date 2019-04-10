package net.craftingstore.sponge.inventory;

import org.spongepowered.api.data.Property;
import org.spongepowered.api.item.inventory.property.AbstractInventoryProperty;

public class CraftingStoreInventoryProperty extends AbstractInventoryProperty<String, InventoryAttachment> {

    public CraftingStoreInventoryProperty(InventoryAttachment inventoryAttachment) {
        super(inventoryAttachment);
    }

    @Override
    public int compareTo(Property<?, ?> o) {
        return 0;
    }
}
