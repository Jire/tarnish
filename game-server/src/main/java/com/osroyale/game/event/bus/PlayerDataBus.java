package com.osroyale.game.event.bus;

import com.osroyale.game.event.Event;
import com.osroyale.game.event.listener.PlayerEventListener;
import com.osroyale.game.world.entity.mob.player.Player;

import java.util.HashSet;
import java.util.Set;

public final class PlayerDataBus {

    private static final PlayerDataBus INSTANCE = new PlayerDataBus();

    private PlayerDataBus() {

    }

    public static PlayerDataBus getInstance() {
        return INSTANCE;
    }

    private static final Set<PlayerEventListener> chain = new HashSet<>();

    public void subscribe(PlayerEventListener listener) {
        chain.add(listener);
    }

    public void unsubscribe(PlayerEventListener listener) {
        chain.remove(listener);
    }

    public boolean publish(Player player, Event event) {
        for (PlayerEventListener listener : chain) {
            if (listener.accept(player, event)) {
                return true;
            }
        }
        return false;
    }

}
