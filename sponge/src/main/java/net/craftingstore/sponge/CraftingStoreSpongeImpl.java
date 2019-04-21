package net.craftingstore.sponge;

import com.google.inject.Inject;
import net.craftingstore.core.CraftingStorePlugin;
import net.craftingstore.core.PluginConfiguration;
import net.craftingstore.core.logging.CraftingStoreLogger;
import net.craftingstore.core.models.donation.Donation;
import net.craftingstore.sponge.config.Config;
import net.craftingstore.sponge.events.DonationReceivedEvent;
import net.craftingstore.sponge.logging.Slf4jLogger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Platform;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.plugin.PluginContainer;

import java.util.concurrent.TimeUnit;

public class CraftingStoreSpongeImpl implements CraftingStorePlugin {

    @Inject
    private CraftingStoreSponge spongePlugin;

    @Inject
    private Game game;

    @Inject
    private Config config;

    @Inject
    private SpongePluginConfiguration configuration;

    private Slf4jLogger logger;

    @Inject
    private void setLogger(Slf4jLogger logger) {
        this.logger = logger;
        this.logger.setDebugging(this.config.getConfig().getNode("debug").getBoolean());
    }

    public boolean executeDonation(Donation donation) {
        if (donation.getPlayer().isRequiredOnline()) {
            Player player = game.getServer().getPlayer(donation.getPlayer().getUsername()).orElse(null);
            if (player == null || !player.isOnline()) {
                return false;
            }
        }

        DonationReceivedEvent event = new DonationReceivedEvent(donation);
        game.getEventManager().post(event);
        if (event.isCancelled()) {
            return false;
        }

        game.getScheduler().createTaskBuilder()
                .execute(() -> game.getCommandManager().process(game.getServer().getConsole(), donation.getCommand()))
                .submit(spongePlugin);
        return true;
    }

    public CraftingStoreLogger getLogger() {
        return this.logger;
    }

    public void registerRunnable(Runnable runnable, int delay, int interval) {
        game.getScheduler().createTaskBuilder()
                .execute(runnable)
                .async()
                .delay(delay, TimeUnit.SECONDS)
                .interval(interval, TimeUnit.SECONDS)
                .submit(spongePlugin);
    }

    public void runAsyncTask(Runnable runnable) {
        game.getScheduler().createTaskBuilder()
                .execute(runnable)
                .async()
                .submit(spongePlugin);
    }

    public String getToken() {
        return config.getConfig().getNode("api-key").getString();
    }

    @Override
    public PluginConfiguration getConfiguration() {
        return this.configuration;
    }
}
