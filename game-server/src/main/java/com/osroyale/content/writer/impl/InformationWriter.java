package com.osroyale.content.writer.impl;

import com.osroyale.content.WellOfGoodwill;
import com.osroyale.content.bloodmoney.BloodMoneyChest;
import com.osroyale.content.writer.InterfaceWriter;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.combat.strategy.npc.boss.skotizo.SkotizoUtility;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.net.packet.out.SendScrollbar;
import com.osroyale.util.Utility;

/**
 * Class handles writing on the quest tab itemcontainer.
 *
 * @author Daniel
 */
public class InformationWriter extends InterfaceWriter {

    public InformationWriter(Player player) {
        super(player);
    }

    private final String[] text = {
            " <col=FF7000>Game Information:",
            "  -Players online: <col=FFB83F>" + World.getPlayerCount(),
            "  -Staff online: <col=FFB83F>" + World.getStaffCount(),
            "  -Uptime: <col=FFB83F>",
            " <col=FF7000>Events:",
            "  -Skotizo: <col=FFB83F>" + SkotizoUtility.getInformation(),
            "  -Blood Chest: <col=FFB83F>" + BloodMoneyChest.getInformation(),
            "  -Well of Goodwill: <col=FFB83F>" + WellOfGoodwill.getInformation(),
            " <col=FF7000>Player Information:",
            "  -Rank: <col=FFB83F>" + PlayerRight.getCrown(player) + " " + player.right.getName(),
            "  -Play time: <col=FFB83F>" + Utility.getTime(player.playTime),
            "  -Networth: <col=FFB83F>" + Utility.formatPrice(player.playerAssistant.networth()) + " " + (Utility.formatPrice(player.playerAssistant.networth()).endsWith("!") ? "" : "GP"),
            " <col=FF7000>Player Statistics:",
            "  -Kills: <col=FFB83F>" + player.kill,
            "  -Deaths: <col=FFB83F> " + player.death,
            "  -KDR: <col=FFB83F>" + player.playerAssistant.kdr(),
            "  -Killstreak: <col=FFB83F>" + player.killstreak.streak,
            "  -Donator Credits: <col=FFB83F>" + Utility.formatDigits(player.donation.getCredits()),
            "  -Vote Points: <col=FFB83F>" + Utility.formatDigits(player.votePoints),
            "  -Skilling Points: <col=FFB83F>" + Utility.formatDigits(player.skillingPoints),
            "  -Trivia Answered: <col=FFB83F>" + Utility.formatDigits(player.answeredTrivias),
            " <col=FF7000>Slayer Information:",
            "  -Slayer Points: <col=FFB83F>" + Utility.formatDigits(player.slayer.getPoints()),
            "  -Slayer Task: <col=FFB83F>" + (player.slayer.getTask() == null ? "None" : player.slayer.getTask().getName()),
            "  -Task Remaining: <col=FFB83F>" + Utility.formatDigits(player.slayer.getAmount()),




    };

    private int[][] font = {
            {29451, 3},
            {29455, 3},
            {29459, 3},
            {29463, 3},
            {29472, 3}


    };

    @Override
    protected int startingIndex() {
        return 29451;
    }

    @Override
    public void scroll() {
        player.send(new SendScrollbar(29450, 475));
    }

    @Override
    protected String[] text() {
        return text;
    }

    @Override
    protected int[][] color() {
        return null;
    }

    @Override
    protected int[][] font() {
        return font;
    }

}
