package net.craftingstore.core;

public interface PluginConfiguration {
    String getName();

    String[] getMainCommands();

    String getVersion();

    String getPlatform();

    boolean isBuyCommandEnabled();

    int getTimeBetweenCommands();
}
