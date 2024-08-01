package com.osroyale.net;

import com.osroyale.Config;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.haproxy.HAProxyMessage;

/**
 * @author Jire
 */
public final class HAProxyMessageHandler extends SimpleChannelInboundHandler<HAProxyMessage> {

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx,
                                final HAProxyMessage msg) {
        switch (msg.command()) {
            case LOCAL -> ctx.close();
            case PROXY -> {
                final String sourceAddress = msg.sourceAddress();
                if (sourceAddress != null) {
                    ctx.channel().attr(Config.SOURCE_ADDRESS).set(sourceAddress);
                }
            }
        }
    }

}
