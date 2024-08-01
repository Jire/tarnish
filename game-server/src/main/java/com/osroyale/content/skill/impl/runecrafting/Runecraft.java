package com.osroyale.content.skill.impl.runecrafting;

import com.osroyale.Config;
import com.osroyale.content.achievement.AchievementHandler;
import com.osroyale.content.achievement.AchievementKey;
import com.osroyale.content.activity.randomevent.RandomEventHandler;
import com.osroyale.content.clanchannel.content.ClanTaskKey;
import com.osroyale.content.event.impl.ItemInteractionEvent;
import com.osroyale.content.event.impl.ObjectInteractionEvent;
import com.osroyale.content.pet.PetData;
import com.osroyale.content.pet.Pets;
import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.object.GameObject;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.Utility;

/**
 * Handles the runecrafting skill.
 *
 * @author Daniel
 */
public class Runecraft extends Skill {

    public Runecraft(int level, double experience) {
        super(Skill.RUNECRAFTING, level, experience);
    }

    @Override
    protected double modifier() {
        return Config.RUNECRAFTING_MODIFICATION;
    }

    @Override
    protected boolean clickItem(Player player, ItemInteractionEvent event) {
        Item item = event.getItem();

        RunecraftPouchData pouch = RunecraftPouchData.forItem(item.getId());

        if (pouch == null) {
            return false;
        }

        int opcode = event.getOpcode();

        switch (opcode) {
            case 0:
                player.runecraftPouch.deposit(pouch);
                break;
            case 1:
                player.runecraftPouch.withdraw(pouch);
                break;
        }
        return true;
    }

    @Override
    protected boolean clickObject(Player player, ObjectInteractionEvent event) {

        if (event.getOpcode() != 0) {
            return false;
        }

        GameObject object = event.getObject();

        if (click(player, object)) {
            return true;
        }

        if (!RunecraftData.forId(object.getId()).isPresent()) {
            return false;
        }

        RunecraftData rune = RunecraftData.forId(object.getId()).get();

        int essence = getEssence(player);

        if (player.skills.getLevel(Skill.RUNECRAFTING) < rune.getLevel()) {
            player.send(new SendMessage("You need a runecrafting level of " + rune.getLevel() + " to do this!"));
            return true;
        }

        if (essence == -1) {
            player.send(new SendMessage("You do not have any essence!"));
            return true;
        }

        int amount = player.inventory.computeAmountForId(essence);
        int multiplier = multiplier(player, rune);

        player.locking.lock(2);

        if (rune == RunecraftData.OURNIA) {
            player.animate(new Animation(791));
            player.graphic(new Graphic(186));
            player.inventory.remove(new Item(essence, amount), -1, true);

            World.schedule(1, () -> {
                for (int index = 0; index < amount; index++) {
                    RunecraftData data = Utility.randomElement(RunecraftData.values());

                    while (data == RunecraftData.OURNIA) {
                        data = Utility.randomElement(RunecraftData.values());
                    }

                    player.inventory.add(data.getRunes(), multiplier(player, data) * 2);
                    player.skills.addExperience(Skill.RUNECRAFTING, data.getExperience() * modifier());
                }

                player.playerAssistant.activateSkilling(1);
                RandomEventHandler.trigger(player);
                Pets.onReward(player, PetData.RIFT_GUARDIAN.getItem(), 9000);
                AchievementHandler.activate(player, AchievementKey.RUNECRAFTING, amount * multiplier);
            });
            return true;
        }

        craft(player, essence, amount, rune);

        World.schedule(1, () -> {
            player.inventory.add(rune.getRunes(), amount * multiplier);
            player.playerAssistant.activateSkilling(1);
            Pets.onReward(player, PetData.RIFT_GUARDIAN.getItem(), 9000);
            AchievementHandler.activate(player, AchievementKey.RUNECRAFTING, amount * multiplier);
        });
        return true;
    }


    private void craft(Player player, int essence, int amount, RunecraftData rune) {
        if (player.skills.getLevel(Skill.RUNECRAFTING) < rune.getLevel()) {
            player.send(new SendMessage("You need a runecrafting level of " + rune.getLevel() + " to do this!"));
            return;
        }

        if (essence == -1) {
            player.send(new SendMessage("You do not have any essence!"));
            return;
        }
        player.animate(new Animation(791));
        player.graphic(new Graphic(186));
        player.inventory.remove(new Item(essence, amount), -1, true);
        player.skills.addExperience(Skill.RUNECRAFTING, (rune.getExperience() * amount) * modifier());
        RandomEventHandler.trigger(player);

        if (rune == RunecraftData.BLOOD) {
            player.forClan(channel -> channel.activateTask(ClanTaskKey.BLOOD_RUNE, player.getName(), amount));
        } else if (rune == RunecraftData.DEATH) {
            player.forClan(channel -> channel.activateTask(ClanTaskKey.DEATH_RUNE, player.getName(), amount));
        }
    }

    /** Handles teleporting to various runecrafting altars. */
    private boolean click(Player player, GameObject object) {
        if (!RunecraftTeleport.forId(object.getId()).isPresent()) {
            return false;
        }

        RunecraftTeleport teleport = RunecraftTeleport.forId(object.getId()).get();
        player.move(teleport.getPosition());
        System.out.println("reached here");
        return true;
    }

    /** Gets the essence identification from player. */
    private int getEssence(Player player) {
        return player.inventory.contains(1436) ? 1436 : (player.inventory.contains(7936) ? 7936 : -1);
    }

    /** The runecrafting multiplier. */
    private int multiplier(Player player, RunecraftData rune) {
        int multiplier = 1;
        for (int i = 0; i < rune.getMultiplier().length; i++) {
            if (player.skills.getLevel(Skill.RUNECRAFTING) >= rune.getMultiplier()[i]) {
                multiplier++;
            }
        }
        return multiplier;
    }
}
