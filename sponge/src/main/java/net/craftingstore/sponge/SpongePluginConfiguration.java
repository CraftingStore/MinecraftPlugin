package net.craftingstore.sponge;

import com.google.inject.Inject;
import net.craftingstore.core.PluginConfiguration;
import org.spongepowered.api.Game;
import org.spongepowered.api.Platform;
import org.spongepowered.api.plugin.PluginContainer;

public class SpongePluginConfiguration implements PluginConfiguration {

    @Inject
    private Game game;

    @Inject
    private PluginContainer pluginContainer;

    @Inject
    private CraftingStoreSponge craftingStoreSponge;

    @Override
    public String getName() {
        return "Sponge";
    }

    @Override
    public String[] getMainCommands() {
        return new String[]{"craftingstore", "cs"};
    }

    @Override
    public String getVersion() {
        return pluginContainer.getVersion().orElse("unknown-version");
    }

    @Override
    public String getPlatform() {
        return "sponge/"
                + game.getPlatform().getContainer(Platform.Component.API).getVersion().orElse("unknown")
                + "/" + game.getPlatform().getContainer(Platform.Component.IMPLEMENTATION).getVersion().orElse("unknown");
    }

    @Override
    public boolean isBuyCommandEnabled() {
        return !craftingStoreSponge.getConfigWrapper().getConfig().getNode("disable-buy-command").getBoolean(false);
    }

    @Override
    public int getTimeBetweenCommands() {
        return craftingStoreSponge.getConfigWrapper().getConfig().getNode("time-between-commands").getInt(200);
    }
}
