package net.craftingstore;

import net.craftingstore.core.CraftingStorePlugin;
import net.craftingstore.core.models.api.inventory.CraftingStoreInventory;
import net.craftingstore.core.models.donation.Donation;

import java.util.logging.Logger;

public class TestImpl implements CraftingStorePlugin {

    public boolean executeDonation(Donation donation) {
        System.out.println("Executing command " + donation.getCommand());
        return true;
    }

    public void openInventory(CraftingStoreInventory inventory) {

    }

    public Logger getLogger() {
        return null;
    }

    public void disable() {

    }

    public void registerRunnable(Runnable runnable, int delay, int interval) {
        System.out.println("Executing runnable instantly");
        runnable.run();
    }

    public String getToken() {
        return null;
    }
}
