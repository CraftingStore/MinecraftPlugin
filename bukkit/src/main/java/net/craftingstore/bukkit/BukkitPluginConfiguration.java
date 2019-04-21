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
}
