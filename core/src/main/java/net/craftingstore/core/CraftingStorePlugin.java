package net.craftingstore.core;

import net.craftingstore.core.logging.CraftingStoreLogger;
import net.craftingstore.core.models.donation.Donation;

public interface CraftingStorePlugin {

    boolean executeDonation(Donation donation);

    CraftingStoreLogger getLogger();

    void registerRunnable(Runnable runnable, int delay, int interval);

    void runAsyncTask(Runnable runnable);

    String getToken();

    PluginConfiguration getConfiguration();
}
