package com.osroyale.game.world.items.containers.bank;

/**
 * All the bank vault currency types.
 *
 * @author Daniel
 */
public enum VaultCurrency {
    COINS("Coins", 995),
    BLOOD_MONEY("Blood Money", 13307);

    public final String name;
    public final int id;

    VaultCurrency(String name, int id) {
        this.name = name;
        this.id = id;
    }

}
