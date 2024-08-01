package com.osroyale.game.world.entity.combat.attack.listener.other.prayer;

import com.osroyale.game.world.entity.combat.attack.listener.SimplifiedListener;
import com.osroyale.game.world.entity.mob.Mob;

public class ChivalryListener extends SimplifiedListener<Mob> {

    @Override
    public int modifyAttackLevel(Mob attacker, Mob defender, int level) {
        return level * 23 / 20;
    }

    @Override
    public int modifyStrengthLevel(Mob attacker, Mob defender, int level) {
        return level * 59 / 50;
    }

    @Override
    public int modifyDefenceLevel(Mob attacker, Mob defender, int level) {
        return level * 6 / 5;
    }

}
