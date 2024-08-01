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
public class DragonThrownaxe extends PlayerRangedStrategy {
    private static final DragonThrownaxe INSTANCE = new DragonThrownaxe();
    private static final Animation ANIMATION = new Animation(7521, UpdatePriority.VERY_HIGH);
    private static Projectile PROJECTILE;

    static {
        try {
            setProjectiles(CombatProjectile.getDefinition("Dragon thrownaxe"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DragonThrownaxe() {}

    @Override
    protected void sendStuff(Player attacker, Mob defender) {
        attacker.animate(ANIMATION, true);
        PROJECTILE.send(attacker, defender);
    }

    @Override
    public CombatHit[] getHits(Player attacker, Mob defender) {
        return new CombatHit[] { nextRangedHit(attacker, defender) };
    }

    @Override
    public int modifyAccuracy(Player attacker, Mob defender, int roll) {
        return roll * 6 / 5;
    }

    private static void setProjectiles(CombatProjectile projectile) {
        if (!projectile.getProjectile().isPresent())
            throw new NullPointerException("No Dragon Thrownaxe projectile found.");
        PROJECTILE = projectile.getProjectile().get();
    }

    public static DragonThrownaxe get() {
        return INSTANCE;
    }

}