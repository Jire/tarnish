package com.osroyale.game.world.entity.mob.npc.drop;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.items.ItemDefinition;
import com.osroyale.util.RandomUtils;
import com.osroyale.util.parser.impl.NpcDropParser;

import java.util.LinkedList;
import java.util.List;

/**
 * The class which represents a npc drop table.
 *
 * @author Michael | Chex
 */
public final class NpcDropTable {

    /** The npc ids that share this drop table. */
    public final int[] npcIds;

    /** Determines if this table has access to the rare drop table. */
    private final boolean rareDropTable;

    /** The cached array of {@link NpcDrop}s. */
    public final NpcDrop[] drops;

    public final NpcDrop[][] table = new NpcDrop[5][];

    private int maxRoll = 20_000;

    // common = 1/2
    // uncommon = 1 / 4
    // rare = 1/75
    // very rare = 1/100
    private static final int[] ROLL_DATA = {
        /* very rare roll */
        200,

        /* rare roll */
        350,

        /* uncommon roll */
        7000,

        /* common roll */
        12500,

        /* empty drop roll */
        200
    };

    private int[] rollData;

    /** Constructs a new {@link NpcDropTable}. */
    public NpcDropTable(int[] npcIds, boolean rareDropTable, NpcDrop[] npcDrops, NpcDrop[] always, NpcDrop[] common, NpcDrop[] uncommon, NpcDrop[] rare, NpcDrop[] veryRare) {
        this.npcIds = npcIds;
        this.rareDropTable = rareDropTable;
        this.drops = npcDrops;
        rollData = ROLL_DATA;
        table[0] = veryRare;
        table[1] = rare;
        table[2] = uncommon;
        table[3] = common;
        table[4] = always;
    }

    public List<NpcDrop> generate(Player player, boolean simulated) {
        LinkedList<NpcDrop> items = new LinkedList<>();
        int roll = RandomUtils.inclusive(maxRoll);
        if (player.equipment.hasRow()) {
            int old = roll;
            roll = PlayerRight.dropRateIncrease(player, roll);

            if (roll < rollData[0] && old >= rollData[0]) {
                if (!simulated) {
                    player.message("Very Rare drop table accessed by ring");
                    player.playerAssistant.useROW();
                }
            } else if (roll < rollData[1] && old >= rollData[1]) {
                if (!simulated) {
                    player.message("Rare drop table accessed by ring");
                    player.playerAssistant.useROW();
                }
            } else {
                /* undo the ring effect since both tables weren't accessed */
                roll = old;
            }
        }

        for (int chance = 0, index = 0; index < rollData.length - 1; index++) {
            chance += rollData[index];

            if (rollData[index] == 0 || roll >= chance) {
                continue;
            }

            if (table[index].length > 0) {
                items.addFirst(RandomUtils.random(table[index]));
                break;
            }
        }

        for (NpcDrop drop : table[4]) {
            items.addFirst(drop);
        }

        return items;
    }

    public void setRollData(int[] rollData) {
        this.rollData = rollData;
        maxRoll = 0;
        for (int roll : rollData) {
            maxRoll += roll;
        }
    }

    public static void main(String[] args) {
        int npcId = 7241;

        /*
         *
         *  DONT USE RATES LESS THAN 100!!!
         *
         * The ring of wealth works best with values larger than 100
         * since 3% of 100 is 3, anything lower than 100 will fuck up ROW
         *
         */

        int[] customRolls = {
            /* very rare roll */
            200,

            /* rare roll */
            350,

            /* uncommon roll */
            7000,

            /* common roll */
            12500,

            /* empty drop roll */
            200
        };

        ItemDefinition.createParser().run();
        new NpcDropParser().run();

        NpcDropTable table = NpcDropManager.NPC_DROPS.get(npcId);

        System.out.println();
        System.out.println("Table lengths (vr, r, uc, c): ");
        for (int index = 0; index < table.table.length - 1; index++) {
            System.out.print(table.table[index].length + " ");
        }
        System.out.println("\n");
        System.out.println("--------------------------------------------------");
        System.out.println("Current rolls: \t \t \t \t Custom rolls:");
        printRates(table.table, table.rollData, customRolls);
        System.out.println();
    }

    private static void printRates(NpcDrop[][] tables, int[] rollData, int[] rollData2) {
        long maxRoll = 0, maxRoll2 = 0;

        for (int index = 0; index < rollData.length; index++) {
            maxRoll += rollData[index];
            maxRoll2 += rollData2[index];
        }

        System.out.println("--------------------------------------------------");
        System.out.println("Very Rare ---- " + ((long) rollData[0] * 100_00 / maxRoll) / 100.0 + "% \t\t Very Rare ---- " + ((long) rollData2[0] * 100_00 / maxRoll2) / 100.0 + "%");
        System.out.println("Rare --------- " + ((long) rollData[1] * 100_00 / maxRoll) / 100.0 + "% \t\t Rare --------- " + ((long) rollData2[1] * 100_00 / maxRoll2) / 100.0 + "%");
        System.out.println("Uncommon ----- " + ((long) rollData[2] * 100_00 / maxRoll) / 100.0 + "% \t\t Uncommon ----- " + ((long) rollData2[2] * 100_00 / maxRoll2) / 100.0 + "%");
        System.out.println("Common ------- " + ((long) rollData[3] * 100_00 / maxRoll) / 100.0 + "% \t\t Common ------- " + ((long) rollData2[3] * 100_00 / maxRoll2) / 100.0 + "%");
        System.out.println("Empty Drop --- " + ((long) rollData[4] * 100_00 / maxRoll) / 100.0 + "% \t\t Empty Drop --- " + ((long) rollData2[4] * 100_00 / maxRoll2) / 100.0 + "%");
        System.out.println("--------------------------------------------------");
        System.out.println("Very Rare ---- " + ((long) rollData[0] * 100_00 / maxRoll) / tables[0].length / 100.0 + "% \t\t Very Rare ---- " + ((long) rollData2[0] * 100_00 / maxRoll2) / tables[0].length / tables[0].length / 100.0 + "%");
        System.out.println("Rare --------- " + ((long) rollData[1] * 100_00 / maxRoll) / tables[1].length / 100.0 + "% \t\t Rare --------- " + ((long) rollData2[1] * 100_00 / maxRoll2) / tables[0].length / tables[1].length / 100.0 + "%");
        System.out.println("Uncommon ----- " + ((long) rollData[2] * 100_00 / maxRoll) / tables[2].length / 100.0 + "% \t\t Uncommon ----- " + ((long) rollData2[2] * 100_00 / maxRoll2) / tables[0].length / tables[2].length / 100.0 + "%");
        System.out.println("Common ------- " + ((long) rollData[3] * 100_00 / maxRoll) / tables[3].length / 100.0 + "% \t\t Common ------- " + ((long) rollData2[3] * 100_00 / maxRoll2) / tables[0].length / tables[3].length / 100.0 + "%");
        System.out.println("--------------------------------------------------");
    }

}
