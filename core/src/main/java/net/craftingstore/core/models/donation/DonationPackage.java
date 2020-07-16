package net.craftingstore.core.models.donation;

import java.math.BigDecimal;

public class DonationPackage {

    private String name;
    private int priceInCents;

    public DonationPackage(String name, int priceInCents) {
        this.name = name;
        this.priceInCents = priceInCents;
    }

    public String getName() {
        return this.name;
    }

    @Deprecated
    public int getPrice() {
        return (int) Math.round(this.priceInCents / 100d);
    }

    public BigDecimal getPriceDecimal() {
        return BigDecimal.valueOf(this.priceInCents / 100d);
    }

    public int getPriceInCents() {
        return this.priceInCents;
    }
}
