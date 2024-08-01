package com.osroyale.game.world.entity.mob.player.relations;

import com.osroyale.util.ChatCodec;

/**
 * Represents a chat message that can be displayed over an entities head.
 *
 * @author nshusa
 */
public final class ChatMessage {

	/**
	 *  When a chat message decoded there's a character buffer that can only hold 100 characters
	 **/
	public static final int CHARACTER_LIMIT = 100;

	private final ChatColor color;
	private final ChatEffect effect;
	private final byte[] encoded;

	private ChatMessage(byte[] encoded) {
		this(encoded, ChatColor.YELLOW, ChatEffect.NONE);
	}

	private ChatMessage(byte[] encoded, ChatColor color, ChatEffect effect) {
		this.encoded = encoded;
		this.color = color;
		this.effect = effect;
	}

	public static ChatMessage create(String message) {
		return create(message, ChatColor.YELLOW, ChatEffect.NONE);
	}

	public static ChatMessage create(String message, ChatColor color) {
		return create(message, color, ChatEffect.NONE);
	}

	public static ChatMessage create(String message, ChatColor color, ChatEffect effect) {
		return new ChatMessage(ChatCodec.encode(message.isEmpty() || message.length() > CHARACTER_LIMIT ? "Cabbage" : message), color, effect);
	}

	public boolean isValid() {
		if (color.getCode() < 0 || color.getCode() >= ChatColor.values().length) {
			return false;
		}
		if (effect.getCode() < 0 || effect.getCode() >= ChatColor.values().length) {
			return false;
		}
        return encoded.length != 0 && encoded.length <= CHARACTER_LIMIT;
    }

	public ChatColor getColor() {
		return color;
	}

	public ChatEffect getEffect() {
		return effect;
	}

	public byte[] getEncoded() {
		return encoded;
	}

}
