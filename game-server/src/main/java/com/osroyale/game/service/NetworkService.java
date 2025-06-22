package com.osroyale.game.service;

import com.osroyale.Config;
import com.osroyale.Starter;
import com.osroyale.net.ServerPipelineInitializer;
import com.osroyale.util.Stopwatch;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.util.ResourceLeakDetector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jire.tarnishps.BootstrapFactory;

import java.util.concurrent.TimeUnit;

/**
 * The bootstrap that will prepare the game and net.
 *
 * @author Seven
 * @author Jire
 */
public final class NetworkService {

    private static final Logger logger = LogManager.getLogger();

    private final Starter starter;

    public NetworkService(final Starter starter) {
        this.starter = starter;
    }

    public void start(final int port) throws Exception {
        logger.info("Starting network service on port: {}", port);

        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.DISABLED);

        final BootstrapFactory bootstrapFactory = new BootstrapFactory();

        final EventLoopGroup parentGroup = bootstrapFactory.createParentLoopGroup();
        final EventLoopGroup childGroup = bootstrapFactory.createChildLoopGroup();

        try {
            final ServerBootstrap bootstrap = bootstrapFactory
                    .createServerBootstrap(parentGroup, childGroup)
                    .childOption(ChannelOption.AUTO_READ, true)
                    .childOption(ChannelOption.IP_TOS, Config.IP_TOS)
                    .childHandler(new ServerPipelineInitializer(starter));

            final ChannelFuture channelFuture = bootstrap
                    .bind(port)
                    .syncUninterruptibly();

            starter.setServerStarted(true);

            final Stopwatch uptime = starter.getUptime();
            logger.info("Server built successfully (took {} seconds).", uptime.elapsedTime(TimeUnit.SECONDS));
            uptime.reset();

            channelFuture
                    .channel()
                    .closeFuture()
                    .sync();
        } catch (final Exception ex) {
            logger.error("error starting network service.", ex);
        } finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }

}
