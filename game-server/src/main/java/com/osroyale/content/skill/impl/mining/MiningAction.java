package com.osroyale.content.skill.impl.mining;

import com.osroyale.Config;
import com.osroyale.content.achievement.AchievementHandler;
import com.osroyale.content.achievement.AchievementKey;
import com.osroyale.content.activity.randomevent.RandomEventHandler;
import com.osroyale.content.clanchannel.content.ClanTaskKey;
import com.osroyale.content.pet.PetData;
import com.osroyale.content.pet.Pets;
import com.osroyale.content.prestige.PrestigePerk;
import com.osroyale.content.skill.impl.smithing.SmeltingData;
import com.osroyale.content.skillcape.SkillCape;
import com.osroyale.game.Animation;
import com.osroyale.game.action.SkillIdAction;
import com.osroyale.game.action.policy.WalkablePolicy;
import com.osroyale.game.task.impl.ObjectReplacementEvent;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.containers.equipment.Equipment;
import com.osroyale.game.world.object.GameObject;
import com.osroyale.game.world.position.Area;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.RandomUtils;
import com.osroyale.util.Utility;


/**
 * Created by Daniel on 2017-12-18.
 */
public class MiningAction extends SkillIdAction<Player> {
    private final GameObject object;
    private final OreData ore;
    private final PickaxeData pickaxe;

    MiningAction(Player player, GameObject object, OreData ore, PickaxeData pickaxe) {
        super(player, 3, false, Skill.MINING);
        this.object = object;
        this.ore = ore;
        this.pickaxe = pickaxe;
    }

    private boolean mine() {
        if(!getMob().equipment.containsAny(Equipment.PICKAXES) && !getMob().inventory.containsAny(Equipment.PICKAXES)) {
            getMob().message("You need a pickaxe to mine this rock.");
            getMob().animate(Animation.RESET);
            return false;
        }
        if (getMob().inventory.getFreeSlots() == 0) {
            getMob().dialogueFactory.sendStatement("You can't carry anymore ore.").execute();
            getMob().animate(Animation.RESET);
            return false;
        }
        getMob().animate(pickaxe.animation);

        if (Mining.success(getMob(), ore, pickaxe)) {
            if (object == null || !object.active()) {
                return false;
            }

            int harvest = ore.ore;
            boolean gem = harvest == -1;

            if (gem) {
                harvest = Mining.GEM_ITEMS.next().getId();
            }

            SmeltingData smeltingData = SmeltingData.getSmeltData(harvest);
            boolean combust = smeltingData != null && pickaxe == PickaxeData.INFERNO_ADZE && Utility.random(3) == 0;

            if(!combust) {
                if (harvest == 453 && getMob().inventory.contains(24480) && getMob().coalBag.container.hasCapacityFor(new Item(harvest)))
                    getMob().coalBag.container.add(new Item(harvest));
                else
                    getMob().inventory.add(harvest, 1);
            } else {
                harvest = smeltingData.produced[0].getId();// Bar
                getMob().skills.addExperience(Skill.SMITHING, ore.infernalExperience * Config.SMITHING_MODIFICATION);
                getMob().inventory.add(harvest, 1);
            }

            getMob().skills.addExperience(Skill.MINING, ore.experience * Config.MINING_MODIFICATION);
            getMob().playerAssistant.activateSkilling(1);
            RandomEventHandler.trigger(getMob());
            Pets.onReward(getMob(), PetData.ROCK_GOLEM);
            AchievementHandler.activate(getMob(), AchievementKey.MINNING);

            if (ore == OreData.RUNITE) {
                getMob().forClan(channel -> channel.activateTask(ClanTaskKey.RUNITE_ORES, getMob().getName()));
            }

            if ((SkillCape.isEquipped(getMob(), SkillCape.MINING) || getMob().prestige.hasPerk(PrestigePerk.THE_ROCK)) && RandomUtils.success(.10)) {
                getMob().inventory.addOrDrop(new Item(harvest, 1));
            }

            if (!gem) { handleCelestialRing(harvest); }

            int base_chance = ore.ordinal() * 45;
            int modified_chance = /*getMob().equipment.isWearingChargedGlory() ? (int) (base_chance / 2.2) :*/ base_chance;

            if (Utility.random(modified_chance) == 1) {
                if (getMob().inventory.getFreeSlots() != 0 && !gem) {
                    Item item = Mining.GEM_ITEMS.next();
                    getMob().inventory.add(item);
                    getMob().send(new SendMessage("You have found " + Utility.getAOrAn(item.getName()) + " " + item.getName() + "."));
                    AchievementHandler.activate(getMob(), AchievementKey.MINNING);
                }
            }

            if (object.active() && (!ore.equals(OreData.RUNE_ESSENCE) && Utility.random(8) <= 0 && !Area.inSuperDonatorZone(object) && !Area.inRegularDonatorZone(object))) {
                this.cancel();
                getMob().resetAnimation();
                object.getGenericAttributes().set("ores", -1);
                getMob().skills.get(Skill.MINING).setDoingSkill(false);
                getMob().animate(Animation.RESET, true);
                World.schedule(new ObjectReplacementEvent(object, ore.replacement, ore.respawn, () -> {
                    object.getGenericAttributes().set("ores", ore.oreCount);
                }));
            }
        }
        return true;
    }

    private void handleCelestialRing(int oreId) {
        if (!getMob().equipment.containsAny(25541, 25545)) {
            return;
        }

        if (getMob().celestialRingCharges <= 0) {
            getMob().equipment.replace(25541, 25539, true);
            return;
        }

        if (Utility.hasOneOutOf(10)) {
            if(oreId == 453 && getMob().inventory.contains(24480) && getMob().coalBag.container.hasCapacityFor(new Item(oreId))) {
                getMob().coalBag.container.add(new Item(oreId));
            } else {
                getMob().inventory.add(oreId, 1);
            }
            getMob().skills.addExperience(Skill.MINING, ore.experience * Config.MINING_MODIFICATION);
            getMob().message("You receive an additional ore from your Celestial ring.");
        }

        //always remove a charge, even if additional ore is not received
        getMob().celestialRingCharges--;
    }

    @Override
    public void onSchedule() {
        if (!object.getGenericAttributes().has("ores")) {
            object.getGenericAttributes().set("ores", ore.oreCount);
        }

        getMob().animate(pickaxe.animation);
    }

    @Override
    public void execute() {
        if (!getMob().skills.get(Skill.MINING).isDoingSkill()) {
            cancel();
            return;
        }
        if (object == null || !object.active() || object.getGenericAttributes() == null) {
            cancel();
            return;
        }

        if (!mine()) {
            cancel();
        }
    }

    @Override
    public void onCancel(boolean logout) {
        getMob().resetFace();
        getMob().skills.get(Skill.MINING).setDoingSkill(false);
    }

    @Override
    public WalkablePolicy getWalkablePolicy() {
        return WalkablePolicy.NON_WALKABLE;
    }

    @Override
    public String getName() {
        return "mining-action";
    }
}