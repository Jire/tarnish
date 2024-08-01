package com.osroyale.game.world.entity.mob.player.exchange;

import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.util.MessageColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 25-1-2017.
 */
public final class ExchangeSessionManager {

    /**
     * The players this player has requested.
     */
    public final List<Player> requested_players = new ArrayList<>();

    /**
     * The player this manager is for.
     */
    public final Player player;

    /**
     * Constructs a new {@link ExchangeSessionManager}.
     * @param player    the player this manager is for.
     */
    public ExchangeSessionManager(Player player) {
        this.player = player;
    }

    /**
     * Attempts to request the specified {@code session}.
     * @param session   the session to request.
     */
    public boolean request(ExchangeSession session) {
        return session.request();
    }

    /**
     * Attempts to deposit an item to an exchange session.
     * @param slot      the slot this item was added from.
     * @param amount    the amount that was added.
     */
    public boolean deposit(int slot, int amount) {
        ExchangeSession session = ExchangeSession.getSession(player).orElse(null);

        if(session == null) {
            return false;
        }

        return session.add(player, slot, amount);
    }

    /**
     * Attempts to withdraw an item from the exchange session.
     * @param slot      the slot this item was removed from.
     * @param amount    the amount that was removed.
     */
    public boolean withdraw(int slot, int amount) {
        ExchangeSession session = ExchangeSession.getSession(player).orElse(null);

        if(session == null) {
            return false;
        }

        return session.remove(player, slot, amount);
    }


    /**
     * Resets all the session for the player dependant on the {@code type}.
     */
    public void reset(ExchangeSessionType type) {
        Optional<ExchangeSession> session = ExchangeSession.getSession(player, type);

        if(!session.isPresent()) {
            return;
        }

        session.get().forEach(p -> {
            session.get().finalize(ExchangeCompletionType.RESTORE);
            resetRequests();
            p.interfaceManager.close();
        });

        Player other = session.get().getOther(player);
        other.send(new SendMessage("The other player has declined.", MessageColor.RED));
        ExchangeSession.SESSIONS.remove(session.get());
    }

    /**
     * Resets all the sessions the player is in regardless of the session state.
     */
    public boolean reset() {
        Optional<ExchangeSession> session = ExchangeSession.getSession(player);

        if(!session.isPresent()) {
            return false;
        }

        reset(session.get().type);
        return true;
    }

    /**
     * Clears the list of requested players.
     */
    public void resetRequests() {
        player.exchangeSession.requested_players.clear();
    }
}
