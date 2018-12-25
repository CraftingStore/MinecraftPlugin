package net.craftingstore.sponge.commands;

import com.google.inject.Inject;
import net.craftingstore.core.CraftingStore;
import net.craftingstore.sponge.CraftingStoreSponge;
import org.spongepowered.api.Game;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class CraftingStoreCommand implements CommandExecutor {

    @Inject
    private CraftingStore craftingStore;

    @Inject
    private CraftingStoreSponge spongePlugin;

    @Inject
    private Game game;

    @Override
    public CommandResult execute(@Nonnull CommandSource src, @Nonnull CommandContext args) {
        Optional<String> action = args.getOne("action");
        Optional<String> key = args.getOne("key");

        if (action.isPresent() && action.get().equalsIgnoreCase("reload")) {
            craftingStore.reload();

            src.sendMessage(spongePlugin.getPrefix().toBuilder()
                    .append(Text.builder("The plugin is reloading!").build())
                    .build());

            return CommandResult.success();
        } else if (action.isPresent() && key.isPresent() && action.get().equalsIgnoreCase("key")) {
            spongePlugin.getConfigWrapper().getConfig().getNode("api-key").setValue(key.get());
            spongePlugin.getConfigWrapper().saveConfig();

            game.getScheduler().createTaskBuilder().execute(() -> {
                try {
                    if (craftingStore.reload().get()) {
                        src.sendMessage(spongePlugin.getPrefix().toBuilder()
                                .append(Text.builder("The new API key has been set in the config, and the plugin has been reloaded.").build())
                                .build());
                    } else {
                        src.sendMessage(spongePlugin.getPrefix().toBuilder()
                                .append(Text.builder("The API key is invalid. The plugin will not work until you set a valid API key.").build())
                                .build());
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }).async().submit(spongePlugin);
            return CommandResult.success();
        }

        src.sendMessage(Text.builder("-----------------------").color(TextColors.GRAY).style(TextStyles.STRIKETHROUGH).build());
        src.sendMessage(Text.builder("> ").color(TextColors.DARK_GRAY)
                .append(Text.builder("/cs reload").color(TextColors.GRAY).build())
                .append(Text.builder(" -> ").color(TextColors.DARK_GRAY).build())
                .append(Text.builder("Reload the config.").color(TextColors.GRAY).build())
                .build()
        );

        src.sendMessage(Text.builder("> ").color(TextColors.DARK_GRAY)
                .append(Text.builder("/cs key <your key>").color(TextColors.GRAY).build())
                .append(Text.builder(" -> ").color(TextColors.DARK_GRAY).build())
                .append(Text.builder("Update the key.").color(TextColors.GRAY).build())
                .build()
        );
        src.sendMessage(Text.builder("-----------------------").color(TextColors.GRAY).style(TextStyles.STRIKETHROUGH).build());

        return CommandResult.success();
    }
}
