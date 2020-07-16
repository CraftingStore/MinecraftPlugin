package net.craftingstore.core.models.donation;

import java.math.BigDecimal;

public class DonationPackage {

    private String name;
    private BigDecimal price;

    public DonationPackage(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    @Deprecated
    public int getPrice() {
        return price.intValue();
    }

    public BigDecimal getPriceDecimal() {
        return price;
    }
}
