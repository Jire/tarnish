package com.osroyale.game.world.entity.combat.strategy.npc;

import com.osroyale.game.Animation;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.world.entity.combat.CombatType;
import com.osroyale.game.world.entity.combat.attack.FightType;
import com.osroyale.game.world.entity.combat.effect.impl.CombatPoisonEffect;
import com.osroyale.game.world.entity.combat.effect.impl.CombatVenomEffect;
import com.osroyale.game.world.entity.combat.hit.CombatHit;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.strategy.basic.MeleeStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.util.RandomUtils;

public class NpcMeleeStrategy extends MeleeStrategy<Npc> {

    private static final NpcMeleeStrategy INSTANCE = new NpcMeleeStrategy();

    protected NpcMeleeStrategy() {
    }

    @Override
    public void start(Npc attacker, Mob defender, Hit[] hits) {
        attacker.animate(getAttackAnimation(attacker, defender), true);
    }

    @Override
    public void attack(Npc attacker, Mob defender, Hit hit) {
        if (!attacker.definition.isPoisonous()) {
            return;
        }

        if (CombatVenomEffect.isVenomous(attacker) && RandomUtils.success(0.25)) {
            defender.venom();
        } else {
            CombatPoisonEffect.getPoisonType(attacker.id).ifPresent(defender::poison);
        }
    }

    @Override
    public int getAttackDelay(Npc attacker, Mob defender, FightType fightType) {
        return attacker.definition.getAttackDelay();
    }

    @Override
    public int getAttackDistance(Npc attacker, FightType fightType) {
/*        final NpcDefinition definition = attacker.definition;
        return definition == null ? 1 : definition.getSize();*/
        return 1;
    }

    @Override
    public CombatHit[] getHits(Npc attacker, Mob defender) {
        return new CombatHit[]{nextMeleeHit(attacker, defender)};
    }

    @Override
    public Animation getAttackAnimation(Npc attacker, Mob defender) {
        return new Animation(attacker.definition.getAttackAnimation(), UpdatePriority.HIGH);
    }

    @Override
    public boolean canAttack(Npc attacker, Mob defender) {
        return true;
    }

    @Override
    public CombatType getCombatType() {
        return CombatType.MELEE;
    }

    public static NpcMeleeStrategy get() {
        return INSTANCE;
    }

}
