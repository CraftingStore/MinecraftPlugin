package net.craftingstore.hytale.logging.impl;

import com.hypixel.hytale.logger.HytaleLogger;
import net.craftingstore.core.logging.CraftingStoreLogger;

public class HytaleLoggerImpl extends CraftingStoreLogger {
    private final HytaleLogger logger;

    public HytaleLoggerImpl(HytaleLogger logger) {
        this.logger = logger;
    }

    @Override
    public void info(String message) {
        this.logger.atInfo().log(message);
    }

    @Override
    public void error(String message) {
        this.logger.atSevere().log(message);
    }
}
