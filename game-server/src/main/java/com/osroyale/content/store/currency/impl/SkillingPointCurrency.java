package com.osroyale.content.store.currency.impl;

import com.osroyale.content.store.currency.Currency;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.out.SendMessage;

public final class SkillingPointCurrency implements Currency {

    @Override
    public boolean tangible() {
        return false;
    }

    @Override
    public boolean takeCurrency(Player player, int amount) {
        if (player.skillingPoints >= amount) {
            player.skillingPoints -= amount;
            return true;
        } else {
            player.send(new SendMessage("You do not have enough skilling points."));
            return false;
        }
    }

    @Override
    public void recieveCurrency(Player player, int amount) {
        player.skillingPoints += amount;
    }

    @Override
    public int currencyAmount(Player player) {
        return player.skillingPoints;
    }

    @Override
    public boolean canRecieveCurrency(Player player) {
        return true;
    }
}
