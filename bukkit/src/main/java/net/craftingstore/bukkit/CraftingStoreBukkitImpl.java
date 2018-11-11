package net.craftingstore.bukkit;

import net.craftingstore.bukkit.events.DonationReceivedEvent;
import net.craftingstore.core.CraftingStorePlugin;
import net.craftingstore.core.models.api.inventory.CraftingStoreInventory;
import net.craftingstore.core.models.donation.Donation;
import org.bukkit.Bukkit;
import org.bukkit.Server;

import java.util.logging.Logger;

public class CraftingStoreBukkitImpl implements CraftingStorePlugin {

    private CraftingStoreBukkit bukkitPlugin;

    public CraftingStoreBukkitImpl(CraftingStoreBukkit bukkitPlugin) {
        this.bukkitPlugin = bukkitPlugin;
    }

    public boolean executeDonation(Donation donation) {
        if (donation.getPlayer().isRequiredOnline()) {
            if (Bukkit.getPlayerExact(donation.getPlayer().getUsername()) == null) {
                return false;
            }
        }
        Server server = bukkitPlugin.getServer();

        DonationReceivedEvent event = new DonationReceivedEvent(donation);
        server.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return false;
        }

        server.getScheduler().runTask(bukkitPlugin, () -> server.dispatchCommand(server.getConsoleSender(), donation.getCommand()));
        return true;
    }

    public void openInventory(CraftingStoreInventory inventory) {

    }

    public Logger getLogger() {
        return bukkitPlugin.getLogger();
    }

    public void disable() {
        bukkitPlugin.getPluginLoader().disablePlugin(bukkitPlugin);
    }

    public void registerRunnable(Runnable runnable, int delay, int interval) {
        bukkitPlugin.getServer().getScheduler().runTaskTimerAsynchronously(bukkitPlugin, runnable, delay * 20, interval * 20);
    }

    public String getToken() {
        return bukkitPlugin.getConfig().getString("api-key");
    }

    @Override
    public String getVersion() {
        return bukkitPlugin.getDescription().getVersion();
    }

    @Override
    public String getPlatform() {
        return bukkitPlugin.getServer().getBukkitVersion();
    }
}
