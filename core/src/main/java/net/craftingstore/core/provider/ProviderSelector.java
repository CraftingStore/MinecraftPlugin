package net.craftingstore.core.provider;

import net.craftingstore.core.CraftingStore;
import net.craftingstore.core.models.api.provider.ProviderInformation;

public class ProviderSelector {

    private CraftingStore craftingStore;
    private ProviderInformation[] providers;
    private ProviderStatus status;

    public ProviderSelector(CraftingStore craftingStore) {
        this.craftingStore = craftingStore;
    }

    public void setProviders(ProviderInformation[] providers) {
        this.providers = providers;
    }

    public void selectProvider() {

    }
}
