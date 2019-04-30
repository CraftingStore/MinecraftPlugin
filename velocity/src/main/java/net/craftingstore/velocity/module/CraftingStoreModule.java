package net.craftingstore.velocity.module;

import com.google.inject.AbstractModule;
import net.craftingstore.core.CraftingStore;
import net.craftingstore.velocity.CraftingStoreVelocity;
import net.craftingstore.velocity.annotation.Prefix;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;

public class CraftingStoreModule extends AbstractModule {

    private CraftingStore craftingStore;
    private TextComponent prefix = TextComponent.builder("")
            .append(TextComponent.of("[", TextColor.GRAY))
            .append(TextComponent.of("CraftingStore", TextColor.RED))
            .append(TextComponent.of("] ", TextColor.GRAY))
            .append(TextComponent.of("", TextColor.WHITE))
            .build();

    public CraftingStoreModule(CraftingStore craftingStore) {
        this.craftingStore = craftingStore;
    }

    protected void configure() {
        bind(CraftingStore.class).toInstance(craftingStore);
        bind(TextComponent.class).annotatedWith(Prefix.class).toInstance(prefix);
    }
}
