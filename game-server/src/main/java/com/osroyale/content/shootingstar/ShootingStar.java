package com.osroyale.content.shootingstar;

import com.osroyale.content.skill.impl.mining.PickaxeData;
import com.osroyale.game.task.Task;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.object.GameObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ShootingStar {

    private static final Logger logger = LogManager.getLogger();

    /**
     * Checks if the star has been found before
     */
    public static boolean starHasBeenFound = false;

    /**
     * The current data of the shooting star
     */
    public static ShootingStarData shootingStarData;

    /**
     * Ticks till the next shooting star will spawn
     */
    public static int starTick = 30;

    /**
     * Handles the mine option of the shooting star
     * @param player
     */
    public static void mine(Player player, GameObject o) {
        if(!starHasBeenFound) {
            starHasBeenFound = true;
            player.dialogueFactory.sendStatement("Congratulations! You were the first person to find this star!").execute();
            player.skills.addExperience(14, 10000);
            return;
        }

        PickaxeData pickaxe = PickaxeData.getBestPickaxe(player).orElse(null);

        if(pickaxe == null) {
            player.message("You don't have a pickaxe.");
            return;
        }

        if (!player.skills.get(Skill.MINING).reqLevel(pickaxe.level)) {
            player.message("You need a level of " + pickaxe.level + " to use this pickaxe!");
            return;
        }

        if(!player.skills.get(Skill.MINING).reqLevel(shootingStarData.getMiningLevel())) {
            player.dialogueFactory.sendStatement("You need a Mining level of " + shootingStarData.getMiningLevel() + " to mine here.").execute();
            return;
        }

        if(!player.inventory.hasCapacityFor(new Item(25527))) {
            player.message("Not enough space in your inventory.");
            return;
        }

        player.message("You swing your pick at the rock.");

        player.animate(pickaxe.animation);

        player.action.execute(new ShootingStarAction(player, pickaxe, o));
    }

    /**
     * Handles prospecting the shooting star
     * @param player
     */
    public static void prospect(Player player) {
        if(!starHasBeenFound) {
            starHasBeenFound = true;
            player.dialogueFactory.sendStatement("Congratulations! You were the first person to find this star!", "You have been granted 10,000 mining xp!").execute();
            player.skills.addExperience(14, 10000);
            return;
        }

        player.dialogueFactory.sendStatement("This is a size-"+(shootingStarData.starLevel + 1)+" star. A Mining level of at least "+shootingStarData.getMiningLevel()+" is required to", "mine this layer. It has been mined "+shootingStarData.getPercentage()+"% of the way to the next layer.").execute();
    }

    /**
     * Handles initializing the shooting stars event
     */
    public static void init() {
        logger.info("Loaded Shooting Stars event.");
        World.schedule(new Task(1) {
            @Override
            public void execute() {
                starTick--;
                if(starTick <= 0) {
                    if(shootingStarData != null) {
                        shootingStarData.destruct();
                        starHasBeenFound = false;
                    }

                    shootingStarData = new ShootingStarData();
                    World.sendMessage("@red@A shooting star has just crashed " + shootingStarData.getHint() +" " + shootingStarData.getLocationName() + "!");
                 //   DiscordPlugin.sendSimpleMessage("A shooting star has just crashed " + shootingStarData.getHint() +" " + shootingStarData.getLocationName() + "!");
                    //4 Hours in ticks
                    starTick = 24_000;
                }
            }

            @Override
            public void onCancel(boolean logout) {
            }
        });
    }
}