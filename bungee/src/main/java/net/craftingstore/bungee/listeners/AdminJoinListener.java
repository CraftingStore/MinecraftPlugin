package net.craftingstore.bungee.listeners;

import net.craftingstore.bungee.CraftingStoreBungee;
import net.craftingstore.core.models.api.misc.CraftingStoreInformation;
import net.craftingstore.core.models.api.misc.UpdateInformation;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class AdminJoinListener implements Listener {

    private CraftingStoreBungee instance;

    public AdminJoinListener(CraftingStoreBungee instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onPostLogin(PostLoginEvent e) {
        ProxiedPlayer p = e.getPlayer();
        if (!p.hasPermission(instance.getCraftingStore().ADMIN_PERMISSION)) {
            return;
        }
        CraftingStoreInformation information = instance.getCraftingStore().getInformation();
        UpdateInformation update = null;
        if (information != null) {
            update = information.getUpdateInformation();

            // Update notification
            if (update != null) {
                p.sendMessage(new TextComponent(instance.getPrefix() + update.getMessage()));
            }
        }

        if (!instance.getCraftingStore().isEnabled()) {
            if (update != null && update.shouldDisable()) {
                p.sendMessage(new TextComponent(instance.getPrefix() + "The CraftingStore plugin has been disabled because this is an outdated version. Please update the plugin."));
            } else {
                p.sendMessage(new TextComponent(instance.getPrefix() + "The CraftingStore plugin has not been set-up correctly. Please set your API key using /csb key <your key>."));
            }
        }
    }
}
