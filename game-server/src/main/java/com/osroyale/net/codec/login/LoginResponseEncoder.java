package com.osroyale.net.codec.login;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class LoginResponseEncoder extends MessageToByteEncoder<LoginResponsePacket> {

	@Override
	protected void encode(ChannelHandlerContext ctx, LoginResponsePacket msg, ByteBuf out) throws Exception {
		out.writeByte(msg.getResponse().getOpcode());

		if (msg.getResponse() == LoginResponse.NORMAL) {
			out.writeByte(msg.getRights().getCrown());
			out.writeByte(msg.isFlagged() ? 1 : 0);
		}
	}

}
