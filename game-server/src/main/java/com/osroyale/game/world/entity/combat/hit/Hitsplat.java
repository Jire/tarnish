package com.osroyale.game.world.entity.combat.hit;

public enum Hitsplat {

    /** Represents a normal hit type. */
    NORMAL(0),

    /** Represents a poison hit type. */
    POISON(2),

    /** Represents a venom hit type. */
    VENOM(3),

    /** Represents a heal hit type. */
    CRITICAL(4);

    /** The identification for this hit type. */
    private final int id;

    /**
     * Create a new {@link Hitsplat}.
     *
     * @param id the identification for this hit type.
     */
    Hitsplat(int id) {
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
