package net.craftingstore.nukkit;

import net.craftingstore.core.PluginConfiguration;

public class NukkitPluginConfiguration implements PluginConfiguration {
    private CraftingStoreNukkit plugin;

    NukkitPluginConfiguration(CraftingStoreNukkit plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "Nukkit";
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
        return !plugin.getConfigFile().getBoolean("disable-buy-command", false);
    }
}
