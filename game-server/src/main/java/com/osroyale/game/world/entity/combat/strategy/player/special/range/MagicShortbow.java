package com.osroyale.game.world.entity.combat.strategy.player.special.range;

import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
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
public class MagicShortbow extends PlayerRangedStrategy {

    private static final MagicShortbow INSTANCE = new MagicShortbow();
    private static final Animation ANIMATION = new Animation(1074, UpdatePriority.HIGH);
    private static Projectile PROJECTILE_1;
    private static Projectile PROJECTILE_2;

    static {
        try {
            setProjectiles(CombatProjectile.getDefinition("Magic Shortbow"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private MagicShortbow() {
    }

    @Override
    protected void sendStuff(Player attacker, Mob defender) {
        attacker.animate(ANIMATION, true);
        attacker.graphic(new Graphic(256, 30, 92));
        PROJECTILE_1.send(attacker, defender);
        PROJECTILE_2.send(attacker, defender);
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
            throw new NullPointerException("No Magic Shortbow projectile found.");
        PROJECTILE_1 = projectile.getProjectile().get();
        PROJECTILE_2 = PROJECTILE_1.copy();
        final int delay = 30 + PROJECTILE_1.getDelay();
        PROJECTILE_2.setDelay(delay);
        PROJECTILE_2.setDuration(20 + PROJECTILE_1.getDuration());
    }

    public static MagicShortbow get() {
        return INSTANCE;
    }

}