package com.osroyale.content.activity.impl.battleground;

import com.osroyale.content.activity.lobby.LobbyManager;
import com.osroyale.content.activity.lobby.LobbyNode;

/**
 * Created by Daniel on 2018-03-10.
 */
public class BattlegroundLobby extends LobbyManager {

    protected BattlegroundLobby(int gameCapacity, int playerCapacity, int minimumRequired, int lobbyCooldown, int gameCooldown) {
        super(gameCapacity, playerCapacity, minimumRequired, lobbyCooldown, gameCooldown);
    }

    @Override
    protected LobbyNode createLobby() {
        return null;
    }
}
