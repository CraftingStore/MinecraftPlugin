package net.craftingstore.velocity.module;

import com.google.inject.AbstractModule;
import net.craftingstore.velocity.config.Config;

public class ConfigModule extends AbstractModule {

    private Config config;

    public ConfigModule(Config config) {
        this.config = config;
    }

    @Override
    protected void configure() {
        bind(Config.class).toInstance(config);
    }
}
