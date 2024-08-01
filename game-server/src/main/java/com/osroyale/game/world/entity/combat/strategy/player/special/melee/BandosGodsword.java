package com.osroyale.game.world.entity.combat.strategy.player.special.melee;

import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.strategy.player.PlayerMeleeStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;

/**
 * @author Michael | Chex
 */
public class BandosGodsword extends PlayerMeleeStrategy {

    //BGS(normal): 7642, BGS(OR): 7643
    private static final Animation ANIMATION = new Animation(7642, UpdatePriority.HIGH);
    private static final Graphic GRAPHIC = new Graphic(1212);

    private static final BandosGodsword INSTANCE = new BandosGodsword();

    private BandosGodsword() { }

    @Override
    public void start(Player attacker, Mob defender, Hit[] hits) {
        super.start(attacker, defender, hits);
    }

    @Override
    public void attack(Player attacker, Mob defender, Hit h) {
        super.attack(attacker, defender, h);
        attacker.graphic(GRAPHIC);

        if (h.isAccurate()) {
            int damage = h.getDamage();
            int[] skillOrder = {Skill.DEFENCE, Skill.STRENGTH, Skill.ATTACK, Skill.PRAYER, Skill.MAGIC, Skill.RANGED};

            for (int s : skillOrder) {

                //Getting the skill value to decrease.
                int removeFromSkill;

                if (h.getDamage() > defender.skills.getLevel(s)) {
                    int difference = damage - defender.skills.getLevel(s);
                    removeFromSkill = damage - difference;
                } else
                    removeFromSkill = damage;

                //Decreasing the skill.
                defender.skills.get(s).removeLevel(removeFromSkill);
                defender.skills.refresh(s);

                //Changing the damage left to decrease.
                damage -= removeFromSkill;
                String skill = Skill.getName(s);

                if (defender.isPlayer()) {
                    defender.getPlayer().message("Your " + skill + " level has been drained.");
                }
            }
        }
    }

    @Override
    public Animation getAttackAnimation(Player attacker, Mob defender) {
        return ANIMATION;
    }

    @Override
    public int modifyAccuracy(Player attacker, Mob defender, int roll) {
        return 2 * roll;
    }

    @Override
    public int modifyDamage(Player attacker, Mob defender, int damage) {
        return (int) (damage * 1.21);
    }

    public static BandosGodsword get() {
        return INSTANCE;
    }

}