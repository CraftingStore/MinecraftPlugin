package net.craftingstore.velocity.module;

import com.google.inject.AbstractModule;
import net.craftingstore.core.CraftingStore;
import net.craftingstore.velocity.annotation.Prefix;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;

public class CraftingStoreModule extends AbstractModule {

    private CraftingStore craftingStore;
    private TextComponent prefix = Component.text()
            .append(Component.text("[", NamedTextColor.GRAY))
            .append(Component.text("CraftingStore", NamedTextColor.RED))
            .append(Component.text("] ", NamedTextColor.GRAY))
            .append(Component.text("", NamedTextColor.WHITE))
            .build();

    public CraftingStoreModule(CraftingStore craftingStore) {
        this.craftingStore = craftingStore;
    }

    protected void configure() {
        bind(CraftingStore.class).toInstance(craftingStore);
        bind(Component.class).annotatedWith(Prefix.class).toInstance(prefix);
    }
}
