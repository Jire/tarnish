package com.osroyale.game.world.entity.mob.player;

import com.osroyale.game.task.Task;
import com.osroyale.game.world.World;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class IPBannedPlayers {

    private static final String IP_BAN_LIST_PATH = "./data/ip-bans.txt";

    public static final List<String> ipBans = new ArrayList<>();

    public static void load() {
        if (!ipBans.isEmpty()) {
            ipBans.clear();
        }
        try {
            ipBans.addAll(Files.readAllLines(Paths.get(IP_BAN_LIST_PATH)));
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
                    Files.write(Paths.get(IP_BAN_LIST_PATH), ipBans, Charset.defaultCharset());
                    cancel();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
