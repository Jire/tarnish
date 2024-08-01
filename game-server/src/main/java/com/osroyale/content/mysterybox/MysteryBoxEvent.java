package com.osroyale.content.mysterybox;

import com.osroyale.game.task.TickableTask;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.out.SendColor;
import com.osroyale.net.packet.out.SendString;
import com.osroyale.util.Utility;

import java.util.*;


public class MysteryBoxEvent extends TickableTask {
    private final Player player;
    private List<MysteryItem> items;
    private List<MysteryItem> allItems;
    private int speed;
    private final MysteryBoxManager mysteryBox;


    MysteryBoxEvent(Player player) {
        super(false, 1);
        this.player = player;
        this.items = new ArrayList<>();
        this.allItems = new ArrayList<>();
        this.speed = Utility.random(30, 40);
        this.mysteryBox = player.mysteryBox;
    }

    private void move() {
        allItems.add(items.get(0));
        items.remove(0);
        MysteryItem next = getNextItem();
        allItems.remove(next);
        items.add(next);
    }

    private MysteryItem getNextItem() {
        MysteryItem next = null;
        for (MysteryItem item : allItems) {
            if (!items.contains(item)) {
                next = item;
                break;
            }
        }
        return next;
    }

    private double getItemProbability(MysteryItem item) {
        switch (item.rarity) {
            case COMMON:
                return 0.40;
            case UNCOMMON:
                return 0.35;
            case RARE:
                return 0.15;
            case EXOTIC:
                return 0.10;
            default:
                return 0;
        }
    }



    private void reward() {
        double randomValue = Math.random();
        double cumulativeProbability = 0.0;
        MysteryItem reward = null;

        Collections.sort(items, Comparator.comparingDouble(this::getItemProbability).reversed());

        for (MysteryItem item : items) {
            double itemProbability = getItemProbability(item);
            cumulativeProbability += itemProbability;
            if (randomValue <= cumulativeProbability) {
                reward = item;
                break;
            }
        }

        if(reward == null) {
            reward = items.get(0);
        }
        if (reward.rarity == MysteryRarity.EXOTIC) {
            World.sendMessage("<icon=17><col=5739B3> Tarnish: <col=" + player.right.getColor() + ">" + player.getName() + " </col>has won " + Utility.getAOrAn(reward.getName()) + " <col=5739B3>" + reward.getName() + " </col>from the <col=5739B3>" + mysteryBox.box.name() + "</col>.");
        }

        player.locking.unlock();
        player.inventory.add(reward.getId(), reward.getAmount());
        player.message("Congratulations! You have won @red@" + Utility.getAOrAn(reward.getName()) + " " + reward.getName() + "!\nThis item falls in the rarity of " + reward.rarity.name().toLowerCase() + ".");
        System.out.println("Rarity: " + reward.rarity.name().toLowerCase());
    }
    @Override
    public boolean canSchedule() {
        return player.mysteryBox.box != null;
    }
    @Override
    public void onSchedule() {
        player.dialogueFactory.clear();
        player.locking.lock();
        allItems.addAll(Arrays.asList(mysteryBox.box.rewards()));
        player.inventory.remove(mysteryBox.box.item(), 1);
        mysteryBox.count = player.inventory.computeAmountForId(mysteryBox.box.item());
        Collections.shuffle(allItems);
        for (int index = 0; index < 11; index++) {
            if (index >= allItems.size())
                continue;
            MysteryItem item = allItems.get(index);
            items.add(item);
            allItems.remove(index);
        }

        player.send(new SendColor(59508, 0xF01616));
        player.send(new SendString("You have " + mysteryBox.count + " mystery box available!", 59507));
    }

    @Override
    protected void tick() {
        cancel();
    }

    @Override
    public void onCancel(boolean logout) {
        if (logout) {
            player.inventory.add(mysteryBox.box.item(), 1);
        } else {
            reward();
        }
    }
}
