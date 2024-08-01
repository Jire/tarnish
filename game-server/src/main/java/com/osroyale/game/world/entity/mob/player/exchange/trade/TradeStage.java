package com.osroyale.game.world.entity.mob.player.exchange.trade;

/**
 * The enumerated types who's elements represent the stages of a trade
 * session.
 * 
 * @author Daniel 
 */
public enum TradeStage {

	/**
	 * The stage before the session between two players is created.
	 */
	REQUEST,

	/**
	 * The stage when two players are able to offer items.
	 */
	OFFER,

	/**
	 * The stage when two players initially accept each others offers.
	 */
	FIRST_ACCEPT,

	/**
	 * The stage that validates both offers.
	 */
	FINAL_ACCEPT
}