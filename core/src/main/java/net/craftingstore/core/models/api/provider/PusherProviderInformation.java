package net.craftingstore.core.models.api.provider;

public class PusherProviderInformation extends ProviderInformation {

    private String apiKey;
    private String cluster;

    public String getApiKey() {
        return apiKey;
    }

    public String getCluster() {
        return cluster;
    }
}
