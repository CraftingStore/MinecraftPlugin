package net.craftingstore.hytale.events;

import com.hypixel.hytale.event.ICancellable;
import com.hypixel.hytale.event.IEvent;
import net.craftingstore.core.models.donation.Donation;

public class DonationReceivedEvent implements IEvent<String>, ICancellable {

    private Donation donation;

    private boolean cancelled = false;

    public DonationReceivedEvent(Donation donation) {
        this.donation = donation;
    }

    public Donation getDonation() {
        return donation;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}