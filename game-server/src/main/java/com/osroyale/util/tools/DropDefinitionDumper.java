package com.osroyale.util.tools;

import com.osroyale.game.world.entity.mob.npc.definition.NpcDefinition;
import com.osroyale.util.tools.DropDefinitionDumper.ItemDrop.Rarity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DropDefinitionDumper {

	public static void main(String[] args) throws IOException {
		NpcDefinition.createParser().run();
		int npcId = 260;
		boolean rare_table = true;
		String name = NpcDefinition.get(npcId).getName();

		List<ItemDrop> drops = new ArrayList<>();

		drops.add(new ItemDrop(536, 1, 1, Rarity. ALWAYS));
		drops.add(new ItemDrop(1753, 1, 1, Rarity.ALWAYS));
		drops.add(new ItemDrop(209, 1, 1, Rarity.COMMON));
		drops.add(new ItemDrop(205, 1, 1, Rarity.COMMON));
		drops.add(new ItemDrop(199, 1, 1, Rarity.UNCOMMON));
		drops.add(new ItemDrop(211, 1, 1, Rarity.UNCOMMON));
		drops.add(new ItemDrop(215, 1, 1, Rarity.UNCOMMON));
		drops.add(new ItemDrop(201, 1, 1, Rarity.COMMON));
		drops.add(new ItemDrop(203, 1, 1, Rarity.UNCOMMON));
		drops.add(new ItemDrop(207, 1, 1, Rarity.UNCOMMON));
		drops.add(new ItemDrop(213, 1, 1, Rarity.UNCOMMON));
		drops.add(new ItemDrop(2485, 1, 1, Rarity.UNCOMMON));
		drops.add(new ItemDrop(217, 1, 1, Rarity.UNCOMMON));
		drops.add(new ItemDrop(1365, 1, 1, Rarity.COMMON));
		drops.add(new ItemDrop(20173, 1, 1, Rarity.COMMON));
		drops.add(new ItemDrop(1243, 1, 1, Rarity.UNCOMMON));
		drops.add(new ItemDrop(12297, 1, 1, Rarity.UNCOMMON));
		drops.add(new ItemDrop(20561, 1, 1, Rarity.UNCOMMON));
		drops.add(new ItemDrop(1213, 1, 1, Rarity.UNCOMMON));
		drops.add(new ItemDrop(995, 11, 440, Rarity.COMMON));
		drops.add(new ItemDrop(9691, 75, 75, Rarity.COMMON));
		drops.add(new ItemDrop(9699, 37, 37, Rarity.COMMON));
		drops.add(new ItemDrop(11693, 15, 15, Rarity.COMMON));
		drops.add(new ItemDrop(11695, 3, 3, Rarity.COMMON));
		drops.add(new ItemDrop(11941, 2, 2, Rarity.UNCOMMON));
		drops.add(new ItemDrop(449, 2, 2, Rarity.UNCOMMON));
		drops.add(new ItemDrop(2722, 1, 1, Rarity.RARE));



		if (rare_table) {
			drops.add(new ItemDrop(20527, 3000, 3000, Rarity.COMMON));
			drops.add(new ItemDrop(1623, 1, 1, Rarity.COMMON));
			drops.add(new ItemDrop(1619, 1, 1, Rarity.UNCOMMON));
			drops.add(new ItemDrop(1621, 1, 1, Rarity.UNCOMMON));
			drops.add(new ItemDrop(1462, 1, 1, Rarity.UNCOMMON));
			drops.add(new ItemDrop(1452, 1, 1, Rarity.UNCOMMON));
			drops.add(new ItemDrop(561, 67, 67, Rarity.UNCOMMON));
			drops.add(new ItemDrop(2363, 1, 1, Rarity.UNCOMMON));
			drops.add(new ItemDrop(1247, 1, 1, Rarity.RARE));
			drops.add(new ItemDrop(1319, 1, 1, Rarity.RARE));
			drops.add(new ItemDrop(830, 5, 5, Rarity.RARE));
			drops.add(new ItemDrop(1201, 1, 1, Rarity.RARE));
			drops.add(new ItemDrop(892, 42, 42, Rarity.RARE));
			drops.add(new ItemDrop(1373, 1, 1, Rarity.RARE));
			drops.add(new ItemDrop(1617, 1, 1, Rarity.RARE));
			drops.add(new ItemDrop(443, 100, 100, Rarity.RARE));
			drops.add(new ItemDrop(829, 20, 20, Rarity.RARE));
			drops.add(new ItemDrop(1185, 1, 1, Rarity.RARE));
			drops.add(new ItemDrop(886, 150, 150, Rarity.RARE));
			drops.add(new ItemDrop(563, 45, 45, Rarity.RARE));
			drops.add(new ItemDrop(560, 45, 45, Rarity.RARE));
			drops.add(new ItemDrop(1615, 1, 1, Rarity.RARE));
			drops.add(new ItemDrop(1149, 1, 1, Rarity.RARE));
			drops.add(new ItemDrop(2368, 1, 1, Rarity.RARE));
			drops.add(new ItemDrop(1249, 1, 1, Rarity.VERY_RARE));
			drops.add(new ItemDrop(2366, 1, 1, Rarity.VERY_RARE));
		}

		List<ItemDrop> always = drops.stream().filter(item -> item.rarity == Rarity.ALWAYS).collect(Collectors.toList());
		List<ItemDrop> common = drops.stream().filter(item -> item.rarity == Rarity.COMMON).collect(Collectors.toList());
		List<ItemDrop> uncommon = drops.stream().filter(item -> item.rarity == Rarity.UNCOMMON).collect(Collectors.toList());
		List<ItemDrop> rare = drops.stream().filter(item -> item.rarity == Rarity.RARE).collect(Collectors.toList());
		List<ItemDrop> very_rare = drops.stream().filter(item -> item.rarity == Rarity.VERY_RARE).collect(Collectors.toList());

		System.out.println("   {");
		System.out.println("    \"name\": \"" + name + "\",");
		System.out.println("    \"npc\": [");
		System.out.println("      " + npcId);
		System.out.println("    ],");
		System.out.println("  \"drop\": [");

		if (!always.isEmpty()) {
			for (int index = 0; index < always.size(); index++) {
				ItemDrop item = always.get(index);

				System.out.println("   {");
				System.out.println("    \"id\": " + item.id + ",");
				System.out.println("    \"minimum\": " + item.min + ",");
				System.out.println("    \"maximum\": " + item.max + ",");
				System.out.println("    \"chance\": " + item.rarity + "");

				if (index + 1 == always.size()) {
					System.out.println("   }");
				} else {
					System.out.println("   },");
				}
			}
		}

		if (!common.isEmpty()) {
			for (int index = 0; index < common.size(); index++) {
				ItemDrop item = common.get(index);

				System.out.println("   {");
				System.out.println("    \"id\": " + item.id + ",");
				System.out.println("    \"minimum\": " + item.min + ",");
				System.out.println("    \"maximum\": " + item.max + ",");
				System.out.println("    \"chance\": " + item.rarity + "");

				if (index + 1 == common.size() && uncommon.isEmpty() && rare.isEmpty() && very_rare.isEmpty()) {
					System.out.println("   }");
				} else {
					System.out.println("   },");
				}
			}
		}

		if (!uncommon.isEmpty()) {
			for (int index = 0; index < uncommon.size(); index++) {
				ItemDrop item = uncommon.get(index);

				System.out.println("   {");
				System.out.println("    \"id\": " + item.id + ",");
				System.out.println("    \"minimum\": " + item.min + ",");
				System.out.println("    \"maximum\": " + item.max + ",");
				System.out.println("    \"chance\": " + item.rarity + "");

				if (index + 1 == uncommon.size() && rare.isEmpty() && very_rare.isEmpty()) {
					System.out.println("   }");
				} else {
					System.out.println("   },");
				}
			}
		}

		if (!rare.isEmpty()) {
			for (int index = 0; index < rare.size(); index++) {
				ItemDrop item = rare.get(index);

				System.out.println("   {");
				System.out.println("    \"id\": " + item.id + ",");
				System.out.println("    \"minimum\": " + item.min + ",");
				System.out.println("    \"maximum\": " + item.max + ",");
				System.out.println("    \"chance\": " + item.rarity + "");

				if (index + 1 == rare.size() && very_rare.isEmpty()) {
					System.out.println("   }");
				} else {
					System.out.println("   },");
				}
			}
		}

		if (!very_rare.isEmpty()) {
			for (int index = 0; index < very_rare.size(); index++) {
				ItemDrop item = very_rare.get(index);

				System.out.println("   {");
				System.out.println("    \"id\": " + item.id + ",");
				System.out.println("    \"minimum\": " + item.min + ",");
				System.out.println("    \"maximum\": " + item.max + ",");
				System.out.println("    \"chance\": " + item.rarity + "");

				if (index + 1 == very_rare.size()) {
					System.out.println("   }");
				} else {
					System.out.println("   },");
				}
			}
		}

		System.out.println("  ]");
		System.out.println("},");

	}

	static class ItemDrop {
		enum Rarity {
			ALWAYS,
			COMMON,
			UNCOMMON,
			RARE,
			VERY_RARE;

			public static Rarity get(String rarity) {
				for (Rarity r : values()) {
					if (r.name().equals(rarity)) {
						return r;
					}
				}
				return UNCOMMON;
			}
		}

		public final int id;
		public final int min, max;
		public final Rarity rarity;

		public ItemDrop(int id, int min, int max, Rarity rarity) {
			this.id = id;
			this.min = min;
			this.max = max;
			this.rarity = rarity;
		}

		@Override
		public String toString() {
			return "[" + id + ", " + min + ", " + max + ", " + rarity.name() + "]";
		}
	}
}