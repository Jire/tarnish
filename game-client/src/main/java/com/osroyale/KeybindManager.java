package com.osroyale;

import java.awt.event.KeyEvent;
import java.io.*;

import static com.osroyale.Configuration.CHAR_PATH;

/**
 * Handles management of custom key binds.
 *
 * @author Daniel.
 */
public final class KeybindManager {

	private static final int
		ATTACK_TAB = 0,
		SKILL_TAB = 1,
		QUEST_TAB = 2,
		INVENTORY_TAB = 3,
		EQUIPMENT_TAB = 4,
		PRAYER_TAB = 5,
		MAGIC_TAB = 6,
		FRIENDS_TAB = 8,
		IGNORE_TAB = 9,
		CLAN_TAB = 13,
		WRENCH_TAB = 11,
		EMOTE_TAB = 12,
		MINIGAME_TAB = 14,
		LOGOUT_TAB = 10;

	private int[] keys = new int[15];

	/** The file location of the file. */
	private static final String FILE_NAME = "keybinds.dat";

	/** Constructs a new <code>KeybindManager</code>.  */
	KeybindManager() { }

	/** Handles binding the key.  */
	public void set(int key, int value) {
		System.out.println("Prekey: " + key);
		key = fixKey(key); //Fixes keys being wonky
		System.out.println("Setting key " + key + " to option " + value);
		value = getKey(value);
		System.out.println("Value is actually: " + value);
		int slot = checkBind(value);
		System.out.println("slot: " + slot);

		int old = keys[key];
		keys[key] = value;

		if (slot != -1) {
			keys[slot] = -1;
		}

		save();
		update();
	}

	private int fixKey(int key) {
		System.out.println("key: " + key);
		switch (key)
		{
			case 7:
				return 8;
			case 8:
				return 9;
			case 9:
				return 10;
			case 13:
				return 11;
			case 11:
				return 13;
			case 10:
				return 14;
		}
		return key;
	}

	/** Handles setting the default key binds. */
	void defaultBind() {
		keys[ATTACK_TAB] = KeyEvent.VK_F5;
		keys[SKILL_TAB] = KeyEvent.VK_F6;
		keys[QUEST_TAB] = KeyEvent.VK_F7;
		keys[INVENTORY_TAB] = KeyEvent.VK_F1;
		keys[EQUIPMENT_TAB] = KeyEvent.VK_F2;
		keys[PRAYER_TAB] = KeyEvent.VK_F3;
		keys[MAGIC_TAB] = KeyEvent.VK_F4;
		keys[FRIENDS_TAB] = KeyEvent.VK_F7;
		keys[IGNORE_TAB] = KeyEvent.VK_F8;
		keys[CLAN_TAB] = KeyEvent.VK_F9;
		keys[WRENCH_TAB] = KeyEvent.VK_F10;
		keys[EMOTE_TAB] = KeyEvent.VK_F11;
		keys[LOGOUT_TAB] = KeyEvent.VK_ESCAPE;
		keys[MINIGAME_TAB] = -1;
	}

	/** Handles updating the key binding interface. */
	void update() {
		Client.sendString(getKeyFormat(keys[ATTACK_TAB]), 39305);
		Client.sendString(getKeyFormat(keys[SKILL_TAB]), 39306);
		Client.sendString(getKeyFormat(keys[QUEST_TAB]), 39307);
		Client.sendString(getKeyFormat(keys[INVENTORY_TAB]), 39308);
		Client.sendString(getKeyFormat(keys[EQUIPMENT_TAB]), 39309);
		Client.sendString(getKeyFormat(keys[PRAYER_TAB]), 39310);
		Client.sendString(getKeyFormat(keys[MAGIC_TAB]), 39311);
		Client.sendString(getKeyFormat(keys[FRIENDS_TAB]), 39312);
		Client.sendString(getKeyFormat(keys[IGNORE_TAB]), 39313);
		Client.sendString(getKeyFormat(keys[LOGOUT_TAB]), 39314);
		Client.sendString(getKeyFormat(keys[WRENCH_TAB]), 39318);
		Client.sendString(getKeyFormat(keys[EMOTE_TAB]), 39317);
		Client.sendString(getKeyFormat(keys[CLAN_TAB]), 39316);
		Client.sendString(getKeyFormat(keys[MINIGAME_TAB]), 39315);
	}

