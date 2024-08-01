package com.osroyale.game.world.entity.combat.attack.listener;

import com.osroyale.game.world.entity.combat.CombatType;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.mob.Mob;

public class SimplifiedListener<T extends Mob> implements CombatListener<T> {

	public static final CombatListener<Mob> CANT_ATTACK = new SimplifiedListener<Mob>() {
		@Override
		public boolean canAttack(Mob attacker, Mob defender) {
			return false;
		}
	};

	@Override
	public boolean withinDistance(T attacker, Mob defender) {
		return true;
	}

	@Override
	public boolean canOtherAttack(Mob attacker, T defender) {
		return true;
	}

	@Override
	public boolean canAttack(T attacker, Mob defender) {
		return true;
	}
	
	@Override
	public void start(T attacker, Mob defender, Hit[] hits) { }

	@Override
	public void attack(T attacker, Mob defender, Hit hit) { }

	@Override
	public void hit(T attacker, Mob defender, Hit hit) { }

	@Override
	public void block(Mob attacker, T defender, Hit hit, CombatType combatType) { }

	@Override
	public void preDeath(Mob attacker, T defender, Hit hit) { }

	@Override
	public void onDeath(Mob attacker, T defender, Hit hit) { }

	@Override
	public void preKill(Mob attacker, Mob defender, Hit hit) {
	}

	@Override
	public void onKill(T attacker, Mob defender, Hit hit) { }

	@Override
	public void hitsplat(T attacker, Mob defender, Hit hit) {
	}

	@Override
	public void finishIncoming(Mob attacker, T defender) { }

	@Override
	public void finishOutgoing(T attacker, Mob defender) { }

	@Override
	public void onDamage(T defender, Hit hit) {

	}
}
