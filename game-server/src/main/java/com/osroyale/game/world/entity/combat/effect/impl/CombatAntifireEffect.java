package com.osroyale.game.world.entity.combat.effect.impl;

import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.game.world.entity.combat.effect.AntifireDetails;
import com.osroyale.game.world.entity.combat.effect.CombatEffect;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.player.Player;

/**
 * The class which is responsible for the effect when you drink an anti-fire potion.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class CombatAntifireEffect extends CombatEffect {

	/**
	 * The type of antifire this player has drunk.
	 */
	private final AntifireDetails.AntifireType type;

	/**
	 * Constructs a new {@link CombatAntifireEffect}.
	 * @param type {@link #type}.
	 */
	public CombatAntifireEffect(AntifireDetails.AntifireType type) {
		super(1);
		this.type = type;
	}

	@Override
	public boolean apply(Mob mob) {
		if(!mob.isPlayer()) {
			return false;
		}

		Player player = mob.getPlayer();

		if(player.getAntifireDetails().isPresent()) {
			player.setAntifireDetail(new AntifireDetails(type));
			return false;
		}

		player.setAntifireDetail(new AntifireDetails(type));
		return true;
	}

	@Override
	public boolean removeOn(Mob mob) {
		if(mob.isPlayer()) {
			Player player = mob.getPlayer();

            return !player.getAntifireDetails().isPresent();

        }
		return true;
	}

	@Override
	public void process(Mob mob) {
		if(mob.isPlayer() && mob.getPlayer().getAntifireDetails().isPresent()) {
			Player player = mob.getPlayer();
			AntifireDetails detail = player.getAntifireDetails().get();
			int count = detail.getAntifireDelay().decrementAndGet();
			if(count == 30) {
				player.send(new SendMessage("@red@Your resistance to dragon fire is about to wear off!"));
			}
			if(count < 1) {
				player.setAntifireDetail(null);
				player.send(new SendMessage("Your resistance to dragon fire has worn off!"));
			}
		}
	}

	@Override
	public boolean onLogin(Mob mob) {
		return mob.isPlayer() && mob.getPlayer().getAntifireDetails().isPresent();
	}

}