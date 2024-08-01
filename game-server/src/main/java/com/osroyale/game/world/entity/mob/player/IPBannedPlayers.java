package com.osroyale.game.world.entity.mob.player;

import com.osroyale.game.task.Task;
import com.osroyale.game.world.World;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class IPBannedPlayers {

    private static final Path IP_BAN_LIST_PATH = Path.of("./data/ip-bans.txt");

    public static final List<String> ipBans = new ArrayList<>();

    public static void load() {
        if (!ipBans.isEmpty()) {
            ipBans.clear();
        }
        try {
            if (Files.notExists(IP_BAN_LIST_PATH)) {
                return;
            }

            ipBans.addAll(Files.readAllLines(IP_BAN_LIST_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void ipBan(String host) {
        host = host.toLowerCase();
        if (ipBans.contains(host)) {
            return;
        }
        ipBans.add(host);
        writeIpBans();
    }

    private static void writeIpBans() {
        World.schedule(new Task(1) {
            @Override
            public void execute() {
                try {
                    Files.write(IP_BAN_LIST_PATH, ipBans, Charset.defaultCharset());
                    cancel();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
