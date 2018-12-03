package net.craftingstore.sponge.config;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Config extends File {

    private final YAMLConfigurationLoader loader;
    private ConfigurationNode config;

    /**
     * Create a new instance of Config, specifying a file name and Plugin.
     *
     * @param name the file name, including .yml
     */
    public Config(File dir, String name) {
        super(dir, name);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        if (!exists()) {
            try {
                if (getClass().getClassLoader().getResource(name) != null) {
                    Files.copy(getClass().getClassLoader().getResourceAsStream(name), toPath());
                } else {
                    createNewFile();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        loader = YAMLConfigurationLoader.builder().setFile(this).build();

        try {
            config = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ConfigurationNode getConfig() {
        return this.config;
    }

    /**
     * Save all changes to the disk.
     */
    public void saveConfig() {
        try {
            loader.save(config);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
