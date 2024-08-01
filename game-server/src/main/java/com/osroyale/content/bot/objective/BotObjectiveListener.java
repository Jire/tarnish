package com.osroyale.content.bot.objective;

import com.osroyale.content.bot.PlayerBot;

public interface BotObjectiveListener {

    void init(PlayerBot bot);

    void finish(PlayerBot bot);

}
