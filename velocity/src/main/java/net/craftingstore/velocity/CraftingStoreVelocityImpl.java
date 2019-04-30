package net.craftingstore.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.craftingstore.core.CraftingStorePlugin;
import net.craftingstore.core.PluginConfiguration;
import net.craftingstore.core.logging.CraftingStoreLogger;
import net.craftingstore.core.models.donation.Donation;
import net.craftingstore.velocity.config.Config;
import net.craftingstore.velocity.events.DonationReceivedEvent;
import net.craftingstore.velocity.events.DonationResult;
import net.craftingstore.velocity.logging.Slf4jLogger;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class CraftingStoreVelocityImpl implements CraftingStorePlugin {

    @Inject
    private CraftingStoreVelocity velocityPlugin;

    @Inject
    private Config config;

    @Inject
    private ProxyServer proxyServer;

    private Slf4jLogger logger;

    @Inject
    private VelocityPluginConfiguration configuration;

    @Inject
    private void setLogger(Slf4jLogger logger) {
        this.logger = logger;
        this.logger.setDebugging((Boolean) this.config.getConfig().getOrDefault("debug", false));
    }

    @Override
    public boolean executeDonation(Donation donation) {
        if (donation.getPlayer().isRequiredOnline()) {
            Optional<Player> player = proxyServer.getPlayer(donation.getPlayer().getUsername());
            if (!player.isPresent() || !player.get().isActive()) {
                return false;
            }
        }

        DonationReceivedEvent event = new DonationReceivedEvent(donation);
        event.setResult(new DonationResult(true));
        proxyServer.getEventManager().fire(event);
        if (!event.getResult().isAllowed()) {
            return false;
        }

        if (!proxyServer.getCommandManager().execute(proxyServer.getConsoleCommandSource(), donation.getCommand())) {
            logger.error("Command " + donation.getCommand() + " failed");
        }
        return true;
    }

    @Override
    public CraftingStoreLogger getLogger() {
        return logger;
    }

    @Override
    public void registerRunnable(Runnable runnable, int delay, int interval) {
        proxyServer.getScheduler().buildTask(velocityPlugin, runnable)
                .delay(delay, TimeUnit.SECONDS)
                .repeat(interval, TimeUnit.SECONDS)
                .schedule();
    }

    @Override
    public void runAsyncTask(Runnable runnable) {
        proxyServer.getScheduler().buildTask(velocityPlugin, runnable).schedule();
    }

    @Override
    public String getToken() {
        return (String) config.getConfig().get("api-key");
    }

    @Override
    public PluginConfiguration getConfiguration() {
        return this.configuration;
    }
}
