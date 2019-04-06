package net.craftingstore.core.models.api.provider;

import net.craftingstore.core.provider.CraftingStoreProvider;
import net.craftingstore.core.provider.SocketProvider;

public enum ProviderType {
    SOCKET(SocketProviderInformation.class, SocketProvider.class);

    private Class c;
    private Class<? extends CraftingStoreProvider> implementation;

    ProviderType(Class c, Class<? extends CraftingStoreProvider> implementation) {
        this.c = c;
        this.implementation = implementation;
    }

    public Class getActualClass() {
        return c;
    }

    public Class<? extends CraftingStoreProvider> getImplementation() {
        return implementation;
    }
}