package com.osroyale.content.writer.impl;

import com.osroyale.content.writer.InterfaceWriter;
import com.osroyale.game.world.entity.mob.player.Player;

/**
 * Class handles writing on the quest tab interface.
 * 
 * @author Daniel
 */
public class ToolWriter extends InterfaceWriter {

	private final String[] text = {
			"View My Profile",
			"Activity Logger",
			"Title Manager",
			"Drop Display",
			"Drop Simulator",
			"Game Records",
	};

	public ToolWriter(Player player) {
		super(player);
	}

	@Override
	protected int startingIndex() {
		return 35451;
	}

	@Override
	protected String[] text() {
		return text;
	}

	@Override
	protected int[][] color() {
		return null;
	}

	@Override
	protected int[][] font() {
		return null;
	}

}
