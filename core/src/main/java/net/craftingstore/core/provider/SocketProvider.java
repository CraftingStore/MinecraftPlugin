package net.craftingstore.core.provider;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import net.craftingstore.core.CraftingStore;
import net.craftingstore.core.models.api.provider.SocketProviderInformation;

import java.net.URISyntaxException;

public class SocketProvider extends CraftingStoreProvider {

    private Socket client;

    public SocketProvider(CraftingStore craftingStore, ProviderStatus status) {
        super(craftingStore, status);
        this.connect();
    }

    @Override
    public boolean isConnected() {
        return client != null && client.connected();
    }

    @Override
    public void disconnect() {
        if (client != null) {
            client.disconnect();
        }
    }

    private void connect() {
        SocketProviderInformation information = (SocketProviderInformation) this.information;
        try {
            IO.Options options = new IO.Options();
            options.reconnection = false;
            this.client = IO.socket(information.getUrl(), options);

            // Authenticate
            this.client.on(Socket.EVENT_CONNECT, (Object... args) -> this.client.emit("auth-client", this.craftingStore.getApi().getToken()));
            this.client.on(Socket.EVENT_DISCONNECT, (Object... args) -> this.disconnected());
            this.client.on(Socket.EVENT_CONNECT_ERROR, (Object... args) -> this.disconnected());

            this.client.on("receive-donation", (Object... args) -> {
                craftingStore.getLogger().info("Received donation from Socket server");
                this.craftingStore.executeQueue();
            });
            this.client.on("reload-plugin", (Object... args) -> this.craftingStore.reload());
            this.client.connect();
            craftingStore.getLogger().info("Connecting to CraftingStore websocket at " + information.getUrl());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
