package net.craftingstore.bungee;

import net.craftingstore.bungee.events.DonationReceivedEvent;
import net.craftingstore.core.CraftingStorePlugin;
import net.craftingstore.core.models.donation.Donation;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class CraftingStoreBungeeImpl implements CraftingStorePlugin {

    private CraftingStoreBungee bungeePlugin;

    public CraftingStoreBungeeImpl(CraftingStoreBungee bungeePlugin) {
        this.bungeePlugin = bungeePlugin;
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

    public Logger getLogger() {
        return bungeePlugin.getLogger();
    }

    public void disable() {
        // Not possible on bungeecord
    }

    public void registerRunnable(Runnable runnable, int delay, int interval) {
        bungeePlugin.getProxy().getScheduler().schedule(bungeePlugin, runnable, delay, interval, TimeUnit.SECONDS);
    }

    public String getToken() {
        return bungeePlugin.getConfig().getString("api-key");
    }

    public String getVersion() {
        return bungeePlugin.getDescription().getVersion();
    }

    public String getPlatform() {
        return bungeePlugin.getProxy().getVersion();
    }
}
