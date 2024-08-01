package com.osroyale.content.activity.impl.warriorguild;

import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.game.world.entity.mob.player.Player;

/**
 * This class handles util methods for the Warrior's activity.
 *
 * @author Daniel
 */
public class WarriorGuildUtility {

    /** The warrior guild animator armour data .*/
    static final int[][] ARMOUR = {{1155, 1117, 1075}, {1153, 1115, 1067}, {1157, 1119, 1069}, {1165, 1125, 1077}, {1159, 1121, 1071}, {1161, 1123, 1073}, {1163, 1127, 1079}};

    /** The warrior guild defenders. */
    private static final int[] DEFENDER = new int[]{8844, 8845, 8846, 8847, 8848, 8849, 8850, 12954};

    /** The warrior guild animated npcs. */
    static final int[] ANIMATED = {2450, 2451, 2452, 2453, 2454, 2455, 2456};

    /** The warrior guild cyclops npcs. */
    public static final int[] CYCLOPS = {2463, 2464, 2465, 2466, 2467, 2468};

    /** Gets the defender tier index. */
    static int getDefenderIndex(Player player) {
        int foundIndex = 0;
        for (int index = 0; index < DEFENDER.length; index++) {
            if (player.inventory.contains(DEFENDER[index]) || player.equipment.contains(DEFENDER[index]))
                foundIndex = index;
        }
        return foundIndex;
    }

    /**  Gets the defender index to drop for the player. */
    public static int getDefender(Player player) {
        int foundIndex = -1;
        for (int i = 0; i < DEFENDER.length; i++) {
            if (player.inventory.contains(DEFENDER[i]) || player.equipment.contains(DEFENDER[i]))
                foundIndex = i;
        }
        if (foundIndex != 7)
            foundIndex++;
        return DEFENDER[foundIndex];
    }

    /** Checks if the player has a complete armour set to activate the animator. */
    public static int contains(Player player, int itemId) {
        int itemIndex = -1;
        for (int index = 0; index < ARMOUR.length; index++) {
            for (int j = 0; j < ARMOUR[index].length; j++) {
                if (itemId == ARMOUR[index][j]) {
                    itemIndex = index;
                    for (int k = 0; k < ARMOUR[index].length; k++) {
                        if (!player.inventory.contains(ARMOUR[index][k])) {
                            player.send(new SendMessage("You need a complete armour set to do this!"));
                            return -1;
                        }
                    }
                    break;
                }
            }
        }
        return itemIndex;
    }
}
