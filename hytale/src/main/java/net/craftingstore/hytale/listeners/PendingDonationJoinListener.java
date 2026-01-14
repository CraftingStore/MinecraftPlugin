package net.craftingstore.hytale.listeners;

import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import net.craftingstore.core.exceptions.CraftingStoreApiException;
import net.craftingstore.core.jobs.ProcessPendingPaymentsJob;
import net.craftingstore.hytale.CraftingStoreHytale;

public class PendingDonationJoinListener {
    private final CraftingStoreHytale instance;

    public PendingDonationJoinListener(CraftingStoreHytale instance) {
        this.instance = instance;
    }

    public void onPlayerJoin(PlayerConnectEvent event) {
        PlayerRef player = event.getPlayerRef();
        String username = player.getUsername();

        instance.getCraftingStore().getImplementation().runAsyncTask(() -> {
            try {
                new ProcessPendingPaymentsJob(instance.getCraftingStore(), username);
            } catch (CraftingStoreApiException ex) {
                ex.printStackTrace();
            }
        });
    }
}
