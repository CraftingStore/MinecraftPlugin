package net.craftingstore.hytale.config;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class CraftingStoreConfig {

    public static final BuilderCodec<CraftingStoreConfig> CODEC = BuilderCodec.builder(CraftingStoreConfig.class, CraftingStoreConfig::new)
            .append(new KeyedCodec<>("ApiKey", Codec.STRING),
                    (craftingStoreConfig, value) -> craftingStoreConfig.apiKey = value,
                    craftingStoreConfig -> craftingStoreConfig.apiKey)
            .add()
            .append(new KeyedCodec<>("Debug", Codec.BOOLEAN),
                    (craftingStoreConfig, value) -> craftingStoreConfig.debug = value,
                    craftingStoreConfig -> craftingStoreConfig.debug)
            .add()
            .append(new KeyedCodec<>("TimeBetweenCommands", Codec.INTEGER),
                    (craftingStoreConfig, value) -> craftingStoreConfig.timeBetweenCommands = value,
                    craftingStoreConfig -> craftingStoreConfig.timeBetweenCommands)
            .add()
            .build();

    private String apiKey;
    private boolean debug;
    private int timeBetweenCommands = 200;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public int getTimeBetweenCommands() {
        return timeBetweenCommands;
    }
}
