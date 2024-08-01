package com.osroyale.util.parser.impl;

import com.google.gson.JsonObject;
import com.osroyale.net.packet.PacketRepository;
import com.osroyale.util.parser.GsonParser;

/**
 * Parses through the packet sizes file and associates their opcode with a size.
 * 
 * @author SeVen
 */
public final class PacketSizeParser extends GsonParser {

    /**
     * Creates a new {@code PacketSizeParser}.
     */
    public PacketSizeParser() {
		super("io/message_sizes", false);
    }

	@Override
	protected void parse(JsonObject data) {
		final int opcode = data.get("opcode").getAsInt();
		final int size = data.get("size").getAsInt();
		PacketRepository.registerType(opcode, size);
	}

}
