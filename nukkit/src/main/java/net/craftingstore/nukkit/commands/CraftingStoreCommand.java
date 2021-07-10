package net.craftingstore.nukkit.commands;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandExecutor;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import net.craftingstore.nukkit.CraftingStoreNukkit;

import java.util.concurrent.ExecutionException;

public class CraftingStoreCommand implements CommandExecutor {
    private CraftingStoreNukkit instance;

    public CraftingStoreCommand(CraftingStoreNukkit instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(instance.getCraftingStore().ADMIN_PERMISSION)) {
            sender.sendMessage(instance.getPrefix() + "You don't have the required permission!");
            return false;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            instance.getCraftingStore().reload();
            sender.sendMessage(instance.getPrefix() + "The plugin is reloading!");
            return true;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("key")) {
            instance.getConfigFile().set("api-key", args[1]);
            instance.getConfigWrapper().saveConfig();
            instance.getServer().getScheduler().scheduleTask(instance, () -> {
                try {
                    if (instance.getCraftingStore().reload().get()) {
                        sender.sendMessage(instance.getPrefix() + "The new API key has been set in the config, and the plugin has been reloaded.");
                    } else {
                        sender.sendMessage(instance.getPrefix() + "The API key is invalid. The plugin will not work until you set a valid API key.");
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }, true);
            return true;
        }

        sender.sendMessage(TextFormat.GRAY + "" + TextFormat.STRIKETHROUGH + "-----------------------");
        sender.sendMessage(TextFormat.DARK_GRAY + ">" + TextFormat.GRAY + " /" + label + " reload" + TextFormat.DARK_GRAY + " -> " + TextFormat.GRAY + "Reload the config.");
        sender.sendMessage(TextFormat.DARK_GRAY + ">" + TextFormat.GRAY + " /" + label + " key <your key>" + TextFormat.DARK_GRAY + " -> " + TextFormat.GRAY + "Update the key.");
        sender.sendMessage(TextFormat.GRAY + "" + TextFormat.STRIKETHROUGH + "-----------------------");
        return true;
    }
}
