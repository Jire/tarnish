package com.osroyale.game.world.entity.combat.attack.listener.npc;

import com.osroyale.game.Animation;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.world.entity.combat.attack.listener.NpcCombatListenerSignature;
import com.osroyale.game.world.entity.combat.attack.listener.SimplifiedListener;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcMagicStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;

import static com.osroyale.game.world.entity.combat.projectile.CombatProjectile.getDefinition;

/**
 * @author Daniel
 */
@NpcCombatListenerSignature(npcs = {443,444,445,446,447})
public class InfernalMage extends SimplifiedListener<Npc> {

    private static MageAttack MAGE = new MageAttack();

    @Override
    public void start(Npc attacker, Mob defender, Hit[] hits) {
        attacker.setStrategy(MAGE);
    }

    private static class MageAttack extends NpcMagicStrategy {
        private MageAttack() {
            super(getDefinition("Fire Bolt"));
        }

        @Override
        public Animation getAttackAnimation(Npc attacker, Mob defender) {
            return new Animation(426, UpdatePriority.VERY_HIGH);
        }
    }
}
