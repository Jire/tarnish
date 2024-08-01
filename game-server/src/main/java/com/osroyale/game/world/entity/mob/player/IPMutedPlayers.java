package com.osroyale.game.world.entity.mob.player;

import com.osroyale.game.task.Task;
import com.osroyale.game.world.World;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class IPMutedPlayers {

    private static final String IP_MUTE_LIST_PATH = "./data/ip-mutes.txt";

    public static final List<String> ipMutes = new ArrayList<>();

    public static void load() {
        if (!ipMutes.isEmpty()) {
            ipMutes.clear();
        }
        try {
            ipMutes.addAll(Files.readAllLines(Paths.get(IP_MUTE_LIST_PATH)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isIpMuted(String host) {
        return ipMutes.contains(host);
    }

    public static void ipMute(String host) {
        host = host.toLowerCase();
        if (ipMutes.contains(host)) {
            return;
        }
        ipMutes.add(host);
        writeIpMutes();
    }

    public static boolean unIpMute(String host) {
        host = host.toLowerCase();
        if (!ipMutes.remove(host)) {
            return false;
        }
        writeIpMutes();
        return true;
    }

    private static void writeIpMutes() {
        World.schedule(new Task(1) {
            @Override
            public void execute() {
                try {
                    Files.write(Paths.get(IP_MUTE_LIST_PATH), ipMutes, Charset.defaultCharset());
                    cancel();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
