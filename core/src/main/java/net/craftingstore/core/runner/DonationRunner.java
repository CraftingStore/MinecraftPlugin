package net.craftingstore.core.runner;

import net.craftingstore.core.CraftingStore;
import net.craftingstore.core.exceptions.CraftingStoreApiException;
import net.craftingstore.core.jobs.ExecuteDonationsJob;
import net.craftingstore.core.models.donation.Donation;
import net.craftingstore.core.util.ArrayUtil;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

public class DonationRunner {

    protected ExecutorService executor = Executors.newSingleThreadExecutor();
    protected CraftingStore craftingStore;

    public DonationRunner(CraftingStore craftingStore) {
        this.craftingStore = craftingStore;
    }

    public void runDonations() {
        this.craftingStore.getLogger().debug("Scheduling new donation runner job.");
        this.executor.submit(() -> {
            this.craftingStore.getLogger().debug("Requesting donation queue.");
            try {
                Donation[] donationQueue = this.craftingStore.getApi().getDonationQueue().get();
                this.craftingStore.getLogger().debug(String.format("Found %d donations in queue.", donationQueue.length));
                Donation[][] chunked = ArrayUtil.splitArray(donationQueue, ExecuteDonationsJob.CHUNK_SIZE);
                for (int i = 0; i < chunked.length; i++) {
                    Donation[] chunk = chunked[i];
                    this.craftingStore.getLogger().debug(String.format("Creating ExecuteDonationsJob for chunk %d/%d", i + 1, chunked.length));
                    new ExecuteDonationsJob(this.craftingStore, chunk);
                }
            } catch (CraftingStoreApiException | InterruptedException | ExecutionException e) {
                if (craftingStore.getLogger().isDebugging()) {
                    e.printStackTrace();
                } else {
                    craftingStore.getLogger().info("Failed to retrieve donation. The plugin will retry every 30 minutes.");
                }
            }
        });
    }

    public ExecutorService getExecutor() {
        return executor;
    }
}
