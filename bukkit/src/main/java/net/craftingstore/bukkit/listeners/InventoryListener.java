package net.craftingstore.bukkit.listeners;

import net.craftingstore.bukkit.CraftingStoreBukkit;
import net.craftingstore.bukkit.inventory.CraftingStoreInventoryHolder;
import net.craftingstore.bukkit.inventory.InventoryBuilder;
import net.craftingstore.core.models.api.inventory.*;
import net.craftingstore.core.models.api.inventory.types.InventoryItemBackButton;
import net.craftingstore.core.models.api.inventory.types.InventoryItemCategory;
import net.craftingstore.core.models.api.inventory.types.InventoryItemCloseButton;
import net.craftingstore.core.models.api.inventory.types.InventoryItemMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class InventoryListener implements Listener {

    private CraftingStoreBukkit instance;
    private InventoryBuilder inventoryBuilder = new InventoryBuilder();

    public InventoryListener(CraftingStoreBukkit instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        // Fix "Negative, non outside slot -1" error.
        if (e.getRawSlot() < 0) {
            return;
        }
        if (e.getInventory() == null
                || e.getInventory().getHolder() == null
                || !(e.getInventory().getHolder() instanceof CraftingStoreInventoryHolder)) {
            return;
        }
        e.setCancelled(true);
        Player p = (Player) e.getWhoClicked();
        CraftingStoreInventoryHolder holder = (CraftingStoreInventoryHolder) e.getInventory().getHolder();
        InventoryItem item = holder.getCsInventory().getByIndex(e.getRawSlot());
        if (item == null) {
            return;
        }
        if (item instanceof InventoryItemMessage) {
            handleMessageItem(p, (InventoryItemMessage) item);
        } else if (item instanceof InventoryItemCategory) {
            handleCategoryItem(p, (InventoryItemCategory) item, holder);
        } else if (item instanceof InventoryItemBackButton) {
            handleBackButton(p, holder);
        } else if (item instanceof InventoryItemCloseButton) {
            handleCloseButton(p);
        }
    }

    private void handleCloseButton(Player p) {
        p.closeInventory();
    }

    private void handleBackButton(Player p, CraftingStoreInventoryHolder holder) {
        if (holder.getParentInventory() != null) {
            Inventory inventory = inventoryBuilder.buildInventory(holder.getParentInventory().getCsInventory(), holder.getParentInventory().getParentInventory());
            p.openInventory(inventory);
        }
    }

    private void handleCategoryItem(Player p, InventoryItemCategory category, CraftingStoreInventoryHolder parent) {
        CraftingStoreInventory csInventory = new CraftingStoreInventory(category.getTitle(), category.getContent(), category.getSize());
        Inventory inventory = inventoryBuilder.buildInventory(csInventory, parent);
        p.openInventory(inventory);
    }

    private void handleMessageItem(Player p, InventoryItemMessage itemMessage) {
        for (String message : itemMessage.getMessages()) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
        if (itemMessage.shouldClose()) {
            p.closeInventory();
        }
    }
}
