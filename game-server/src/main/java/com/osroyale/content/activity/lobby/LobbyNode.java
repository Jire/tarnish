package com.osroyale.content.activity.lobby;

import com.osroyale.content.activity.Activity;
import com.osroyale.content.activity.GroupActivity;
import com.osroyale.game.world.entity.mob.player.Player;

public abstract class LobbyNode extends GroupActivity {
    protected final LobbyManager manager;
    boolean lobby;

    protected LobbyNode(LobbyManager manager) {
        super(manager.getLobbyCooldown(), manager.getPlayerCapacity());
        this.manager = manager;
    }

    @Override
    protected final void start() {
        if (manager.canStart(this)) {
            super.start();
            onStart();
            lobby = false;
            cooldown(manager.getGameCooldown());
        }
    }

    protected void onStart() {
        /* do nothing by default */
    }

    protected abstract Activity createActivity(Player player);

    protected boolean finished() {
        return getTicks() == FINISH || getActiveSize() == 0;
    }

    protected boolean contains(Player player) {
        return activities.containsKey(player);
    }

    public boolean inLobby() {
        return lobby;
    }

}
