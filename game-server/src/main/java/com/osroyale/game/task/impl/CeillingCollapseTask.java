package com.osroyale.game.task.impl;

import com.osroyale.game.task.TickableTask;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.position.Area;
import com.osroyale.net.packet.out.SendCameraReset;
import com.osroyale.net.packet.out.SendCameraShake;
import com.osroyale.util.Utility;

public class CeillingCollapseTask extends TickableTask {
    private final Player player;

    public CeillingCollapseTask(Player player) {
        super(false, 6);
        this.player = player;
    }

    @Override
    public void onSchedule() {
        player.send(new SendCameraShake(3, 2, 3, 2));
    }

    @Override
    public void onCancel(boolean logout) {
        player.send(new SendCameraReset());
    }

    @Override
    protected void tick() {
        if (!Area.inBarrowsChamber(player)) {
            cancel();
            return;
        }
        player.graphic(60);
        player.speak("Ouch!");
        player.damage(new Hit(Utility.random(5, 8)));
        player.message("Some rocks fall from the ceiling and hit you.");
    }
}
