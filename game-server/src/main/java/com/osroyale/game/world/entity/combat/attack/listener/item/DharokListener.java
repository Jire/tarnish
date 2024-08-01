package com.osroyale.game.world.entity.combat.attack.listener.item;

import com.osroyale.content.achievement.AchievementHandler;
import com.osroyale.content.achievement.AchievementKey;
import com.osroyale.game.world.entity.combat.attack.listener.ItemCombatListenerSignature;
import com.osroyale.game.world.entity.combat.attack.listener.NpcCombatListenerSignature;
import com.osroyale.game.world.entity.combat.attack.listener.SimplifiedListener;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.mob.Mob;

/**
 * Handles the Dharok's armor effects to the assigned npc and item ids.
 * @author Michael | Chex
 */
@NpcCombatListenerSignature(npcs = {1673})
@ItemCombatListenerSignature(requireAll = true, items = {4716, 4718, 4720, 4722})
public class DharokListener extends SimplifiedListener<Mob> {

	@Override
	public int modifyDamage(Mob attacker, Mob defender, int damage) {
		int health = attacker.getMaximumHealth() - attacker.getCurrentHealth();
		if (health < 0) health = 0;
		return damage + damage * health / 100;
	}

	@Override
	public void onKill(Mob attacker, Mob defender, Hit hit) {
		if (attacker.isPlayer() && defender.isPlayer()) {
			AchievementHandler.activate(attacker.getPlayer(), AchievementKey.DHAROK);
		}
	}
}
