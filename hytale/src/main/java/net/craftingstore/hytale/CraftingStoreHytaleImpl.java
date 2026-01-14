package net.craftingstore.hytale;

import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.NameMatching;
import com.hypixel.hytale.server.core.console.ConsoleSender;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import net.craftingstore.core.CraftingStorePlugin;
import net.craftingstore.core.PluginConfiguration;
import net.craftingstore.core.logging.CraftingStoreLogger;
import net.craftingstore.core.models.donation.Donation;
import net.craftingstore.hytale.events.DonationReceivedEvent;
import net.craftingstore.hytale.logging.impl.HytaleLoggerImpl;

import java.util.concurrent.TimeUnit;

public class CraftingStoreHytaleImpl implements CraftingStorePlugin {
    private final CraftingStoreHytale hytalePlugin;
    private final HytalePluginConfiguration  pluginConfiguration;
    private final HytaleLoggerImpl logger;

    public CraftingStoreHytaleImpl(CraftingStoreHytale hytalePlugin) {
        this.hytalePlugin = hytalePlugin;
        this.pluginConfiguration = new HytalePluginConfiguration(hytalePlugin);
        this.logger = new HytaleLoggerImpl(hytalePlugin.getLogger());
        this.logger.setDebugging(this.hytalePlugin.getConfig().get().isDebug());
    }

    @Override
    public boolean executeDonation(Donation donation) {
        if (donation.getPlayer().isRequiredOnline()) {
            PlayerRef player = Universe.get().getPlayerByUsername(donation.getPlayer().getUsername(), NameMatching.EXACT_IGNORE_CASE);
            if (player == null) {
                return false;
            }
        }

        DonationReceivedEvent event = HytaleServer.get().getEventBus()
                .dispatchFor(DonationReceivedEvent.class)
                .dispatch(new DonationReceivedEvent(donation));
        if (event.isCancelled()) {
            return false;
        }

        HytaleServer.get().getCommandManager().handleCommand(ConsoleSender.INSTANCE, donation.getCommand());
        return true;
    }

    @Override
    public CraftingStoreLogger getLogger() {
        return this.logger;
    }

    @Override
    public void registerRunnable(Runnable runnable, int delay, int interval) {
        HytaleServer.SCHEDULED_EXECUTOR.scheduleAtFixedRate(runnable, delay, interval, TimeUnit.SECONDS);
    }

    @Override
    public void runAsyncTask(Runnable runnable) {
        HytaleServer.SCHEDULED_EXECUTOR.execute(runnable);
    }

    @Override
    public String getToken() {
        return this.hytalePlugin.getConfig().get().getApiKey();
    }

    @Override
    public PluginConfiguration getConfiguration() {
        return this.pluginConfiguration;
    }
}
