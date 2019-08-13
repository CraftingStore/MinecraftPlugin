package net.craftingstore.nukkit.inventory.handlers;

import cn.nukkit.Player;
import cn.nukkit.inventory.Inventory;
import net.craftingstore.core.models.api.inventory.types.InventoryItemBackButton;
import net.craftingstore.nukkit.inventory.CraftingStoreInventoryHolder;
import net.craftingstore.nukkit.inventory.InventoryBuilder;
import net.craftingstore.nukkit.inventory.InventoryItemHandler;

public class BackButtonHandler implements InventoryItemHandler<InventoryItemBackButton> {
    private InventoryBuilder builder;

    public BackButtonHandler(InventoryBuilder builder) {
        this.builder = builder;
    }

    @Override
    public void handle(Player p, InventoryItemBackButton item, CraftingStoreInventoryHolder holder) {
        if (holder.getParentInventory() != null) {
            Inventory inventory = builder.buildInventory(holder.getParentInventory().getCSInventory(), holder.getParentInventory().getParentInventory());
            p.addWindow(inventory);
        }
    }
}
