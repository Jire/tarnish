package com.osroyale.game.world.entity.combat.strategy.npc.boss;

import com.osroyale.game.Animation;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.world.entity.combat.CombatUtil;
import com.osroyale.game.world.entity.combat.attack.FightType;
import com.osroyale.game.world.entity.combat.hit.CombatHit;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.strategy.npc.MultiStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcMagicStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcMeleeStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcRangedStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.pathfinding.path.SimplePathChecker;
import com.osroyale.game.world.position.Position;
import com.osroyale.util.Utility;

import static com.osroyale.game.world.entity.combat.projectile.CombatProjectile.getDefinition;

/** @author Daniel */
public class Zulrah extends MultiStrategy {
    private final MeleeAttack MELEE = new MeleeAttack();
    private static final MagicAttack MAGIC = new MagicAttack();
    private static final RangedAttack RANGED = new RangedAttack();

    public Zulrah() {
        setRanged();
    }

    public void setMelee() {
        currentStrategy = MELEE;
    }

    public void setMagic() {
        currentStrategy = MAGIC;
    }

    public void setRanged() {
        currentStrategy = RANGED;
    }

    private class MeleeAttack extends NpcMeleeStrategy {
        private Position end;

        @Override
        public boolean withinDistance(Npc attacker, Mob defender) {
            return Utility.within(attacker, defender, getAttackDistance(attacker, attacker.getCombat().getFightType()))
                    && (SimplePathChecker.checkProjectile(attacker, defender) || SimplePathChecker.checkProjectile(defender, attacker));
        }

        @Override
        public void start(Npc attacker, Mob defender, Hit[] hits) {
            attacker.blockInteract = true;
            attacker.resetFace();
            attacker.face(end = defender.getPosition());
        }

        @Override
        public void hit(Npc attacker, Mob defender, Hit hit) {
            attacker.blockInteract = false;
            attacker.animate(new Animation(5806, UpdatePriority.HIGH));

            if (!defender.getPosition().equals(end)) {
                /* not in same position, remove hit */
                hit.setDamage(-1);
                hit.setAccurate(false);
            } else {
                /* re-roll the hit */
                attacker.getCombat().addModifier(this);
                hit.setAs(nextMeleeHit(attacker, defender, 41));
                attacker.getCombat().removeModifier(this);
                defender.animate(CombatUtil.getBlockAnimation(defender));
            }
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            return new CombatHit[] { nextMeleeHit(attacker, defender, -1, 4, 0, false) };
        }

        @Override
        public int getAttackDelay(Npc attacker, Mob defender, FightType fightType) {
            return 6;
        }

        @Override
        public int getAttackDistance(Npc attacker, FightType fightType) {
            return 4;
        }

        @Override
        public Animation getAttackAnimation(Npc attacker, Mob defender) {
            return new Animation(5807, UpdatePriority.HIGH);
        }

        @Override
        public int modifyAttackLevel(Npc attacker, Mob defender, int level) {
            /* level 300 attack */
            return 300;
        }

    }

    private static class RangedAttack extends NpcRangedStrategy {
        private RangedAttack() {
            super(getDefinition("Zulrah Ranged"));
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            return new CombatHit[]{nextRangedHit(attacker, defender, 41)};
        }

        @Override
        public int getAttackDistance(Npc attacker, FightType fightType) {
            return 10;
        }

    }

    private static class MagicAttack extends NpcMagicStrategy {
        private MagicAttack() {
            super(getDefinition("Zulrah Magic"));
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            CombatHit combatHit = nextMagicHit(attacker, defender, 41);
            combatHit.setAccurate(true);
            return new CombatHit[]{combatHit};
        }

        @Override
        public int getAttackDistance(Npc attacker, FightType fightType) {
            return 10;
        }

    }

}
