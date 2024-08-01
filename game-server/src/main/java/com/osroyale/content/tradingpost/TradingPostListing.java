package com.osroyale.content.tradingpost;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.out.SendMessage;

public class TradingPostListing {
    private final int itemId;
    private final String seller;
    private int amountSold = 0;
    private int price;
    private int initialQuantity = 1;

    public TradingPostListing(int itemId, String seller) {
        this.itemId = itemId;
        this.seller = seller;
    }

    public int getItemId() {
        return itemId;
    }

    public int getInitialQuantity() {
        return initialQuantity;
    }

    public void setQuantity(Player player, int quantity) {
        this.initialQuantity = quantity;
        if (this.initialQuantity == 0) {
            this.initialQuantity = 1;
        }
        checkAndUpdate(player);
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(Player player, int price) {
        if ((long) price * initialQuantity > Integer.MAX_VALUE) {
            player.send(new SendMessage("Unable to set this price to this quantity. reason: above max integer value."));
            return;
        }
        this.price = price;
        player.tradingPost.updatePriceStrings();
    }

    public String getSeller() {
        return seller;
    }

    public void removeQuantity(Player player, int amount) {
        initialQuantity -= amount;
        if (initialQuantity <= 0) {
            initialQuantity = 1;
        }
        player.tradingPost.updateQuantityString();
        player.tradingPost.updatePriceStrings();
    }

    public void addQuantity(Player player, int amount) {
        initialQuantity += amount;
        checkAndUpdate(player);
    }

    public void checkAndUpdate(Player player) {
        int playerAmount = player.inventory.computeAmountForId(itemId);
        if (initialQuantity > playerAmount) {
            initialQuantity = playerAmount;
        }
        player.tradingPost.updateQuantityString();
        player.tradingPost.updatePriceStrings();
    }

    public int getAmountLeft() {
        return initialQuantity - amountSold;
    }

    public int getAmountSold() {
        return amountSold;
    }

    public void addToAmountSold(int amountSold) {
        this.amountSold += amountSold;
    }

    public void setAmountSold(int amountSold) {
        this.amountSold = amountSold;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setInitialQuantity(int initialQuantity) {
        this.initialQuantity = initialQuantity;
    }
}
