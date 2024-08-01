package com.osroyale.content.activity.record;

import com.osroyale.content.activity.ActivityType;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.net.packet.out.*;
import com.osroyale.util.Utility;

import java.util.*;

public class PlayerRecord {

    private final Player player;
    public long time;
    public boolean global;
    private Map<ActivityType, TreeSet<GameRecord>> personalRecord = new HashMap<>();

    public PlayerRecord(Player player) {
        this.player = player;
    }

    public void start() {
        time = System.currentTimeMillis();
    }

    public long end(ActivityType activity) {
        return end(activity, true);
    }

    public long end(ActivityType activity, boolean track) {
        long record = System.currentTimeMillis() - time;

        /* Don't add if time is longer than 30 minutes */
        if (record > 1_800_000) {
            return 0;
        }

        if (track && !PlayerRight.isAdministrator(player)) {
            GameRecord tracker = new GameRecord(player, record, activity);
           // GlobalRecords.add(player, tracker);
            add(tracker);
        }

        time = -1;
        return record;
    }

    public void add(GameRecord tracker) {
        ActivityType activity = tracker.activityType;
        TreeSet<GameRecord> personal = personalRecord.computeIfAbsent(activity, act -> new TreeSet<>(Comparator.comparingLong(record -> record.time)));
        if (personal.size() < 10) {
            personal.add(tracker);
        } else if (tracker.time < personal.first().time) {
            personal.pollLast();
            personal.add(tracker);
        }
    }

    void clean(int size) {
        int string = 32401;
        for (int count = 0; count < (size < 25 ? 25 : size); count++) {
            player.send(new SendString("", ++string));
            player.send(new SendString("", ++string));
            player.send(new SendString("", ++string));
            player.send(new SendString("", ++string));
            string++;
        }
        player.send(new SendScrollbar(32400, (size < 10 ? 10 : size) * 28));
    }

    void showActivities(ActivityType viewing) {
        int count = 0;
        String[] marquee = new String[5];
        List<ActivityType> activities = ActivityType.getRecordable();
        for (ActivityType activity : activities) {
            String name = Utility.formatEnum(activity.name());
            player.send(new SendString((viewing == activity ? "<col=ffffff>" : "</col>") + name, 32352 + count));
            player.send(new SendTooltip("Display " + name + " records", 32352 + count));
            count +=2;
        }
        for (int index = 0; index < marquee.length; index++) {
            ActivityType type = Utility.randomElement(activities);
          //  GameRecord top = GlobalRecords.getTopRecord(type);
            activities.remove(type);
         //   String details = top == null ? "None" : top.name + "(" + Utility.getTime(top.time) + ")";
         //   marquee[index] = Utility.formatEnum(type.name()) + ": " + details;
        }
        int size = ActivityType.values().length < 12 ? 12 : ActivityType.values().length;
        player.send(new SendScrollbar(32350 , size * 23));
        player.send(new SendMarquee(32306, marquee));
    }

    public void display(ActivityType activity) {
        display(global, activity);
    }

    public void display(boolean global, ActivityType activity) {
        this.global = global;
        player.send(new SendConfig(325, global ? 1 : 0));

      /*  if (global) {
            GlobalRecords.display(player, activity);
            return;
        }*/

        clean(0);
        showActivities(activity);
        TreeSet<GameRecord> records = personalRecord.getOrDefault(activity, new TreeSet<>());

        int idx = 0;
        for (GameRecord personal : records) {
            player.send(new SendString((idx / 5 + 1) + ")", (idx + 1) + 32401));
            player.send(new SendString(Utility.formatName(personal.name), (idx + 2) + 32401));
            player.send(new SendString(personal.time == 0 ? "None!" : Utility.getTime(personal.time), (idx + 3) + 32401));
            player.send(new SendString(personal.date, (idx + 4) + 32401));
            idx += 5;
        }
        player.send(new SendScrollbar(32400, (records.size() < 9 ? 9 : records.size()) * 28));
        player.interfaceManager.open(32300);
    }
}
