package com.osroyale.game.engine.sync;

import com.osroyale.game.engine.sync.task.NpcPostUpdateTask;
import com.osroyale.game.engine.sync.task.NpcUpdateTask;
import com.osroyale.game.engine.sync.task.PlayerPostUpdateTask;
import com.osroyale.game.engine.sync.task.PlayerUpdateTask;
import com.osroyale.game.world.entity.MobList;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.player.Player;

public final class SequentialClientSynchronizer implements ClientSynchronizer {

    @Override
    public void synchronize(MobList<Player> players, MobList<Npc> npcs) {
/*        npcs.forEach(npc -> new NpcPreUpdateTask(npc).run());
        players.forEach(player -> new PlayerPreUpdateTask(player).run());*/

        players.forEach(player -> new PlayerUpdateTask(player).run());
        players.forEach(player -> new NpcUpdateTask(player).run());

        npcs.forEach(npc -> new NpcPostUpdateTask(npc).run());
        players.forEach(player -> new PlayerPostUpdateTask(player).run());
    }

}
