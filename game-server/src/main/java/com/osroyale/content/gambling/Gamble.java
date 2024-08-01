package com.osroyale.content.gambling;

import com.osroyale.game.world.entity.mob.player.Player;

public abstract class Gamble {

    /**
     * Players inside the 'gamble'
     */
    private Player host, opponent;

    public Player getHost() {
        return host;
    }

    public Player getOpponent() {
        return opponent;
    }

    /**
     * Score betwen the players inside the 'gamble'
     */
    public int hostScore, opponentScore;

    public Gamble(Player host, Player opponent) {
        this.host = host;
        this.opponent = opponent;
    }

    public abstract void gamble();

}
