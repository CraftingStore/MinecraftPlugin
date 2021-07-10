package net.craftingstore.nukkit.inventory;

import cn.nukkit.inventory.Inventory;
import cn.nukkit.item.Item;
import cn.nukkit.utils.TextFormat;
import com.nukkitx.fakeinventories.inventory.ChestFakeInventory;
import net.craftingstore.core.models.api.inventory.CraftingStoreInventory;
import net.craftingstore.core.models.api.inventory.InventoryItem;
import net.craftingstore.nukkit.CraftingStoreNukkit;

import java.util.Arrays;

public class InventoryBuilder {
    private CraftingStoreNukkit instance;

    public InventoryBuilder(CraftingStoreNukkit instance) {
        this.instance = instance;
    }

    public Inventory buildInventory(CraftingStoreInventory csInventory) {
        return buildInventory(csInventory, null);
    }

    public Inventory buildInventory(CraftingStoreInventory csInventory, CraftingStoreInventoryHolder parent) {
        String title = csInventory.getTitle();
        if (title == null || title.isEmpty()) {
            title = "CraftingStore";
        }
        title = TextFormat.colorize('&', title);
        Inventory inventory = new ChestFakeInventory(new CraftingStoreInventoryHolder(csInventory, parent), title);

        for (InventoryItem inventoryItem : csInventory.getContent()) {
            Item item;
            if (inventoryItem.getIcon().getMaterial() == null) {
                item = Item.get(Item.CHEST);
            } else {
                item = Item.fromString(inventoryItem.getIcon().getMaterial());
                if (item == null) {
                    instance.getCraftingStore().getLogger().debug("Material " + inventoryItem.getIcon().getMaterial() + " not found.");
                    item = Item.get(Item.CHEST);
                }
            }
            Item itemStack = Item.get(item.getId(), inventoryItem.getIcon().getAmount());
            itemStack.setCustomName(TextFormat.colorize('&', inventoryItem.getName()));
            if (inventoryItem.getDescription() != null && inventoryItem.getDescription().length != 0) {
                itemStack.setLore(Arrays.toString(Arrays.stream(inventoryItem.getDescription())
                        .map(d -> TextFormat.colorize('&', d))
                        .toArray())
                );
            }
            inventory.setItem(inventoryItem.getIndex(), itemStack);
        }

        return inventory;
    }
}
