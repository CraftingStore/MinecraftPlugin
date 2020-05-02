package net.craftingstore.core.runner;

import net.craftingstore.core.CraftingStore;
import net.craftingstore.core.exceptions.CraftingStoreApiException;
import net.craftingstore.core.jobs.ExecuteDonationsJob;
import net.craftingstore.core.models.donation.Donation;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DonationRunner {

    protected ExecutorService executor = Executors.newSingleThreadExecutor();
    protected CraftingStore craftingStore;

    public DonationRunner(CraftingStore craftingStore) {
        this.craftingStore = craftingStore;
    }

    public void runDonations() {
        this.craftingStore.getLogger().debug("Scheduling new execute donations job");
        this.executor.submit(() -> {
            this.craftingStore.getLogger().debug("Executing donation queue.");
            try {
                Donation[] donationQueue = this.craftingStore.getApi().getDonationQueue().get();
                new ExecuteDonationsJob(this.craftingStore, donationQueue);
            } catch (CraftingStoreApiException | InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
    }
}
