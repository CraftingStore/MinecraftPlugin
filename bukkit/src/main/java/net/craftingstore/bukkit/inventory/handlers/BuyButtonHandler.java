package net.craftingstore.bukkit.inventory.handlers;

import net.craftingstore.bukkit.CraftingStoreBukkit;
import net.craftingstore.bukkit.inventory.BuyInventoryBuilder;
import net.craftingstore.bukkit.inventory.BuyMenuInventoryHolder;
import net.craftingstore.bukkit.inventory.CraftingStoreInventoryHolder;
import net.craftingstore.bukkit.inventory.InventoryItemHandler;
import net.craftingstore.bukkit.util.ChatColorUtil;
import net.craftingstore.core.models.api.ApiPackageInformation;
import net.craftingstore.core.models.api.inventory.types.InventoryItemBuyButton;
import net.craftingstore.core.models.api.inventory.types.InventoryItemBuyablePackage;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class BuyButtonHandler implements InventoryItemHandler<InventoryItemBuyButton> {

    private final CraftingStoreBukkit instance;
    private final BuyInventoryBuilder buyInventoryBuilder;

    public BuyButtonHandler(CraftingStoreBukkit instance, BuyInventoryBuilder buyInventoryBuilder) {
        this.instance = instance;
        this.buyInventoryBuilder = buyInventoryBuilder;
    }

    @Override
    public void handle(Player p, InventoryItemBuyButton item, CraftingStoreInventoryHolder holder) {
        if (!(holder instanceof BuyMenuInventoryHolder)) {
            throw new RuntimeException("BuyButtonHandler only works in a buy menu");
        }
        InventoryItemBuyablePackage itemBuyablePackage = ((BuyMenuInventoryHolder) holder).getItemBuyablePackage();

        InventoryItemBuyButton.InventoryItemBuyButtonAction action = item.getAction();
        if (action.equals(InventoryItemBuyButton.InventoryItemBuyButtonAction.ONLINE)) {
            for (String message : itemBuyablePackage.getMessages()) {
                p.sendMessage(ChatColorUtil.translate(message));
            }
            p.closeInventory();
            return;
        }

        if (action.equals(InventoryItemBuyButton.InventoryItemBuyButtonAction.IN_GAME)) {
            Inventory loading = this.buyInventoryBuilder.buildLoadInventory();
            p.openInventory(loading);
            this.instance.getCraftingStore().getImplementation().runAsyncTask(() -> {
                try {
                    String ip = p.getAddress().getAddress().getHostAddress();
                    ApiPackageInformation packageInformation = this.instance.getCraftingStore().getApi().getPackageInformation(
                            p.getName(),
                            p.getUniqueId(),
                            ip,
                            itemBuyablePackage.getPackageId()
                    ).get();
                    if (!packageInformation.isAllowedToBuy()) {
                        p.sendMessage(ChatColorUtil.translate(packageInformation.getMessage()));
                        for (String message : itemBuyablePackage.getMessages()) {
                            p.sendMessage(ChatColorUtil.translate(message));
                        }
                        closeInventory(p);
                        return;
                    }
                    if (packageInformation.getPrice() != itemBuyablePackage.getPrice()) {
                        p.sendMessage(this.instance.getPrefix() + "There was a problem with the payment data. Please try again later.");
                        closeInventory(p);
                        return;
                    }
                    if (!this.instance.getVaultHook().getEconomy().has(p, packageInformation.getPrice())) {
                        p.sendMessage(ChatColorUtil.translate(
                                this.instance.getCraftingStore().getImplementation().getConfiguration().getNotEnoughBalanceMessage()
                        ));
                        closeInventory(p);
                        return;
                    }
                    EconomyResponse response = this.instance.getVaultHook().getEconomy().withdrawPlayer(
                            p,
                            packageInformation.getPrice()
                    );
                    if (!response.transactionSuccess()) {
                        p.sendMessage(this.instance.getPrefix() + "There was a problem with the transaction. Please try again later");
                        closeInventory(p);
                        return;
                    }

                    try {
                        boolean success = this.instance.getCraftingStore().getApi().createPayment(
                                p.getName(),
                                packageInformation.getPrice(),
                                new int[]{itemBuyablePackage.getPackageId()}
                        ).get();
                        if (success) {
                            p.sendMessage(ChatColorUtil.translate(itemBuyablePackage.getSuccessMessage()));
                            closeInventory(p);
                            return;
                        }
                        throw new RuntimeException("Failed to create a transaction at CraftingStore. Giving money back.");
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        this.instance.getCraftingStore().getLogger().error(String.format(
                                "Unable to create a transaction for player %s with package %s and cost %s. Giving the player's money back...",
                                p.getName(),
                                itemBuyablePackage.getPackageId(),
                                packageInformation.getPrice()
                        ));
                        EconomyResponse rollbackResponse = this.instance.getVaultHook().getEconomy().depositPlayer(
                                p,
                                packageInformation.getPrice()
                        );
                        if (!rollbackResponse.transactionSuccess()) {
                            p.sendMessage(this.instance.getPrefix() + "There was a problem with the transaction, and we were unable to give you back your money. Please contact an administrator");
                            this.instance.getCraftingStore().getLogger().error(String.format(
                                    "Unable to give the player's money back. Player: %s, cost: %s.",
                                    p.getName(),
                                    packageInformation.getPrice()
                            ));
                            closeInventory(p);
                            return;
                        }

                        p.sendMessage(this.instance.getPrefix() + "A problem occurred while creating your payment. Your money has been given back. Please try again later");
                        closeInventory(p);
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                    closeInventory(p);
                    p.sendMessage(this.instance.getPrefix() + "A problem occurred. Please try again later.");
                }
            });
        }
    }

    private void closeInventory(Player p) {
        this.instance.runSyncTask(() -> {
            if (p.isOnline()) {
                p.closeInventory();
            }
        });
    }
}
