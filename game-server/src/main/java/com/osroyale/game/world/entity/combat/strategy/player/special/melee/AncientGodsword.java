package com.osroyale.game.world.entity.combat.strategy.player.special.melee;

import com.osroyale.content.achievement.AchievementHandler;
import com.osroyale.content.achievement.AchievementKey;
import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.strategy.player.PlayerMeleeStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.player.Player;

/** @author Michael | Chex */
public class AncientGodsword extends PlayerMeleeStrategy {

    //AGS(normal): 7644, AGS(OR): 7645
    private static final Animation ANIMATION = new Animation(9171, UpdatePriority.HIGH);
    private static final Graphic GRAPHIC = new Graphic(1996);

    private static final AncientGodsword INSTANCE = new AncientGodsword();

    private AncientGodsword() {
    }

    @Override

    public void start(Player attacker, Mob defender, Hit[] hits) {
        super.start(attacker, defender, hits);
        attacker.animate(ANIMATION, true);
        attacker.graphic(GRAPHIC);
    }

    @Override
    public void hitsplat(Player attacker, Mob defender, Hit hit) {
        super.hitsplat(attacker, defender, hit);
        if (defender.isPlayer() && hit.getDamage() >= 80) {
            AchievementHandler.activate(attacker, AchievementKey.AGS_MAX);
        }
    }

    @Override
    public void onKill(Player attacker, Mob defender, Hit hit) {
        if (defender.isPlayer()) {
            AchievementHandler.activate(attacker, AchievementKey.AGS_SPEC);
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
        return (int) (damage * 1.375);
    }

    public static AncientGodsword get() {
        return INSTANCE;
    }

}