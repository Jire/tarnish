package com.osroyale.content.skill.impl.farming.patches;

import com.osroyale.content.skill.impl.farming.FarmingConstants;
import com.osroyale.content.skill.impl.farming.zones.FarmingZone;
import com.osroyale.game.world.Interactable;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;

/**
 * A {@link DiseasablePatch} that can prevent disease with water.
 *
 * @author Michael | Chex
 */
public abstract class WaterablePatch extends DiseasablePatch {

    /**
     * Constructs a new {@link WaterablePatch} object.
     *
     * @param player     the player that owns this patch
     * @param zone       the zone this patch belongs in
     * @param boundaries the boundaries of this patch
     */
    public WaterablePatch(Player player, FarmingZone zone, Interactable[] boundaries) {
        super(player, zone, boundaries);
    }

    /**
     * Waters this patch to prevent disease if requirements are met.
     *
     * @param itemId the presumed watering can item id
     * @param index  the inventory index of the item
     * @return {@code true} if this patch was successfully watered
     */
    private boolean waterPatch(int itemId, int index) {
        if (player.locking.locked()) {
            return false;
        }

        if (itemId != 5331 && (itemId < 5333 || itemId > 5340)) {
            return false;
        }

        if (!isGrowing()) {
            player.message("This patch needs to be growing something first.");
            return true;
        }

        if (isWatered() || fullyGrown) {
            player.message("This patch doesn't need watering.");
            return true;
        }

        if (itemId == 5331) {
            player.dialogueFactory.sendStatement("This watering can is empty.").execute();
            return true;
        }

        player.message("You water the patch.");
        player.animate(FarmingConstants.WATERING_CAN_ANIM);
        player.inventory.replace(itemId, itemId == 5333 ? itemId - 2 : itemId - 1, index, true);
        player.locking.lock(5 * 3 / 5);
        setWatered();

        World.schedule(5, () -> {
            player.resetAnimation();
            zone.sendPatchConfigs(player);
        });
        return true;
    }

    @Override
    public boolean itemOnObject(Item item, int index) {
        return waterPatch(item.getId(), index) || super.itemOnObject(item, index);
    }

/*
    @Override
    public boolean rollDisease() {
        return !isWatered() && super.rollDisease();
    }
*/

    @Override
    protected void onGrowth() {
        super.onGrowth();
        if (isWatered()) {
            setNormal();
        }
    }

}
