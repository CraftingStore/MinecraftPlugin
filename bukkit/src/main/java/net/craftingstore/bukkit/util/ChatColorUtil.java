package net.craftingstore.bukkit.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatColorUtil {
    private static final Pattern HEX_PATTERN = Pattern.compile("&#(\\w{5}[0-9a-f])", Pattern.CASE_INSENSITIVE);

    public static String translate(String textToTranslate) {
        if (VersionUtil.isHexAvailable()) {
            return translateHexCodes(textToTranslate);
        }
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', textToTranslate);
    }

    private static String translateHexCodes(String textToTranslate) {
        Matcher matcher = HEX_PATTERN.matcher(textToTranslate);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            matcher.appendReplacement(buffer, net.md_5.bungee.api.ChatColor.of("#" + matcher.group(1)).toString());
        }

        return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());
    }
}
