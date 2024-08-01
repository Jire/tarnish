package com.osroyale.game.action.policy;

/**
 * A queue policy determines when the clients should queue up actions.
 * 
 * @author Graham Edgecombe
 */
public enum QueuePolicy {

	/** This indicates actions will always be queued. */
	ALWAYS,

	/** This indicates actions will never be queued. */
	NEVER

}