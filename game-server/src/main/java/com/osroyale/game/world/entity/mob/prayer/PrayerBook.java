package com.osroyale.game.world.entity.mob.prayer;

import java.util.*;
import java.util.function.Consumer;

/**
 * A {@code PrayerBook} which stores prayers. Prayers in a {@code PrayerBook}
 * can be activated and deactivated.
 *
 * @author Michael | Chex
 */
public class PrayerBook {

    /** The set of prayers. */
    private final Set<Prayer> active = EnumSet.noneOf(Prayer.class);

    /** The method of sending the activated prayer config id to the client. */
    private transient Consumer<Prayer> sendActivated;

    /** The method of sending the deactivated prayer config id to the client. */
    private transient Consumer<Prayer> sendDeactivated;

    /** The method of sending the overhead prayer to the client. */
    private transient Consumer<Prayer> sendOverhead;

    /** The method of removing the overhead prayer from the client. */
    private transient Consumer<Prayer> sendNoOverhead;

    /** The drain counter. */
    public transient int drainCounter;

    /**
     * Checks if all given prayers are active.
     *
     * @param prayers The prayers to check.
     * @return {@code True} if the prayer is active.
     */
    public boolean isActive(Prayer... prayers) {
        for (Prayer prayer : prayers)
            if (!active.contains(prayer)) return false;
        return true;
    }

    public boolean hasOverhead() {
        for (Prayer prayer : Prayer.OVERHEAD) {
            if (isActive(prayer)) {
                return true;
            }
        }
        return false;
    }

    public void deactivateOverhead() {
        for (Prayer prayer : Prayer.OVERHEAD) {
            if (isActive(prayer)) {
                deactivate(prayer);
            }
        }
    }

    /**
     * Checks if any given prayers are active.
     *
     * @param prayers The prayers to check.
     * @return {@code True} if any prayer is active.
     */
    public boolean anyActive(Prayer... prayers) {
        for (Prayer prayer : prayers)
            if (active.contains(prayer)) return true;
        return false;
    }

    /**
     * Checks if none of the given prayers are active.
     *
     * @param prayers the prayers to check
     * @return {@code true} if none of the given prayers are active
     */
    public boolean noneActive(Prayer... prayers) {
        for (Prayer prayer : prayers)
            if (active.contains(prayer)) return false;
        return true;
    }

    /**
     * Activates only the prayers provided. Any currently active prayers will be
     * deactivated. If all the prayers are already active, then they will be
     * deactivated, along with any other active prayer.
     *
     * @param prayers The prayers to toggle.
     */
    private void onlyOrNone(Prayer... prayers) {
        if (isActive(prayers)) reset();
        else only(prayers);
    }

    /**
     * Activates only the prayers provided. Any currently active prayers will be
     * deactivated.
     *
     * @param prayers The prayers to toggle.
     */
    public void only(Prayer... prayers) {
        List<Prayer> list = Arrays.asList(prayers);
        Set<Prayer> activate = new HashSet<>(list);
        Set<Prayer> deactivate = new HashSet<>(active);
        deactivate.removeAll(activate);
        mutate(activate, deactivate);
    }

    /**
     * Sends the activate prayer packet to the client. Head icons will also be
     * activated.
     *
     * @param prayers An array of prayers to activate.
     */
    private void activate(Prayer... prayers) {
        for (Prayer prayer : prayers) {
            if (!active.add(prayer) || sendActivated == null) continue;
            sendActivated.accept(prayer);
            if (sendOverhead != null && prayer.is(Prayer.Type.OVERHEAD)) {
                sendOverhead.accept(prayer);
            }
        }
    }

    /**
     * Sends the dectivate prayer packet to the client. Head icons will also be
     * deactivated.
     *
     * @param prayers An array of prayers to deactivate.
     */
    public void deactivate(Prayer... prayers) {
        for (Prayer prayer : prayers) {
            if (!active.remove(prayer) || sendDeactivated == null) continue;
            sendDeactivated.accept(prayer);
            if (sendNoOverhead != null && prayer.is(Prayer.Type.OVERHEAD)) {
                sendNoOverhead.accept(prayer);
            }
        }
    }

