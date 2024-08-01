package com.osroyale.content.skill.impl.farming.patches;

import com.google.gson.JsonObject;
import com.osroyale.content.skill.impl.farming.FarmingConstants;
import com.osroyale.content.skill.impl.farming.zones.FarmingZone;
import com.osroyale.game.world.Interactable;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;

public abstract class DiseasablePatch extends HarvestablePatch {
    protected boolean watched;

    protected DiseasablePatch(Player player, FarmingZone zone, Interactable[] boundaries) {
        super(player, zone, boundaries);
    }

    @Override
    protected void onGrowth() {
        if (isDead()) {
            return;
        }

      /*  if (isDiseased()) {
            setDead();
        } else if (!fullyGrown) {
            handleDisease();
        }*/
    }

    protected void resetPatch() {
        super.resetPatch();
        watched = false;
    }

    public boolean clickObject(int opcode) {
        if (opcode == 1 && isDiseased()) {
            curePlant(-1);
            return true;
        }
        return super.clickObject(opcode);
    }

    public boolean itemOnObject(Item item, int index) {
        if (item.matchesId(FarmingConstants.PLANT_CURE)) {
            curePlant(index);
            return true;
        }
        return super.itemOnObject(item, index);
    }

   /* public boolean rollDisease() {
        return growth > 0 && !watched && !isWatered() && RandomUtils.success(compost.getProtection());
    }*/

    /*private void handleDisease() {
        if (rollDisease()) {
            setDiseased();
            player.message("One of your crops is diseased!");
        }
    }*/

    private void curePlant(int index) {
        if (player.locking.locked()) {
            return;
        }

        if (plant == null) {
            return;
        }

        if (!isDiseased()) {
            player.message("This plant doesn't need to be cured.");
            return;
        }

        if (!player.inventory.contains(FarmingConstants.PLANT_CURE)) {
            player.message("You need plant cure to cure this plant.");
            return;
        }


        if (index != -1) {
            player.inventory.replace(FarmingConstants.PLANT_CURE, 229, index, true);
        } else {
            player.inventory.replace(FarmingConstants.PLANT_CURE, 229, true);
        }

        player.locking.lock(7);
        player.animate(FarmingConstants.CURING_ANIM);
        setNormal();

        World.schedule(7, () -> {
            player.resetAnimation();
            player.message("You cure the plant with a plant cure.");
            zone.sendPatchConfigs(player);
        });
    }

    @Override
    public JsonObject toJson() {
        JsonObject object = super.toJson();
        object.addProperty("watched", watched);
        return object;
    }

    @Override
    public void fromJson(JsonObject object) {
        watched = object.get("watched").getAsBoolean();
        super.fromJson(object);
    }

}
