package net.craftingstore.velocity.command;

import com.google.inject.Inject;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ProxyServer;
import net.craftingstore.core.CraftingStore;
import net.craftingstore.velocity.CraftingStoreVelocity;
import net.craftingstore.velocity.annotation.Prefix;
import net.craftingstore.velocity.config.Config;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.concurrent.ExecutionException;

public class CraftingStoreCommand implements Command {

    @Inject
    private CraftingStore craftingStore;

    @Inject
    @Prefix
    private TextComponent prefix;

    @Inject
    private CraftingStoreVelocity velocityPlugin;

    @Inject
    private Config config;

    @Inject
    private ProxyServer proxyServer;

    @Override
    public void execute(CommandSource sender, @NonNull String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            craftingStore.reload();
            sender.sendMessage(TextComponent.builder("")
                    .append(prefix)
                    .append(TextComponent.of("The plugin is reloading!"))
                    .build());
            return;
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("key")) {
            config.getConfig().put("api-key", args[1]);
            config.saveConfig();
            proxyServer.getScheduler().buildTask(velocityPlugin, () -> {
                try {
                    if (craftingStore.reload().get()) {
                        sender.sendMessage(TextComponent.builder("")
                                .append(prefix)
                                .append(TextComponent.of("The new API key has been set in the config, and the plugin has been reloaded."))
                                .build());
                    } else {
                        sender.sendMessage(TextComponent.builder("")
                                .append(prefix)
                                .append(TextComponent.of("The API key is invalid. The plugin will not work until you set a valid API key."))
                                .build());
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }).schedule();
            return;
        }

        sender.sendMessage(
                TextComponent.of("-----------------------", TextColor.GRAY).decoration(TextDecoration.STRIKETHROUGH, true)
        );
        sender.sendMessage(TextComponent.builder("")
                .append(TextComponent.of("> ", TextColor.DARK_GRAY))
                .append(TextComponent.of("/csv reload", TextColor.GRAY))
                .append(TextComponent.of(" -> ", TextColor.DARK_GRAY))
                .append(TextComponent.of("Reload the config.", TextColor.GRAY))
                .build()
        );
        sender.sendMessage(TextComponent.builder("")
                .append(TextComponent.of("> ", TextColor.DARK_GRAY))
                .append(TextComponent.of("/csv key <your key>", TextColor.GRAY))
                .append(TextComponent.of(" -> ", TextColor.DARK_GRAY))
                .append(TextComponent.of("Update the key.", TextColor.GRAY))
                .build()
        );
        sender.sendMessage(
                TextComponent.of("-----------------------", TextColor.GRAY).decoration(TextDecoration.STRIKETHROUGH, true)
        );
    }

    @Override
    public boolean hasPermission(CommandSource source, @NonNull String[] args) {
        return source.hasPermission("craftingstore.admin");
    }
}
