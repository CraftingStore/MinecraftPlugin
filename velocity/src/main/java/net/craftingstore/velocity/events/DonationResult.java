package net.craftingstore.velocity.events;

import com.velocitypowered.api.event.ResultedEvent;

public class DonationResult implements ResultedEvent.Result {

    private final boolean allowed;

    public DonationResult(boolean allowed) {
        this.allowed = allowed;
    }

    @Override
    public boolean isAllowed() {
        return allowed;
    }
}
