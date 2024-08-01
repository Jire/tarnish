package com.osroyale.game.world.entity.combat.attack.listener.other.prayer;

import com.osroyale.game.world.entity.combat.attack.listener.SimplifiedListener;
import com.osroyale.game.world.entity.mob.Mob;

public class AuguryListener extends SimplifiedListener<Mob> {

    @Override
    public int modifyMagicLevel(Mob attacker, Mob defender, int level) {
        return level * 5 / 4;
    }

    @Override
    public int modifyDefenceLevel(Mob attacker, Mob defender, int damage) {
        return damage * 5 / 4;
    }

}
