package com.osroyale.net.discord;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 *
 * Discord.java | 8:06:53 AM
 */
public class DiscordPlugin {

    public static final boolean ENABLED = false;

    private static JDA discord = null;

    public static void startUp() {
        if (!ENABLED) {
            return;
        }

        System.out.println("Initing Discord...");
        discord = JDABuilder.createDefault(Constants.TOKEN).build();
        discord.addEventListener(new BotListener());
        discord.getPresence().setActivity(Activity.playing("Tarnish"));
    }

    public static JDA getJDA() {
        return discord;
    }

    public static void sendSimpleMessage(String message) {
        if (discord == null) {
            System.out.println("discord error: discord is null.");
            return;
        }
        discord.getTextChannelById(Constants.EVENTS_CHANNEL).sendMessage(message).queue();
    }

    public static void sendBugReport(String playername, String message) {
        if (discord == null) {
            System.out.println("discord error: discord is null.");
            return;
        }
        discord.getTextChannelById(Constants.BUG_CHANNEL).sendMessage("Bug report from " + playername + ": " + message).queue();
    }
    public static void sendAnnouncement(String message) {
        if (discord == null) {
            System.out.println("discord error: discord is null.");
            return;
        }
        discord.getTextChannelById(Constants.ANNOUNCEMENT_CHANNEL).sendMessage(message).queue();
    }

    public static void sendSuggestion(String playername, String message) {
        if (discord == null) {
            System.out.println("discord error: discord is null.");
            return;
        }
        discord.getTextChannelById(Constants.SUGGESTION_CHANNEL).sendMessage("Suggestion from " + playername + ": " + message).queue();
    }

    public static void sendPunishmentMessage(String staffMember, String action, String playerName, String time) {
        if (discord == null) {
            System.out.println("discord error: discord is null.");
            return;
        }
        Objects.requireNonNull(discord.getTextChannelById(Constants.PUNISHMENT_CHANNEL))
                .sendMessage(staffMember + " has " + action + " " + playerName + " " + time).queue();
    }

    public static void sendEventMessage(String message, String reciever, String icon, int skillID) {
        if (discord == null) {
            System.out.println("discord error: discord is null.");
            return;
        }

        Objects.requireNonNull(discord.getTextChannelById(Constants.EVENTS_CHANNEL)).sendMessage((CharSequence) new EmbedBuilder()
                .setAuthor(reciever, "https://tarnishps.com", "https://tarnishps.com/files/discord/"+icon+".png")
                .setDescription(message)
                .setThumbnail("https://tarnishps.com/files/discord/"+skillID+".png")
                .build()).queue();
    }

    public static void sendUpdateMessage(String reciever) {
        if (discord == null) {
            System.out.println("discord error: discord is null.");
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String formatedDateTime = now.format(formatter);
        String fileLine = "";
        try {
            BufferedReader file = new BufferedReader(new FileReader("source/tools/website_logs/DiscordUpdateLog.txt"));
            String line = file.readLine();

            while (line != null) {
                fileLine += "> "+line + "\n";
                // read next line
                line = file.readLine();
            }

            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Objects.requireNonNull(discord.getTextChannelById(Constants.UPDATE_CHANNEL)).sendMessage((CharSequence) new EmbedBuilder()
                .setAuthor(reciever, "https://tarnishps.com", "https://tarnishps.com/assets/images/avatar.png")
                .setDescription(fileLine)
                .setThumbnail("https://oldschool.runescape.wiki/images/thumb/Ring_of_wealth_scroll_detail.png/1200px-Ring_of_wealth_scroll_detail.png?b1944")
                .setTitle("Update Log - " + formatedDateTime)
        );
    }

    public static void pollYN(String question) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(question);
        embedBuilder.setDescription("\uD83D\uDC4D Yes    |    \uD83D\uDC4E No");

        discord.getTextChannelById(Constants.POLL_CHANNEL).sendMessage((CharSequence) embedBuilder.build()).queue(msg->{
        });
    }

}



