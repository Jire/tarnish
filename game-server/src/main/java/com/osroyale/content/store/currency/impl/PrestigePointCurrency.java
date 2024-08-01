package com.osroyale.content.store.currency.impl;

import com.osroyale.content.store.currency.Currency;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.out.SendMessage;

public final class PrestigePointCurrency implements Currency {

    @Override
    public boolean tangible() {
        return false;
    }

    @Override
    public boolean takeCurrency(Player player, int amount) {
        if (player.prestige.getPrestigePoint() >= amount) {
            player.prestige.setPrestigePoint(player.prestige.getPrestigePoint() - amount);
            return true;
        } else {
            player.send(new SendMessage("You do not have enough prestige points."));
            return false;
        }
    }

    @Override
    public void recieveCurrency(Player player, int amount) {
        player.prestige.setPrestigePoint(player.prestige.getPrestigePoint() + amount);
    }

    @Override
    public int currencyAmount(Player player) {
        return player.prestige.getPrestigePoint();
    }

    @Override
    public boolean canRecieveCurrency(Player player) {
        return true;
    }
}
