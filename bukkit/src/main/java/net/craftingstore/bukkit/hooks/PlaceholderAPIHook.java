package net.craftingstore.bukkit.hooks;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.craftingstore.core.CraftingStore;
import net.craftingstore.core.exceptions.CraftingStoreApiException;
import net.craftingstore.core.models.api.ApiPayment;
import net.craftingstore.core.models.api.ApiTopDonator;
import org.bukkit.OfflinePlayer;

import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceholderAPIHook extends PlaceholderExpansion {

    private CraftingStore instance;

    public PlaceholderAPIHook(CraftingStore instance) {
        this.instance = instance;
        this.register(); // Documentation is missing so we are using this method.
    }

    @Override
    public String onRequest(OfflinePlayer player, String s) {
        try {
            if (s.startsWith("donator")) {
                return handleDonators(player, s);
            } else if (s.startsWith("payment")) {
                return handlePayments(player, s);
            }
        } catch (CraftingStoreApiException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String handleDonators(OfflinePlayer player, String s) throws CraftingStoreApiException, ExecutionException, InterruptedException {
        ApiTopDonator[] topDonators = instance.getApi().getTopDonators().get();
        if (topDonators == null) {
            return ""; // Donators are not retrieved yet.
        } else if (s.equalsIgnoreCase("donator")) {
            StringBuilder builder = new StringBuilder();
            for (ApiTopDonator donator : topDonators) {
                builder.append(donator.getUsername()).append(": ").append(donator.getTotal()).append(", ");
            }
            builder.substring(0, builder.length() - 2); // Remove the last ', ' from the string
            return builder.toString();
        } else if (s.startsWith("donator_")) {
            Pattern pattern = Pattern.compile("donator_([1-5])");
            Matcher matcher = pattern.matcher(s);
            if (matcher.matches()) {
                int id = Integer.parseInt(matcher.group(1));
                if (topDonators.length >= id) {
                    id--; // Zero based array
                    ApiTopDonator donator = topDonators[id];
                    return donator.getUsername() + ": " + donator.getTotal();
                }
            }
        }
        return "";
    }

    private String handlePayments(OfflinePlayer player, String s) throws CraftingStoreApiException, ExecutionException, InterruptedException {
        ApiPayment[] payments = instance.getApi().getPayments().get();
        if (payments == null || payments.length == 0) {
            return ""; // Recent payments are not retrieved yet or there are no payments.
        } else if (s.equalsIgnoreCase("payment")) {
            StringBuilder builder = new StringBuilder();
            for (ApiPayment payment : payments) {
                builder.append(payment.getUsername()).append(": ").append(payment.getPackageName()).append(", ");
            }
            builder.substring(0, builder.length() - 2); // Remove the last ', ' from the string
            return builder.toString();
        } else if (s.startsWith("payment_")) {
            Pattern pattern = Pattern.compile("payment_([1-5])");
            Matcher matcher = pattern.matcher(s);
            if (matcher.matches()) {
                int id = Integer.parseInt(matcher.group(1));
                if (payments.length >= id) {
                    id--; // Zero based array
                    ApiPayment payment = payments[id];
                    return payment.getUsername() + ": " + payment.getPackageName();
                }
            }
        }
        return "";
    }

    @Override
    public String getIdentifier() {
        return "craftingstore";
    }

    @Override
    public String getAuthor() {
        return "CraftingStore";
    }

    @Override
    public String getVersion() {
        return this.instance.getImplementation().getConfiguration().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }
}
