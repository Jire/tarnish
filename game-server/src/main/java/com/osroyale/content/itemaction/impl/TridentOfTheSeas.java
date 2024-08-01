package com.osroyale.content.itemaction.impl;

import com.osroyale.content.ItemCreation;
import com.osroyale.content.itemaction.ItemAction;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.util.Items;
import com.osroyale.util.Utility;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class TridentOfTheSeas extends ItemAction {
    private static final short TRIDENT_ID = 11907;
    private static final short CHARGE_LIMIT = 2500;
    private static final DecimalFormat FORMATTER = new DecimalFormat("#.#");

    public TridentOfTheSeas() {
        FORMATTER.setRoundingMode(RoundingMode.FLOOR);
    }

    @Override
    public String name() {
        return "Trident of the Seas";
    }

    @Override
    public boolean itemOnItem(Player player, Item first, Item second) {
        if (first.getId() != TRIDENT_ID && second.getId() != TRIDENT_ID) {
            return false;
        }

        if (ItemCreation.CreationData.forItems(first, second).isPresent()) {
            if (player.tridentSeasCharges > 0) {
                player.message("You must empty your trident charges before doing this.");
                return true;
            }
            return false;
        }

        if (player.tridentSeasCharges >= CHARGE_LIMIT) {
            player.message("Your Trident of the Seas is already fully charged.");
            return true;
        }

        int id1 = first.getId();
        int id2 = second.getId();

        if (id1 == TRIDENT_ID && id2 != TRIDENT_ID)
            if (id2 != 560 && id2 != 562 && id2 != 554 && id2 != 995)
                return true;

        if (id2 == TRIDENT_ID && id1 != TRIDENT_ID)
            if (id1 != 560 && id1 != 562 && id1 != 554 && id1 != 995)
                return true;

        int death = player.inventory.computeAmountForId(560);
        int chaos = player.inventory.computeAmountForId(562);
        int fire = player.inventory.computeAmountForId(554);
        int coins = player.inventory.computeAmountForId(995);

        if (death >= 1 && chaos >= 1 && fire >= 5 && coins >= 10) {
            int minF = fire / 5;
            int minC = coins / 10;
            int charges = Math.min(Math.min(death, chaos), Math.min(minF, minC));

            if (charges > CHARGE_LIMIT)
                charges = CHARGE_LIMIT;

            if (charges > CHARGE_LIMIT - player.tridentSeasCharges)
                charges = CHARGE_LIMIT - player.tridentSeasCharges;

            if (charges > 0) {
                player.inventory.remove(new Item(560, charges), -1, false);
                player.inventory.remove(new Item(562, charges), -1, false);
                player.inventory.remove(new Item(554, charges * 5), -1, false);
                player.inventory.remove(new Item(995, charges * 10), -1, false);
                player.inventory.refresh();
                player.tridentSeasCharges += charges;
                player.message("You added " + charges + " charges to your Trident of the Seas");
            }
        } else {
            player.message("You need at least 1 x death rune, 1 x chaos rune, 5 x fire runes and 10gp for one charge.", "Your Trident of the Seas can hold ");
        }
        return true;
    }

    @Override
    public boolean inventory(Player player, Item item, int opcode) {
        if (item.getId() != TRIDENT_ID) {
            return false;
        }
        if (opcode == 2) {
            check(player);
            return true;
        }
        if (opcode == 3) {
            uncharge(player);
            return true;
        }
        return true;
    }

    @Override
    public boolean equipment(Player player, Item item, int opcode) {
        if (item.getId() != TRIDENT_ID) {
            return false;
        }
        if (opcode == 1) {
            check(player);
            return true;
        }
        return true;
    }

    private static void check(Player player) {
        player.message("You have " + Utility.formatDigits(player.tridentSeasCharges) + " charges in your Trident of the Seas.");
    }

    private static void uncharge(Player player) {
        if (player.tridentSeasCharges < 1) {
            return;
        }

        int amount = player.tridentSeasCharges;
        if (!player.inventory.hasCapacityFor(new Item(560, amount), new Item(562, amount), new Item(554, amount * 5))) {
            player.message("You don't have enough inventory space to uncharge your trident.");
            return;
        }

        player.inventory.add(new Item(560, amount), -1, false);
        player.inventory.add(new Item(562, amount), -1, false);
        player.inventory.add(new Item(554, amount * 5), -1, false);
        player.inventory.refresh();
        player.tridentSeasCharges = 0;
        player.message("You uncharge your trident.");
    }

}
