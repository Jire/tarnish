package com.osroyale.content.activity.impl.warriorguild;

import com.osroyale.content.activity.ActivityListener;
import com.osroyale.game.world.entity.mob.Mob;

import java.util.Arrays;

/**
 * Handles the warrior guild activity combat listener.
 *
 * @author Daniel.
 */
public class WarriorGuildActivityListener extends ActivityListener<WarriorGuild> {

    /**  Constructs a new <code>WarriorGuildActivityListener</code>. */
    WarriorGuildActivityListener(WarriorGuild minigame) {
        super(minigame);
    }

    @Override
    public boolean canAttack(Mob attacker, Mob defender) {
        boolean cyclop = false;

        for (int id : WarriorGuildUtility.CYCLOPS) {
            if (id == defender.id) {
                cyclop = true;
                break;
            }
        }

        if (cyclop && activity.state == WarriorGuildState.ANIMATOR) {
            return false;
        }

        return super.canAttack(attacker, defender);
    }
}
