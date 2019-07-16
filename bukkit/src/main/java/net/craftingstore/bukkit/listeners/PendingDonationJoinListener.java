package net.craftingstore.bukkit.listeners;

import net.craftingstore.bukkit.CraftingStoreBukkit;
import net.craftingstore.core.exceptions.CraftingStoreApiException;
import net.craftingstore.core.jobs.ProcessPendingPaymentsJob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PendingDonationJoinListener implements Listener {

    private final CraftingStoreBukkit instance;

    public PendingDonationJoinListener(CraftingStoreBukkit instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        String username = p.getName();

        instance.getCraftingStore().getImplementation().runAsyncTask(() -> {
            try {
                new ProcessPendingPaymentsJob(instance.getCraftingStore(), username);
            } catch (CraftingStoreApiException ex) {
                ex.printStackTrace();
            }
        });
    }
}
