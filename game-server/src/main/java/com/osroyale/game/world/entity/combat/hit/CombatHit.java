package com.osroyale.game.world.entity.combat.hit;

import java.util.function.Function;

/**
 * A wrapper for a {@link Hit} object, adding additional variables for hit and
 * hitsplat delays.
 *
 * @author Michael | Chex
 */
public final class CombatHit extends Hit {

    /** The hit delay. */
    private final int hitDelay;

    /** The hitsplat delay. */
    private final int hitsplatDelay;

    private final boolean multipleHitsAllowed;

    /** Constructs a new {@link CombatHit} object. */
    public CombatHit(Hit hit, int hitDelay, int hitsplatDelay) {
        super(hit.getDamage(), hit.getHitsplat(), hit.getHitIcon(), hit.isAccurate());
        this.hitDelay = hitDelay;
        this.hitsplatDelay = hitsplatDelay;
        this.multipleHitsAllowed = false;
    }

    public CombatHit(Hit[] hits, int hitDelay, int hitsplatDelay) {
        super(hits);
        this.hitDelay = hitDelay;
        this.hitsplatDelay = hitsplatDelay;
        this.multipleHitsAllowed = true;
    }

    /**
     * Copies and modifies this combat hit.
     *
     * @param modifier the damage modification
     * @return a copy of this combat hit with the damage modifier applied
     */
    public CombatHit copyAndModify(Function<Integer, Integer> modifier) {
        CombatHit next = new CombatHit(this, hitDelay, hitsplatDelay);
        next.modifyDamage(modifier);
        return next;
    }

    /** @return the hit delay. */
    public int getHitDelay() {
        return hitDelay;
    }

    /** @return the hit delay. */
    public int getHitsplatDelay() {
        return hitsplatDelay;
    }

    public boolean getMultipleHitsAllowed() {
        return multipleHitsAllowed;
    }
}
