package net.craftingstore.bukkit.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

public class Config {

    private Plugin instance;
    private FileConfiguration config = null;
    private File configFile = null;
    private String filename;

    /**
     * Create a new instance of Config, specifying a file name and Plugin.
     *
     * @param filename the file name, including .yml
     * @param instance the instance
     */
    public Config(String filename, Plugin instance) {
        this.filename = filename;
        this.instance = instance;

        reload();

        File file = new File(instance.getDataFolder() + File.pathSeparator + filename);
        if (!file.exists()) {
            if (instance.getResource(filename) != null) {
                Reader defaultConfigFile = new InputStreamReader(instance.getResource(filename), StandardCharsets.UTF_8);
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defaultConfigFile);
                config.setDefaults(defConfig);
                config.options().copyDefaults(true);
                saveConfig();
            }
        }
    }

    /**
     * Reload the config.
     */
    public void reload() {
        if (configFile == null) {
            configFile = new File(instance.getDataFolder(), filename);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    /**
     * Get the FileConfiguration associated with this Config.
     *
     * @return the FileConfiguration
     */
    public FileConfiguration getConfig() {
        if (configFile == null) {
            reload();
        }
        return config;
    }

    /**
     * Save all changes to the disk.
     */
    public void saveConfig() {
        if (config == null || configFile == null) {
            return;
        }

        try {
            getConfig().save(configFile);
        } catch (IOException e) {
            instance.getLogger().log(Level.SEVERE, "An error occurred while saving the config file " + filename + ".", e);
        }
    }
}
