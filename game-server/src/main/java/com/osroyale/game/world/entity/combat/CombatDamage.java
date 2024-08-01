package com.osroyale.game.world.entity.combat;

import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.util.Stopwatch;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * A fs of players who have inflicted damage on another player in a combat
 * session.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class CombatDamage {

    /**
     * The damages of players who have inflicted damage.
     */
    private final Map<Mob, DamageCounter> attackers = new HashMap<>();

    public Hit lastHit;

    /**
     * Registers damage in the backing collection for {@code character}. This
     * method has no effect if the character isn't a {@code PLAYER} or if
     * {@code amount} is below {@code 0}.
     *
     * @param character the character to register damage for.
     * @param hit    the hit to register.
     */
    public void add(Mob character, Hit hit) {
        if (hit.getDamage() > 0) {
            DamageCounter counter = attackers.putIfAbsent(character, new DamageCounter(hit.getDamage()));
            if (counter != null)
                counter.incrementAmount(hit.getDamage());
            lastHit = hit;
        }
    }

    /**
     * Determines which player in the backing collection has inflicted the most
     * damage.
     *
     * @return the player who has inflicted the most damage, or an empty
     * optional if there are no entries.
     */
    public Optional<Npc> getNpcKiller() {
        int amount = 0;
        Npc killer = null;
        for (Map.Entry<Mob, DamageCounter> entry : attackers.entrySet()) {
            DamageCounter counter = entry.getValue();
            Mob entity = entry.getKey();

            if (!entity.isNpc() || entity.isDead() || !entity.isValid() || counter.isTimeout())
                continue;
            if (counter.getAmount() > amount) {
                amount = counter.getAmount();
                killer = entity.getNpc();
            }
        }
        return Optional.ofNullable(killer);
    }

    /**
     * Determines which player in the backing collection has inflicted the most
     * damage.
     *
     * @return the player who has inflicted the most damage, or an empty
     * optional if there are no entries.
     */
    public Optional<Player> getPlayerKiller() {
        int amount = 0;
        Player killer = null;
        for (Map.Entry<Mob, DamageCounter> entry : attackers.entrySet()) {
            DamageCounter counter = entry.getValue();
            Mob entity = entry.getKey();

            if (!entity.isPlayer() || entity.isDead() || entity.isValid() || counter.isTimeout())
                continue;
            if (counter.getAmount() > amount) {
                amount = counter.getAmount();
                killer = entity.getPlayer();
            }
        }
        return Optional.ofNullable(killer);
    }

    /**
     * Determines which entity in the backing collection has inflicted the most
     * damage.
     *
     * @return the player who has inflicted the most damage, or an empty
     * optional if there are no entries.
     */
    public Optional<Mob> calculateProperKiller() {
        int amount = 0;
        Mob killer = null;
        for (Map.Entry<Mob, DamageCounter> entry : attackers.entrySet()) {
            DamageCounter counter = entry.getValue();
            Mob mob = entry.getKey();

            if (mob.isDead() || !mob.isValid() || counter.isTimeout())
                continue;

            if (attackers.size() > 1 && mob.isPlayer() && PlayerRight.isIronman(mob.getPlayer()))
                continue;

            if (counter.getAmount() > amount) {
                amount = counter.getAmount();
                killer = mob;
            }
        }
        return Optional.ofNullable(killer);
    }

    /**
     * Clears all data from the backing collection.
     */
    public void clear() {
        attackers.clear();
    }

    /**
     * A counter that will track the amount of damage dealt and whether that
     * damaged has timed out or not.
     *
     * @author lare96 <http://github.com/lare96>
     */
    private static final class DamageCounter {

        /**
         * The amount of damage within this counter.
         */
        private int amount;

        /**
         * The stopwatch that will determine when a timeout occurs.
         */
        private final Stopwatch stopwatch = Stopwatch.start();

        /**
         * Creates a new {@link DamageCounter}.
         *
         * @param amount the amount of damage within this counter.
         */
        public DamageCounter(int amount) {
            this.amount = amount;
        }

        /**
         * Gets the amount of damage within this counter.
         *
         * @return the amount of damage.
         */
        public int getAmount() {
            return amount;
        }

        /**
         * Increments the amount of damage within this counter.
         *
         * @param amount the amount to increment by.
         */
        public void incrementAmount(int amount) {
            if (this.isTimeout()) {
                this.amount = 0;
            }
            this.amount += amount;
            this.stopwatch.reset();
        }

        /**
         * Determines if this counter has timed out or not.
         *
         * @return {@code true} if this counter has timed out, {@code false}
         * otherwise.
         */
        public boolean isTimeout() {
            return stopwatch.elapsed(CombatConstants.DAMAGE_CACHE_TIMEOUT, TimeUnit.SECONDS);
        }
    }
}
