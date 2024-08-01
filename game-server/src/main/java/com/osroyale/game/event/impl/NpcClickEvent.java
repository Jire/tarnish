package com.osroyale.game.event.impl;

import com.osroyale.game.event.Event;
import com.osroyale.game.world.entity.mob.npc.Npc;

public class NpcClickEvent implements Event {

    private final int type;

    private final Npc npc;

    public NpcClickEvent(int type, Npc npc) {
        this.type = type;
        this.npc = npc;
    }

    public int getType() {
        return type;
    }

    public Npc getNpc() {
        return npc;
    }

}
