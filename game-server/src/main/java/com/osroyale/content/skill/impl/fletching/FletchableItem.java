package com.osroyale.content.skill.impl.fletching;

import com.osroyale.game.world.items.Item;

public final class FletchableItem {
	
	private final Item product;
	
	private final int level;
	
	private final  double experience;

	public FletchableItem(Item product, int level, double experience) {
		this.product = product;
		this.level = level;
		this.experience = experience;
	}

	public Item getProduct() {
		return product;
	}

	public int getLevel() {
		return level;
	}

	public double getExperience() {
		return experience;
	}
}