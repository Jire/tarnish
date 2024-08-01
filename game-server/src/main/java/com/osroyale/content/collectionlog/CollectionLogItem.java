package com.osroyale.content.collectionlog;

import java.util.ArrayList;

public class CollectionLogItem {

    private CollectionLogData data;
    private int counter;
    private boolean claimed;
    private ArrayList<CollectionItem> items;

    public CollectionLogItem(CollectionLogData data) {
        this.data = data;
        this.counter = 0;
        this.claimed = false;
        this.items = new ArrayList<>();
    }

    public void addItem(int item, int amount) {
        for (int i = 0; i < items.size(); ++i) {
            if (items.get(i).getId() == item) {
                items.get(i).setAmount(items.get(i).getAmount() + amount);
                return;
            }
        }
        items.add(new CollectionItem(item, amount));
    }

    public CollectionLogData getData() {
        return this.data;
    }

    public ArrayList<CollectionItem> getItems() {
        return this.items;
    }

    public int getCounter() {
        return this.counter;
    }

    public boolean hasClaimed() {
        return this.claimed;
    }

    public void setClaimed(boolean claim) {
        this.claimed = claim;
    }

    public void setCounter(int count) {
        this.counter = count;
    }

}