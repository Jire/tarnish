package com.osroyale.game.action.policy;

/**
 * A queue policy determines whether the action can occur while walking.
 * 
 * @author Graham Edgecombe
 * @author Brett Russell
 */
public enum WalkablePolicy {

	/** This indicates actions may occur while walking. */
	WALKABLE,

	/** This indicates actions cannot occur while walking. */
	NON_WALKABLE,

	/** This indicates actions can continue while following. */
	FOLLOW
}