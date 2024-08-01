package com.osroyale.game.world.entity.combat;

import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.pathfinding.path.SimplePathChecker;
import com.osroyale.game.world.position.Area;
import com.osroyale.game.world.position.Position;
import com.osroyale.game.world.region.Region;
import com.osroyale.util.Utility;

import java.util.concurrent.TimeUnit;

public class CombatTarget {

    /** The aggression timeout in minutes. */
    private static final int AGGRESSION_TIMEOUT = 20;
    private final Mob mob;
    private Mob target;
    private int distance;

    CombatTarget(Mob mob) {
        this.mob = mob;
        this.distance = Integer.MAX_VALUE;
    }

    /** Checks the aggression for this mob if a target is set. */
    void checkAggression(Position spawn) {
        /* No target */
        if (target == null)
            return;

        /* The target is unreachable */
        if (distance == Integer.MAX_VALUE)
            return;

        /* The mob is too far from spawn */
        if (Utility.getDistance(mob, spawn) > Region.VIEW_DISTANCE && !Area.inFightCaves(mob) && !Area.inGodwarsChambers(mob)) {
            if (mob.getCombat().isUnderAttack()) {
                Mob trgt = target;
                distance = Integer.MAX_VALUE;
                mob.getCombat().reset();
                if (mob.isNpc() && mob.getNpc().boundaries.length > 0) {
                    Position pos = Utility.randomElement(mob.getNpc().boundaries);
                    mob.interact(trgt);
                    mob.walkExactlyTo(pos, () -> {
                        mob.resetFace();
                        resetTarget();
                    });
                }
            } else {
                mob.getCombat().reset();
                mob.resetFace();
                resetTarget();
            }
            return;
        }

        int dist = Utility.getDistance(target, mob);
        int aggressionRadius = mob.width() + 3;

        if (Area.inGodwarsChambers(mob)) {
            aggressionRadius = Region.SIZE;
        }

        /* The mob is too far from target */
        if (dist > aggressionRadius)
            return;

        /* The mob is already in combat with the target */
        if (mob.getCombat().isAttacking(target))
            return;

        if (!mob.getCombat().attack(target)) {
            mob.getCombat().reset();
        }
    }

    /**
     * Compares the given mob with the current target. If the give mob is closer
     * than the current target, the target will be set to the given mob.
     *
     * @param other the mob to compare to the target
     */

    void compare(Mob other, int level, Position spawn) {
        int dist = Utility.getDistance(mob, other);

        /* Already targeting this mob */
        if (isTarget(other))
            return;

        /* The npc is too far from target */
        if (dist > Region.VIEW_DISTANCE)
            return;

        if (spawn != null && !(mob instanceof Player && mob.activity != null) && !Area.inFightCaves(mob) && !Area.inGodwars(mob) && Utility.getDistance(other, spawn) > Region.VIEW_DISTANCE)
            return;

        if (other.skills.getCombatLevel() > level * 2 && !Area.inWilderness(mob))
            return;

        /* Found a closer target */
        if (dist < distance && (SimplePathChecker.checkProjectile(mob, other) || SimplePathChecker.checkProjectile(other, mob))) {
            target = other;
            distance = dist;
        }
    }

    public static void checkAggression(Player player) {
        if (!player.isVisible())
            return;

        if (player.viewport.getNpcsInViewport().isEmpty())
            return;

        if (player.getCombat().inCombat() && !Area.inMulti(player))
            return;

        for (Npc npc : player.viewport.getNpcsInViewport()) {
            if (npc == null || !npc.isValid())
                continue;

            if (!npc.definition.isAttackable() || !npc.definition.isAggressive())
                continue;

            if (npc.isDead() || !npc.isVisible() || npc.forceWalking)
                continue;

            if (npc.locking.locked())
                continue;

            if (player.aggressionTimer.elapsed(AGGRESSION_TIMEOUT, TimeUnit.MINUTES)
                    && !Area.inGodwars(npc)
                    && player.activity == null) {
                if (npc.getCombat().isAttacking(player) && !player.getCombat().isAttacking(npc))
                    npc.getCombat().reset();
                continue;
            }

            /* Godwars check */
            //TODO CLEANER WAY SORRY MICHAEL LOL
            if (Area.inGodwars(npc) && !Area.inGodwarsChambers(npc)) {
                if (npc.npcAssistant.isArmadyl()) {
                    boolean found = false;
                    for (Npc npc1 : npc.getRegion().getNpcs(npc.getHeight())) {
                        if (Utility.getDistance(npc, npc1.getPosition()) < 5 && !npc1.npcAssistant.isArmadyl()) {
                            found = true;
                            npc.getCombat().attack(npc1);
                            break;
                        }
                    }

                    if (found) {
                        continue;
                    }

                    if (player.equipment.hasArmadyl()) {
                        continue;
                    }
                } else if (npc.npcAssistant.isBandos()) {
                    boolean found = false;
                    for (Npc npc1 : npc.getRegion().getNpcs(npc.getHeight())) {
                        if (Utility.getDistance(npc, npc1.getPosition()) < 5 && !npc1.npcAssistant.isBandos()) {
                            found = true;
                            npc.getCombat().attack(npc1);
                            break;
                        }
                    }

                    if (found) {
                        continue;
                    }

                    if (player.equipment.hasBandos()) {
                        continue;
                    }
                } else if (npc.npcAssistant.isSaradomin()) {
                    boolean found = false;
                    for (Npc npc1 : npc.getRegion().getNpcs(npc.getHeight())) {
                        if (Utility.getDistance(npc, npc1.getPosition()) < 5 && !npc1.npcAssistant.isSaradomin()) {
                            found = true;
                            npc.getCombat().attack(npc1);
                            break;
                        }
                    }

                    if (found) {
                        continue;
                    }

                    if (player.equipment.hasSaradomin()) {
                        continue;
                    }
                } else if (npc.npcAssistant.isZamorak()) {
                    boolean found = false;
                    for (Npc npc1 : npc.getRegion().getNpcs(npc.getHeight())) {
                        if (Utility.getDistance(npc, npc1.getPosition()) < 5 && !npc1.npcAssistant.isZamorak()) {
                            found = true;
                            npc.getCombat().attack(npc1);
                            break;
                        }
                    }

                    if (found) {
                        continue;
                    }

                    if (player.equipment.hasZamorak()) {
                        continue;
                    }
                }
            }

            npc.getCombat().compare(player, npc.definition.getCombatLevel(),  npc.spawnPosition);
        }
    }

    void resetTarget() {
        target = null;
        distance = Integer.MAX_VALUE;
    }

    public void setTarget(Mob target) {
        this.target = target;
        distance = Utility.getDistance(mob, target);
    }

    boolean isTarget(Mob mob) {
        return mob.equals(target);
    }

    public Mob getTarget() {
        return target;
    }
}
