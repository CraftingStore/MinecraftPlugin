package net.craftingstore.bukkit.inventory;

import net.craftingstore.bukkit.CraftingStoreBukkit;
import net.craftingstore.bukkit.util.ChatColorUtil;
import net.craftingstore.core.exceptions.CraftingStoreApiException;
import net.craftingstore.core.models.api.ApiInventory;
import net.craftingstore.core.models.api.ApiPackageInformation;
import net.craftingstore.core.models.api.inventory.CraftingStoreInventory;
import net.craftingstore.core.models.api.inventory.InventoryItem;
import net.craftingstore.core.models.api.inventory.InventoryItemIcon;
import net.craftingstore.core.models.api.inventory.InventoryItemType;
import net.craftingstore.core.models.api.inventory.types.InventoryItemBuyablePackage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class BuyInventoryBuilder {

    private final CraftingStoreBukkit instance;
    private final InventoryBuilder inventoryBuilder;

    public BuyInventoryBuilder(CraftingStoreBukkit instance, InventoryBuilder inventoryBuilder) {
        this.instance = instance;
        this.inventoryBuilder = inventoryBuilder;
    }

    public Inventory build(Player p, InventoryItemBuyablePackage item, CraftingStoreInventoryHolder holder) throws CraftingStoreApiException, ExecutionException, InterruptedException {
        ApiInventory buyMenu = this.instance.getCraftingStore().getApi().getGUI().get().getBuyMenu();
        CraftingStoreInventory craftingStoreInventory = new CraftingStoreInventory(buyMenu.getTitle(), buyMenu.getContent(), buyMenu.getSize());

        String ip = p.getAddress().getAddress().getHostAddress();
        ApiPackageInformation packageInformation = this.instance.getCraftingStore().getApi().getPackageInformation(
                p.getName(),
                p.getUniqueId(),
                ip,
                item.getPackageId()
        ).get();
        if (!packageInformation.isAllowedToBuy()) {
            p.sendMessage(ChatColorUtil.translate(packageInformation.getMessage()));
            this.closeInventory(p);
            return null;
        }
        if (packageInformation.getPrice() != item.getPrice()) {
            p.sendMessage(this.instance.getPrefix() + "There was a problem with the payment data. Please try again later.");
            this.closeInventory(p);
            return null;
        }

        Map placeholders = new HashMap<>();
        placeholders.put("package_name", item.getName());
        placeholders.put("package_price_ingame", packageInformation.getPrice());
        return this.inventoryBuilder.buildInventory(
                craftingStoreInventory,
                new BuyMenuInventoryHolder(craftingStoreInventory, holder, item),
                placeholders
        );
    }

    public Inventory buildLoadInventory() {
        InventoryItemIcon icon = new InventoryItemIcon("LIGHT_BLUE_STAINED_GLASS_PANE", 1, 0, null);
        InventoryItem[] content = new InventoryItem[9];
        for (int i = 0; i < 9; i++) {
            content[i] = new InventoryItem("Loading...", null, InventoryItemType.NULL, icon, i);
        }

        CraftingStoreInventory craftingStoreInventory = new CraftingStoreInventory(
                "Loading...",
                content,
                9
        );
        return this.inventoryBuilder.buildInventory(
                craftingStoreInventory,
                new CraftingStoreInventoryHolder(craftingStoreInventory, null)
        );
    }

    private void closeInventory(Player p) {
        this.instance.runSyncTask(() -> {
            if (p.isOnline()) {
                p.closeInventory();
            }
        });
    }
}
