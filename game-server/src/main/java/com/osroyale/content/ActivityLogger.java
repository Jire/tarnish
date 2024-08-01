package com.osroyale.content;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.out.SendScrollbar;
import com.osroyale.net.packet.out.SendString;
import com.osroyale.util.Utility;


/**
 * Handles displaying player's activity log.
 *
 * @author Daniel
 */
public class ActivityLogger {
    private final Player player;

    public ActivityLogger(Player player) {
        this.player = player;
    }

    public void add(ActivityLog log) {
        add(log, 1);
    }

    public void add(ActivityLog log, int amount) {
        int current = player.loggedActivities.computeIfAbsent(log, a -> 0);
        player.loggedActivities.put(log, current + amount);
        player.message("Your " + Utility.formatEnum(log.name()) + " count is now <col=FF0000>" + get(log) + "</col>.");
    }

    public int get(ActivityLog log) {
        if (!player.loggedActivities.containsKey(log)) {
            player.loggedActivities.put(log, 0);
        }
        return player.loggedActivities.get(log);
    }

    public void open() {
        for (int index = 0, string = 37111; index < 80; index++) {
            player.send(new SendString("", string));
            string++;
            player.send(new SendString("", string));
            string++;
        }

        int string = 37111;
        for (ActivityLog log : ActivityLog.values()) {
            player.send(new SendString(Utility.formatEnum(log.name()) + ": <col=255>" + Utility.formatDigits(get(log)), string));
            string++;
        }

        player.send(new SendString("", 37107));
        player.send(new SendString("Activity Logger", 37103));
        player.send(new SendScrollbar(37110, (player.loggedActivities.size() * 55)));
        player.interfaceManager.open(37100);
    }
}

