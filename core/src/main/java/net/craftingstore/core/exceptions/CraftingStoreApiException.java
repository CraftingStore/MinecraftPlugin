package net.craftingstore.core.exceptions;

import com.mashape.unirest.http.exceptions.UnirestException;

public class CraftingStoreApiException extends Exception {

    public CraftingStoreApiException(String s, UnirestException e) {
        super(s, e.getCause());
    }
}
