package net.craftingstore.velocity.events;

import com.velocitypowered.api.event.ResultedEvent;
import net.craftingstore.core.models.donation.Donation;

public class DonationReceivedEvent implements ResultedEvent<DonationResult> {
    private Donation donation;
    private DonationResult result;

    public DonationReceivedEvent(Donation donation) {
        this.donation = donation;
    }

    public Donation getDonation() {
        return donation;
    }

    @Override
    public DonationResult getResult() {
        return result;
    }

    @Override
    public void setResult(DonationResult result) {
        result = result;
    }
}