package com.osroyale.content.activity.impl.pestcontrol;

import com.osroyale.content.activity.lobby.LobbyManager;
import com.osroyale.content.activity.lobby.LobbyNode;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.position.Position;

/**
 * A {@code PestControlLobby} manages all the {@link PestControlGame} that are
 * active.
 *
 * @author Michael | Chex
 */
public class PestControlLobby extends LobbyManager {

    /** The pest control instance, used for managing all activities. */
    private static final PestControlLobby PEST_CONTROL = new PestControlLobby();

    /** The active game limit. */
    private static final int GAME_CAPACITY = 1;

    /** The player capacity. */
    private static final int PLAYER_CAPACITY = 25;

    /** The lobby cooldown timer in ticks (30 seconds). */
    private static final int LOBBY_COOLDOWN = 50;

    /** The game cooldown timer in ticks (10 minutes). */
    private static final int GAME_COOLDOWN = 1000;

    /** The minimum amount of players required to start. */
    private static final int MINIMUM_PLAYERS = 3;

    /** The position inside the pest control boat. */
    private static final Position INSIDE_BOAT_POSITION = new Position(2661, 2639);

    /** Constructs a new {@link PestControlLobby}. */
    private PestControlLobby() {
        super(GAME_CAPACITY, PLAYER_CAPACITY, MINIMUM_PLAYERS, LOBBY_COOLDOWN, GAME_COOLDOWN);
    }

    /**
     * Joins a {@link Player} into a {@link PestControlGame} lobby.
     *
     * @param player the player who is joining
     */
    public static void joinBoat(Player player) {
        PEST_CONTROL.enter(player);
    }

    /** Sequences all the active {@link PestControlGame}. */
    public static void sequence() {
        PEST_CONTROL.sequenceActive();
    }

    @Override
    protected void onEnter(Player player) {
        player.move(INSIDE_BOAT_POSITION);
        player.message(true, "You have entered the pest control boat.");
    }

    @Override
    protected void onLeave(Player player) {
        player.message(true, "You have left the pest control waiting boat.");
    }

    @Override
    protected void exceededPlayerCapacity(Player player) {
        player.message("This boat doesn't need any more players.");
    }

    @Override
    protected void exceededGameCapacity(Player player) {
        player.message("A game is already active. Please wait for the next boat to depart.");
    }

    @Override
    protected LobbyNode createLobby() {
        return new PestControlGame(PEST_CONTROL);
    }

}
