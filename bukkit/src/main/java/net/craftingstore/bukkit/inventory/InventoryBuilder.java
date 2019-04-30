package net.craftingstore.bukkit.inventory;

import net.craftingstore.bukkit.CraftingStoreBukkit;
import net.craftingstore.bukkit.util.XMaterial;
import net.craftingstore.core.CraftingStore;
import net.craftingstore.core.models.api.inventory.CraftingStoreInventory;
import net.craftingstore.core.models.api.inventory.InventoryItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.stream.Collectors;

public class InventoryBuilder {

    private CraftingStoreBukkit instance;

    public InventoryBuilder(CraftingStoreBukkit instance) {
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
        title = ChatColor.translateAlternateColorCodes('&', title);
        Inventory inventory = Bukkit.createInventory(new CraftingStoreInventoryHolder(csInventory, parent), csInventory.getSize(), title);

        for (InventoryItem inventoryItem : csInventory.getContent()) {
            XMaterial xMaterial;
            if (inventoryItem.getIcon().getMaterial() == null) {
                xMaterial = XMaterial.CHEST;
            } else {
                xMaterial = XMaterial.fromString(inventoryItem.getIcon().getMaterial());
                if (xMaterial == null || xMaterial.parseMaterial() == null) {
                    instance.getCraftingStore().getLogger().debug("Material " + inventoryItem.getIcon().getMaterial() + " not found.");
                    xMaterial = XMaterial.CHEST;
                }
            }
            ItemStack itemStack = xMaterial.parseItem(inventoryItem.getIcon().getAmount());
            ItemMeta meta = itemStack.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', inventoryItem.getName()));
            if (inventoryItem.getDescription() != null && inventoryItem.getDescription().length != 0) {
                meta.setLore(Arrays.stream(inventoryItem.getDescription())
                        .map(d -> ChatColor.translateAlternateColorCodes('&', d))
                        .collect(Collectors.toList())
                );
            }
            itemStack.setItemMeta(meta);
            inventory.setItem(inventoryItem.getIndex(), itemStack);
        }

        return inventory;
    }
}
