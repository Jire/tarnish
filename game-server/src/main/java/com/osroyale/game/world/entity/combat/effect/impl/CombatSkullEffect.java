package com.osroyale.game.world.entity.combat.effect.impl;

import com.osroyale.game.world.entity.combat.effect.CombatEffect;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.position.Area;

/**
 * The combat effect applied when a player needs to be skulled.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class CombatSkullEffect extends CombatEffect {

    /**
     * Creates a new {@link CombatSkullEffect}.
     */
    public CombatSkullEffect() {
        super(50);
    }

    @Override
    public boolean apply(Mob mob) {
        if (mob.isPlayer()) {
            Player player = (Player) mob;

           /* if (Area.inEventArena(mob)) {
                return false;
            }*/

            if (!Area.inWilderness(mob)) {
                return false;
            }

            if (player.skulling.isSkulled()) {
                return false;
            }

            player.skulling.skull();
            return true;
        }
        return false;
    }

    @Override
    public boolean removeOn(Mob mob) {
        if (mob.isPlayer()) {
            Player player = (Player) mob;

            if (!player.skulling.isSkulled()) {
                player.skulling.unskull();
                return true;
            }

            return false;
        }

        return true;
    }

    @Override
    public void process(Mob mob) {
        // nothing to process
    }

    @Override
    public boolean onLogin(Mob mob) {
        if (mob.isPlayer()) {
            Player player = (Player) mob;

            if (player.skulling.isSkulled()) {
                return true;
            }
        }
        return false;
    }
}
