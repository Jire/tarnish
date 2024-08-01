package com.osroyale.game.world.entity.combat.strategy.player.special.melee;

import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.world.entity.combat.hit.CombatHit;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.strategy.player.PlayerMeleeStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;

import static com.osroyale.game.world.entity.skill.Skill.HITPOINTS;
import static com.osroyale.game.world.entity.skill.Skill.PRAYER;

/**
 * Handles the saradomin sword weapon special attack.
 *
 * @author Daniel
 */
public class SaradominGodsword extends PlayerMeleeStrategy {

    //SGS(normal): 7640, SGS(OR): 7641
    private static final Animation ANIMATION = new Animation(7640, UpdatePriority.HIGH);
    private static final Graphic GRAPHIC = new Graphic(1209, UpdatePriority.HIGH);

    private static final SaradominGodsword INSTANCE = new SaradominGodsword();

    @Override
    public void attack(Player attacker, Mob defender, Hit hit) {
        super.attack(attacker, defender, hit);
        attacker.graphic(GRAPHIC);
        int heal = hit.getDamage() / 2;
        int prayerRestore = hit.getDamage() / 4;

        Skill skill = attacker.skills.get(HITPOINTS);
        if (skill.getLevel() < skill.getMaxLevel()) {
            int level = skill.getLevel() + heal;
            if (skill.getLevel() + heal > skill.getMaxLevel())
                level = skill.getMaxLevel();
            attacker.skills.setLevel(HITPOINTS, level);
            System.out.println("here");
            System.out.println("healed " + heal + " hp");
        }

        skill = attacker.skills.get(PRAYER);
        if (skill.getLevel() < skill.getMaxLevel()) {
            int level = skill.getLevel() + prayerRestore;
            if (skill.getLevel() + prayerRestore > skill.getMaxLevel())
                level = skill.getMaxLevel();
            attacker.skills.setLevel(PRAYER, level);
            System.out.println("here");
            System.out.println("restored " + prayerRestore + " prayer");
        }
    }

    @Override
    public void hit(Player attacker, Mob defender, Hit hit) {
        super.hit(attacker, defender, hit);
    }

    @Override
    public CombatHit[] getHits(Player attacker, Mob defender) {
        return new CombatHit[]{nextMeleeHit(attacker, defender)};
    }

    @Override
    public Animation getAttackAnimation(Player attacker, Mob defender) {
        return ANIMATION;
    }

    @Override
    public int modifyAccuracy(Player attacker, Mob defender, int roll) {
        return roll * 2;
    }

    @Override
    public int modifyDamage(Player attacker, Mob defender, int damage) {
        return damage * 11 / 10;
    }

    public static SaradominGodsword get() {
        return INSTANCE;
    }

}