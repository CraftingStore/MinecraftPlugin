package net.craftingstore.core.models.donation;

import java.util.UUID;

public class DonationPlayer {

    private String username;
    private UUID uuid;
    private boolean requiredOnline;

    public DonationPlayer(String username, String uuid, boolean requiredOnline) {
        this.username = username;
        this.requiredOnline = requiredOnline;
        if (uuid != null && !uuid.isEmpty()) {
            uuid = uuid.replaceFirst("([0-9a-fA-F]{8})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]+)", "$1-$2-$3-$4-$5");
            this.uuid = UUID.fromString(uuid);
        }
    }

    public String getUsername() {
        return username;
    }

    public UUID getUUID() {
        return uuid;
    }

    public boolean isRequiredOnline() {
        return requiredOnline;
    }
}
