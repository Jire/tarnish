package com.osroyale.util.tools;

import com.osroyale.game.world.entity.mob.player.Player;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class LogReader {
    private static String fileLine = "";

    public static boolean alreadyClaimedReferral(Player player) {
        try {
            BufferedReader file = new BufferedReader(new FileReader("backup/logs/referrals.txt"));
            String line;
            while ((line = file.readLine()) != null) {
                fileLine = line;
                if (fileLine.contains(player.lastHost) || fileLine.contains(player.getUsername())) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            if (fileLine.contains(player.lastHost) || fileLine.contains(player.getUsername())) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}
