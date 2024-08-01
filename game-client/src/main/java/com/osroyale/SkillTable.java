package com.osroyale;

public enum SkillTable {
    ATTACK(21987, 0),
    HITPOINTS(22564, 3),
    STRENGTH(22565, 2),
    DEFENCE(22566, 1),
    RANGED(22567, 4),
    AGILITY(22568, 16),
    HERBLORE(22569, 15),
    THIEVING(22570, 17),
    MINING(22571, 14),
    SMITHING(22572, 13),
    FISHING(22573, 10),
    COOKING(22574, 7),
    PRAYER(22575, 5),
    CRAFTING(22576, 12),
    FIREMAKING(22577, 11),
    RUNECRAFTING(22578, 20),
    SLAYER(22579, 18),
    FARMING(22580, 19),
    CONSTRUCTION(22581, 21),
    HUNTER(22582, 22),
    MAGIC(22583, 6),
    FLETCHING(22584, 9),
    WOODCUTTING(22585, 8)
    ;

    public int id, skillId;

    SkillTable(int id, int skillId) {
        this.id = id;
        this.skillId = skillId;
    }

    public static SkillTable forId(int id) {
        for(SkillTable skillTable : values())
            if(skillTable.id == id)
                return skillTable;
        return null;
    }
}
