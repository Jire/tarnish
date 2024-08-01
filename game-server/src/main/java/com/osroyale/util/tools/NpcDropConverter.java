package com.osroyale.util.tools;

import com.osroyale.game.world.entity.mob.npc.drop.NpcDropTable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 2018-02-06.
 */
public class NpcDropConverter {
//
//    public static void main(String[] args) throws IOException {
////        int npcId = 6618;
//
//        List<NpcDropTable> drops = new ArrayList<>();
//
//		/* Always Drops */
//        drops.add(new ItemDrop(526, 1, 1, Rarity.ALWAYS));//Bones
//
//		/* Common Drops */
//        drops.add(new ItemDrop(1731, 1, 1, Rarity.COMMON)); //Amulet of power
//        drops.add(new ItemDrop(268, 4, 4, Rarity.COMMON)); //Dwarf weed
//        drops.add(new ItemDrop(443, 40, 40, Rarity.COMMON)); //Silver ore
//        drops.add(new ItemDrop(686, 1, 1, Rarity.COMMON)); //Rusty sword
//        drops.add(new ItemDrop(1624, 4, 4, Rarity.COMMON)); //Uncut sapphire
//        drops.add(new ItemDrop(995, 529, 4000, Rarity.COMMON)); //Coins
//        drops.add(new ItemDrop(385, 1, 3, Rarity.COMMON)); //Shark
//        drops.add(new ItemDrop(1622, 6, 6, Rarity.COMMON)); //Uncut emerald
//
//		/* Uncommon Drops */
//        drops.add(new ItemDrop(2501, 1, 1, Rarity.UNCOMMON)); //Red d'hide body
//        drops.add(new ItemDrop(868, 10, 10, Rarity.UNCOMMON)); //Rune knife
//        drops.add(new ItemDrop(4698, 30, 30, Rarity.UNCOMMON)); //Mud rune
//        drops.add(new ItemDrop(2298, 8, 8, Rarity.UNCOMMON)); //Anchovy pizza
//        drops.add(new ItemDrop(2, 150, 150, Rarity.UNCOMMON)); //Cannonball
//        drops.add(new ItemDrop(991, 1, 1, Rarity.UNCOMMON)); //Muddy key
//        drops.add(new ItemDrop(2434, 1, 1, Rarity.UNCOMMON)); //Prayer potion(4)
//        drops.add(new ItemDrop(1750, 10, 10, Rarity.UNCOMMON)); //Red dragonhide
//        drops.add(new ItemDrop(240, 10, 10, Rarity.UNCOMMON)); //White berries
//        drops.add(new ItemDrop(6705, 1, 1, Rarity.UNCOMMON)); //Potato with cheese
//        drops.add(new ItemDrop(6705, 1, 1, Rarity.UNCOMMON)); //Potato with cheese
//        drops.add(new ItemDrop(6705, 1, 1, Rarity.UNCOMMON)); //Potato with cheese
//        drops.add(new ItemDrop(9194, 12, 12, Rarity.UNCOMMON)); //Onyx bolt tips
//        drops.add(new ItemDrop(12746, 1, 1, Rarity.UNCOMMON)); //Mysterious emblem
//
//		/* Rare Drops */
//        drops.add(new ItemDrop(9185, 1, 1, Rarity.RARE)); //Rune crossbow
//        drops.add(new ItemDrop(9185, 1, 1, Rarity.RARE)); //Rune crossbow
//        drops.add(new ItemDrop(1373, 1, 1, Rarity.RARE)); //Rune battleaxe
//        drops.add(new ItemDrop(11212, 75, 75, Rarity.RARE)); //Dragon arrow
//        drops.add(new ItemDrop(560, 50, 50, Rarity.RARE)); //Death rune
//        drops.add(new ItemDrop(10976, 1, 1, Rarity.RARE)); //Long bone
//        drops.add(new ItemDrop(11932, 1, 1, Rarity.RARE)); //Malediction shard 2
//        drops.add(new ItemDrop(11929, 1, 1, Rarity.RARE)); //Odium shard 2
//
//        drops.add(new ItemDrop(995, 3000, 3000, Rarity.COMMON));//Coins
//        drops.add(new ItemDrop(1623, 1, 1, Rarity.COMMON));//Uncut sapphire
//        drops.add(new ItemDrop(1619, 1, 1, Rarity.UNCOMMON));//Uncut ruby
//        drops.add(new ItemDrop(1621, 1, 1, Rarity.UNCOMMON));//Uncut emerald
//        drops.add(new ItemDrop(1462, 1, 1, Rarity.UNCOMMON));//Nature talisman
//        drops.add(new ItemDrop(1452, 1, 1, Rarity.UNCOMMON));//Chaos talisman
//        drops.add(new ItemDrop(561, 67, 67, Rarity.UNCOMMON));//Nature rune
//        drops.add(new ItemDrop(2363, 1, 1, Rarity.UNCOMMON));//Runite bar
//        drops.add(new ItemDrop(1247, 1, 1, Rarity.RARE));//Rune spear
//        drops.add(new ItemDrop(1319, 1, 1, Rarity.RARE));//Rune 2h sword
//        drops.add(new ItemDrop(830, 5, 5, Rarity.RARE));//Rune javelin
//        drops.add(new ItemDrop(1201, 1, 1, Rarity.RARE));//Rune kiteshield
//        drops.add(new ItemDrop(892, 42, 42, Rarity.RARE));//Rune arrow
//        drops.add(new ItemDrop(1373, 1, 1, Rarity.RARE));//Rune battleaxe
//        drops.add(new ItemDrop(1617, 1, 1, Rarity.RARE));//Uncut diamond
//        drops.add(new ItemDrop(443, 100, 100, Rarity.RARE));//Silver ore
//        drops.add(new ItemDrop(829, 20, 20, Rarity.RARE));//Adamant javelin
//        drops.add(new ItemDrop(1185, 1, 1, Rarity.RARE));//Rune sq shield
//        drops.add(new ItemDrop(886, 150, 150, Rarity.RARE));//Steel arrow
//        drops.add(new ItemDrop(563, 45, 45, Rarity.RARE));//Law rune
//        drops.add(new ItemDrop(560, 45, 45, Rarity.RARE));//Death rune
//        drops.add(new ItemDrop(1615, 1, 1, Rarity.RARE));//Dragonstone
//        drops.add(new ItemDrop(1149, 1, 1, Rarity.RARE));//Dragon med helm
//        drops.add(new ItemDrop(1249, 1, 1, Rarity.RARE));//Dragon spear
//        drops.add(new ItemDrop(2366, 1, 1, Rarity.RARE));//Shield left half
//        drops.add(new ItemDrop(2368, 1, 1, Rarity.RARE));//Shield right half
//
//
//        List<ItemDrop> always = drops.stream().filter(item -> item.rarity == Rarity.ALWAYS).collect(Collectors.toList());
//        List<ItemDrop> common = drops.stream().filter(item -> item.rarity == Rarity.COMMON).collect(Collectors.toList());
//        List<ItemDrop> uncommon = drops.stream().filter(item -> item.rarity == Rarity.UNCOMMON).collect(Collectors.toList());
//        List<ItemDrop> rare = drops.stream().filter(item -> item.rarity == Rarity.RARE).collect(Collectors.toList());
//
//        System.out.println("	<ItemDropDefinition>");
//        System.out.println("		<id>" + npcId + "</id>");
//        if (always.isEmpty()) {
//            System.out.println("		<constant>null</constant>");
//        } else {
//            System.out.println("		<constant>");
//            System.out.println("			<scrolls>null</scrolls>");
//            System.out.println("			<charms>null</charms>");
//            System.out.println("			<drops>");
//            for (ItemDrop drop : always) {
//                System.out.println("				<itemDrop>");
//                System.out.println("					<id>" + drop.itemId + "</id>");
//                System.out.println("					<min>" + drop.min + "</min>");
//                System.out.println("					<max>" + drop.max + "</max>");
//                System.out.println("				</itemDrop>");
//            }
//            System.out.println("			</drops>");
//            System.out.println("		</constant>");
//        }
//        if (common.isEmpty()) {
//            System.out.println("		<common>null</common>");
//        } else {
//            System.out.println("		<common>");
//            System.out.println("			<scrolls>null</scrolls>");
//            System.out.println("			<charms>null</charms>");
//            System.out.println("			<drops>");
//            for (ItemDrop drop : common) {
//                System.out.println("				<itemDrop>");
//                System.out.println("					<id>" + drop.itemId + "</id>");
//                System.out.println("					<min>" + drop.min + "</min>");
//                System.out.println("					<max>" + drop.max + "</max>");
//                System.out.println("				</itemDrop>");
//            }
//            System.out.println("			</drops>");
//            System.out.println("		</common>");
//        }
//        if (uncommon.isEmpty()) {
//            System.out.println("		<uncommon>null</uncommon>");
//        } else {
//            System.out.println("		<uncommon>");
//            System.out.println("			<scrolls>null</scrolls>");
//            System.out.println("			<charms>null</charms>");
//            System.out.println("			<drops>");
//            for (ItemDrop drop : uncommon) {
//                System.out.println("				<itemDrop>");
//                System.out.println("					<id>" + drop.itemId + "</id>");
//                System.out.println("					<min>" + drop.min + "</min>");
//                System.out.println("					<max>" + drop.max + "</max>");
//                System.out.println("				</itemDrop>");
//            }
//            System.out.println("			</drops>");
//            System.out.println("		</uncommon>");
//        }
//        if (rare.isEmpty()) {
//            System.out.println("		<rare>null</rare>");
//        } else {
//            System.out.println("		<rare>");
//            System.out.println("			<scrolls>null</scrolls>");
//            System.out.println("			<charms>null</charms>");
//            System.out.println("			<drops>");
//            for (ItemDrop drop : rare) {
//                System.out.println("				<itemDrop>");
//                System.out.println("					<id>" + drop.itemId + "</id>");
//                System.out.println("					<min>" + drop.min + "</min>");
//                System.out.println("					<max>" + drop.max + "</max>");
//                System.out.println("				</itemDrop>");
//            }
//            System.out.println("			</drops>");
//            System.out.println("		</rare>");
//        }
//        System.out.println("		<useRareTable>" + !rare.isEmpty() + "</useRareTable>");
//        System.out.println("	</ItemDropDefinition>");
//
//    }
//
//    static class ItemDrop {
//        static enum Rarity {
//            ALWAYS,
//            COMMON,
//            UNCOMMON,
//            RARE;
//
//            public static Rarity get(String rarity) {
//                for (Rarity r : values()) {
//                    if (r.name().equals(rarity)) {
//                        return r;
//                    }
//                }
//                return UNCOMMON;
//            }
//        }
//
//        public final int itemId;
//        public final int min, max;
//        public final Rarity rarity;
//
//        public ItemDrop(int itemId, int min, int max, Rarity rarity) {
//            this.itemId = itemId;
//            this.min = min;
//            this.max = max;
//            this.rarity = rarity;
//        }
//
//        @Override
//        public String toString() {
//            return "[" + itemId + ", " + min + ", " + max + ", " + rarity.name() + "]";
//        }
//    }
}
