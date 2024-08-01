package com.osroyale.game.world.entity.combat.strategy.player.special.melee;

import com.osroyale.content.achievement.AchievementHandler;
import com.osroyale.content.achievement.AchievementKey;
import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.world.entity.combat.attack.FightType;
import com.osroyale.game.world.entity.combat.hit.CombatHit;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.strategy.player.PlayerMeleeStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.player.Player;

public class GraniteMaul extends PlayerMeleeStrategy {
	private static final Animation ANIMATION = new Animation(1667, UpdatePriority.HIGH);
	private static final Graphic GRAPHIC = new Graphic(340);
	private static final GraniteMaul INSTANCE = new GraniteMaul();

	private GraniteMaul() { }

	@Override
	public void start(Player attacker, Mob defender, Hit[] hits) {
		super.start(attacker, defender, hits);

		if (attacker.attributes.is("granite-maul-spec")) {
			CombatHit damage = nextMeleeHit(attacker, defender);
			defender.damage(damage);
			defender.getCombat().getDamageCache().add(attacker, damage);
			attacker.getCombatSpecial().drain(attacker);
			attacker.attributes.remove("granite-maul-spec");
		}

		attacker.graphic(GRAPHIC);
	}

	@Override
	public CombatHit[] getHits(Player attacker, Mob defender) {
		return new CombatHit[] { nextMeleeHit(attacker, defender) };
	}

	@Override
	public void onKill(Player attacker, Mob defender, Hit hit) {
		if (defender.isPlayer()) {
			AchievementHandler.activate(attacker, AchievementKey.GMAULED_SPEC);
		}
	}

	@Override
	public Animation getAttackAnimation(Player attacker, Mob defender) {
		return ANIMATION;
	}

	@Override
	public int getAttackDelay(Player attacker, Mob defender, FightType fightType) {
		return attacker.attributes.has("granite-maul-spec") ? 0 : 1;
	}

	public static GraniteMaul get() {
		return INSTANCE;
	}

}