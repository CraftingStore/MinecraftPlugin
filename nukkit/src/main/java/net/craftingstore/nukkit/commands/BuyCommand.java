package net.craftingstore.nukkit.commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandExecutor;
import cn.nukkit.command.CommandSender;
import cn.nukkit.inventory.Inventory;
import net.craftingstore.core.exceptions.CraftingStoreApiException;
import net.craftingstore.core.models.api.inventory.CraftingStoreInventory;
import net.craftingstore.nukkit.CraftingStoreNukkit;
import net.craftingstore.nukkit.inventory.InventoryBuilder;

import java.util.concurrent.ExecutionException;

public class BuyCommand implements CommandExecutor {
    private CraftingStoreNukkit instance;

    public BuyCommand(CraftingStoreNukkit instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        if (!instance.getCraftingStore().isEnabled()) {
            sender.sendMessage(instance.getPrefix() + "The plugin has not been set-up correctly. Please contact an administrator.");
            return false;
        }

        Player p = (Player) sender;
        Server.getInstance().getScheduler().scheduleTask(instance, () -> {
            try {
                InventoryBuilder builder = new InventoryBuilder(this.instance);
                CraftingStoreInventory gui = instance.getCraftingStore().getApi().getGUI().get();
                Inventory inventory = builder.buildInventory(gui);
                Server.getInstance().getScheduler().scheduleTask(instance, () -> {
                    if (p.isOnline()) {
                        p.addWindow(inventory);
                    }
                });
            } catch (CraftingStoreApiException | InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }, true);

        return true;
    }
}