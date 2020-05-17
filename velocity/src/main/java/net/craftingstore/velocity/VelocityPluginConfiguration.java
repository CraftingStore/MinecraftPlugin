package net.craftingstore.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.PluginDescription;
import com.velocitypowered.api.proxy.ProxyServer;
import net.craftingstore.core.PluginConfiguration;
import net.craftingstore.velocity.config.Config;

public class VelocityPluginConfiguration implements PluginConfiguration {

    @Inject
    private PluginDescription description;

    @Inject
    private ProxyServer proxyServer;

    @Inject
    private Config config;

    @Override
    public String getName() {
        return "Velocity";
    }

    @Override
    public String[] getMainCommands() {
        return new String[] {"csv"};
    }

    @Override
    public String getVersion() {
        return description.getVersion().orElse("unknown");
    }

    @Override
    public String getPlatform() {
        return proxyServer.getVersion().toString();
    }

    @Override
    public boolean isBuyCommandEnabled() {
        return false;
    }

    @Override
    public int getTimeBetweenCommands() {
        Object value = config.getConfig().getOrDefault("time-between-commands", 200);
        if (value instanceof Integer) {
            return (int) value;
        }
        if (value instanceof Long) {
            return ((Long) value).intValue();
        }
        throw new RuntimeException("Invalid integer value in time-between-commands");
    }
}
