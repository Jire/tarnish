package com.osroyale.content.emote;

import com.osroyale.net.packet.out.SendConfig;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.game.action.impl.EmoteAction;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.containers.equipment.Equipment;
import com.osroyale.util.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class handles emotes from the emote tab and skill cape.
 * 
 * @author Daniel | Obey
 */
public class EmoteHandler {

	/**
	 * Handles refreshing the emote tab.
	 * 
	 * @param player
	 *            The player refreshing the emote tab.
	 */
	public static void refresh(Player player) {
		for (Emote emote : Emote.values()) {
			if (emote.getConfig() != -1) {
				int flag = emote.activated(player) ? 0 : 1;
				player.send(new SendConfig(emote.getConfig(), flag));
			}
		}
		updateSkillcape(player);
	}

	/**
	 * Updates the skillcape emote.
	 * 
	 * @param player
	 *            The player instance.
	 */
	public static void updateSkillcape(Player player) {
		int flag = 1;
		Item item = player.equipment.get(Equipment.CAPE_SLOT);

		if (item != null) {
			Skillcape skillcape = Skillcape.forId(item.getId());
			if (skillcape != null) {
				flag = 0;
			}
		}
		
		player.send(new SendConfig(1114, flag));
	}

	public static boolean contains(Player player, EmoteUnlockable emote) {
		return player.emoteUnlockable.contains(emote);
	}

	public static boolean containsAll(Player player, EmoteUnlockable... emotes) {
		for (EmoteUnlockable emote : emotes) {
			if (!contains(player, emote)) {
				return false;
			}
		}
		return true;
	}

	public static EmoteUnlockable selectRandom(Player player, EmoteUnlockable... emotes) {
		List<EmoteUnlockable> selected = new ArrayList<>(emotes.length);
		for (EmoteUnlockable emote : emotes) {
			if (!contains(player, emote)) {
				selected.add(emote);
			}
		}
		return selected.isEmpty() ? null : Utility.randomElement(selected);
	}

	/**
	 * Handles unlocking an emote.
	 * 
	 * @param player
	 *            the player unlocking the emote.
	 * @param emote
	 *            The emote being activated.
	 */
	public static void unlock(Player player, EmoteUnlockable emote) {
		if (!player.emoteUnlockable.contains(emote)) {
			player.emoteUnlockable.add(emote);
			refresh(player);
			player.send(new SendMessage("Congratulations, you have unlocked the " + Utility.formatEnum(emote.name()) + " emote."));
		}
	}

	/**
	 * Handles unlocking all the emotes.
	 * 
	 * @param player
	 *            The player unlocking all the emotes.
	 */
	public static void unlockAll(Player player) {
		Arrays.stream(EmoteUnlockable.values()).forEach(e -> {
			if (!player.emoteUnlockable.contains(e)) {
				player.emoteUnlockable.add(e);
			}
		});
		refresh(player);
		player.send(new SendMessage("You have successfully unlocked all the emotes."));
	}

}
