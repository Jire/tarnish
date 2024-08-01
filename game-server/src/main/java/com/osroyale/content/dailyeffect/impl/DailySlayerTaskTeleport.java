package com.osroyale.content.dailyeffect.impl;

import com.osroyale.content.dailyeffect.DailyEffect;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;

public class DailySlayerTaskTeleport extends DailyEffect {

    @Override
    public int maxUses(Player player) {
        if (PlayerRight.isElite(player)) {
            return 10;
        } else if (PlayerRight.isExtreme(player)) {
            return 8;
        } else if (PlayerRight.isSuper(player)) {
            return 6;
        } else if (PlayerRight.isDonator(player)) {
            return 4;
        }
        return 2;
    }
}
