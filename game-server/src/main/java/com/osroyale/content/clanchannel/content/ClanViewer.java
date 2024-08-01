package com.osroyale.content.clanchannel.content;

import com.osroyale.Config;
import com.osroyale.content.clanchannel.ClanMember;
import com.osroyale.content.clanchannel.ClanRepository;
import com.osroyale.content.clanchannel.channel.ClanChannel;
import com.osroyale.content.clanchannel.channel.ClanChannelHandler;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.net.packet.out.*;
import com.osroyale.util.Difficulty;
import com.osroyale.util.Utility;

import java.util.*;

/**
 * Handles viewing clan information.
 *
 * @author Daniel
 */
public class ClanViewer {

    public enum ClanTab {OVERVIEW, MEMBERS}
    private final Player player;
    private ClanChannel viewing;
    public int clanIndex = 0;
    public int memberIndex = 0;
    public List<ClanChannel> clanList = new ArrayList<>();
    public List<ClanMember> clanMembers = new ArrayList<>();
    private String searchKey = "";
    public Filter filter = Filter.ALL_TIME;

    public ClanViewer(Player player) {
        this.player = player;
    }

    public void open(ClanTab tab) {
        open(viewing, tab);
    }

    public void open(ClanChannel channel, ClanTab tab) {
        int interfaceId = 0;
        this.viewing = channel;
        String name = Utility.formatName(filter.name().toLowerCase().replaceAll("_", " "));
        Item[] showcase = viewing == null ? new Item[3] : viewing.getShowcaseItems();

        if (tab == ClanTab.OVERVIEW) {
            drawClanList(searchKey);
            drawClanDetails();
            interfaceId = 53000;
            player.send(new SendString(name + ":", 53006));
        } else if (tab == ClanTab.MEMBERS) {
            drawClanMembers();
            interfaceId = 53500;
            player.send(new SendString("Member List:", 53006));
        }

        player.send(new SendConfig(531, tab.ordinal()));
        player.send(new SendString(viewing != null ? viewing.getName() : "", 53009));
        player.send(new SendString(viewing != null ? viewing.getSlogan() : "", 53010));
        player.send(new SendString(searchKey.isEmpty() ? "Search for clan" : searchKey, 53012));
        player.send(new SendString(name, 53019));
        player.send(new SendItemOnInterface(53011, showcase));
        player.interfaceManager.open(interfaceId);
    }

    private void drawClanList(String key) {
        Set<ClanChannel> channels = ClanRepository.getTopChanels(filter).orElse(null);
        if (channels == null) return;
        if (!key.isEmpty()) {
            channels.removeIf(channel -> !channel.getName().contains(key));
        }
        int size = channels.size() > 13 ? channels.size() : 13;
        Iterator<ClanChannel> iterator = channels.iterator();
        for (int index = 0, string = 53202; index < size; index++, string += 2) {
            if (iterator.hasNext()) {
                ClanChannel channel = iterator.next();
                String prefix = (index + 1) + ")";
                player.send(new SendString((clanIndex == index ? "<col=ffffff>" : "") + prefix + " " + channel.getName(), string));
                player.send(new SendTooltip("View " + channel.getName() + "'s clan profile", string));
            } else {
                player.send(new SendString("", string));
                player.send(new SendTooltip("", string));
            }
        }
        clanList = new ArrayList<>(channels);
        player.send(new SendScrollbar(53300, size * 14));
    }

    private void drawClanDetails() {
        String[] strings = getDetails();
        assert strings.length != 0;
        for (int index = 0, string = 53102; index < strings.length; index++, string += 2) {
            player.send(new SendString(strings[index] == null ? "" : strings[index], string));
        }
    }

    /** Gets the clan information details. */
    private String[] getDetails() {
        if (viewing == null)
            return new String[10];
        return new String[]{
                "</col>Owner: " + viewing.getColor() + "<clan=7> " + Utility.formatName(viewing.getOwner()),
                "</col>Established: " + viewing.getColor() + "" + viewing.getDetails().established,
                "</col>Members: " + viewing.getColor() + "" + viewing.getMembers().size(),
                "</col>Type: " + viewing.getColor() + viewing.getDetails().type.getIcon() + " " + Utility.formatEnum(viewing.getDetails().type.name()),
                "</col>Level: " + viewing.getColor() + "" + Utility.formatEnum(viewing.getDetails().level.name()),
                "</col>Tag " + viewing.getColor() + "" + (viewing.getTag().length() <= 0 ? "None" : ("[" + viewing.getTag()) + "]"),
                "</col>Experience: " + viewing.getColor() + "" + Utility.formatDigits(viewing.getDetails().experience),
                "</col>Points: " + viewing.getColor() + "" + Utility.formatDigits(viewing.getDetails().points),
                "</col>Achievements Completed: " + viewing.getColor() + "NA",
                "</col>Tasks Completed: " + viewing.getColor() + "NA",
//           (channel.modifier == null ? "None" : (channel.modifier.name) + "(" + channel.modifierDuration + " minute(s)") + ")",
                "</col>Avg. Total: " + viewing.getColor() + "" + viewing.getDetails().getAverageTotal(),
        };
    }

