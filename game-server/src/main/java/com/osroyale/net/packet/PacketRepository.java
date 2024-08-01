package com.osroyale.net.packet;

import com.google.common.base.Preconditions;
import com.osroyale.game.world.entity.mob.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

/**
 * The repository that stores packets sizes and listeners for how to execute the packets.
 *
 * @author nshusa
 */
public final class PacketRepository {

    private static final Logger logger = LogManager.getLogger();

    /**
     * The number of client packets known.
     */
    private static final int MAX_CLIENT_PACKETS = 257;

    /**
     * The client -> server packet listeners.
     */
    private static final PacketListener[] packetListeners = new PacketListener[MAX_CLIENT_PACKETS];

    /**
     * The array that holds packet types.
     */
    private static final int[] packetTypes = new int[MAX_CLIENT_PACKETS];

    public static void sendToListener(Player player, GamePacket packet) {
        final PacketListener listener = packetListeners[packet.getOpcode()];
        if (listener != null) {
            if (packet.getOpcode() != 0) {
                //logger.info("Packet recv Opcode: {} Player name: {} Size: {}", packet.getOpcode(), player.getUsername(), packet.getSize());
            }
            listener.handlePacket(player, packet);
        }
    }

    /**
     * Registers a listener to listen for client -> server packet events.
     *
     * @param opcode   The packet to intercept.
     * @param listener The listener that will execute this packet.
     */
    public static void registerListener(int opcode, PacketListener listener) {
        Preconditions.checkArgument(opcode >= 0 && opcode < packetListeners.length);
        packetListeners[opcode] = listener;
    }

    /**
     * Looks up the listener for this packet.
     *
     * @param opcode The opcode of the packet
     * @return The result.
     */
    @Nullable
    public static PacketListener lookupListener(final int opcode) {
        final PacketListener[] packetListeners = PacketRepository.packetListeners;
        if (opcode < 0 || opcode >= packetListeners.length) {
            return null;
        }
        return packetListeners[opcode];
    }

    /**
     * The method that looks up the packets type.
     *
     * @param opcode The opcode to lookup
     * @return The packet type.
     */
    public static PacketType lookupType(int opcode) {
        if (opcode < 0 || opcode >= packetTypes.length) {
            return PacketType.EMPTY;
        }

        final int size = packetTypes[opcode];

        if (size == -2) {
            return PacketType.VAR_SHORT;
        } else if (size == -1) {
            return PacketType.VAR_BYTE;
        } else if (size > 0) {
            return PacketType.FIXED;
        } else {
            return PacketType.EMPTY;
        }
    }

    /**
     * Looks up the reference size of a packet.
     *
     * @param opcode The packet to lookup
     * @return The reference size.
     */
    public static int lookupSize(int opcode) {
        if (opcode < 0 || opcode >= packetTypes.length) {
            return 0;
        }
        return packetTypes[opcode];
    }

    /**
     * Registers a type for a packet.
     * <p>
     * The following types are...
     * <p>
     * {@code -2} var_short
     * {@code -1} var_byte
     * {@code 0} empty
     * > 0 fixed
     *
     * @param opcode The packet to register this type for
     * @param type   The type for this packet.
     */
    public static void registerType(int opcode, int type) {
        Preconditions.checkArgument(opcode >= 0 && opcode < packetListeners.length);
        packetTypes[opcode] = type;
    }

}
