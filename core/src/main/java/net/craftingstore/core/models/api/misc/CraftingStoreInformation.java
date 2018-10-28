package net.craftingstore.core.models.api.misc;

import net.craftingstore.core.models.api.provider.ProviderInformation;

public class CraftingStoreInformation {

    private UpdateInformation update;
    private ProviderInformation[] providers;

    public UpdateInformation getUpdateInformation() {
        return update;
    }

    public ProviderInformation[] getProviders() {
        return providers;
    }
}
