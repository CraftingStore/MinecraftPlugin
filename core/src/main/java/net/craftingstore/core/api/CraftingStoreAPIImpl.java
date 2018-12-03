package net.craftingstore.core.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpMethod;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.HttpRequestWithBody;
import net.craftingstore.core.CraftingStore;
import net.craftingstore.core.CraftingStoreAPI;
import net.craftingstore.core.api.util.GenericOf;
import net.craftingstore.core.api.util.InformationAdapter;
import net.craftingstore.core.api.util.InventoryAdapter;
import net.craftingstore.core.exceptions.CraftingStoreApiException;
import net.craftingstore.core.models.api.*;
import net.craftingstore.core.models.api.inventory.CraftingStoreInventory;
import net.craftingstore.core.models.api.inventory.InventoryItem;
import net.craftingstore.core.models.api.misc.CraftingStoreInformation;
import net.craftingstore.core.models.api.provider.ProviderInformation;
import net.craftingstore.core.models.donation.Donation;
import net.craftingstore.core.models.donation.DonationPackage;
import net.craftingstore.core.models.donation.DonationPlayer;

import java.util.Arrays;

public class CraftingStoreAPIImpl extends CraftingStoreAPI {

    //private final String BASE_URL = "http://localhost/craftingstore.php?action=";
    private final String BASE_URL = "https://api.craftingstore.net/v4/";
    private CraftingStore instance;
    private Gson gson;

    public CraftingStoreAPIImpl(CraftingStore instance) {
        this.instance = instance;
        setup();
    }

    public CraftingStoreInformation getInformation() throws CraftingStoreApiException {
        try {
            HttpRequestWithBody request = getHttpRequestWithBody("info");
            request.field("version", instance.getImplementation().getVersion());
            request.field("platform", instance.getImplementation().getPlatform());
            return request.asObject(CraftingStoreInformation.class).getBody();
        } catch (UnirestException e) {
            throw new CraftingStoreApiException("Info call failed", e);
        }
    }

    public Root checkKey() throws CraftingStoreApiException {
        try {
            return getHttpRequest("validateToken").asObject(Root.class).getBody();
        } catch (UnirestException e) {
            throw new CraftingStoreApiException("Check Key call failed", e);
        }
    }

    public Donation[] getDonationQueue() throws CraftingStoreApiException {
        try {
            ApiDonation[] apiDonations = getHttpRequest("queue").asObject(ApiDonation[].class).getBody();
            return Arrays.stream(apiDonations).map(apiDonation -> {
                DonationPlayer player = new DonationPlayer(apiDonation.getMcName(), apiDonation.getUuid(), apiDonation.getRequireOnline());
                DonationPackage donationPackage = new DonationPackage(apiDonation.getPackageName(), apiDonation.getPackagePrice());
                return new Donation(apiDonation.getId(), apiDonation.getCommand(), player, donationPackage, apiDonation.getCouponDiscount());
            }).toArray(Donation[]::new);
        } catch (UnirestException e) {
            throw new CraftingStoreApiException("Donation Queue call failed", e);
        }
    }

    public void completeDonations(int[] ids) throws CraftingStoreApiException {
        try {
            HttpRequestWithBody request = getHttpRequestWithBody("queue/markComplete");
            request.field("removeIds", gson.toJson(ids));
            request.asJson();
        } catch (UnirestException e) {
            throw new CraftingStoreApiException("Complete Donations call failed", e);
        }
    }

    public ApiPayment[] getPayments() throws CraftingStoreApiException {
        try {
            return getHttpRequest("buyers/recent").asObject(ApiPayment[].class).getBody();
        } catch (UnirestException e) {
            throw new CraftingStoreApiException("Payments call failed", e);
        }
    }

    @Deprecated
    public ApiCategory[] getCategories() throws CraftingStoreApiException {
        try {
            return getHttpRequest("plugin/inventory").asObject(ApiCategory[].class).getBody();
        } catch (UnirestException e) {
            throw new CraftingStoreApiException("Categories call failed", e);
        }
    }

    public CraftingStoreInventory getGUI() throws CraftingStoreApiException {
        try {
            return getHttpRequest("plugin/gui").asObject(CraftingStoreInventory.class).getBody();
        } catch (UnirestException e) {
            throw new CraftingStoreApiException("Inventory call failed", e);
        }
    }

    public ApiTopDonator[] getTopDonators() throws CraftingStoreApiException {
        try {
            return getHttpRequest("buyers/top").asObject(ApiTopDonator[].class).getBody();
        } catch (UnirestException e) {
            throw new CraftingStoreApiException("Top Donators call failed", e);
        }
    }

    private void setup() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ProviderInformation.class, new InformationAdapter());
        gsonBuilder.registerTypeAdapter(InventoryItem.class, new InventoryAdapter());
        this.gson = gsonBuilder.create();
        Unirest.setObjectMapper(new ObjectMapper() {
            public <T> T readValue(String value, Class<T> aClass) {
                if (aClass == Root.class) {
                    return gson.fromJson(value, aClass);
                }
                Root<T> result = gson.fromJson(value, new GenericOf<>(Root.class, aClass));
                return result.getResult();
            }

            public String writeValue(Object o) {
                return gson.toJson(o);
            }
        });
    }

    private HttpRequest getHttpRequest(String endpoint) {
        return new HttpRequest(HttpMethod.GET, BASE_URL + endpoint)
                .header("token", this.token)
                .header("version", this.instance.getImplementation().getVersion())
                .header("platform", this.instance.getImplementation().getPlatform());
    }

    private HttpRequestWithBody getHttpRequestWithBody(String endpoint) {
        return new HttpRequestWithBody(HttpMethod.POST, BASE_URL + endpoint)
                .header("token", this.token)
                .header("version", this.instance.getImplementation().getVersion())
                .header("platform", this.instance.getImplementation().getPlatform());
    }
}
