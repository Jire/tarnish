package com.osroyale.game.world.entity.combat.hit;

import java.util.function.Function;

/**
 * A {@code Hit} object holds the damage amount and hitsplat data.
 *
 * @author Michael | Chex
 */
public class Hit {

    /** The damage amount. */
    private int damage;

    /** The hitsplat type. */
    private Hitsplat hitsplat;

    /** The hit icon. */
    private HitIcon hitIcon;

    /** Whether or not this hit is accurate. */
    private boolean accurate;

    private Hit[] multipleHits = null;

    /**
     * Constructs a new {@link Hit} object.
     *
     * @param multipleHits A array of multiple hits
     */
    public Hit(Hit[] multipleHits) {
        this.multipleHits = multipleHits;
        int totalDamage = 0;
        for(Hit hit : multipleHits) totalDamage += hit.damage;
        this.damage = totalDamage;
    }

    /**
     * Constructs a new {@link Hit} object.
     *
     * @param damage   the damage amount
     * @param hitsplat the hitsplat type
     * @param hitIcon  the hit icon
     * @param accurate whether or not this hit is accurate
     */
    public Hit(int damage, Hitsplat hitsplat, HitIcon hitIcon, boolean accurate) {
        this.damage = damage;
        this.hitsplat = hitsplat;
        this.hitIcon = hitIcon;
        this.accurate = accurate;
    }

    /**
     * Constructs a new {@link Hit} object.
     *
     * @param damage   the damage amount
     * @param hitsplat the hitsplat type
     * @param hitIcon  the hit icon
     */
    public Hit(int damage, Hitsplat hitsplat, HitIcon hitIcon) {
        this(damage, hitsplat, hitIcon, damage > 0);
    }

    /**
     * Constructs a new {@link Hit} object.
     *
     * @param damage  the damage amount
     * @param hitIcon the hit icon
     */
    public Hit(int damage, HitIcon hitIcon) {
        this(damage, Hitsplat.NORMAL, hitIcon, damage > 0);
    }

    /**
     * Constructs a new {@link Hit} object.
     *
     * @param damage   the damage amount
     * @param hitsplat the hitsplat type
     */
    public Hit(int damage, Hitsplat hitsplat) {
        this(damage, hitsplat, HitIcon.NONE, damage > 0);
    }

    /**
     * Constructs a new {@link Hit} object.
     *
     * @param damage the damage amount
     */
    public Hit(int damage) {
        this(damage, Hitsplat.NORMAL, HitIcon.NONE, damage > 0);
    }

    /**
     * Sets the hit damage.
     *
     * @param damage the damage to set
     */
    public void setDamage(int damage) {
        this.damage = damage;
    }

    /**
     * Sets the hit damage with a function. If the damage is less than one, the
     * damage is set to zero and hitsplat setVenom to block.
     *
     * @param modifier the modifier to damage
     */
    public void modifyDamage(Function<Integer, Integer> modifier) {
        damage = modifier.apply(damage);

        if (damage <= 0) {
            damage = 0;
        }
    }

    /**
     * Gets the damage amount.
     *
     * @return the damage amount
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Gets the damage type.
     *
     * @return the damage type
     */
    public Hitsplat getHitsplat() {
        return hitsplat;
    }

    /**
     * Gets the hit icon.
     *
     * @return the hit icon
     */
    public HitIcon getHitIcon() {
        return hitIcon;
    }

    /**
     * Checks if the hit is accurate.
     *
     * @return {@code true} if the hit is accurate
     */
    public boolean isAccurate() {
        return accurate;
    }

    public Hit[] getMultipleHits() { return multipleHits; }

    /**
     * Sets the hit icon.
     *
     * @param hitIcon the hit icon to set
     */
    public void setIcon(HitIcon hitIcon) {
        this.hitIcon = hitIcon;
    }

    public void setHitsplat(Hitsplat hitsplat) {
        this.hitsplat = hitsplat;
    }

    public void setAs(Hit other) {
        this.damage = other.damage;
        this.hitIcon = other.hitIcon;
        this.hitsplat = other.hitsplat;
        this.accurate = other.accurate;
    }

    public void setAccurate(boolean accurate) {
        this.accurate = accurate;
    }
}
