package net.craftingstore.core.models.donation;

public class DonationPackage {

    private String name;
    private float price;

    public DonationPackage(String name, float price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    @Deprecated
    public int getPrice() {
        return (int) price;
    }

    public float getPriceFloat() {
        return price;
    }
}
