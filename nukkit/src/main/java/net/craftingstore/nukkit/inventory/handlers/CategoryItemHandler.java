package net.craftingstore.nukkit.inventory.handlers;

import cn.nukkit.Player;
import cn.nukkit.inventory.Inventory;
import net.craftingstore.core.models.api.inventory.CraftingStoreInventory;
import net.craftingstore.core.models.api.inventory.types.InventoryItemCategory;
import net.craftingstore.nukkit.inventory.CraftingStoreInventoryHolder;
import net.craftingstore.nukkit.inventory.InventoryBuilder;
import net.craftingstore.nukkit.inventory.InventoryItemHandler;

public class CategoryItemHandler implements InventoryItemHandler<InventoryItemCategory> {
    private InventoryBuilder builder;

    public CategoryItemHandler(InventoryBuilder builder) {
        this.builder = builder;
    }

    @Override
    public void handle(Player p, InventoryItemCategory category, CraftingStoreInventoryHolder holder) {
        CraftingStoreInventory csInventory = new CraftingStoreInventory(category.getTitle(), category.getContent(), category.getSize());
        Inventory inventory = builder.buildInventory(csInventory, holder);
        p.addWindow(inventory);
    }
}
