package com.osroyale.content.skill.impl.hunter.net;

import com.osroyale.game.world.items.Item;

import java.util.Arrays;
import java.util.Optional;

/**
 * Holds the impling rewards.
 * 
 * @author Daniel
 */
public enum ImplingReward {
	BABY(11238, 
			new Item(1755), new Item(1734), new Item(1733), new Item(946), new Item(1985), 
			new Item(2347), new Item(1759), new Item(1927), new Item(319), new Item(2007), 
			new Item(1779), new Item(7170), new Item(1438), new Item(2355), new Item(1607), 
			new Item(1743), new Item(379), new Item(1761)),
	YOUNG(11240,
			new Item(361), new Item(1902), new Item(1539), new Item(1524), new Item(7936),
			new Item(855), new Item(1353), new Item(2293), new Item(7178), new Item(247),
			new Item(453), new Item(1777), new Item(231), new Item(1761), new Item(8778),
			new Item(133), new Item(2359)),	
	GOURMENT(11242,
			new Item(365), new Item(361), new Item(2011), new Item(1897), new Item(2327),
			new Item(2007), new Item(5970), new Item(380, 4), new Item(7179, 3), new Item(386, 3),
			new Item(1883), new Item(3145, 3), new Item(5755), new Item(5406), new Item(10137, 5)),
	EARTH(11244,
			new Item(6033, 6), new Item(1440), new Item(5535), new Item(557, 32), new Item(1442),
			new Item(1784, 4), new Item(1273), new Item(447), new Item(1606, 2)),
	ESSENCE(11246,
			new Item(7936, 20), new Item(555, 30), new Item(556, 30), new Item(558, 25), new Item(559, 28),
			new Item(562, 4), new Item(1448), new Item(564, 4), new Item(563, 13), new Item(565, 7),
			new Item(566, 11)),
	ECLECTIC(11248,
			new Item(1273), new Item(5970), new Item(231), new Item(556, 35), new Item(8779, 4),
			new Item(1199), new Item(4527), new Item(444), new Item(2358, 5), new Item(7937, 25),
			new Item(237), new Item(2493), new Item(10083), new Item(1213), new Item(450, 10),
			new Item(5760, 2), new Item(7208), new Item(5321, 3), new Item(1391), new Item(1601)),
	NATURE(11250,
			new Item(5100), new Item(5104), new Item(5281), new Item(5294), new Item(6016),
			new Item(1513), new Item(254, 4), new Item(5313), new Item(5286), new Item(5285),
			new Item(3000), new Item(5974), new Item(5297), new Item(5299), new Item(5298, 5),
			new Item(5304), new Item(5295), new Item(270, 2), new Item(5303)),	
	MAGPIE(11252,
			new Item(1701, 3), new Item(1732, 3), new Item(2569, 3), new Item(3391), new Item(4097),
			new Item(5541), new Item(1747, 6), new Item(1347), new Item(2571, 4), new Item(4095),
			new Item(2364, 2), new Item(1215), new Item(1185), new Item(1602, 4), new Item(5287),
			new Item(987), new Item(985), new Item(993), new Item(5300)),	
	NINJA(11254,
			new Item(4097), new Item(3385), new Item(892, 70), new Item(140, 4), new Item(1748, 13),
			new Item(1113), new Item(1215), new Item(1333), new Item(1347), new Item(9342, 2),
			new Item(5938, 4), new Item(6156, 3), new Item(9194, 4), new Item(6313), new Item(805, 50)),		
	DRAGON(11256,
			new Item(11212, 50), new Item(9341, 10), new Item(1305), new Item(11232, 75),
			new Item(11237, 125),
			new Item(9193, 15), new Item(535, 55), new Item(1215, 1), new Item(11230, 35),
			new Item(5316),
			new Item(537, 15), new Item(1615, 1), new Item(1704, 1), new Item(5300, 2),
			new Item(7219, 5),
			new Item(4093), new Item(5547), new Item(1701, 3));
	
	private final int item;
	private final Item[] lootation;

	ImplingReward(int item, Item... lootation) {
		this.item = item;
		this.lootation = lootation;
	}
	
	public int getItem() {
		return item;
	}

	public Item[] getLootation() {
		return lootation;
	}
	
	public static Optional<ImplingReward> forId(int item) {
		return Arrays.stream(values()).filter(a -> a.item == item).findAny();
	}
}
