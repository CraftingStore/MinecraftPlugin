package net.craftingstore.nukkit.events;

import cn.nukkit.event.Cancellable;
import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;
import net.craftingstore.core.models.donation.Donation;

import java.util.UUID;

public class DonationReceivedEvent extends Event implements Cancellable {
    private Donation donation;

    private boolean cancelled = false;

    public DonationReceivedEvent(Donation donation) {
        this.donation = donation;
    }

    public Donation getDonation() {
        return this.donation;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
