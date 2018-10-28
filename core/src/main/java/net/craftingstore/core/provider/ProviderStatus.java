package net.craftingstore.core.provider;

import net.craftingstore.core.models.api.provider.ProviderType;

public class ProviderStatus {
    private ProviderType type;
    private long lastSuccessful;
    private long lastFailed;
    private int retries;

    public ProviderStatus(ProviderType type) {
        this.type = type;
    }

    public ProviderType getType() {
        return type;
    }

    public long getLastSuccessful() {
        return lastSuccessful;
    }

    public void setLastSuccessful(long lastSuccessful) {
        this.lastSuccessful = lastSuccessful;
    }

    public long getLastFailed() {
        return lastFailed;
    }

    public void setLastFailed(long lastFailed) {
        this.lastFailed = lastFailed;
    }

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }
}
