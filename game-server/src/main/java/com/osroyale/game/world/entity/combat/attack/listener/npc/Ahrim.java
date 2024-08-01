package com.osroyale.game.world.entity.combat.attack.listener.npc;

import com.osroyale.game.world.entity.combat.attack.listener.NpcCombatListenerSignature;
import com.osroyale.game.world.entity.combat.attack.listener.SimplifiedListener;
import com.osroyale.game.world.entity.combat.hit.CombatHit;
import com.osroyale.game.world.entity.combat.strategy.CombatStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcMagicStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;

import static com.osroyale.game.world.entity.combat.CombatUtil.createStrategyArray;
import static com.osroyale.game.world.entity.combat.CombatUtil.randomStrategy;
import static com.osroyale.game.world.entity.combat.projectile.CombatProjectile.getDefinition;

/**
 * @author Daniel
 */
@NpcCombatListenerSignature(npcs = { 1672 })
public class Ahrim extends SimplifiedListener<Npc> {

    private static FireBlast FIRE_BLAST = new FireBlast();
    private static Confuse CONFUSE = new Confuse();
    private static Weaken WEAKEN = new Weaken();
    private static Curse CURSE = new Curse();
    private static final CombatStrategy<Npc>[] STRATEGIES = createStrategyArray(
            FIRE_BLAST, FIRE_BLAST, FIRE_BLAST, CONFUSE, WEAKEN, CURSE
    );

    @Override
    public void finishOutgoing(Npc attacker, Mob defender) {
        attacker.setStrategy(randomStrategy(STRATEGIES));
    }

    private static class FireBlast extends NpcMagicStrategy {
        private FireBlast() {
            super(getDefinition("Fire Blast"));
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            CombatHit hit = nextMagicHit(attacker, defender, 25);
            hit.setAccurate(true);
            return new CombatHit[] { hit };
        }
    }

    private static class Confuse extends NpcMagicStrategy {
        private Confuse() {
            super(getDefinition("Confuse"));
        }
    }

    private static class Weaken extends NpcMagicStrategy {
        private Weaken() {
            super(getDefinition("Weaken"));
        }
    }

    private static class Curse extends NpcMagicStrategy {
        private Curse() {
            super(getDefinition("Curse"));
        }
    }
}
