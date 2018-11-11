package net.craftingstore.core.provider;

import net.craftingstore.core.models.api.provider.ProviderInformation;

public class ProviderStatus {
    private ProviderInformation information;
    private long lastFailed;
    private int retries;

    public ProviderStatus(ProviderInformation information) {
        this.information = information;
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

    public ProviderInformation getInformation() {
        return information;
    }
}
