package com.osroyale.game.world.entity.mob.movement.waypoint;

import com.osroyale.content.activity.Activity;
import com.osroyale.content.activity.impl.kraken.KrakenActivity;
import com.osroyale.game.world.entity.combat.attack.FightType;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.util.Utility;

public class CombatWaypoint extends Waypoint {

    public CombatWaypoint(Mob mob, Mob target) {
        super(mob, target);
    }

    @Override
    public void onDestination() {
        mob.movement.reset();
    }

    @Override
    protected boolean withinDistance() {
        if (target.equals(mob.getCombat().getDefender())) {
            return mob.isPlayer() && Activity.evaluate(mob.getPlayer(), it -> {
                if (it instanceof KrakenActivity) {
                    Mob kraken = ((KrakenActivity) it).kraken;
                    return Utility.getDistance(mob, kraken) <= getRadius() && mob.getStrategy().withinDistance(mob, kraken);
                }
                return false;
            }) || Utility.getDistance(mob, target) <= getRadius() && mob.getStrategy().withinDistance(mob, (Mob) target);
        }
        return super.withinDistance();
    }

    @Override
    protected int getRadius() {
        if (target.equals(mob.getCombat().getDefender())) {
            FightType fightType = mob.getCombat().getFightType();
            return mob.getStrategy().getAttackDistance(mob, fightType);
        }
        return 1;
    }

}
