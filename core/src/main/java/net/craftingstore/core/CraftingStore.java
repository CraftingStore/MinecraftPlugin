package net.craftingstore.core;

import net.craftingstore.core.exceptions.CraftingStoreApiException;
import net.craftingstore.core.http.CraftingStoreCachedAPI;
import net.craftingstore.core.logging.CraftingStoreLogger;
import net.craftingstore.core.models.api.Root;
import net.craftingstore.core.models.api.misc.CraftingStoreInformation;
import net.craftingstore.core.models.api.misc.UpdateInformation;
import net.craftingstore.core.models.donation.Donation;
import net.craftingstore.core.provider.ProviderSelector;
import net.craftingstore.core.runner.DonationRunner;
import net.craftingstore.core.scheduler.APICacheRenewer;
import net.craftingstore.core.scheduler.DonationChecker;
import net.craftingstore.core.scheduler.InformationUpdater;
import net.craftingstore.core.scheduler.InventoryRenewer;
import net.craftingstore.core.scheduler.ProviderChecker;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CraftingStore {

    private CraftingStorePlugin plugin;
    private CraftingStoreAPI api;
    private ProviderSelector selector;
    private CraftingStoreInformation information;
    private DonationRunner donationRunner;
    private boolean enabled = false;
    public final String ADMIN_PERMISSION = "craftingstore.admin";
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Map<Integer, Donation> pendingDonations;

    public CraftingStore(CraftingStorePlugin implementation) {
        this.plugin = implementation;
        this.pendingDonations = new HashMap<>();
        this.api = new CraftingStoreCachedAPI(this);
        this.selector = new ProviderSelector(this);
        this.donationRunner = new DonationRunner(this);
        this.getImplementation().runAsyncTask(() -> {
            try {
                if (this.reload().get()) {
                    Random random = new Random();
                    // ~every 4–5 minutes, initial delay 10–30 seconds
                    this.plugin.registerRunnable(new DonationChecker(this),
                            10 + random.nextInt(20),
                            (4 * 60) + random.nextInt(60));

                    // ~every 50–70 seconds, initial delay 50–70 seconds
                    this.plugin.registerRunnable(new ProviderChecker(this),
                            50 + random.nextInt(20),
                            50 + random.nextInt(20));

                    // ~every 19-21 minutes, initial delay 19-21 minutes
                    this.plugin.registerRunnable(new InventoryRenewer(this),
                            (19 * 60) + random.nextInt(60 * 2),
                            (19 * 60) + random.nextInt(60 * 2));

                    // ~every 23-25 minutes, initial delay 10–30 seconds
                    this.plugin.registerRunnable(new APICacheRenewer(this),
                            10 + random.nextInt(30),
                            (60 * 23) + random.nextInt(60 * 2));

                    // ~every 23-24 hours, initial delay 23–24 hours
                    this.plugin.registerRunnable(new InformationUpdater(this),
                            (23 * 60 * 60) + random.nextInt(60 * 60),
                            (23 * 60 * 60) + random.nextInt(60 * 60));
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

    public CraftingStorePlugin getImplementation() {
        return plugin;
    }

    public CraftingStoreAPI getApi() {
        return api;
    }

    public ProviderSelector getProviderSelector() {
        return this.selector;
    }

    public CraftingStoreLogger getLogger() {
        return getImplementation().getLogger();
    }

    public Future<Boolean> reload() {
        this.getApi().setToken(this.getImplementation().getToken());
        return executor.submit(() -> {
            try {
                if (this.getApi().token == null || this.getApi().token.isEmpty()) {
                    getLogger().error(String.format(
                            "API key not set in the config. You need to set the correct api key using /%s key <key>.",
                            this.getImplementation().getConfiguration().getMainCommands()[0]
                    ));
                    setEnabled(false);
                    return false;
                }
                Root keyResult = this.getApi().checkKey().get();
                if (!keyResult.isSuccess()) {
                    getLogger().error("API key is invalid. The plugin will not work.");
                    setEnabled(false);
                    return false;
                }
                information = this.getApi().getInformation().get();

                if (information.getUpdateInformation() != null) {
                    UpdateInformation update = information.getUpdateInformation();
                    getLogger().info(update.getMessage());
                    if (update.shouldDisable()) {
                        getLogger().error("Plugin will be disabled until you install the latest update.");
                        setEnabled(false);
                        return false;
                    }
                }
                setEnabled(true);

                selector.setProviders(information.getProviders());
                selector.selectProvider();

                // Renew the inventory on reload / start
                new InventoryRenewer(this).run();
                getLogger().debug("Startup complete");
            } catch (CraftingStoreApiException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        });
    }

    public void executeQueue() {
        this.donationRunner.runDonations();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (!enabled) {
            getProviderSelector().disconnect();
        }
    }

    public CraftingStoreInformation getInformation() {
        return information;
    }

    public Map<Integer, Donation> getPendingDonations() {
        return pendingDonations;
    }

    public DonationRunner getDonationRunner() {
        return donationRunner;
    }
}
