package net.craftingstore.bukkit;

import net.craftingstore.bukkit.commands.BuyCommand;
import net.craftingstore.bukkit.commands.CraftingStoreCommand;
import net.craftingstore.bukkit.config.Config;
import net.craftingstore.bukkit.hooks.PlaceholderAPIHook;
import net.craftingstore.bukkit.hooks.VaultHook;
import net.craftingstore.bukkit.listeners.InventoryListener;
import net.craftingstore.bukkit.listeners.AdminJoinListener;
import net.craftingstore.bukkit.listeners.PendingDonationJoinListener;
import net.craftingstore.core.CraftingStore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class CraftingStoreBukkit extends JavaPlugin {

    private CraftingStore craftingStore;
    private Config config;
    private final String prefix = ChatColor.GRAY + "[" + ChatColor.RED + "CraftingStore" + ChatColor.GRAY + "] " + ChatColor.WHITE;
    private VaultHook vaultHook;

    @Override
    public void onEnable() {
        config = new Config("config.yml", this);
        craftingStore = new CraftingStore(new CraftingStoreBukkitImpl(this));

        Objects.requireNonNull(getCommand("craftingstore")).setExecutor(new CraftingStoreCommand(this));
        if (craftingStore.getImplementation().getConfiguration().isBuyCommandEnabled()) {
            Objects.requireNonNull(getCommand("csbuy")).setExecutor(new BuyCommand(this));
        }

        getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
        getServer().getPluginManager().registerEvents(new AdminJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PendingDonationJoinListener(this), this);

        // PlaceholderAPI hook
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PlaceholderAPIHook(craftingStore);
            craftingStore.getLogger().info("Hooked with PlaceholderAPI");
        }
        // Vault hook
        if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            vaultHook = new VaultHook(this);
            if (vaultHook.register()) {
                craftingStore.getLogger().info("Hooked with Vault");
            } else {
                craftingStore.getLogger().info("There was a problem hooking with Vault");
                vaultHook = null;
            }
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
        return config.getConfig();
    }

    public Config getConfigWrapper() {
        return config;
    }

    public String getPrefix() {
        return prefix;
    }

    public VaultHook getVaultHook() {
        return vaultHook;
    }

    public boolean isHookedWithVault() {
        return vaultHook != null;
    }
}
