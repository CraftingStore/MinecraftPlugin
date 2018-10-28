package net.craftingstore.core.models.api;

import com.google.gson.annotations.SerializedName;

public class ApiDonation {
    private int id;
    private String command;
    @SerializedName("mcName")
    private String username;
    private String uuid;
    private String packageName;
    private boolean requireOnline;
    private int packagePrice;
    private int couponDiscount;

    public String getCommand() {
        return command;
    }

    public int getId() {
        return id;
    }

    public String getMcName() {
        return username;
    }

    public String getUuid() {
        return uuid;
    }

    public String getPackageName() {
        return packageName;
    }

    public boolean getRequireOnline() {
        return requireOnline;
    }

    public int getPackagePrice() {
        return packagePrice;
    }

    public int getCouponDiscount() {
        return couponDiscount;
    }
}
