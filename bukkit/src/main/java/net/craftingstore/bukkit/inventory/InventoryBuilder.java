package net.craftingstore.bukkit.inventory;

import net.craftingstore.bukkit.CraftingStoreBukkit;
import net.craftingstore.bukkit.util.ChatColorUtil;
import net.craftingstore.bukkit.util.VersionUtil;
import net.craftingstore.core.models.api.inventory.CraftingStoreInventory;
import net.craftingstore.core.models.api.inventory.InventoryItem;
import org.apache.commons.text.StringSubstitutor;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class InventoryBuilder {

    private final InventoryItemBuilder inventoryItemBuilder;

    public InventoryBuilder(CraftingStoreBukkit instance) {
        inventoryItemBuilder = new InventoryItemBuilder(instance);
    }

    public Inventory buildInventory(CraftingStoreInventory csInventory) {
        return buildInventory(csInventory, new CraftingStoreInventoryHolder(csInventory, null));
    }

    public Inventory buildInventory(CraftingStoreInventory csInventory, CraftingStoreInventoryHolder holder) {
        return buildInventory(csInventory, holder, new HashMap<>());
    }

    public Inventory buildInventory(CraftingStoreInventory csInventory, CraftingStoreInventoryHolder holder, Map<String, ?> placeholders) {
        String title = csInventory.getTitle();
        if (title == null || title.isEmpty()) {
            title = "CraftingStore";
        }

        StringSubstitutor stringSubstitutor = new StringSubstitutor(placeholders, "{", "}");
        title = ChatColorUtil.translate(stringSubstitutor.replace(title));
        Inventory inventory = Bukkit.createInventory(holder, csInventory.getSize(), title);

        for (InventoryItem inventoryItem : csInventory.getContent()) {
            ItemStack itemStack = inventoryItemBuilder.getItemStack(inventoryItem.getIcon().getMaterial(), inventoryItem.getIcon().getAmount());
            ItemMeta meta = itemStack.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(ChatColorUtil.translate(stringSubstitutor.replace(inventoryItem.getName())));
                if (inventoryItem.getDescription() != null && inventoryItem.getDescription().length != 0) {
                    meta.setLore(Arrays.stream(inventoryItem.getDescription())
                            .map(stringSubstitutor::replace)
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
