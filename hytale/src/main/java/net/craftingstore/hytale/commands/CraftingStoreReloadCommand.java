package net.craftingstore.hytale.commands;

import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import net.craftingstore.hytale.CraftingStoreHytale;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class CraftingStoreReloadCommand extends CommandBase {

    private final CraftingStoreHytale plugin;

    public CraftingStoreReloadCommand(CraftingStoreHytale plugin) {
        super("reload", "Command to reload the plugin");
        this.plugin = plugin;
    }

    @Override
    protected void executeSync(@NonNullDecl CommandContext commandContext) {
        this.plugin.getCraftingStore().reload();
        commandContext.sendMessage(this.plugin.getPrefix().insert("The plugin is reloading!"));
    }
}
