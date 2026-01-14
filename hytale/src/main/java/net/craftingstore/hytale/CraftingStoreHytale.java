package net.craftingstore.hytale;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.util.Config;
import net.craftingstore.core.CraftingStore;
import net.craftingstore.hytale.commands.CraftingStoreCommand;
import net.craftingstore.hytale.config.CraftingStoreConfig;
import net.craftingstore.hytale.listeners.AdminJoinListener;
import net.craftingstore.hytale.listeners.PendingDonationJoinListener;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.awt.*;

public class CraftingStoreHytale extends JavaPlugin {
    private final Config<CraftingStoreConfig> config = withConfig(CraftingStoreConfig.CODEC);
    private CraftingStore craftingStore;
    private final Message prefix = Message.join(Message.raw("[").color(Color.GRAY), Message.raw("CraftingStore").color(Color.RED), Message.raw("] ").color(Color.GRAY));

    public CraftingStoreHytale(@NonNullDecl JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void start() {
        this.craftingStore = new CraftingStore(new CraftingStoreHytaleImpl(this));
        this.getCommandRegistry().registerCommand(new CraftingStoreCommand(this));
        this.getEventRegistry().registerGlobal(PlayerConnectEvent.class, new AdminJoinListener(this)::onPlayerJoin);
        this.getEventRegistry().registerGlobal(PlayerConnectEvent.class, new PendingDonationJoinListener(this)::onPlayerJoin);
    }

    @Override
    protected void shutdown() {
        this.craftingStore.setEnabled(false);
    }

    public Message getPrefix() {
        return Message.empty().insert(this.prefix);
    }

    public CraftingStore getCraftingStore() {
        return craftingStore;
    }

    public Config<CraftingStoreConfig> getConfig() {
        return config;
    }
}
