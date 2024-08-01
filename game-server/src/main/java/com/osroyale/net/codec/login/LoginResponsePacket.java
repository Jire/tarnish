package com.osroyale.net.codec.login;

import com.osroyale.game.world.entity.mob.player.PlayerRight;

/**
 * An immutable message that is written through a channel and forwarded to the
 * {@code LoginResponseEncoder} where it is encoded and sent to the client.
 *
 * @author lare96 <http://github.org/lare96>
 */
public final class LoginResponsePacket {

	/**
	 * The actual login response.
	 */
	private final LoginResponse response;

	/**
	 * The {@code Player}s authority level.
	 */
	private final PlayerRight rights;

	/**
	 * If the {@code Player} is flagged.
	 */
	private final boolean flagged;

	/**
	 * Creates a new {@code LoginResponsePacket}.
	 *
	 * @param response
	 *            The actual login response.
	 * @param rights
	 *            The {@code Player}s authority level.
	 * @param flagged
	 *            If the {@code Player} is flagged.
	 */
	public LoginResponsePacket(LoginResponse response, PlayerRight rights, boolean flagged) {
		this.response = response;
		this.rights = rights;
		this.flagged = flagged;
	}

	/**
	 * Creates a new {@code LoginResponsePacket} with an authority level of
	 * {@code PLAYER} and a {@code flagged} value of {@code false}.
	 *
	 * @param response
	 *            The actual login response.
	 */
	public LoginResponsePacket(LoginResponse response) {
		this(response, PlayerRight.PLAYER, false);
	}

	/**
	 * @return The actual login response.
	 */
	public LoginResponse getResponse() {
		return response;
	}

	/**
	 * @return The {@code Player}s authority level.
	 */
	public PlayerRight getRights() {
		return rights;
	}

	/**
	 * @return {@code true} if flagged
	 */
	public boolean isFlagged() {
		return flagged;
	}
}
