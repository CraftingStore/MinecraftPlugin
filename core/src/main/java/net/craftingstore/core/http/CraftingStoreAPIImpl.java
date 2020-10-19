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
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

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
        try {
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, null, null);
            httpClient = HttpClients.custom()
                    .setSSLContext(sslContext)
                    .build();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
    }

    public Future<CraftingStoreInformation> getInformation() throws CraftingStoreApiException {
        return executor.submit(() -> {
            try {
                HttpPost request = post("info");
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("version", instance.getImplementation().getConfiguration().getVersion()));
                params.add(new BasicNameValuePair("platform", instance.getImplementation().getConfiguration().getPlatform()));
                request.setEntity(new UrlEncodedFormEntity(params));
                return httpClient.execute(request, new JsonResponseHandler<>(gson, CraftingStoreInformation.class));
            } catch (IOException e) {
                throw new CraftingStoreApiException("Info call failed", e);
            }
        });
    }

    public Future<Root> checkKey() throws CraftingStoreApiException {
        return executor.submit(() -> {
            try {
                return httpClient.execute(get("validateToken"), new JsonResponseHandler<>(gson, Root.class));
            } catch (IOException e) {
                throw new CraftingStoreApiException("Check Key call failed", e);
            }
        });
    }

    public Future<Donation[]> getDonationQueue() throws CraftingStoreApiException {
        return executor.submit(() -> {
            try {
                ApiDonation[] apiDonations = httpClient.execute(get("queue"), new JsonResponseHandler<>(gson, ApiDonation[].class));
                return Arrays.stream(apiDonations).map(apiDonation -> {
                    DonationPlayer player = new DonationPlayer(apiDonation.getMcName(), apiDonation.getUuid(), apiDonation.getRequireOnline());
                    DonationPackage donationPackage = new DonationPackage(apiDonation.getPackageName(), apiDonation.getPackagePriceCents());
                    return new Donation(
                            apiDonation.getCommandId(),
                            apiDonation.getPaymentId(),
                            apiDonation.getCommand(),
                            player,
                            donationPackage,
                            apiDonation.getCouponDiscount()
                    );
                }).toArray(Donation[]::new);
            } catch (IOException e) {
                throw new CraftingStoreApiException("Donation Queue call failed", e);
            }
        });
    }

    public void completeDonations(int[] ids) throws CraftingStoreApiException {
        executor.submit(() -> {
            try {
                HttpPost request = post("queue/markComplete");
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("removeIds", gson.toJson(ids)));
                request.setEntity(new UrlEncodedFormEntity(params));
                HttpResponse response = httpClient.execute(request);
                EntityUtils.consumeQuietly(response.getEntity());
            } catch (IOException e) {
                instance.getLogger().error("Complete Donations call failed");
                e.printStackTrace();
            }
        });
    }

    public Future<ApiPayment[]> getPayments() throws CraftingStoreApiException {
        return executor.submit(() -> {
            try {
                return httpClient.execute(get("buyers/recent"), new JsonResponseHandler<>(gson, ApiPayment[].class));
            } catch (IOException e) {
                throw new CraftingStoreApiException("Payments call failed", e);
            }
        });
    }

    @Deprecated
    public Future<ApiCategory[]> getCategories() throws CraftingStoreApiException {
        return executor.submit(() -> {
            try {
                return httpClient.execute(get("plugin/inventory"), new JsonResponseHandler<>(gson, ApiCategory[].class));
            } catch (IOException e) {
                throw new CraftingStoreApiException("Categories call failed", e);
            }
        });
    }

    public Future<CraftingStoreInventory> getGUI() throws CraftingStoreApiException {
        return executor.submit(() -> {
            try {
                return httpClient.execute(get("plugin/gui"), new JsonResponseHandler<>(gson, CraftingStoreInventory.class));
            } catch (IOException e) {
                throw new CraftingStoreApiException("Inventory call failed", e);
            }
        });
    }

    public Future<ApiTopDonator[]> getTopDonators() throws CraftingStoreApiException {
        return executor.submit(() -> {
            try {
                return httpClient.execute(get("buyers/top"), new JsonResponseHandler<>(gson, ApiTopDonator[].class));
            } catch (IOException e) {
                throw new CraftingStoreApiException("Top Donators call failed", e);
            }
        });
    }

    private HttpGet get(String endpoint) {
        HttpGet request = new HttpGet(BASE_URL + endpoint);
        request.setConfig(RequestConfig.custom()
                .setSocketTimeout(10000)
                .setConnectTimeout(10000)
                .setConnectionRequestTimeout(10000)
                .build());
        addHeaders(request);
        return request;
    }

    private HttpPost post(String endpoint) {
        HttpPost request = new HttpPost(BASE_URL + endpoint);
        request.setConfig(RequestConfig.custom()
                .setSocketTimeout(10000)
                .setConnectTimeout(10000)
                .setConnectionRequestTimeout(10000)
                .build());
        addHeaders(request);
        return request;
    }

    private void addHeaders(HttpUriRequest request) {
        this.instance.getLogger().debug(request.getMethod() + " -> " + request.getURI());
        request.addHeader("token", this.token);
        request.addHeader("version", this.instance.getImplementation().getConfiguration().getVersion());
        request.addHeader("platform", this.instance.getImplementation().getConfiguration().getPlatform());
    }
}
