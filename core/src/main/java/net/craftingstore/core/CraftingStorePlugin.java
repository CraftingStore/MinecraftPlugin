package net.craftingstore.core;

import net.craftingstore.core.models.api.inventory.CraftingStoreInventory;
import net.craftingstore.core.models.donation.Donation;

import java.util.logging.Logger;

public interface CraftingStorePlugin {

    boolean executeDonation(Donation donation);

    void openInventory(CraftingStoreInventory inventory);

    Logger getLogger();

    void disable();

    void registerRunnable(Runnable runnable, int delay, int interval);

    String getToken();
}
