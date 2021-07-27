package net.craftingstore.bukkit.listeners;

import net.craftingstore.bukkit.CraftingStoreBukkit;
import net.craftingstore.bukkit.inventory.BuyInventoryBuilder;
import net.craftingstore.bukkit.inventory.CraftingStoreInventoryHolder;
import net.craftingstore.bukkit.inventory.InventoryBuilder;
import net.craftingstore.bukkit.inventory.InventoryItemHandler;
import net.craftingstore.bukkit.inventory.handlers.*;
import net.craftingstore.core.models.api.inventory.*;
import net.craftingstore.core.models.api.inventory.types.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;

public class InventoryListener implements Listener {

    private final HashMap<Class<? extends InventoryItem>, InventoryItemHandler> handlers = new HashMap<>();

    private final CraftingStoreBukkit instance;
    private final InventoryBuilder inventoryBuilder;

    public InventoryListener(CraftingStoreBukkit instance) {
        this.instance = instance;
        this.inventoryBuilder = new InventoryBuilder(instance);
        BuyInventoryBuilder buyInventoryBuilder = new BuyInventoryBuilder(instance, this.inventoryBuilder);
        this.handlers.put(InventoryItemBackButton.class, new BackButtonHandler(this.inventoryBuilder));
        this.handlers.put(InventoryItemCategory.class, new CategoryItemHandler(this.inventoryBuilder));
        this.handlers.put(InventoryItemCloseButton.class, new CloseButtonHandler());
        this.handlers.put(InventoryItemMessage.class, new MessageButtonHandler());
        this.handlers.put(InventoryItemBuyablePackage.class, new BuyablePackageHandler(
                instance,
                buyInventoryBuilder
        ));
        this.handlers.put(InventoryItemBuyButton.class, new BuyButtonHandler(instance, buyInventoryBuilder));
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        // Fix "Negative, non outside slot -1" error.
        if (e.getRawSlot() < 0) {
            return;
        }
        if (e.getInventory() == null
                || e.getInventory().getHolder() == null
                || !(e.getInventory().getHolder() instanceof CraftingStoreInventoryHolder)) {
            return;
        }
        e.setCancelled(true);
        Player p = (Player) e.getWhoClicked();
        CraftingStoreInventoryHolder holder = (CraftingStoreInventoryHolder) e.getInventory().getHolder();
        InventoryItem item = holder.getCsInventory().getByIndex(e.getRawSlot());
        if (item == null) {
            return;
        }

        if (handlers.containsKey(item.getClass())) {
            handlers.get(item.getClass()).handle(p, item, holder);
        }
    }
}
