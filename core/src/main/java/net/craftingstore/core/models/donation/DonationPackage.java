package net.craftingstore.core.models.donation;

import java.math.BigDecimal;

public class DonationPackage {

    private final String name;
    private final int priceInCents;

    public DonationPackage(String name, int priceInCents) {
        this.name = name;
        this.priceInCents = priceInCents;
    }

    public String getName() {
        return name;
    }

    @Deprecated
    public int getPrice() {
        return (int) Math.round(priceInCents / 100d);
    }

}
