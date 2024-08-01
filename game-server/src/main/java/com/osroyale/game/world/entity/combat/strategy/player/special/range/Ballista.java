package com.osroyale.game.world.entity.combat.strategy.player.special.range;

import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.world.entity.combat.attack.FightType;
import com.osroyale.game.world.entity.combat.hit.CombatHit;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.strategy.player.PlayerRangedStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.player.Player;

/** @author Michael | Chex */
public class Ballista extends PlayerRangedStrategy {
    private static final Animation ANIMATION = new Animation(7222, UpdatePriority.HIGH);
    private static final Graphic GRAPHIC = new Graphic(344, 50, UpdatePriority.HIGH);
    private static final Ballista INSTANCE = new Ballista();

    private Ballista() {
    }

    @Override
    public void hitsplat(Player attacker, Mob defender, Hit hit) {
        super.hitsplat(attacker, defender, hit);
        defender.graphic(GRAPHIC);
    }

    @Override
    public CombatHit[] getHits(Player attacker, Mob defender) {
        return new CombatHit[]{nextRangedHit(attacker, defender)};
    }

    @Override
    public int getAttackDelay(Player attacker, Mob defender, FightType fightType) {
        return 10;
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
    public int modifyDamage(Player attacker, Mob defender, int roll) {
        return roll * 5 / 4;
    }

    public static Ballista get() {
        return INSTANCE;
    }

}