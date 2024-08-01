package com.osroyale.content.bot.botclass;

import com.osroyale.content.bot.PlayerBot;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;

public interface BotClass {

    Item[] inventory();

    Item[] equipment();

    int[] skills();

    void initCombat(Player target, PlayerBot bot);

    void handleCombat(Player target, PlayerBot bot);

    void endFight(PlayerBot bot);

    void pot(Player target, PlayerBot bot);

    void eat(Player target, PlayerBot bot);

}
