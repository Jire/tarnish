package com.osroyale.game.world.entity.combat.strategy.player.special.range;

import com.osroyale.content.achievement.AchievementHandler;
import com.osroyale.content.achievement.AchievementKey;
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
 * Handles the magic shortbow weapon special attack.
 *
 * @author Daniel
 * @author Michaael | Chex
 */
public class DarkBow extends PlayerRangedStrategy {
    private static final DarkBow INSTANCE = new DarkBow();

    private static Projectile DARKNESS_PROJECTILE_1;
    private static Projectile DARKNESS_PROJECTILE_2;

    private static Projectile DRAGONS_PROJECTILE_1;
    private static Projectile DRAGONS_PROJECTILE_2;

    static {
        try {
            CombatProjectile darkness = CombatProjectile.getDefinition("Dark Bow Darkness");
            CombatProjectile dragons = CombatProjectile.getDefinition("Dark Bow Dragons");
            setProjectiles(darkness, dragons);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DarkBow() {
    }

    @Override
    protected void sendStuff(Player attacker, Mob defender) {
        super.sendStuff(attacker, defender);

        if (isDragonArrow(attacker.equipment.get(ARROWS_SLOT))) {
            DRAGONS_PROJECTILE_1.send(attacker, defender);
            DRAGONS_PROJECTILE_2.send(attacker, defender);
        } else {
            DARKNESS_PROJECTILE_1.send(attacker, defender);
            DARKNESS_PROJECTILE_2.send(attacker, defender);
        }
    }

    @Override
    public void hit(Player attacker, Mob defender, Hit hit) {
        if (isDragonArrow(attacker.equipment.get(ARROWS_SLOT))) {
            defender.graphic(new Graphic(1100, true, UpdatePriority.HIGH));
        } else {
            defender.graphic(new Graphic(1103, true, UpdatePriority.HIGH));
        }
        super.hit(attacker, defender, hit);
    }

    @Override
    public CombatHit[] getHits(Player attacker, Mob defender) {
        CombatHit first = nextRangedHit(attacker, defender);
        CombatHit second = nextRangedHit(attacker, defender);
        int minimum = isDragonArrow(attacker.equipment.get(ARROWS_SLOT)) ? 8 : 5;

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
    public int modifyDamage(Player attacker, Mob defender, int roll) {
        if (isDragonArrow(attacker.equipment.get(ARROWS_SLOT)))
            return roll * 3 / 2;
        return roll * 13 / 10;
    }

    @Override
    public void onKill(Player attacker, Mob defender, Hit hit) {
        if (defender.isPlayer()) {
            AchievementHandler.activate(attacker, AchievementKey.DARKBOW_SPEC);
        }
    }

    private static void setProjectiles(CombatProjectile darkness, CombatProjectile dragons) {
        if (!darkness.getProjectile().isPresent())
            throw new NullPointerException("No Decent of Darkness projectile found.");

        DARKNESS_PROJECTILE_1 = darkness.getProjectile().get();

        DARKNESS_PROJECTILE_2 = DARKNESS_PROJECTILE_1.copy();
        DARKNESS_PROJECTILE_2.setDuration(15 + DARKNESS_PROJECTILE_2.getDuration());
        DARKNESS_PROJECTILE_2.setCurve(32);

        if (!dragons.getProjectile().isPresent())
            throw new NullPointerException("No Decent of Dragons projectile found.");

        DRAGONS_PROJECTILE_1 = dragons.getProjectile().get().copy();

        DRAGONS_PROJECTILE_2 = DRAGONS_PROJECTILE_1.copy();
        DRAGONS_PROJECTILE_2.setDuration(15 + DRAGONS_PROJECTILE_1.getDuration());
        DRAGONS_PROJECTILE_2.setCurve(32);
    }

    private static boolean isDragonArrow(Item item) {
        if (item != null) {
            int id = item.getId();
            return id == 11212 || id == 11227 || id == 11228 || id == 11229;
        }
        return false;
    }

    public static DarkBow get() {
        return INSTANCE;
    }

}
