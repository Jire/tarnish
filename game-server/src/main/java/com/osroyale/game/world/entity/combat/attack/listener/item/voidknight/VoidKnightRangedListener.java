package com.osroyale.game.world.entity.combat.attack.listener.item.voidknight;

import com.osroyale.game.world.entity.combat.attack.listener.ItemCombatListenerSignature;
import com.osroyale.game.world.entity.combat.attack.listener.SimplifiedListener;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.player.Player;

/**
 * Handles the slayer helmet / black mask effects ofr slayer tasks.
 *
 * @author Michael | Chex
 */
@ItemCombatListenerSignature(requireAll = true, items = { 11664, 8839, 8840, 8842 })
public class VoidKnightRangedListener extends SimplifiedListener<Player> {

    @Override
    public int modifyRangedLevel(Player attacker, Mob defender, int level) {
        return level * 11 / 10;
    }

}
