package com.osroyale.game.world.entity.combat.ranged;

import java.util.Arrays;

public class RangedWeaponDefinition {

	private final RangedWeaponType type;
	private final RangedAmmunition[] allowed;

	public RangedWeaponDefinition(RangedWeaponType type, RangedAmmunition... allowed) {
		this.type = type;
		this.allowed = allowed;
	}

	public int getSlot() {
		return type.slot;
	}

	public RangedWeaponType getType() {
		return type;
	}

	public boolean isValid(RangedAmmunition ammunition) {
		if (ammunition == null) return false;
		for (RangedAmmunition ammo : allowed) {
			if (ammo == ammunition) {
				return true;
			}
		}
		return false;
	}

	public RangedAmmunition[] getAllowed() {
		return allowed;
	}

	@Override
	public String toString() {
		return "RangedWeaponDefinition{" +
				"type=" + type +
				", allowed=" + Arrays.toString(allowed) +
				'}';
	}
}