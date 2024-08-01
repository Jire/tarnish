package com.osroyale.net;

import com.google.common.collect.ConcurrentHashMultiset;
import com.google.common.collect.Multiset;
import com.osroyale.Config;
import com.osroyale.game.world.entity.mob.player.IPBannedPlayers;
import com.osroyale.net.codec.login.LoginResponse;
import com.osroyale.net.codec.login.LoginResponsePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The {@code ChannelInboundHandlerAdapter} implementation that will filter out
 * unwanted connections from propagating down the pipeline.
 *
 * @author Seven
 * @author Michael | Chex
 */
@Sharable
public class ChannelFilter extends ChannelInboundHandlerAdapter {

	/** A set of connections currently active within the server. */
	private final Multiset<Connection> connections = ConcurrentHashMultiset.create();

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Connection connection = getConenction(ctx);
		connection.setCanConnect(true);
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		Connection connection = getConenction(ctx);

		/* Make sure this channel is not going to get flooded. */
		if (!connection.canConnect()) {
			return;
		}

		/*
		 * If this local then, do nothing and proceed to next handler in the
		 * pipeline.
		 */
		if (connection.getHost().equalsIgnoreCase("127.0.0.1")) {
			return;
		}

		/* Add the host */
		connection.addConnection();

		/* Evaluate the host */
		if (connection.getConnections() > Config.CONNECTION_LIMIT) {
			disconnect(ctx, LoginResponse.LOGIN_LIMIT_EXCEEDED);
			return;
		}

		if (IPBannedPlayers.ipBans.contains(connection.getHost())) {
			disconnect(ctx, LoginResponse.ACCOUNT_DISABLED);
			return;
		}

		connections.add(connection);
		connection.setCanConnect(false);

		/*
		 * Nothing went wrong, so register the channel and forward the randomevent to
		 * next handler in the pipeline.
		 */
		ctx.fireChannelRegistered();
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		Connection connection = getConenction(ctx);

		/*
		 * If this is local, do nothing and proceed to next handler in the
		 * pipeline.
		 */
		if (connection.getHost().equalsIgnoreCase("127.0.0.1")) {
			return;
		}

		connection.removeConnection();

		/* Remove the host from the connection list */
		if (connection.getConnections() == 0) {
			connections.remove(connection);
		}

		/*
		 * The connection is unregistered so forward the randomevent to the next
		 * handler in the pipeline.
		 */
		ctx.fireChannelUnregistered();
	}

	/**
	 * Disconnects {@code ctx} with {@code response} as the response code.
	 *
	 * @param ctx
	 *            The channel handler context.
	 * @param response
	 *            The response to disconnect with.
	 */
	private void disconnect(ChannelHandlerContext ctx, LoginResponse response) {
		LoginResponsePacket message = new LoginResponsePacket(response);
		ByteBuf initialMessage = ctx.alloc().buffer(8).writeLong(0);

		ctx.write(initialMessage, ctx.voidPromise());
		ctx.writeAndFlush(message).addListener(ChannelFutureListener.CLOSE);
	}

	/**
	 * Gets the host address of the user logging in.
	 *
	 * @param ctx
	 *            The context of this channel.
	 *
	 * @return The host address of this connection.
	 */
	private Connection getConenction(ChannelHandlerContext ctx) {
		String host = ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress();
		return connections.stream().filter(conn -> conn.getHost().equalsIgnoreCase(host)).findFirst().orElse(new Connection(host));
	}

	/**
	 * Creates a conenction for a host address.
	 *
	 * @author Michael | Chex
	 */
	private static class Connection {

		/** The stopwatch which caches the time of our last login. */
		private final AtomicBoolean canConnect = new AtomicBoolean(true);

		/** The host address. */
		private final String host;

		/** The amount of connections associated with this host address. */
		private int connections;

		/**
		 * Constructs a new {@code Connection} object.
		 *
		 * @param host
		 *            The host address.
		 */
		public Connection(String host) {
			this.host = host;
		}

		/**
		 * Gets the host address.
		 *
		 * @return The host address.
		 */
		public String getHost() {
			return host;
		}

		/**
		 * Gets the amount of connections associated with this host address.
		 *
		 * @return The amount of connections associated with this host address.
		 */
		public int getConnections() {
			return connections;
		}

		/**
		 * Checks if this host can connect.
		 *
		 * @return {@code True} if this host can connect.
		 */
		public boolean canConnect() {
			return canConnect.get();
		}

		/** Adds a connection. */
		public void addConnection() {
			connections++;
		}

		/** Removes a connection. */
		public void removeConnection() {
			connections--;
		}

		public void setCanConnect(boolean val) {
			canConnect.compareAndSet(!val, val);
		}

		@Override
		public int hashCode() {
			return Objects.hash(host);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Connection) {
				Connection other = (Connection) obj;
				return host.equalsIgnoreCase(other.getHost());
			}
			return obj == this;
		}

		@Override
		public String toString() {
			return String.format("Connection[host=%s, connections=%s, elapsed=%s]", host, connections, canConnect);
		}

	}

}