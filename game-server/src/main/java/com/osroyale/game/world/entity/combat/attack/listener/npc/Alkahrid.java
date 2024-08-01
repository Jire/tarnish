package com.osroyale.game.world.entity.combat.attack.listener.npc;

import com.osroyale.game.world.entity.combat.CombatType;
import com.osroyale.game.world.entity.combat.attack.listener.NpcCombatListenerSignature;
import com.osroyale.game.world.entity.combat.attack.listener.SimplifiedListener;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.util.Utility;

/** @author Daniel */
@NpcCombatListenerSignature(npcs = {3103})
public class Alkahrid extends SimplifiedListener<Npc> {

    @Override
    public void block(Mob attacker, Npc defender, Hit hit, CombatType combatType) {
        if (!attacker.isPlayer())
            return;

        int currentHealth = defender.getCurrentHealth();
        int maximumHealth = defender.getMaximumHealth();

        if (currentHealth == maximumHealth) {
            Player player = attacker.getPlayer();

            for (Npc monster : player.viewport.getNpcsInViewport()) {
                if (monster.id != 3103) continue;
                if (monster.equals(defender)) continue;
                if (monster.getCombat().inCombat() && monster.getCombat().getDefender() != null) continue;
                if (!Utility.within(attacker.getPosition(), monster.getPosition(), 10)) continue;
                monster.speak("Brother, I will help thee with this infidel!");
                monster.getCombat().attack(attacker);
            }
        }
    }
}
