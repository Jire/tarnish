package com.osroyale.content.skill.impl.fletching;

import com.osroyale.game.world.items.Item;

public interface Fletchable {
	
	int getAnimation();

	int getGraphics();
	
	Item getUse();
	
	Item getWith();
	
	FletchableItem[] getFletchableItems();
	
	Item[] getIngredients();
	
	String getProductionMessage();
}