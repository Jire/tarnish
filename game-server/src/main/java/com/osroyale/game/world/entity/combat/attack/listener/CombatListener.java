package com.osroyale.game.world.entity.combat.attack.listener;

import com.osroyale.game.world.entity.combat.CombatType;
import com.osroyale.game.world.entity.combat.FormulaModifier;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.mob.Mob;

/**
 * A combat attack is used to describe what the attacking and defending mobs
 * should do in each stage of combat.
 *
 * @author Michael | Chex
 */
public interface CombatListener<T extends Mob> extends FormulaModifier<T> {

    boolean withinDistance(T attacker, Mob defender);

    /**
     * Checks if the attacker can attack the defender.
     *
     * @param attacker the attacking mob
     * @param defender the defending mob
     */
    boolean canAttack(T attacker, Mob defender);

    /**
     * Checks if the attacker can attack the defender.
     *
     * @param attacker the attacking mob
     * @param defender the defending mob
     */
    boolean canOtherAttack(Mob attacker, T defender);

    /**
     * Called when the strategy initializes.
     *
     * @param attacker the attacking mob
     * @param defender the defending mob
     */
    default void init(T attacker, Mob defender) {}

    /**
     * Called when the strategy starts.
     *
     * @param attacker the attacking mob
     * @param defender the defending mob
     */
    void start(T attacker, Mob defender, Hit[] hits);

    /**
     * Called when the attacking hit executes on the defender.
     *
     * @param attacker the attacking mob
     * @param defender the defending mob
     * @param hit      the hit to apply
     */
    void attack(T attacker, Mob defender, Hit hit);

    /**
     * Called when the attacking mob performs an attack on the defender.
     *
     * @param attacker the attacking mob
     * @param defender the defending mob
     * @param hit      the hit to apply
     */
    void hit(T attacker, Mob defender, Hit hit);

    /**
     * Called when the defending mob blocks a hit from the attacker.
     *
     * @param attacker   the attacking mob
     * @param defender   the defending mob
     * @param hit        the hit being applied
     * @param combatType the combat type for this hit
     */
    void block(Mob attacker, T defender, Hit hit, CombatType combatType);

    /**
     * Called right before the defending mob dies.
     *
     * @param attacker the attacking mob
     * @param defender the defending mob
     * @param hit      the hit that killed the defender
     */
    void preDeath(Mob attacker, T defender, Hit hit);

    /**
     * Called when the defending mob dies.
     *
     * @param attacker the attacking mob
     * @param defender the defending mob
     * @param hit      the hit that killed the defender
     */
    void onDeath(Mob attacker, T defender, Hit hit);

    /**
     * Called before attacker killed defender.
     *
     * @param attacker the attacking mob
     * @param defender the defending mob
     * @param hit      the hit that killed the defender
     */
    void preKill(Mob attacker, Mob defender, Hit hit);


    /**
     * Called when attacker killed defender.
     *
     * @param attacker the attacking mob
     * @param defender the defending mob
     * @param hit      the hit that killed the defender
     */
    void onKill(T attacker, Mob defender, Hit hit);

    /**
     * Called when attacker does the hitsplat
     *
     * @param attacker the attacking mob
     * @param defender the defending mob
     * @param hit      the hit that killed the defender
     */
    void hitsplat(T attacker, Mob defender, Hit hit);

    /**
     * Called when the defending mob finishes their strategy's attack.
     *
     * @param attacker the attacking mob
     * @param defender the defending mob
     */
    void finishOutgoing(T attacker, Mob defender);

    /**
     * Called when the attacking mob finishes their strategy's attack.
     *
     * @param attacker the attacking mob
     * @param defender the defending mob
     */
    void finishIncoming(Mob attacker, T defender);

    void onDamage(T defender, Hit hit);

    default void performChecks(T attacker, Mob defender) {

    }

}

