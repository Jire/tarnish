package com.osroyale.content.activity.lobby;

import com.osroyale.game.world.entity.mob.player.Player;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

public abstract class LobbyManager {
    private final Deque<LobbyNode> active = new LinkedList<>();

    private final int playerCapacity;
    private final int gameCapacity;
    private final int lobbyCooldown;
    private final int gameCooldown;
    private final int minimumRequired;

    protected LobbyManager(int gameCapacity, int playerCapacity, int minimumRequired, int lobbyCooldown, int gameCooldown) {
        this.playerCapacity = playerCapacity;
        this.gameCapacity = gameCapacity;
        this.lobbyCooldown = lobbyCooldown;
        this.gameCooldown = gameCooldown;
        this.minimumRequired = minimumRequired;
    }

    public final void enter(Player player) {
        LobbyNode lobby = nextLobby();

        if (lobby == null) {
            return;
        }

        if (lobby.getActiveSize() >= playerCapacity) {
            exceededPlayerCapacity(player);
            return;
        }

        lobby.addActivity(player, lobby.createActivity(player));

        /* reset lobby cooldown since first player just joined */
        if (minimumRequired > -1 && lobby.getActiveSize() == 1) {
            lobby.cooldown(lobbyCooldown);
        }

        onEnter(player);
    }

    public final void leave(Player player) {
        LobbyNode lobby = findNode(player);

        if (lobby == null) {
            return;
        }

        lobby.removeActivity(player);
        onLeave(player);
    }

    public final void sequenceActive() {
        Iterator<LobbyNode> iterator = active.iterator();

        while (iterator.hasNext()) {
            LobbyNode node = iterator.next();

            if (node.finished()) {
                node.finish();
                node.removeAll();
                node.cleanup();
                iterator.remove();
                continue;
            }

            node.sequence();
        }
    }

    protected abstract LobbyNode createLobby();

    protected void onEnter(Player player) {
        /* do nothing by default */
    }

    protected void onLeave(Player player) {
        /* do nothing by default */
    }

    protected void exceededGameCapacity(Player player) {
        /* do nothing by default */
    }

    protected void exceededPlayerCapacity(Player player) {
        /* do nothing by default */
    }

    private LobbyNode findNode(Player player) {
        for (LobbyNode node : active) {
            if (node.contains(player)) {
                return node;
            }
        }
        return null;
    }

    private LobbyNode nextLobby() {
        for (LobbyNode node : active) {
            if (node.inLobby()) {
                return node;
            }
        }

        if (active.size() >= 1 + gameCapacity) {
            return null;
        }

        LobbyNode activity = createLobby();
        active.addFirst(activity);
        activity.lobby = true;
        return activity;
    }

    public boolean canStart(LobbyNode node) {
        if (!node.lobby) {
            return false;
        }

        int activeGames = 0;
        for (LobbyNode activity : active) {
            if (!activity.lobby)
                activeGames++;
        }

        if (activeGames >= gameCapacity) {
            node.cooldown(lobbyCooldown);
            node.forEachActivity((mob, activity) -> {
                if (mob.isPlayer()) {
                    exceededGameCapacity(mob.getPlayer());
                }
            });
            return false;
        }

        if (minimumRequired > node.getActiveSize()) {
            node.cooldown(lobbyCooldown);
            node.groupMessage("This activity requires at least " + minimumRequired + " players to start.");
            return false;
        }

        return true;
    }

    public int getGameCapacity() {
        return gameCapacity;
    }

    public int getPlayerCapacity() {
        return playerCapacity;
    }

    public int getMinimumRequired() {
        return minimumRequired;
    }

    public int getGameCooldown() {
        return gameCooldown;
    }

    public int getLobbyCooldown() {
        return lobbyCooldown;
    }

}
