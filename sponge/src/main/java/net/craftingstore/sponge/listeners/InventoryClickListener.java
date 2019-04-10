package net.craftingstore.sponge.listeners;

import com.google.inject.Inject;
import net.craftingstore.core.models.api.inventory.CraftingStoreInventory;
import net.craftingstore.core.models.api.inventory.InventoryItem;
import net.craftingstore.core.models.api.inventory.types.InventoryItemBackButton;
import net.craftingstore.core.models.api.inventory.types.InventoryItemCategory;
import net.craftingstore.core.models.api.inventory.types.InventoryItemCloseButton;
import net.craftingstore.core.models.api.inventory.types.InventoryItemMessage;
import net.craftingstore.sponge.CraftingStoreSponge;
import net.craftingstore.sponge.inventory.CraftingStoreInventoryProperty;
import net.craftingstore.sponge.inventory.InventoryAttachment;
import net.craftingstore.sponge.inventory.InventoryItemHandler;
import net.craftingstore.sponge.inventory.handlers.BackButtonHandler;
import net.craftingstore.sponge.inventory.handlers.CategoryItemHandler;
import net.craftingstore.sponge.inventory.handlers.CloseButtonHandler;
import net.craftingstore.sponge.inventory.handlers.MessageButtonHandler;
import org.spongepowered.api.Game;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.item.inventory.transaction.SlotTransaction;

import java.util.Collection;
import java.util.HashMap;

public class InventoryClickListener {

    private HashMap<Class<? extends InventoryItem>, InventoryItemHandler> handlers = new HashMap<>();

    @Inject
    private Game game;

    @Inject
    private CraftingStoreSponge spongePlugin;

    @Inject
    public InventoryClickListener(BackButtonHandler backButtonHandler, CategoryItemHandler categoryItemHandler,
                                  CloseButtonHandler closeButtonHandler, MessageButtonHandler messageButtonHandler) {
        handlers.put(InventoryItemBackButton.class, backButtonHandler);
        handlers.put(InventoryItemCategory.class, categoryItemHandler);
        handlers.put(InventoryItemCloseButton.class, closeButtonHandler);
        handlers.put(InventoryItemMessage.class, messageButtonHandler);
    }

    @Listener
    public void onClick(ClickInventoryEvent e, @First Player p) {
        Collection<CraftingStoreInventoryProperty> properties = e.getTargetInventory()
                .getProperties(CraftingStoreInventoryProperty.class);
        if (properties.size() == 0) {
            return;
        }
        e.setCancelled(true);
        if (e.getTransactions().size() == 0) {
            return;
        }
        SlotTransaction transaction = e.getTransactions().get(0);
        SlotIndex slotIndex = transaction.getSlot().getInventoryProperty(SlotIndex.class).orElse(null);
        if (slotIndex == null) {
            return;
        }
        int index = slotIndex.getValue();

        CraftingStoreInventoryProperty csProperty = properties.iterator().next();
        InventoryAttachment attachment = csProperty.getValue();
        CraftingStoreInventory csInventory = attachment.getCsInventory();
        InventoryItem item = csInventory.getByIndex(index);
        if (item == null) {
            return;
        }

        if (handlers.containsKey(item.getClass())) {
            game.getScheduler().createTaskBuilder()
                    .execute(() -> handlers.get(item.getClass()).handle(p, item, attachment))
                    .submit(spongePlugin);
        }
    }
}
