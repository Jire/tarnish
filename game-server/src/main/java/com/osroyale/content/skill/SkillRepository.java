package com.osroyale.content.skill;

import com.osroyale.Config;
import com.osroyale.content.skill.impl.agility.Agility;
import com.osroyale.content.skill.impl.crafting.impl.Gem;
import com.osroyale.content.skill.impl.crafting.impl.Hide;
import com.osroyale.content.skill.impl.fishing.Fishable;
import com.osroyale.content.skill.impl.fishing.FishingSpot;
import com.osroyale.content.skill.impl.fishing.FishingTool;
import com.osroyale.content.skill.impl.fletching.impl.*;
import com.osroyale.content.skill.impl.herblore.GrimyHerb;
import com.osroyale.content.skill.impl.hunter.net.impl.Butterfly;
import com.osroyale.content.skill.impl.hunter.net.impl.Impling;
import com.osroyale.content.skill.impl.mining.OreData;
import com.osroyale.content.skill.impl.prayer.BoneData;
import com.osroyale.content.skill.impl.woodcutting.TreeData;
import com.osroyale.game.world.entity.mob.Direction;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.position.Position;
import com.osroyale.util.RandomUtils;
import com.osroyale.util.Utility;

import java.util.ArrayList;
import java.util.List;

public class SkillRepository {

    private static final List<Integer> SKILLING_ITEMS = new ArrayList<>();
    private static final List<Integer> HERBLORE_ITEMS = new ArrayList<>();
    private static final List<Integer> BONE_ITEMS = new ArrayList<>();

    private static final List<Integer> ESSENCE_ITEMS = new ArrayList<>();


    /** Holds all the impling onSpawn locations. */
    private static final Position[] HUNTER_SPAWN_POSITION = {
            new Position(3081, 3484), new Position(3101, 3484), new Position(3115, 3506),
            new Position(3094, 3453), new Position(3134, 3511), new Position(3180, 3422),
            new Position(3240, 3440), new Position(3287, 3351), new Position(3257, 3247),
            new Position(3221, 3213), new Position(2803, 3445), new Position(2721, 3472),
            new Position(3349, 3271), new Position(3283, 3196), new Position(3574, 3322),
            new Position(2659, 2660), new Position(3377, 3168), new Position(2660, 3706),
            new Position(2318, 3813), new Position(3054, 3514)
    };

    /** Spawns all the skilling npcs. */
    public static void spawn() {
        for (Position position : HUNTER_SPAWN_POSITION) {
            int npc;

            if (RandomUtils.success(.50)) {
                npc = Utility.randomElement(Butterfly.values()).butterfly;
            } else {
                npc = Utility.randomElement(Impling.values()).impling;
            }

            new Npc(npc, position, Config.NPC_WALKING_RADIUS, Mob.DEFAULT_INSTANCE, Direction.NORTH).register();
        }

        for (int[] IMPLING : IMPLINGS) {
            new Npc(IMPLING[0], new Position(IMPLING[1], IMPLING[2]), Config.NPC_WALKING_RADIUS, Mob.DEFAULT_INSTANCE, Direction.NORTH).register();

        }

    }

    /** Loads all the skilling data. */
    public static void load() {
        Gem.load();
        Hide.load();
        Arrow.load();
        Carvable.load();
        Bolt.load();
        Crossbow.load();
        Featherable.load();
        Stringable.load();
        Agility.declare();
        FishingTool.declare();
        Battlestaff.load();
        Fishable.declare();
        FishingSpot.declare();
        spawn();
        declareItems();
    }

    private static void declareItems() {
        for (TreeData tree : TreeData.values()) {
            SKILLING_ITEMS.add(tree.item);
        }
        for (OreData ore : OreData.values()) {
            SKILLING_ITEMS.add(ore.ore);
        }
        for (Fishable fish : Fishable.values()) {
            SKILLING_ITEMS.add(fish.getRawFishId());
        }
        for (GrimyHerb herb : GrimyHerb.values()) {
            HERBLORE_ITEMS.add(herb.getCleanID());
        }
        for (GrimyHerb herb : GrimyHerb.values()) {
            HERBLORE_ITEMS.add(herb.getGrimyID());
        }
        for (BoneData bones : BoneData.values()) {
            BONE_ITEMS.add(bones.getId2());
        }
    }

