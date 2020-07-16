package net.craftingstore.core.models.api;

import com.google.gson.annotations.SerializedName;

public class ApiDonation {
    @SerializedName("id")
    private int commandId;
    private int paymentId;
    private String command;
    @SerializedName("mcName")
    private String username;
    private String uuid;
    private String packageName;
    private boolean requireOnline;
    private int packagePriceCents;
    private int couponDiscount;

    public int getCommandId() {
        return commandId;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public String getCommand() {
        return command;
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

    public int getPackagePriceCents() {
        return packagePriceCents;
    }

    public int getCouponDiscount() {
        return couponDiscount;
    }
}
