package com.osroyale;

import java.util.HashMap;
import java.util.Map;

public enum WeaponInterface {
	STAFF(328),
	WARHAMMER(425, 7474, 7486),
	MAUL(425, 7474, 7486),
	GRANITE_MAUL(425, 7474, 7486),
	VERACS_FLAIL(3796, 7624, 7636),
	SCYTHE(776),
	BATTLEAXE(1698, 7499, 7511),
	GREATAXE(1698, 7499, 7511),
	CROSSBOW(1764, 7549, 7561),
	BALLISTA(1764, 7549, 7561),
	KARILS_CROSSBOW(1764, 7549, 7561),
	SHORTBOW(1764, 7549, 7561),
	LONGBOW(1764, 7549, 7561),
	DRAGON_DAGGER(2276, 7574, 7586),
	DAGGER(2276, 7574, 7586),
	SWORD(2276, 7574, 7586),
	SCIMITAR(2423, 7599, 7611),
	LONGSWORD(2423, 7599, 7611),
	MACE(3796, 7624, 7636),
	KNIFE(4446, 7649, 7661),
	OBBY_RINGS(4446, 7649, 7661),
	SPEAR(4679, 7674, 7686),
	TWO_HANDED_SWORD(4705, 7699, 7711),
	PICKAXE(5570),
	CLAWS(7762, 7800, 7812),
	HALBERD(8460, 8493, 8505),
	UNARMED(5855),
	WHIP(12290, 12323, 12335),
	THROWNAXE(4446, 7649, 7661),
	DART(4446, 7649, 7661),
	JAVELIN(4446, 7649, 7661),
	ANCIENT_STAFF(328),
	DARK_BOW(1764, 7549, 7561),
	GODSWORD(4705, 7699, 7711),
	SARADOMIN_SWORD(4705, 7699, 7711),
	ELDER_MAUL(425, 7474, 7486);

	private int interfaceId;
	private int specialBar;
	private int specialMeter;

	WeaponInterface(int interfaceId, int specialBar, int specialMeter) {
		this.interfaceId = interfaceId;
		this.specialBar = specialBar;
		this.specialMeter = specialMeter;
	}

	WeaponInterface(int interfaceId) {
		this(interfaceId, -1, -1);
	}

	public int getInterfaceId() {
		return interfaceId;
	}

	public int getSpecialBar() {
		return specialBar;
	}

	public int getSpecialMeter() {
		return specialMeter;
	}

	private static final Map<Integer, WeaponInterface> interfaces = new HashMap<>(WeaponInterface.values().length);

	public static WeaponInterface get(int interfaceId) {
		return interfaces.get(interfaceId);
	}

	static {
		for (WeaponInterface wep : WeaponInterface.values()) {
			interfaces.put(wep.getInterfaceId(), wep);
		}
	}

}