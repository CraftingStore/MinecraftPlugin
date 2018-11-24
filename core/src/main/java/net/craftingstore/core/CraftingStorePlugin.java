package net.craftingstore.core;

import net.craftingstore.core.models.donation.Donation;

import java.util.logging.Logger;

public interface CraftingStorePlugin {

    boolean executeDonation(Donation donation);

    Logger getLogger();

    void disable();

    void registerRunnable(Runnable runnable, int delay, int interval);

    String getToken();

    String getVersion();

    String getPlatform();
}