    private String[] getProfile(ClanMember member) {
        if (member == null)
            return new String[10];
        return new String[]{
                "</col>Rank: " + viewing.getColor() + "" + member.rank.getString() + " " + member.rank.name,
                "</col>Joined: " + viewing.getColor() + "" + member.joined,
                "</col>Exp Gained: " + viewing.getColor() + "" + Utility.formatDigits(member.expGained),
                "</col>PvP Kills: " + viewing.getColor() + "" + member.playerKills,
                "</col>PvM Kills: " + viewing.getColor() + "" + member.npcKills,
                "",
                "",
                "",
                "",
        };
    }

    private void drawClanMembers() {
        int size = 18;
        List<ClanMember> membersList = new ArrayList<>(size);
        if (viewing != null) {
            viewing.forEach(o -> {
                if (o != null)
                    membersList.add(o);
            });
            size = membersList.size() < 18 ? 18 : membersList.size();
        }
        for (int i = 0, string = 55052; i < size; i++, string += 2) {
            boolean valid = i < membersList.size();
            ClanMember member = valid ? membersList.get(i) : null;
            String name = valid ? member.rank.getString() + " " + member.name : "";
            player.send(new SendString((i == memberIndex ? "<col=ffffff>" : "") + name, string));
            player.send(new SendTooltip(valid ? "View " + member.name + "'s clan profile" : "", string));
        }
        player.send(new SendScrollbar(55050, size * 18));
        clanMembers = membersList;

        if (memberIndex > membersList.size())
            return;

        ClanMember member = membersList.get(memberIndex);

        if (member == null)
            return;

        String[] strings = getProfile(member);

        for (int index = 0, string = 55012; index < strings.length; index++, string += 2) {
            player.send(new SendString(strings[index] == null ? "" : strings[index], string));
        }
    }

    public void update(ClanChannel channel) {
        if (player.interfaceManager.isInterfaceOpen(53000))
            open(channel, ClanTab.OVERVIEW);
        if (player.interfaceManager.isSidebar(Config.CLAN_TAB, 42000)) {
            ClanChannelHandler.manage(player);
        }
    }

    public void viewAchievements() {
        ClanChannel channel = player.clanChannel;
        int size = ClanAchievement.values().length;

        Difficulty old = null;

        for (int index = 0, string = 37114; index < size; index++, string++) {
            Optional<ClanAchievement> achievement = ClanAchievement.forOrdinal(index);
            if (achievement.isPresent()) {
                boolean completed = channel.getDetails().completedAchievement(achievement.get());
                Difficulty difficulty = achievement.get().difficulty;

                if (old != difficulty) {
                    string++;
                    player.send(new SendString("<col=255>[" + Utility.formatEnum(difficulty.name()) + "]", string));
                    string++;
                    player.send(new SendString("", string));
                    string++;
                }

                old = difficulty;
                int progress = channel.getDetails().getAchievementCompletion(achievement.get());
                int goal = achievement.get().amount;

                String name = (completed ? "<col=347043>" + "" : "") + achievement.get().details + " (" + Utility.getPercentageAmount(progress, goal) + "%)";
                player.send(new SendString(name, string));
            } else {
                player.send(new SendString("", string));
            }
        }

        player.send(new SendString("<col=000000>These are all the clan achievements available", 37111));
        player.send(new SendString("<col=000000>Completed achievements will be highlighted in <col=347043>green</col>", 37112));
        player.send(new SendString("Completed: 0/" + size, 37113));
        player.send(new SendString("", 37114));
        player.send(new SendString("", 37107));
        player.send(new SendString(channel.getName() + "'s Achievements", 37103));
        player.send(new SendScrollbar(37110, 500));
        player.interfaceManager.open(37100);
    }

    public enum Filter {
        ALL_TIME,
        TOP_PVP,
        TOP_PVM,
        TOP_SKILLING,
        TOP_IRON_MAN
    }

}
