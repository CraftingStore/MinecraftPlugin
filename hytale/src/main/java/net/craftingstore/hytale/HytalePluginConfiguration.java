package net.craftingstore.hytale;

import com.hypixel.hytale.common.util.java.ManifestUtil;
import net.craftingstore.core.PluginConfiguration;

public class HytalePluginConfiguration implements PluginConfiguration {
    private final CraftingStoreHytale plugin;

    public HytalePluginConfiguration(CraftingStoreHytale plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "Hytale";
    }

    @Override
    public String[] getMainCommands() {
        return new String[]{"craftingstore", "cs"};
    }

    @Override
    public String getVersion() {
        return plugin.getManifest().getVersion().toString();
    }

    @Override
    public String getPlatform() {
        return ManifestUtil.getVersion().toString();
    }

    @Override
    public boolean isBuyCommandEnabled() {
        return false;
    }

    @Override
    public int getTimeBetweenCommands() {
        return plugin.getConfig().get().getTimeBetweenCommands();
    }

    @Override
    public String getNotEnoughBalanceMessage() {
        return "";
    }

    @Override
    public boolean isUsingAlternativeApi() {
        return false;
    }
}
