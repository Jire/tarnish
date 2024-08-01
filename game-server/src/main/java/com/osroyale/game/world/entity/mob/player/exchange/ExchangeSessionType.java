package com.osroyale.game.world.entity.mob.player.exchange;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 23-1-2017.
 */
public enum ExchangeSessionType {
    TRADE("trade"),
    DUEL("duel");

    public final String name;

    ExchangeSessionType(String name) {
        this.name = name;
    }
}
