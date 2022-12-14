package net.craftingstore.bukkit.inventory.handlers;

import net.craftingstore.bukkit.CraftingStoreBukkit;
import net.craftingstore.bukkit.inventory.*;
import net.craftingstore.bukkit.util.ChatColorUtil;
import net.craftingstore.core.models.api.inventory.types.InventoryItemBuyablePackage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class BuyablePackageHandler implements InventoryItemHandler<InventoryItemBuyablePackage> {

    private final CraftingStoreBukkit instance;
    private final BuyInventoryBuilder buyInventoryBuilder;

    public BuyablePackageHandler(CraftingStoreBukkit instance, BuyInventoryBuilder buyInventoryBuilder) {
        this.instance = instance;
        this.buyInventoryBuilder = buyInventoryBuilder;
    }

    @Override
    public void handle(Player p, InventoryItemBuyablePackage item, CraftingStoreInventoryHolder holder) {
        if (!instance.isHookedWithVault()) {
            p.sendMessage(instance.getPrefix() + "CraftingStore is not hooked with Vault!");
            p.closeInventory();
            return;
        }

        if (!instance.getVaultHook().getEconomy().has(p, item.getPrice())) {
            p.sendMessage(ChatColorUtil.translate(
                    instance.getCraftingStore().getImplementation().getConfiguration().getNotEnoughBalanceMessage()
            ));
            for (String message : item.getMessages()) {
                p.sendMessage(ChatColorUtil.translate(message));
            }
            p.closeInventory();
            return;
        }

        Inventory loading = buyInventoryBuilder.buildLoadInventory();
        p.openInventory(loading);
        Bukkit.getScheduler().runTaskAsynchronously(instance, () -> {
            try {
                Inventory inventory = buyInventoryBuilder.build(p, item, holder);
                if (inventory != null) {
                    Bukkit.getScheduler().runTask(instance, () -> {
                        if (p.isOnline()) {
                            p.openInventory(inventory);
                        }
                    });
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }
}
