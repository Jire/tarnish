/*
package com.osroyale.content.activity.record;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.osroyale.content.activity.ActivityType;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.out.SendScrollbar;
import com.osroyale.net.packet.out.SendString;
import com.osroyale.util.GsonUtils;
import com.osroyale.util.Utility;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

*/
/**
 * Created by Daniel on 2017-11-22.
 *//*

public class GlobalRecords {

    private static Set<GameRecord> GLOBAL_RECORDS = new HashSet<>();

    public static void display(Player player, ActivityType activity) {
        List<GameRecord> list = getActivities(activity);
        player.gameRecord.showActivities(activity);

        for (int index = 0, count = 0; index < 25; index++) {
            if (index >= list.size())
                continue;
            GameRecord record = list.get(index);
            count++;
            String prefix = index == 1 ? "<clan=6>" : (count == 6 ? "<clan=5>" : (count == 11 ? "<clan=4>" : (count / 5) + 1 + ")"));
            player.send(new SendString(prefix, 32401 + count));
            count++;
            player.send(new SendString(record.name, 32401 + count));
            count++;
            player.send(new SendString(Utility.getTime(record.time), 32401 + count));
            count++;
            player.send(new SendString(record.date, 32401 + count));
            count++;
        }

        player.send(new SendScrollbar(32400, 696));
        player.interfaceManager.open(32300);
    }

    public static void add(Player player, GameRecord tracker) {
        GameRecord record = getTracker(player.getName(), tracker.activityType);
        if (record == null) {
            GLOBAL_RECORDS.add(tracker);
            return;
        }
        if (tracker.time > record.time) {
            GLOBAL_RECORDS.remove(record);
            GLOBAL_RECORDS.add(tracker);
        }
    }

    private static GameRecord getTracker(String name, ActivityType activity) {
        Set<GameRecord> set = getGlobalRecords(name);

        if (set == null || set.isEmpty()) {
            return null;
        }

        for (GameRecord t : set) {
            if (t.activityType == activity)
                return t;
        }
        return null;
    }

    static GameRecord getTopRecord(ActivityType activity) {
        List<GameRecord> records = getActivities(activity);
        return records.isEmpty() ? null : records.get(0);
    }

    private static Set<GameRecord> getGlobalRecords(String name) {
        Set<GameRecord> set = new HashSet<>();
        if (GLOBAL_RECORDS == null || GLOBAL_RECORDS.isEmpty()) {
            return set;
        }

        for (GameRecord tracker : GLOBAL_RECORDS) {
            if (!tracker.name.equalsIgnoreCase(name))
                continue;
            set.add(tracker);
        }
        return set;
    }

    private static List<GameRecord> getActivities(ActivityType activity) {
        List<GameRecord> set = new ArrayList<>();
        for (GameRecord tracker : GLOBAL_RECORDS) {
            if (tracker.activityType != activity)
                continue;
            set.add(tracker);
        }
        set.sort(Comparator.comparingLong(record -> record.time));
        return set;
    }

    public static void load() {
        Type type = new TypeToken<Set<GameRecord>>() {
        }.getType();
        Path path = Paths.get("data", "/content/game/game_records.json");
        try (FileReader reader = new FileReader(path.toFile())) {
            JsonParser parser = new JsonParser();
            GLOBAL_RECORDS = new GsonBuilder().create().fromJson(parser.parse(reader), type);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        new Thread(() -> {
            try (FileWriter fw = new FileWriter("./data/content/game/game_records.json")) {
                fw.write(GsonUtils.JSON_PRETTY_NO_NULLS.toJson(GLOBAL_RECORDS));
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
*/
