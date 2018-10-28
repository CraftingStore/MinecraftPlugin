package net.craftingstore.bukkit;

import net.craftingstore.bukkit.config.Config;
import net.craftingstore.core.CraftingStore;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class CraftingStoreBukkit extends JavaPlugin {

    private CraftingStore craftingStore;
    private Config config;

    @Override
    public void onEnable() {
        config = new Config("config.yml", this);
        this.craftingStore = new CraftingStore(new CraftingStoreBukkitImpl(this));
    }

    public CraftingStore getCraftingStore() {
        return craftingStore;
    }

    @Override
    public FileConfiguration getConfig() {
        return this.config.getConfig();
    }
}
