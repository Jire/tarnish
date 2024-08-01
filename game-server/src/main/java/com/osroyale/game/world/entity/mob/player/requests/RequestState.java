package com.osroyale.game.world.entity.mob.player.requests;

/**
 * Holds the different states the manager can be in.
 * 
 * @author Graham Edgecombe
 */
public enum RequestState {
	
	/** Nobody has offered a request. */
	NORMAL,
	
	/** Somebody has offered some kind of request. */
	REQUESTED,
	
	/**
	 * The player is participating in an existing request of this type, so
	 * cannot accept new requests at all.
	 */
	PARTICIPATING
}