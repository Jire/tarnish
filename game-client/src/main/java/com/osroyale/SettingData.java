package com.osroyale;

/**
 * Holds all the setting data.
 */
public enum SettingData implements SettingsAction<Client> {
	//    SNOW_SETTING("Snow") {
	//        @Override
	//        public String name(Client client) {
	//            return SNOW + setting;
	//        }
	//
	//        @Override
	//        public void handle(Client client) {
	//            SNOW = !SNOW;
	//        }
	//    },
	GROUND_TEXTURES_SETTING("Ground Decorations") {
		@Override
		public String name(Client client) {
			return Settings.GROUND_DECORATIONS + setting;
		}

		@Override
		public boolean status() {
			return Settings.GROUND_DECORATIONS;
		}

		@Override
		public void handle(Client client) {
			Settings.GROUND_DECORATIONS = !Settings.GROUND_DECORATIONS;
			client.loadingStage = 1;
			client.minimapImage.method343();
		}
	},
	PARTICLES_SETTING("Particles") {
		@Override
		public String name(Client client) {
			return Settings.PARTICLES + setting;
		}

		public boolean status() {
			return Settings.PARTICLES;
		}

		@Override
		public void handle(Client client) {
			Settings.PARTICLES = !Settings.PARTICLES;
		}
	},
	MOVING_TEXTURE_SETTING("Moving Textures") {
		@Override
		public String name(Client client) {
			return Settings.MOVING_TEXTURE + setting;
		}

		public boolean status() {
			return Settings.MOVING_TEXTURE;
		}

		@Override
		public void handle(Client client) {
			Settings.MOVING_TEXTURE = !Settings.MOVING_TEXTURE;
		}
	},
	SMOOTH_SHADING_SETTING("Smooth Shading") {
		@Override
		public String name(Client client) {
			return Settings.SMOOTH_SHADING + setting;
		}

		public boolean status() {
			return Settings.SMOOTH_SHADING;
		}

		@Override
		public void handle(Client client) {
			Settings.SMOOTH_SHADING = !Settings.SMOOTH_SHADING;
		}
	},
	ENTITY_FEED_SETTING("Entity Feed") {
		@Override
		public String name(Client client) {
			return Settings.ENTITY_FEED + setting;
		}

		public boolean status() {
			return Settings.ENTITY_FEED;
		}

		@Override
		public void handle(Client client) {
			Settings.ENTITY_FEED = !Settings.ENTITY_FEED;
		}
	},
	KILL_FEED_SETTING("Kill Feed") {
		@Override
		public String name(Client client) {
			return Settings.DISPLAY_KILL_FEED + setting;
		}

		public boolean status() {
			return Settings.DISPLAY_KILL_FEED;
		}

		@Override
		public void handle(Client client) {
			Settings.DISPLAY_KILL_FEED = !Settings.DISPLAY_KILL_FEED;
		}
	},
	WIDGETS_SETTING("Widgets") {
		@Override
		public String name(Client client) {
			return Settings.WIDGET + setting;
		}

		public boolean status() {
			return Settings.WIDGET;
		}

		@Override
		public void handle(Client client) {
			Settings.WIDGET = !Settings.WIDGET;
		}
	},
	TWEENING_SETTING("Tweening") {
		@Override
		public String name(Client client) {
			return Settings.TWEENING + setting;
		}

		public boolean status() {
			return Settings.TWEENING;
		}

		@Override
		public void handle(Client client) {
			Settings.TWEENING = !Settings.TWEENING;
		}
	},
	//    ATTACK_PRIORITY_SETTING("Attack Priority") {
	//        @Override
	//        public String name(Client client) {
	//            return Settings.ATTACK_PRIORITY + setting;
	//        }
	//
	//        @Override
	//        public void handle(Client client) {
	//            Settings.ATTACK_PRIORITY = !Settings.ATTACK_PRIORITY;
	//        }
	//    },
	SHIFT_DROP_SETTING("Shift Drop") {
		@Override
		public String name(Client client) {
			return Settings.SHIFT_DROP + setting;
		}

		public boolean status() {
			return Settings.SHIFT_DROP;
		}

		@Override
		public void handle(Client client) {
			Settings.SHIFT_DROP = !Settings.SHIFT_DROP;
		}
	},
	MINIMAP_RANK_SETTING("Minimap Rank") {
		@Override
		public String name(Client client) {
			return Settings.MINIMAP_RANK + setting;
		}

		public boolean status() {
			return Settings.MINIMAP_RANK;
		}

		@Override
		public void handle(Client client) {
			Settings.MINIMAP_RANK = !Settings.MINIMAP_RANK;
		}
	},
	DISPLAY_NAME_SETTING("Display Names") {
		@Override
		public String name(Client client) {
			return Settings.DISPLAY_NAMES + setting;
		}

		public boolean status() {
			return Settings.DISPLAY_NAMES;
		}

		@Override
		public void handle(Client client) {
			Settings.DISPLAY_NAMES = !Settings.DISPLAY_NAMES;
		}
	},
	DISPLAY_CLAN_TAGS_SETTING("Display Clan Tags") {
		@Override
		public String name(Client client) {
			return Settings.DISPLAY_CLAN_TAG + setting;
		}

		public boolean status() {
			return Settings.DISPLAY_CLAN_TAG;
		}

		@Override
		public void handle(Client client) {
			Settings.DISPLAY_CLAN_TAG = !Settings.DISPLAY_CLAN_TAG;
		}
	},
	DISPLAY_GROUND_ITEMS_SETTING("Display Ground Items") {
		@Override
		public String name(Client client) {
			return Settings.DISPLAY_GROUND_ITEM + setting;
		}

		public boolean status() {
			return Settings.DISPLAY_GROUND_ITEM;
		}

		@Override
		public void handle(Client client) {
			Settings.DISPLAY_GROUND_ITEM = !Settings.DISPLAY_GROUND_ITEM;
		}
	},
	ITEM_RARITY_COLOR_SETTING("Ground Item Rarity") {
		@Override
		public String name(Client client) {
			return Settings.ITEM_RARITY_COLOR + setting;
		}

		public boolean status() {
			return Settings.ITEM_RARITY_COLOR;
		}

		@Override
		public void handle(Client client) {
			Settings.ITEM_RARITY_COLOR = !Settings.ITEM_RARITY_COLOR;
		}
	},
	SNOW_SETTING("Winter") {
		@Override
		public String name(Client client) {
			return Settings.SNOW + setting;
		}

		public boolean status() {
			return Settings.SNOW;
		}

		@Override
		public void handle(Client client) {
			Settings.SNOW = !Settings.SNOW;
			client.pushMessage("Reload your client for full effect!", false);
		}
	},;

	/**
	 * The setting name.
	 */
	public final String setting;

	/**
	 * Constructs a new <code>SettingData</code>.
	 */
	SettingData(String setting) {
		this.setting = setting;
	}

	/**
	 * Gets the setting data based off the ordinal of the setting.
	 */
	public static SettingData forOrdinal(int index) {
		for (SettingData data : values()) {
			if (data.ordinal() == index) {
				return data;
			}
		}
		return null;
	}
}