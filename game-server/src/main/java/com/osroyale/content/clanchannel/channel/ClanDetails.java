package com.osroyale.content.clanchannel.channel;

import com.osroyale.content.clanchannel.ClanMember;
import com.osroyale.content.clanchannel.ClanType;
import com.osroyale.content.clanchannel.content.ClanAchievement;
import com.osroyale.content.clanchannel.content.ClanLevel;
import com.osroyale.content.clanchannel.content.ClanMemberComporator;
import com.osroyale.content.clanchannel.content.ClanTask;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * The clan channel details.
 *
 * @author Daniel.
 */
public class ClanDetails {

    /** The clan channel instance. */
    private final ClanChannel channel;

    /** The clan channel owner. */
    public String owner;

    public String created;

    public ClanType type;

    public ClanLevel level;

    public ClanTask clanTask;

    public int taskAmount;

    /** The clan channel points. */
    public int points;

    /** The clan channel total experience. */
    public double experience;

    /** The date the clan channel was created. */
    public String established;

    /** The clan achievements. */
    public HashMap<ClanAchievement, Integer> achievements = new HashMap<ClanAchievement, Integer>(ClanAchievement.values().length) {
        private static final long serialVersionUID = 1842952445111093360L;

        {
            for (final ClanAchievement achievement : ClanAchievement.values())
                put(achievement, 0);
        }
    };

    /** Constructs a new <code>ClanDetails</code>. */
    ClanDetails(ClanChannel channel) {
        this.channel = channel;
    }

    public int getAchievementCompletion(ClanAchievement achievement) {
        int count = 0;
        if (achievements.containsKey(achievement)) {
            count = achievements.get(achievement);
        }
        return count;
    }

    public boolean completedAchievement(ClanAchievement achievement) {
        int progress = getAchievementCompletion(achievement);
        int goal = achievement.amount;
        return progress == goal;
    }

    /** Gets the average total level of the clan. */
    public int getAverageTotal() {
        return 0;
//        int total = 0;
//        Iterator<ClanMember> iterator = channel.iterator();
//        while (iterator.hasNext()) {
//            ClanMember member = iterator.next();
//            total += member.totalLevel;
//        }
//        return total / channel.size();
    }

    public int getClanRank(ClanMember member) {
        List<ClanMember> members = new LinkedList<>();
        members.addAll(channel.getMembers());
        members.sort(ClanMemberComporator.NAME);

        int index = 0;
        for (ClanMember next : members) {
            index++;
            if (next.equals(member)) {
                return index;
            }
        }
        return index;
    }

}