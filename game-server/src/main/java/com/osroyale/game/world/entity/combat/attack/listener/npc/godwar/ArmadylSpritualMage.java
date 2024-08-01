package com.osroyale.game.world.entity.combat.attack.listener.npc.godwar;

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
@NpcCombatListenerSignature(npcs = {3168})
public class ArmadylSpritualMage extends SimplifiedListener<Npc> {

    private static MagicAttack MAGIC = new MagicAttack();

    @Override
    public void start(Npc attacker, Mob defender, Hit[] hits) {
        attacker.setStrategy(MAGIC);
    }

    private static class MagicAttack extends NpcMagicStrategy {
        private MagicAttack() {
            super(getDefinition("Spirtual Mage"));
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            CombatHit combatHit = nextMagicHit(attacker, defender, 15);
            combatHit.setAccurate(true);
            return new CombatHit[]{combatHit};
        }
    }
}
