package net.craftingstore.nukkit.inventory.handlers;

import cn.nukkit.Player;
import net.craftingstore.core.models.api.inventory.types.InventoryItemCloseButton;
import net.craftingstore.nukkit.inventory.CraftingStoreInventoryHolder;
import net.craftingstore.nukkit.inventory.InventoryItemHandler;

public class CloseButtonHandler implements InventoryItemHandler<InventoryItemCloseButton> {
    @Override
    public void handle(Player p, InventoryItemCloseButton item, CraftingStoreInventoryHolder holder) {
        p.removeAllWindows(); // TODO: Check if this is the right way to close an inventory window
    }
}
