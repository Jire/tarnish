package com.osroyale.game.world.entity.mob.player.requests;

import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.region.Region;
import com.osroyale.util.Utility;

/**
 * The request manager manages
 * 
 * @author Graham Edgecombe
 */
public class RequestManager {

	/** The player. */
	private Player player;

	/** The current state. */
	private RequestState state = RequestState.NORMAL;

	/** The current request type. */
	private RequestType requestType;

	/** The current 'acquaintance'. */
	private Player acquaintance;

	/**
	 * Creates the request manager.
	 * 
	 * @param player
	 *            The player whose requests the manager is managing.
	 */
	public RequestManager(Player player) {
		this.player = player;
	}

	/**
	 * Sends another player a request.
	 * 
	 * @param other
	 *            The other player to request.
	 * @param type
	 *            The request type.
	 * @return {@code True} if the request was mutual
	 */
	public boolean request(Player other, RequestType type) {
		if (!validate(player) || !validate(other)) {
			return false;
		}

		if (!Utility.withinDistance(other, player, Region.VIEW_DISTANCE)) {
			player.send(new SendMessage("Unable to find " + Utility.formatName(other.getName()) + "."));
			return false;
		}

		RequestManager otherManager = other.requestManager;

		if (state == RequestState.PARTICIPATING || otherManager.state == RequestState.PARTICIPATING) {
			return false;
		}

		requestType = type;
		acquaintance = other;
		state = RequestState.REQUESTED;

		if (mutualRequest(otherManager)) {
			state = RequestState.PARTICIPATING;
			otherManager.state = RequestState.PARTICIPATING;
			return true;
		}

		return false;
	}

	/**
	 * Performs a check to validate a player for a request.
	 * 
	 * @param entity
	 *            The player to validate.
	 * @return {@code True} if the player is valid
	 */
	private boolean validate(Player entity) {
		return entity != null && !entity.positionChange;
	}

	/**
	 * Checks if a request is mutual between two players.
	 * 
	 * @param otherManager
	 *            The other player's {@codeplain RequestManager}.
	 * @return {@code True} if the request was mutual
	 */
	private boolean mutualRequest(RequestManager otherManager) {
		boolean otherRequested = otherManager.state == RequestState.REQUESTED;
		boolean sameRequestType = requestType == otherManager.requestType;
		boolean mutualAcquantances = player.equals(otherManager.acquaintance);
		return otherRequested && sameRequestType && mutualAcquantances;
	}

	/** Resets the variables for a request. */
	private void reset() {
		requestType = null;
		acquaintance = null;
		state = RequestState.NORMAL;
	}

	/** Called when an itemcontainer is closed. */
	public void close() {
		reset();
	}

}