package com.osroyale.game.world.entity.combat.strategy.player.special.range;

import com.osroyale.game.Animation;
import com.osroyale.game.Projectile;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.world.entity.combat.accuracy.RangeAccuracy;
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
public class ZaryteCrossbow extends PlayerRangedStrategy {

    private static final ZaryteCrossbow INSTANCE = new ZaryteCrossbow();
    private static final Animation ANIMATION = new Animation(9166, UpdatePriority.HIGH);
    private static Projectile PROJECTILE;

    static {
        try {
            setProjectiles(CombatProjectile.getDefinition("zaryte special"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ZaryteCrossbow() {
    }

    @Override
    protected void sendStuff(Player attacker, Mob defender) {
        attacker.animate(ANIMATION, true);
        PROJECTILE.send(attacker, defender);
    }

    @Override
    public CombatHit[] getHits(Player attacker, Mob defender) {
        return new CombatHit[]{nextRangedHit(attacker, defender)};
    }


    /*
    Handles x2 accuracy https://oldschool.runescape.wiki/w/Zaryte_crossbow
     */
    @Override
    public int modifyAccuracy(Player attacker, Mob defender, int roll) {
        return (int) (2 * RangeAccuracy.getAttackRoll(attacker));
    }

    private static void setProjectiles(CombatProjectile projectile) {
        if (!projectile.getProjectile().isPresent())
            throw new NullPointerException("No Magic Shortbow projectile found.");
        PROJECTILE = projectile.getProjectile().get();
    }

    public static ZaryteCrossbow get() {
        return INSTANCE;
    }

}