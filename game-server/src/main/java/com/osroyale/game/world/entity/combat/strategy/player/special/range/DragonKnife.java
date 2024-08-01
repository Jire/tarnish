package com.osroyale.game.world.entity.combat.strategy.player.special.range;

import com.osroyale.content.achievement.AchievementHandler;
import com.osroyale.content.achievement.AchievementKey;
import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.Projectile;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.world.entity.combat.hit.CombatHit;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.projectile.CombatProjectile;
import com.osroyale.game.world.entity.combat.strategy.player.PlayerRangedStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;

import static com.osroyale.game.world.items.containers.equipment.Equipment.ARROWS_SLOT;

/**
 * @author Andys1814
 */
public final class DragonKnife extends PlayerRangedStrategy {

    private static final DragonKnife INSTANCE = new DragonKnife();

    private static CombatProjectile PROJECTILE;

    static {
        try {
            PROJECTILE = CombatProjectile.getDefinition("Dragon knife special");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DragonKnife() {
    }

    @Override
    protected void sendStuff(Player attacker, Mob defender) {
        super.sendStuff(attacker, defender);

        PROJECTILE.getProjectile().ifPresent(projectile -> projectile.send(attacker, defender));
    }

    @Override
    public void hit(Player attacker, Mob defender, Hit hit) {
        super.hit(attacker, defender, hit);
    }

    @Override
    public CombatHit[] getHits(Player attacker, Mob defender) {
        CombatHit first = nextRangedHit(attacker, defender);
        CombatHit second = nextRangedHit(attacker, defender);
        int minimum = 5;

        if (first.getDamage() < minimum) {
            first.setDamage(minimum);
            first.setAccurate(true);
        }

        if (second.getDamage() < minimum) {
            second.setDamage(minimum);
            second.setAccurate(true);
        }

        return new CombatHit[] { first, second };
    }

    @Override
    public Animation getAttackAnimation(Player attacker, Mob defender) {
        return new Animation(8291);
    }

    public static DragonKnife get() {
        return INSTANCE;
    }

}
