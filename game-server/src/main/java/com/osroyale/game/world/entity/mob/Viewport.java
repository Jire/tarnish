package com.osroyale.game.world.entity.mob;

import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.player.Player;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents a viewport in which a {@link Player} can see.
 *
 * @author nshusa
 */
public final class Viewport {

    /** The amount of entities that can be added to this viewport in a single tick **/
    public static final int ADD_THRESHOLD = 15;

    /** The amount of entities that can be visible at all at once 255 players and 255 npcs **/
    public static final int CAPACITY = 100;

    /** The amount of tiles out an entity can see within this viewport **/
    public static final int VIEW_DISTANCE = 15;

    /** The collection of players that are visible in this viewport **/
    private final List<Player> playersInViewport = new LinkedList<>();

    /** The collection of npcs that are visible in this viewport **/
    private final List<Npc> npcsInViewport = new LinkedList<>();

    /** The amount of tiles out this mob can see players  **/
    private final AtomicInteger playerViewingDistance = new AtomicInteger(VIEW_DISTANCE);

    /** The amount of tiles out this mob can see npcs **/
    private final AtomicInteger npcViewingDistance = new AtomicInteger(VIEW_DISTANCE);

    /**
     * The player that this viewport belongs to.
     */
    private final Player player;

    public Viewport(Player player) {
        this.player = player;
    }

    /**
     * Adds a {@link Mob} to this viewport.
     *
     * {@code true} If the {@link Mob} was added, otherwise {@code false}.
     */
    public boolean add(Mob other) {
        if (!canAdd(other)) {
            return false;
        }

        if (other.isPlayer()) {
            playersInViewport.add(other.getPlayer());
            other.updateFlags.add(UpdateFlag.APPEARANCE);
        } else if (other.isNpc()) {
            npcsInViewport.add(other.getNpc());
            other.updateFlags.add(UpdateFlag.FACE_COORDINATE); // doing this for players for some reason crashes the players client
        }
        return true;
    }

    /**
     * Determines if a {@code Mob} can be added to this viewport.
     *
     * {@code true} If this {@link Mob} can be added, otherwise {@code false}.
     */
    private boolean canAdd(Mob other) {
        if (shouldRemove(other)) {
            return false;
        }

        if (other.isPlayer()) {
            Player player = other.getPlayer();
            return !playersInViewport.contains(player);
        } else if (other.isNpc()) {
            Npc npc = other.getNpc();
            return !npcsInViewport.contains(npc);
        }

        return false;
    }

    /**
     * Determines if a {@link Mob} should be removed from this viewport.
     *
     * {@code true} If the {@link Mob} should be removed, otherwise {@code false}.
     */
    public boolean shouldRemove(Mob other) {
        boolean sameEntity = player == other;
        boolean notValid = !other.isValid();
        boolean notVisible = !other.isVisible();
        boolean notSameInstance = player.instance != other.instance;
        boolean notInDistance = !player.getPosition().isWithinDistance(other.getPosition().copy(), other.isPlayer() ? player.viewport.getPlayerViewingDistance() : player.viewport.getNpcViewingDistance());
        boolean positionChanged = other.positionChange || other.teleportRegion;
        boolean regionChanged = other.regionChange && other.teleporting;
        return sameEntity || notValid || notVisible || notSameInstance || notInDistance || regionChanged || positionChanged;
    }

    /**
     * Determines how many tiles out a player can see players or npcs.
     */
    public void calculateViewingDistance() {
        if (playersInViewport.size() >= Viewport.CAPACITY) {
            if (playerViewingDistance.decrementAndGet() < 1) {
                playerViewingDistance.set(1);
            }
        } else {
            if (playerViewingDistance.incrementAndGet() > VIEW_DISTANCE) {
                playerViewingDistance.set(VIEW_DISTANCE);
            }
        }

        if (npcsInViewport.size() >= Viewport.CAPACITY) {
            if (npcViewingDistance.decrementAndGet() < 1) {
                npcViewingDistance.set(1);
            }
        } else {
            if (npcViewingDistance.incrementAndGet() > VIEW_DISTANCE) {
                npcViewingDistance.set(VIEW_DISTANCE);
            }
        }
    }

    /**
     * The collection of players in this viewport
     */
    public List<Player> getPlayersInViewport() {
        return playersInViewport;
    }

    /**
     * The collection of npcs in this viewport
     */
    public List<Npc> getNpcsInViewport() {
        return npcsInViewport;
    }

    /**
     * How many tiles out this player can see other players
     */
    public int getPlayerViewingDistance() {
        return playerViewingDistance.get();
    }

    /**
     * This is how many tiles out this player can see npcs
     */
    public int getNpcViewingDistance() {
        return npcViewingDistance.get();
    }

}
