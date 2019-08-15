package net.craftingstore.nukkit.inventory.handlers;

import cn.nukkit.Player;
import cn.nukkit.Server;
import net.craftingstore.core.models.api.inventory.types.InventoryItemCloseButton;
import net.craftingstore.nukkit.CraftingStoreNukkit;
import net.craftingstore.nukkit.inventory.CraftingStoreInventoryHolder;
import net.craftingstore.nukkit.inventory.InventoryItemHandler;

public class CloseButtonHandler implements InventoryItemHandler<InventoryItemCloseButton> {
    @Override
    public void handle(Player p, InventoryItemCloseButton item, CraftingStoreInventoryHolder holder) {
        Server.getInstance().getScheduler().scheduleDelayedTask(CraftingStoreNukkit.getInstance(), p::removeAllWindows, 2);
    }
}
