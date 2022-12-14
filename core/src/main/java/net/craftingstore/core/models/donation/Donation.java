package net.craftingstore.core.models.donation;

public class Donation {

    private final int commandId;
    private final int paymentId;
    private final String command;
    private final DonationPlayer player;
    private final DonationPackage donationPackage;
    private final int discount;

    public Donation(int commandId, int paymentId, String command, DonationPlayer player, DonationPackage donationPackage, int discount) {
        this.commandId = commandId;
        this.paymentId = paymentId;
        this.command = command;
        this.player = player;
        this.donationPackage = donationPackage;
        this.discount = discount;
    }

    @Deprecated
    public int getId() {
        return commandId;
    }

    public int getCommandId() {
        return commandId;
    }

    public int getPaymentId() {
        return paymentId;
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