    /**
     * Inverts a prayer's active state. If the prayer is active, then the prayer
     * will be deactivated. If the prayer is inactive, then the prayer will be
     * activated. Activating some prayers will cause others to deactivate based
     * on their {@linkplain Prayer.Type}.
     *
     * @param prayers A list of prayers to toggle.
     */
    public void toggle(Prayer... prayers) {
        Set<Prayer> activate = new HashSet<>();
        Set<Prayer> deactivate = new HashSet<>();
        for (Prayer prayer : prayers) {
            if (deactivate.contains(prayer)) continue;
            if (!active.contains(prayer)) {
                deactivate.addAll(prayer.toDeactivate());
                activate.add(prayer);
            } else deactivate.add(prayer);
        }
        deactivate.removeAll(activate);
        mutate(activate, deactivate);
    }

    /**
     * Sets all prayers active from another prayer book.
     *
     * @param book The other book.
     */
    public void setAs(PrayerBook book) {
        onlyOrNone(book.toArray());
    }

    /**
     * Toggles prayers supplied in the activate and deactivate sets.
     *
     * @param activate   A set of prayers to activate.
     * @param deactivate A set of prayers to deactivate.
     */
    private void mutate(Set<Prayer> activate, Set<Prayer> deactivate) {
        deactivate(deactivate.toArray(new Prayer[deactivate.size()]));
        activate(activate.toArray(new Prayer[activate.size()]));
    }

    /**
     * Calculates the amount of prayer points to drain relative to the given
     * game ticks.
     *
     * @param bonus The current prayer bonus.
     * @return The amount of prayer points to drain.
     */
    public int drainAmount(int bonus) {
        if (active.isEmpty())
            return 0;

        int effect = 0, amount = 0;
        int resistance = 60 + 2 * bonus;

        for (Prayer prayer : active) {
            effect += prayer.getDrainRate();
        }

        drainCounter += effect;

        if (drainCounter > resistance) {
            amount = drainCounter / resistance;
            drainCounter -= amount * resistance;
        }

        return amount;
    }

    public void resetProtection() {
        if (isActive(Prayer.PROTECT_FROM_MAGIC))
            deactivate(Prayer.PROTECT_FROM_MAGIC);
        if (isActive(Prayer.PROTECT_FROM_MELEE))
            deactivate(Prayer.PROTECT_FROM_MELEE);
        if (isActive(Prayer.PROTECT_FROM_RANGE))
            deactivate(Prayer.PROTECT_FROM_RANGE);
    }

    /** Iterates through the active prayers and disables them all. */
    public void reset() {
        deactivate(active.toArray(new Prayer[active.size()]));
    }

    public boolean isActive() {
        return !active.isEmpty();
    }

    public Set<Prayer> getEnabled() {
        return active;
    }

    /** @return an array of active prayers. */
    public Prayer[] toArray() {
        if (active.isEmpty()) return new Prayer[0];
        return active.toArray(new Prayer[active.size()]);
    }

    /**
     * Sets the method of sending the prayer update to the client.
     *
     * @param sendActivated   The consumer that sends the activated prayer
     *                        update to the client.
     * @param sendDeactivated The consumer that sends the deactivated prayer
     *                        update to the client.
     * @param sendOverhead    The consumer that sends the prayer head icon
     *                        update to the client.
     * @param sendNoOverhead  The consumer that removes the prayer head icon
     *                        from the client.
     */
    public void setOnChange(Consumer<Prayer> sendActivated, Consumer<Prayer> sendDeactivated, Consumer<Prayer> sendOverhead, Consumer<Prayer> sendNoOverhead) {
        this.sendActivated = sendActivated;
        this.sendDeactivated = sendDeactivated;
        this.sendOverhead = sendOverhead;
        this.sendNoOverhead = sendNoOverhead;
    }

    @Override
    public String toString() {
        return "PrayerBook[active=" + active.toString() + "]";
    }

}