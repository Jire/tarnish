package com.osroyale.content.skill.impl.hunter.trap;

import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.util.Utility;

public class TrapExecution {

    public static void setTrapProcess(Trap trap) {
        for (final Npc npc : TrapManager.HUNTER_NPC_LIST) {
            if (npc == null || !npc.isVisible()) {
                continue;
            }
            if (trap instanceof BoxTrap && npc.id != 5079 && npc.id != 5080)
                continue;
            if (trap instanceof SnareTrap && (npc.id == 5079 || npc.id == 5080))
                continue;
            if (npc.getPosition().isWithinDistance(trap.getGameObject().getPosition(), 1)) {
                if (Utility.random(100) < successFormula(trap)) {
                    TrapManager.catchNPC(trap, npc);
                    return;
                }
            }
        }
    }

    public static int successFormula(Trap trap) {
        if (trap.getOwner() == null)
            return 0;
        int chance = 70;

        if (TrapManager.hasLarupia(trap.getOwner())) {
            chance = chance + 10;
        }

        chance = chance + (int) (trap.getOwner().skills.getLevel(Skill.HUNTER) / 1.5) + 10;

        if (trap.getOwner().skills.getLevel(Skill.HUNTER) < 25)
            chance = (int) (chance * 1.5) + 8;
        if (trap.getOwner().skills.getLevel(Skill.HUNTER) < 40)
            chance = (int) (chance * 1.4) + 3;
        if (trap.getOwner().skills.getLevel(Skill.HUNTER) < 50)
            chance = (int) (chance * 1.3) + 1;
        if (trap.getOwner().skills.getLevel(Skill.HUNTER) < 55)
            chance = (int) (chance * 1.2);
        if (trap.getOwner().skills.getLevel(Skill.HUNTER) < 60)
            chance = (int) (chance * 1.1);
        if (trap.getOwner().skills.getLevel(Skill.HUNTER) < 65)
            chance = (int) (chance * 1.05) + 3;

        return chance;
    }

    public static boolean trapTimerManagement(Trap trap) {
        if (trap.getTicks() > 0)
            trap.setTicks(trap.getTicks() - 1);
        if (trap.getTicks() <= 0) {
            TrapManager.deregister(trap);
            if (trap.getOwner() != null)
                trap.getOwner().message("You left your trap for too long, and it collapsed.");
        }
        return true;
    }
}
