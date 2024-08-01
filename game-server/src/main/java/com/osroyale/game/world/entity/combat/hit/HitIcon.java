package com.osroyale.game.world.entity.combat.hit;

/**
 * The enumerated type whose elements represent the hit icon of a {@link Hit}.
 *
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public enum HitIcon {

    /** Represents no hit icon at all. */
    NONE(255),

    /** Represents the melee sword hit icon. */
    MELEE(0),

    /** Represents the ranged bow hit icon. */
    RANGED(1),

    /** Represents the magic hat hit icon. */
    MAGIC(2),

    /** Represents the leech hit icon. */
    DEFLECT(3),

    /** Represents the canon hit icon. */
    CANON(4);

    /** The identification for this hit type. */
    private final int id;

    /**
     * Create a new {@link HitIcon}.
     *
     * @param id the identification for this hit type.
     */
    HitIcon(int id) {
        this.id = id;
    }

    /**
     * Gets the identification for this hit type.
     *
     * @return the identification for this hit type.
     */
    public final int getId() {
        return id;
    }

}