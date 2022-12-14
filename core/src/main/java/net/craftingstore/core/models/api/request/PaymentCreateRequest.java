package net.craftingstore.core.models.api.request;

public class PaymentCreateRequest {
    String inGameName;
    String notes;
    String gateway;
    int inGamePricePaid;
    Integer pricePaid;
    boolean executeCommands;
    int[] packages;

    public PaymentCreateRequest( String inGameName, String notes, String gateway, int inGamePricePaid, Integer pricePaid, boolean executeCommands, int[] packages) {
        this.inGameName = inGameName;
        this.notes = notes;
        this.gateway = gateway;
        this.inGamePricePaid = inGamePricePaid;
        this.pricePaid = pricePaid;
        this.executeCommands = executeCommands;
        this.packages = packages;
    }
}
