package com.osroyale.util.tools;

import com.osroyale.game.world.entity.mob.player.Player;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

public class LogPrinter {
    public static void printReferralLog(Player player) {
        try {
            BufferedWriter bf = new BufferedWriter(new FileWriter("backup/logs/referrals.txt", true));
            bf.write("[" + DateFormat.getDateTimeInstance().format(new Date()) +"] Referral claimed. Player: " + player.getUsername() + "IP: " + player.lastHost);
            bf.close();
        } catch (IOException ignored) {
        }
    }
    public static void printToplist(Player player, String toplist) {
        try {
            BufferedWriter bf = new BufferedWriter(new FileWriter("backup/logs/"+toplist+".txt", true));
            bf.write("[" + DateFormat.getDateTimeInstance().format(new Date()) +"] Player: " + player.getUsername());
            bf.close();
        } catch (IOException ignored) {
        }
    }
}
