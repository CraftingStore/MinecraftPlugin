package net.craftingstore.bukkit;

import net.craftingstore.bukkit.commands.BuyCommand;
import net.craftingstore.bukkit.commands.CraftingStoreCommand;
import net.craftingstore.bukkit.config.Config;
import net.craftingstore.bukkit.hooks.PlaceholderAPIHook;
import net.craftingstore.bukkit.hooks.VaultHook;
import net.craftingstore.bukkit.listeners.InventoryListener;
import net.craftingstore.bukkit.listeners.AdminJoinListener;
import net.craftingstore.bukkit.listeners.PendingDonationJoinListener;
import net.craftingstore.bukkit.util.VersionUtil;
import net.craftingstore.core.CraftingStore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class CraftingStoreBukkit extends JavaPlugin {

    private CraftingStore craftingStore;
    private Config config;
    private String prefix = ChatColor.GRAY + "[" + ChatColor.RED + "CraftingStore" + ChatColor.GRAY + "] " + ChatColor.WHITE;
    private VaultHook vaultHook;

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
        // Vault hook
        if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            this.vaultHook = new VaultHook(this);
            if (this.vaultHook.register()) {
                craftingStore.getLogger().info("Hooked with Vault");
            } else {
                craftingStore.getLogger().info("There was a problem hooking with Vault");
                this.vaultHook = null;
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
        return this.config.getConfig();
    }

    public Config getConfigWrapper() {
        return this.config;
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

    public void runSyncTask(Runnable runnable) {
        if (VersionUtil.isFoliaSchedulerAvailable()) {
            this.getServer().getGlobalRegionScheduler().run(this, (task) -> runnable.run());
        } else {
            this.getServer().getScheduler().runTask(this, runnable);
        }
    }
}
