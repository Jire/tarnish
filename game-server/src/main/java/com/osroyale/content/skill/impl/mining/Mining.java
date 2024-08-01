package com.osroyale.content.skill.impl.mining;

import com.osroyale.content.event.impl.ObjectInteractionEvent;
import com.osroyale.content.skill.SkillRepository;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.object.GameObject;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.Utility;
import com.osroyale.util.chance.Chance;
import com.osroyale.util.chance.WeightedChance;

import java.util.Arrays;

/**
 * Handles the mining skill.
 *
 * @author Daniel
 */
public final class Mining extends Skill {
    static final Chance<Item> GEM_ITEMS = new Chance<>(Arrays.asList(
            new WeightedChance<>(6, new Item(1623, 1)), // SAPPHIRE
            new WeightedChance<>(5, new Item(1621, 1)), // EMERALD
            new WeightedChance<>(4, new Item(1619, 1)), // RUBY
            new WeightedChance<>(3, new Item(1617, 1)), // DIAMOND
            new WeightedChance<>(1, new Item(1631, 1)) // DRAGON_STONE
    ));

    public static double getBonus(Player player) {
        double bonus = 0;
        if(player.equipment.getId(0) == 12013 || player.equipment.getId(0) == 25549)
            bonus += 0.4;
        if(player.equipment.getId(4) == 12014 || player.equipment.getId(0) == 25551)
            bonus += 0.8;
        if(player.equipment.getId(7) == 12015 || player.equipment.getId(0) == 25553)
            bonus += 0.6;
        if(player.equipment.getId(10) == 12016 || player.equipment.getId(0) == 25555)
            bonus += 0.2;

        var wornPieces = 0;
        int[] pieces = {12013, 12014, 12015, 12016, 25549, 25551, 25553, 25555};
        for (int piece : pieces) {
            if (player.equipment.contains(piece)) {
                wornPieces++;
            }
        }

        if (wornPieces > 3) {
            bonus = 2.5;
        }

        return bonus;
    }


    /** Constructs a new {@link Mining} skill. */
    public Mining(int level, double experience) {
        super(Skill.MINING, level, experience);
    }

    @Override
    protected boolean clickObject(Player player, ObjectInteractionEvent event) {
        GameObject object = event.getObject();
        OreData ore = OreData.forId(object.getDefinition().getId());

        if (ore == null) {
            return false;
        }

        if (!object.active()) {
            return false;
        }

        switch (event.getType()) {
            case FIRST_CLICK_OBJECT:
                attempt(player, object, ore);

                break;
            case SECOND_CLICK_OBJECT:
                player.send(new SendMessage("You examine the rock for ores..."));
                World.schedule(2, () -> player.send(new SendMessage("This rock contains " + Utility.formatEnum(ore.name()) + ".")));
                break;
        }
        return true;
    }

    private void attempt(Player player, GameObject object, OreData ore) {
        PickaxeData pickaxe = PickaxeData.getBestPickaxe(player).orElse(null);

        if (pickaxe == null) {
            player.message("You don't have a pickaxe.");
            return;
        }

        if (!player.skills.get(Skill.MINING).reqLevel(pickaxe.level)) {
            player.message("You need a level of " + pickaxe.level + " to use this pickaxe!");
            return;
        }

        if (!player.skills.get(Skill.MINING).reqLevel(ore.level)) {
            player.message("You need a mining level of " + ore.level + " to mine this ore!");
            return;
        }


        start(player, object, ore, pickaxe);
    }

    private void start(Player player, GameObject object, OreData ore, PickaxeData pickaxe) {
        if (!object.getGenericAttributes().has("ores")) {
            object.getGenericAttributes().set("ores", ore.oreCount);
        }
        player.walkTo(object.getPosition(), () -> {
            player.face(object.getPosition());
        });
        World.schedule(1, () -> {
            player.animate(pickaxe.animation);
            player.action.execute(new MiningAction(player, object, ore, pickaxe));
        });
        player.message(true, "You swing your pick at the rock...");
    }

    public static boolean success(Player player, OreData ore, PickaxeData pickaxe) {
        var playerMiningLevel = player.skills.getMaxLevel(Skill.MINING);
        //Mining boost for celestial rings
        if (player.equipment.containsAny(25539, 25541, 25543, 25545)) {
            playerMiningLevel += 4;
        }
        double level = (playerMiningLevel + pickaxe.level) / 2.0D;
        double successChance = Math.ceil((((level * 50.0D) - ((double) ore.level * 15.0D)) / (double) ore.level / 3.0D) * 4.0D);
        int roll = Utility.random(99);
        return successChance >= roll;
    }

    public static boolean success(Player player, int level, PickaxeData pickaxe) {
        return SkillRepository.isSuccess(player, Skill.MINING, level, pickaxe.level);
    }
}
