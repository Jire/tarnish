package com.osroyale.content.skill.impl.farming.patches;

import com.google.gson.JsonObject;
import com.osroyale.Config;
import com.osroyale.content.skill.impl.farming.CompostType;
import com.osroyale.content.skill.impl.farming.FarmingConstants;
import com.osroyale.content.skill.impl.farming.FarmingStage;
import com.osroyale.content.skill.impl.farming.FarmingState;
import com.osroyale.content.skill.impl.farming.plants.Plant;
import com.osroyale.content.skill.impl.farming.zones.FarmingZone;
import com.osroyale.game.action.Action;
import com.osroyale.game.action.policy.WalkablePolicy;
import com.osroyale.game.task.Task;
import com.osroyale.game.world.Interactable;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.position.Position;
import com.osroyale.util.Stopwatch;
import com.osroyale.util.Utility;

import java.util.concurrent.TimeUnit;

public abstract class FarmingPatch {
    private static final double CLEARING_EXPERIENCE = 4;

    protected Plant plant;

    protected FarmingStage stage = FarmingStage.FULL_WEEDS;
    protected FarmingState state = FarmingState.NORMAL;

    protected int growth;

    protected boolean fullyGrown;
    protected CompostType compost;

    protected final Player player;
    protected final FarmingZone zone;
    private final Interactable[] boundaries;
    protected final Stopwatch timer = Stopwatch.start();

    FarmingPatch(Player player, FarmingZone zone, Interactable[] boundaries) {
        this.player = player;
        this.zone = zone;
        this.boundaries = boundaries;
        resetPatch();
    }

    public void tick() {

        /* if the patch is empty, grow back the weeds */
        if (!stage.equals(FarmingStage.FULL_WEEDS) && plant == null && timer.elapsed(5, TimeUnit.MINUTES)) {
            stage = stage.getPreviousStage();
            zone.sendPatchConfigs(player);
            timer.reset();
        }

        /* nothing is growing on this patch */
        if (plant == null) {
            return;
        }

        int elapsed = (int) timer.elapsedTime(TimeUnit.SECONDS) / 12;
        int growthStates = plant.getEndingState() - plant.getStartingState();
        int growthTime = plant.getGrowthTime();

        if (elapsed > growthTime) {
            elapsed = growthTime;
        }

        int completed = growthStates * elapsed / growthTime;

        if (growth != completed) {
            fullyGrown = completed == growthStates;
            onGrowth();
            if (!isDead()) {
                growth = completed;
            }
            zone.sendPatchConfigs(player);
        }
    }

    private void simulate(long elapsed) {

        /* nothing is growing on this patch */
        if (plant == null || isDead()) {
            return;
        }

        int growthStates = plant.getEndingState() - plant.getStartingState();
        int growthTime = plant.getGrowthTime();

        if (elapsed > growthTime) {
            elapsed = growthTime;
        }

        int completed = (int) (growthStates * elapsed / growthTime);

        for (int i = growth; i <= completed; i++) {
            fullyGrown = i == growthStates;
            onGrowth();

            if (isDead()) {
                return;
            }

            growth = i;
        }
    }

    protected void onGrowth() {}

    public boolean clickObject(int opcode) {
        if (opcode == 1 && (needsWeeding() || isDead())) {
            clearPatch();
            return true;
        }
        if (opcode == 2) {
            inspect();
            return true;
        }
        return false;
    }

    public boolean itemOnObject(Item item, int index) {
        if (item.matchesId(FarmingConstants.RAKE) || item.matchesId(FarmingConstants.SPADE)) {
            clearPatch();
            return true;
        }
        return putCompost(item.getId(), index) || plantSeed(item.getId(), index);
    }

    private void clearPatch() {
        int animation, delay;

        if (isEmpty()) {
            player.dialogueFactory.sendStatement("This patch is already empty.").execute();
            return;
        }

        if (isGrowing()) {
            if (!player.inventory.contains(FarmingConstants.SPADE)) {
                player.dialogueFactory.sendStatement("You need a spade to clear this patch.").execute();
                return;
            }

            delay = 3;
            animation = FarmingConstants.SPADE_ANIM;
        } else {
            if (!player.inventory.contains(FarmingConstants.RAKE)) {
                player.dialogueFactory.sendStatement("You need a rake to clear this patch.").execute();
                return;
            }

            delay = 5;
            animation = FarmingConstants.RAKING_ANIM;
        }

        player.action.execute(createClearingAction(player, animation, delay));
    }

