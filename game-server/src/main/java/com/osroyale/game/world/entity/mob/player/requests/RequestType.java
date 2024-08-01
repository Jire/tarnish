package com.osroyale.game.world.entity.mob.player.requests;

/**
 * Represents the different types of request.
 * 
 * @author Graham Edgecombe
 */
public enum RequestType {
	
	/** A trade request. */	
	TRADE("tradereq"),
	
	/** A duel request. */
	DUEL("duelreq");

	/** The client-side name of the request. */
	private String clientName;

	/**
	 * Creates a type of request.
	 * 
	 * @param clientName
	 *            The name of the request client-side.
	 */
    RequestType(String clientName) {
		this.clientName = clientName;
	}

	/**
	 * Gets the client name.
	 * 
	 * @return The client name.
	 */
	public String getClientName() {
		return clientName;
	}
}