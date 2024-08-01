package com.osroyale.game.world.entity.combat.attack.listener.item;

import com.osroyale.game.world.entity.combat.attack.listener.ItemCombatListenerSignature;
import com.osroyale.game.world.entity.combat.attack.listener.SimplifiedListener;
import com.osroyale.game.world.entity.combat.effect.impl.CombatPoisonEffect;
import com.osroyale.game.world.entity.combat.effect.impl.CombatVenomEffect;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.containers.equipment.Equipment;
import com.osroyale.util.RandomUtils;

@ItemCombatListenerSignature(requireAll = false, items = { 12_931, 13_197, 13_199 })
public class ZulrahHelm extends SimplifiedListener<Player> {

    @Override
    public void hit(Player attacker, Mob defender, Hit hit) {
        if (!defender.isNpc()) {
            return;
        }

        Item helm = attacker.equipment.get(Equipment.HELM_SLOT);

        if (helm == null) {
            return;
        }

        if (helm.matchesId(12_931)) {
            serp(attacker, defender, this);
        } else if (helm.matchesId(13_197)) {
            tanz(attacker, defender, this);
        } else if (helm.matchesId(13_199)) {
            magma(attacker, defender, this);
        }
    }
    
    private static void serp(Player attacker, Mob defender, ZulrahHelm listener) {
        if (attacker.serpentineHelmCharges > 0) {
            attacker.serpentineHelmCharges -= 2;
            boolean poisonous = false;

            if (attacker.equipment.hasWeapon()) {
                poisonous = CombatPoisonEffect.getPoisonType(attacker.equipment.getWeapon()).isPresent();
            }

            if (!poisonous && attacker.equipment.hasAmmo()) {
                poisonous = CombatPoisonEffect.getPoisonType(attacker.equipment.get(Equipment.ARROWS_SLOT)).isPresent();
            }

            if (CombatVenomEffect.isVenomous(attacker.equipment.getWeapon())) {
                defender.venom();
            } else if (RandomUtils.success(poisonous ? 0.50 : 1 / 6.0)) {
                defender.venom();
            }
        }

        if (attacker.serpentineHelmCharges == 0) {
            attacker.message("Your Serpentine helm is out of charges.");
            attacker.equipment.set(Equipment.HELM_SLOT, new Item(12_929), true);
            attacker.getCombat().removeListener(listener);
        }
    }

    private static void tanz(Player attacker, Mob defender, ZulrahHelm listener) {
        if (attacker.tanzaniteHelmCharges > 0) {
            attacker.tanzaniteHelmCharges -= 2;
            boolean poisonous = false;

            if (attacker.equipment.hasWeapon()) {
                poisonous = CombatPoisonEffect.getPoisonType(attacker.equipment.getWeapon()).isPresent();
            }

            if (!poisonous && attacker.equipment.hasAmmo()) {
                poisonous = CombatPoisonEffect.getPoisonType(attacker.equipment.get(Equipment.ARROWS_SLOT)).isPresent();
            }

            if (CombatVenomEffect.isVenomous(attacker.equipment.getWeapon())) {
                defender.venom();
            } else if (RandomUtils.success(poisonous ? 0.50 : 1 / 6.0)) {
                defender.venom();
            }
        }

        if (attacker.tanzaniteHelmCharges == 0) {
            attacker.message("Your Tanzanite helm is out of charges.");
            attacker.equipment.set(Equipment.HELM_SLOT, new Item(13_196), true);
            attacker.getCombat().removeListener(listener);
        }
    }

    private static void magma(Player attacker, Mob defender, ZulrahHelm listener) {
        if (attacker.magmaHelmCharges > 0) {
            attacker.magmaHelmCharges -= 2;
            boolean poisonous = false;

            if (attacker.equipment.hasWeapon()) {
                poisonous = CombatPoisonEffect.getPoisonType(attacker.equipment.getWeapon()).isPresent();
            }

            if (!poisonous && attacker.equipment.hasAmmo()) {
                poisonous = CombatPoisonEffect.getPoisonType(attacker.equipment.get(Equipment.ARROWS_SLOT)).isPresent();
            }

            if (CombatVenomEffect.isVenomous(attacker.equipment.getWeapon())) {
                defender.venom();
            } else if (RandomUtils.success(poisonous ? 0.50 : 1 / 6.0)) {
                defender.venom();
            }
        }

        if (attacker.magmaHelmCharges == 0) {
            attacker.message("Your Magma helm is out of charges.");
            attacker.equipment.set(Equipment.HELM_SLOT, new Item(13_198), true);
            attacker.getCombat().removeListener(listener);
        }
    }
    

}
