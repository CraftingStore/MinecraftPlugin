package net.craftingstore.bukkit.commands;

import net.craftingstore.bukkit.CraftingStoreBukkit;
import net.craftingstore.bukkit.inventory.InventoryBuilder;
import net.craftingstore.core.exceptions.CraftingStoreApiException;
import net.craftingstore.core.models.api.ApiInventory;
import net.craftingstore.core.models.api.inventory.CraftingStoreInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.concurrent.ExecutionException;

public class BuyCommand implements CommandExecutor {

    private CraftingStoreBukkit instance;

    public BuyCommand(CraftingStoreBukkit instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        if (!this.instance.getCraftingStore().isEnabled()) {
            sender.sendMessage(instance.getPrefix() + "The plugin has not been set-up correctly. Please contact an administrator.");
            return false;
        }
        Player p = (Player) sender;
        this.instance.getCraftingStore().getImplementation().runAsyncTask(() -> {
            try {
                InventoryBuilder builder = new InventoryBuilder(this.instance);
                ApiInventory gui = instance.getCraftingStore().getApi().getGUI().get();
                Inventory inventory = builder.buildInventory(
                        new CraftingStoreInventory(gui.getTitle(), gui.getContent(), gui.getSize())
                );
                this.instance.runSyncTask(() -> {
                    if (p.isOnline()) {
                        p.openInventory(inventory);
                    }
                });
            } catch (CraftingStoreApiException | InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
        return true;
    }
}
