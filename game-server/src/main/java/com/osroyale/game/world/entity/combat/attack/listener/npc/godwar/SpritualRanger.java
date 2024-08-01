package com.osroyale.game.world.entity.combat.attack.listener.npc.godwar;

import com.osroyale.game.Animation;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.world.entity.combat.attack.listener.NpcCombatListenerSignature;
import com.osroyale.game.world.entity.combat.attack.listener.SimplifiedListener;
import com.osroyale.game.world.entity.combat.attack.listener.npc.Dessourt;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcMeleeStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcRangedStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;

import static com.osroyale.game.world.entity.combat.CombatUtil.createStrategyArray;
import static com.osroyale.game.world.entity.combat.projectile.CombatProjectile.getDefinition;

/**
 * @author Daniel
 */
@NpcCombatListenerSignature(npcs = {3160, 2211})
public class SpritualRanger extends SimplifiedListener<Npc> {

    private static RangedAttack RANGED = new RangedAttack();

    @Override
    public void start(Npc attacker, Mob defender, Hit[] hits) {
        attacker.setStrategy(RANGED);
    }

    private static class RangedAttack extends NpcRangedStrategy {
        private RangedAttack() {
            super(getDefinition("Spirtual Ranger"));
        }

        @Override
        public Animation getAttackAnimation(Npc attacker, Mob defender) {
            return new Animation(426, UpdatePriority.VERY_HIGH);
        }
    }
}
