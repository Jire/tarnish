package com.osroyale.content.activity.randomevent;


import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.content.activity.randomevent.impl.DrillDemonEvent;
import com.osroyale.content.activity.randomevent.impl.GenieEvent;
import com.osroyale.content.activity.randomevent.impl.MimeEvent;
import com.osroyale.game.world.position.Area;
import com.osroyale.util.RandomUtils;
import com.osroyale.util.Utility;

public class RandomEventHandler {

    public static void trigger(Player player) {
        if (Area.inWilderness(player))
            return;
        if (player.playerAssistant.busy())
            return;
        if (player.inActivity())
            return;
        if (!player.isVisible())
            return;

        int base = 200;

        if (Utility.random(base) != 0)
            return;

        int events = RandomUtils.inclusive(1, 3);

        switch (events) {
            case 1: GenieEvent.create(player); break;
            case 2: MimeEvent.create(player);  break;
            case 3: DrillDemonEvent.create(player); break;
        }
    }
}
