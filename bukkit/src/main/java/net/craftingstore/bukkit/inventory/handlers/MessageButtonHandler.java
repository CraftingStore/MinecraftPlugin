package net.craftingstore.bukkit.inventory.handlers;

import net.craftingstore.bukkit.inventory.CraftingStoreInventoryHolder;
import net.craftingstore.bukkit.inventory.InventoryItemHandler;
import net.craftingstore.core.models.api.inventory.types.InventoryItemMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageButtonHandler implements InventoryItemHandler<InventoryItemMessage> {
    @Override
    public void handle(Player p, InventoryItemMessage item, CraftingStoreInventoryHolder holder) {
        for (String message : item.getMessages()) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
        if (item.shouldClose()) {
            p.closeInventory();
        }
    }
}
