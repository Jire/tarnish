package com.osroyale.game.world.entity.combat.strategy.player.special.melee;

import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.world.entity.combat.CombatUtil;
import com.osroyale.game.world.entity.combat.hit.CombatHit;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.strategy.player.PlayerMeleeStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/** @author Daniel | Obey */
public class DinhsBulwark extends PlayerMeleeStrategy {
	private static final Graphic GRAPHIC = new Graphic(1336, UpdatePriority.HIGH);
	private static final Animation ANIMATION = new Animation(7511, UpdatePriority.HIGH);
	private static final DinhsBulwark INSTANCE = new DinhsBulwark();

	private DinhsBulwark() { }

	@Override
	public void start(Player attacker, Mob defender, Hit[] hits) {
		attacker.getCombatSpecial().drain(attacker);
		attacker.animate(getAttackAnimation(attacker, defender), true);

		List<Hit> extra = new LinkedList<>();
		CombatUtil.areaAction(attacker, 10, 11, other -> hitEvent(attacker, defender, other, extra));

		if (!defender.isPlayer() || !PlayerRight.isIronman(attacker)) {
			Collections.addAll(extra, hits);
			addCombatExperience(attacker, extra.toArray(new Hit[extra.size()]));
		}

		attacker.graphic(GRAPHIC);
		attacker.animate(ANIMATION, true);
	}

	@Override
	public int modifyAccuracy(Player attacker, Mob defender, int roll) {
		return roll * 6 / 5;
	}

	private void hitEvent(Player attacker, Mob defender, Mob other, List<Hit> extra) {
		if (!CombatUtil.canBasicAttack(attacker, other)) {
			return;
		}

		if (attacker.equals(other) || defender.equals(other)) {
			return;
		}

		CombatHit hit = nextMeleeHit(attacker, defender);
		attacker.getCombat().submitHits(other, hit);
		if (extra != null) extra.add(hit);
	}

	public static DinhsBulwark get() {
		return INSTANCE;
	}

}