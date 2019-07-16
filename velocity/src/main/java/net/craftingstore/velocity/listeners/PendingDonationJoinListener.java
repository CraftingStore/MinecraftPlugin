package net.craftingstore.velocity.listeners;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;
import net.craftingstore.core.CraftingStore;
import net.craftingstore.core.exceptions.CraftingStoreApiException;
import net.craftingstore.core.jobs.ProcessPendingPaymentsJob;

public class PendingDonationJoinListener {

    @Inject
    private CraftingStore craftingStore;

    @Subscribe
    public void onPlayerJoin(PostLoginEvent event) {
        Player player = event.getPlayer();
        String username = player.getUsername();

        craftingStore.getImplementation().runAsyncTask(() -> {
            try {
                new ProcessPendingPaymentsJob(craftingStore, username);
            } catch (CraftingStoreApiException ex) {
                ex.printStackTrace();
            }
        });
    }
}
