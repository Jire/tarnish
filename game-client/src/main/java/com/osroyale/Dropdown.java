package com.osroyale;

/**
 * Handles all the drop down menu actions.
 *
 * @author Daniel
 */
public enum Dropdown {
	DEFAULT() {
		@Override
		public void selectOption(int option, Client client, RSInterface rsint) {
			client.outgoing.writeOpcode(255);
			client.outgoing.writeDWord(rsint.interfaceId);
			client.outgoing.writeByte(option);
			Client.sendString(rsint.disabledMessage, option);
		}
	},
	KEYBINDING() {
		@Override
		public void selectOption(int option, Client client, RSInterface rsint) {
			int key = rsint.interfaceId - 39305;
			client.keybindManager.set(key, option);
		}
	},
	SKILL_ORB() {
		@Override
		public void selectOption(int option, Client client, RSInterface rsint) {
			Client.sendString(rsint.disabledMessage, option);
			Settings.EXPERIENCE_ORBS = option == 0;
		}
	},
	COUNTER_PROGRESS() {
		@Override
		public void selectOption(int option, Client client, RSInterface rsint) {
			Client.sendString(rsint.disabledMessage, option);
			Settings.COUNTER_PROGRESS = option == 0;
		}
	},
	COUNTER_COLOR() {
		@Override
		public void selectOption(int option, Client client, RSInterface rsint) {
			Client.sendString(rsint.disabledMessage, option);
			if (option == 0) {//WHITE
				Settings.COUNTER_COLOR = 0xFFFFFF;
			} else if (option == 1) {//CYAN
				Settings.COUNTER_COLOR = 0x00E5FF;
			} else if (option == 2) {//LIME
				Settings.COUNTER_COLOR = 0x30C978;
			} else if (option == 3) {//PINK
				Settings.COUNTER_COLOR = 0xFF66E8;
			} else if (option == 4) {//RED
				Settings.COUNTER_COLOR = 0xFF4545;
			} else if (option == 5) {//ORANGE
				Settings.COUNTER_COLOR = 0xFF9233;
			}
		}
	},
	COUNTER_SIZE() {
		@Override
		public void selectOption(int option, Client client, RSInterface rsint) {
			Client.sendString(rsint.disabledMessage, option);
			Settings.COUNTER_SIZE = option;
		}
	},
	COUNTER_SPEED() {
		@Override
		public void selectOption(int option, Client client, RSInterface rsint) {
			Client.sendString(rsint.disabledMessage, option);
			if (option == 0) {
				Settings.COUNTER_SPEED = 1;
			} else if (option == 1) {
				Settings.COUNTER_SPEED = 0.5f;
			} else if (option == 2) {
				Settings.COUNTER_SPEED = 2;
			}
		}
	},
	COUNTER_POSITION() {
		@Override
		public void selectOption(int option, Client client, RSInterface rsint) {
			Client.sendString(rsint.disabledMessage, option);
			Settings.COUNTER_POSITION = option;
		}
	};

	Dropdown() {
	}

	public abstract void selectOption(int option, Client client, RSInterface rsint);
}
