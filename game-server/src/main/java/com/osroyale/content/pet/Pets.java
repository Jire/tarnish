package com.osroyale.content.pet;

import com.osroyale.content.achievement.AchievementHandler;
import com.osroyale.content.achievement.AchievementKey;
import com.osroyale.content.collectionlog.CollectionLog;
import com.osroyale.content.collectionlog.CollectionLogData;
import com.osroyale.game.Animation;
import com.osroyale.game.task.Task;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.net.packet.out.SendScrollbar;
import com.osroyale.net.packet.out.SendString;
import com.osroyale.util.MessageColor;
import com.osroyale.util.Utility;

import java.util.*;

/**
 * Handles spawning, rewarding and picking up of pets.
 *
 * @author Daniel
 */
public class Pets {

    /** The cost of insuring a pet. */
    public final static int INSRUANCE_COST = 8000000;

    /** Handles calculating the chance of a player receiving a skilling pet. */
    public static void onReward(Player player, int item, int chance) {
        if (Utility.random(chance) == 0)
            onObtain(player, item);
    }

    /**
     * Handles rewarding with a 100% a chance
     * @param player
     * @param item
     * @param chance
     */
    public static void onReward(Player player, int item) {
        onObtain(player, item);
    }

    /** Generates a unique equation for special defined pets. */
    public static boolean onReward(Player player, PetData pet) {
        int chance = -1;
        if (pet == PetData.JAD)
            chance = 200;
        if (pet == PetData.ROCKY)
            chance = 5000;
        if (pet == PetData.ROCK_GOLEM)
            chance = 6500;
        if (pet == PetData.BABY_DARTH)
            chance = 30000;

        if (chance != -1 && Utility.random(chance) == 0) {
            onObtain(player, pet.getItem());
            return true;
        }

        return false;
    }

    /** Handles a player receiving a pet onReward. */
    private static void onObtain(Player player, int item) {
        Position position = Utility.findAccessableTile(player);
        if(PetData.forItem(item).isPresent() && (player.inventory.contains(item) || player.bank.contains(item) || (player.pet != null && player.pet.id == PetData.forItem(item).get().getNpc()))) {
                player.send(new SendMessage("<col=FF0000>You have a funny feeling like you would've been followed."));
                return;

        }
        if (player.pet == null && position != null) {
            onSpawn(player, item, false);
        } else if (player.inventory.hasCapacityFor(new Item(item, 1))) {
            player.inventory.add(item, 1);
            player.send(new SendMessage("You feel something weird sneaking into your backpack", MessageColor.RED));
        } else {
            player.send(new SendMessage("There was no space for the pet, he has vanished.", MessageColor.RED));
        }

        if (item == 20693) { //phoenix pet from wintertodt
            CollectionLog.logItem(player, CollectionLogData.WINTERTODT, 20693, 1);
        }
    }

    /** Handles spawning a pet. */
    public static boolean onSpawn(Player player, int item, boolean drop) {
        if (!PetData.forItem(item).isPresent())
            return false;
        if (player.pet != null) {
            player.send(new SendMessage("You already have a pet following you!"));
            return true;
        }
        Position position = Utility.findAccessableTile(player);
        if (position == null) {
            player.send(new SendMessage("You cannot drop your pet from your current location!"));
            return true;
        }
        PetData pets = PetData.forItem(item).get();
        Npc pet = new Npc(pets.getNpc(), position);
        pet.owner = player;

        if (drop) {
            player.face(position);
            player.animate(new Animation(827));
            player.inventory.remove(item, 1);
        }

        pet.register();
        pet.interact(player);
        pet.follow(player);
        pet.instance = player.instance;
        player.pet = pet;

        if (!drop) {
            player.message("<col=FF0000>You have a funny feeling like you're being followed.");
            World.sendMessage("<icon=11> <col=FF0000>Tarnish: <col=" + player.right.getColor() + ">" + player.getName() + "</col> has just received a pet " + pet.getName() + "!");
            AchievementHandler.activate(player, AchievementKey.OBTAIN_PET);
        }
        return true;
    }

