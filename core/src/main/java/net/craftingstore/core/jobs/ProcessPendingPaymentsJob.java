package net.craftingstore.core.jobs;

import net.craftingstore.core.CraftingStore;
import net.craftingstore.core.exceptions.CraftingStoreApiException;
import net.craftingstore.core.models.donation.Donation;

import java.util.Map;

public class ProcessPendingPaymentsJob {
    private CraftingStore instance;
    private String username;

    public ProcessPendingPaymentsJob(CraftingStore instance, String username) throws CraftingStoreApiException {
        this.instance = instance;
        this.username = username;
        this.execute();
    }

    private void execute() throws CraftingStoreApiException {
        instance.getLogger().debug("Executing ProcessPendingPaymentsJob.");

        Donation[] donations = instance.getPendingDonations().entrySet().stream()
                .filter(d -> d.getValue().getPlayer().getUsername().equals(this.username))
                .map(Map.Entry::getValue)
                .toArray(Donation[]::new);
        if (donations.length == 0) {
            return;
        }

        instance.getLogger().debug("Executing pending donations for player " + this.username);
        new ExecuteDonationsJob(instance, donations);
    }
}
