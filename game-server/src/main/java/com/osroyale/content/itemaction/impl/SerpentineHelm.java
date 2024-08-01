package com.osroyale.content.itemaction.impl;

import com.osroyale.content.itemaction.ItemAction;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.net.packet.out.SendChatBoxInterface;
import com.osroyale.net.packet.out.SendItemOnInterfaceSlot;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.net.packet.out.SendString;
import com.osroyale.util.Utility;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class SerpentineHelm extends ItemAction {
    private static final short FULL = 11_000;

    private static final short ZULRAH_SCALES = 12_934;

    private static final short UNCHARGED_HELM = 12_929;
    private static final short CHARGED_HELM = 12_931;

    private static final short TANZ_MUTAGEN = 13200;
    private static final short MAGMA_MUTAGEN = 13201;

    private static final DecimalFormat FORMATTER = new DecimalFormat("#.#");

    public SerpentineHelm() {
        FORMATTER.setRoundingMode(RoundingMode.FLOOR);
    }

    @Override
    public String name() {
        return "Serpentine helm";
    }

    @Override
    public boolean itemOnItem(Player player, Item first, Item second) {
        Item item = first;

        if (first.getId() == UNCHARGED_HELM || first.getId() == CHARGED_HELM) {
            item = second;
        }

        if ((first.matchesId(CHARGED_HELM) && second.matchesId(TANZ_MUTAGEN))
                || (second.matchesId(CHARGED_HELM) && first.matchesId(TANZ_MUTAGEN))) {
            player.message("You need to uncharge your Serpentine helm first.");
            return true;
        }

        if ((first.matchesId(CHARGED_HELM) && second.matchesId(MAGMA_MUTAGEN))
                || (second.matchesId(CHARGED_HELM) && first.matchesId(MAGMA_MUTAGEN))) {
            player.message("You need to uncharge your Serpentine helm first.");
            return true;
        }

        if ((first.matchesId(UNCHARGED_HELM) && second.matchesId(TANZ_MUTAGEN))
                || (second.matchesId(UNCHARGED_HELM) && first.matchesId(TANZ_MUTAGEN))) {
            player.inventory.remove(TANZ_MUTAGEN);
            player.inventory.replace(UNCHARGED_HELM, 13_196, true);
            player.message(true, "You recolor your Serpentine helm into a Tanzanite helm!");
            return true;
        }

        if ((first.matchesId(UNCHARGED_HELM) && second.matchesId(MAGMA_MUTAGEN))
                || (second.matchesId(UNCHARGED_HELM) && first.matchesId(MAGMA_MUTAGEN))) {
            player.inventory.remove(MAGMA_MUTAGEN);
            player.inventory.replace(UNCHARGED_HELM, 13_198, true);
            player.message(true, "You recolor your Serpentine helm into a Magma helm!");
            return true;
        }

        if (item.getId() == ZULRAH_SCALES) {
            charge(player, item.equals(first) ? second : first, item);
            return true;
        }

        return false;
    }

    @Override
    public boolean inventory(Player player, Item item, int opcode) {
        if (opcode == 3 && item.matchesId(UNCHARGED_HELM)) {
            dismantle(player, item);
            return true;
        }

        if (opcode == 2) {
            check(player);
        }

        return true;
    }

    @Override
    public boolean equipment(Player player, Item item, int opcode) {
        if (opcode == 1) {
            check(player);
        }
        return true;
    }

    @Override
    public boolean drop(Player player, Item item) {
        if (item.matchesId(CHARGED_HELM)) {
            uncharge(player);
            return true;
        }
        if (item.matchesId(UNCHARGED_HELM)) {
            dismantle(player, item);
            return true;
        }
        return false;
    }

    public static void check(Player player) {
        String scales = FORMATTER.format(player.serpentineHelmCharges * 100 / FULL) + "%";
        player.message("Charges: <col=007F00>" + scales);
    }

    private static void charge(Player player, Item helm, Item scales) {
        if (player.serpentineHelmCharges >= FULL) {
            if (helm.matchesId(UNCHARGED_HELM)) {
                player.inventory.replace(UNCHARGED_HELM, CHARGED_HELM, true);
            }
            player.message("Your Serpentine helm is already fully charged.");
            return;
        }

        if (Integer.MAX_VALUE - scales.getAmount() < FULL) {
            scales = scales.copy();
            scales.setAmount(FULL - player.serpentineHelmCharges);
        }

        if (player.serpentineHelmCharges + scales.getAmount() > FULL) {
            scales = scales.copy();
            scales.setAmount(FULL - player.serpentineHelmCharges);
        }

        player.serpentineHelmCharges += scales.getAmount();
        player.inventory.remove(scales);
        player.inventory.replace(UNCHARGED_HELM, CHARGED_HELM, true);
        player.message(String.format("You load %s scales into your Serpentine helm.", Utility.formatDigits(scales.getAmount())));
    }

    private static void uncharge(Player player) {
        Item scales = new Item(ZULRAH_SCALES, player.serpentineHelmCharges);

        if (!player.inventory.hasCapacityFor(scales)) {
            player.send(new SendMessage("You need more inventory space to do this."));
            return;
        }

        player.serpentineHelmCharges = 0;
        player.inventory.add(scales);
        player.inventory.replace(CHARGED_HELM, UNCHARGED_HELM, true);
        player.message(String.format("You uncharged your Serpentine helm and received %s of Zulra's scales.", Utility.formatDigits(scales.getAmount())));
    }

    private static void dismantle(Player player, Item item) {
        player.send(new SendItemOnInterfaceSlot(14171, item, 0));
        player.send(new SendString("Are you sure you want to uncharge this item?", 14174));
        player.send(new SendString("Yes.", 14175));
        player.send(new SendString("No.", 14176));
        player.send(new SendString("", 14177));
        player.send(new SendString("This will replace your helm with 20,000 Zulrah's scales.", 14182));
        player.send(new SendString("", 14183));
        player.send(new SendString(item.getName(), 14184));
        player.send(new SendChatBoxInterface(14170));
        player.attributes.set("UNCHARGE_HELM_KEY", item);
    }

}
