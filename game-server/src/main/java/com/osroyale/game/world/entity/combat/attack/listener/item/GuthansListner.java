package com.osroyale.game.world.entity.combat.attack.listener.item;

import com.osroyale.game.Graphic;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.world.entity.combat.attack.listener.ItemCombatListenerSignature;
import com.osroyale.game.world.entity.combat.attack.listener.NpcCombatListenerSignature;
import com.osroyale.game.world.entity.combat.attack.listener.SimplifiedListener;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.mob.Mob;

/**
 * Handles the guthan item set listener
 * OSRS Wiki: http://oldschoolrunescape.wikia.com/wiki/Guthan_the_Infested%27s_equipment
 *
 * @author Daniel
 */
@NpcCombatListenerSignature(npcs = {1674})
@ItemCombatListenerSignature(requireAll = true, items = {4726, 4724, 4728, 4730})
public class GuthansListner extends SimplifiedListener<Mob> {

	@Override
	public void hit(Mob attacker, Mob defender, Hit hit) {
		if (Math.random() > 0.75) {
			attacker.heal(hit.getDamage());
			attacker.graphic(new Graphic(398, UpdatePriority.HIGH));
		}
	}
}
