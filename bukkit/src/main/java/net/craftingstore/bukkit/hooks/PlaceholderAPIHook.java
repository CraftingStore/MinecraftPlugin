package net.craftingstore.bukkit.hooks;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderHook;
import net.craftingstore.core.CraftingStore;
import net.craftingstore.core.exceptions.CraftingStoreApiException;
import net.craftingstore.core.models.api.ApiPayment;
import net.craftingstore.core.models.api.ApiTopDonator;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceholderAPIHook extends PlaceholderHook {

    private CraftingStore instance;

    public PlaceholderAPIHook(CraftingStore instance) {
        this.instance = instance;
        PlaceholderAPI.registerPlaceholderHook("craftingstore", this);
    }

    @Override
    public String onPlaceholderRequest(Player player, String s) {
        try {
            if (s.startsWith("donator")) {
                return handleDonators(player, s);
            } else if (s.startsWith("payment")) {
                return handlePayments(player, s);
            }
        } catch (CraftingStoreApiException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String handleDonators(Player player, String s) throws CraftingStoreApiException {
        if (instance.getApi().getTopDonators() == null) {
            return null; // Donators are not retrieved yet.
        } else if (s.equalsIgnoreCase("donator")) {
            StringBuilder builder = new StringBuilder();
            for (ApiTopDonator donator : instance.getApi().getTopDonators()) {
                builder.append(donator.getUsername()).append(": ").append(donator.getTotal()).append(", ");
            }
            builder.substring(0, builder.length() - 2); // Remove the last ', ' from the string
            return builder.toString();
        } else if (s.startsWith("donator_")) {
            Pattern pattern = Pattern.compile("donator_([1-5])");
            Matcher matcher = pattern.matcher(s);
            if (matcher.matches()) {
                int id = Integer.parseInt(matcher.group(1));
                if (instance.getApi().getTopDonators().length >= id) {
                    id--; // Zero based array
                    ApiTopDonator donator = instance.getApi().getTopDonators()[id];
                    return donator.getUsername() + ": " + donator.getTotal();
                }
            }
        }
        return null;
    }

    private String handlePayments(Player player, String s) throws CraftingStoreApiException {
        if (instance.getApi().getPayments() == null) {
            return null; // Recent payments are not retrieved yet.
        } else if (s.equalsIgnoreCase("payment")) {
            StringBuilder builder = new StringBuilder();
            for (ApiPayment payment : instance.getApi().getPayments()) {
                builder.append(payment.getUsername()).append(": ").append(payment.getPackageName()).append(", ");
            }
            builder.substring(0, builder.length() - 2); // Remove the last ', ' from the string
            return builder.toString();
        } else if (s.startsWith("payment_")) {
            Pattern pattern = Pattern.compile("payment_([1-5])");
            Matcher matcher = pattern.matcher(s);
            if (matcher.matches()) {
                int id = Integer.parseInt(matcher.group(1));
                if (instance.getApi().getPayments().length >= id) {
                    id--; // Zero based array
                    ApiPayment payment = instance.getApi().getPayments()[id];
                    return payment.getUsername() + ": " + payment.getPackageName();
                }
            }
        }
        return null;
    }
}
