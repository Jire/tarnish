package com.osroyale.game.world.entity.combat.strategy.player.special.range;

import com.osroyale.game.Animation;
import com.osroyale.game.Projectile;
import com.osroyale.game.world.entity.combat.CombatType;
import com.osroyale.game.world.entity.combat.attack.FightType;
import com.osroyale.game.world.entity.combat.hit.CombatHit;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.strategy.basic.RangedStrategy;
import com.osroyale.game.world.entity.combat.strategy.player.custom.ToxicBlowpipeStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;

public class Blowpipe extends RangedStrategy<Player> {
    private static final Blowpipe INSTANCE = new Blowpipe();
    private static final Projectile SPECIAL_PROJ = new Projectile(1043);

    @Override
    public boolean canAttack(Player attacker, Mob defender) {
        return ToxicBlowpipeStrategy.get().canAttack(attacker, defender);
    }

    @Override
    public void start(Player attacker, Mob defender, Hit[] hits) {
        attacker.animate(getAttackAnimation(attacker, defender), true);
        SPECIAL_PROJ.send(attacker, defender);

        if (!defender.isPlayer() || !PlayerRight.isIronman(attacker)) {
            addCombatExperience(attacker, hits);
        }

        attacker.getCombatSpecial().drain(attacker);
        for (Hit hit : hits) {
            attacker.heal((int) Math.floor(hit.getDamage() / 2));
        }
    }

    @Override
    public void attack(Player attacker, Mob defender, Hit hit) {
        ToxicBlowpipeStrategy.get().attack(attacker, defender, hit);
    }

    @Override
    public Animation getAttackAnimation(Player attacker, Mob defender) {
        return ToxicBlowpipeStrategy.get().getAttackAnimation(attacker, defender);
    }

    @Override
    public int getAttackDelay(Player attacker, Mob defender, FightType fightType) {
        return ToxicBlowpipeStrategy.get().getAttackDelay(attacker, defender, fightType);
    }

    @Override
    public int getAttackDistance(Player attacker, FightType fightType) {
        return ToxicBlowpipeStrategy.get().getAttackDistance(attacker, fightType);
    }

    @Override
    public CombatHit[] getHits(Player attacker, Mob defender) {
        return new CombatHit[]{nextRangedHit(attacker, defender)};
    }

    @Override
    public CombatType getCombatType() {
        return CombatType.RANGED;
    }

    public static Blowpipe get() {
        return INSTANCE;
    }

}
