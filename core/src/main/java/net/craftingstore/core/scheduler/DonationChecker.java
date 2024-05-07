package net.craftingstore.core.scheduler;

import net.craftingstore.core.CraftingStore;
import net.craftingstore.core.exceptions.CraftingStoreApiException;
import net.craftingstore.core.jobs.ExecuteDonationsJob;
import net.craftingstore.core.models.donation.Donation;
import net.craftingstore.core.util.ArrayUtil;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class DonationChecker implements Runnable {

    private CraftingStore instance;
    private long lastRun = 0;

    public DonationChecker(CraftingStore instance) {
        this.instance = instance;
    }

    public void run() {
        if (!instance.isEnabled()) {
            return;
        }
        if (instance.getProviderSelector().isConnected()) {
            // Only run this runnable every 30 minutes when we are connected to a websocket.
            long target = System.currentTimeMillis() - (30 * 60 * 1000);
            if (lastRun > target) {
                return;
            }
        }
        lastRun = System.currentTimeMillis();
        this.instance.getLogger().debug("Checking for donations by interval.");
        this.instance.getDonationRunner().runDonations();
    }
}
