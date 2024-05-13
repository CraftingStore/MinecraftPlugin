package net.craftingstore.bukkit;

import net.craftingstore.bukkit.events.DonationReceivedEvent;
import net.craftingstore.bukkit.util.VersionUtil;
import net.craftingstore.core.CraftingStorePlugin;
import net.craftingstore.core.PluginConfiguration;
import net.craftingstore.core.logging.CraftingStoreLogger;
import net.craftingstore.core.logging.impl.JavaLogger;
import net.craftingstore.core.models.donation.Donation;
import org.bukkit.Bukkit;
import org.bukkit.Server;

import java.util.concurrent.TimeUnit;


public class CraftingStoreBukkitImpl implements CraftingStorePlugin {

    private CraftingStoreBukkit bukkitPlugin;
    private JavaLogger logger;
    private PluginConfiguration pluginConfiguration;

    CraftingStoreBukkitImpl(CraftingStoreBukkit bukkitPlugin) {
        this.bukkitPlugin = bukkitPlugin;
        this.pluginConfiguration = new BukkitPluginConfiguration(bukkitPlugin);
        this.logger = new JavaLogger(bukkitPlugin.getLogger());
        this.logger.setDebugging(bukkitPlugin.getConfig().getBoolean("debug", false));
    }

    public boolean executeDonation(Donation donation) {
        if (donation.getPlayer().isRequiredOnline()) {
            if (Bukkit.getPlayerExact(donation.getPlayer().getUsername()) == null) {
                return false;
            }
        }
        Server server = this.bukkitPlugin.getServer();

        DonationReceivedEvent event = new DonationReceivedEvent(donation);
        server.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return false;
        }

        this.bukkitPlugin.runSyncTask(() -> server.dispatchCommand(server.getConsoleSender(), donation.getCommand()));
        return true;
    }

    public CraftingStoreLogger getLogger() {
        return this.logger;
    }

    public void registerRunnable(Runnable runnable, int delay, int interval) {
        if (VersionUtil.isFoliaSchedulerAvailable()) {
            this.bukkitPlugin.getServer().getAsyncScheduler().runAtFixedRate(this.bukkitPlugin, task -> runnable.run(), delay, interval, TimeUnit.SECONDS);
        } else {
            this.bukkitPlugin.getServer().getScheduler().runTaskTimerAsynchronously(this.bukkitPlugin, runnable, delay * 20, interval * 20);
        }
    }

    public void runAsyncTask(Runnable runnable) {
        if (VersionUtil.isFoliaSchedulerAvailable()) {
            this.bukkitPlugin.getServer().getAsyncScheduler().runNow(this.bukkitPlugin, task -> runnable.run());
        } else {
            this.bukkitPlugin.getServer().getScheduler().runTaskAsynchronously(this.bukkitPlugin, runnable);
        }
    }

    public String getToken() {
        return bukkitPlugin.getConfig().getString("api-key");
    }

    @Override
    public PluginConfiguration getConfiguration() {
        return this.pluginConfiguration;
    }
}
