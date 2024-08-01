package com.osroyale.content.store;

/**
 * Represents ways items can be sold in a shop.
 *
 * @author nshusa
 */
public enum SellType {

    /**
     * No items can be sold in the shop.
     */
    NONE,

    /**
     * Can only sell items that are contained in the shop.
     */
    CONTAINS,

    /**
     * Can sell any items to the shop.
     */
    ANY,

}
