package com.osroyale.content.tradingpost;

public class ItemHistory {
    private final int quantity;
    private final int itemId;
    private final int price;
    private final String seller;
    private final String buyer;

    public ItemHistory(int quantity, int itemId, int price, String seller, String buyer) {
        this.quantity = quantity;
        this.itemId = itemId;
        this.price = price;
        this.seller = seller;
        this.buyer = buyer;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getItemId() {
        return itemId;
    }

    public int getPrice() {
        return price;
    }

    public String getSeller() {
        return seller;
    }

    public String getBuyer() {
        return buyer;
    }
}
