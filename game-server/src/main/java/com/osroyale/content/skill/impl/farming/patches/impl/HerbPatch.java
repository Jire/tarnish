package com.osroyale.content.skill.impl.farming.patches.impl;

import com.osroyale.content.skill.impl.farming.FarmingConstants;
import com.osroyale.content.skill.impl.farming.patches.DiseasablePatch;
import com.osroyale.content.skill.impl.farming.patches.HarvestablePatch;
import com.osroyale.content.skill.impl.farming.plants.Herb;
import com.osroyale.content.skill.impl.farming.plants.Plant;
import com.osroyale.content.skill.impl.farming.zones.FarmingZone;
import com.osroyale.game.world.Interactable;
import com.osroyale.game.world.entity.mob.player.Player;

/**
 * A {@link HarvestablePatch} that can grow herbs.
 *
 * @author Michael | Chex
 */
public class HerbPatch extends DiseasablePatch {

    /** The minimum amount of produce to harvest, */
    private static final int START_HARVEST_AMOUNT = 3;

    /** The maximum amount of produce to harvest, */
    private static final int END_HARVEST_AMOUNT = 18;

    /**
     * Constructs a new {@link HerbPatch} object.
     *
     * @param player     the player that owns this patch
     * @param zone       the zone this patch belongs to
     * @param boundaries the boundaries of this patch
     */
    public HerbPatch(Player player, FarmingZone zone, Interactable[] boundaries) {
        super(player, zone, boundaries);
    }

    @Override
    public Plant plantForSeed(int seedId) {
        return Herb.forId(seedId);
    }

    @Override
    protected int getMinAmount() {
        return START_HARVEST_AMOUNT;
    }

    @Override
    protected int getMaxAmount() {
        return END_HARVEST_AMOUNT;
    }

    @Override
    protected int animation() {
        return FarmingConstants.PICKING_HERB_ANIM;
    }

   /* @Override
    public int getConfig() {
        if (isDead()) {
            return growth + 169;
        }
        return super.getConfig();
    }*/

}