    /** Handles speaking to a pet. */
    public static boolean dialogue(Player player, Npc npc) {
        Optional<PetData> pet = PetData.forNpc(npc.id);
        if (!pet.isPresent()) {
            return false;
        }
        if (npc.owner == null || !npc.owner.equals(player)) {
            player.send(new SendMessage(npc.getName() + " seems uninterested in speaking to you."));
            return true;
        }
        pet.get().dialogue(player.dialogueFactory);
        return true;
    }

    /** Handles what happens to a pet when a player dies. TODO: MULTIPLE OF THE SAME PET */
    public static void onDeath(Player player) {
        Map<PetData, Boolean> lostPets = new HashMap<>();
        if (player.pet != null) {
            Optional<PetData> petData = PetData.forNpc(player.pet.id);
            petData.ifPresent(pet -> {
                lostPets.put(pet, true);
            });
        }
        for (Item items : player.inventory) {
            if (items != null) {
                Optional<PetData> petData = PetData.forItem(items.getId());
                petData.ifPresent(pet -> {
                    lostPets.put(pet, false);
                });
            }
        }
        for (PetData pets : lostPets.keySet()) {
            if (hasInsurance(player, pets)) {
                player.lostPets.add(pets);
                player.inventory.remove(pets.getItem());
                if (lostPets.get(pets)) {
                    player.pet.unregister();
                    player.pet = null;
                }
                player.message("You have lost your pet, luckily you insured it! Speak to the insurance agent to claim.");
            } else {
                player.inventory.remove(pets.getItem());
                if (lostPets.get(pets)) {
                    player.pet.unregister();
                    player.pet = null;
                } else {
                    Npc npc = new Npc(pets.getNpc(), player.getPosition());
                    npc.unregister();
                    World.schedule(abandon(npc));
                }
                player.send(new SendMessage("Your " + pets.getName() + " has disappeared forever! Make sure to insure it!"));
            }
        }
    }


    /** The pet abandon task. */
    public static Task abandon(Npc npc) {
        return new Task(5) {
            int count = 0;

            @Override
            public void onSchedule() {
                npc.resetWaypoint();
                npc.resetFace();
            }

            @Override
            public void execute() {
                if (count >= 15) {
                    cancel();
                    return;
                }
                if (npc.movement.isMoving()) {
                    return;
                }
                Position[] boundaries = Utility.getInnerBoundaries(npc.getPosition().transform(-3, -2), 7, 7);
                npc.walk(Utility.randomElement(boundaries));
                count++;
            }

            @Override
            public void onCancel(boolean logout) {
                npc.unregister();
            }
        };
    }


    /** Handles what happens to a pet when a player logouts. */
    public static void onLogout(Player player) {
        if (player.pet != null) {
            player.pet.unregister();
        }
    }

    /** Handles what happens to a pet when a player logs in. */
    public static void onLogin(Player player) {
        if (player.pet != null) {
            Npc npc = player.pet;
            npc.owner = player;
            npc.register();
            World.schedule(5, () -> {
                npc.face(player);
                npc.interact(player);
                npc.follow(player);
            });
        }
    }

    /** Handles purchasing insurance for a pet. */
    public static void buyInsurance(Player player, Item item) {
        Optional<PetData> petData = PetData.forItem(item.getId());
        if (!petData.isPresent()) {
            player.dialogueFactory.sendNpcChat(7601, "I can't insure " + item.getName() + "!", "What do I look like to you?", "Show me some respect and use a pet on me.").execute();
            return;
        }
        if (hasInsurance(player, petData.get())) {
            player.dialogueFactory.sendNpcChat(7601, "You already have insurance for " + petData.get().getName() + "!").execute();
            return;
        }
        if (!player.inventory.contains(995, INSRUANCE_COST)) {
            player.dialogueFactory.sendNpcChat(7601, "Naw, it's going to cost " + Utility.formatDigits(INSRUANCE_COST) + " coins", "to insure your pet. Start hustling fam.").execute();
            return;
        }
        player.inventory.remove(995, INSRUANCE_COST);
        player.petInsurance.add(petData.get());
        player.dialogueFactory.sendNpcChat(7601, "You can put your trust in my that I will", "protect your little " + petData.get().getName() + ".").execute();
    }

