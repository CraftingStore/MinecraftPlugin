package net.craftingstore.bukkit;

import net.craftingstore.bukkit.commands.BuyCommand;
import net.craftingstore.bukkit.commands.CraftingStoreCommand;
import net.craftingstore.bukkit.config.Config;
import net.craftingstore.bukkit.hooks.PlaceholderAPIHook;
import net.craftingstore.bukkit.listeners.InventoryListener;
import net.craftingstore.bukkit.listeners.AdminJoinListener;
import net.craftingstore.bukkit.listeners.PendingDonationJoinListener;
import net.craftingstore.core.CraftingStore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class CraftingStoreBukkit extends JavaPlugin {

    private CraftingStore craftingStore;
    private Config config;
    private String prefix = ChatColor.GRAY + "[" + ChatColor.RED + "CraftingStore" + ChatColor.GRAY + "] " + ChatColor.WHITE;

    @Override
    public void onEnable() {
        config = new Config("config.yml", this);
        this.craftingStore = new CraftingStore(new CraftingStoreBukkitImpl(this));

        this.getCommand("craftingstore").setExecutor(new CraftingStoreCommand(this));
        if (this.craftingStore.getImplementation().getConfiguration().isBuyCommandEnabled()) {
            this.getCommand("csbuy").setExecutor(new BuyCommand(this));
        }

        this.getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
        this.getServer().getPluginManager().registerEvents(new AdminJoinListener(this), this);
        this.getServer().getPluginManager().registerEvents(new PendingDonationJoinListener(this), this);

        // PlaceholderAPI hook
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PlaceholderAPIHook(craftingStore);
            craftingStore.getLogger().info("Hooked with PlaceholderAPI");
        }
    }

    @Override
    public void onDisable() {
        craftingStore.setEnabled(false);
        craftingStore.getLogger().debug("Shutdown complete");
    }

    public CraftingStore getCraftingStore() {
        return craftingStore;
    }

    @Override
    public FileConfiguration getConfig() {
        return this.config.getConfig();
    }

    public Config getConfigWrapper() {
        return this.config;
    }

    public String getPrefix() {
        return prefix;
    }
}
