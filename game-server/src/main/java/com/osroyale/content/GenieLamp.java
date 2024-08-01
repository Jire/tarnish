package com.osroyale.content;

import com.osroyale.game.world.entity.skill.Skill;

import java.util.Arrays;
import java.util.Optional;

/**
 * Class that handles genie lamp
 *
 * @author Daniel
 */
public enum GenieLamp {
    ATTACK(2812, Skill.ATTACK),
    STRENGTH(2813, Skill.STRENGTH),
    RANGE(2814, Skill.RANGED),
    MAGIC(2815, Skill.MAGIC),
    DEFENCE(2816, Skill.DEFENCE),
    HITPOINTS(2817, Skill.HITPOINTS),
    PRAYER(2818, Skill.PRAYER),

    AGILITY(2819, Skill.AGILITY),
    HERBLORE(2820, Skill.HERBLORE),
    THIEVING(2821, Skill.THIEVING),
    CRAFTING(2822, Skill.CRAFTING),
    RUNECRAFTING(2823, Skill.RUNECRAFTING),
    SLAYER(12034, Skill.SLAYER),
    FARMING(13914, Skill.FARMING),

    MINING(2824, Skill.MINING),
    SMITHING(2825, Skill.SMITHING),
    FISHING(2826, Skill.FISHING),
    COOKING(2827, Skill.COOKING),
    FIREMAKING(2828, Skill.FIREMAKING),
    WOODCUTTING(2829, Skill.WOODCUTTING),
    FLETCHING(2830, Skill.FLETCHING),

    HUNTER(28149, Skill.HUNTER),
    CONSTRUCTION(28151, Skill.CONSTRUCTION);

    private final int buttonId;
    private final int skillId;

    GenieLamp(int buttonId, int skillId) {
        this.buttonId = buttonId;
        this.skillId = skillId;
    }

    public int getButton() {
        return buttonId;
    }

    public int getSkill() {
        return skillId;
    }

    public static Optional<GenieLamp> forButton(int button) {
        return Arrays.stream(values()).filter(lamp -> lamp.buttonId == button).findFirst();
    }
}

