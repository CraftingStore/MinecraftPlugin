package net.craftingstore.nukkit.logging;

import cn.nukkit.plugin.PluginLogger;
import cn.nukkit.utils.LogLevel;
import net.craftingstore.core.logging.CraftingStoreLogger;

public class NukkitLogger extends CraftingStoreLogger {
    private PluginLogger logger;

    public NukkitLogger(PluginLogger logger) {
        this.logger = logger;
    }

    public void info(String message) {
        logger.info(message);
    }

    public void error(String message) {
        logger.log(LogLevel.ERROR, message);
    }
}
