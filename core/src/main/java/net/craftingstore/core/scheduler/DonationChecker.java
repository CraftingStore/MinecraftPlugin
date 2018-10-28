package net.craftingstore.core.scheduler;

import net.craftingstore.core.CraftingStore;
import net.craftingstore.core.exceptions.CraftingStoreApiException;
import net.craftingstore.core.models.donation.Donation;

import java.util.AbstractMap;
import java.util.Arrays;

public class DonationChecker implements Runnable {

    private CraftingStore instance;

    public DonationChecker(CraftingStore instance) {
        this.instance = instance;
    }

    public void run(){
        try {
            Donation[] donationQueue = instance.getApi().getDonationQueue();
            int[] completedIds = Arrays.stream(donationQueue)
                    .map(donation -> new AbstractMap.SimpleEntry<>(donation, instance.getImplementation().executeDonation(donation)))
                    .filter(AbstractMap.SimpleEntry::getValue)
                    .mapToInt(entry -> entry.getKey().getId())
                    .toArray();
            if (completedIds.length > 0) {
                instance.getApi().completeDonations(completedIds);
            }
        } catch (CraftingStoreApiException e) {
            e.printStackTrace();
        }
    }
}
