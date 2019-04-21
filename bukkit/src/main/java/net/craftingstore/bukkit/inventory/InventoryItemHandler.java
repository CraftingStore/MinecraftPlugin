package net.craftingstore.bukkit.inventory;

import net.craftingstore.core.models.api.inventory.InventoryItem;
import org.bukkit.entity.Player;

public interface InventoryItemHandler<T extends InventoryItem> {
    void handle(Player p, T item, CraftingStoreInventoryHolder holder);
}
