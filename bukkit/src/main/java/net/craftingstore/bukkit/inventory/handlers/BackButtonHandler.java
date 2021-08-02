package net.craftingstore.bukkit.inventory.handlers;

import net.craftingstore.bukkit.inventory.CraftingStoreInventoryHolder;
import net.craftingstore.bukkit.inventory.InventoryBuilder;
import net.craftingstore.bukkit.inventory.InventoryItemHandler;
import net.craftingstore.core.models.api.inventory.types.InventoryItemBackButton;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class BackButtonHandler implements InventoryItemHandler<InventoryItemBackButton> {

    private InventoryBuilder builder;

    public BackButtonHandler(InventoryBuilder builder) {
        this.builder = builder;
    }
    
    @Override
    public void handle(Player p, InventoryItemBackButton item, CraftingStoreInventoryHolder holder) {
        if (holder.getParentInventory() != null) {
            CraftingStoreInventoryHolder parentInventory = holder.getParentInventory();
            Inventory inventory = builder.buildInventory(
                    parentInventory.getCsInventory(),
                    new CraftingStoreInventoryHolder(parentInventory.getCsInventory(), parentInventory.getParentInventory())
            );
            p.openInventory(inventory);
        }
    }
}
