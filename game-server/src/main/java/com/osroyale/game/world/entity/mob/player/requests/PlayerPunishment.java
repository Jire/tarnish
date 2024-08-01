package com.osroyale.game.world.entity.mob.player.requests;


import com.osroyale.content.activity.impl.JailActivity;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.IPMutedPlayers;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.util.Utility;

import java.util.concurrent.TimeUnit;

/**
 * Handles the player punishment
 *
 * @author Daniel
 */
public class PlayerPunishment {
    private final Player player;

    public long muteStart, muteDuration;
    public long jailStart, jailDuration;

    public PlayerPunishment(Player player) {
        this.player = player;
    }

    /** Muting */
    public void mute(long duration, TimeUnit unit) {
        muteStart = System.currentTimeMillis();
        muteDuration = TimeUnit.MILLISECONDS.convert(duration, unit);
        player.message("<col=F21827>You have been muted for " + duration + " " + unit.name().toLowerCase());
        player.dialogueFactory.sendStatement("You have been muted for " + duration + " " + unit.name().toLowerCase()).execute();
    }

    public void unmute() {
        muteStart = -1;
        muteDuration = -1;
    }

    public boolean isMuted() {
        return IPMutedPlayers.isIpMuted(player.lastHost) || muteStart > 0 && System.currentTimeMillis() - muteStart < muteDuration;
    }

    /** Jailing */
    public void jail(long duration, TimeUnit unit) {
        jailStart = System.currentTimeMillis();
        jailDuration = TimeUnit.MILLISECONDS.convert(duration, unit);
        player.message("<col=F21827>You have been jailed for " + duration + " " + unit.name().toLowerCase());
        player.dialogueFactory.sendStatement("You have been jailed for " + duration + " " + unit.name().toLowerCase()).execute();
        JailActivity.create(player);
    }

    public void unJail() {
        jailStart = -1;
        jailDuration = -1;
    }

    public boolean isJailed() {
        return jailStart > 0 && System.currentTimeMillis() - jailStart < jailDuration;
    }
}
