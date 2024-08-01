package com.osroyale.game.world.entity.combat.strategy.player.special.melee;

import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.world.entity.combat.attack.FightType;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.strategy.player.PlayerMeleeStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;

/** @author Daniel | Obey */
public class DragonWarhammer extends PlayerMeleeStrategy {
    private static final Animation ANIMATION = new Animation(1378, UpdatePriority.HIGH);
    private static final Graphic GRAPHIC = new Graphic(1292);
    private static final DragonWarhammer INSTANCE = new DragonWarhammer();

    private DragonWarhammer() {
    }

    @Override
    public void start(Player attacker, Mob defender, Hit[] hits) {
        super.start(attacker, defender, hits);
        attacker.graphic(GRAPHIC);
    }

    @Override
    public void attack(Player attacker, Mob defender, Hit h) {
        super.attack(attacker, defender, h);

        if (h.getDamage() > 0) {
            Skill skill = defender.skills.get(Skill.DEFENCE);
            int level = skill.getLevel();
            int reduction = (int) (level * 0.30);

            defender.skills.get(skill.getSkill()).removeLevel(reduction);
            defender.skills.refresh(skill.getSkill());

            attacker.message("You've drained " + defender.getName() + "'s defence level by 30%.");

            if (defender.isPlayer()) {
                defender.getPlayer().message("Your defence level has been drained by 30%.");
            }
        }
    }

    @Override
    public int getAttackDelay(Player attacker, Mob defender, FightType fightType) {
        return 4;
    }

    @Override
    public Animation getAttackAnimation(Player attacker, Mob defender) {
        return ANIMATION;
    }

    @Override
    public int modifyDamage(Player attacker, Mob defender, int damage) {
        return damage * 3 / 2;
    }

    public static DragonWarhammer get() {
        return INSTANCE;
    }

}