package net.craftingstore.bungee;

import net.craftingstore.bungee.events.DonationReceivedEvent;
import net.craftingstore.core.CraftingStorePlugin;
import net.craftingstore.core.PluginConfiguration;
import net.craftingstore.core.logging.CraftingStoreLogger;
import net.craftingstore.core.logging.impl.JavaLogger;
import net.craftingstore.core.models.donation.Donation;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.concurrent.TimeUnit;

public class CraftingStoreBungeeImpl implements CraftingStorePlugin {

    private CraftingStoreBungee bungeePlugin;
    private JavaLogger logger;
    private BungeePluginConfiguration configuration;

    CraftingStoreBungeeImpl(CraftingStoreBungee bungeePlugin) {
        this.bungeePlugin = bungeePlugin;
        this.configuration = new BungeePluginConfiguration(bungeePlugin);
        this.logger = new JavaLogger(bungeePlugin.getLogger());
        this.logger.setDebugging(bungeePlugin.getConfig().getBoolean("debug", false));
    }

    public boolean executeDonation(final Donation donation) {
        ProxyServer proxy = bungeePlugin.getProxy();
        if (donation.getPlayer().isRequiredOnline()) {
            ProxiedPlayer player = proxy.getPlayer(donation.getPlayer().getUsername());
            if (player == null || !player.isConnected()) {
                return false;
            }
        }

        DonationReceivedEvent event = new DonationReceivedEvent(donation);
        proxy.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return false;
        }

        proxy.getScheduler().runAsync(bungeePlugin, () -> proxy.getPluginManager().dispatchCommand(proxy.getConsole(), donation.getCommand()));
        return true;
    }

    public CraftingStoreLogger getLogger() {
        return this.logger;
    }

    public void registerRunnable(Runnable runnable, int delay, int interval) {
        bungeePlugin.getProxy().getScheduler().schedule(bungeePlugin, runnable, delay, interval, TimeUnit.SECONDS);
    }

    public void runAsyncTask(Runnable runnable) {
        bungeePlugin.getProxy().getScheduler().runAsync(bungeePlugin, runnable);
    }

    public String getToken() {
        return bungeePlugin.getConfig().getString("api-key");
    }

    @Override
    public PluginConfiguration getConfiguration() {
        return this.configuration;
    }
}
