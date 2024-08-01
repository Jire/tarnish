package com.osroyale.game.world.entity.combat.attack.listener.other.prayer.ranged;

import com.osroyale.game.world.entity.combat.attack.listener.SimplifiedListener;
import com.osroyale.game.world.entity.mob.Mob;

public class SharpListener extends SimplifiedListener<Mob> {

    @Override
    public int modifyRangedLevel(Mob attacker, Mob defender, int damage) {
        return damage * 21 / 20;
    }

}
