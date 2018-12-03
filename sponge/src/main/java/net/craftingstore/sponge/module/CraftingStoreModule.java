package net.craftingstore.sponge.module;

import com.google.inject.AbstractModule;
import net.craftingstore.core.CraftingStore;

public class CraftingStoreModule extends AbstractModule {

    private CraftingStore craftingStore;

    public CraftingStoreModule(CraftingStore craftingStore) {
        this.craftingStore = craftingStore;
    }

    protected void configure() {
        bind(CraftingStore.class).toInstance(craftingStore);
    }
}
