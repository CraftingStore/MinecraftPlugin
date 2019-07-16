package net.craftingstore.core.provider;

import io.socket.client.IO;
import io.socket.client.Socket;
import net.craftingstore.core.CraftingStore;
import net.craftingstore.core.models.api.provider.SocketProviderInformation;

import java.net.URISyntaxException;

public class SocketProvider extends CraftingStoreProvider {

    private Socket client;
    private static final long DISABLE_TIMEOUT = 5 * 1000;

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
            // Let the current thread wait until disconnected, to prevent the server from killing the threads.
            try {
                long disconnectStart = System.currentTimeMillis();
                while (client.connected()) {
                    Thread.sleep(5);
                    if (System.currentTimeMillis() - disconnectStart > DISABLE_TIMEOUT) {
                        break;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void connect() {
        SocketProviderInformation information = (SocketProviderInformation) this.information;
        try {
            IO.Options options = new IO.Options();
            options.reconnection = false;
            this.client = IO.socket(information.getUrl(), options);

            // Authenticate
            this.client.on(Socket.EVENT_CONNECT, (Object... args) -> {
                craftingStore.getLogger().debug("Socket server connected, sending authentication token");
                this.client.emit("auth-client", this.craftingStore.getApi().getToken());
            });
            this.client.on(Socket.EVENT_DISCONNECT, (Object... args) -> {
                craftingStore.getLogger().debug("Socket server disconnected, reason: " + args[0]);
                this.disconnected();
            });
            this.client.on(Socket.EVENT_CONNECT_ERROR, (Object... args) -> {
                craftingStore.getLogger().debug("Socket server connect error event called");
                this.disconnected();
            });
            this.client.on("authenticated", (Object... args) -> {
                craftingStore.getLogger().debug("Socket server authenticated");
            });

            this.client.on("receive-donation", (Object... args) -> {
                craftingStore.getLogger().debug("Received donation from Socket server");
                this.craftingStore.executeQueue();
            });
            this.client.on("reload-plugin", (Object... args) -> this.craftingStore.reload());
            this.client.on(Socket.EVENT_ERROR, (Object... args) -> {
                craftingStore.getLogger().debug("Socket error event called");
            });
            this.client.on(Socket.EVENT_RECONNECT, (Object... args) -> {
                craftingStore.getLogger().debug("Socket reconnect event called");
            });
            this.client.on(Socket.EVENT_MESSAGE, (Object... args) -> {
                craftingStore.getLogger().debug("Socket message event called");
            });
            this.client.connect();
            craftingStore.getLogger().debug("Connecting to CraftingStore websocket at " + information.getUrl());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
