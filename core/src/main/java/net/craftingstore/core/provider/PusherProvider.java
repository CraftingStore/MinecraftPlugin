package net.craftingstore.core.provider;

import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;
import net.craftingstore.core.CraftingStore;
import net.craftingstore.core.models.api.provider.ProviderInformation;
import net.craftingstore.core.models.api.provider.PusherProviderInformation;

public class PusherProvider extends CraftingStoreProvider {

    private Pusher client;

    public PusherProvider(CraftingStore craftingStore, ProviderStatus status) {
        super(craftingStore, status);
        this.connect();
    }

    @Override
    public boolean isConnected() {
        return client != null && client.getConnection().getState() == ConnectionState.CONNECTED;
    }

    @Override
    public void disconnect() {
        client.disconnect();
    }

    private void connect() {
        PusherProviderInformation information = (PusherProviderInformation) this.information;

        PusherOptions options = new PusherOptions().setCluster(information.getCluster());
        client = new Pusher(information.getApiKey(), options);

        client.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                if (change.getCurrentState() == ConnectionState.DISCONNECTED) {
                    disconnected();
                }
            }

            @Override
            public void onError(String s, String s1, Exception e) {
                e.printStackTrace();
                disconnect();
            }
        }, ConnectionState.ALL);

        // Subscribe to API key.
        Channel channel = client.subscribe(this.craftingStore.getApi().getToken());

        channel.bind("receive-donation", (channel1, event, data) -> this.craftingStore.executeQueue());
        channel.bind("reload-plugin", (channel1, event, data) -> this.craftingStore.reload());
    }
}
