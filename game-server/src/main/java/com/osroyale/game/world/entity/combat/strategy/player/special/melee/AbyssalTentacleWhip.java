package com.osroyale.game.world.entity.combat.strategy.player.special.melee;

import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.world.entity.combat.hit.CombatHit;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.strategy.player.PlayerMeleeStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.player.Player;

/**
 * @author Daniel
 */
public class AbyssalTentacleWhip extends PlayerMeleeStrategy {
	private static final Animation ANIMATION = new Animation(1658, UpdatePriority.HIGH);
	private static final Graphic GRAPHIC = new Graphic(181, 100);
	private static final AbyssalTentacleWhip INSTANCE = new AbyssalTentacleWhip();
	
	@Override
	public void attack(Player attacker, Mob defender, Hit hit) {
		super.attack(attacker, defender, hit);
		defender.graphic(GRAPHIC);
	}

	@Override
	public void start(Player attacker, Mob defender, Hit[] hits) {
		super.start(attacker, defender, hits);
		//TODO EFFECT
	}

	@Override
	public CombatHit[] getHits(Player attacker, Mob defender) {
		return new CombatHit[]{nextMeleeHit(attacker, defender)};
	}
	
	@Override
	public Animation getAttackAnimation(Player attacker, Mob defender) {
		return ANIMATION;
	}

	public static AbyssalTentacleWhip get() {
		return INSTANCE;
	}

}