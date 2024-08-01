package com.osroyale.game.world.entity.combat.strategy.player.special.melee;

import com.osroyale.content.achievement.AchievementHandler;
import com.osroyale.content.achievement.AchievementKey;
import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.world.entity.combat.attack.FightType;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.strategy.player.PlayerMeleeStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.player.Player;

/** @author Daniel | Obey */
public class DragonMace extends PlayerMeleeStrategy {
	private static final Animation ANIMATION = new Animation(1060, UpdatePriority.HIGH);
	private static final Graphic GRAPHIC = new Graphic(251);
	private static final DragonMace INSTANCE = new DragonMace();

	private DragonMace() { }

	@Override
	public void start(Player attacker, Mob defender, Hit[] hits) {
		super.start(attacker, defender, hits);
		attacker.graphic(GRAPHIC);
	}

	@Override
	public void hitsplat(Player attacker, Mob defender, Hit hit) {
		super.hitsplat(attacker, defender, hit);
		if (defender.isPlayer() && hit.getDamage() >= 45) {
			AchievementHandler.activate(attacker, AchievementKey.DMACE_MAX);
		}
	}

	@Override
	public void onKill(Player attacker, Mob defender, Hit hit) {
		AchievementHandler.activate(attacker, AchievementKey.DMACE_SPEC);
	}

	@Override
	public int getAttackDelay(Player attacker, Mob defender, FightType fightType) {
		return 3;
	}
	
	@Override
	public Animation getAttackAnimation(Player attacker, Mob defender) {
		return ANIMATION;
	}

	@Override
	public int modifyAccuracy(Player attacker, Mob defender, int roll) {
		return roll * 5 / 4;
	}

	@Override
	public int modifyDamage(Player attacker, Mob defender, int damage) {
		return 3 / 2;
	}

	public static DragonMace get() {
		return INSTANCE;
	}

}