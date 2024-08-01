package com.osroyale.content.collectionlog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.osroyale.game.world.entity.mob.player.Player;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CollectionLogSaving {

    public static void save(Player player) {
        Path path = Paths.get("./data/profile/save/collectionLogs/"+player.getUsername()+".json");
        File file = path.toFile();
        file.getParentFile().setWritable(true);
        if (!file.getParentFile().exists()) {
            try {
                file.getParentFile().mkdirs();
            } catch (SecurityException e) {
                System.out.println("Unable to create directory for player data!");
            }
        }
        try (FileWriter writer = new FileWriter(file)) {
            Gson builder = new GsonBuilder().setPrettyPrinting().create();
            JsonObject object = new JsonObject();

            object.add("collectionLog", builder.toJsonTree(player.getCollectionLog()));

            writer.write(builder.toJson(object));
        } catch (Exception e) {
        }
    }

    public static CollectionLog load(Player player) {
        CollectionLog log = new CollectionLog();

        Path path = Paths.get("./data/profile/save/collectionLogs/"+player.getUsername()+".json");
        File file = path.toFile();
        if (!file.exists()) {
            return log;
        }
        try (FileReader fileReader = new FileReader(file)) {
            JsonParser fileParser = new JsonParser();
            Gson builder = new GsonBuilder().create();
            JsonObject reader = (JsonObject) fileParser.parse(fileReader);
            if (reader.has("collectionLog")) {
                log = builder.fromJson(reader.get("collectionLog"), CollectionLog.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return log;
    }

}