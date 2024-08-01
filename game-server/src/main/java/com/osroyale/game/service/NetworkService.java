package com.osroyale.game.service;

import com.osroyale.Config;
import com.osroyale.Starter;
import com.osroyale.net.ServerPipelineInitializer;
import com.osroyale.util.Stopwatch;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ResourceLeakDetector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

/**
 * The bootstrap that will prepare the game and net.
 * @author Seven
 */
public final class NetworkService {

	private static final Logger logger = LogManager.getLogger();

	private final Starter starter;

	public NetworkService(Starter starter) {
		this.starter = starter;
	}

	public void start(int port) throws Exception {
		logger.info("Starting network service on port: " + port);

		ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.DISABLED);
		final EventLoopGroup bossGroup = Epoll.isAvailable() ? new EpollEventLoopGroup(1) : new NioEventLoopGroup(1);
		final EventLoopGroup workerGroup = Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup();

		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
					.channel(Epoll.isAvailable() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
					.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
					.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
					.childOption(ChannelOption.TCP_NODELAY, true)
					.childOption(ChannelOption.AUTO_READ, true)
					.childOption(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(2 << 16, 2 << 18))
					.childOption(ChannelOption.SO_SNDBUF, 65536)
					.childOption(ChannelOption.SO_RCVBUF, 65536)
					.childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30_000)
					.childOption(ChannelOption.IP_TOS, Config.IP_TOS)
					.childHandler(new ServerPipelineInitializer(starter));

			ChannelFuture f = b.bind(port).syncUninterruptibly();

			starter.setServerStarted(true);

			Stopwatch uptime = starter.getUptime();
			logger.info(String.format("Server built successfully (took %d seconds).", uptime.elapsedTime(TimeUnit.SECONDS)));
			uptime.reset();
			f.channel().closeFuture().sync();
		} catch (Exception ex) {
			logger.error("error starting network service.", ex);
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

}