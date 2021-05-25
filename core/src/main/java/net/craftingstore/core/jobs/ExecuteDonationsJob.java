package net.craftingstore.core.jobs;

import net.craftingstore.core.CraftingStore;
import net.craftingstore.core.exceptions.CraftingStoreApiException;
import net.craftingstore.core.models.donation.Donation;

import java.util.*;
import java.util.stream.Stream;

public class ExecuteDonationsJob {

    public static final int CHUNK_SIZE = 25;

    private CraftingStore instance;
    private Donation[] donations;

    public ExecuteDonationsJob(CraftingStore instance, Donation[] donations) throws CraftingStoreApiException {
        this.instance = instance;
        this.donations = donations;
        this.execute();
    }

    private void execute() throws CraftingStoreApiException {
        this.instance.getLogger().debug(String.format("Executing ExecuteDonationsJob for %d donations.", this.donations.length));

        Map<Donation, Boolean> donations = new HashMap<>();
        for (int i = 0; i < this.donations.length; i++) {
            Donation donation = this.donations[i];
            this.instance.getLogger().debug(String.format("Executing donation #%d with command id #%d", donation.getPaymentId(), donation.getCommandId()));
            this.instance.getLogger().debug(String.format("Command is '%s'", donation.getCommand()));
            boolean result = instance.getImplementation().executeDonation(donation);
            donations.put(donation, result);
            this.instance.getLogger().debug(String.format("Result of execution is %s", result));

            if (i < (this.donations.length - 1)) {
                if (!result) {
                    this.instance.getLogger().debug("Not delaying command execution because last command did not successfully execute.");
                    continue;
                }

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
            } else if (!entry.getValue()) {
                // Add the donation to the list of pending donations so it gets executed when the player gets online.
                instance.getPendingDonations().put(entry.getKey().getId(), entry.getKey());
            }
        }
        this.instance.getLogger().debug("Execution of ExecuteDonationsJob finished.");
    }
}
