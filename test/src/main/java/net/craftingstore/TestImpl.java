package net.craftingstore;

import net.craftingstore.core.CraftingStorePlugin;
import net.craftingstore.core.models.api.inventory.CraftingStoreInventory;
import net.craftingstore.core.models.donation.Donation;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

public class TestImpl implements CraftingStorePlugin {

    private Timer timer;

    public TestImpl() {
        timer = new Timer();
    }

    public boolean executeDonation(Donation donation) {
        System.out.println("Executing command " + donation.getCommand());
        return true;
    }

    public void openInventory(CraftingStoreInventory inventory) {

    }

    public Logger getLogger() {
        return Logger.getLogger("Test Logger");
    }

    public void disable() {
        timer.cancel();
    }

    public void registerRunnable(final Runnable runnable, int delay, int interval) {
        getLogger().info("Scheduling runnable");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getLogger().info("Executing runnable");
                runnable.run();
            }
        }, delay * 1000, interval * 1000);
    }

    public String getToken() {
        return null;
    }

    public String getVersion() {
        return "test";
    }

    public String getPlatform() {
        return "test";
    }
}
