package com.osroyale.content.itemaction.impl;

import com.osroyale.content.itemaction.ItemAction;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.ItemDefinition;
import com.osroyale.net.packet.out.SendInputAmount;
import com.osroyale.util.Utility;

public class CrawsBow extends ItemAction {

    public static final short CRAWS_UNCHARGED_ID = 22547;
    public static final short CRAWS_CHARGED_ID = 22550;
    public static final short ETHER_ID = 21820;

    @Override
    public String name() {
        return "Craw's bow";
    }

    @Override
    public boolean itemOnItem(Player player, Item first, Item second) {
        if ((first.getId() != CRAWS_UNCHARGED_ID && second.getId() != CRAWS_UNCHARGED_ID) && (first.getId() != CRAWS_CHARGED_ID && second.getId() != CRAWS_CHARGED_ID)) {
            return false;
        }

        if(first.getId() == ETHER_ID || second.getId() == ETHER_ID) {
            if (first.getId() == CRAWS_UNCHARGED_ID || second.getId() == CRAWS_UNCHARGED_ID) {
                if(!player.inventory.contains(ETHER_ID, 1000)) {
                    player.message("You need at least 1000 "+ ItemDefinition.get(ETHER_ID).getName() +" to charge the "+name()+".");
                    return true;
                }
                player.inventory.remove(ETHER_ID, 1000);
                player.inventory.remove(CRAWS_UNCHARGED_ID);
                player.inventory.add(CRAWS_CHARGED_ID, 1);
                player.inventory.refresh();
            } else {
                System.out.println("Recharge craws bow");
                player.send(new SendInputAmount("How many charges would you like add? (0-"+player.inventory.computeAmountForId(ETHER_ID)+")", 10, input -> charge(player, Integer.parseInt(input))));
            }
            return true;
        }

        return false;
    }

    @Override
    public boolean inventory(Player player, Item item, int opcode) {
        if (item.getId() != CRAWS_CHARGED_ID) {
            return false;
        }

        if (opcode == 2) {
            check(player);
            return true;
        }

        return false;
    }

    @Override
    public boolean equipment(Player player, Item item, int opcode) {
        if (item.getId() != CRAWS_CHARGED_ID) {
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
        if (item.getId() != CRAWS_CHARGED_ID) {
            return false;
        }

        player.inventory.remove(CRAWS_CHARGED_ID);
        player.inventory.add(CRAWS_UNCHARGED_ID, 1);
        player.inventory.add(ETHER_ID, 1000 + player.crawsBowCharges);
        player.crawsBowCharges = 0;

        player.message("You uncharge your "+name()+".");

        return true;
    }

    private void check(Player player) {
        player.message("You have " + Utility.formatDigits(player.crawsBowCharges) + " charges in your "+name()+".");
    }

    private void charge(Player player, int amount) {
        if (amount > player.inventory.computeAmountForId(ETHER_ID)) {
            amount = player.inventory.computeAmountForId(ETHER_ID);
        }
        if (amount > 0) {
            player.inventory.remove(ETHER_ID, amount);
            player.inventory.refresh();
            player.crawsBowCharges += amount;
            player.message("You added " + amount + " charges to your "+name()+"");
        }
    }

}
