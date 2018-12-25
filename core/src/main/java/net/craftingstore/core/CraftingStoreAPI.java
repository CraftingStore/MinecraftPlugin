package net.craftingstore.core;

import net.craftingstore.core.exceptions.CraftingStoreApiException;
import net.craftingstore.core.models.api.ApiCategory;
import net.craftingstore.core.models.api.ApiPayment;
import net.craftingstore.core.models.api.ApiTopDonator;
import net.craftingstore.core.models.api.Root;
import net.craftingstore.core.models.api.inventory.CraftingStoreInventory;
import net.craftingstore.core.models.api.misc.CraftingStoreInformation;
import net.craftingstore.core.models.donation.Donation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public abstract class CraftingStoreAPI {

    protected String token;
    protected ExecutorService executor = Executors.newSingleThreadExecutor();

    public abstract Future<CraftingStoreInformation> getInformation() throws CraftingStoreApiException;

    public abstract Future<Root> checkKey() throws CraftingStoreApiException;

    public abstract Future<Donation[]> getDonationQueue() throws CraftingStoreApiException;

    public abstract void completeDonations(int[] ids) throws CraftingStoreApiException;

    public abstract Future<ApiPayment[]> getPayments() throws CraftingStoreApiException;

    public abstract Future<ApiCategory[]> getCategories() throws CraftingStoreApiException;

    public abstract Future<CraftingStoreInventory> getGUI() throws CraftingStoreApiException;

    public abstract Future<ApiTopDonator[]> getTopDonators() throws CraftingStoreApiException;

    public String getToken() {
        return this.token;
    }

    protected void setToken(String token) {
        this.token = token;
    }

    public void shutdown() {
        executor.shutdown();
    }
}
