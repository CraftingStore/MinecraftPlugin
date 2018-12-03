package net.craftingstore.sponge;

import com.google.inject.Inject;
import com.google.inject.Injector;
import net.craftingstore.core.CraftingStore;
import net.craftingstore.sponge.commands.CraftingStoreCommand;
import net.craftingstore.sponge.config.Config;
import net.craftingstore.sponge.module.ConfigModule;
import net.craftingstore.sponge.module.CraftingStoreModule;
import org.slf4j.Logger;
import org.spongepowered.api.command.CommandManager;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.LiteralText;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.nio.file.Path;

@Plugin(id = "craftingstore", name = "CraftingStore", version = "2.0")
public class CraftingStoreSponge {

    @Inject
    private Logger logger;

    private CraftingStore craftingStore;

    @Inject
    private Injector injector;

    @Inject
    @DefaultConfig(sharedRoot = false)
    private Path defaultConfig;

    @Inject
    private CommandManager commandManager;

    private Config config;

    private LiteralText prefix = Text.builder("[").color(TextColors.GRAY)
            .append(Text.builder("CraftingStore").color(TextColors.RED).build())
            .append(Text.builder("] ").color(TextColors.WHITE).build()).build();

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        config = new Config(defaultConfig.toFile(), "config.yml");
        injector = injector.createChildInjector(new ConfigModule(config));

        this.craftingStore = new CraftingStore(injector.getInstance(CraftingStoreSpongeImpl.class));
        injector = injector.createChildInjector(new CraftingStoreModule(craftingStore));

        CommandSpec craftingStoreCommand = CommandSpec.builder()
                .description(Text.of("CraftingStore main command"))
                .permission(craftingStore.ADMIN_PERMISSION)
                .arguments(
                        GenericArguments.optional(GenericArguments.string(Text.of("action"))),
                        GenericArguments.optional(GenericArguments.string(Text.of("key")))
                )
                .executor(injector.getInstance(CraftingStoreCommand.class))
                .build();
        commandManager.register(this, craftingStoreCommand, "craftingstore", "cs");
    }

    public Config getConfigWrapper() {
        return config;
    }

    public LiteralText getPrefix() {
        return prefix;
    }
}
