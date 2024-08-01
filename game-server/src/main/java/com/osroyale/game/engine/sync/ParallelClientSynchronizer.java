package com.osroyale.game.engine.sync;

import com.osroyale.game.engine.sync.task.*;
import com.osroyale.game.world.entity.MobList;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.player.Player;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

public final class ParallelClientSynchronizer implements ClientSynchronizer {

    private static final ExecutorService executor = Executors
            .newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private final Phaser phaser = new Phaser(1);

    @Override
    public void synchronize(MobList<Player> players, MobList<Npc> npcs) {
        // npc movement
/*        phaser.bulkRegister(npcs.size());
        npcs.forEach(npc -> executor.submit(new PhasedUpdateTask(phaser, new NpcPreUpdateTask(npc))));
        phaser.arriveAndAwaitAdvance();*/

        // player movement
/*        phaser.bulkRegister(players.size());
        players.forEach(player -> executor.submit(new PhasedUpdateTask(phaser, new PlayerPreUpdateTask(player))));
        phaser.arriveAndAwaitAdvance();*/

        // player updating
        phaser.bulkRegister(players.size());
        players.forEach(player -> executor.submit(new PhasedUpdateTask(phaser, new PlayerUpdateTask(player))));
        phaser.arriveAndAwaitAdvance();

        // npc updating
        phaser.bulkRegister(players.size());
        players.forEach(player -> executor.submit(new PhasedUpdateTask(phaser, new NpcUpdateTask(player))));
        phaser.arriveAndAwaitAdvance();

        // reset npc
        phaser.bulkRegister(npcs.size());
        npcs.forEach(npc -> executor.submit(new PhasedUpdateTask(phaser, new NpcPostUpdateTask(npc))));
        phaser.arriveAndAwaitAdvance();

        // reset player
        phaser.bulkRegister(players.size());
        players.forEach(player -> executor.submit(new PhasedUpdateTask(phaser, new PlayerPostUpdateTask(player))));
        phaser.arriveAndAwaitAdvance();
    }

}
