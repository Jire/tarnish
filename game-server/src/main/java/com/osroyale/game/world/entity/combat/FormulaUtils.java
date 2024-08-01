package com.osroyale.game.world.entity.combat;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.containers.ItemContainer;
import com.osroyale.game.world.position.Area;

import java.util.Arrays;

import static com.osroyale.util.Items.*;

public class FormulaUtils {
    public static boolean obbyArmour(Player player) {
        ItemContainer eq = player.equipment;
        return ((eq.contains(21298) && eq.contains(21301) && eq.contains(21304)));
    }

    public static boolean hasViggorasChainMace(Player player) {
        return ((player.equipment.contains(22545) && Area.inWilderness(player))) && player.viggorasChainmaceCharges > 0;
    }

    public static boolean hasThammaronSceptre(Player player) {
        ItemContainer eq = player.equipment;
        return (eq.contains(22555) && (Area.inWilderness(player))) && player.thammoranSceptreCharges > 0;
    }

    public static boolean hasCrawsBow(Player player) {
        return ((player.equipment.contains(22550)) && Area.inWilderness(player)) && player.crawsBowCharges > 0;
    }

    public static boolean hasAmuletOfAvarice(Player player) {
        ItemContainer eq = player.equipment;
        return (eq.contains(22557) && Area.inWilderness(player));
    }

    public static boolean hasObbyWeapon(Player player) {
        var eq = player.equipment;
        return eq.contains(6525) || eq.contains(6528) || eq.contains(6523);
    }

    public static boolean voidBase(Player player) {
        return ((player.equipment.contains(8839)) && player.equipment.contains(8840)) || (player.equipment.contains(13072)) && (player.equipment.contains(13073)) && player.equipment.contains(8842);

    }

    public static boolean voidRanger(Player player) {
        return player.equipment.contains(11664) && voidBase(player);
    }

    public static boolean voidMelee(Player player) {
        return player.equipment.contains(11665) && voidBase(player);
    }

    public static boolean voidMagic(Player player) {
        return player.equipment.contains(11663) && voidBase(player);
    }

    public static boolean wearingEliteVoid(Player p) {
        return (p.equipment.contains(11665) || p.equipment.contains(11664) || p.equipment.contains(11663)) && p.equipment.contains(13072) && p.equipment.contains(13073) && p.equipment.contains(8842);
    }

    private static final int[] BLACK_MASK = new int[]{BLACK_MASK_1_, BLACK_MASK_2, BLACK_MASK_3_, BLACK_MASK_4_, BLACK_MASK_5_, BLACK_MASK_6_, BLACK_MASK_7_, BLACK_MASK_8_, BLACK_MASK_9_, BLACK_MASK_10_};
    private static final int[] BLACK_MASK_IMBUED = new int[]{BLACK_MASK_1__I_, BLACK_MASK_2__I_, BLACK_MASK_3__I_, BLACK_MASK_4__I_, BLACK_MASK_5__I_, BLACK_MASK_6__I_, BLACK_MASK_7__I_, BLACK_MASK_8__I_, BLACK_MASK_9__I_, BLACK_MASK_10__I_};

    public static boolean wearingBlackMask(Player player) {
        return Arrays.stream(BLACK_MASK).anyMatch(mask -> player.equipment.contains(mask));
    }

    public static boolean wearingBlackMaskImbued(Player player) {
        return Arrays.stream(BLACK_MASK_IMBUED).anyMatch(mask -> player.equipment.contains(mask));
    }


}
