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
            if (!donations.isEmpty()) {
                // Wait x ms before executing the next command, so donations with a lot of commands will not create lag
                try {
                    int waitingTime = this.instance.getImplementation().getConfiguration().getTimeBetweenCommands();
                    this.instance.getLogger().debug(String.format("Waiting %dms before executing next donation", waitingTime));
                    Thread.sleep(waitingTime);
                    this.instance.getLogger().debug("Waiting done");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.instance.getLogger().debug(String.format("Executing donation %d", donation.getId()));
            this.instance.getLogger().debug(String.format("Command is '%s'", donation.getCommand()));
            boolean result = instance.getImplementation().executeDonation(donation);
            donations.put(donation, result);
            this.instance.getLogger().debug(String.format("Result of execution is %s", result));
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
        this.instance.getLogger().debug("Execution of ExecuteDonationsJob finished.");
    }
}
