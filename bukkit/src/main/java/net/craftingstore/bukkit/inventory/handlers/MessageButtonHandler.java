package net.craftingstore.bukkit.inventory.handlers;

import net.craftingstore.bukkit.inventory.CraftingStoreInventoryHolder;
import net.craftingstore.bukkit.inventory.InventoryItemHandler;
import net.craftingstore.bukkit.util.ChatColorUtil;
import net.craftingstore.core.models.api.inventory.types.InventoryItemMessage;
import org.bukkit.entity.Player;

public class MessageButtonHandler implements InventoryItemHandler<InventoryItemMessage> {
    @Override
    public void handle(Player p, InventoryItemMessage item, CraftingStoreInventoryHolder holder) {
        for (String message : item.getMessages()) {
            p.sendMessage(ChatColorUtil.translate(message));
        }
        if (item.shouldClose()) {
            p.closeInventory();
        }
    }
}
