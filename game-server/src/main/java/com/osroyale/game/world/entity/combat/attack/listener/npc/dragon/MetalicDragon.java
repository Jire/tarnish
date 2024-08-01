package com.osroyale.game.world.entity.combat.attack.listener.npc.dragon;

import com.osroyale.game.world.entity.combat.CombatType;
import com.osroyale.game.world.entity.combat.CombatUtil;
import com.osroyale.game.world.entity.combat.attack.FightType;
import com.osroyale.game.world.entity.combat.attack.listener.NpcCombatListenerSignature;
import com.osroyale.game.world.entity.combat.attack.listener.SimplifiedListener;
import com.osroyale.game.world.entity.combat.hit.CombatHit;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.strategy.CombatStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcMeleeStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.impl.DragonfireStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;

import static com.osroyale.game.world.entity.combat.CombatUtil.createStrategyArray;
import static com.osroyale.game.world.entity.combat.CombatUtil.getHitDelay;
import static com.osroyale.game.world.entity.combat.CombatUtil.randomStrategy;
import static com.osroyale.game.world.entity.combat.projectile.CombatProjectile.getDefinition;

/**
 * @author Michael | Chex
 */
@NpcCombatListenerSignature(npcs = {
    /* Bronze */  270, 271,
    /* Iron */    272, 273,
	/* Steel */   139, 274, 275,
	/* Mithril */ 2919
})
public class MetalicDragon extends SimplifiedListener<Npc> {

	private static Dragonfire DRAGONFIRE;
	private static CombatStrategy<Npc>[] STRATEGIES;

	static {
		try {
			DRAGONFIRE = new Dragonfire();
			STRATEGIES = createStrategyArray(NpcMeleeStrategy.get(), DRAGONFIRE);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean canAttack(Npc attacker, Mob defender) {
		if(!NpcMeleeStrategy.get().withinDistance(attacker, defender)) {
			attacker.setStrategy(DRAGONFIRE);
		}
		return attacker.getStrategy().canAttack(attacker, defender);
	}

	@Override
	public void start(Npc attacker, Mob defender, Hit[] hits) {
		if(!NpcMeleeStrategy.get().withinDistance(attacker, defender)) {
			attacker.setStrategy(DRAGONFIRE);
		} else {
			attacker.setStrategy(randomStrategy(STRATEGIES));
		}
	}

	private static class Dragonfire extends DragonfireStrategy {
		private Dragonfire() {
			super(getDefinition("Metalic dragonfire"));
		}

		@Override
		public int getAttackDistance(Npc attacker, FightType fightType) {
			return 10;
		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			return new CombatHit[] { CombatUtil.generateDragonfire(attacker, defender, 60, false) };
		}
	}

}
