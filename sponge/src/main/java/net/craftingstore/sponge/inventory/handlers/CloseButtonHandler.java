package net.craftingstore.sponge.inventory.handlers;

import net.craftingstore.core.models.api.inventory.types.InventoryItemCloseButton;
import net.craftingstore.sponge.inventory.InventoryAttachment;
import net.craftingstore.sponge.inventory.InventoryItemHandler;
import org.spongepowered.api.entity.living.player.Player;

public class CloseButtonHandler implements InventoryItemHandler<InventoryItemCloseButton> {

    @Override
    public void handle(Player p, InventoryItemCloseButton item, InventoryAttachment attachment) {
        p.closeInventory();
    }
}
