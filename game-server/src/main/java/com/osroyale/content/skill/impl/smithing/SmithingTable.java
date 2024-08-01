package com.osroyale.content.skill.impl.smithing;

import com.osroyale.game.world.items.Item;
import com.osroyale.util.StringUtils;

public interface SmithingTable {

    /**
     * The item identification for the bar used to forge the table
     * of items.
     * @return the item object.
     */
    Item getBar();

    /**
     * The level requirement for smithing this bar.
     * @return the numerical value.
     */
    int getLevelRequirement();

    /**
     * The experience gained upon smithing this bar.
     * @return the numerical value.
     */
    double getExperience();

    /**
     * The bars required for smithing this bar.
     * @return the numerical value.
     */
    int getBarsRequired();

    /**
     * The produced item for smithing this bar.
     * @return the item object.
     */
    Item getProduced();

    /**
     * The name of the producable item.
     * @return the alphabetic value.
     */
    String getName();

    /**
     * The table for the bronze items.
     */
    enum BronzeTable implements SmithingTable {
        DAGGER(1, 12.5, 1, new Item(1205)),
        HATCHET(1, 12.5, 1, new Item(1351)),
        CHAIN_BODY(11, 37.5, 3, new Item(1103)),
        MEDIUM_HELM(3, 12.5, 1, new Item(1139)),
        KNIVES(7, 12.5, 1, new Item(864, 5)),
        SWORD(4, 12.5, 1, new Item(1277)),
        MACE(2, 12.5, 1, new Item(1422)),
        PLATELEGS(16, 37.5, 3, new Item(1075)),
        FULL_HELM(7, 25, 2, new Item(1155)),
        DART_TIPS(4, 12.5, 1, new Item(819, 10)),
        SCIMITAR(5, 25, 2, new Item(1321)),
        WARHAMMER(9, 37.5, 3, new Item(1337)),
        PLATE_SKIRT(16, 37.5, 3, new Item(1087)),
        SQUARE_SHIELD(8, 25, 2, new Item(1173)),
        BOLTS(5, 12.5, 1, new Item(9375, 10)),
        LONGSWORD(6, 25, 2, new Item(1291)),
        BATTLE_AXE(10, 37.5, 3, new Item(1375)),
        PLATE_BODY(18, 62.5, 5, new Item(1117)),
        KITE_SHIELD(12, 37.5, 3, new Item(1189)),
        NAILS(4, 12.5, 1, new Item(4819, 15)),
        TWO_HANDED_SWORD(14, 37.5, 3, new Item(1307), "2 hand sword"),
        CLAWS(13, 25, 2, new Item(3095)),
        LIMBS(6, 12.5, 1, new Item(9420)),
        BOOTS(4, 12.5, 2, new Item(4119)),
        //WIRE(4, 12.5, 1, new Item(1794)),
        ARROWTIPS(5, 12.5, 1, new Item(39, 15));

        /**
         * The requirement required to smith this item.
         */
        private final int requirement;

        /**
         * The experience gained for smithing this item.
         */
        private final double experience;

        /**
         * The bars required for smithing this item.
         */
        private final int barsRequired;

        /**
         * The produced item for smithing this bar.
         */
        private final Item produced;

        /**
         * The name of this producable item.
         */
        private final String name;

        /**
         * Constructs a new {@link BronzeTable}.
         * @param requirement  {@link #requirement}.
         * @param experience   {@link #experience}.
         * @param barsRequired {@link #barsRequired}.
         * @param produced     {@link #produced}.
         * @param name         {@link #name}.
         */
        BronzeTable(int requirement, double experience, int barsRequired, Item produced, String name) {
            this.requirement = requirement;
            this.experience = experience;
            this.barsRequired = barsRequired;
            this.produced = produced;
            this.name = name;
        }

        /**
         * Constructs a new {@link BronzeTable}.
         * @param requirement  {@link #requirement}.
         * @param experience   {@link #experience}.
         * @param barsRequired {@link #barsRequired}.
         * @param produced     {@link #produced}.
         */
        BronzeTable(int requirement, double experience, int barsRequired, Item produced) {
            this.requirement = requirement;
            this.experience = experience;
            this.barsRequired = barsRequired;
            this.produced = produced;
            this.name = StringUtils.capitalize(toString().replace("_", " "));
        }

        @Override
        public Item getBar() {
            return new Item(2349);
        }

        @Override
        public int getLevelRequirement() {
            return requirement;
        }

        @Override
        public double getExperience() {
            return experience;
        }

