package net.craftingstore.core.provider;

import net.craftingstore.core.CraftingStore;
import net.craftingstore.core.models.api.provider.ProviderInformation;

public abstract class CraftingStoreProvider {

    CraftingStore craftingStore;
    ProviderInformation information;
    ProviderStatus status;

    public CraftingStoreProvider(CraftingStore craftingStore, ProviderStatus status) {
        this.craftingStore = craftingStore;
        this.status = status;
        information = status.getInformation();
    }

    public abstract boolean isConnected();

    public abstract void disconnect();

    public void disconnected() {
        craftingStore.getLogger().debug("Disconnected from provider " + information.getType());
        if (!craftingStore.isEnabled()) {
            return;
        }
        status.setRetries(status.getRetries() + 1);
        status.setLastFailed(System.currentTimeMillis());
        craftingStore.getProviderSelector().selectProvider();
    }
}
