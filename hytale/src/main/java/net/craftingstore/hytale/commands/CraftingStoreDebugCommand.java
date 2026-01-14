package net.craftingstore.hytale.commands;

import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import net.craftingstore.hytale.CraftingStoreHytale;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class CraftingStoreDebugCommand extends CommandBase {

    private final CraftingStoreHytale plugin;
    private final RequiredArg<Boolean> debugRef;

    public CraftingStoreDebugCommand(CraftingStoreHytale plugin) {
        super("debug", "Command to enable/disable debug mode");
        this.debugRef = this.withRequiredArg("enabled", "true/false", ArgTypes.BOOLEAN);
        this.plugin = plugin;
    }

    @Override
    protected void executeSync(@NonNullDecl CommandContext commandContext) {
        boolean isDebugging = debugRef.get(commandContext);
        this.plugin.getConfig().get().setDebug(isDebugging);
        this.plugin.getConfig().save();
        commandContext.sendMessage(this.plugin.getPrefix().insert(String.format(
                "Debug mode has been %s.",
                isDebugging ? "enabled" : "disabled"
        )));
    }
}
