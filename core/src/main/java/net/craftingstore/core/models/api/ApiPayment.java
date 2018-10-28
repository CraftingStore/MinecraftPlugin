package net.craftingstore.core.models.api;

import com.google.gson.annotations.SerializedName;

public class ApiPayment {

    @SerializedName("playerName")
    private String username;

    private String gateway;
    private long timestamp;
    private String discount;
    private String coupon;
    private String price;

    private int packageId;
    private String packageName;

    public String getUsername() {
        return username;
    }

    public String getGateway() {
        return gateway;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getDiscount() {
        return discount;
    }

    public String getCoupon() {
        return coupon;
    }

    public String getPrice() {
        return price;
    }

    public String getPackageName() {
        return packageName;
    }

    public int getPackageId() {
        return packageId;
    }
}
