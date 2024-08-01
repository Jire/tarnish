package com.osroyale.content.skill.impl.herblore;

import com.osroyale.game.world.items.Item;

import java.util.Arrays;

public enum UnfinishedPotion implements Potion {
	GUAM_POTION(91, 249, 1),
	MARRENTILL_POTION(93, 251, 5),
	TARROMIN_POTION(95, 253, 12),
	HARRALANDER_POTION(97, 255, 22),
	RANARR_POTION(99, 257, 30),
	TOADFLAX_POTION(3002, 2998, 34),
	SPIRIT_WEED_POTION(12181, 12172, 40),
	IRIT_POTION(101, 259, 45),
	WERGALI_POTION(14856, 14854, 1),
	AVANTOE_POTION(103, 261, 50),
	KWUARM_POTION(105, 263, 55),
	SNAPDRAGON_POTION(3004, 3000, 63),
	CADANTINE_POTION(107, 265, 66),
	LANTADYME(2483, 2481, 69),
	DWARF_WEED_POTION(109, 267, 72),
	TORSTOL_POTION(111, 269, 78);

	private final int product;
	private final Item[] ingredients;
	private final int level;

	UnfinishedPotion(int product, int ingredient, int level) {
		this.product = product;
		this.ingredients = new Item[] { new Item(227), new Item(ingredient) };
		this.level = level;
	}
	
	public static UnfinishedPotion get(Item use, Item with) {
		for (final UnfinishedPotion potion : values()) {
			if (potion.ingredients[0].equals(use) && potion.ingredients[1].equals(with) || potion.ingredients[1].equals(use) && potion.ingredients[0].equals(with)) {
				return potion;
			}
		}
		return null;
	}

	@Override
	public int getAnimation() {
		return -1;
	}

	@Override
	public double getExperience() {
		return 0;
	}

	@Override
	public Item[] getIngredients() {
		return Arrays.copyOf(ingredients, ingredients.length);
	}

	@Override
	public int getLevel() {
		return level;
	}

	@Override
	public Item getProduct() {
		return new Item(product);
	}

	@Override
	public String toString() {
		return "UnfinishedPotion[product: " + getProduct() + ", level: " + level + ", ingredients: " + Arrays.toString(ingredients) + "]";
	}
}