    private boolean plantSeed(int seedId, int slot) {
        if (player.locking.locked()) {
            return false;
        }

        Plant toPlant = plantForSeed(seedId);

        if (toPlant == null) {
            return false;
        }

        if (!isEmpty()) {
            player.message("You can't plant a seed here.");
            return false;
        }

        if (toPlant.getLevelRequired() > player.skills.getLevel(Skill.FARMING)) {
            player.dialogueFactory.sendStatement("You need a farming level of " + toPlant.getLevelRequired() + " to plant this seed.").execute();
            return true;
        }

        if (!player.inventory.contains(FarmingConstants.SEED_DIBBER)) {
            player.dialogueFactory.sendStatement("You need a seed dibber to plant seeds here.").execute();
            return true;
        }

        if (!player.inventory.contains(toPlant.getSeedId(), toPlant.getSeedAmount())) {
            player.dialogueFactory.sendStatement("You need at least " + toPlant.getSeedAmount() + " seeds to plant here.").execute();
            return true;
        }

        plant = toPlant;
        setNormal();
        timer.reset();

        player.locking.lock(3 * 3 / 5);
        player.animate(FarmingConstants.SEED_DIBBING);
        player.inventory.remove(new Item(seedId, toPlant.getSeedAmount()), slot, true);

        World.schedule(3, () -> {
            player.resetAnimation();
            player.skills.addExperience(Skill.FARMING, plant.getPlantingXp() * Config.FARMING_MODIFICATION);
            zone.sendPatchConfigs(player);
        });
        return true;
    }

    private boolean putCompost(int itemId, int slot) {
        if (player.locking.locked()) {
            return false;
        }

        CompostType type = CompostType.forItem(itemId);

        if (type == CompostType.NONE) {
            return false;
        }

        if (needsWeeding() || isComposted()) {
            player.message("This patch doesn't need compost.");
            return true;
        }

        player.locking.lock(7 * 3 / 5);
        player.animate(FarmingConstants.PUTTING_COMPOST);
        player.inventory.replace(itemId, FarmingConstants.BUCKET, slot, true);

        player.message("You pour some " + type.name().toLowerCase() + " on the patch.");
        player.skills.addExperience(Skill.FARMING, type.getExp() * Config.FARMING_MODIFICATION);
        compost = type;
        return true;
    }

    private void inspect() {
        if (isDiseased()) {
            player.dialogueFactory.sendStatement("This plant is diseased. Use a plant cure on it to cure it, ", "or clear the patch with a spade.").execute();
            return;
        } else if (isDead()) {
            player.dialogueFactory.sendStatement("This plant is dead. You did not cure it while it was diseased.", "Clear the patch with a spade.").execute();
            return;
        }
        
        String patch = getClass().getSimpleName().replace("Patch", "").toLowerCase();
        patch = Utility.getAOrAn(patch) + " " + patch;

        if (needsWeeding()) {
            player.dialogueFactory.sendStatement("This is " + patch + " patch. The soil has not been treated.", "The patches needs weeding.").execute();
        } else if (isWatered() && isEmpty()) {
            if (compost == CompostType.COMPOST) {
                player.dialogueFactory.sendStatement("This is " + patch + " patch. The soil has been treated with water", "and compost. The patches is empty and weeded.").execute();
            } else if (compost == CompostType.SUPERCOMPOST) {
                player.dialogueFactory.sendStatement("This is " + patch + " patch. The soil has been treated with water", "and supercompost. The patches is empty and weeded.").execute();
            } else if (compost == CompostType.ULTRACOMPOST) {
                player.dialogueFactory.sendStatement("This is " + patch + " patch. The soil has been treated with water", "and ultracompost. The patches is empty and weeded.").execute();
            } else {
                player.dialogueFactory.sendStatement("This is " + patch + " patch. The soil has been treated with water.", "The patches is empty and weeded.").execute();
            }
        } else if (isComposted() && isEmpty()) {
            if (compost == CompostType.COMPOST) {
                player.dialogueFactory.sendStatement("This is " + patch + " patch. The soil has been treated with compost.", "The patches is empty and weeded.").execute();
            } else if (compost == CompostType.SUPERCOMPOST) {
                player.dialogueFactory.sendStatement("This is " + patch + " patch. The soil has been treated with supercompost.", "The patches is empty and weeded.").execute();
            } else if (compost == CompostType.ULTRACOMPOST) {
                player.dialogueFactory.sendStatement("This is " + patch + " patch. The soil has been treated with ultracompost.", "The patches is empty and weeded.").execute();
            }
        } else if (isEmpty()) {
            player.dialogueFactory.sendStatement("This is " + patch + " patch. The soil has not been treated.", "The patches is empty and weeded.").execute();
        } else {
            player.locking.lock(5);
            player.message("You bend down and start to inspect the patch...");
            player.animate(1331);

            World.schedule(new Task(5) {
                @Override
                public void execute() {
                    int index = growth % plant.getInspectMessages().length;
                    if (growth > index) index = growth - index - 1;
                    player.dialogueFactory.sendStatement(plant.getInspectMessages()[index]).execute();
                    cancel();
                }

                @Override
                public void onCancel(boolean logout) {
                    player.animate(1332);
                }
            });
        }
    }

