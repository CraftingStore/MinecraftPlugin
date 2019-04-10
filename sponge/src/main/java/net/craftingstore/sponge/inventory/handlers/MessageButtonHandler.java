package net.craftingstore.sponge.inventory.handlers;

import net.craftingstore.core.models.api.inventory.types.InventoryItemMessage;
import net.craftingstore.sponge.inventory.InventoryAttachment;
import net.craftingstore.sponge.inventory.InventoryItemHandler;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.net.MalformedURLException;
import java.net.URL;

public class MessageButtonHandler implements InventoryItemHandler<InventoryItemMessage> {
    @Override
    public void handle(Player p, InventoryItemMessage item, InventoryAttachment attachment) {
        for (String message : item.getMessages()) {
            Text text = TextSerializers.FORMATTING_CODE.deserialize(message);
            String url = null;
            if (message.contains("http://")) {
                url = message.substring(message.indexOf("http"));
            }
            if (message.contains("https://")) {
                url = message.substring(message.indexOf("https"));
            }
            if (url != null) {
                if (url.contains(" ")) {
                    url = url.substring(0, url.indexOf(" "));
                }
                try {
                    text = Text.builder()
                            .append(text)
                            .onClick(TextActions.openUrl(new URL(url)))
                            .build();
                } catch (MalformedURLException e) {
                    // Do nothing when url is invalid
                }
            }
            p.sendMessage(text);
        }
        if (item.shouldClose()) {
            p.closeInventory();
        }
    }
}
