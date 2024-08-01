package com.osroyale.game.world.entity.combat.attack.listener.item;

import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.game.world.entity.combat.CombatType;
import com.osroyale.game.world.entity.combat.attack.listener.ItemCombatListenerSignature;
import com.osroyale.game.world.entity.combat.attack.listener.NpcCombatListenerSignature;
import com.osroyale.game.world.entity.combat.attack.listener.SimplifiedListener;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.hit.HitIcon;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.containers.equipment.Equipment;

/**
 * @author Michael | Chex
 */
@NpcCombatListenerSignature(npcs = {2192})
@ItemCombatListenerSignature(requireAll = false, items = {2550})
public class RingOfRecoilListener extends SimplifiedListener<Mob> {

    @Override
    public void block(Mob attacker, Mob defender, Hit hit, CombatType combatType) {
        if (hit.getDamage() < 1)
            return;

        int recoil = hit.getDamage() < 10 ? 1 : hit.getDamage() / 10;

        if (defender.isNpc()) {
            handleRecoil(attacker, defender, recoil);
            return;
        }

        Player player = defender.getPlayer();
        int charges = player.ringOfRecoil;
        charges -= recoil;

        if (charges <= 0) {
            player.getCombat().removeListener(this);
            player.send(new SendMessage("Your ring of recoil has shattered!"));
            player.equipment.set(Equipment.RING_SLOT, null, true);
            recoil += charges;
            charges = 40;
        }

        player.ringOfRecoil = charges;
        if (recoil > 0)
            handleRecoil(attacker, defender, recoil);
    }

    private void handleRecoil(Mob attacker, Mob defender, int recoil) {
        Hit hit = new Hit(recoil, HitIcon.DEFLECT);
        attacker.damage(hit);
        attacker.getCombat().getDamageCache().add(defender, hit);
    }
}
