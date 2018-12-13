package net.craftingstore.core.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.craftingstore.core.CraftingStore;
import net.craftingstore.core.CraftingStoreAPI;
import net.craftingstore.core.exceptions.CraftingStoreApiException;
import net.craftingstore.core.http.util.InformationAdapter;
import net.craftingstore.core.http.util.InventoryAdapter;
import net.craftingstore.core.http.util.JsonResponseHandler;
import net.craftingstore.core.models.api.*;
import net.craftingstore.core.models.api.inventory.CraftingStoreInventory;
import net.craftingstore.core.models.api.inventory.InventoryItem;
import net.craftingstore.core.models.api.misc.CraftingStoreInformation;
import net.craftingstore.core.models.api.provider.ProviderInformation;
import net.craftingstore.core.models.donation.Donation;
import net.craftingstore.core.models.donation.DonationPackage;
import net.craftingstore.core.models.donation.DonationPlayer;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.Arrays;

public class CraftingStoreAPIImpl extends CraftingStoreAPI {

    private final String BASE_URL = "https://api.craftingstore.net/v4/";
    private CraftingStore instance;
    private Gson gson;
    private HttpClient httpClient;

    public CraftingStoreAPIImpl(CraftingStore instance) {
        this.instance = instance;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ProviderInformation.class, new InformationAdapter());
        gsonBuilder.registerTypeAdapter(InventoryItem.class, new InventoryAdapter());
        this.gson = gsonBuilder.create();
        httpClient = HttpClients.createDefault();
    }

    public CraftingStoreInformation getInformation() throws CraftingStoreApiException {
        try {
            RequestBuilder requestBuilder = post("info");
            requestBuilder.addParameter("version", instance.getImplementation().getVersion());
            requestBuilder.addParameter("platform", instance.getImplementation().getPlatform());
            return httpClient.execute(requestBuilder.build(), new JsonResponseHandler<>(gson, CraftingStoreInformation.class));
        } catch (IOException e) {
            throw new CraftingStoreApiException("Info call failed", e);
        }
    }

    public Root checkKey() throws CraftingStoreApiException {
        try {
            return httpClient.execute(get("validateToken"), new JsonResponseHandler<>(gson, Root.class));
        } catch (IOException e) {
            throw new CraftingStoreApiException("Check Key call failed", e);
        }
    }

    public Donation[] getDonationQueue() throws CraftingStoreApiException {
        try {
            ApiDonation[] apiDonations = httpClient.execute(get("queue"), new JsonResponseHandler<>(gson, ApiDonation[].class));
            return Arrays.stream(apiDonations).map(apiDonation -> {
                DonationPlayer player = new DonationPlayer(apiDonation.getMcName(), apiDonation.getUuid(), apiDonation.getRequireOnline());
                DonationPackage donationPackage = new DonationPackage(apiDonation.getPackageName(), apiDonation.getPackagePrice());
                return new Donation(apiDonation.getId(), apiDonation.getCommand(), player, donationPackage, apiDonation.getCouponDiscount());
            }).toArray(Donation[]::new);
        } catch (IOException e) {
            throw new CraftingStoreApiException("Donation Queue call failed", e);
        }
    }

    public void completeDonations(int[] ids) throws CraftingStoreApiException {
        try {
            RequestBuilder requestBuilder = post("queue/markComplete");
            requestBuilder.addParameter("removeIds", gson.toJson(ids));
            httpClient.execute(requestBuilder.build());
        } catch (IOException e) {
            throw new CraftingStoreApiException("Complete Donations call failed", e);
        }
    }

    public ApiPayment[] getPayments() throws CraftingStoreApiException {
        try {
            return httpClient.execute(get("buyers/recent"), new JsonResponseHandler<>(gson, ApiPayment[].class));
        } catch (IOException e) {
            throw new CraftingStoreApiException("Payments call failed", e);
        }
    }

    @Deprecated
    public ApiCategory[] getCategories() throws CraftingStoreApiException {
        try {
            return httpClient.execute(get("plugin/inventory"), new JsonResponseHandler<>(gson, ApiCategory[].class));
        } catch (IOException e) {
            throw new CraftingStoreApiException("Categories call failed", e);
        }
    }

    public CraftingStoreInventory getGUI() throws CraftingStoreApiException {
        try {
            return httpClient.execute(get("plugin/gui"), new JsonResponseHandler<>(gson, CraftingStoreInventory.class));
        } catch (IOException e) {
            throw new CraftingStoreApiException("Inventory call failed", e);
        }
    }

    public ApiTopDonator[] getTopDonators() throws CraftingStoreApiException {
        try {
            return httpClient.execute(get("buyers/top"), new JsonResponseHandler<>(gson, ApiTopDonator[].class));
        } catch (IOException e) {
            throw new CraftingStoreApiException("Top Donators call failed", e);
        }
    }

    private HttpUriRequest get(String endpoint) {
        return RequestBuilder.get(BASE_URL + endpoint)
                .addHeader("token", this.token)
                .addHeader("version", this.instance.getImplementation().getVersion())
                .addHeader("platform", this.instance.getImplementation().getPlatform())
                .build();
    }

    private RequestBuilder post(String endpoint) {
        return RequestBuilder.post(BASE_URL + endpoint)
                .addHeader("token", this.token)
                .addHeader("version", this.instance.getImplementation().getVersion())
                .addHeader("platform", this.instance.getImplementation().getPlatform());
    }
}
