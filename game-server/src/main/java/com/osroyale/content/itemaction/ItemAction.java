package com.osroyale.content.itemaction;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;

/**
 * Handles executing an item action.
 * 
 * @author Daniel
 */
public abstract class ItemAction {

	/** The name of the action. */
	public abstract String name();
	
	/** The message of the action. */
	public String message(Item item) {
		return "";
	}

	/** The item click delay of the action. */
	public int delay() {
		return -1;
	}

	/** The execution method of the action. */
	public boolean inventory(Player player, Item item, int opcode) {
		return false;
	}

	public boolean equipment(Player player, Item item, int opcode) {
		return false;
	}

	public boolean itemOnItem(Player player, Item first, Item second) {
		return false;
	}

	public boolean drop(Player player, Item item) {
		return false;
	}

	@Override
	public String toString() {
		return String.format("Action=%s", name());
	}

}
