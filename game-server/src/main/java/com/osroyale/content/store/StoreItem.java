package com.osroyale.content.store;

import com.osroyale.content.store.currency.CurrencyType;
import com.osroyale.game.world.items.Item;

import java.util.Optional;
import java.util.OptionalInt;

/**
 * A simple wrapper class which holds extra attributes for the item object.
 *
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 4-1-2017.
 */
public final class StoreItem extends Item {

    /**
     * The time in minutes an item in the store has to wait before it can be reduced.
     */
    public static final int RESTOCK_RATE = 5;

    /**  Gets the optional value for this shop item.   */
    public OptionalInt value = OptionalInt.empty();

    /**  Gets the optional currency for this shop item. */
    public Optional<CurrencyType> currency = Optional.empty();

    /**
     * The time in minutes it takes to restock this store item.
     */
    private int restockTimer = RESTOCK_RATE;

    /** Creates a new {@link Item}. */
    public StoreItem(int id, int amount, OptionalInt value, Optional<CurrencyType> currency) {
        super(id, amount);
        this.value = value;
        this.currency = currency;
    }

    public StoreItem(int id, int amount, int value) {
        this(id, amount, OptionalInt.of(value), Optional.of(CurrencyType.COINS));
    }

    public StoreItem(int id, int amount) {
        this(id, amount, OptionalInt.empty(), Optional.empty());
    }

    public int getShopValue() {
        return value.orElse((int)((double)getValue() * 1.20));
    }

    public void setShopValue(int value) {
        this.value = OptionalInt.of(value);
    }

    public CurrencyType getShopCurrency(Store store) {
        return currency.orElse(store.currencyType);
    }

    public void setCurrency(CurrencyType type) {
        this.currency = currency;
    }

    public boolean canReduce() {
        if (--restockTimer <= 0) {
            restockTimer = RESTOCK_RATE;
            return true;
        }
        return false;
    }

    @Override
    public StoreItem copy() {
        return new StoreItem(getId(), getAmount(), value, currency);
    }
}
