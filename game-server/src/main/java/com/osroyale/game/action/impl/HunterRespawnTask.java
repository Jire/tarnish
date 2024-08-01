package com.osroyale.game.action.impl;

import com.osroyale.content.skill.impl.hunter.net.impl.Butterfly;
import com.osroyale.content.skill.impl.hunter.net.impl.Impling;
import com.osroyale.game.task.Task;
import com.osroyale.game.world.entity.mob.npc.Npc;

import java.util.Optional;

/**
 * Teleports an entity to another part of the world.
 *
 * @author Daniel
 */
public final class HunterRespawnTask extends Task {
    public final Npc npc;

    public HunterRespawnTask(Npc npc) {
        super(false, 80);
        this.npc = npc;
    }

    @Override
    public void onSchedule() {
        Optional<Impling> impling = Impling.forId(npc.id);
        Optional<Butterfly> butterfly = Butterfly.forId(npc.id);

        if (impling.isPresent()) {
            setDelay(impling.get().delay);
        } else butterfly.ifPresent(butterfly1 -> setDelay(butterfly1.delay));

        npc.resetFace();
        npc.setVisible(false);
    }

    @Override
    public void execute() {
        npc.setVisible(true);
        cancel();
    }
}
