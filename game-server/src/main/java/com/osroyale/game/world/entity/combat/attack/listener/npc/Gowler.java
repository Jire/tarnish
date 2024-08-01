package com.osroyale.game.world.entity.combat.attack.listener.npc;

import com.osroyale.game.Animation;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.world.entity.combat.attack.listener.NpcCombatListenerSignature;
import com.osroyale.game.world.entity.combat.attack.listener.SimplifiedListener;
import com.osroyale.game.world.entity.combat.hit.CombatHit;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcMagicStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;

import static com.osroyale.game.world.entity.combat.projectile.CombatProjectile.getDefinition;

/**
 * @author Daniel
 */
@NpcCombatListenerSignature(npcs = {2207})
public class Gowler extends SimplifiedListener<Npc> {

    private static MagicAttack MAGIC;

    static {
        try {
            MAGIC = new MagicAttack();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean canAttack(Npc attacker, Mob defender) {
        return attacker.getStrategy().canAttack(attacker, defender);
    }

    @Override
    public void start(Npc attacker, Mob defender, Hit[] hits) {
        attacker.setStrategy(MAGIC);
    }

    private static class MagicAttack extends NpcMagicStrategy {
        private MagicAttack() {
            super(getDefinition("Fire Wave"));
        }

        @Override
        public void start(Npc attacker, Mob defender, Hit[] hits) {
            attacker.animate(new Animation(7036, UpdatePriority.VERY_HIGH));
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            CombatHit hit = nextMagicHit(attacker, defender, 16, combatProjectile);
            hit.setAccurate(true);
            return new CombatHit[]{hit};
        }
    }
}
