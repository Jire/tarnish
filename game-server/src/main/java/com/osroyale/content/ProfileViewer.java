package com.osroyale.content;

import com.osroyale.content.achievement.AchievementHandler;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.net.packet.out.SendPlayerIndex;
import com.osroyale.net.packet.out.SendString;
import com.osroyale.net.packet.out.SendTooltip;
import com.osroyale.util.Utility;

import java.util.Optional;

/**
 * Handles viewing other player's profiles.
 *
 * @author Daniel
 */
public class ProfileViewer {

    /** Gets the main strings to display. */
    public static String[] string(Player player) {
        return new String[]{
                "Created: ", player.created,
                "Total play time:", Utility.getTime(player.playTime),
                "Networth:", "" + Utility.formatPrice(player.playerAssistant.networth()),
                "Clan:", player.clanChannel == null ? "None" : player.clanChannel.getName(),
                "Total level:", "" + Utility.formatDigits(player.skills.getTotalLevel()),
                "Kills/Deaths/KDR", "" + player.kill + "/" + player.death + "/" + player.playerAssistant.kdr() + "",
                "Current KC:", "" + player.killstreak.streak,
                "Vote Points:", "" + Utility.formatDigits(player.votePoints),
                "Total Votes:", "" + Utility.formatDigits(player.totalVotes),
                "Skilling Points:", "" + Utility.formatDigits(player.skillingPoints),
                "Mage's Arena Points:", "" + Utility.formatDigits(player.mageArenaPoints),
                "Pest Control Points:", "" + Utility.formatDigits(player.pestPoints),
                "Achievements Completed:", "" + AchievementHandler.getTotalCompleted(player) + "",
        };
    }

    /** Opens the profile interface. */
    public static void open(Player player, Player other) {
        if (!other.getPosition().isWithinDistance(player.getPosition(), 26)) {
            player.message("You must get closer to that player if you want to view their profile!");
            return;
        }

        if (player.getCombat().inCombat()) {
            player.send(new SendMessage("You can't view profiles whilst in combat!"));
            return;
        }

        for (int index = 0, string = 51832; index < Skill.SKILL_COUNT; index++, string += 2) {
            Skill skill = other.skills.get(index);
            String color = Integer.toHexString(other.prestige.getPrestigeColor(index));
            player.send(new SendString(skill.getMaxLevel() + "<col=" + color + "></col>/<col=" + color + ">" + skill.getMaxLevel(), string));
        }

        int string = 51902;
        for (String context : string(other)) {
            player.send(new SendString(context, string));
            string += 2;
        }

        if (PlayerRight.isModerator(player) && !player.equals(other)) {
            player.send(new SendString("Manage Player: " + other.getName(), 51811));
            player.send(new SendTooltip("Manage Player: " + other.getName(), 51811));
            player.managing = Optional.of(other);
        } else {
            player.send(new SendString("", 51811));
            player.send(new SendTooltip("", 51811));
        }

        player.send(new SendPlayerIndex(other.getIndex()));
        player.send(new SendString("", 51810));
        player.send(new SendString("</col>Name: <col=FFB83F>" + other.getName(), 51807));
        player.send(new SendString("</col>Rank: <col=FFB83F>" + PlayerRight.getCrown(other) + " " + other.right.getName(), 51808));
        player.send(new SendString("</col>Level: <col=FFB83F>" + other.skills.getCombatLevel(), 51809));
        player.send(new SendMessage("You are now viewing " + other.getName() + "'s profile."));
        player.interfaceManager.open(51800);
    }
}