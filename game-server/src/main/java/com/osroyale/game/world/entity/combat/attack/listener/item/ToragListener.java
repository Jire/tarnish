package com.osroyale.game.world.entity.combat.attack.listener.item;

import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.game.Graphic;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.world.entity.combat.attack.listener.ItemCombatListenerSignature;
import com.osroyale.game.world.entity.combat.attack.listener.NpcCombatListenerSignature;
import com.osroyale.game.world.entity.combat.attack.listener.SimplifiedListener;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.util.Utility;

/**
 * Handles the Torag's armor effects to the assigned npc and item ids.
 *
 * @author Daniel
 */
@NpcCombatListenerSignature(npcs = {1676})
@ItemCombatListenerSignature(requireAll = true, items = {4745, 4747, 4749, 4751})
public class ToragListener extends SimplifiedListener<Mob> {

    @Override
    public void hit(Mob attacker, Mob defender, Hit hit) {
        if (defender.isPlayer() && hit.getDamage() > 1) {
            boolean success = Utility.random(100) <= 25;

            if (!success)
                return;

            Player player = defender.getPlayer();
            int energy = player.runEnergy;
            int drain = energy < 50 ? 10 : 20;

            energy -= drain;

            if (energy < 0)
                energy = 0;

            player.runEnergy = energy;
            player.send(new SendMessage(drain + "% run energy has been drained by " + attacker.getName() + "."));
            player.graphic(new Graphic(399, UpdatePriority.VERY_HIGH));

            if (attacker.isPlayer()) {
                attacker.getPlayer().send(new SendMessage("You have drained " + drain +"% of " + defender.getName() + "'s run energy."));
            }
        }

        super.hit(attacker, defender, hit);
    }
}
