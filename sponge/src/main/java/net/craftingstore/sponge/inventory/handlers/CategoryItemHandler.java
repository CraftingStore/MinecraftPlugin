package net.craftingstore.sponge.inventory.handlers;

import com.google.inject.Inject;
import net.craftingstore.core.models.api.inventory.CraftingStoreInventory;
import net.craftingstore.core.models.api.inventory.types.InventoryItemCategory;
import net.craftingstore.sponge.inventory.InventoryAttachment;
import net.craftingstore.sponge.inventory.InventoryBuilder;
import net.craftingstore.sponge.inventory.InventoryItemHandler;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.Inventory;

public class CategoryItemHandler implements InventoryItemHandler<InventoryItemCategory> {

    @Inject
    private InventoryBuilder inventoryBuilder;

    @Override
    public void handle(Player p, InventoryItemCategory item, InventoryAttachment attachment) {
        CraftingStoreInventory csInventory = new CraftingStoreInventory(item.getTitle(), item.getContent(), item.getSize());
        Inventory inventory = inventoryBuilder.buildInventory(csInventory, attachment);
        p.openInventory(inventory);
    }
}
