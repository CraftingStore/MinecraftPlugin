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
        this.information = status.getInformation();
    }

    public abstract boolean isConnected();

    public abstract void disconnect();

    public void disconnected() {
        craftingStore.getLogger().debug("Disconnected from provider " + information.getType());
        if (!craftingStore.isEnabled()) {
            return;
        }
        this.status.setRetries(this.status.getRetries() + 1);
        this.status.setLastFailed(System.currentTimeMillis());
        this.craftingStore.getProviderSelector().selectProvider();
    }
}
