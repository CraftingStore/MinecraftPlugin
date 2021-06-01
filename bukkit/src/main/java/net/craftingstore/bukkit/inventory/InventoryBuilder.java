package net.craftingstore.bukkit.inventory;

import net.craftingstore.bukkit.CraftingStoreBukkit;
import net.craftingstore.bukkit.util.ChatColorUtil;
import net.craftingstore.bukkit.util.VersionUtil;
import net.craftingstore.core.models.api.inventory.CraftingStoreInventory;
import net.craftingstore.core.models.api.inventory.InventoryItem;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.stream.Collectors;

public class InventoryBuilder {

    private final CraftingStoreBukkit instance;
    private final InventoryItemBuilder inventoryItemBuilder;

    public InventoryBuilder(CraftingStoreBukkit instance) {
        this.instance = instance;
        this.inventoryItemBuilder = new InventoryItemBuilder(instance);
    }

    public Inventory buildInventory(CraftingStoreInventory csInventory) {
        return buildInventory(csInventory, null);
    }

    public Inventory buildInventory(CraftingStoreInventory csInventory, CraftingStoreInventoryHolder parent) {
        String title = csInventory.getTitle();
        if (title == null || title.isEmpty()) {
            title = "CraftingStore";
        }
        title = ChatColorUtil.translate(title);
        Inventory inventory = Bukkit.createInventory(new CraftingStoreInventoryHolder(csInventory, parent), csInventory.getSize(), title);

        for (InventoryItem inventoryItem : csInventory.getContent()) {
            ItemStack itemStack = this.inventoryItemBuilder.getItemStack(inventoryItem.getIcon().getMaterial(), inventoryItem.getIcon().getAmount());
            ItemMeta meta = itemStack.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(ChatColorUtil.translate(inventoryItem.getName()));
                if (inventoryItem.getDescription() != null && inventoryItem.getDescription().length != 0) {
                    meta.setLore(Arrays.stream(inventoryItem.getDescription())
                            .map(ChatColorUtil::translate)
                            .collect(Collectors.toList())
                    );
                }
                if (VersionUtil.isCustomModalDataAvailable() && inventoryItem.getIcon().getCustomModelData() != null) {
                    meta.setCustomModelData(inventoryItem.getIcon().getCustomModelData());
                }
                itemStack.setItemMeta(meta);
            }
            inventory.setItem(inventoryItem.getIndex(), itemStack);
        }

        return inventory;
    }
}
