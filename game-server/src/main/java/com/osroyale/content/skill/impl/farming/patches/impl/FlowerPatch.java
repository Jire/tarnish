package com.osroyale.content.skill.impl.farming.patches.impl;

import com.osroyale.content.skill.impl.farming.FarmingConstants;
import com.osroyale.content.skill.impl.farming.patches.WaterablePatch;
import com.osroyale.content.skill.impl.farming.plants.Flower;
import com.osroyale.content.skill.impl.farming.plants.Plant;
import com.osroyale.content.skill.impl.farming.zones.FarmingZone;
import com.osroyale.game.world.Interactable;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;

/**
 * A {@link WaterablePatch} that can grow flowers and scarecrows for battling
 * disease on an {@link AllotmentPatch}.
 *
 * @author Michael | Chex
 */
public class FlowerPatch extends WaterablePatch {

    /** The scarecrow item id. */
    public static final int SCARECROW = 6059;

    /**
     * Constructs a new {@link FlowerPatch} object.
     *
     * @param player     the player that owns this patch
     * @param zone       the zone this patch belongs to
     * @param boundaries the boundaries of this patch
     */
    public FlowerPatch(Player player, FarmingZone zone, Interactable[] boundaries) {
        super(player, zone, boundaries);
    }

    /**
     * Plants a scarecrow in this patch if the requirements are met.
     *
     * @param item  the presumed scarecrow item id
     * @param index the index of the item
     * @return {@code true} if the scarecrow was successfully planted
     */
    private boolean plantScareCrow(int item, int index) {
        if (player.locking.locked()) {
            return false;
        }

        if (plant == null || item != SCARECROW) {
            return false;
        }

        if (!isEmpty()) {
            player.message("You need to clear the patch before planting a scarecrow.");
            return false;
        }

        player.animate(832);
        player.inventory.remove(new Item(SCARECROW), index, true);
        player.locking.lock(2 * 3 / 5);

        World.schedule(2, () -> {
            player.message("You put a scarecrow on the flower patch, and some weeds start to grow around it.");
            plant = plantForSeed(0x24);
            timer.reset();
            zone.sendPatchConfigs(player);
        });
        return true;
    }

    /**
     * Checks if this flower patch protects an allotment patch from disease.
     *
     * @param protect the allotment patch's protect flower id
     * @return {@code true} if this flower will protect the allotment from
     * disease
     */
    public boolean protectsDisease(int protect) {
        if (!isDead() && fullyGrown && plant.getSeedId() == protect) {
            setDead();
            return true;
        }
        return protect == SCARECROW && plant.getSeedId() >= 0x21 && plant.getSeedId() <= 0x24;
    }

    @Override
    public Plant plantForSeed(int seedId) {
        return Flower.forId(seedId);
    }

    @Override
    public boolean itemOnObject(Item item, int index) {
        return plantScareCrow(item.getId(), index) || super.itemOnObject(item, index);
    }

    @Override
    protected int getMinAmount() {
        return 1;
    }

    @Override
    protected int getMaxAmount() {
        return 1;
    }

    @Override
    protected int animation() {
        return FarmingConstants.PICKING_VEGETABLE_ANIM;
    }

}
