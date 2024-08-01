package com.osroyale.content.itemaction.impl;

import com.osroyale.content.itemaction.ItemAction;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.net.packet.out.SendInputAmount;
import com.osroyale.util.Utility;

public class CelestialRing extends ItemAction {

    public static final short UNCHARGED_RING = 25539;
    public static final short CHARGED_RING = 25541;
    public static final short STARDUST = 25527;

    @Override
    public String name() {
        return "Celestial ring";
    }

    @Override
    public boolean itemOnItem(Player player, Item first, Item second) {
        if ((first.getId() != UNCHARGED_RING && second.getId() != UNCHARGED_RING) && (first.getId() != CHARGED_RING && second.getId() != CHARGED_RING)) {
            return false;
        }

        if(first.getId() == STARDUST || second.getId() == STARDUST) {
            if (first.getId() == UNCHARGED_RING || second.getId() == UNCHARGED_RING) {
                player.send(new SendInputAmount("How many charges would you like add? (0-"+player.inventory.computeAmountForId(STARDUST)+")", 10, input -> charge(player, Integer.parseInt(input), UNCHARGED_RING)));
            } else {
                player.send(new SendInputAmount("How many charges would you like add? (0-"+player.inventory.computeAmountForId(STARDUST)+")", 10, input -> charge(player, Integer.parseInt(input), CHARGED_RING)));
            }
            return true;
        }

        return false;
    }

    @Override
    public boolean inventory(Player player, Item item, int opcode) {
        if (item.getId() != UNCHARGED_RING && item.getId() != CHARGED_RING) {
            return false;
        }

        if (opcode == 1 && item.getId() == CHARGED_RING) {
            player.equipment.equip(new Item(CHARGED_RING));
            return true;
        }
        if (opcode == 2) {
            player.send(new SendInputAmount("How many charges would you like add? (0-"+player.inventory.computeAmountForId(STARDUST)+")", 10, input -> charge(player, Integer.parseInt(input), item.getId())));
            return true;
        }

        return false;
    }

    @Override
    public boolean equipment(Player player, Item item, int opcode) {
        if (item.getId() != CHARGED_RING) {
            return false;
        }
        if (opcode == 1) {
            check(player);
            return true;
        }
        return true;
    }

    @Override
    public boolean drop(Player player, Item item) {
        if (item.getId() != CHARGED_RING) {
            return false;
        }
        if (player.celestialRingCharges > 0) {
            player.inventory.remove(CHARGED_RING);
            player.inventory.add(UNCHARGED_RING, 1);
            player.inventory.add(STARDUST, player.celestialRingCharges);
            player.celestialRingCharges = 0;
            player.message("You uncharge your "+name()+".");
        }

        return true;
    }

    public static void check(Player player) {
        player.message("You have " + Utility.formatDigits(player.celestialRingCharges) + " charges in your Celestial ring.");
    }

    private void charge(Player player, int amount, int itemId) {
        if (amount > player.inventory.computeAmountForId(STARDUST)) {
            amount = player.inventory.computeAmountForId(STARDUST);
        }
        if (amount > 0) {
            if (itemId == UNCHARGED_RING) {
                player.inventory.remove(UNCHARGED_RING, 1);
                player.inventory.add(CHARGED_RING, 1);
            }
            player.inventory.remove(STARDUST, amount);
            player.inventory.refresh();
            player.celestialRingCharges += amount;
            player.message("You added " + amount + " charges to your "+name()+"");
        }
    }

}
