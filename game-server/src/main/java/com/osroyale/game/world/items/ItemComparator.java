package com.osroyale.game.world.items;

import com.osroyale.game.world.items.containers.pricechecker.PriceType;

import java.util.Comparator;

public enum ItemComparator implements Comparator<Item> {
	HIGH_ALCH_COMPARATOR,
	LOW_ALCH_COMPARATOR,
	ITEM_WEIGHT_COMPARATOR,
	SHOP_VALUE_COMPARATOR,
	ITEM_ID_COMPARATOR,
	ITEM_AMOUNT_COMPARATOR;

	@Override
	public int compare(Item first, Item second) {
		double firstValue = 0;
		double secondValue = 0;

		if (first == null && second == null) {
			return 0;
		}

		if (first == null) {
			return 1;
		}

		if (second == null) {
			return -1;
		}

		switch (this) {

		case HIGH_ALCH_COMPARATOR:
			firstValue = first.getValue(PriceType.HIGH_ALCH_VALUE);
			secondValue = second.getValue(PriceType.HIGH_ALCH_VALUE);
			break;

		case ITEM_AMOUNT_COMPARATOR:
			firstValue = first.getAmount();
			secondValue = second.getAmount();
			break;

		case ITEM_ID_COMPARATOR:
			firstValue = first.getId();
			secondValue = second.getId();
			break;

		case ITEM_WEIGHT_COMPARATOR:
			firstValue = first.getWeight();
			secondValue = second.getWeight();
			break;

		case LOW_ALCH_COMPARATOR:
			firstValue = first.getLowAlch();
			secondValue = second.getLowAlch();
			break;

		case SHOP_VALUE_COMPARATOR:
			firstValue = first.getValue(PriceType.VALUE);
			secondValue = second.getValue(PriceType.VALUE);
			break;

		}

		return Integer.signum((int) (secondValue - firstValue));
	}
}
