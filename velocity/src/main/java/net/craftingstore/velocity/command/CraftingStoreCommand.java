package net.craftingstore.velocity.command;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.ProxyServer;
import net.craftingstore.core.CraftingStore;
import net.craftingstore.velocity.CraftingStoreVelocity;
import net.craftingstore.velocity.annotation.Prefix;
import net.craftingstore.velocity.config.Config;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.concurrent.ExecutionException;

public class CraftingStoreCommand implements SimpleCommand {

    @Inject
    private CraftingStore craftingStore;

    @Inject
    @Prefix
    private Component prefix;

    @Inject
    private CraftingStoreVelocity velocityPlugin;

    @Inject
    private Config config;

    @Inject
    private ProxyServer proxyServer;

    @Override
    public void execute(Invocation invocation) {
        String[] args = invocation.arguments();
        CommandSource sender = invocation.source();
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            craftingStore.reload();
            sender.sendMessage(Component.text()
                    .append(prefix)
                    .append(Component.text("The plugin is reloading!"))
                    .build());
            return;
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("key")) {
            config.getConfig().put("api-key", args[1]);
            config.saveConfig();
            proxyServer.getScheduler().buildTask(velocityPlugin, () -> {
                try {
                    if (craftingStore.reload().get()) {
                        sender.sendMessage(Component.text()
                                .append(prefix)
                                .append(Component.text("The new API key has been set in the config, and the plugin has been reloaded."))
                                .build());
                    } else {
                        sender.sendMessage(Component.text()
                                .append(prefix)
                                .append(Component.text("The API key is invalid. The plugin will not work until you set a valid API key."))
                                .build());
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }).schedule();
            return;
        }

        sender.sendMessage(
                Component.text("-----------------------", NamedTextColor.GRAY).decoration(TextDecoration.STRIKETHROUGH, true)
        );
        sender.sendMessage(Component.text()
                .append(Component.text("> ", NamedTextColor.DARK_GRAY))
                .append(Component.text("/csv reload", NamedTextColor.GRAY))
                .append(Component.text(" -> ", NamedTextColor.DARK_GRAY))
                .append(Component.text("Reload the config.", NamedTextColor.GRAY))
                .build()
        );
        sender.sendMessage(Component.text()
                .append(Component.text("> ", NamedTextColor.DARK_GRAY))
                .append(Component.text("/csv key <your key>", NamedTextColor.GRAY))
                .append(Component.text(" -> ", NamedTextColor.DARK_GRAY))
                .append(Component.text("Update the key.", NamedTextColor.GRAY))
                .build()
        );
        sender.sendMessage(
                Component.text("-----------------------", NamedTextColor.GRAY).decoration(TextDecoration.STRIKETHROUGH, true)
        );
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("craftingstore.admin");
    }
}
