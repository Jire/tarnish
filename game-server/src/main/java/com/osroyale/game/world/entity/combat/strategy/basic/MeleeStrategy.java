package com.osroyale.game.world.entity.combat.strategy.basic;

import com.osroyale.game.world.entity.combat.attack.FightType;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.hit.HitIcon;
import com.osroyale.game.world.entity.combat.strategy.CombatStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.pathfinding.path.SimplePathChecker;
import com.osroyale.util.Utility;

/**
 * @author Michael | Chex
 */
public abstract class MeleeStrategy<T extends Mob> extends CombatStrategy<T> {

    @Override
    public boolean withinDistance(T attacker, Mob defender) {
        final FightType fightType = attacker.getCombat().getFightType();
        final int distance = getAttackDistance(attacker, fightType);

        final boolean withinDistance = Utility.inRange(attacker, defender, distance);
        if (defender.id == 1739 || defender.id == 1740 || defender.id == 1741 || defender.id == 1742 || defender.id == 1756) {
            return withinDistance;
        }
        return withinDistance
                && (SimplePathChecker.checkLine(attacker, defender)
                || SimplePathChecker.checkLine(defender, attacker));
    }

    protected static void addCombatExperience(Player player, Hit... hits) {
        int exp = 0;
        for (Hit hit : hits) {
            if (hit.getDamage() <= 0) continue;
            if (hit.getHitIcon() == HitIcon.MELEE) {
                exp += hit.getDamage();
            } else if (hit.getHitIcon() == HitIcon.MAGIC) {
                MagicStrategy.addCombatExperience(player, 0, hit);
            }
        }

        exp *= player.experienceRate;
        player.skills.addExperience(Skill.HITPOINTS, exp / 3);
        switch (player.getCombat().getFightType().getStyle()) {
            case ACCURATE:
                player.skills.addExperience(Skill.ATTACK, exp);
                break;
            case AGGRESSIVE:
                player.skills.addExperience(Skill.STRENGTH, exp);
                break;
            case DEFENSIVE:
                player.skills.addExperience(Skill.DEFENCE, exp);
                break;
            case CONTROLLED:
                exp /= 3;
                player.skills.addExperience(Skill.ATTACK, exp);
                player.skills.addExperience(Skill.STRENGTH, exp);
                player.skills.addExperience(Skill.DEFENCE, exp);
                break;
        }
    }

}
