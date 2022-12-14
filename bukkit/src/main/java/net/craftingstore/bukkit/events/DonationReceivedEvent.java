package net.craftingstore.bukkit.events;

import net.craftingstore.core.models.donation.Donation;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class DonationReceivedEvent extends Event implements Cancellable {

    @Deprecated
    private final String command;
    @Deprecated
    private final String username;
    @Deprecated
    private final UUID uuid;
    @Deprecated
    private final String packageName;
    @Deprecated
    private final int packagePrice;
    @Deprecated
    private final int couponDiscount;

    private Donation donation;

    private boolean cancelled = false;

    @Deprecated
    public DonationReceivedEvent(String command, String username, UUID uuid, String packageName, int packagePrice, int couponDiscount) {
        super(true);
        this.command = command;
        this.username = username;
        this.uuid = uuid;
        this.packageName = packageName;
        this.packagePrice = packagePrice;
        this.couponDiscount = couponDiscount;
    }

    public DonationReceivedEvent(Donation donation) {
        super(true);
        command = donation.getCommand();
        username = donation.getPlayer().getUsername();
        uuid = donation.getPlayer().getUUID();
        packageName = donation.getPackage().getName();
        packagePrice = donation.getPackage().getPrice();
        couponDiscount = donation.getDiscount();
        this.donation = donation;
    }

    public Donation getDonation() {
        return donation;
    }

    @Deprecated
    public String getCommand() {
        return command;
    }

    @Deprecated
    public String getUsername() {
        return username;
    }

    @Deprecated
    public UUID getUuid() {
        return uuid;
    }

    @Deprecated
    public String getPackageName() {
        return packageName;
    }

    @Deprecated
    public int getPackagePrice() {
        return packagePrice;
    }

    @Deprecated
    public int getCouponDiscount() {
        return couponDiscount;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        cancelled = cancelled;
    }

    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}