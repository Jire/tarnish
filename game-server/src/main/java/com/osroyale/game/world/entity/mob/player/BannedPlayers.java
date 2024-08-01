package com.osroyale.game.world.entity.mob.player;

import com.osroyale.game.task.Task;
import com.osroyale.game.world.World;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BannedPlayers {

    private static final Path BAN_LIST_PATH = Path.of("./data/bans.txt");

    public static final List<String> bans = new ArrayList<>();

    public static void load() {
        if (!bans.isEmpty()) {
            bans.clear();
        }
        try {
            if (Files.notExists(BAN_LIST_PATH)) {
                return;
            }

            bans.addAll(Files.readAllLines(BAN_LIST_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void ban(String player) {
        player = player.toLowerCase();
        if (bans.contains(player)) {
            return;
        }
        bans.add(player);
        writeBans();
    }

    public static boolean unban(String player) {
        player = player.toLowerCase();
        if (!bans.remove(player)) {
            return false;
        }
        writeBans();
        return true;
    }

    private static void writeBans() {
        World.schedule(new Task(1) {
            @Override
            public void execute() {
                try {
                    Files.write(BAN_LIST_PATH, bans, Charset.defaultCharset());
                    cancel();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
