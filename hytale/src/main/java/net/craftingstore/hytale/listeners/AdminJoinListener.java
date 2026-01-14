package net.craftingstore.hytale.listeners;

import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.permissions.PermissionsModule;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import net.craftingstore.core.models.api.misc.CraftingStoreInformation;
import net.craftingstore.core.models.api.misc.UpdateInformation;
import net.craftingstore.hytale.CraftingStoreHytale;

public class AdminJoinListener {
    private final CraftingStoreHytale instance;

    public AdminJoinListener(CraftingStoreHytale instance) {
        this.instance = instance;
    }

    public void onPlayerJoin(PlayerConnectEvent event) {
        PlayerRef player = event.getPlayerRef();
        if (!PermissionsModule.get().hasPermission(player.getUuid(), instance.getCraftingStore().ADMIN_PERMISSION)) {
            return;
        }

        CraftingStoreInformation information = instance.getCraftingStore().getInformation();
        UpdateInformation update = null;
        if (information != null) {
            update = information.getUpdateInformation();

            // Update notification
            if (update != null) {
                player.sendMessage(instance.getPrefix().insert(update.getMessage()));
            }
        }

        if (!instance.getCraftingStore().isEnabled()) {
            if (update != null && update.shouldDisable()) {
                player.sendMessage(instance.getPrefix().insert("The CraftingStore plugin has been disabled because this is an outdated version. Please update the plugin."));
            } else {
                player.sendMessage(instance.getPrefix().insert("The CraftingStore plugin has not been set-up correctly. Please set your API key using /craftingstore key <your key>."));
            }
        }
    }
}
