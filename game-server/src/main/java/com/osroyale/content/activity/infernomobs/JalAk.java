package com.osroyale.content.activity.infernomobs;

import com.osroyale.game.Animation;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.world.entity.combat.CombatType;
import com.osroyale.game.world.entity.combat.attack.FightType;
import com.osroyale.game.world.entity.combat.hit.CombatHit;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.strategy.CombatStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.MultiStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcMagicStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcMeleeStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcRangedStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.prayer.Prayer;

import static com.osroyale.game.world.entity.combat.CombatUtil.createStrategyArray;
import static com.osroyale.game.world.entity.combat.CombatUtil.randomStrategy;
import static com.osroyale.game.world.entity.combat.projectile.CombatProjectile.getDefinition;

public class JalAk extends MultiStrategy {

	private static final CrushMelee MELEE = new CrushMelee();
	private static final Mage MAGE = new Mage();
	private static final Ranged RANGE = new Ranged();

	private static final CombatStrategy<Npc>[] FULL_STRATEGIES = createStrategyArray(RANGE, MAGE, MELEE);
	private static final CombatStrategy<Npc>[] NON_MELEE = createStrategyArray(RANGE, MAGE);

	public JalAk() {
		currentStrategy = randomStrategy(NON_MELEE);
	}

	@Override
	public boolean withinDistance(Npc attacker, Mob defender) {
		if (!currentStrategy.withinDistance(attacker, defender)) {
			currentStrategy = randomStrategy(NON_MELEE);
		}
		return currentStrategy.withinDistance(attacker, defender);
	}

	@Override
	public boolean canAttack(Npc attacker, Mob defender) {
		if (!currentStrategy.canAttack(attacker, defender)) {
			currentStrategy = randomStrategy(NON_MELEE);
		}
		return currentStrategy.canAttack(attacker, defender);
	}

	@Override
	public void block(Mob attacker, Npc defender, Hit hit, CombatType combatType) {
		currentStrategy.block(attacker, defender, hit, combatType);
		defender.getCombat().attack(attacker);
	}

	@Override
	public void finishOutgoing(Npc attacker, Mob defender) {
		currentStrategy.finishOutgoing(attacker, defender);
		if (MELEE.withinDistance(attacker, defender)) {
			currentStrategy = randomStrategy(FULL_STRATEGIES);
		} else {
			currentStrategy = randomStrategy(NON_MELEE);
		}
	}

	@Override
	public int getAttackDelay(Npc attacker, Mob defender, FightType fightType) {
		return attacker.definition.getAttackDelay();
	}

	@Override
	public void hit(Npc attacker, Mob defender, Hit hit) {
		super.hit(attacker, defender, hit);

		if (!defender.isPlayer())
			return;
		Player player = defender.getPlayer();

		if (currentStrategy.getCombatType().equals(CombatType.MELEE)
				&& player.prayer.isActive(Prayer.PROTECT_FROM_MELEE)) {
			hit.setDamage(0);
		} else if (currentStrategy.getCombatType().equals(CombatType.RANGED)
				&& player.prayer.isActive(Prayer.PROTECT_FROM_RANGE)) {
			hit.setDamage(0);
		} else if (currentStrategy.getCombatType().equals(CombatType.MAGIC)
				&& player.prayer.isActive(Prayer.PROTECT_FROM_MAGIC)) {
			hit.setDamage(0);
		}
	}

	private static final class CrushMelee extends NpcMeleeStrategy {
		private static final Animation ANIMATION = new Animation(7582, UpdatePriority.HIGH);

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
			return new CombatHit[] { nextMeleeHit(attacker, defender, 29) };
		}
	}

	private static class Mage extends NpcMagicStrategy {
		private static final Animation ANIMATION = new Animation(7583, UpdatePriority.HIGH);

		private Mage() {
			super(getDefinition("jalak mage"));
		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			return new CombatHit[] { nextMagicHit(attacker, defender, 29) };
		}

		@Override
		public Animation getAttackAnimation(Npc attacker, Mob defender) {
			return ANIMATION;
		}
	}

	private static class Ranged extends NpcRangedStrategy {
		private static final Animation ANIMATION = new Animation(7581, UpdatePriority.HIGH);

		private Ranged() {
			super(getDefinition("jalak range"));
		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			return new CombatHit[] { nextRangedHit(attacker, defender, 29) };
		}

		@Override
		public Animation getAttackAnimation(Npc attacker, Mob defender) {
			return ANIMATION;
		}
	}
}
