package net.craftingstore.core.jobs;

import net.craftingstore.core.CraftingStore;
import net.craftingstore.core.exceptions.CraftingStoreApiException;
import net.craftingstore.core.models.donation.Donation;

import java.util.*;
import java.util.stream.Stream;

public class ExecuteDonationsJob {
    private CraftingStore instance;
    private Donation[] donations;

    public ExecuteDonationsJob(CraftingStore instance, Donation[] donations) throws CraftingStoreApiException {
        this.instance = instance;
        this.donations = donations;
        this.execute();
    }

    private void execute() throws CraftingStoreApiException {
        this.instance.getLogger().debug("Executing ExecuteDonationsJob.");

        Map<Donation, Boolean> donations = new HashMap<>();
        for (Donation donation : this.donations) {
            donations.put(donation, instance.getImplementation().executeDonation(donation));
        }

        int[] completedIds = donations.entrySet().stream()
                .filter(Map.Entry::getValue)
                .mapToInt(entry -> entry.getKey().getId())
                .toArray();

        if (completedIds.length > 0) {
            this.instance.getLogger().debug("Marking executed donations as complete.");
            instance.getApi().completeDonations(completedIds);
        }

        for (Map.Entry<Donation, Boolean> entry : donations.entrySet()) {
            // Check if the donation has been executed and is in the cache
            if (entry.getValue() && instance.getPendingDonations().containsKey(entry.getKey().getId())) {
                instance.getPendingDonations().remove(entry.getKey().getId());
            } else if (!entry.getValue()){
                // Add the donation to the list of pending donations so it gets executed when the player gets online.
                instance.getPendingDonations().put(entry.getKey().getId(), entry.getKey());
            }
        }
        this.instance.getLogger().debug("Execution done.");
    }
}
