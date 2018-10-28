package net.craftingstore.core.models.api.provider;

public enum ProviderType {
    HTTP(HttpProviderInformation.class), SOCKET(SocketProviderInformation.class), PUSHER(PusherProviderInformation.class);

    private Class c;

    ProviderType(Class c) {
        this.c = c;
    }

    public Class getActualClass() {
        return c;
    }
}