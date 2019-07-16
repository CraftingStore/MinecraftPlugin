package net.craftingstore.sponge.listeners;

import com.google.inject.Inject;
import net.craftingstore.core.CraftingStore;
import net.craftingstore.core.exceptions.CraftingStoreApiException;
import net.craftingstore.core.jobs.ProcessPendingPaymentsJob;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;

public class PendingDonationJoinListener {

    @Inject
    private CraftingStore craftingStore;

    @Listener
    public void onJoin(ClientConnectionEvent.Join e) {
        if (!e.getTargetEntity().getPlayer().isPresent()) {
            return;
        }
        Player p = e.getTargetEntity().getPlayer().get();
        String username = p.getName();

        craftingStore.getImplementation().runAsyncTask(() -> {
            try {
                new ProcessPendingPaymentsJob(this.craftingStore, username);
            } catch (CraftingStoreApiException ex) {
                ex.printStackTrace();
            }
        });
    }
}
