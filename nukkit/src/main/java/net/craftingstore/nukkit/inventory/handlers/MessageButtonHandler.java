package net.craftingstore.nukkit.inventory.handlers;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.utils.TextFormat;
import net.craftingstore.core.models.api.inventory.types.InventoryItemMessage;
import net.craftingstore.nukkit.inventory.CraftingStoreInventoryHolder;
import net.craftingstore.nukkit.inventory.InventoryItemHandler;

public class MessageButtonHandler implements InventoryItemHandler<InventoryItemMessage> {
    @Override
    public void handle(Player p, InventoryItemMessage item, CraftingStoreInventoryHolder holder) {
        for (String message : item.getMessages()) {
            p.sendMessage(TextFormat.colorize('&', message));
        }
        if (item.shouldClose()) {
            Server.getInstance().getScheduler().scheduleDelayedTask(p::removeAllWindows, 2);
        }
    }
}
