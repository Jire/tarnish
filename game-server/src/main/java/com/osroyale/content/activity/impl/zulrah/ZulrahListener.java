package com.osroyale.content.activity.impl.zulrah;

import com.osroyale.content.activity.ActivityListener;
import com.osroyale.game.world.entity.mob.Mob;

/**
 * The zulrah activity combat listener.
 *
 * @author Daniel
 */
public class ZulrahListener extends ActivityListener<ZulrahActivity> {

    /** Constructs a new <code>ZulrahListener</code>. */
    ZulrahListener(ZulrahActivity activity) {
        super(activity);
    }

    @Override
    public boolean canOtherAttack(Mob attacker, Mob defender) {
        return true;
    }

    @Override
    public boolean canAttack(Mob attacker, Mob defender) {
        if (attacker.isNpc() && attacker.getNpc().id != 2045) {
            return attacker.getNpc().canAttack;
        }
        return true;
    }
}
