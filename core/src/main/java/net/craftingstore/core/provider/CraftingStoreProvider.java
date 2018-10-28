package net.craftingstore.core.provider;

import net.craftingstore.core.CraftingStore;
import net.craftingstore.core.models.api.provider.ProviderInformation;

public abstract class CraftingStoreProvider {

    CraftingStore craftingStore;
    ProviderInformation information;

    public CraftingStoreProvider(CraftingStore craftingStore, ProviderInformation information) {
        this.craftingStore = craftingStore;
        this.information = information;
    }

    public abstract boolean isConnected();

    public void disconnect() {

    }
}
