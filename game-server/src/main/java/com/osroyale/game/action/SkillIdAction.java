package com.osroyale.game.action;

import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.skill.Skill;

/**
 * @author Jire
 */
public abstract class SkillIdAction<T extends Mob> extends Action<T> {

    private final int skillId;

    public SkillIdAction(T mob, int delay, boolean instant, int skillId) {
        super(mob, delay, instant);
        this.skillId = skillId;
    }

    public SkillIdAction(T mob, int delay, int skillId) {
        super(mob, delay);
        this.skillId = skillId;
    }

    public int getSkillId() {
        return skillId;
    }

    @Override
    public void beforeSchedule() {
        Skill skill = getMob().skills.get(getSkillId());

        /* Mark as cancelled so it doesn't run now */
        if (skill.isDoingSkill()) {
            setRunning(false);
            return;
        }

        skill.setDoingSkill(true);
    }

}
