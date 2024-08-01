package com.osroyale.net;

import com.osroyale.Config;
import com.osroyale.net.session.Session;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A {@link SimpleChannelInboundHandler} implementation for re-routing
 * channel-events to its bound {@link Session}.
 *
 * @author nshusa
 */
public final class ChannelHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger logger = LogManager.getLogger();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object o) {
        try {
            final Session session = ctx.channel().attr(Config.SESSION_KEY).get();
            if (session != null) {
                session.handleClientPacket(o);
            }
        } catch (Exception ex) {
            logger.error("Error reading channel!", ex);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        final Session session = ctx.channel().attr(Config.SESSION_KEY).get();
        if (session != null) {
            session.close();
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                ctx.channel().close();
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) {
        if (!Config.IGNORED_EXCEPTIONS.contains(e.getMessage())) {
            logger.error("Exception caught upstream!", e);
        }

        final Session session = ctx.channel().attr(Config.SESSION_KEY).get();
        if (session != null) {
            session.close();
        }
    }

}
