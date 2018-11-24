package net.craftingstore.bukkit.listeners;

import net.craftingstore.bukkit.CraftingStoreBukkit;
import net.craftingstore.core.models.api.misc.UpdateInformation;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private CraftingStoreBukkit instance;

    public JoinListener(CraftingStoreBukkit instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!p.hasPermission(instance.ADMIN_PERMISSION)) {
            return;
        }
        UpdateInformation update = instance.getCraftingStore().getInformation().getUpdateInformation();
        if (update == null) {
            return;
        }
        p.sendMessage(instance.getPrefix() + update.getMessage());
    }
}
