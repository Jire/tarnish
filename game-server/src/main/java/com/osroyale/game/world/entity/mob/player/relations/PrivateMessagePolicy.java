package com.osroyale.game.world.entity.mob.player.relations;

/** The enumeration of a players private messaging policy. */
public enum PrivateMessagePolicy {

	/** This setting allows for everyone to see this user's online status. */
	ALLOW,

	/** This setting only allows friends to see this user's online status. */
	FRIENDS_ONLY,

	/** This setting hides this user's online status to all players. */
	BLOCK
}