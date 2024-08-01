package com.osroyale.util.parser.impl;

import com.google.gson.JsonObject;
import com.osroyale.game.world.entity.mob.npc.drop.NpcDrop;
import com.osroyale.game.world.entity.mob.npc.drop.NpcDropChance;
import com.osroyale.game.world.entity.mob.npc.drop.NpcDropManager;
import com.osroyale.game.world.entity.mob.npc.drop.NpcDropTable;
import com.osroyale.game.world.items.ItemDefinition;
import com.osroyale.util.parser.GsonParser;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import org.jire.tarnishps.OldToNew;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Loads npc drops on startup.
 *
 * @author Daniel
 */
public class NpcDropParser extends GsonParser {

    public NpcDropParser() {
        super("def/npc/npc_drops", false);
    }

    @Override
    protected void parse(JsonObject data) {
        int[] npcIds = builder.fromJson(data.get("id"), int[].class);
        IntSet newNpcIds = new IntOpenHashSet(npcIds.length);
        for (int id : npcIds) {
            int newId = OldToNew.get(id);
            if (newId != -1) {
                newNpcIds.add(newId);
            } else {
                newNpcIds.add(id);
            }
        }
        npcIds = newNpcIds.toIntArray();

        boolean rareDropTable = data.get("rare_table").getAsBoolean();
        NpcDrop[] npcDrops = builder.fromJson(data.get("drops"), NpcDrop[].class);

        List<NpcDrop> always = new LinkedList<>();
        List<NpcDrop> common = new LinkedList<>();
        List<NpcDrop> uncommon = new LinkedList<>();
        List<NpcDrop> rare = new LinkedList<>();
        List<NpcDrop> veryRare = new LinkedList<>();

        for (NpcDrop drop : npcDrops) {
            ItemDefinition itemDefinition = ItemDefinition.get(drop.id);

            if (itemDefinition == null)
                continue;

            if (drop.id == 12073) {// elite clue
                veryRare.add(drop);
                drop.setType(NpcDropChance.VERY_RARE);
                continue;
            }

            if (drop.id == 2722) {// hard clue
                rare.add(drop);
                drop.setType(NpcDropChance.RARE);
                continue;
            }

            if (drop.id == 2801) {// medium clue
                uncommon.add(drop);
                drop.setType(NpcDropChance.UNCOMMON);
                continue;
            }

            if (drop.id == 2677) {// easy clue
                common.add(drop);
                drop.setType(NpcDropChance.COMMON);
                continue;
            }

            if (drop.id == 11942) {// ecu key
                veryRare.add(drop);
                drop.setType(NpcDropChance.VERY_RARE);
                continue;
            }

            if (drop.id == 1436) {
                drop.id = 7936;
            }

            if (drop.id == 1437) {
                drop.id = 7937;
            }

            if (drop.type == NpcDropChance.ALWAYS) {
                always.add(drop);
            } else if (drop.type == NpcDropChance.COMMON) {
                common.add(drop);
            } else if (drop.type == NpcDropChance.UNCOMMON) {
                uncommon.add(drop);
            } else if (drop.type == NpcDropChance.RARE) {
                rare.add(drop);
            } else if (drop.type == NpcDropChance.VERY_RARE) {
                veryRare.add(drop);
            }
        }

        //Custom drops
//        for (int npc : npcIds) {
//            if (NpcDefinition.get(npc).getCombatLevel() > 10) {
//                uncommon.add(new NpcDrop(985, NpcDropChance.UNCOMMON, 1, 1, 1));
//                uncommon.add(new NpcDrop(987, NpcDropChance.UNCOMMON, 1, 1, 1));
//                rare.add(new NpcDrop(405, NpcDropChance.RARE, 1, 1, 1));
//            }
//        }


        Arrays.sort(npcDrops);

        NpcDropTable table = new NpcDropTable(npcIds, rareDropTable, npcDrops, always.toArray(new NpcDrop[0]), common.toArray(new NpcDrop[0]), uncommon.toArray(new NpcDrop[0]), rare.toArray(new NpcDrop[0]), veryRare.toArray(new NpcDrop[0]));

        if (data.has("roll-data")) {
            int[] rollData = builder.fromJson(data.get("roll-data"), int[].class);
            table.setRollData(rollData);
        }

        for (int id : npcIds) {
            NpcDropManager.NPC_DROPS.put(id, table);
        }
    }
}
