package com.osroyale.game.world.entity.combat.attack.listener.item;

import com.osroyale.game.world.entity.combat.attack.listener.ItemCombatListenerSignature;
import com.osroyale.game.world.entity.combat.attack.listener.SimplifiedListener;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;

/**
 * Handles the twisted bow modifiers.
 * @author Michael | Chex
 */
@ItemCombatListenerSignature(requireAll = false, items = {20997})
public class TwistedBowListener extends SimplifiedListener<Player> {

	@Override
	public int modifyAccuracy(Player attacker, Mob defender, int roll) {
		int level = defender.skills.getMaxLevel(Skill.MAGIC);
		if (level > 360) level = 360;
		int a = (3 * level) / 10 - 100;
		int mod = 140 + (3 * level - 10) / 100 - (a * a) / 100;
		if (mod > 140) mod = 140;
		return roll * mod / 100;
	}

	@Override
	public int modifyDamage(Player attacker, Mob defender, int roll) {
		int level = defender.skills.getMaxLevel(Skill.MAGIC);
		if (level > 360) level = 360;
		int a = (3 * level) / 10 - 140;
		int mod = 250 + (3 * level - 14) / 100 - (a * a) / 100;
		if (mod > 250) mod = 250;
		return roll * mod / 100;
	}

}
