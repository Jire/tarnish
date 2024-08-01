package com.osroyale.content.combat;

import com.osroyale.content.achievement.AchievementHandler;
import com.osroyale.content.achievement.AchievementKey;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.items.Item;
import com.osroyale.util.Utility;

public class Killstreak {
    private final Player player;
    public int streak;

    public Killstreak(Player player) {
        this.player = player;
    }

    public void add() {
        streak++;

        if (announcementNeeded()) {
            reward();
            announce();
        }
    }

    public void end(Player killer) {
        if (announcementNeeded()) {
            String icon = "<icon=0>";
            String name = PlayerRight.getCrown(player) + " " + player.getName();
            int bounty = streak * 150;
            World.sendMessage("" + icon + " <col=FF0000>" + name + "</col> has lost their kill streak of <col=FF0000>" + streak + "</col> to <col=FF0000>" + killer.getName() + "</col>!");
            killer.inventory.addOrDrop(new Item(13307, bounty));
            killer.message("You were rewarded with " + Utility.formatDigits(bounty) + " blood money for claiming " + player.getName() + "'s bounty!");
        }

        streak = 0;
    }

    public void reward() {
        int bm = streak * 500;
        player.inventory.addOrDrop(new Item(13307, bm));
        player.message("<col=FF0000>You are rewarded with " + Utility.formatDigits(bm) + " blood money.");
    }

    private void announce() {
        String icon = "<icon=0>";
        String name = PlayerRight.getCrown(player) + " " + player.getName();
        int bounty = streak * 150;
        World.sendMessage("<col=FF0000>" + icon + " " + name + " </col>is now on a killstreak of <col=FF0000>" + streak + "</col>. Bounty: <col=FF0000>" + Utility.formatDigits(bounty) + "</col> BM.");

        if (streak == 5) {
            AchievementHandler.activate(player, AchievementKey.KILLSTKREAK_5);
        } else if (streak == 10) {
            AchievementHandler.activate(player, AchievementKey.KILLSTKREAK_10);
        } else if (streak == 15) {
            AchievementHandler.activate(player, AchievementKey.KILLSTKREAK_15);
        } else if (streak == 25) {
            AchievementHandler.activate(player, AchievementKey.KILLSTKREAK_25);
        }
    }

    private boolean announcementNeeded() {
        return streak >= 5;
    }
}
