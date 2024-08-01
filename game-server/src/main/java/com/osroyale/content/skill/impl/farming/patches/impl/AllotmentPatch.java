package com.osroyale.content.skill.impl.farming.patches.impl;

import com.osroyale.content.skill.impl.farming.FarmingConstants;
import com.osroyale.content.skill.impl.farming.patches.WaterablePatch;
import com.osroyale.content.skill.impl.farming.plants.Allotment;
import com.osroyale.content.skill.impl.farming.plants.Plant;
import com.osroyale.content.skill.impl.farming.zones.FarmingZone;
import com.osroyale.game.world.Interactable;
import com.osroyale.game.world.entity.mob.player.Player;

/**
 * An implementation of an allotment patch.
 *
 * @author Michael | Chex
 */
public class AllotmentPatch extends WaterablePatch {

    /** The minimum amount of produce to harvest, */
    private static final int START_HARVEST_AMOUNT = 3;

    /** The maximum amount of produce to harvest, */
    private static final int END_HARVEST_AMOUNT = 56;

    /** The flower patch that can protect this patch from disease. */
    private final FlowerPatch flower;

    /**
     * Constructs a new {@link AllotmentPatch}.
     *
     * @param player     the player that owns this patch
     * @param zone       the zone this patch belongs to
     * @param flower     the protective flower patch
     * @param boundaries the boundaries of this patch
     */
    public AllotmentPatch(Player player, FarmingZone zone, FlowerPatch flower, Interactable[] boundaries) {
        super(player, zone, boundaries);
        this.flower = flower;
    }

    /**
     * Gets an {@link Allotment} type for a seed item id.
     *
     * @param seedId the seed item id
     * @return the allotment for the seed
     */
    public Plant plantForSeed(int seedId) {
        return Allotment.forId(seedId);
    }

  /*  @Override
    public boolean rollDisease() {
        if (flower == null || !flower.isGrowing()) {
            return super.rollDisease();
        }
        return !flower.protectsDisease(plant.getFlowerProtect()) && super.rollDisease();
    }
*/
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
        return FarmingConstants.PICKING_VEGETABLE_ANIM;
    }

}
