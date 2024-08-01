package com.osroyale.game.world.entity.combat.effect;

import com.google.common.collect.ImmutableSet;
import com.osroyale.game.world.entity.combat.effect.impl.*;

/**
 * The enumerated type whose values represent the collection of different combat
 * effect types.
 *
 * @author lare96 <http://github.org/lare96>
 */
public enum CombatEffectType {
    POISON(new CombatPoisonEffect()),
    VENOM(new CombatVenomEffect()),
    SKULL(new CombatSkullEffect()),
    TELEBLOCK(new CombatTeleblockEffect()),
    ANTIFIRE_POTION(new CombatAntifireEffect(AntifireDetails.AntifireType.REGULAR)),
    SUPER_ANTIFIRE_POTION(new CombatAntifireEffect(AntifireDetails.AntifireType.SUPER));
    public static final ImmutableSet<CombatEffectType> TYPES = ImmutableSet.copyOf(values());

    static {
        TYPES.forEach($it -> CombatEffect.EFFECTS.put($it, $it.getEffect()));
    }

    /** The combat effect that contains the data for this type. */
    private final CombatEffect effect;

    /** Creates a new {@link CombatEffectType}. */
    CombatEffectType(CombatEffect effect) {
        this.effect = effect;
    }

    /** Gets the combat effect that contains the data for this type. */
    public final CombatEffect getEffect() {
        return effect;
    }
}
