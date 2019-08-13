package net.craftingstore.nukkit.events;

import cn.nukkit.event.Cancellable;
import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;
import net.craftingstore.core.models.donation.Donation;

import java.util.UUID;

public class DonationReceivedEvent extends Event implements Cancellable {
    @Deprecated
    private String command, username, packageName;
    @Deprecated
    private UUID uuid;
    @Deprecated
    private int packagePrice, couponDiscount;

    private Donation donation;

    private boolean cancelled = false;

    @Deprecated
    public DonationReceivedEvent(String command, String username, UUID uuid, String packageName, int packagePrice, int couponDiscount) {
        this.command = command;
        this.username = username;
        this.uuid = uuid;
        this.packageName = packageName;
        this.packagePrice = packagePrice;
        this.couponDiscount = couponDiscount;
    }

    public DonationReceivedEvent(Donation donation) {
        this.command = donation.getCommand();
        this.username = donation.getPlayer().getUsername();
        this.uuid = donation.getPlayer().getUUID();
        this.packageName = donation.getPackage().getName();
        this.packagePrice = donation.getPackage().getPrice();
        this.couponDiscount = donation.getDiscount();
        this.donation = donation;
    }

    public Donation getDonation() {
        return this.donation;
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
        this.cancelled = cancelled;
    }

    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
