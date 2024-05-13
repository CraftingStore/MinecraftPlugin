package net.craftingstore.bukkit.inventory.handlers;

import net.craftingstore.bukkit.CraftingStoreBukkit;
import net.craftingstore.bukkit.inventory.*;
import net.craftingstore.bukkit.util.ChatColorUtil;
import net.craftingstore.core.models.api.inventory.types.InventoryItemBuyablePackage;
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
        if (!this.instance.isHookedWithVault()) {
            p.sendMessage(instance.getPrefix() + "CraftingStore is not hooked with Vault!");
            p.closeInventory();
            return;
        }

        if (!this.instance.getVaultHook().getEconomy().has(p, item.getPrice())) {
            p.sendMessage(ChatColorUtil.translate(
                    this.instance.getCraftingStore().getImplementation().getConfiguration().getNotEnoughBalanceMessage()
            ));
            for (String message : item.getMessages()) {
                p.sendMessage(ChatColorUtil.translate(message));
            }
            p.closeInventory();
            return;
        }

        Inventory loading = this.buyInventoryBuilder.buildLoadInventory();
        p.openInventory(loading);
        this.instance.getCraftingStore().getImplementation().runAsyncTask(() -> {
            try {
                Inventory inventory = this.buyInventoryBuilder.build(p, item, holder);
                if (inventory != null) {
                    this.instance.runSyncTask(() -> {
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
