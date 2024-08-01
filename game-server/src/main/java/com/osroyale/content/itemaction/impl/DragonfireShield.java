package com.osroyale.content.itemaction.impl;

import com.osroyale.content.itemaction.ItemAction;
import com.osroyale.game.world.entity.combat.Combat;
import com.osroyale.game.world.entity.combat.strategy.player.custom.DragonfireShieldStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;

public class DragonfireShield extends ItemAction {

    @Override
    public String name() {
        return "Dragonfire shield";
    }

    @Override
    public boolean inventory(Player player, Item item, int opcode) {
        if (opcode == 2) {
            inspect(player);
        } else if (opcode == 3) {
            empty(player);
        }
        return true;
    }

    private void empty(Player player) {
        if (player.dragonfireCharges > 0) {
            player.dragonfireCharges = 0;
            player.graphic(1160);
            player.inventory.replace(11283, 11284, true);
            player.message("You clear your remaining charges.");
        } else {
            player.message("You have no remaining charges.");
        }
    }

    private void inspect(Player player) {
        player.message("You have " + player.dragonfireCharges + " charges remaining.");
    }

    @Override
    public boolean equipment(Player player, Item item, int opcode) {
        if (opcode == 1) {
            long remaining = System.currentTimeMillis() - player.dragonfireUsed;
            if (remaining < 120_000) {
                player.message("My shield still needs " + (120 - remaining / 1000) + " seconds to recharge.");
                return true;
            }
            if (player.dragonfireCharges == 0) {
                player.message("You have no charges to do this!");
                return true;
            }
            Mob defender = player.getCombat().getDefender();
            if (defender == null && player.getCombat().isAttacking()) {
                defender = player.getCombat().getLastVictim();
            }
            if (defender == null && player.getCombat().isUnderAttack()) {
                defender = player.getCombat().getLastAggressor();
            }
            if (defender == null) {
                player.message("You are not in combat with anything...");
                return true;
            }
            if (defender.isDead() || defender.getCurrentHealth() == 0) {
                player.message("He dead tho...");
                return true;
            }
            player.dragonfireUsed = System.currentTimeMillis();

            final Combat<Player> combat = player.getCombat();
            combat.performChecks(defender);
            combat.submitStrategy(defender, DragonfireShieldStrategy.get());
        }
        return true;
    }

}
