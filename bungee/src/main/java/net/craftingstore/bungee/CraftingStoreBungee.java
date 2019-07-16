package net.craftingstore.bungee;

import net.craftingstore.bungee.commands.CraftingStoreCommand;
import net.craftingstore.bungee.config.Config;
import net.craftingstore.bungee.listeners.AdminJoinListener;
import net.craftingstore.bungee.listeners.PendingDonationJoinListener;
import net.craftingstore.core.CraftingStore;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;

public class CraftingStoreBungee extends Plugin {

    private Config config;
    private CraftingStore craftingStore;
    private String prefix = ChatColor.GRAY + "[" + ChatColor.RED + "CraftingStore" + ChatColor.GRAY + "] " + ChatColor.WHITE;

    @Override
    public void onEnable() {
        config = new Config(this, "config.yml");
        this.craftingStore = new CraftingStore(new CraftingStoreBungeeImpl(this));
        getProxy().getPluginManager().registerCommand(this, new CraftingStoreCommand(this));
        getProxy().getPluginManager().registerListener(this, new AdminJoinListener(this));
        getProxy().getPluginManager().registerListener(this, new PendingDonationJoinListener(this));
    }

    @Override
    public void onDisable() {
        craftingStore.setEnabled(false);
    }

    public Configuration getConfig() {
        return config.getConfig();
    }

    public Config getConfigWrapper() {
        return config;
    }

    public CraftingStore getCraftingStore() {
        return craftingStore;
    }

    public String getPrefix() {
        return prefix;
    }
}
