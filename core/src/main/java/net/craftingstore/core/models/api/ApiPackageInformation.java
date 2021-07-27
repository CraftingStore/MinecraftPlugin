package net.craftingstore.core.models.api;

public class ApiPackageInformation {
    private boolean mayBuy;
    private int price;
    private String message;

    public boolean isAllowedToBuy() {
        return mayBuy;
    }

    public int getPrice() {
        return price;
    }

    public String getMessage() {
        return message;
    }
}
