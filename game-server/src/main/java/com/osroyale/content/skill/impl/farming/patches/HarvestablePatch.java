package com.osroyale.content.skill.impl.farming.patches;

import com.google.gson.JsonObject;
import com.osroyale.Config;
import com.osroyale.content.skill.impl.farming.FarmingConstants;
import com.osroyale.content.skill.impl.farming.zones.FarmingZone;
import com.osroyale.content.skillcape.SkillCape;
import com.osroyale.game.action.Action;
import com.osroyale.game.action.policy.WalkablePolicy;
import com.osroyale.game.world.Interactable;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.util.RandomUtils;

/**
 * A {@link WaterablePatch} that can harvest produce.
 *
 * @author Michael | Chex
 */
public abstract class HarvestablePatch extends FarmingPatch {

    /** The harvest amount. */
    private int harvest;

    /**
     * Constructs a new {@link HarvestablePatch} object.
     *
     * @param player     the player that owns this patch
     * @param zone       the zone this patch belongs in
     * @param boundaries the boundaries of this patch
     */
    HarvestablePatch(Player player, FarmingZone zone, Interactable[] boundaries) {
        super(player, zone, boundaries);
    }

    /**
     * Harvests produce in this patch if the requirements are met.
     *
     * @param min       the minimum amount of produce to harvest
     * @param max       the maximum amount of produce to harvest
     * @return {@code true} if this patch has successfully begun to harvest
     * produce
     */
    private boolean harvest(int min, int max) {
        if (plant == null) {
            return false;
        }

        if (!player.inventory.contains(FarmingConstants.SECATEURS) && !player.inventory.contains(FarmingConstants.MAGIC_SECATEURS) &&
        !player.equipment.contains(FarmingConstants.MAGIC_SECATEURS)) {
            player.dialogueFactory.sendStatement("You need secateurs to harvest here.").execute();
            return true;
        }

        min = min * compost.produceIncrease() / 100;
        player.action.execute(createHarvestAction(player, min, max));
        return true;
    }

    /**
     * Creates the harvesting action.
     *
     * @param player the player harvesting produce
     * @param min    the minimum amount of produce to harvest
     * @param max    the maximum amount of produce to harvest
     * @return a new {@link Action} specifically designed to harvest produce
     */
    private Action<Player> createHarvestAction(Player player, int min, int max) {
        return new Action<Player>(player, 2) {

            @Override
            public boolean canSchedule() {
                return !player.locking.locked() && player.inventory.getFreeSlots() > 0;
            }

            @Override
            public void onSchedule() {
                player.locking.lock(2);
                player.animate(animation());
                player.message("You begin to harvest the crop for " + plant.getProductType() + ".");
            }

            @Override
            public void execute() {
                if (player.inventory.getFreeSlots() <= 0) {
                    cancel();
                    return;
                }

                if (harvest == 0) {
                    harvest = RandomUtils.inclusive(min, max);

                    if (player.equipment.contains(FarmingConstants.MAGIC_SECATEURS)) {
                        harvest = harvest * 13 / 12;
                    }

                    if (SkillCape.isEquipped(player, SkillCape.FARMING)) {
                        harvest += harvest * 13 / 12;
                    }

                    harvest++;
                }

                if (harvest == 1) {
                    resetPatch();
                    cancel();
                    return;
                }

                harvest--;
                if (harvest % 2 == 0)
                    player.animate(animation());
                player.inventory.add(plant.getHarvestId(), 1);
                player.skills.addExperience(Skill.FARMING, plant.getHarvestXp() * Config.FARMING_MODIFICATION);
            }

            @Override
            public void onCancel(boolean logout) {
                player.resetAnimation();
                zone.sendPatchConfigs(player);
            }

            @Override
            public WalkablePolicy getWalkablePolicy() {
                return WalkablePolicy.NON_WALKABLE;
            }

            @Override
            public String getName() {
                return "Farming Harvest";
            }
        };
    }

    /** @return the minimum amount of produce to harvest */
    protected abstract int getMinAmount();

    /** @return the maximum amount of produce to harvest */
    protected abstract int getMaxAmount();

    /** @return the harvesting animation */
    protected abstract int animation();

    @Override
    public boolean clickObject(int opcode) {
        if (opcode == 1 && !isDead() && fullyGrown) {
            return harvest(getMinAmount(), getMaxAmount());
        }
        return super.clickObject(opcode);
    }

    @Override
    protected void resetPatch() {
        super.resetPatch();
        harvest = 0;
    }

    @Override
    public JsonObject toJson() {
        JsonObject object = super.toJson();
        object.addProperty("harvest", harvest);
        return object;
    }

    @Override
    public void fromJson(JsonObject object) {
        harvest = object.get("harvest").getAsInt();
        super.fromJson(object);
    }

}
