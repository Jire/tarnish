package com.osroyale.game.world.entity.combat.strategy.player.special.melee;

import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.world.entity.combat.attack.FightType;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.strategy.player.PlayerMeleeStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.player.Player;

/** @author Daniel | Obey */
public class DragonLongsword extends PlayerMeleeStrategy {
	private static final Animation ANIMATION = new Animation(1058, UpdatePriority.HIGH);
	private static final Graphic GRAPHIC = new Graphic(248);
	private static final DragonLongsword INSTANCE = new DragonLongsword();

	private DragonLongsword() { }

	@Override
	public void start(Player attacker, Mob defender, Hit[] hits) {
		super.start(attacker, defender, hits);
		attacker.graphic(GRAPHIC);
	}

	@Override
	public int getAttackDelay(Player attacker, Mob defender, FightType fightType) {
		return 4;
	}
	
	@Override
	public Animation getAttackAnimation(Player attacker, Mob defender) {
		return ANIMATION;
	}

	@Override
	public int modifyDamage(Player attacker, Mob defender, int damage) {
		return (int) (damage * 1.15);
	}

	public static DragonLongsword get() {
		return INSTANCE;
	}

}