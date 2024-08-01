package com.osroyale.game.world.entity.combat.strategy.npc;

import com.osroyale.game.Animation;
import com.osroyale.game.world.entity.combat.CombatType;
import com.osroyale.game.world.entity.combat.attack.FightType;
import com.osroyale.game.world.entity.combat.hit.CombatHit;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.strategy.CombatStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;

public abstract class MultiStrategy extends CombatStrategy<Npc> {
    protected CombatStrategy<Npc> currentStrategy;

    @Override
    public boolean withinDistance(Npc attacker, Mob defender) {
        return currentStrategy.withinDistance(attacker, defender);
    }

    @Override
    public boolean canAttack(Npc attacker, Mob defender) {
        return currentStrategy.canAttack(attacker, defender);
    }

    @Override
    public boolean canOtherAttack(Mob attacker, Npc defender) {
        return currentStrategy.canOtherAttack(attacker, defender);
    }

    @Override
    public void start(Npc attacker, Mob defender, Hit[] hits) {
        currentStrategy.start(attacker, defender, hits);
    }

    @Override
    public void attack(Npc attacker, Mob defender, Hit hit) {
        currentStrategy.attack(attacker, defender, hit);
    }

    @Override
    public void hit(Npc attacker, Mob defender, Hit hit) {
        currentStrategy.hit(attacker, defender, hit);
    }

    @Override
    public void hitsplat(Npc attacker, Mob defender, Hit hit) {
        currentStrategy.hitsplat(attacker, defender, hit);
    }

    @Override
    public void block(Mob attacker, Npc defender, Hit hit, CombatType combatType) {
        currentStrategy.block(attacker, defender, hit, combatType);
    }

    @Override
    public void preDeath(Mob attacker, Npc defender, Hit hit) {
        currentStrategy.preDeath(attacker, defender, hit);
    }

    @Override
    public void onDeath(Mob attacker, Npc defender, Hit hit) {
        currentStrategy.onDeath(attacker, defender, hit);
    }

    @Override
    public void preKill(Mob attacker, Mob defender, Hit hit) {
        currentStrategy.preKill(attacker, defender, hit);
    }

    @Override
    public void onKill(Npc attacker, Mob defender, Hit hit) {
        currentStrategy.onKill(attacker, defender, hit);
    }

    @Override
    public void finishIncoming(Mob attacker, Npc defender) {
        currentStrategy.finishIncoming(attacker, defender);
    }

    @Override
    public void finishOutgoing(Npc attacker, Mob defender) {
        currentStrategy.finishOutgoing(attacker, defender);
    }

    @Override
    public int getAttackDelay(Npc attacker, Mob defender, FightType fightType) {
        return currentStrategy.getAttackDelay(attacker, defender, fightType);
    }

    @Override
    public int getAttackDistance(Npc attacker, FightType fightType) {
        return currentStrategy.getAttackDistance(attacker, fightType);
    }

    @Override
    public CombatHit[] getHits(Npc attacker, Mob defender) {
        return currentStrategy.getHits(attacker, defender);
    }

    @Override
    public Animation getAttackAnimation(Npc attacker, Mob defender) {
        return currentStrategy.getAttackAnimation(attacker, defender);
    }

    @Override
    public CombatType getCombatType() {
        return currentStrategy.getCombatType();
    }

    @Override
    public int modifyAccuracy(Npc attacker, Mob defender, int roll) {
        return currentStrategy.modifyAccuracy(attacker, defender, roll);
    }

    @Override
    public int modifyAggressive(Npc attacker, Mob defender, int roll) {
        return currentStrategy.modifyAggressive(attacker, defender, roll);
    }

    @Override
    public int modifyDefensive(Mob attacker, Npc defender, int roll) {
        return currentStrategy.modifyDefensive(attacker, defender, roll);
    }

    @Override
    public int modifyDamage(Npc attacker, Mob defender, int damage) {
        return currentStrategy.modifyDamage(attacker, defender, damage);
    }

    @Override
    public int modifyAttackLevel(Npc attacker, Mob defender, int level) {
        return currentStrategy.modifyAttackLevel(attacker, defender, level);
    }

    @Override
    public int modifyStrengthLevel(Npc attacker, Mob defender, int level) {
        return currentStrategy.modifyStrengthLevel(attacker, defender, level);
    }

    @Override
    public int modifyDefenceLevel(Mob attacker, Npc defender, int level) {
        return currentStrategy.modifyDefenceLevel(attacker, defender, level);
    }

    @Override
    public int modifyRangedLevel(Npc attacker, Mob defender, int level) {
        return currentStrategy.modifyRangedLevel(attacker, defender, level);
    }

    @Override
    public int modifyMagicLevel(Npc attacker, Mob defender, int level) {
        return currentStrategy.modifyMagicLevel(attacker, defender, level);
    }

    @Override
    public int modifyOffensiveBonus(Npc attacker, Mob defender, int bonus) {
        return currentStrategy.modifyOffensiveBonus(attacker, defender, bonus);
    }

    @Override
    public int modifyAggressiveBonus(Npc attacker, Mob defender, int bonus) {
        return currentStrategy.modifyAggressiveBonus(attacker, defender, bonus);
    }

    @Override
    public int modifyDefensiveBonus(Mob attacker, Npc defender, int bonus) {
        return currentStrategy.modifyDefensiveBonus(attacker, defender, bonus);
    }

    @Override
    public void onDamage(Npc defender, Hit hit) {

    }
}
