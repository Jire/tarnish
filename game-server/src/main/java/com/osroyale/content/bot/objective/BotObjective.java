package com.osroyale.content.bot.objective;

import com.osroyale.content.bot.PlayerBot;
import com.osroyale.content.bot.objective.impl.*;

public enum BotObjective implements BotObjectiveListener {
    WALK_IN_WILDERNESS(new WildernessWalkObjective()),
    WALK_TO_DITCH(new WildernessDitchObjective()),
    WALK_TO_BANK(new BankObjective()),
    RESTOCK(new RestockObjective()),
    COMBAT(new CombatObjective());

    private final BotObjectiveListener listener;

    BotObjective(BotObjectiveListener listener) {
        this.listener = listener;
    }

    @Override
    public void init(PlayerBot bot) {
        listener.init(bot);
    }

    @Override
    public void finish(PlayerBot bot) {
        listener.finish(bot);
    }

}
