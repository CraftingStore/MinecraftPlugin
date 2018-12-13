package net.craftingstore.core.provider;

import net.craftingstore.core.CraftingStore;
import net.craftingstore.core.models.api.provider.ProviderInformation;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProviderSelector {

    private CraftingStore instance;
    private ProviderStatus[] providers;
    private CraftingStoreProvider currentProvider;

    public ProviderSelector(CraftingStore instance) {
        this.instance = instance;
    }

    public void setProviders(ProviderStatus[] providers) {
        this.providers = providers;
    }

    public void setProviders(ProviderInformation[] providers) {
        this.providers = Arrays.stream(providers)
                .map((Function<ProviderInformation, Object>) ProviderStatus::new)
                .toArray(ProviderStatus[]::new);
    }

    public void selectProvider() {
        if (currentProvider != null && currentProvider.isConnected()) {
            currentProvider.disconnect();
            return;
        }
        currentProvider = getAvailableProvider();
    }

    private CraftingStoreProvider getAvailableProvider() {
        long disallowedRange = System.currentTimeMillis() - (1000 * 60); // 1 minute
        List<ProviderStatus> availableProviders = Arrays.stream(providers)
                .filter(p -> p.getLastFailed() < disallowedRange)
                .sorted(Comparator.comparingInt(p -> p.getInformation().getPriority()))
                .collect(Collectors.toList());
        if (availableProviders.size() == 0) {
            instance.getLogger().debug("No providers available");
            return null;
        }
        ProviderStatus status = availableProviders.get(0);
        Class<? extends CraftingStoreProvider> implementation = status.getInformation().getType().getImplementation();
        try {
            Constructor<?> ctor = implementation.getConstructor(CraftingStore.class, ProviderStatus.class);
            return (CraftingStoreProvider) ctor.newInstance(instance, status);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public CraftingStoreProvider getCurrentProvider() {
        return currentProvider;
    }

    public boolean isConnected() {
        return getCurrentProvider() != null && getCurrentProvider().isConnected();
    }

    public void disconnect() {
        if (isConnected()) {
            getCurrentProvider().disconnect();
        }
    }
}
