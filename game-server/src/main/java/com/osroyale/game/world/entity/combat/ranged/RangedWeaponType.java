package com.osroyale.game.world.entity.combat.ranged;

import com.osroyale.game.world.items.containers.equipment.Equipment;

public enum RangedWeaponType {
	SHOT(Equipment.ARROWS_SLOT),
	THROWN(Equipment.WEAPON_SLOT);
	
	final int slot;
	
	RangedWeaponType(int slot) {
		this.slot = slot;
	}
	
	public int getSlot() {
		return slot;
	}
}
