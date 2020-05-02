package net.craftingstore.bukkit;

import net.craftingstore.core.PluginConfiguration;

public class BukkitPluginConfiguration implements PluginConfiguration {

    private CraftingStoreBukkit plugin;

    BukkitPluginConfiguration(CraftingStoreBukkit plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "Bukkit";
    }

    @Override
    public String[] getMainCommands() {
        return new String[]{"craftingstore", "cs"};
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String getPlatform() {
        return plugin.getServer().getVersion();
    }

    @Override
    public boolean isBuyCommandEnabled() {
        return !plugin.getConfig().getBoolean("disable-buy-command", false);
    }

    @Override
    public int getTimeBetweenCommands() {
        return plugin.getConfig().getInt("time-between-commands", 200);
    }
}
