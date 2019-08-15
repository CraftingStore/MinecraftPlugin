package net.craftingstore.nukkit.config;

import cn.nukkit.plugin.Plugin;
import cn.nukkit.utils.Config;

import java.io.File;

public class Configuration {
    private Plugin instance;
    private Config config = null;
    private File configFile = null;
    private String fileName;

    /**
     * Create a new instance of Config, specifying a file name and Plugin
     *
     * @param fileName the file name, including.yml
     * @param instance the instance
     */
    public Configuration(String fileName, Plugin instance) {
        this.fileName = fileName;
        this.instance = instance;

        reload();

        File file = new File(instance.getDataFolder() + File.pathSeparator + fileName);
        if (!file.exists()) {
            if (instance.getResource(fileName) != null) {
                Config defConfig = new Config(fileName, Config.YAML);
                defConfig.setDefault(defConfig.getRootSection());
                saveConfig();
            }
        }
    }

    /**
     * reload config
     */
    public void reload() {
        if (configFile == null) {
            configFile = new File(instance.getDataFolder(), fileName);
        }
        config = new Config(configFile, Config.YAML);
    }

    /**
     * Get the FileConfiguration associated with this Config
     *
     * @return the FileConfiguration
     */
    public Config getConfig() {
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

        getConfig().save(configFile);
    }
}
