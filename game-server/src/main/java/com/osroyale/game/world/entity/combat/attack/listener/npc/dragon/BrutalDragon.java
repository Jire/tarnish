package com.osroyale.game.world.entity.combat.attack.listener.npc.dragon;

import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.Projectile;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.combat.attack.FightType;
import com.osroyale.game.world.entity.combat.attack.listener.NpcCombatListenerSignature;
import com.osroyale.game.world.entity.combat.attack.listener.SimplifiedListener;
import com.osroyale.game.world.entity.combat.hit.CombatHit;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.projectile.CombatProjectile;
import com.osroyale.game.world.entity.combat.strategy.CombatStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcMagicStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcMeleeStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.impl.BrutalDragonfireStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.impl.DragonfireStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.util.Utility;

import static com.osroyale.game.world.entity.combat.CombatUtil.createStrategyArray;
import static com.osroyale.game.world.entity.combat.CombatUtil.randomStrategy;
import static com.osroyale.game.world.entity.combat.projectile.CombatProjectile.getDefinition;

/** @author Michael | Chex */
@NpcCombatListenerSignature(npcs = {
2918, 7273, 7274, 7275, 8081, 8087, 8092
})
public class BrutalDragon extends SimplifiedListener<Npc> {
    private static BrutalDragonfireStrategy DRAGONFIRE;
    private static CombatStrategy<Npc>[] STRATEGIES;

    static {
        try {
            DRAGONFIRE = new BrutalDragonfireStrategy(getDefinition("Chromatic dragonfire"));
            STRATEGIES = createStrategyArray(new CrushMelee(), new StabMelee());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean canAttack(Npc attacker, Mob defender) {
        final var style = Utility.random(NpcMeleeStrategy.get().withinDistance(attacker, defender) ? 1 : 2);
        if (style == 0) {
            attacker.setStrategy(DRAGONFIRE);
        } else if (style == 1) {
            attacker.setStrategy(new MagicAttack());
        } else {
            attacker.setStrategy(randomStrategy(STRATEGIES));
        }
        return attacker.getStrategy().canAttack(attacker, defender);
    }

    @Override
    public void finishOutgoing(Npc attacker, Mob defender) {
        final var style = Utility.random(NpcMeleeStrategy.get().withinDistance(attacker, defender) ? 1 : 2);
        if (style == 0) {
            attacker.setStrategy(DRAGONFIRE);
        } else if (style == 1) {
            attacker.setStrategy(new MagicAttack());
        } else {
            attacker.setStrategy(randomStrategy(STRATEGIES));
        }
    }

    private static final class CrushMelee extends NpcMeleeStrategy {
        private static final Animation ANIMATION = new Animation(80, UpdatePriority.HIGH);

        @Override
        public int getAttackDistance(Npc attacker, FightType fightType) {
            return 1;
        }

        @Override
        public Animation getAttackAnimation(Npc attacker, Mob defender) {
            return ANIMATION;
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            return new CombatHit[]{nextMeleeHit(attacker, defender)};
        }
    }

    private static final class StabMelee extends NpcMeleeStrategy {
        private static final Animation ANIMATION = new Animation(91, UpdatePriority.HIGH);

        @Override
        public int getAttackDistance(Npc attacker, FightType fightType) {
            return 1;
        }

        @Override
        public Animation getAttackAnimation(Npc attacker, Mob defender) {
            return ANIMATION;
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            return new CombatHit[]{nextMeleeHit(attacker, defender)};
        }
    }

    private static class MagicAttack extends NpcMagicStrategy {
        private MagicAttack() {
            super(getDefinition("EMPTY"));
        }

        @Override
        public Animation getAttackAnimation(Npc attacker, Mob defender) {
            World.sendProjectile(attacker, defender, getMagicProjectile(attacker.getId()));
            return new Animation(6722, UpdatePriority.VERY_HIGH);
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            CombatHit combatHit = nextMagicHit(attacker, defender, getMaxHit(attacker.getId()));
            combatHit.setAccurate(true);
            return new CombatHit[] { combatHit };
        }
    }

    private static int getMaxHit(int npcId) {
        return switch (npcId) {
            case 2918, 8081 -> 18; //green
            case 7273 -> 21; //blue
            case 7274, 8087 -> 22; //red
            case 7275, 8092 -> 29; //black
            default -> -1;
        };
    }

    private static Projectile getMagicProjectile(int npcId) {
        return switch (npcId) {
            case 2918, 8081 -> new Projectile(136, 40, 90, 27, 30, 5); //green
            case 7273 -> new Projectile(133, 40, 90, 27, 30, 5); //blue
            case 7274, 8087 -> new Projectile(130, 40, 90, 27, 30, 5); //red
            case 7275, 8092 -> new Projectile(88, 40, 90, 27, 30, 5); //black
            default -> null;
        };
    }

}