	/** Handles binding the value to a key.  */
	public void handleBind(int value) {
		for (int index = 0; index < keys.length; index++) {
			if (keys[index] == value) {

				Client.setTab(getTab(index));
				break;
			}
		}
	}

	private int getTab(int index) {
		switch(index) {
			case 10:
				return 14;
			case 13:
				return 10;
			case 14:
				return 13;
		}
		return index;
	}

	/** Handles checking the value of the bind. */
	private int checkBind(int value) {
		for (int index = 0; index <= 14; index++) {
			if (keys[index] > 0 && keys[index] == value)
				return index;
		}
		return -1;
	}

	/** Gets the next available value to bind.  */
	private int getAvailable() {
		for (int index = 0; index < 14; index++) {
			if (keys[index] == 0)
				return getKey(index);
		}
		return -1;
	}

	/** Gets the key. */
	private int getKey(int key) {
		switch (key) {
			case 0:
				return 0;
			case 1:
				return KeyEvent.VK_F1;
			case 2:
				return KeyEvent.VK_F2;
			case 3:
				return KeyEvent.VK_F3;
			case 4:
				return KeyEvent.VK_F4;
			case 5:
				return KeyEvent.VK_F5;
			case 6:
				return KeyEvent.VK_F6;
			case 7:
				return KeyEvent.VK_F7;
			case 8:
				return KeyEvent.VK_F8;
			case 9:
				return KeyEvent.VK_F9;
			case 10:
				return KeyEvent.VK_F10;
			case 11:
				return KeyEvent.VK_F11;
			case 12:
				return KeyEvent.VK_F12;
			case 13:
				return KeyEvent.VK_ESCAPE;
		}
		return 0;
	}

	/** Gets the key format. */
	private String getKeyFormat(int key) {
		switch (key) {
			case 0:
				return "None";
			case KeyEvent.VK_F1:
				return "F1";
			case KeyEvent.VK_F2:
				return "F2";
			case KeyEvent.VK_F3:
				return "F3";
			case KeyEvent.VK_F4:
				return "F4";
			case KeyEvent.VK_F5:
				return "F5";
			case KeyEvent.VK_F6:
				return "F6";
			case KeyEvent.VK_F7:
				return "F7";
			case KeyEvent.VK_F8:
				return "F8";
			case KeyEvent.VK_F9:
				return "F9";
			case KeyEvent.VK_F10:
				return "F10";
			case KeyEvent.VK_F11:
				return "F11";
			case KeyEvent.VK_F12:
				return "F12";
			case KeyEvent.VK_F13:
				return "F13";
			case KeyEvent.VK_ESCAPE:
				return "ESC";
		}
		return "None";
	}

	/** Saves the key binds. */
	void save() {
		if (keys == null) {
			return;
		}
		try {
			File file = FileUtility.getOrCreate(CHAR_PATH, FILE_NAME);
			DataOutputStream output = new DataOutputStream(new FileOutputStream(file));
			output.writeByte(14);
			for (int index = 0; index < 14; index++) {
				output.writeByte(keys[index]);
			}
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** Loads the key binds. */
	public void load() {
		try {
			File file = FileUtility.getOrCreate(CHAR_PATH, FILE_NAME);
			if (file.length() == 0) {
				defaultBind();
				save();
				return;
			}
			DataInputStream input = new DataInputStream(new FileInputStream(file));
			int fileSize = input.readByte();
			for (int index = 0; index < fileSize; index++) {
				int key = input.readByte();
				keys[index] = key;
			}
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
