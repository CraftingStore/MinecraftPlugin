package net.craftingstore.bukkit.util;

public class VersionUtil {
    private static boolean hexAvailable = false;
    private static boolean customModalDataAvailable = false;

    static {
        try {
            Class.forName("net.md_5.bungee.api.ChatColor").getMethod("of", String.class);
            hexAvailable = true;
        } catch (NoSuchMethodException | ClassNotFoundException ignored) {
        }
        try {
            Class.forName("org.bukkit.inventory.meta.ItemMeta").getMethod("hasCustomModelData");
            customModalDataAvailable = true;
        } catch (NoSuchMethodException | ClassNotFoundException ignored) {
        }
    }

    public static boolean isHexAvailable() {
        return hexAvailable;
    }

    public static boolean isCustomModalDataAvailable() {
        return customModalDataAvailable;
    }
}
