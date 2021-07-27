package net.craftingstore.core.models.api;

public class RootV7<T> {

    private T data;
    private boolean success;
    private String message;

    public T getData() {
        return data;
    }

    public boolean isSuccess() {
        return success;
    }

}
