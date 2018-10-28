package net.craftingstore.core.models.api.misc;

public class UpdateInformation {

    private String message;
    private boolean disable;

    public String getMessage() {
        return message;
    }

    public boolean shouldDisable() {
        return disable;
    }
}