        @Override
        public int getBarsRequired() {
            return barsRequired;
        }

        @Override
        public Item getProduced() {
            return produced;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    /**
     * The table for the iron items.
     */
    enum IronTable implements SmithingTable {
        DAGGER(15, 25, 1, new Item(1203)),
        HATCHET(16, 25, 1, new Item(1349)),
        CHAIN_BODY(26, 37.5, 3, new Item(1101)),
        MEDIUM_HELM(18, 25, 1, new Item(1137)),
        KNIVES(22, 25, 1, new Item(863, 5)),
        SWORD(19, 25, 1, new Item(1279)),
        MACE(17, 25, 1, new Item(1420)),
        PLATELEGS(31, 75, 3, new Item(1067)),
        FULL_HELM(22, 50, 2, new Item(1153)),
        DART_TIPS(19, 25, 1, new Item(820, 10)),
        SCIMITAR(20, 50, 2, new Item(1323)),
        WARHAMMER(24, 75, 3, new Item(1335)),
        PLATE_SKIRT(31, 75, 3, new Item(1081)),
        SQUARE_SHIELD(23, 50, 2, new Item(1175)),
        BOLTS(20, 25, 1, new Item(9377, 10)),
        LONGSWORD(21, 50, 2, new Item(1293)),
        BATTLE_AXE(25, 75, 3, new Item(1363)),
        PLATE_BODY(33, 125, 5, new Item(1115)),
        KITE_SHIELD(27, 75, 3, new Item(1191)),
        NAILS(19, 25, 1, new Item(4820, 15)),
        TWO_HANDED_SWORD(29, 75, 3, new Item(1309), "2 hand sword"),
        CLAWS(28, 50, 2, new Item(3096)),
        OIL_LANTERN_FRAME(26, 25, 1, new Item(4540)),
        LIMBS(23, 25, 1, new Item(9423)),
//        CANNON_SHOTS(60, 80, 3, new Item(15243, 10)),
		ARROWTIPS(20, 25, 1, new Item(40, 15));

        /**
         * The requirement required to smith this item.
         */
        private final int requirement;

        /**
         * The experience gained for smithing this item.
         */
        private final double experience;

        /**
         * The bars required for smithing this item.
         */
        private final int barsRequired;

        /**
         * The produced item for smithing this bar.
         */
        private final Item produced;

        /**
         * The name of this producable item.
         */
        private final String name;

        /**
         * Constructs a new {@link IronTable}.
         * @param requirement  {@link #requirement}.
         * @param experience   {@link #experience}.
         * @param barsRequired {@link #barsRequired}.
         * @param produced     {@link #produced}.
         * @param name         {@link #name}.
         */
        IronTable(int requirement, double experience, int barsRequired, Item produced, String name) {
            this.requirement = requirement;
            this.experience = experience;
            this.barsRequired = barsRequired;
            this.produced = produced;
            this.name = name;
        }

        /**
         * Constructs a new {@link IronTable}.
         * @param requirement  {@link #requirement}.
         * @param experience   {@link #experience}.
         * @param barsRequired {@link #barsRequired}.
         * @param produced     {@link #produced}.
         */
        IronTable(int requirement, double experience, int barsRequired, Item produced) {
            this.requirement = requirement;
            this.experience = experience;
            this.barsRequired = barsRequired;
            this.produced = produced;
            this.name = StringUtils.capitalize(toString().replace("_", " "));
        }

        @Override
        public Item getBar() {
            return new Item(2351);
        }

        @Override
        public int getLevelRequirement() {
            return requirement;
        }

        @Override
        public double getExperience() {
            return experience;
        }

        @Override
        public int getBarsRequired() {
            return barsRequired;
        }

        @Override
        public Item getProduced() {
            return produced;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    /**
     * The table for the steel items.
     */
    enum SteelTable implements SmithingTable {
        DAGGER(30, 37.5, 1, new Item(1207)),
        HATCHET(31, 37.5, 1, new Item(1353)),
        CHAIN_BODY(41, 112.5, 3, new Item(1105)),
        MEDIUM_HELM(33, 37.5, 1, new Item(1141)),
        KNIVES(37, 37.5, 1, new Item(865, 5)),
        SWORD(34, 37.5, 1, new Item(1281)),
        MACE(32, 37.5, 1, new Item(1424)),
        PLATELEGS(46, 112.5, 3, new Item(1069)),
        FULL_HELM(37, 75, 2, new Item(1157)),
        DART_TIPS(34, 37.5, 1, new Item(821, 10)),
        SCIMITAR(35, 75, 2, new Item(1325)),
        WARHAMMER(40, 112.5, 3, new Item(1339)),
        PLATE_SKIRT(46, 112.5, 3, new Item(1083)),
        SQUARE_SHIELD(38, 75, 2, new Item(1177)),
        CANNONBALLS(35, 25.5, 1, new Item(2, 4)),
        LONGSWORD(36, 75, 2, new Item(1295)),
        BATTLE_AXE(40, 112.5, 3, new Item(1365)),
        PLATE_BODY(48, 187.5, 5, new Item(1119)),
        KITE_SHIELD(42, 112.5, 3, new Item(1193)),
        NAILS(34, 37.5, 1, new Item(1539, 15)),
        TWO_HANDED_SWORD(44, 112.5, 3, new Item(1311), "2 hand sword"),
        CLAWS(43, 75, 2, new Item(3097)),
        STUDS(36, 37.5, 1, new Item(2370)),
        LIMBS(36, 37.5, 1, new Item(9425)),
        ARROWTIPS(35, 37.5, 1, new Item(41, 15));

        /**
         * The requirement required to smith this item.
         */
        private final int requirement;

        /**
         * The experience gained for smithing this item.
         */
        private final double experience;

        /**
         * The bars required for smithing this item.
         */
        private final int barsRequired;

        /**
         * The produced item for smithing this bar.
         */
        private final Item produced;

        /**
         * The name of this producable item.
         */
        private final String name;

        /**
         * Constructs a new {@link SteelTable}.
         * @param requirement  {@link #requirement}.
         * @param experience   {@link #experience}.
         * @param barsRequired {@link #barsRequired}.
         * @param produced     {@link #produced}.
         * @param name         {@link #name}.
         */
        SteelTable(int requirement, double experience, int barsRequired, Item produced, String name) {
            this.requirement = requirement;
            this.experience = experience;
            this.barsRequired = barsRequired;
            this.produced = produced;
            this.name = name;
        }

        /**
         * Constructs a new {@link SteelTable}.
         * @param requirement  {@link #requirement}.
         * @param experience   {@link #experience}.
         * @param barsRequired {@link #barsRequired}.
         * @param produced     {@link #produced}.
         */
        SteelTable(int requirement, double experience, int barsRequired, Item produced) {
            this.requirement = requirement;
            this.experience = experience;
            this.barsRequired = barsRequired;
            this.produced = produced;
            this.name = StringUtils.capitalize(toString().replace("_", " "));
        }

        @Override
        public Item getBar() {
            return new Item(2353);
        }

        @Override
        public int getLevelRequirement() {
            return requirement;
        }

        @Override
        public double getExperience() {
            return experience;
        }

        @Override
        public int getBarsRequired() {
            return barsRequired;
        }

        @Override
        public Item getProduced() {
            return produced;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    /**
     * The table for the mithril items.
     */
    enum MithrilTable implements SmithingTable {
        DAGGER(50, 50, 1, new Item(1209)),
        HATCHET(51, 50, 1, new Item(1355)),
        CHAIN_BODY(61, 150, 3, new Item(1109)),
        MEDIUM_HELM(53, 50, 1, new Item(1143)),
        KNIVES(57, 50, 1, new Item(866, 5)),
        SWORD(54, 50, 1, new Item(1285)),
        MACE(52, 50, 1, new Item(1428)),
        PLATELEGS(66, 150, 3, new Item(1071)),
        FULL_HELM(57, 100, 2, new Item(1159)),
        DART_TIPS(54, 50, 1, new Item(822, 10)),
        SCIMITAR(55, 100, 2, new Item(1329)),
        WARHAMMER(59, 150, 3, new Item(1343)),
        PLATE_SKIRT(66, 150, 3, new Item(1085)),
        SQUARE_SHIELD(58, 100, 2, new Item(1181)),
        BOLTS(55, 50, 1, new Item(9379, 10)),
        LONGSWORD(56, 100, 2, new Item(1299)),
        BATTLE_AXE(60, 150, 3, new Item(1369)),
        PLATE_BODY(68, 175, 5, new Item(1121)),
        KITE_SHIELD(62, 150, 3, new Item(1197)),
        NAILS(54, 50, 1, new Item(4822, 15)),
        TWO_HANDED_SWORD(64, 150, 3, new Item(1315), "2 hand sword"),
        CLAWS(63, 150, 2, new Item(3099)),
        MITH_GRAPPLE(59, 50, 1, new Item(9416)),
        LIMBS(56, 50, 1, new Item(9427)),
        ARROWTIPS(55, 50, 1, new Item(42, 15));

        /**
         * The requirement required to smith this item.
         */
        private final int requirement;

        /**
         * The experience gained for smithing this item.
         */
        private final double experience;

        /**
         * The bars required for smithing this item.
         */
        private final int barsRequired;

        /**
         * The produced item for smithing this bar.
         */
        private final Item produced;

        /**
         * The name of this producable item.
         */
        private final String name;

        /**
         * Constructs a new {@link MithrilTable}.
         * @param requirement  {@link #requirement}.
         * @param experience   {@link #experience}.
         * @param barsRequired {@link #barsRequired}.
         * @param produced     {@link #produced}.
         * @param name         {@link #name}.
         */
        MithrilTable(int requirement, double experience, int barsRequired, Item produced, String name) {
            this.requirement = requirement;
            this.experience = experience;
            this.barsRequired = barsRequired;
            this.produced = produced;
            this.name = name;
        }

        /**
         * Constructs a new {@link MithrilTable}.
         * @param requirement  {@link #requirement}.
         * @param experience   {@link #experience}.
         * @param barsRequired {@link #barsRequired}.
         * @param produced     {@link #produced}.
         */
        MithrilTable(int requirement, double experience, int barsRequired, Item produced) {
            this.requirement = requirement;
            this.experience = experience;
            this.barsRequired = barsRequired;
            this.produced = produced;
            this.name = StringUtils.capitalize(toString().replace("_", " "));
        }

        @Override
        public Item getBar() {
            return new Item(2359);
        }

        @Override
        public int getLevelRequirement() {
            return requirement;
        }

        @Override
        public double getExperience() {
            return experience;
        }

        @Override
        public int getBarsRequired() {
            return barsRequired;
        }

        @Override
        public Item getProduced() {
            return produced;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    /**
     * The table for the adamant items.
     */
    enum AdamantTable implements SmithingTable {
        DAGGER(70, 62.5, 1, new Item(1211)),
        HATCHET(71, 62.5, 1, new Item(1357)),
        CHAIN_BODY(81, 187.5, 3, new Item(1111)),
        MEDIUM_HELM(73, 62.5, 1, new Item(1145)),
        KNIVES(77, 62.5, 1, new Item(867, 5)),
        SWORD(74, 62.5, 1, new Item(1287)),
        MACE(72, 62.5, 1, new Item(1430)),
        PLATELEGS(86, 187.5, 3, new Item(1073)),
        FULL_HELM(77, 125, 2, new Item(1161)),
        DART_TIPS(74, 62.5, 1, new Item(823, 10)),
        SCIMITAR(75, 125, 2, new Item(1331)),
        WARHAMMER(79, 187.5, 3, new Item(1345)),
        PLATE_SKIRT(86, 187.5, 3, new Item(1091)),
        SQUARE_SHIELD(78, 125, 2, new Item(1183)),
        BOLTS(75, 62.5, 1, new Item(9380, 10)),
        LONGSWORD(76, 125, 2, new Item(1301)),
        BATTLE_AXE(80, 187.5, 3, new Item(1371)),
        PLATE_BODY(88, 312.5, 5, new Item(1123)),
        KITE_SHIELD(82, 187.5, 3, new Item(1199)),
        NAILS(74, 62.5, 1, new Item(4823, 15)),
        TWO_HANDED_SWORD(84, 187.5, 3, new Item(1317), "2 hand sword"),
        CLAWS(83, 125, 2, new Item(3100)),
        BOOTS(77, 125, 2, new Item(4129)),
        LIMBS(76, 62.5, 1, new Item(9429)),
        ARROWTIPS(75, 62.5, 1, new Item(43, 15));

        /**
         * The requirement required to smith this item.
         */
        private final int requirement;

        /**
         * The experience gained for smithing this item.
         */
        private final double experience;

        /**
         * The bars required for smithing this item.
         */
        private final int barsRequired;

        /**
         * The produced item for smithing this bar.
         */
        private final Item produced;

        /**
         * The name of this producable item.
         */
        private final String name;

        /**
         * Constructs a new {@link AdamantTable}.
         * @param requirement  {@link #requirement}.
         * @param experience   {@link #experience}.
         * @param barsRequired {@link #barsRequired}.
         * @param produced     {@link #produced}.
         * @param name         {@link #name}.
         */
        AdamantTable(int requirement, double experience, int barsRequired, Item produced, String name) {
            this.requirement = requirement;
            this.experience = experience;
            this.barsRequired = barsRequired;
            this.produced = produced;
            this.name = name;
        }

        /**
         * Constructs a new {@link AdamantTable}.
         * @param requirement  {@link #requirement}.
         * @param experience   {@link #experience}.
         * @param barsRequired {@link #barsRequired}.
         * @param produced     {@link #produced}.
         */
        AdamantTable(int requirement, double experience, int barsRequired, Item produced) {
            this.requirement = requirement;
            this.experience = experience;
            this.barsRequired = barsRequired;
            this.produced = produced;
            this.name = StringUtils.capitalize(toString().replace("_", " "));
        }

        @Override
        public Item getBar() {
            return new Item(2361);
        }

        @Override
        public int getLevelRequirement() {
            return requirement;
        }

        @Override
        public double getExperience() {
            return experience;
        }

        @Override
        public int getBarsRequired() {
            return barsRequired;
        }

        @Override
        public Item getProduced() {
            return produced;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    /**
     * The table for the rune items.
     */
    enum RuniteTable implements SmithingTable {
        DAGGER(85, 75, 1, new Item(1213)),
        HATCHET(86, 75, 1, new Item(1359)),
        CHAIN_BODY(96, 225, 3, new Item(1113)),
        MEDIUM_HELM(88, 75, 1, new Item(1147)),
        KNIVES(92, 75, 1, new Item(868, 5)),
        SWORD(89, 75, 1, new Item(1289)),
        MACE(87, 75, 1, new Item(1432)),
        PLATELEGS(99, 225, 3, new Item(1079)),
        FULL_HELM(92, 150, 2, new Item(1163)),
        DART_TIPS(89, 75, 1, new Item(824, 10)),
        SCIMITAR(90, 150, 2, new Item(1333)),
        WARHAMMER(94, 225, 3, new Item(1347)),
        PLATE_SKIRT(99, 225, 3, new Item(1093)),
        SQUARE_SHIELD(93, 150, 2, new Item(1185)),
        BOLTS(90, 75, 1, new Item(9381, 10)),
        LONGSWORD(91, 150, 2, new Item(1303)),
        BATTLE_AXE(95, 225, 3, new Item(1373)),
        PLATE_BODY(99, 375, 5, new Item(1127)),
        KITE_SHIELD(97, 225, 3, new Item(1201)),
        NAILS(89, 75, 1, new Item(4824, 15)),
        TWO_HANDED_SWORD(99, 225, 3, new Item(1319), "2 hand sword"),
        CLAWS(98, 150, 2, new Item(3101)),
        BOOTS(92, 150, 2, new Item(4131)),
        LIMBS(91, 75, 1, new Item(9431)),
        ARROWTIPS(90, 75, 1, new Item(44, 15));

        /**
         * The requirement required to smith this item.
         */
        private final int requirement;

        /**
         * The experience gained for smithing this item.
         */
        private final double experience;

        /**
         * The bars required for smithing this item.
         */
        private final int barsRequired;

        /**
         * The produced item for smithing this bar.
         */
        private final Item produced;

        /**
         * The name of this producable item.
         */
        private final String name;

        /**
         * Constructs a new {@link RuniteTable}.
         * @param requirement  {@link #requirement}.
         * @param experience   {@link #experience}.
         * @param barsRequired {@link #barsRequired}.
         * @param produced     {@link #produced}.
         * @param name         {@link #name}.
         */
        RuniteTable(int requirement, double experience, int barsRequired, Item produced, String name) {
            this.requirement = requirement;
            this.experience = experience;
            this.barsRequired = barsRequired;
            this.produced = produced;
            this.name = name;
        }

        /**
         * Constructs a new {@link RuniteTable}.
         * @param requirement  {@link #requirement}.
         * @param experience   {@link #experience}.
         * @param barsRequired {@link #barsRequired}.
         * @param produced     {@link #produced}.
         */
        RuniteTable(int requirement, double experience, int barsRequired, Item produced) {
            this.requirement = requirement;
            this.experience = experience;
            this.barsRequired = barsRequired;
            this.produced = produced;
            this.name = StringUtils.capitalize(toString().replace("_", " "));
        }

        @Override
        public Item getBar() {
            return new Item(2363);
        }

        @Override
        public int getLevelRequirement() {
            return requirement;
        }

        @Override
        public double getExperience() {
            return experience;
        }

        @Override
        public int getBarsRequired() {
            return barsRequired;
        }

        @Override
        public Item getProduced() {
            return produced;
        }

        @Override
        public String getName() {
            return name;
        }
    }
}
