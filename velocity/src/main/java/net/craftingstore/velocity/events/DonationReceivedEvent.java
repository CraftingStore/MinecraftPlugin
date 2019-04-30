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
        return this.donation;
    }

    @Override
    public DonationResult getResult() {
        return this.result;
    }

    @Override
    public void setResult(DonationResult result) {
        this.result = result;
    }
}