package net.craftingstore.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.PluginDescription;
import com.velocitypowered.api.proxy.ProxyServer;
import net.craftingstore.core.PluginConfiguration;

public class VelocityPluginConfiguration implements PluginConfiguration {

    @Inject
    private PluginDescription description;

    @Inject
    private ProxyServer proxyServer;

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
}
