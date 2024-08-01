package com.osroyale.content.itemaction.impl;

import com.osroyale.content.itemaction.ItemAction;
import com.osroyale.game.world.entity.combat.ranged.RangedAmmunition;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.Utility;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class ToxicBlowpipe extends ItemAction {
    private static final short FULL = 16_383;
    private static final short ZULRAH_SCALES = 12_934;
    private static final short UNCHARGED_BLOWPIPE = 12_924;
    private static final short CHARGED_BLOWPIPE = 12_926;
    private static final DecimalFormat FORMATTER = new DecimalFormat("#.#");

    public ToxicBlowpipe() {
        FORMATTER.setRoundingMode(RoundingMode.FLOOR);
    }

    @Override
    public String name() {
        return "Toxic Blowpipe";
    }

    @Override
    public boolean itemOnItem(Player player, Item first, Item second) {
        Item item = first;

        if ((first.getId() == UNCHARGED_BLOWPIPE && second.getId() != ZULRAH_SCALES)
                || (second.getId() == UNCHARGED_BLOWPIPE && first.getId() != ZULRAH_SCALES)) {
            player.send(new SendMessage("You can't load an uncharged blowpipe!"));
            return true;
        }

        if (first.getId() == UNCHARGED_BLOWPIPE || first.getId() == CHARGED_BLOWPIPE) {
            item = second;
        }

        if (item.getId() == ZULRAH_SCALES) {
            charge(player, item);
            return true;
        }

        RangedAmmunition ammo = RangedAmmunition.find(null, item);
        if (ammo == null || !ammo.isDart()) {
            player.send(new SendMessage("You can't load your blowpipe with this!"));
            return true;
        }

        load(player, item);
        return true;
    }

    @Override
    public boolean inventory(Player player, Item item, int opcode) {
        if (opcode == 2) {
            check(player);
        } else if (opcode == 3) {
            unload(player, item);
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
        uncharge(player);
        return true;
    }

    public static void check(Player player) {
        String ammo = "None";
        if (player.blowpipeDarts != null && player.blowpipeDarts.getAmount() > 0)
            ammo = player.blowpipeDarts.getName() + " x " + player.blowpipeDarts.getAmount();
        String scales = FORMATTER.format(player.blowpipeScales * 100 / FULL) + "%";
        player.send(new SendMessage("Darts: <col=007F00>" + ammo + "</col>. Charges: <col=007F00>" + scales));
    }

    private static void unload(Player player, Item item) {
        if (item.getId() == 12924) {
            int current = player.inventory.computeAmountForId(ZULRAH_SCALES);
            if (Integer.MAX_VALUE - current < 20_000) {
                player.send(new SendMessage("You need to deposit some of your scales first."));
                return;
            }

            player.inventory.remove(item);
            player.inventory.add(new Item(ZULRAH_SCALES, 20_000));
            player.send(new SendMessage("You dismantle your blowpipe and receive 20,000 of Zulra's scales."));
        } else {
            if (player.inventory.getFreeSlots() < 1) {
                player.send(new SendMessage("You need at least one free slot to unload your blowpipe."));
                return;
            }

            Item darts = player.blowpipeDarts;
            if (darts == null) {
                player.send(new SendMessage("You have no darts to unload."));
                return;
            }

            player.inventory.add(darts);
            player.blowpipeDarts = null;
            player.send(new SendMessage("You unload the darts out of your blowpipe."));
        }
    }

    private static void charge(Player player, Item scales) {
        if (player.blowpipeScales >= FULL) {
            player.send(new SendMessage("Your blowpipe is already fully charged."));
            return;
        }

        if (Integer.MAX_VALUE - scales.getAmount() < FULL) {
            scales = scales.copy();
            scales.setAmount((int) (FULL - player.blowpipeScales));
        }

        if (player.blowpipeScales + scales.getAmount() > FULL) {
            scales = scales.copy();
            scales.setAmount((int) (FULL - player.blowpipeScales));
        }

        player.blowpipeScales += scales.getAmount();
        player.inventory.remove(scales);
        player.inventory.replace(UNCHARGED_BLOWPIPE, CHARGED_BLOWPIPE, true);
        player.send(new SendMessage(String.format("You load %s scales into your blowpipe.", Utility.formatDigits(scales.getAmount()))));
    }

    private static void uncharge(Player player) {
        if (player.blowpipeScales <= 0) {
            player.send(new SendMessage("Your blowpipe doesn't have any charges."));
            return;
        }

        if (player.blowpipeDarts != null && player.blowpipeDarts.getAmount() > 0) {
            player.send(new SendMessage("You need to unload your blowpipe first."));
            return;
        }

        Item scales = new Item(ZULRAH_SCALES, (int) player.blowpipeScales);
        if (!player.inventory.hasCapacityFor(scales)) {
            player.send(new SendMessage("You need more inventory space to do this."));
            return;
        }

        player.blowpipeScales = 0;
        player.inventory.add(scales);
        player.inventory.replace(CHARGED_BLOWPIPE, UNCHARGED_BLOWPIPE, true);
        player.send(new SendMessage(String.format("You uncharged your blowpipe and received %s of Zulra's scales.", Utility.formatDigits(scales.getAmount()))));
    }

    private static void load(Player player, Item ammo) {
        if (player.blowpipeDarts != null && player.blowpipeDarts.getAmount() >= FULL) {
            player.send(new SendMessage("Your blowpipe is already fully loaded."));
            return;
        }

        if (ammo.getAmount() > FULL) {
            ammo = new Item(ammo.getId(), FULL);
        }

        if (player.blowpipeDarts == null) {
            player.blowpipeDarts = ammo;
            player.inventory.remove(ammo);
            player.send(new SendMessage(String.format("You load your blowpipe with %s %ss.", Utility.formatDigits(ammo.getAmount()), ammo.getName())));
            return;
        }

        if (player.blowpipeDarts.getId() != ammo.getId()) {
            player.send(new SendMessage("You need to unload your blowpipe before adding a different type of dart!"));
            return;
        }

        int remove;
        if (player.blowpipeDarts.getAmount() + ammo.getAmount() > FULL) {
            remove = FULL - player.blowpipeDarts.getAmount();
        } else {
            remove = ammo.getAmount();
        }

        player.blowpipeDarts.incrementAmountBy(remove);
        player.inventory.remove(ammo.getId(), remove);
        player.send(new SendMessage(String.format("You load your blowpipe with %s %s.", Utility.formatDigits(ammo.getAmount()), ammo.getName())));
    }

}
