package com.osroyale.net.session;

import com.osroyale.Config;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.GamePacket;
import com.osroyale.net.packet.PacketRepository;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jctools.queues.MessagePassingQueue;
import org.jctools.queues.MpscArrayQueue;

/**
 * Represents a {@link Session} when a {@link Player}
 * has been authenticated and active in the game world.
 *
 * @author nshusa
 */
public final class GameSession extends Session {

    private static final Logger logger = LogManager.getLogger();

    private final Player player;

    private final MessagePassingQueue<GamePacket> incomingPackets
            = new MpscArrayQueue<>(Config.CLIENT_PACKET_THRESHOLD);

    private final MessagePassingQueue<GamePacket> outgoingPackets
            = new MpscArrayQueue<>(Config.SERVER_PACKET_THRESHOLD);

    GameSession(Channel channel, Player player) {
        super(channel);
        this.player = player;
    }

    @Override
    public void handleClientPacket(Object o) {
        if (o instanceof GamePacket) {
            queueClientPacket((GamePacket) o);
        }
    }

    @Override
    protected void onClose(ChannelFuture f) {
        World.queueLogout(player);
    }

    private void queueClientPacket(final GamePacket packet) {
        incomingPackets.offer(packet);
    }

    public void processClientPackets() {
        final MessagePassingQueue<GamePacket> incomingPackets = this.incomingPackets;
        final Player player = this.player;

        for (int i = 0; i < Config.CLIENT_PACKET_THRESHOLD; i++) {
            final GamePacket packet = incomingPackets.poll();
            if (packet == null) break;

            try {
                PacketRepository.sendToListener(player, packet);
            } catch (Exception ex) {
                logger.error(String.format("error processing client packet for %s", player), ex);
            } finally {
                packet.release();
            }
        }
    }

    public void queueServerPacket(GamePacket packet) {
        outgoingPackets.offer(packet);
    }

    public void processServerPacketQueue() {
        final Channel channel = this.channel;
        final boolean channelActive = channel.isActive();

        final MessagePassingQueue<GamePacket> outgoingPackets = this.outgoingPackets;

        int count = 0;
        for (; count < Config.SERVER_PACKET_THRESHOLD; count++) {
            final GamePacket packet = outgoingPackets.poll();
            if (packet == null) break;
            if (channelActive) {
                try {
                    channel.write(packet, channel.voidPromise());
                } catch (final Exception ex) {
                    logger.error(String.format("error writing packet %s for %s", packet, player));
                }
            } else {
                if (packet.refCnt() > 0) {
                    packet.release();
                }
            }
        }
        if (channelActive && count > 0) {
            channel.flush();
        }
    }

    public Player getPlayer() {
        return player;
    }

}
