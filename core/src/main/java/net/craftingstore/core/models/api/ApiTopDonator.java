package net.craftingstore.core.models.api;

public class ApiTopDonator {

    private String mcName;
    private float total;
    private String uuid;

    public String getUsername() {
        return mcName;
    }

    public float getTotal() {
        return total;
    }

    public String getUuid() {
        return uuid;
    }
}
