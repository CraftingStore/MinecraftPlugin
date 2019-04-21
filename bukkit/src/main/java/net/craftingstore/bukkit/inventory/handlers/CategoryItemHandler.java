package net.craftingstore.bukkit.inventory.handlers;

import net.craftingstore.bukkit.inventory.CraftingStoreInventoryHolder;
import net.craftingstore.bukkit.inventory.InventoryBuilder;
import net.craftingstore.bukkit.inventory.InventoryItemHandler;
import net.craftingstore.core.models.api.inventory.CraftingStoreInventory;
import net.craftingstore.core.models.api.inventory.types.InventoryItemCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class CategoryItemHandler implements InventoryItemHandler<InventoryItemCategory> {

    private InventoryBuilder builder;

    public CategoryItemHandler(InventoryBuilder builder) {
        this.builder = builder;
    }

    @Override
    public void handle(Player p, InventoryItemCategory category, CraftingStoreInventoryHolder holder) {
        CraftingStoreInventory csInventory = new CraftingStoreInventory(category.getTitle(), category.getContent(), category.getSize());
        Inventory inventory = builder.buildInventory(csInventory, holder);
        p.openInventory(inventory);
    }
}
