package net.craftingstore.bungee;

import net.craftingstore.core.PluginConfiguration;

public class BungeePluginConfiguration implements PluginConfiguration {

    private CraftingStoreBungee plugin;

    BungeePluginConfiguration(CraftingStoreBungee plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "BungeeCord";
    }

    @Override
    public String[] getMainCommands() {
        return new String[]{"csb"};
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String getPlatform() {
        return plugin.getProxy().getVersion();
    }

    @Override
    public boolean isBuyCommandEnabled() {
        return false;
    }

    @Override
    public int getTimeBetweenCommands() {
        return plugin.getConfig().getInt("time-between-commands", 200);
    }
}
