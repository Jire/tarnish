package com.osroyale.content.skill.impl.firemaking;

import com.osroyale.Config;
import com.osroyale.content.activity.randomevent.RandomEventHandler;
import com.osroyale.content.clanchannel.content.ClanTaskKey;
import com.osroyale.content.event.impl.ItemOnItemInteractionEvent;
import com.osroyale.content.event.impl.ObjectInteractionEvent;
import com.osroyale.content.pet.PetData;
import com.osroyale.content.pet.Pets;
import com.osroyale.content.prestige.PrestigePerk;
import com.osroyale.content.skillcape.SkillCape;
import com.osroyale.game.action.Action;
import com.osroyale.game.action.policy.WalkablePolicy;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.object.GameObject;
import com.osroyale.util.RandomUtils;
import com.osroyale.util.Utility;

/**
 * The firemaking skill.
 *
 * @author Daniel
 */
public class Firemaking extends Skill {

    public Firemaking(int level, double experience) {
        super(Skill.FIREMAKING, level, experience);
    }

    @Override
    protected boolean useItem(Player player, ItemOnItemInteractionEvent event) {
        Item first = event.getFirst();
        Item second = event.getSecond();
        FiremakingData firemaking = null;
        Item item = null;
        if (!FiremakingData.forId(second.getId()).isPresent() && !FiremakingData.forId(first.getId()).isPresent())
            return false;
        if (first.getId() == 590) {
            firemaking = FiremakingData.forId(second.getId()).get();
            item = second;
        } else if (second.getId() == 590) {
            firemaking = FiremakingData.forId(first.getId()).get();
            item = first;
        }
        if (firemaking == null)
            return false;
        player.action.execute(new FiremakingAction(player, item, firemaking), true);
        return true;
    }
    public static double getBonus(Player player) {
        double bonus = 0;
        if(player.equipment.getId(0) == 20708)
            bonus += 0.4;
        if(player.equipment.getId(4) == 20704)
            bonus += 0.8;
        if(player.equipment.getId(7) == 20706)
            bonus += 0.6;
        if(player.equipment.getId(10) == 20710)
            bonus += 0.2;

        if(player.equipment.containsAll(20708, 20704, 20706, 20710))
            bonus = 2.5;

        return bonus;
    }
    @Override
    protected boolean clickObject(Player player, ObjectInteractionEvent event) {
        if (event.getOpcode() != 0 || event.getObject().getId() != 5249) {
            return false;
        }

        if (player.getPosition().equals(event.getObject().getPosition())) {
            player.message("Please step out of the fire!");
            return true;
        }

        FiremakingData firemaking = null;

        for (Item item : player.inventory) {
            if (item != null && FiremakingData.forId(item.getId()).isPresent()) {
                firemaking = FiremakingData.forId(item.getId()).get();
                break;
            }
        }

        if (firemaking == null) {
            player.dialogueFactory.sendStatement("You have no logs in your inventory to add to this fire!").execute();
            return true;
        }

        if (player.skills.getMaxLevel(Skill.FIREMAKING) < firemaking.getLevel()) {
            player.message("You need a firemaking level of " + firemaking.getLevel() + " to light this log!");
            return true;
        }

        player.action.execute(bonfireAction(player, event.getObject(), firemaking, player.inventory.computeAmountForId(firemaking.getLog())));
        return true;
    }


    private Action<Player> bonfireAction(Player player, GameObject object, FiremakingData firemaking, int toBurn) {
        return new Action<Player>(player, 3) {
            int amount = toBurn;

            @Override
            public WalkablePolicy getWalkablePolicy() {
                return WalkablePolicy.NON_WALKABLE;
            }

            @Override
            public String getName() {
                return "Bonfire action";
            }

            @Override
            public void execute() {
                if (amount <= 0) {
                    cancel();
                    return;
                }
                if (!object.active()) {
                    cancel();
                    return;
                }
                if (!player.inventory.contains(firemaking.getLog())) {
                    player.message("You have no more logs!");
                    cancel();
                    return;
                }
                final boolean saveLog = SkillCape.isEquipped(player, SkillCape.FIREMAKING) && Utility.random(1, 3) == 1;
                if (!saveLog) {
                    player.inventory.remove(firemaking.getLog(), 1);
                }
                player.animate(733);
                player.skills.addExperience(Skill.FIREMAKING, firemaking.getExperience() * Config.FIREMAKING_MODIFICATION);
                RandomEventHandler.trigger(player);
                player.playerAssistant.activateSkilling(1);
                Pets.onReward(getMob(), PetData.PHOENIX.getItem(), 8562);

                if (firemaking == FiremakingData.WILLOW_LOG) {
                    player.forClan(channel -> channel.activateTask(ClanTaskKey.BURN_WILLOW_LOG, getMob().getName()));
                }

                if (player.prestige.hasPerk(PrestigePerk.FLAME_ON) && RandomUtils.success(.25)) {
                    player.inventory.remove(firemaking.getLog(), 1);
                    player.skills.addExperience(Skill.FIREMAKING, firemaking.getExperience() * Config.FIREMAKING_MODIFICATION);
                }

                if (!saveLog) {
                    amount--;
                }
            }

            @Override
            public void onCancel(boolean logout) {
//                player.resetAnimation();
            }
        };
    }
}
