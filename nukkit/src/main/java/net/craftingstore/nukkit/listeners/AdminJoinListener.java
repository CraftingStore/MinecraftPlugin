package net.craftingstore.nukkit.listeners;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import net.craftingstore.core.models.api.misc.CraftingStoreInformation;
import net.craftingstore.core.models.api.misc.UpdateInformation;
import net.craftingstore.nukkit.CraftingStoreNukkit;

public class AdminJoinListener implements Listener {
    private CraftingStoreNukkit instance;

    public AdminJoinListener(CraftingStoreNukkit instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!p.hasPermission(instance.getCraftingStore().ADMIN_PERMISSION)) {
            return;
        }
        CraftingStoreInformation information = instance.getCraftingStore().getInformation();
        UpdateInformation update = null;
        if (information != null) {
            update = information.getUpdateInformation();

            // Update notification
            if (update != null) {
                p.sendMessage(instance.getPrefix() + update.getMessage());
            }
        }

        if (!instance.getCraftingStore().isEnabled()) {
            if (update != null && update.shouldDisable()) {
                p.sendMessage(instance.getPrefix() + "The CraftingStore plugin has been disabled because this is an outdated version. Please update the plugin.");
            } else {
                p.sendMessage(instance.getPrefix() + "The CraftingStore plugin has not been set-up correctly. Please set your API key using /craftingstore key <your key>.");
            }
        }
    }
}
