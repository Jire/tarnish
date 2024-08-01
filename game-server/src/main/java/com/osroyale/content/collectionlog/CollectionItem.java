package com.osroyale.content.collectionlog;

public class CollectionItem {

    private int id;
    private int amount;

    public CollectionItem(int item, int amount) {
        this.id = item;
        this.amount = amount;
    }

    public int getId() {
        return this.id;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

}