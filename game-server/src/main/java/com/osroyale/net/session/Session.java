package com.osroyale.net.session;

import com.osroyale.Config;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import java.net.InetSocketAddress;

/**
 * Represents a session between a user and the server.
 *
 * @author nshusa
 */
public class Session {

    /**
     * The channel attached to this session.
     */
    protected final Channel channel;

    /**
     * The users host address.
     */
    protected final String host;

    /**
     * Creates a new {@link Session}.
     *
     * @param channel
     *      The channel that is attached to this session.
     */
    public Session(Channel channel) {
        this.channel = channel;
        this.host = ((InetSocketAddress) channel.remoteAddress()).getAddress().getHostAddress();
    }

    /**
     * The method to close this session.
     */
    public final void close() {
        onClose(channel.close());
    }

    /**
     * The method called after a session has been closed.
     *
     * @param future
     * The result of the session being closed.
     */
    protected void onClose(ChannelFuture future) {

    }

    /**
     * The method that is called when the client sends packets to the server.
     *
     * @param o
     *      The vague object packet.
     */
    public void handleClientPacket(Object o) {

    }

    /**
     * Gets the underlying {@link Channel} for this {@link Session}.
     *
     * @return The underlying channel.
     */
    public Channel getChannel() {
        return channel;
    }

    /**
     * Gets the users host address
     */
    public String getHost() {
        final Channel channel = getChannel();
        if (channel != null && channel.hasAttr(Config.SOURCE_ADDRESS)) {
            return channel.attr(Config.SOURCE_ADDRESS).get();
        }
        return host;
    }

}
