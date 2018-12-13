package net.craftingstore.core.logging;

public abstract class CraftingStoreLogger {

    private boolean debugging;

    public abstract void info(String message);

    public abstract void error(String message);

    public void debug(String message) {
        if (this.debugging) {
            this.info(message);
        }
    }

    public void setDebugging(boolean debugging) {
        this.debugging = debugging;
        if (debugging) {
            this.debug("Debug logging has been enabled!");
        }
    }
}
