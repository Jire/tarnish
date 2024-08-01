package com.osroyale.game.task.impl;

import com.osroyale.game.task.Task;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerOption;
import com.osroyale.game.world.position.Area;
import com.osroyale.net.packet.out.SendPlayerOption;
import com.osroyale.net.packet.out.SendString;

import java.util.concurrent.TimeUnit;

public class PvPTimerTask extends Task {
    private static final int[] TIMES = {  10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0, -1  };

    private final Player player;

    public PvPTimerTask(Player player) {
        super(false, 1);
        this.player = player;
    }

    @Override
    public void onSchedule() {
        player.pvpTimer.reset();
        player.hasPvPTimer = true;
    }

    @Override
    public void execute() {
        int time = TIMES[(int) player.pvpTimer.elapsedTime(TimeUnit.SECONDS)];

        if (!player.pvpInstance || Area.inPvP(player) || player.pvpTimer.elapsedTime(TimeUnit.SECONDS) > 10 || time == -1) {
            cancel();
            return;
        }

        player.send(new SendString("<col=FF0011>" + time, 23327));
    }

    @Override
    public void onCancel(boolean logout) {
        player.getCombat().reset();
        player.send(new SendString("<col=36CF4D>Safe", 23327));
        player.send(new SendPlayerOption(PlayerOption.ATTACK, false, true));
        player.send(new SendPlayerOption(PlayerOption.DUEL_REQUEST, false, true));
        player.hasPvPTimer = false;
    }
}
