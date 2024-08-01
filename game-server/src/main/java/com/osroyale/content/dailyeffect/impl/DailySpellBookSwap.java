package com.osroyale.content.dailyeffect.impl;

import com.osroyale.content.dailyeffect.DailyEffect;
import com.osroyale.game.world.entity.mob.player.Player;

public class DailySpellBookSwap extends DailyEffect {

    @Override
    public int maxUses(Player player) {
        return 5;
    }
}
