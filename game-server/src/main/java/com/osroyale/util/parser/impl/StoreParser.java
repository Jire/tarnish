package com.osroyale.util.parser.impl;

import com.google.gson.JsonObject;
import com.osroyale.content.store.SellType;
import com.osroyale.content.store.Store;
import com.osroyale.content.store.StoreItem;
import com.osroyale.content.store.currency.CurrencyType;
import com.osroyale.content.store.impl.DefaultStore;
import com.osroyale.util.parser.GsonParser;

import java.util.*;

//import com.osroyale.content.shop.Store;
//import com.osroyale.content.shop.StoreItem;
//import com.osroyale.content.shop.StoreRepository;

/**
 * Parses throug the shops files and creates in-game shop object for the game
 * on startup.
 *
 * @author Daniel | Obey
 */
public class StoreParser extends GsonParser {

	public StoreParser() {
		super("def/store/stores", false);
	}

	@Override
	protected void parse(JsonObject data) {
		final String name = Objects.requireNonNull(data.get("name").getAsString());
		final CurrencyType currency = builder.fromJson(data.get("currency"), CurrencyType.class);
		final boolean restock = data.get("restock").getAsBoolean();
		final String sellType = data.get("sellType").getAsString().toUpperCase();
		final LoadedItem[] loadedItems = builder.fromJson(data.get("items"), LoadedItem[].class);

		final List<StoreItem> storeItems = new ArrayList<>(loadedItems.length);

		for(LoadedItem loadedItem : loadedItems) {
			if (loadedItem.value == 0) {
				loadedItem.value = -1;
			}
			storeItems.add(new StoreItem(loadedItem.id, loadedItem.amount, OptionalInt.of(loadedItem.value), Optional.ofNullable(loadedItem.type)));
		}

		StoreItem[] items = storeItems.toArray(new StoreItem[storeItems.size()]);
		Store.STORES.put(name, new DefaultStore(items, name, SellType.valueOf(sellType), restock, currency));
	}

	private static final class LoadedItem {

		private final int id;

		private final int amount;

		private int value;

		private final CurrencyType type;

		public LoadedItem(int id, int amount, int value, CurrencyType type) {
			this.id = id;
			this.amount = amount;
			this.value = value;
			this.type = type;
		}
	}

}
