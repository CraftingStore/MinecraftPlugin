package net.craftingstore.sponge.inventory;

import net.craftingstore.core.models.api.inventory.InventoryItem;
import org.spongepowered.api.entity.living.player.Player;

public interface InventoryItemHandler<T extends InventoryItem> {
    void handle(Player p, T item, InventoryAttachment attachment);
}
