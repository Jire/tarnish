package com.osroyale;


import static com.osroyale.Configuration.*;

/**
 * @author Daniel
 * @author Jire
 */
public enum Connection {

    /**
     * The economy (main) world.
     */
    ECONOMY("ECO",
            LIVE_GAME_ADDRESS, LIVE_GAME_PORT,
            LIVE_CACHE_ADDRESS, LIVE_CACHE_PORT
    ),

    /**
     * The development world - used by developers.
     */
    DEV("DEV",
            DEV_GAME_ADDRESS, DEV_GAME_PORT,
            DEV_CACHE_ADDRESS, DEV_CACHE_PORT
    ),

    ;

    /**
     * The connection name.
     */
    private final String displayName;

    /**
     * The connection IP address.
     */
    private final String gameAddress;

    private final int gamePort;

    private final String cacheAddress;
    private final int cachePort;


    /**
     * Constructs a new <code>Connection</code>.
     */
    Connection(final String displayName,
               final String gameAddress, final int gamePort,
               final String cacheAddress, final int cachePort) {
        this.displayName = displayName;

        this.gameAddress = gameAddress;
        this.gamePort = gamePort;

        this.cacheAddress = cacheAddress;
        this.cachePort = cachePort;
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

    public String getCacheAddress() {
        return cacheAddress;
    }

    public int getCachePort() {
        return cachePort;
    }

}
