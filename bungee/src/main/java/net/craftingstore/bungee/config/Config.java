package net.craftingstore.bungee.config;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.logging.Level;

public class Config {

    private Plugin instance;
    private Configuration configuration;
    private String filename;

    public Config(Plugin instance, String filename) {
        this.instance = instance;
        this.filename = filename;

        instance.getDataFolder().mkdir();

        File file = new File(instance.getDataFolder(), filename);
        if (!file.exists()) {
            InputStream is = instance.getResourceAsStream(filename);
            if (is != null) {
                instance.getLogger().info("Copying default config file.");
                try {
                    Files.copy(is, file.toPath());
                } catch (IOException e) {
                    instance.getLogger().log(Level.SEVERE, "An error occurred while copying the default config file " + filename + ".", e);
                }
            } else {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    instance.getLogger().log(Level.SEVERE, "An error occurred while creating the config file " + filename + ".", e);
                }
            }
        }

        reload();
    }

    public void reload() {
        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(instance.getDataFolder(), filename));
        } catch (IOException e) {
            instance.getLogger().log(Level.SEVERE, "An error occurred while loading the config file " + filename + ".", e);
        }
    }

    public Configuration getConfig() {
        if (configuration == null) {
            reload();
        }
        return configuration;
    }

    public void saveConfig() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, new File(instance.getDataFolder(), filename));
        } catch (IOException e) {
            instance.getLogger().log(Level.SEVERE, "An error occured while saving the config file " + filename + ".", e);
        }
    }

}
