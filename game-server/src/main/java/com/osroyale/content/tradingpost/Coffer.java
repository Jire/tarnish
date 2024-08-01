package com.osroyale.content.tradingpost;

public class Coffer {
    private final String owner;
    private long amount;

    public Coffer(String owner) {
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public void addAmount(int amount) {
        this.amount += amount;
    }

    public void subtractAmount(int amount) {
        this.amount -= amount;
    }
}
