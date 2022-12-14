package net.craftingstore.bungee.commands;

import net.craftingstore.bungee.CraftingStoreBungee;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import java.util.concurrent.ExecutionException;

public class CraftingStoreCommand extends Command {

    private final CraftingStoreBungee instance;

    public CraftingStoreCommand(CraftingStoreBungee instance) {
        super("csb", instance.getCraftingStore().ADMIN_PERMISSION);
        this.instance = instance;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission(instance.getCraftingStore().ADMIN_PERMISSION)) {
            sender.sendMessage(new TextComponent(instance.getPrefix() + "You don't have the required permission!"));
            return;
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            instance.getCraftingStore().reload();
            sender.sendMessage(new TextComponent(instance.getPrefix() + "The plugin is reloading!"));
            return;
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("key")) {
            instance.getConfig().set("api-key", args[1]);
            instance.getConfigWrapper().saveConfig();
            instance.getProxy().getScheduler().runAsync(instance, () -> {
                try {
                    if (instance.getCraftingStore().reload().get()) {
                        sender.sendMessage(new TextComponent(instance.getPrefix() + "The new API key has been set in the config, and the plugin has been reloaded."));
                    } else {
                        sender.sendMessage(new TextComponent(instance.getPrefix() + "The API key is invalid. The plugin will not work until you set a valid API key."));
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            });
            return;
        }
        if ((args.length == 1 || args.length == 2) && args[0].equalsIgnoreCase("debug")) {
            boolean isDebugging = instance.getCraftingStore().getLogger().isDebugging();
            if (args.length == 1) {
                sender.sendMessage(new TextComponent(String.format(
                        "%sDebug mode is currently %s.",
                        instance.getPrefix(),
                        isDebugging ? "enabled" : "disabled"
                )));
                return;
            }
            String debugValue = args[1].toLowerCase();
            if (debugValue.equalsIgnoreCase("true")) {
                isDebugging = true;
            } else if (debugValue.equalsIgnoreCase("false")) {
                isDebugging = false;
            } else {
                sender.sendMessage(new TextComponent(instance.getPrefix() + "Unknown debug value."));
                return;
            }
            instance.getCraftingStore().getLogger().setDebugging(isDebugging);
            instance.getConfig().set("debug", isDebugging);
            instance.getConfigWrapper().saveConfig();
            sender.sendMessage(new TextComponent(String.format(
                    "%sDebug mode has been %s.",
                    instance.getPrefix(),
                    isDebugging ? "enabled" : "disabled"
            )));
            return;
        }

        sender.sendMessage(new TextComponent(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "-----------------------"));
        sender.sendMessage(new TextComponent(ChatColor.DARK_GRAY + ">" + ChatColor.GRAY + " /csb reload" + ChatColor.DARK_GRAY + " -> " + ChatColor.GRAY + "Reload the config."));
        sender.sendMessage(new TextComponent(ChatColor.DARK_GRAY + ">" + ChatColor.GRAY + " /csb key <your key>" + ChatColor.DARK_GRAY + " -> " + ChatColor.GRAY + "Update the key."));
        sender.sendMessage(new TextComponent(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "-----------------------"));
    }
}
