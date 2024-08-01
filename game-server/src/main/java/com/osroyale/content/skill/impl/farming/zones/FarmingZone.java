package com.osroyale.content.skill.impl.farming.zones;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.osroyale.content.skill.impl.farming.patches.FarmingPatch;
import com.osroyale.game.world.Interactable;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.object.GameObject;
import com.osroyale.game.world.position.Position;
import com.osroyale.game.world.region.Region;
import com.osroyale.net.packet.out.SendConfig;
import com.osroyale.util.Utility;

/**
 * A zone that manages farming patches.
 *
 * @author Michael | Chex
 */
public class FarmingZone {

    /** The config id of all the patches. */
    private static final int PATCH_CONFIG = 529;

    /** The boundary of this zone. */
    private final Interactable boundary;

    /** The patches in this zone. */
    private final FarmingPatch[] patches;

    /**
     * Constructs a new farming zone.
     *
     * @param size the amount of patches in this zone
     */
    FarmingZone(Interactable boundary, int size) {
        this.boundary = boundary;
        patches = new FarmingPatch[size];
    }

    /**
     * Sets a farming patch in this zone.
     *
     * @param index the index of the farming patch
     * @param patch the patch to set
     */
    void setPatch(int index, FarmingPatch patch) {
        patches[index] = patch;
    }

    /**
     * Handles clicking a farming patch.
     *
     * @param object the object that was clicked
     * @param opcode the click opcode (starting at 1)
     * @return {@code true} if the object was a patch
     */
    public boolean clickObject(GameObject object, int opcode) {
        boolean success = false;
        for (FarmingPatch patch : patches) {
            if (patch.within(object.getPosition())) {
                success |= patch.clickObject(opcode);
            }
        }
        return success;
    }

    /**
     * Handles using an item on a farming patch.
     *
     * @param object the object an item was used on
     * @param item   the item used on the object
     * @param slot   the inventory index of the item
     * @return {@code true} if the item used on the patch was a farming event
     */
    public boolean itemOnObject(GameObject object, Item item, int slot) {
        boolean success = false;
        for (FarmingPatch patch : patches) {
            if (patch.within(object.getPosition())) {
                success |= patch.itemOnObject(item, slot);
            }
        }
        return success;
    }

    /**
     * Sends the configs of this zone to the player's client.
     *
     * @param player the player to send the config to
     */
    public final void sendPatchConfigs(Player player) {
        if (!isViewable(player.getPosition())) {
            return;
        }

        int config = 0;
        for (int index = 0; index < patches.length; index++) {
            config |= patches[index].getConfig() << (index << 3);
        }

        player.send(new SendConfig(PATCH_CONFIG, config));
    }

    /** Ticks the farming patches in this zone. */
    public final void tick() {
        for (FarmingPatch patch : patches) {
            patch.tick();
        }
    }

    /**
     * Checks if a position can see this farming zone.
     *
     * @param position the position to check
     * @return {@code true} if the zone is viewable from the position
     */
    public boolean isViewable(Position position) {
        return Utility.inside(boundary, position) || Utility.withinDistance(boundary, position, Region.SIZE);
    }

    public JsonArray toJson() {
        JsonArray array = new JsonArray();
        for (FarmingPatch patch : patches) {
            array.add(patch.toJson());
        }
        return array;
    }

    public void fromJson(JsonArray array) {
        int index = 0;
        for (JsonElement element : array) {
            patches[index++].fromJson((JsonObject) element);
        }
    }

}