    public int getConfig() {
        if (isEmpty() || needsWeeding()) {
            return stage.ordinal();
        }
        return (state.ordinal() << 6) | (plant.getStartingState() + growth);
    }

    protected void resetPatch() {
        plant = null;
        state = FarmingState.NORMAL;
        stage = FarmingStage.FULL_WEEDS;
        compost = CompostType.NONE;
        growth = 0;
        fullyGrown = false;
        timer.reset();
    }

    public abstract Plant plantForSeed(int seedId);

    public void setNormal() {
        state = FarmingState.NORMAL;
    }

    public void setWatered() {
        state = FarmingState.WATERED;
    }

    public void setDiseased() {
        state = FarmingState.DISEASED;
    }

    public void setDead() {
        state = FarmingState.DEAD;
    }

    public void setEmpty() {
        plant = null;
        stage = FarmingStage.EMPTY;
    }

    public boolean isNormal() {
        return state.equals(FarmingState.NORMAL);
    }

    public boolean isWatered() {
        return state.equals(FarmingState.WATERED);
    }

    public boolean isDiseased() {
        return state.equals(FarmingState.DISEASED);
    }

    public boolean isDead() {
        return state.equals(FarmingState.DEAD);
    }

    public boolean isEmpty() {
        return stage.equals(FarmingStage.EMPTY) && plant == null;
    }

    public boolean isGrowing() {
        return !isEmpty() && !needsWeeding();
    }

    public boolean isComposted() {
        return compost != CompostType.NONE;
    }

    public boolean needsWeeding() {
        return stage.equals(FarmingStage.FULL_WEEDS) || stage.equals(FarmingStage.ONE_THIRD_WEEDS) || stage.equals(FarmingStage.TWO_THIRDS_WEEDS);
    }

    public boolean within(Position position) {
        for (Interactable boundary : boundaries) {
            if (Utility.inside(boundary, position))
                return true;
        }
        return false;
    }

    private Action<Player> createClearingAction(Player player, int animation, int delay) {
        return new Action<Player>(player, delay) {
            @Override
            public void onSchedule() {
                player.animate(animation);
            }

            @Override
            public void execute() {
                if (needsWeeding()) {
                    stage = stage.getNextStage();
                    player.inventory.add(6055, 1);
                } else {
                    setEmpty();
                }

                timer.reset();
                player.animate(animation);
                player.skills.addExperience(Skill.FARMING, CLEARING_EXPERIENCE * Config.FARMING_MODIFICATION);
                zone.sendPatchConfigs(player);

                if (isEmpty()) {
                    player.message("You clear the patch.");
                    resetPatch();
                    setEmpty();
                    cancel();
                }
            }

            @Override
            public void onCancel(boolean logut) {
                player.resetAnimation();
            }

            @Override
            public WalkablePolicy getWalkablePolicy() {
                return WalkablePolicy.NON_WALKABLE;
            }

            @Override
            public String getName() {
                return "Farming Clear";
            }
        };
    }

    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("stage", stage.name());
        object.addProperty("state", state.name());
        object.addProperty("compost", compost.name());
        object.addProperty("timer", timer.getCachedTime());
        object.addProperty("fully-grown", fullyGrown);
        object.addProperty("growth", growth);
        if (plant != null) {
            object.addProperty("plant", plant.getSeedId());
        }
        return object;
    }

    public void fromJson(JsonObject object) {
        stage = FarmingStage.valueOf(object.get("stage").getAsString());
        state = FarmingState.valueOf(object.get("state").getAsString());
        compost = CompostType.valueOf(object.get("compost").getAsString());
        fullyGrown = object.get("fully-grown").getAsBoolean();
        growth = object.get("growth").getAsInt();

        if (object.has("plant")) {
            plant = plantForSeed(object.get("plant").getAsInt());
        }

        long cached = object.get("timer").getAsLong();
        simulate(TimeUnit.SECONDS.convert(System.nanoTime() - cached, TimeUnit.NANOSECONDS) / 12);
        timer.setCachedTime(cached);
    }

}
