package net.craftingstore.nukkit.listeners;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.inventory.InventoryClickEvent;
import net.craftingstore.core.models.api.inventory.InventoryItem;
import net.craftingstore.core.models.api.inventory.types.InventoryItemBackButton;
import net.craftingstore.core.models.api.inventory.types.InventoryItemCategory;
import net.craftingstore.core.models.api.inventory.types.InventoryItemCloseButton;
import net.craftingstore.core.models.api.inventory.types.InventoryItemMessage;
import net.craftingstore.nukkit.CraftingStoreNukkit;
import net.craftingstore.nukkit.inventory.CraftingStoreInventoryHolder;
import net.craftingstore.nukkit.inventory.InventoryBuilder;
import net.craftingstore.nukkit.inventory.InventoryItemHandler;
import net.craftingstore.nukkit.inventory.handlers.BackButtonHandler;
import net.craftingstore.nukkit.inventory.handlers.CategoryItemHandler;
import net.craftingstore.nukkit.inventory.handlers.CloseButtonHandler;
import net.craftingstore.nukkit.inventory.handlers.MessageButtonHandler;

import java.util.HashMap;

public class InventoryListener implements Listener {
    private HashMap<Class<? extends InventoryItem>, InventoryItemHandler> handlers = new HashMap<>();
    private CraftingStoreNukkit instance;
    private InventoryBuilder inventoryBuilder;

    public InventoryListener(CraftingStoreNukkit instance) {
        this.instance = instance;
        this.inventoryBuilder = new InventoryBuilder(instance);
        this.handlers.put(InventoryItemBackButton.class, new BackButtonHandler(this.inventoryBuilder));
        this.handlers.put(InventoryItemCategory.class, new CategoryItemHandler(this.inventoryBuilder));
        this.handlers.put(InventoryItemCloseButton.class, new CloseButtonHandler());
        this.handlers.put(InventoryItemMessage.class, new MessageButtonHandler());
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        // Fix "Negative, non outside slot -1" error.
        if (e.getSlot() < 0) {
            return;
        }
        if (e.getInventory() == null || e.getInventory().getHolder() == null || !(e.getInventory().getHolder() instanceof CraftingStoreInventoryHolder)) {
            return;
        }
        e.setCancelled(true);
        Player p = e.getPlayer();
        CraftingStoreInventoryHolder holder = (CraftingStoreInventoryHolder) e.getInventory().getHolder();
        InventoryItem item = holder.getCSInventory().getByIndex(e.getSlot());
        if (item == null) {
            return;
        }

        if (handlers.containsKey(item.getClass())) {
            handlers.get(item.getClass()).handle(p, item, holder);
        }
    }
}
