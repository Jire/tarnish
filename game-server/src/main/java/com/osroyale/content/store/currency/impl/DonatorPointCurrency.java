package com.osroyale.content.store.currency.impl;

import com.osroyale.content.store.currency.Currency;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.out.SendMessage;

public final class DonatorPointCurrency implements Currency {

    @Override
    public boolean tangible() {
        return false;
    }

    @Override
    public boolean takeCurrency(Player player, int amount) {
        if (player.donation.getCredits() >= amount) {
            player.donation.setCredits(player.donation.getCredits() - amount);
            return true;
        } else {
            player.send(new SendMessage("You do not have enough donator points."));
            return false;
        }
    }

    @Override
    public void recieveCurrency(Player player, int amount) {
        player.donation.setCredits(player.donation.getCredits() + amount);
    }

    @Override
    public int currencyAmount(Player player) {
        return player.donation.getCredits();
    }

    @Override
    public boolean canRecieveCurrency(Player player) {
        return true;
    }
}
