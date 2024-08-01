package com.osroyale.content.activity.inferno;

import com.osroyale.content.activity.ActivityListener;
import com.osroyale.game.world.entity.combat.CombatType;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.util.Utility;


public class InfernoCavesListener extends ActivityListener<Inferno> {


	public InfernoCavesListener(Inferno minigame) {
		super(minigame);
	}

	@Override
	public void block(Mob attacker, Mob defender, Hit hit, CombatType combatType) {
		if (!defender.isNpc())
			return;
		if (defender.id != 3127)
			return;
		if (Utility.getPercentageAmount(defender.getCurrentHealth(), defender.getMaximumHealth()) > 49)
			return;
		for (Npc npc : activity.npcs) {
			if (npc.id == 3128
					&& (npc.getCombat().inCombatWith(attacker) || Utility.withinDistance(defender, npc, 5))) {
				defender.heal(1);
			}
		}
	}

	@Override
	public void onDeath(Mob attacker, Mob defender, Hit hit) {
		activity.handleDeath(defender);
	}
}
