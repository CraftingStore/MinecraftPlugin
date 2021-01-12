package net.craftingstore.core.exceptions;

import java.io.IOException;

public class CraftingStoreApiException extends Exception {

    public CraftingStoreApiException(String s, IOException e) {
        super(s, e);
    }

    @Override
    public void printStackTrace() {
        super.getCause().printStackTrace();
    }
}
