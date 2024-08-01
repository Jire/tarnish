package com.osroyale.content.bot.objective.impl;

import com.osroyale.content.bot.PlayerBot;
import com.osroyale.content.bot.objective.BotObjectiveListener;
import com.osroyale.game.world.position.Position;
import com.osroyale.util.RandomUtils;

public class WildernessWalkObjective implements BotObjectiveListener {

    @Override
    public void init(PlayerBot bot) {
        bot.loop(1, () -> {
            if (bot.movement.needsPlacement()) {
                return;
            }

            int x = bot.getX() + RandomUtils.inclusive(-5, 5);
            int y = bot.getY() + RandomUtils.inclusive(-5, 5);
            if (x < 3061) x = 3061;
            if (y < 3525) y = 3525;
            if (x > 3101) x = 3101;
            if (y > 3547) y = 3547;

            bot.walkExactlyTo(Position.create(x, y));
            bot.pause(RandomUtils.inclusive(4, 15));
        });
    }

    @Override
    public void finish(PlayerBot bot) {
    }

}
