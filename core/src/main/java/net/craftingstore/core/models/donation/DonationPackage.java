package net.craftingstore.core.models.donation;

public class DonationPackage {

    private String name;
    private int price;

    public DonationPackage(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}
