package net.craftingstore.core.provider;

import io.socket.client.IO;
import io.socket.client.Socket;
import net.craftingstore.core.CraftingStore;
import net.craftingstore.core.models.api.provider.ProviderInformation;
import net.craftingstore.core.models.api.provider.SocketProviderInformation;

import java.net.URISyntaxException;

public class SocketProvider extends CraftingStoreProvider{

    private Socket client;

    public SocketProvider(CraftingStore craftingStore, ProviderInformation information) {
        super(craftingStore, information);
        this.connect();
    }

    @Override
    public boolean isConnected() {
        return client != null && client.connected();
    }

    public void connect() {
        SocketProviderInformation information = (SocketProviderInformation) this.information;
        try {
            IO.Options options = new IO.Options();
            options.reconnection = false;
            this.client = IO.socket(information.getUrl(), options);

            // Authenticate
            this.client.on(Socket.EVENT_CONNECT, (Object... args) -> this.client.emit("auth-client", this.craftingStore.getApi().getToken()));
            this.client.on(Socket.EVENT_DISCONNECT, (Object... args) -> this.disconnect());

            this.client.on("receive-donation", (Object... args) -> this.craftingStore.executeQueue());
            this.client.on("reload-plugin", (Object... args) -> this.craftingStore.reload());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
