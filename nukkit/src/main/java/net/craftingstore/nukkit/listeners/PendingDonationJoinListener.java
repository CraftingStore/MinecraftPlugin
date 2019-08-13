package net.craftingstore.nukkit.listeners;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import net.craftingstore.core.exceptions.CraftingStoreApiException;
import net.craftingstore.core.jobs.ProcessPendingPaymentsJob;
import net.craftingstore.nukkit.CraftingStoreNukkit;

public class PendingDonationJoinListener implements Listener {
    private final CraftingStoreNukkit instance;

    public PendingDonationJoinListener(CraftingStoreNukkit instance) {
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
