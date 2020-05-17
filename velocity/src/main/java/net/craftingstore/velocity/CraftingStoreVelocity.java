package net.craftingstore.velocity;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import net.craftingstore.core.CraftingStore;
import net.craftingstore.velocity.command.CraftingStoreCommand;
import net.craftingstore.velocity.config.Config;
import net.craftingstore.velocity.listeners.PendingDonationJoinListener;
import net.craftingstore.velocity.module.ConfigModule;
import net.craftingstore.velocity.module.CraftingStoreModule;

import java.nio.file.Path;

@Singleton
@Plugin(id = "craftingstore", name = "CraftingStore", version = "2.3.1")
public class CraftingStoreVelocity {

    @Inject
    private CommandManager commandManager;

    @Inject
    private EventManager eventManager;

    private Config config;
    private CraftingStore craftingStore;
    private Injector injector;

    @Inject
    public CraftingStoreVelocity(@DataDirectory Path configDirectory, Injector injector) {
        this.config = new Config(configDirectory.toFile(), "config.json");
        this.injector = injector.createChildInjector(new ConfigModule(config));
    }

    @Subscribe
    public void onInitialization(ProxyInitializeEvent event) {
        this.craftingStore = new CraftingStore(injector.getInstance(CraftingStoreVelocityImpl.class));
        injector = injector.createChildInjector(new CraftingStoreModule(craftingStore));

        commandManager.register(injector.getInstance(CraftingStoreCommand.class), "csv");
        eventManager.register(this, injector.getInstance(PendingDonationJoinListener.class));
    }

    @Subscribe
    public void onShutdown(ProxyShutdownEvent event) {
        this.craftingStore.setEnabled(false);
    }
}
