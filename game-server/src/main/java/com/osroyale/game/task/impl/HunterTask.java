package com.osroyale.game.task.impl;

import com.osroyale.content.skill.impl.hunter.trap.Trap;
import com.osroyale.content.skill.impl.hunter.trap.TrapExecution;
import com.osroyale.content.skill.impl.hunter.trap.TrapManager;
import com.osroyale.game.task.Task;
import com.osroyale.game.world.World;

import java.util.Iterator;

public class HunterTask extends Task {
    private static boolean RUNNING;

    public static void intialize() {
        if (!RUNNING) {
            RUNNING = true;
            World.schedule(new HunterTask());
        }
    }

    public HunterTask() {
        super(false, 1);
    }

    @Override
    public void execute() {
        final Iterator<Trap> iterator = TrapManager.traps.iterator();
        while (iterator.hasNext()) {
            final Trap trap = iterator.next();
            if (trap == null)
                continue;
            if (trap.getOwner() == null || !trap.getOwner().isRegistered())
                TrapManager.deregister(trap);
            TrapExecution.setTrapProcess(trap);
            TrapExecution.trapTimerManagement(trap);
        }

        if (TrapManager.traps.isEmpty()) {
            cancel();
        }
    }

    @Override
    public void onCancel(boolean logout) {
        RUNNING = false;
    }
}
