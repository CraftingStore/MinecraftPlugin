package net.craftingstore.core.models.donation;

public class Donation {

    private int id;
    private String command;
    private DonationPlayer player;
    private DonationPackage donationPackage;
    private int discount;

    public Donation(int id, String command, DonationPlayer player, DonationPackage donationPackage, int discount) {
        this.id = id;
        this.command = command;
        this.player = player;
        this.donationPackage = donationPackage;
        this.discount = discount;
    }

    public int getId() {
        return id;
    }

    public String getCommand() {
        return command;
    }

    public DonationPlayer getPlayer() {
        return player;
    }

    public DonationPackage getPackage() {
        return donationPackage;
    }

    public int getDiscount() {
        return discount;
    }
}
