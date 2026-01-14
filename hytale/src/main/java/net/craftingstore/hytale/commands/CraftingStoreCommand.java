package net.craftingstore.hytale.commands;

import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection;
import net.craftingstore.hytale.CraftingStoreHytale;

public class CraftingStoreCommand extends AbstractCommandCollection {

    public CraftingStoreCommand(CraftingStoreHytale plugin) {
        super("craftingstore", "CraftingStore main command");
        this.addAliases("cs");
        this.requirePermission(plugin.getCraftingStore().ADMIN_PERMISSION);
        this.addSubCommand(new CraftingStoreKeyCommand((plugin)));
        this.addSubCommand(new CraftingStoreReloadCommand((plugin)));
        this.addSubCommand(new CraftingStoreDebugCommand((plugin)));
    }
}
