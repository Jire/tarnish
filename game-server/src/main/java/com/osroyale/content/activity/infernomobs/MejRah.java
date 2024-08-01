package com.osroyale.content.activity.infernomobs;

import com.osroyale.game.task.TickableTask;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.combat.attack.FightType;
import com.osroyale.game.world.entity.combat.hit.CombatHit;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.strategy.npc.MultiStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcRangedStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.skill.Skill;

import static com.osroyale.game.world.entity.combat.projectile.CombatProjectile.getDefinition;

public class MejRah extends MultiStrategy {

	public MejRah() {
		currentStrategy = new Ranged();
	}

	@Override
	public int getAttackDelay(Npc attacker, Mob defender, FightType fightType) {
		return attacker.definition.getAttackDelay();
	}

	private static class Ranged extends NpcRangedStrategy {

		private Ranged() {
			super(getDefinition("MejRah"));
		}

		@Override
		public void hit(Npc attacker, Mob defender, Hit hit) {
			if (!hit.isAccurate())
				return;
			World.schedule(new TickableTask(true, 1) {
				@Override
				protected void tick() {
					Skill attack = defender.skills.get(Skill.ATTACK);
					Skill str = defender.skills.get(Skill.STRENGTH);
					Skill range = defender.skills.get(Skill.RANGED);
					Skill mage = defender.skills.get(Skill.MAGIC);
					Skill def = defender.skills.get(Skill.DEFENCE);
					attack.modifyLevel(level -> level - 2);
					str.modifyLevel(level -> level - 2);
					range.modifyLevel(level -> level - 2);
					mage.modifyLevel(level -> level - 2);
					def.modifyLevel(level -> level - 2);
					defender.skills.refresh();
					if (defender.isPlayer())
						defender.getPlayer().message("@red@You're skills have been drained..");
					if (tick == ((CombatHit) hit).getHitsplatDelay() - 1)
						cancel();
				}
			});
		}

		@Override
		public CombatHit[] getHits(Npc attacker, Mob defender) {
			return new CombatHit[] { nextRangedHit(attacker, defender, 19) };
		}
	}
}
