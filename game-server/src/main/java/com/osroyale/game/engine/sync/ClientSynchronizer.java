package com.osroyale.game.engine.sync;

import com.osroyale.game.world.entity.MobList;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.player.Player;

public interface ClientSynchronizer {

    void synchronize(MobList<Player> players, MobList<Npc> npcs);

}
