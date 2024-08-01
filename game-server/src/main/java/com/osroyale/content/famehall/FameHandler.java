package com.osroyale.content.famehall;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.World;
import com.osroyale.game.world.items.Item;
import com.osroyale.net.packet.out.*;
import com.osroyale.util.Utility;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This class handles the fame system.
 * 
 * @author Daniel
 */
public class FameHandler {

	/** Holds the hall of fame entries. */
	public static Map<FameEntry, Fame> HALL_OF_FAME = new HashMap<>();

	/**
	 * Checks if player should be entered in the hall of fame.
	 * 
	 * @param player
	 *            The player instance.
	 * @param fameEntry
	 *            The fame entry.
	 */
	public static void activate(Player player, FameEntry fameEntry) {
		if (!HALL_OF_FAME.containsKey(fameEntry)) {
			HALL_OF_FAME.put(fameEntry, new Fame(fameEntry.getType(), PlayerRight.getCrown(player) + " " + player.getName(), Utility.getDate()));
			player.send(new SendMessage("Congratulations, you have left your mark on the hall of fame!"));
			World.sendBroadcast(1, player.getName() + " is now on the hall of fame for " + fameEntry.getEntry(), false);
			save();
		}
	}

	/**
	 * Removes a fame entry from the hall of fame.
	 * 
	 * @param fameEntry
	 *            The fame entry to withdraw.
	 */
	public static void remove(FameEntry fameEntry) {
		if (HALL_OF_FAME.containsKey(fameEntry))
			HALL_OF_FAME.remove(fameEntry);
	}

	/**
	 * Filters the hall of fame entries for a certain fame type.
	 * 
	 * @param type
	 *            The fame type to filer.
	 * @return Map of all the entries.
	 */
	public static Map<FameEntry, Fame> getEntries(FameType type) {
		Map<FameEntry, Fame> hall = new HashMap<>();
		for (Entry<FameEntry, Fame> entries : HALL_OF_FAME.entrySet()) {
			if (entries.getKey().getType() == type)
				hall.put(entries.getKey(), entries.getValue());
		}
		return hall;
	}

	/**
	 * Gets the fame by the fame entry.
	 * 
	 * @param entry
	 *            The entry to get.
	 * @return The fame.
	 */
	private static Fame getEntry(FameEntry entry) {
		Fame result = null;
		for (Entry<FameEntry, Fame> fame : getEntries(entry.getType()).entrySet()) {
			if (fame.getKey().name().equalsIgnoreCase(entry.name())) {
				result = fame.getValue();
				break;
			}
		}
		return result;
	}

	/**
	 * Opens the hall of fame itemcontainer.
	 * 
	 * @param player
	 *            The player instance.
	 * @param type
	 *            The fame type tab.
	 */
	public static void open(Player player, FameType type) {
		int string = 58533;
		List<FameEntry> entry_list = FameEntry.getEntries(type);
		Item[] items = new Item[entry_list.size()];

		for (int index = 0; index < entry_list.size(); index++) {
			FameEntry display = entry_list.get(index);			
			Fame fame = getEntry(display);

			items[index] = new Item(display.getDisplay());
			player.send(new SendString(display == null ? "" : display.getEntry(), string));
			string++;
			player.send(new SendString(fame == null ? "---" : fame.getName(), string));
			string++;
			player.send(new SendString(fame == null ? "---" : fame.getAccomplished(), string));
			string++;
			string++;
		}
		
		player.send(new SendConfig(1150, type.ordinal()));
		player.send(new SendString(HALL_OF_FAME.size() + "/" + FameEntry.values().length + " Remaining", 58523));
		player.send(new SendString("<col=" + (type.ordinal() == 0 ? "ffffff" : "ff9933") + ">Player Killing", 58515));
		player.send(new SendString("<col=" + (type.ordinal() == 1 ? "ffffff" : "ff9933") + ">Monster Killing", 58516));
		player.send(new SendString("<col=" + (type.ordinal() == 2 ? "ffffff" : "ff9933") + ">Skilling", 58517));
		player.send(new SendString("<col=" + (type.ordinal() == 3 ? "ffffff" : "ff9933") + ">Miscellaneous", 58518));
		player.send(new SendItemOnInterface(58531, items));
		player.send(new SendScrollbar(58530, (entry_list.size() * 34)));
		player.interfaceManager.open(58500);
	}

	/**
	 * Searches through all the fame entries for a specific player.
	 * 
	 * @param player
	 *            The player instance.
	 * @param context
	 *            The context being searched.
	 */
	public static void search(Player player, String context) {
		int index = 0;
		for (Entry<FameEntry, Fame> entries : HALL_OF_FAME.entrySet()) {
			if (entries.getValue().getName().equalsIgnoreCase(context)) {
				index++;
			}
		}
		
		String message = index == 0 ? Utility.formatName(context) + " was not found in the hall of fame." : index + " entries were found in the hall of fame for " + Utility.formatName(context) + ".";
		player.send(new SendMessage(message));
	}

	/**
	 * Loads the hall of fames and puts them into the map.
	 */
	public static void load() {
		Type type = new TypeToken<Map<FameEntry, Fame>>() {
		}.getType();

		Path path = Paths.get("data", "/content/fame/hall_of_fame_list.json");
		try (FileReader reader = new FileReader(path.toFile())) {
			JsonParser parser = new JsonParser();
			HALL_OF_FAME = new GsonBuilder().create().fromJson(parser.parse(reader), type);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Saves the hall of fames into a json file.
	 */
	public static void save() {
		Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		try (FileWriter fw = new FileWriter("./data/content/fame/hall_of_fame_list.json")) {
			fw.write(gson.toJson(HALL_OF_FAME));
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
