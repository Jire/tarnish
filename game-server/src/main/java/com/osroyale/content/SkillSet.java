package com.osroyale.content;

import com.osroyale.game.world.entity.skill.Skill;

import java.util.Arrays;
import java.util.Optional;

/**
 * Handles setting a combat skill.
 *
 * @author Daniel
 */
public enum SkillSet {
    STRENGTH(8657, Skill.STRENGTH),
    DEFENCE(8660, Skill.DEFENCE),
    RANGED(8663, Skill.RANGED),
    PRAYER(8666, Skill.PRAYER),
    MAGIC(8669, Skill.MAGIC),
    HITPOINTS(8655, Skill.HITPOINTS),
    ATTACK(8654, Skill.ATTACK);
;
    private final int button;
    public final int skill;

    SkillSet(int button, int skill) {
        this.button = button;
        this.skill = skill;
    }

    public static Optional<SkillSet> forButton(int button) {
        return Arrays.stream(values()).filter(skillData -> skillData.button == button).findFirst();
    }
}
