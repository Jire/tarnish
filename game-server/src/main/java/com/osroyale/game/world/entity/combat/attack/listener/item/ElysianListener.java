package com.osroyale.game.world.entity.combat.attack.listener.item;

import com.osroyale.game.Graphic;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.world.entity.combat.CombatType;
import com.osroyale.game.world.entity.combat.attack.listener.ItemCombatListenerSignature;
import com.osroyale.game.world.entity.combat.attack.listener.SimplifiedListener;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.player.Player;

/**
 * Handles the Elysian spirit shield listener.
 * OSRS Wiki: http://oldschoolrunescape.wikia.com/wiki/Elysian_spirit_shield
 *
 * @author Daniel
 */
@ItemCombatListenerSignature(requireAll = false, items = {12817})
public class ElysianListener extends SimplifiedListener<Player> {

    @Override
    public void block(Mob attacker, Player defender, Hit hit, CombatType combatType) {
        if (Math.random() > 0.30) {
            hit.modifyDamage(damage -> damage * 3 / 4);
            defender.graphic(new Graphic(321, UpdatePriority.HIGH));
        }
    }
}
