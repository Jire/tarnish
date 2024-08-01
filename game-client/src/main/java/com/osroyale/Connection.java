package com.osroyale;


import static com.osroyale.Configuration.*;

/**
 * Holds all the connections.
 *
 * @author Daniel
 * @author Jire
 */
public enum Connection {

    /**
     * The economy (main) world.
     */

    ECONOMY("ECO", LIVE_GAME_ADDRESS, LIVE_GAME_PORT, LIVE_CACHE_ADDRESS, LIVE_CACHE_PORT),

    /**
     * The development world - used by developers.
     */
    DEVELOPMENT("DEV", "localhost", 43594, LIVE_CACHE_ADDRESS, LIVE_CACHE_PORT);

    /**
     * The connection name.
     */
    private final String displayName;

    /**
     * The connection IP address.
     */
    private final String gameAddress;

    private final int gamePort;

    private final String updateAddress;
    private final int updatePort;


    /**
     * Constructs a new <code>Connection</code>.
     */
    Connection(String displayName, String gameAddress, int gamePort, String updateAddress, int updatePort) {
        this.displayName = displayName;

        this.gameAddress = gameAddress;
        this.gamePort = gamePort;

        this.updateAddress = updateAddress;
        this.updatePort = updatePort;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getGameAddress() {
        return gameAddress;
    }

    public int getGamePort() {
        return gamePort;
    }

    public String getUpdateAddress() {
        return updateAddress;
    }

    public int getUpdatePort() {
        return updatePort;
    }

}
