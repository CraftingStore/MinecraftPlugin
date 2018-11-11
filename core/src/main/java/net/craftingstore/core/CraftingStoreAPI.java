package net.craftingstore.core;

import net.craftingstore.core.exceptions.CraftingStoreApiException;
import net.craftingstore.core.models.api.ApiCategory;
import net.craftingstore.core.models.api.ApiPayment;
import net.craftingstore.core.models.api.ApiTopDonator;
import net.craftingstore.core.models.api.Root;
import net.craftingstore.core.models.api.inventory.CraftingStoreInventory;
import net.craftingstore.core.models.api.misc.CraftingStoreInformation;
import net.craftingstore.core.models.donation.Donation;

public abstract class CraftingStoreAPI {

    protected String token;

    public abstract CraftingStoreInformation getInformation() throws CraftingStoreApiException;

    public abstract Root checkKey() throws CraftingStoreApiException;

    public abstract Donation[] getDonationQueue() throws CraftingStoreApiException;

    public abstract void completeDonations(int[] ids) throws CraftingStoreApiException;

    public abstract ApiPayment[] getPayments() throws CraftingStoreApiException;

    public abstract ApiCategory[] getCategories() throws CraftingStoreApiException;

    public abstract CraftingStoreInventory getGUI() throws CraftingStoreApiException;

    public abstract ApiTopDonator[] getTopDonators() throws CraftingStoreApiException;

    public String getToken() {
        return this.token;
    }

    protected void setToken(String token) {
        this.token = token;
    }
}
