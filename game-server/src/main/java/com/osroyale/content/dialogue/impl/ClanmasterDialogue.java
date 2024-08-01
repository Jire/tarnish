package com.osroyale.content.dialogue.impl;

import com.osroyale.content.clanchannel.ClanMember;
import com.osroyale.content.clanchannel.channel.ClanChannel;
import com.osroyale.content.clanchannel.content.ClanLevel;
import com.osroyale.content.clanchannel.content.ClanTask;
import com.osroyale.content.clanchannel.content.ClanViewer;
import com.osroyale.content.dialogue.Dialogue;
import com.osroyale.content.dialogue.DialogueFactory;
import com.osroyale.content.store.Store;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.out.SendItemOnInterface;
import com.osroyale.net.packet.out.SendScrollbar;
import com.osroyale.net.packet.out.SendString;
import com.osroyale.util.Difficulty;
import com.osroyale.util.Utility;

import java.util.Optional;

/**
 * Handles the clan master dialogue.
 *
 * @author Daniel
 */
public class ClanmasterDialogue extends Dialogue {

    @Override
    public void sendDialogues(DialogueFactory factory) {
        Player player = factory.getPlayer();
        factory.sendNpcChat(3841, "Ello mate!", "What can I do you for today?");
        factory.sendOption("Information on Clans", () -> clanInformation(factory),
                "World Clan List", () -> player.clanViewer.open(ClanViewer.ClanTab.OVERVIEW),
                "Clan Management", () -> clanManagement(factory),
                "Clan Levels", () -> clanLevel(factory.getPlayer()),
                "Nevermind", factory::clear);
        factory.execute();
    }

    private void mainOptions(DialogueFactory factory) {
    }

    /** The clan information dialogue. */
    private void clanInformation(DialogueFactory factory) {
        factory.sendOption("What is a clan?", () -> clanDefinition(factory),
                "What are clan perks?", () -> clanPerks(factory),
                "Nevermind", factory::clear);
    }

    private void clanDefinition(DialogueFactory factory) {
        factory.sendNpcChat(3841, "A clan is a united group or syndicate of players",
                "who work together to complete various tasks depending on the", "clan type. Clans members can identify one another",
                "by the purple dot on the minimap or their clan tag.");
    }

    private void clanPerks(DialogueFactory factory) {
        factory.sendNpcChat(3841,
                "I offer many perks and items to be used for clans.",
                "Clan perks affect all online clan members when used.",
                "These perks vary from rewarding everyone with",
                "random experience in a random skill to having a drop modifier");
        factory.sendNpcChat(3841, "for a certain amount of time.");
    }

    /** The clan management dialogue. */
    private void clanManagement(DialogueFactory factory) {
        Player player = factory.getPlayer();
        ClanChannel channel = player.clanChannel;

        if (channel == null) {
            factory.sendNpcChat(3841, "You must be in a clan in order to do this!");
            return;
        }

        Optional<ClanMember> member = channel.getMember(player.getName());

        if (!member.isPresent() || !channel.canManage(member.get())) {
            factory.sendNpcChat(3841, "You do not have the proper authorization to manage", "the clan " + channel.getName() + "! If you want to manage your own clan", "please join your own clan and then speak to me again.");
            return;
        }

        factory.sendOption("View store", () -> Store.STORES.get("The Clanmaster's Store").open(player), "Manage tasks", () -> clanTask(channel, factory), "Nevermind", factory::clear);
    }

    /** The clan task dialogue. */
    private void clanTask(ClanChannel channel, DialogueFactory factory) {
        ClanTask task = channel.getDetails().clanTask;
        factory.sendOption("Task Information", () -> {
            if (task == null) {
                factory.sendStatement("Your clan currently does not have a task assigned!");
                return;
            }

            factory.sendStatement("Your current task is to:", task.getName(channel));
        }, "Obtain Clan Task", () -> {
            if (task != null) {
                factory.sendNpcChat(3841, "Your clan currently has a task assigned!", "Cancel or complete it to obtain another one.");
                return;
            }
            factory.sendOption("Easy Task - <col=255>2 CP</col> Reward", () -> channel.receiveTask(Difficulty.EASY), "Medium Task - <col=255>3 CP</col> Reward", () -> channel.receiveTask(Difficulty.MEDIUM), "Hard Task - <col=255>5 CP</col> Reward", () -> channel.receiveTask(Difficulty.HARD));
        }, "Cancel Clan Task (<col=FF2929>5 CP</col>)", () -> {
            if (task == null) {
                factory.sendStatement("Your clan currently does not have a task assigned!");
                return;
            }
            if (channel.getDetails().points < 5) {
                factory.sendStatement("You do not have enough points to cancel your task.", "Current CP: " + channel.getDetails().points);
                return;
            }
            channel.getDetails().points -= 5;
            channel.getDetails().taskAmount = -1;
            channel.getDetails().clanTask = null;
            channel.message("Our clan task was cancelled for 25 CP. We have <col=255>" + Utility.formatDigits(channel.getDetails().points) + "</col> remaining.");
        }, "Nevermind", factory::clear);
    }

    /** Displays all the clan level information. */
    private void clanLevel(Player player) {
        int size = ClanLevel.values().length;
        for (int index = 0, string = 37111; index < size; index++) {
            ClanLevel level = ClanLevel.values()[index];
            String color = "<col=" + level.getColor() + ">";
            String experience = "Experience: <col=7A6856>" + Utility.formatDigits(level.getExperience()) + "</col>";
            String points = "Points: <col=7A6856>" + Utility.formatDigits(level.getPoints()) + "</col>";
            player.send(new SendString(color + Utility.formatEnum(level.name()), string));
            string++;
            player.send(new SendString(experience + " <col=000000>|</col> " + points, string));
            string++;
            player.send(new SendString("", string));
            string++;
        }
        player.send(new SendString("", 37107));
        player.send(new SendString("Clan Levels Information", 37103));
        player.send(new SendScrollbar(37110, 750));
        player.send(new SendItemOnInterface(37199));
        player.interfaceManager.open(37100);
    }
}
