package com.osroyale.content.clanchannel;

/**
 * The enum containing all the rank's data within a clan channel.
 *
 * @author Daniel.
 */
public enum ClanRank {
    MEMBER("Member", -1),
    FRIEND("Friend", 0),
    RECRUIT("Recruit", 1),
    CORPORAL("Corporal", 2),
    SERGEANT("Sergeant", 3),
    LIEUTENANT("Lieutenant", 4),
    CAPTAIN("Captain", 5),
    GENERAL("General", 6),
    LEADER("Leader", 7),
    SYSTEM("System", 8);

    /** The name of the clan rank. */
    public final String name;

    /** The rank index. */
    public final int rank;

    /** Constructs a new <code>ClanRank</code>. */
    ClanRank(String name, int rank) {
        this.name = name;
        this.rank = rank;
    }

    public boolean lessThan(ClanRank other) {
        return rank < other.rank;
    }

    public boolean greaterThanEqual(ClanRank other) {
        return rank >= other.rank;
    }

    public String getName() {
        return this == MEMBER ? "Anyone" : name;
    }

    public String getString() {
        return this == MEMBER ? "" : ("<clan=" + rank + ">");
    }
}