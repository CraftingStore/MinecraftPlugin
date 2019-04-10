package net.craftingstore.sponge.inventory;

import com.google.inject.Inject;
import net.craftingstore.core.models.api.inventory.CraftingStoreInventory;
import net.craftingstore.core.models.api.inventory.InventoryItem;
import net.craftingstore.sponge.CraftingStoreSponge;
import org.spongepowered.api.GameRegistry;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.InventoryDimension;
import org.spongepowered.api.item.inventory.property.InventoryTitle;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.item.inventory.type.GridInventory;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class InventoryBuilder {

    @Inject
    private CraftingStoreSponge spongePlugin;

    @Inject
    private GameRegistry gameRegistry;

    public Inventory buildInventory(CraftingStoreInventory csInventory) {
        return buildInventory(csInventory, null);
    }

    public Inventory buildInventory(CraftingStoreInventory csInventory, InventoryAttachment parent) {
        String title = csInventory.getTitle();
        if (title == null || title.isEmpty()) {
            title = "CraftingStore";
        }
        Text formattedTitle = TextSerializers.FORMATTING_CODE.deserialize(title).toText();

        InventoryAttachment attachment = new InventoryAttachment(csInventory, parent);
        int width = 9;
        int height = csInventory.getSize() / 9;

        Inventory inventory = Inventory.builder()
                .property(new CraftingStoreInventoryProperty(attachment))
                .property(InventoryTitle.of(formattedTitle))
                .property(InventoryDimension.of(width, height))
                .build(this.spongePlugin);

        GridInventory grid = inventory.query(QueryOperationTypes.INVENTORY_TYPE.of(GridInventory.class));
        for (InventoryItem inventoryItem : csInventory.getContent()) {
            ItemType type = ItemTypes.CHEST;
            if (inventoryItem.getIcon().getMaterial() != null) {
                Optional<ItemType> oType = gameRegistry.getType(ItemType.class, inventoryItem.getIcon().getMaterial());
                if (oType.isPresent()) {
                    type = oType.get();
                }
            }
            ItemStack stack = ItemStack.builder()
                    .itemType(type)
                    .quantity(inventoryItem.getIcon().getAmount())
                    .build();

            Text itemName = TextSerializers.FORMATTING_CODE.deserialize(inventoryItem.getName()).toText();
            stack.offer(Keys.DISPLAY_NAME, itemName);
            if (inventoryItem.getDescription() != null && inventoryItem.getDescription().length != 0) {
                stack.offer(Keys.ITEM_LORE, Arrays.stream(inventoryItem.getDescription())
                        .map(d -> TextSerializers.FORMATTING_CODE.deserialize(d).toText())
                        .collect(Collectors.toList()));
            }
            int x = inventoryItem.getIndex() % 9;
            int y = inventoryItem.getIndex() / 9;
            grid.set(x, y, stack);
        }

        return inventory;
    }
}
