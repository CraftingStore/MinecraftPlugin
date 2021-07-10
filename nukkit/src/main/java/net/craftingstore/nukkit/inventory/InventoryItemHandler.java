package net.craftingstore.nukkit.inventory;

import cn.nukkit.Player;
import net.craftingstore.core.models.api.inventory.InventoryItem;

public interface InventoryItemHandler<T extends InventoryItem> {
    void handle(Player p, T item, CraftingStoreInventoryHolder holder);
}
