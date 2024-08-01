package com.osroyale.game.world.entity.combat.attack.listener.item;

import com.osroyale.game.world.entity.combat.attack.listener.ItemCombatListenerSignature;
import com.osroyale.game.world.entity.combat.attack.listener.SimplifiedListener;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.player.Player;

/**
 * Handles the slayer helmet / black mask effects ofr slayer tasks.
 *
 * @author Michael | Chex
 */
@ItemCombatListenerSignature(requireAll = false, items = {11865, 19641, 19645, 19649, 21266})
public class ImbuedSlayerHelmListener extends SimplifiedListener<Player> {

    @Override
    public int modifyAttackLevel(Player attacker, Mob defender, int level) {
        if (attacker.equals(defender) || attacker.slayer.getTask() != null && attacker.slayer.getTask().valid(defender.getName()))
            return level * 7 / 6;
        return level;
    }

    @Override
    public int modifyStrengthLevel(Player attacker, Mob defender, int level) {
        if (attacker.equals(defender) || attacker.slayer.getTask() != null && attacker.slayer.getTask().valid(defender.getName()))
            return level * 7 / 6;
        return level;
    }

    @Override
    public int modifyRangedLevel(Player attacker, Mob defender, int level) {
        if (attacker.equals(defender) || attacker.slayer.getTask() != null && attacker.slayer.getTask().valid(defender.getName()))
            return level * 23 / 20;
        return level;
    }

    @Override
    public int modifyMagicLevel(Player attacker, Mob defender, int level) {
        if (attacker.equals(defender) || attacker.slayer.getTask() != null && attacker.slayer.getTask().valid(defender.getName()))
            return level * 23 / 20;
        return level;
    }

}
