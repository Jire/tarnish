package com.osroyale.game.world.entity.combat.strategy.player.special.melee;

import com.osroyale.content.achievement.AchievementHandler;
import com.osroyale.content.achievement.AchievementKey;
import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.world.entity.combat.hit.CombatHit;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.strategy.player.PlayerMeleeStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.player.Player;

/** @author Michael | Chex */
public class DragonDagger extends PlayerMeleeStrategy {
	private static final DragonDagger INSTANCE = new DragonDagger();

	private static final Animation ANIMATION = new Animation(1062, UpdatePriority.HIGH);
	private static final Graphic GRAPHIC = new Graphic(252, true, UpdatePriority.HIGH);

	@Override
	public void start(Player attacker, Mob defender, Hit[] hits) {
		super.start(attacker, defender, hits);
	}

	@Override
	public void attack(Player attacker, Mob defender, Hit hit) {
		super.attack(attacker, defender, hit);
		attacker.graphic(GRAPHIC);
	}

	@Override
	public void onKill(Player attacker, Mob defender, Hit hit) {
		if (defender.isPlayer()) {
			AchievementHandler.activate(attacker, AchievementKey.DDS_SPEC);
		}
	}

	@Override
	public CombatHit[] getHits(Player attacker, Mob defender) {
		return new CombatHit[]{nextMeleeHit(attacker, defender), nextMeleeHit(attacker, defender)};
	}
	
	@Override
	public Animation getAttackAnimation(Player attacker, Mob defender) {
		return ANIMATION;
	}

	@Override
	public int modifyAttackLevel(Player attacker, Mob defender, int roll) {
		return roll * 5 / 4;
	}

	@Override
	public int modifyStrengthLevel(Player attacker, Mob defender, int damage) {
		return damage * 23 / 20;
	}

	public static DragonDagger get() {
		return INSTANCE;
	}

}