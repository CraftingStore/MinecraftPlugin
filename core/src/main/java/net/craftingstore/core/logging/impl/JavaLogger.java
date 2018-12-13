package net.craftingstore.core.logging.impl;

import net.craftingstore.core.logging.CraftingStoreLogger;

import java.util.logging.Level;
import java.util.logging.Logger;

public class JavaLogger extends CraftingStoreLogger {

    private Logger logger;

    public JavaLogger(Logger logger) {
        this.logger = logger;
    }

    public void info(String message) {
        logger.info(message);
    }

    public void error(String message) {
        logger.log(Level.SEVERE, message);
    }
}
