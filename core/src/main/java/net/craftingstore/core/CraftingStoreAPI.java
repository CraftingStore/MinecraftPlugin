package net.craftingstore.core;

import net.craftingstore.core.exceptions.CraftingStoreApiException;
import net.craftingstore.core.models.api.*;
import net.craftingstore.core.models.api.misc.CraftingStoreInformation;
import net.craftingstore.core.models.donation.Donation;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public abstract class CraftingStoreAPI {

    protected String token;
    protected ExecutorService executor = Executors.newSingleThreadExecutor();

    public abstract Future<CraftingStoreInformation> getInformation() throws CraftingStoreApiException;

    public abstract Future<Root<?>> checkKey() throws CraftingStoreApiException;

    public abstract Future<Donation[]> getDonationQueue() throws CraftingStoreApiException;

    public abstract void completeDonations(int[] ids) throws CraftingStoreApiException;

    public abstract Future<ApiPayment[]> getPayments() throws CraftingStoreApiException;

    public abstract Future<ApiInventory> getGUI() throws CraftingStoreApiException;

    public abstract Future<ApiTopDonator[]> getTopDonators() throws CraftingStoreApiException;

    public abstract Future<ApiPackageInformation> getPackageInformation(
            String inGameName,
            UUID uuid,
            String ip,
            int packageId
    ) throws CraftingStoreApiException;

    public abstract Future<Boolean> createPayment(
            String inGameName,
            int price,
            int[] packages
    ) throws CraftingStoreApiException;

    public String getToken() {
        return token;
    }

    protected void setToken(String token) {
        this.token = token;
    }

}
