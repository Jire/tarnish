package com.osroyale.content.bot.objective.impl;

import com.osroyale.content.bot.PlayerBot;
import com.osroyale.content.bot.objective.BotObjective;
import com.osroyale.content.bot.objective.BotObjectiveListener;
import com.osroyale.game.world.position.Position;
import com.osroyale.util.RandomUtils;

public class BankObjective implements BotObjectiveListener {

    /** The positions of all the bank locations for the bot to access. */
    private static final Position[] BANK_LOCATIONS = {
        new Position(3096, 3493),
        new Position(3098, 3493),
        new Position(3095, 3491),
        new Position(3095, 3489)
    };

    @Override
    public void init(PlayerBot bot) {
        Position position = RandomUtils.random(BANK_LOCATIONS);
        bot.walkTo(position, () -> finish(bot));
    }

    @Override
    public void finish(PlayerBot bot) {
        bot.schedule(RandomUtils.random(6, 12), () -> BotObjective.RESTOCK.init(bot));
    }

}
