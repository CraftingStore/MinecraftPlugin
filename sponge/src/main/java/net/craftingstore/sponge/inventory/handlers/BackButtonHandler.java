package net.craftingstore.sponge.inventory.handlers;

import com.google.inject.Inject;
import net.craftingstore.core.models.api.inventory.types.InventoryItemBackButton;
import net.craftingstore.sponge.inventory.InventoryAttachment;
import net.craftingstore.sponge.inventory.InventoryBuilder;
import net.craftingstore.sponge.inventory.InventoryItemHandler;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.Inventory;

public class BackButtonHandler implements InventoryItemHandler<InventoryItemBackButton> {

    @Inject
    private InventoryBuilder inventoryBuilder;

    @Override
    public void handle(Player p, InventoryItemBackButton item, InventoryAttachment attachment) {
        if (attachment.getParent() != null) {
            Inventory inventory = inventoryBuilder.buildInventory(attachment.getParent().getCsInventory(), attachment.getParent().getParent());
            p.openInventory(inventory);
        }
    }
}
