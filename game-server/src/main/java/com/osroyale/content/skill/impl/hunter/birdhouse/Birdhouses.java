package com.osroyale.content.skill.impl.hunter.birdhouse;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.persist.PlayerSerializer;
import com.osroyale.game.world.entity.skill.SkillData;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.ground.GroundItem;
import com.osroyale.game.world.object.CustomGameObject;
import com.osroyale.game.world.object.ObjectDirection;
import com.osroyale.game.world.object.ObjectType;
import com.osroyale.net.packet.out.SendAddObject;
import com.osroyale.net.packet.out.SendRemoveInterface;
import com.osroyale.util.Utility;

import java.util.ArrayList;
import java.util.Random;

public class Birdhouses {
    static long SECOND = 1000,
            MINUTE = SECOND * 60,
            HOUR = MINUTE * 60,
            DAY = HOUR * 24;

    public static String getTimeLeft(PlayerBirdHouseData playerBirdHouseData) {
        long left = playerBirdHouseData.birdhouseTimer - System.currentTimeMillis();
        long minutes = left / MINUTE;
        return minutes < 0 ? "rougly 1 minute." : minutes + " minute(s).";
    }

    public static void receiveLoot(Player player, PlayerBirdHouseData birdHouseData) {
        player.send(new SendRemoveInterface());

        if(player.inventory.getFreeSlots() < 2) {
            player.message("You must have two spaces in your inventory to dismantle the birdhouse.");
            return;
        }

        ArrayList<Item> receivedItems = new ArrayList<>();
        player.animate(827);

        player.inventory.addOrDrop(new Item(8792));
        receivedItems.add(new Item(8792));
        for(int index = 0; index < 10; index++) {
            GroundItem.create(player, new Item(9978));
            receivedItems.add(new Item(9978));
        }
        int[] featerAmount = { 30, 40, 50, 60 };
        int featherAmount = featerAmount[Utility.random(featerAmount.length - 1)];

        player.inventory.addOrDrop(new Item(314, featherAmount));
        receivedItems.add(new Item(314, featherAmount));

        int nestsReceived = 0;

        player.inventory.addOrDrop(new Item(5073));
        receivedItems.add(new Item(5073));

        if(wasSuccesful(player, SkillData.HUNTER.ordinal(), 0, 200)) {
            player.inventory.addOrDrop(new Item(5073));
            receivedItems.add(new Item(5073));
            nestsReceived++;
        }

        for(int index = 0; index < 10; index++) {
            int hunterLevel = player.skills.getLevel(SkillData.HUNTER.ordinal());

            if(hunterLevel < 50)
                hunterLevel = 50;

            int nestRate = birdHouseData.birdhouseData.succesRates * (hunterLevel - 1) / 98;
            int randomRoll = Utility.random(1000);

            if(randomRoll < nestRate) {
                int nestChance = 100;
                if(player.equipment.contains(10134))
                    nestChance = 95;
                int nestRoll = Utility.random(nestChance);

                if(nestRoll < 1) {
                    player.inventory.addOrDrop(new Item(5072));
                    receivedItems.add(new Item(5072));
                    nestsReceived++;
                }
                else if(nestRoll < 2) {
                    player.inventory.addOrDrop(new Item(5070));
                    receivedItems.add(new Item(5070));
                    nestsReceived++;
                } else if(nestRoll < 3) {
                    player.inventory.addOrDrop(new Item(5071));
                    receivedItems.add(new Item(5071));
                    nestsReceived++;
                } else if(nestRoll < 35) {
                    player.inventory.addOrDrop(new Item(5074));
                    receivedItems.add(new Item(5074));
                    nestsReceived++;
                } else {
                    player.inventory.addOrDrop(new Item(5075));
                    receivedItems.add(new Item(5075));
                    nestsReceived++;
                }
            }
        }

        player.inventory.refresh();

        player.message("You dismantle and discard the trap, retrieving "+nestsReceived+" nest"+(nestsReceived > 1 ? "s" : "")+", 10 dead birds, "+featherAmount+" feathers and "+birdHouseData.birdhouseData.hunterData[1]+" Hunter XP.");
        player.skills.addExperience(SkillData.HUNTER.ordinal(), birdHouseData.birdhouseData.hunterData[1]);

        player.send(new SendAddObject(new CustomGameObject(birdHouseData.oldObjectId, birdHouseData.birdhousePosition, ObjectDirection.valueOf(birdHouseData.rotation).get(), ObjectType.valueOf(birdHouseData.type).get())));
        player.birdHouseData.remove(birdHouseData);
        PlayerSerializer.save(player);
    }

    private static boolean wasSuccesful(Player player, int skill, int low, int high) {
        int level = player.skills.getLevel(skill);

        int odds = 1 + (low * (99 - level) / 98) + (high * (level - 1) / 98);
        double percent = ((double)odds / 256D) * 100D;
        int random = new Random().nextInt(256);

        return odds >= random;
    }

}