package net.craftingstore.hytale.commands;

import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import net.craftingstore.hytale.CraftingStoreHytale;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CraftingStoreKeyCommand extends CommandBase {

    private final CraftingStoreHytale plugin;
    private final RequiredArg<String> keyRef;

    public CraftingStoreKeyCommand(CraftingStoreHytale plugin) {
        super("key", "Command to configure your API key");
        this.keyRef = this.withRequiredArg("key", "Your CraftingStore api key", ArgTypes.STRING);
        this.plugin = plugin;
    }

    @Override
    protected void executeSync(@NonNullDecl CommandContext commandContext) {
        this.plugin.getConfig().get().setApiKey(keyRef.get(commandContext));
        this.plugin.getConfig().save();
        this.plugin.getTaskRegistry().registerTask(CompletableFuture.runAsync(() -> {
            try {
                if (this.plugin.getCraftingStore().reload().get()) {
                    commandContext.sendMessage(this.plugin.getPrefix().insert("The new API key has been set in the config, and the plugin has been reloaded."));
                } else {
                    commandContext.sendMessage(this.plugin.getPrefix().insert("The API key is invalid. The plugin will not work until you set a valid API key."));

                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }));
    }
}
