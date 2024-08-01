package com.osroyale.content.shootingstar;

import com.osroyale.Config;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.object.CustomGameObject;
import com.osroyale.util.RandomUtils;

public class ShootingStarData {

    /**
     * See if the dust needs to be removed double
     */
    private boolean decreaseDouble = false;

    /**
     * The current location of the star
     */
    public ShootingStarLocations starLocation;

    /**
     * The current level of the star
     */
    public int starLevel;

    /**
     * How much dust there currently is in the star
     */
    public int availableDust;

    /**
     * The max dust for this star level
     */
    public int maxDust;

    /**
     * All the possible object ids for the different star levels
     */
    public int[] starIds = { 41229, 41228, 41227, 41226, 41225, 41224, 41223, 41021,  };

    /**
     * The amount of star dust the player receives for the different star levels
     */
    public int[] possibleDust = { 1200, 700, 430, 250, 175, 80, 40, 40, 15 };

    /**
     * Possible xp drops for the differernt star levels
     */
    public int[] xpDrops = { 12, 22, 26, 31, 48, 74, 123, 162, 244 };

    /**
     * The game object linked to the star
     */
    public CustomGameObject starObject;

    /**
     * The chance of doubling stardust depending on the star level
     */
    public int[] doubleChance = { 2, 6, 12, 20, 30, 42, 56, 72, 90 };

    public ShootingStarData() {
        starLevel = getLevel();
        maxDust = possibleDust[starLevel];
        availableDust = maxDust;
        starLocation = ShootingStarLocations.values()[RandomUtils.inclusive(0, ShootingStarLocations.values().length - 1)];

        System.out.println("starLocation.starPosition " + starLocation.starPosition);
        starObject = new CustomGameObject(getObjectId(), starLocation.starPosition);
        starObject.register();
    }

    /**
     * Handles decreasing the dust for a star
     */
    public void decreaseDust(Player player) {
        player.skills.addExperience(Skill.MINING, getXPDrop() * Config.MINING_MODIFICATION);
        int amount = 1;

        //Handles the double stardust chance
        int roll = RandomUtils.inclusive(0, 100);
        if(roll <= doubleChance[starLevel]) amount *= 2;

        player.inventory.add(new Item(25527, amount));
        availableDust -= decreaseDouble ? amount : amount / 2;

        if(availableDust <= 0) {
            decreaseLevel();
            for(Player p : World.getPlayers()) {
                if(p == null || !(p.action.getCurrentAction() instanceof ShootingStarAction)) continue;

                System.out.println("Reset the mining action for all the players with a shooting star action...");
                p.action.cancel();
                p.resetFace();
                p.skills.get(Skill.MINING).setDoingSkill(false);
            }
        }
    }

    /**
     * Handles the decreasing of the star level
     */
    public void decreaseLevel() {
        starLevel--;
        if(starLevel < 0) {
            destruct();
            return;
        }

        maxDust = possibleDust[starLevel];
        availableDust = maxDust;
        starObject.transform(getObjectId());
    }

    /**
     * Handles removing the star from the game
     */
    public void destruct() {
        starObject.unregister();
        starObject = null;
    }

    /**
     * Gets the random star level
     * @return
     */
    public int getLevel() {
        int[] chances = { 0, 16, 18, 20, 17, 12, 9, 5 }; //, 3

        for (int index = chances.length - 1; index > 0; index--) {
            int roll = RandomUtils.inclusive(0, 100);
            if (roll < chances[index])
                return index;
        }
        return 2;
    }

    /**
     * The current object id
     * @return
     */
    public int getObjectId() {
        return starIds[starLevel];
    }

    /**
     * The required mining level for the star level
     * @return
     */
    public int getMiningLevel() {
        return (starLevel + 1) * 10;
    }

    /**
     * Percentage till the next star level
     * @return
     */
    public int getPercentage() {
        return (maxDust - availableDust) * 100 / maxDust;
    }

    /**
     * The xp the player gets for the current star
     * @return
     */
    public int getXPDrop() {
        return xpDrops[starLevel];
    }

    /**
     * The current location of the star
     * @return
     */
    public String getLocationName() {
        return starLocation.location[0];
    }

    /**
     * The current hint of the star location
     * @return
     */
    public String getHint() {
        return starLocation.location[1];
    }
}