    public static boolean isSkillingItem(int item) {
        for (int id : SKILLING_ITEMS) {
            if (item == id)
                return true;
        }
        return false;
    }
    public static boolean isHerbloreItem(int item) {
        for (int id : SKILLING_ITEMS) {
            if (item == id)
                return true;
        }
        return false;
    }

    public static boolean isBones(int item) {
        for (int id : BONE_ITEMS) {
            if (item == id)
                return true;
        }
        return false;
    }


    public static boolean isSuccess(int skill, int levelRequired) {
        double successChance = Math.ceil(((double) skill * 50.0D - (double) levelRequired * 15.0D) / (double) levelRequired / 3.0D * 4.0D);
        int roll = Utility.random(99);
        return successChance >= roll;
    }

    public static boolean isSuccess(Player p, int skillId, int levelRequired, boolean usingDragonHarpoon) {
        double level = p.skills.getMaxLevel(skillId);
        double successChance = Math.ceil((((level * 50.0D) - ((double) levelRequired * 15.0D)) / (double) levelRequired / 3.0D) * 4.0D);
        int roll = usingDragonHarpoon ? Utility.random(79) : Utility.random(99);
        return successChance >= roll;
    }

    public static boolean isSuccess(Player p, int skill, int levelRequired, int toolLevelRequired) {
        double level = (p.skills.getMaxLevel(skill) + toolLevelRequired) / 2.0D;
        double successChance = Math.ceil((((level * 50.0D) - ((double) levelRequired * 15.0D)) / (double) levelRequired / 3.0D) * 4.0D);
        int roll = Utility.random(99);
        return successChance >= roll;
    }

    public static final int[][] IMPLINGS = {
            /**
             * Baby imps
            */
            {1635, 2612, 4318},
            {1635, 2602, 4314},
            {1635, 2610, 4338},
            {1635, 2582, 4344},
            {1635, 2578, 4344},
            {1635, 2568, 4311},
            {1635, 2583, 4295},
            {1635, 2582, 4330},
            {1635, 2600, 4303},
            {1635, 2611, 4301},
            {1635, 2618, 4329},

            /**
             * Young imps
            */
            {1636, 2591, 4332},
            {1636, 2600, 4338},
            {1636, 2595, 4345},
            {1636, 2610, 4327},
            {1636, 2617, 4314},
            {1636, 2619, 4294},
            {1636, 2599, 4294},
            {1636, 2575, 4303},
            {1636, 2570, 4299},

            /**
             * Gourment imps
            */
            {1637, 2573, 4339},
            {1637, 2567, 4328},
            {1637, 2593, 4297},
            {1637, 2618, 4305},
            {1637, 2605, 4316},
            {1637, 2596, 4333},

            /**
             * Earth imps
            */
            {1638, 2592, 4338},
            {1638, 2611, 4345},
            {1638, 2617, 4339},
            {1638, 2614, 4301},
            {1638, 2606, 4295},
            {1638, 2581, 4299},

            /**
             * Essence imps
            */
            {1639, 2602, 4328},
            {1639, 2608, 4333},
            {1639, 2609, 4296},
            {1639, 2581, 4304},
            {1639, 2570, 4318},

            /**
             * Eclectic imps
            */
            {1640, 2611, 4310},
            {1640, 2617, 4319},
            {1640, 2600, 4347},
            {1640, 2570, 4326},
            {1640, 2579, 4310},

            /**
             * Spirit imps
            */

            /**
             * Nature imps
            */
            {1641, 2581, 4310},
            {1641, 2581, 4310},
            {1641, 2603, 4333},
            {1641, 2576, 4335},
            {1641, 2588, 4345},

            /**
             * Magpie imps
            */
            {1642, 2612, 4324},
            {1642, 2602, 4323},
            {1642, 2587, 4348},
            {1642, 2564, 4320},
            {1642, 2566, 4295},

            /**
             * Ninja imps
            */
            {1643, 2570, 4347},
            {1643, 2572, 4327},
            {1643, 2578, 4318},
            {1643, 2610, 4312},
            {1643, 2594, 4341},

            /**
             * Dragon imps
            */
            {1654, 2613, 4341},
            {1654, 2585, 4337},
            {1654, 2576, 4319},
            {1654, 2576, 4294},
            {1654, 2592, 4305},
    };
}
