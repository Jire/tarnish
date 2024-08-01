package com.osroyale.content.clanchannel.channel;

import com.google.gson.JsonObject;
import com.osroyale.content.clanchannel.ClanMember;
import com.osroyale.content.clanchannel.ClanRank;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.net.packet.out.SendScrollbar;
import com.osroyale.net.packet.out.SendString;
import com.osroyale.net.packet.out.SendTooltip;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class ClanManagement {

    /** Clan rank indices. */
    static final int
        ENTER_RANK_INDEX = 0,
        TALK_RANK_INDEX = 1,
        MANAGE_RANK_INDEX = 2;

    /** The amount of privilage options */
    private static final int SIZE = 4;

    /** An array of ranks. */
    private ClanRank[] ranks = new ClanRank[SIZE];

    /** The clan display name. */
    public String name;

    /** The clan password. */
    public String password = "";

    /** The clan slogan. */
    String slogan = "";

    /** The clan forum link. */
    String forum = "";

    /** The clan tag. */
    String tag = "";

    String color = "<col=ffffff>";

    /** The locked state of the clan. */
    public boolean locked;

    /** The lootshare state of the clan. */
    boolean lootshare;

    /** The channel to manage. */
    private final ClanChannel channel;

    ClanManagement(ClanChannel channel) {
        this.channel = channel;
        ranks[ENTER_RANK_INDEX] = ClanRank.MEMBER;
        ranks[TALK_RANK_INDEX] = ClanRank.MEMBER;
        ranks[MANAGE_RANK_INDEX] = ClanRank.LEADER;
    }

    /** Sets the enter rank. */
    public void setEnterRank(ClanRank rank) {
        ranks[ENTER_RANK_INDEX] = rank;
        channel.message("The required rank to enter has changed to " + rank.getName() + ".");
    }

    /** Sets the talk rank. */
    public void setTalkRank(ClanRank rank) {
        ranks[TALK_RANK_INDEX] = rank;
        channel.message("The required rank to talk has changed to " + rank.getName() + ".");
    }

    /** Sets the management rank. */
    public void setManageRank(ClanRank rank) {
        ranks[MANAGE_RANK_INDEX] = rank;
    }

    String getRank(int index) {
        return ranks[index].getString();
    }

    String getEnter() {
        return ranks[ENTER_RANK_INDEX].getName();
    }

    String getTalk() {
        return ranks[TALK_RANK_INDEX].getName();
    }

    String getManage() {
        return ranks[MANAGE_RANK_INDEX].getName();
    }

    boolean canEnter(ClanMember member) {
        return member.rank.greaterThanEqual(ranks[ENTER_RANK_INDEX]);
    }

    boolean canTalk(ClanMember member) {
        return member.rank.greaterThanEqual(ranks[TALK_RANK_INDEX]);
    }

    boolean canManage(ClanMember member) {
        return member.rank.greaterThanEqual(ranks[MANAGE_RANK_INDEX]);
    }

    void loadRanks(JsonObject object) {
        ranks[ENTER_RANK_INDEX] = ClanRank.valueOf(object.get("enter-rank").getAsString());
        ranks[TALK_RANK_INDEX] = ClanRank.valueOf(object.get("talk-rank").getAsString());
        ranks[MANAGE_RANK_INDEX] = ClanRank.valueOf(object.get("manage-rank").getAsString());
    }

    void saveRanks(JsonObject object) {
        object.addProperty("enter-rank", ranks[ENTER_RANK_INDEX].name());
        object.addProperty("talk-rank", ranks[TALK_RANK_INDEX].name());
        object.addProperty("manage-rank", ranks[MANAGE_RANK_INDEX].name());
    }

    public void showBanned(Player player) {
        if (channel.bannedMembers.isEmpty()) {
            player.send(new SendMessage("You have no banned members in your clan."));
            return;
        }
        int string = 51311;
        int size = channel.bannedMembers.size() < 10 ? 10 : channel.bannedMembers.size();
        for (int index = 0; index < size; index++) {
            boolean valid = index < channel.bannedMembers.size();
            Optional<String> banned = valid ? Optional.of(channel.bannedMembers.get(index)) : Optional.empty();
            player.send(new SendString(banned.orElse(""), string));
            player.send(new SendTooltip(valid ? "Unban " + channel.bannedMembers.get(index) : "", string));
            string++;
        }
        player.send(new SendScrollbar(51310, size * 23));
        player.interfaceManager.open(51300);
    }

    public void toggleLootshare() {
        lootshare = !lootshare;
        channel.message("Lootshare has been toggled " + (lootshare ? "on!" : "off!"));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof ClanManagement) {
            ClanManagement other = (ClanManagement) obj;
            return Objects.equals(name, other.name)
                && Objects.equals(slogan, other.slogan)
                && Objects.equals(forum, other.forum)
                && Objects.equals(tag, other.tag)
                && Objects.equals(locked, other.locked)
                && Objects.equals(lootshare, other.lootshare)
                && Arrays.equals(ranks, other.ranks);
        }
        return false;
    }

}
