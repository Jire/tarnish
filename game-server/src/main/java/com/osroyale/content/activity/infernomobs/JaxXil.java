package com.osroyale.content.activity.infernomobs;

import com.osroyale.game.Animation;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.world.entity.combat.attack.FightType;
import com.osroyale.game.world.entity.combat.hit.CombatHit;
import com.osroyale.game.world.entity.combat.strategy.CombatStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.MultiStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcMeleeStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcRangedStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;

import static com.osroyale.game.world.entity.combat.CombatUtil.createStrategyArray;
import static com.osroyale.game.world.entity.combat.projectile.CombatProjectile.getDefinition;

public class JaxXil extends MultiStrategy {

	private static final Melee MELEE = new Melee();
	private static final Ranged RANGED = new Ranged();

	private static final CombatStrategy<Npc>[] STRATEGIES = createStrategyArray(RANGED, MELEE);

	public JaxXil() {
		currentStrategy = RANGED;
	}

	@Override
	public boolean canAttack(Npc attacker, Mob defender) {
		if (!currentStrategy.withinDistance(attacker, defender)) {
			currentStrategy = MELEE;
		}
		return currentStrategy.canAttack(attacker, defender);
	}

	private static class Melee extends NpcMeleeStrategy {
		private static final Animation ANIMATION = new Animation(7604, UpdatePriority.HIGH);

		@Override
		public int getAttackDistance(Npc attacker, FightType fightType) {
			return 2;
		}

		@Override
		public Animation getAttackAnimation(Npc attacker, Mob defender) {
			return ANIMATION;
		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			return new CombatHit[] { nextMeleeHit(attacker, defender) };
		}
	}

	private static class Ranged extends NpcRangedStrategy {

		private static final Animation ANIMATION = new Animation(7605, UpdatePriority.HIGH);

		private Ranged() {
			super(getDefinition("xil"));
		}

		@Override
		public Animation getAttackAnimation(Npc attacker, Mob defender) {
			return ANIMATION;
		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			return new CombatHit[] { nextMagicHit(attacker, defender, 46) };
		}
	}
}