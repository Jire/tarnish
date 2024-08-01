package com.osroyale.content.activity.infernomobs;

import com.osroyale.game.Animation;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.task.Task;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.combat.hit.CombatHit;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.strategy.npc.MultiStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcMeleeStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;

public class Jalimkot extends MultiStrategy {

	private static Melee MELEE = new Melee();
	private static Burrow BURROW = new Burrow();

	public Jalimkot() {
		currentStrategy = MELEE;
	}

	@Override
	public boolean canAttack(Npc attacker, Mob defender) {
		if (attacker.getPosition().isWithinDistance(defender.getPlayer().getPosition(), 5)) {
			currentStrategy = BURROW;
		} else {
			currentStrategy = MELEE;
		}
		return currentStrategy.canAttack(attacker, defender);
	}

	private static class Burrow extends NpcMeleeStrategy {
		private static final Animation ANIMATION = new Animation(7600, UpdatePriority.HIGH);

		@Override
		public void start(Npc attacker, Mob defender, Hit[] hits) {
			attacker.animate(ANIMATION, true);
			World.schedule(new Task(true, 1) {
				int tick = 0;

				@Override
				public void execute() {
					tick++;
					if (tick >= 3) {
						// attacker.move(defender.getPosition().addX(1));
						attacker.animate(7601);
					}
				}
			});
		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			return new CombatHit[] { nextMeleeHit(attacker, defender, 49) };
		}
	}

	private static class Melee extends NpcMeleeStrategy {
		private static final Animation ANIMATION = new Animation(7597, UpdatePriority.HIGH);

		@Override
		public Animation getAttackAnimation(Npc attacker, Mob defender) {
			return ANIMATION;
		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			return new CombatHit[] { nextMeleeHit(attacker, defender, 49) };
		}
	}
}
