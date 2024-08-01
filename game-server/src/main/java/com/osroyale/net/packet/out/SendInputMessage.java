package com.osroyale.net.packet.out;
import com.osroyale.net.packet.OutgoingPacket;
import com.osroyale.net.packet.PacketType;
import com.osroyale.net.codec.ByteModification;
import com.osroyale.game.world.entity.mob.player.Player;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Sends an input dialogue.
 * 
 * @author Daniel
 * @author Michael
 */
public class SendInputMessage extends OutgoingPacket {

	private final Consumer<String> action;
	private final String inputMessage;
	private final int inputLength;

	public SendInputMessage(String message, int length, Consumer<String> action) {
		super(187, PacketType.VAR_SHORT);
		this.action = action;
		this.inputMessage = message;
		this.inputLength = length;
	}

	@Override
	public boolean encode(Player player) {
		player.enterInputListener = Optional.of(action);
		builder.writeString(inputMessage)
		.writeShort(inputLength, ByteModification.ADD);
		return true;
	}

}
