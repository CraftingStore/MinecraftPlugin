package net.craftingstore.bungee.listeners;

import net.craftingstore.bungee.CraftingStoreBungee;
import net.craftingstore.core.exceptions.CraftingStoreApiException;
import net.craftingstore.core.jobs.ProcessPendingPaymentsJob;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PendingDonationJoinListener implements Listener {

    private final CraftingStoreBungee instance;

    public PendingDonationJoinListener(CraftingStoreBungee instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onPostLogin(PostLoginEvent e) {
        ProxiedPlayer p = e.getPlayer();
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
