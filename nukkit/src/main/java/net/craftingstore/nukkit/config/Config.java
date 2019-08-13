package net.craftingstore.nukkit.config;

import cn.nukkit.plugin.Plugin;
import cn.nukkit.utils.LogLevel;
import com.nimbusds.jose.util.StandardCharset;
import org.simpleyaml.configuration.file.FileConfiguration;
import org.simpleyaml.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class Config {
    private Plugin instance;
    private FileConfiguration config = null;
    private File configFile = null;
    private String fileName;

    /**
     * Create a new instance of Config, specifying a file name and Plugin
     *
     * @param fileName the file name, including.yml
     * @param instance the instance
     */
    public Config(String fileName, Plugin instance) {
        this.fileName = fileName;
        this.instance = instance;

        reload();

        File file = new File(instance.getDataFolder() + File.pathSeparator + fileName);
        if (!file.exists()) {
            if (instance.getResource(fileName) != null) {
                Reader defaultConfigFile = new InputStreamReader(instance.getResource(fileName), StandardCharset.UTF_8);
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defaultConfigFile);
                config.setDefaults(defConfig);
                config.options().copyDefaults(true);
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
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    /**
     * Get the FileConfiguration associated with this Config
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
            instance.getLogger().log(LogLevel.ERROR, "An error occurred while saving the config file " + fileName + ".", e);
        }
    }
}
