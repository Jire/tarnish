package com.osroyale.content.bloodmoney;

import com.osroyale.game.task.Task;
import com.osroyale.game.world.World;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

/**
 * Handles the blood chest event.
 *
 * @author Daniel
 */
public class BloodChestEvent extends Task {

    /** Constructs a new <code>BloodChestEvent</code>. */

    private NavigableMap<Double, String> timeToMessage;

    public BloodChestEvent() {
        super(100);
        timeToMessage = new TreeMap<>();
        timeToMessage.put(1.0, "<icon=0><col=FF0000> Blood Money Chest will vanish in 1 hour!");
        timeToMessage.put(0.5, "<icon=0><col=FF0000> Blood Money Chest will vanish in 30 minutes!");
        timeToMessage.put(0.25, "<icon=0><col=FF0000> Blood Money Chest will vanish in 15 minutes!");
        timeToMessage.put(0.0833, "<icon=0><col=FF0000> Blood Money Chest will vanish in 5 minutes!");
    }


    @Override
    public void execute() {
        if (!BloodMoneyChest.active) {
            if (BloodMoneyChest.stopwatch.elapsedTime(TimeUnit.HOURS) >= 4) {
                BloodMoneyChest.spawn();
                BloodMoneyChest.stopwatch.reset();
            }
            return;
        }

        double hoursPassed = BloodMoneyChest.stopwatch.elapsedTime(TimeUnit.HOURS);

        if (hoursPassed >= 4) {
            BloodMoneyChest.finish(false);
            BloodMoneyChest.stopwatch.reset();
        } else {
            double hoursRemaining = 4 - hoursPassed;
            Map.Entry<Double, String> messageEntry = timeToMessage.floorEntry(hoursRemaining);
            if (messageEntry != null && hoursRemaining <= messageEntry.getKey()) {
                String message = messageEntry.getValue();
                World.sendMessage(message);
                System.out.println("Sent message: " + message);
                timeToMessage.remove(messageEntry.getKey());
            }
        }
    }
}