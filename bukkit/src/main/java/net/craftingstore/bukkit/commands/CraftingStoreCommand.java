package net.craftingstore.bukkit.commands;

import net.craftingstore.bukkit.CraftingStoreBukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CraftingStoreCommand implements CommandExecutor {

    private CraftingStoreBukkit instance;

    public CraftingStoreCommand(CraftingStoreBukkit instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("craftingstore.admin")) {
            sender.sendMessage(instance.getPrefix() + "You don't have the required permission!");
            return false;
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            instance.getCraftingStore().reload();
            sender.sendMessage(instance.getPrefix() + "The plugin has been reloaded!");
            return true;
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("key")) {
            instance.getConfig().set("api-key", args[1]);
            instance.getConfigWrapper().saveConfig();

            if (instance.getCraftingStore().reload()) {
                sender.sendMessage(instance.getPrefix() + "The new API key has been set in the config, and the plugin has been reloaded.");
            } else {
                sender.sendMessage(instance.getPrefix() + "The API key is invalid. The plugin will not work until you set a valid API key.");
            }
            return true;
        }

        sender.sendMessage(ChatColor.GRAY + "" +ChatColor.STRIKETHROUGH + "-----------------------");
        sender.sendMessage(ChatColor.DARK_GRAY + ">" + ChatColor.GRAY + " /" + label + " reload" + ChatColor.DARK_GRAY + " -> " + ChatColor.GRAY + "Reload the config.");
        sender.sendMessage(ChatColor.DARK_GRAY + ">" + ChatColor.GRAY + " /" + label + " key <your key>" + ChatColor.DARK_GRAY + " -> " + ChatColor.GRAY + "Update the key.");
        sender.sendMessage(ChatColor.GRAY + "" +ChatColor.STRIKETHROUGH + "-----------------------");
        return true;
    }
}
