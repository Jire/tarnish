package com.osroyale.game.world.entity.combat.attack.listener.item;

import com.osroyale.game.Graphic;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.event.impl.ItemClickEvent;
import com.osroyale.game.world.entity.combat.attack.listener.ItemCombatListenerSignature;
import com.osroyale.game.world.entity.combat.attack.listener.NpcCombatListenerSignature;
import com.osroyale.game.world.entity.combat.attack.listener.SimplifiedListener;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.UpdateFlag;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;

/**
 * Author : Settings 08/23/2023
 *  Discord : tettings
 */
@ItemCombatListenerSignature(requireAll = true, items = {80})
public class StarterWhipListener extends SimplifiedListener<Mob> {

    @Override
    public void hit(Mob attacker, Mob defender, Hit hit) {
        final var player = attacker.getPlayer();

        if (player.whipCharges > 0) {
            player.whipCharges--;
        }
        if (player.whipCharges <= 0 && player.equipment.contains(80)) {
            player.message("Your starter whip is out of charges and has degraded into dust.");
            player.equipment.remove(80);
            player.equipment.refresh();
            player.updateFlags.add(UpdateFlag.APPEARANCE);
        }
    }
}
