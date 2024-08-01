package com.osroyale.game.world.entity.combat.strategy.player.special.range;

import com.osroyale.game.Animation;
import com.osroyale.game.Projectile;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.world.entity.combat.hit.CombatHit;
import com.osroyale.game.world.entity.combat.projectile.CombatProjectile;
import com.osroyale.game.world.entity.combat.strategy.player.PlayerRangedStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.player.Player;

/**
 * Handles the magic shortbow weapon special attack.
 *
 * @author Daniel
 * @author Michaael | Chex
 */
public class CrawsBow extends PlayerRangedStrategy {

    private static final CrawsBow INSTANCE = new CrawsBow();
    private static final Animation ANIMATION = new Animation(426, UpdatePriority.HIGH);
    private static Projectile PROJECTILE_1;

    static {
        try {
            setProjectiles(CombatProjectile.getDefinition("Craw's bow"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private CrawsBow() {
    }

    @Override
    protected void sendStuff(Player attacker, Mob defender) {
        attacker.animate(ANIMATION, true);
        PROJECTILE_1.send(attacker, defender);
    }

    @Override
    public CombatHit[] getHits(Player attacker, Mob defender) {
        return new CombatHit[] { nextRangedHit(attacker, defender), nextRangedHit(attacker, defender) };
    }

    @Override
    public int modifyAccuracy(Player attacker, Mob defender, int roll) {
        return roll - roll / 4;
    }

    private static void setProjectiles(CombatProjectile projectile) {
        if (!projectile.getProjectile().isPresent())
            throw new NullPointerException("No Craw's bow projectile found.");
        PROJECTILE_1 = projectile.getProjectile().get();

    }

    public static CrawsBow get() {
        return INSTANCE;
    }

}