    /** Claims all the lost pets. */
    public static void claimLostPets(Player player) {
        if (player.lostPets.isEmpty()) {
            player.dialogueFactory.sendNpcChat(7601, "You have no lost pets to claim!");
            return;
        }
        int cost = player.lostPets.size() * 250_000;
        if (!player.inventory.contains(995, cost)) {
            player.dialogueFactory.sendNpcChat(7601, "You need " + Utility.formatDigits(cost) + " coins to claim all", "of your lost pets.");
            return;
        }
        if (player.inventory.getFreeSlots() < player.lostPets.size()) {
            player.dialogueFactory.sendNpcChat(7601, "You need " + player.lostPets.size() + " free inventory spaces", "to claim your pets!");
            return;
        }
        player.inventory.remove(995, cost);
        player.lostPets.forEach(petData -> player.inventory.add(new Item(petData.getItem(), 1)));
        player.lostPets.clear();
        player.dialogueFactory.sendNpcChat(7601, "You have claimed all your lost pets for " + Utility.formatDigits(cost) + " coins.");
    }

    /** Handles opening the insurance interface. */
    public static void openInsurance(Player player) {
        int size = PetData.values().length;
        for (int index = 0, string = 37115; index < size; index++) {
            PetData pet = PetData.forOrdinal(index).get();
            player.send(new SendString("<col=" + (hasInsurance(player, pet) ? "347043" : "F24444") + ">" + pet.getName(), string++));
        }
        player.send(new SendString("<col=000000>This is a list of all pets that are allowed to be insured", 37111));
        player.send(new SendString("<col=000000>for <col=3c50b2>" + Utility.formatDigits(INSRUANCE_COST) + "<col=000000> gp. Insurance will protect a pet on death.", 37112));
        player.send(new SendString("<col=000000>Red = <col=F24444>Un-insured<col=000000> | Green = <col=347043>Insured", 37113));
        player.send(new SendString("", 37114));
        player.send(new SendString("Total Pets: " + size, 37107));
        player.send(new SendString("Pet Insurance Information", 37103));
        player.send(new SendScrollbar(37110, size * 22));
        player.interfaceManager.open(37100);
    }

    /** Handles opening the insurance interface. */
    public static void openLostPets(Player player) {
        List<PetData> pets = new ArrayList<>();
        pets.addAll(player.lostPets);
        int size = pets.size() < 7 ? 7 : pets.size();
        for (int index = 0, string = 37115; index < size; index++) {
            boolean valid = !pets.isEmpty() && index < pets.size();
            PetData pet = valid ? pets.get(index) : null;
            player.send(new SendString(valid ? "<col=255>" + pet.getName() : "", string++));
        }
        int cost = player.lostPets.size() * 250_000;
        player.send(new SendString("<col=000000>This is a list of all pets available to be claimed", 37111));
        player.send(new SendString("<col=000000>Total Free <col=3c50b2>" + Utility.formatDigits(cost) + "<col=000000> gp", 37112));
        player.send(new SendString("", 37113));
        player.send(new SendString(pets.isEmpty() ? "You have no pets to collect!" : "", 37114));
        player.send(new SendString("Total Lost: " + pets.size(), 37107));
        player.send(new SendString("Lost Pets", 37103));
        player.send(new SendScrollbar(37110, 0));
        player.interfaceManager.open(37100);
    }

    /** Checks if player already has insurance for a pet. */
    private static boolean hasInsurance(Player player, PetData pet) {
        return player.petInsurance.contains(pet);
    }
}
