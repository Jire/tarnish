package com.osroyale.content.store.currency.impl;

import com.osroyale.content.clanchannel.channel.ClanChannel;
import com.osroyale.content.store.currency.Currency;
import com.osroyale.game.world.entity.mob.player.Player;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 5-1-2017.
 */
public final class ClanPointCurrency implements Currency {

    @Override
    public boolean tangible() {
        return false;
    }

    @Override
    public boolean takeCurrency(Player player, int amount) {
        ClanChannel channel = player.clanChannel;

        if (channel == null) {
            player.interfaceManager.close();
            return false;
        }

        if (channel.getDetails().points >= amount) {
            channel.getDetails().points -= amount;
            return true;
        } else {
            player.message("You do not have enough clan points.");
        }
        return false;
    }

    @Override
    public void recieveCurrency(Player player, int amount) {
        ClanChannel channel = player.clanChannel;

        if (channel == null) {
            player.interfaceManager.close();
            return;
        }

        channel.getDetails().points += amount;
    }

    @Override
    public int currencyAmount(Player player) {
        ClanChannel channel = player.clanChannel;

        if (channel == null) {
            player.interfaceManager.close();
            return 0;
        }

        return channel.getDetails().points;
    }

    @Override
    public boolean canRecieveCurrency(Player player) {
        return true;
    }
}
