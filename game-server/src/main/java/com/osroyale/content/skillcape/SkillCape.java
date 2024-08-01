package com.osroyale.content.skillcape;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;

public enum SkillCape {
    ATTACK(9747, "Free access to Cyclops area found upstairs within the Warriors' Guild."),
    DEFENCE(9753, "Act as a permanent Ring of Life."),
    RANGED(9756, "Act as an Ava's accumulator."),
    PRAYER(9759, "Provides double experience when training Prayer."),
    MAGIC(9762, "Can cast spellbook swap 5 times per day."),
    HITPOINTS(9768, "2x HP restore rate."),
    AGILITY(9771, "Increased run energy restore rate."),
    HERBLORE(9774, "Provides a chance to save ingredients."),
    THIEVING(9777, "Better chance of succeeding when stealing from stalls."),
    CRAFTING(9780, "Provides a chance to save materials."),
    FLETCHING(9783, "Provides a chance to save materials."),
    SLAYER(9786, "Can cancel up to 2 tasks per day."),
    MINING(9792, "Provides a chance to receive double materials."),
    SMITHING(9795, "Increases speed at which you smelt bars/forge items."),
    COOKING(9801, "Impossible to burn any food."),
    FIREMAKING(9804, "Provides a chance to save a log."),
    WOODCUTTING(9807, "Provides a chance to receive double materials."),
    FARMING(9810, "5% increased yield from herb patches.");

    private final int itemId;
    private final String description;

    SkillCape(int itemId, String description) {
        this.itemId = itemId;
        this.description = description;
    }

    public static boolean isEquipped(Player player, SkillCape cape) {
        final Item currentCape = player.equipment.getCape();
        if (currentCape == null) {
            return false;
        }

        // Max cape gives all perks.
        if (currentCape.getId() == 13280) {
            return true;
        }

        return currentCape.getId() == cape.getItemId() || currentCape.getId() == cape.getItemId() + 1;
    }

    private int getItemId() {
        return itemId;
    }

    public String getDescription() {
        return description;
    }
}
