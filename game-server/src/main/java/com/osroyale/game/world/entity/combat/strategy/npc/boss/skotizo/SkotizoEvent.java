package com.osroyale.game.world.entity.combat.strategy.npc.boss.skotizo;

import com.osroyale.game.task.Task;
import com.osroyale.game.world.World;
import com.osroyale.util.Stopwatch;

import java.util.concurrent.TimeUnit;

public class SkotizoEvent extends Task {
    private boolean initial;
    private final Stopwatch stopwatch = Stopwatch.start();

    public SkotizoEvent() {
        super(false, 100);
        this.initial = true;
    }

    @Override
    public void execute() {
        if ((SkotizoUtility.skotizo == null || !SkotizoUtility.skotizo.isRegistered()) && !initial) {
            initial = true;
            stopwatch.reset();
        }

        if (initial) {
            if (stopwatch.elapsedTime(TimeUnit.MINUTES) >= 120) {
                SkotizoUtility.skotizo = SkotizoUtility.generateSpawn();
                initial = false;
                stopwatch.reset();
            }
            return;
        }

        if (stopwatch.elapsedTime(TimeUnit.MINUTES) >= 20) {
            initial = true;
            stopwatch.reset();
            if (SkotizoUtility.skotizo != null) {
                SkotizoUtility. skotizo.speak("Pathetic humans could not kill me! Muhahaha");
                World.schedule(2, () -> SkotizoUtility.skotizo.unregister());
            }
            World.sendMessage("<icon=6><col=8714E6> Skotizo has disappeared! He will return in 60 minutes.");
        }
    }
}
