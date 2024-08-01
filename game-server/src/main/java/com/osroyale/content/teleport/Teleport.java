package com.osroyale.content.teleport;

import com.osroyale.content.skill.impl.magic.teleport.TeleportType;
import com.osroyale.game.world.position.Position;

/**
 * The teleport data.
 *
 * @author Daniel
 */
public enum Teleport {
    /* Minigames */
    GAMBLE_ARENA("Gamble Arena", TeleportType.MINIGAMES, new Position(3164, 3483, 0), false, new int[]{299, -1, 995}, "Gamble at your own risk!", ""),
    DUEL_ARENA("Duel Arena", TeleportType.MINIGAMES, new Position(3365, 3265, 0), false, new int[]{4151, -1, 5698}, "Stake at your own risk!", ""),
    BARROWS("Barrows", TeleportType.MINIGAMES, new Position(3565, 3315, 0), true, new int[]{4718, 7462, 4734}, "43 Prayer is highly recommended", "Don't forget your spade!"),
    FIGHT_CAVE("Fight Caves", TeleportType.MINIGAMES, new Position(2438, 5168, 0), false, new int[]{6529, 6570, 21295}, "43 Prayer & High Range is recommended", ""),
    PEST_CONTROL("Pest Control", TeleportType.MINIGAMES, new Position(2662, 2655, 0), false, new int[]{11663, 11664, 11665}, "Having a group is highly recommended", ""),
    WARRIOR_GUILD("Warrior Guild", TeleportType.MINIGAMES, new Position(2846, 3541, 0), true, new int[]{8844, 8850, 12954}, "Get your defenders here!", ""),
    INFERNO("Inferno", TeleportType.MINIGAMES, new Position(3565, 3315, 0), true, new int[] { 21295, 21285 }, "69 Waves", "Good gear recommended"),
    KOLODIONS_ARENA("Kolodion's Arena", TeleportType.MINIGAMES, new Position(2540, 4717, 0), false, new int[]{2412, 2413, 2414}, "You will be teleported in a safe area", ""),
    LAST_MAN_STANDING("Last Man Standing" , TeleportType.MINIGAMES , new Position(3141, 3634, 0), false, new int[]{4091, 12954, 9185}, "You will be teleported in the LMS Lobby", ""),
    WINTERTODT("Wintertodt", TeleportType.MINIGAMES, new Position(1631, 3942, 0), false, new int[] { 20714, 6739, 20693 }, "You will be teleport to wintertodt.", ""),
    FOSSIL_ISLAND("Fossil Island", TeleportType.MINIGAMES, new Position(3764, 3869, 1), false, new int[] {-1, 8792}, "Set up your birdhouses here!", ""),
//    CLAN_WARS("Clan Wars", TeleportType.MINIGAMES, new Position(3367, 3163, 0), false, "", ""),

    /* Skilling */
    AGILITY("Agility", TeleportType.SKILLING, new Position(3293, 3182, 0), true, new int[]{9773, 9771, 9772}, "Train Agility here!", ""),
    CRAFTING("Crafting", TeleportType.SKILLING, new Position(2747, 3444, 0), false, new int[]{9782, 9780, 9781}, "Train Crafting here!", ""),
    FARMING("Farming", TeleportType.SKILLING, new Position(2809, 3435, 0), true, new int[]{9812, 9810, 9811}, "Train Farming here!", ""),
    FISHING("Fishing", TeleportType.SKILLING, new Position(2809, 3435, 0), true, new int[]{9800, 9798, 9799}, "Train Fishing here!", ""),
    HUNTER("Hunter", TeleportType.SKILLING, new Position(3039, 4836, 0), true, new int[]{9950, 9948, 9949}, "Train Hunter here!", ""),
    MINING("Mining", TeleportType.SKILLING, new Position(3039, 4836, 1), true, new int[]{9794, 9792, 9793}, "Train Mining here!", ""),
    THIEVING("Thieving", TeleportType.SKILLING, new Position(3046, 4969, 1), false, new int[]{9779, 9777, 9778}, "Train Thieving here!", ""),
    RUNECRAFTING("Runecrafting", TeleportType.SKILLING, new Position(3039, 4836, 0), true, new int[]{9767, 9765, 9766}, "Train Runecrafting here!", ""),
    WOODCUTTING("Woodcutting", TeleportType.SKILLING, new Position(2727, 3484, 0), true, new int[]{9809, 9807, 9808}, "Train Woodcutting here!", ""),
    WILDERNESS_RESOURCE("Wilderness Resource", TeleportType.SKILLING, new Position(3184, 3947, 0), false, new int[]{11934, 451, 1513}, "This teleport is 50+ wilderness", ""),

    /* Monster Killing */
    CATACOMBS("Catacombs Of Kourend", TeleportType.MONSTER_KILLING, new Position(1666, 10049, 0), false, new int[] { -1, 411 },  "A massive dungeon with a", "variety of monsters"),
    GOBLINS("Goblins", TeleportType.MONSTER_KILLING, new Position(3259, 3228, 0), false, new int[]{-1, 288}, "Low level training", ""),
    AL_KHARID_WARRIORS("Al-Kharid Warriors", TeleportType.MONSTER_KILLING, new Position(3293, 3182, 0), false, new int[]{-1, 3218}, "Low level training", ""),
    ROCK_CRABS("Rock Crabs", TeleportType.MONSTER_KILLING, new Position(2676, 3711, 0), false, new int[]{-1, 411}, "Medium level training", ""),
    SAND_CRABS("Sand Crabs", TeleportType.MONSTER_KILLING, new Position(1726, 3463, 0), false, new int[]{-1, 411}, "Higher level training", ""),
    HILL_GIANTS("Hill Giants", TeleportType.MONSTER_KILLING, new Position(3117, 9856, 0), false, new int[]{}, "Teleport directly to hill giants", ""),
    YAKS("Yaks", TeleportType.MONSTER_KILLING, new Position(2321, 3804, 0), false, new int[]{}, "Teleport directly to yaks", ""),
    ANKOUS("Ankous", TeleportType.MONSTER_KILLING, new Position(2359, 5236, 0), false, new int[]{}, "Teleport directly to ankous", ""),
    SLAYER_TOWER("Slayer Tower", TeleportType.MONSTER_KILLING, new Position(3429, 3538, 0), false, new int[]{-1, 4151}, "The home of many slayer monsters", ""),
    TAVERLY_DUNGEON("Taverly Dungeon", TeleportType.MONSTER_KILLING, new Position(2884, 9800, 0), false, new int[]{}, "The home of many slayer monsters", ""),
    RELEKKA_DUNGEON("Relekka Dungeon", TeleportType.MONSTER_KILLING, new Position(2792, 10019, 0), false, new int[]{}, "The home of many slayer monsters", ""),
    BRIMHAVEN_DUNGEON("Brimhaven Dungeon", TeleportType.MONSTER_KILLING, new Position(2681, 9563, 0), false, new int[]{}, "The home of many slayer monsters", ""),
    SMOKE_DEVILS("Smoke Devils", TeleportType.MONSTER_KILLING, new Position(2404, 9415, 0), false, new int[]{-1, 12002}, "Teleport directly to smoke devils", ""),
    DUST_DEVILS("Dust Devils", TeleportType.MONSTER_KILLING, new Position(3239, 9364, 0), false, new int[]{20736, 2513, 20736}, "Teleport directly to dust devils", ""),
    DARK_BEAST("Dark Beast", TeleportType.MONSTER_KILLING, new Position(2018, 4639, 0), false, new int[]{-1, 11235}, "Teleport directly to dark beasts", ""),
    DEMONIC_GORILLAS("Demonic Gorillas", TeleportType.MONSTER_KILLING, new Position(2130, 5647, 0), false, new int[]{-1, 19529}, "Teleport directly to demonic gorillas", ""),
    SKELETAL_WYVERNS("Skeletal Wyverns", TeleportType.MONSTER_KILLING, new Position(3055, 9564, 0), false, new int[]{-1, 11286}, "Teleport directly to skeletal wyverns", ""),
    MITHRIL_DRAGONS("Mithril Dragons", TeleportType.MONSTER_KILLING, new Position(1748, 5326, 0), false, new int[]{-1, 11286}, "Teleport directly to mithril dragons", ""),
    POSSESED_PICKAXES("Possessed Pickaxes", TeleportType.MONSTER_KILLING, new Position(3434, 9637, 0), false, new int[]{-1, 1275}, "Teleport directly to possessed pickaxes", ""),

    KALPHITE_LAIR("Kalphite Lair", TeleportType.MONSTER_KILLING, new Position(3484, 9510, 2), false, new int[]{-1, 13489}, "Teleport directly to possessed pickaxes", ""),

    /* Player Killing */
    MAGE_BANK("Mage Bank", TeleportType.PLAYER_KILLING, new Position(2540, 4717, 0), false, new int[]{2412, 2413, 2414}, "You will be teleported in a safe area", ""),
    CASTLE("Castle", TeleportType.PLAYER_KILLING, new Position(3002, 3626, 0), false, new int[]{}, "This teleport is in 14 Wilderness", ""),
    EAST_DRAGONS("East Dragons", TeleportType.PLAYER_KILLING, new Position(3333, 3666, 0), false, new int[]{-1, 1540}, "This teleport is in 19 Wilderness", ""),
    WEST_DRAGONS("West Dragons", TeleportType.PLAYER_KILLING, new Position(2976, 3597, 0), false, new int[]{-1, 1540}, "This teleport is in 10 Wilderness", ""),
    GRAVES("Graveyard", TeleportType.PLAYER_KILLING, new Position(3180, 3671, 0), false, new int[]{-1, 6722}, "This teleport is in 19 Wilderness", ""),
    LAVA_DRAGONS("Lava Dragons", TeleportType.PLAYER_KILLING, new Position(3195, 3865, 0), false, new int[]{-1, 11286}, "This teleport is in the wilderness lvl 44", "It is also in multi!"),
    REVENANT_CAVE("Revenant Cave", TeleportType.PLAYER_KILLING, new Position(3074, 3652, 0), false, new int[]{21807, 21813, 21810}, "This teleport is in the wilderness lvl 17+", "It is also in multi!"),

    /* Boss Killing */
    GODWARS("Godwars", TeleportType.BOSS_KILLING, new Position(2860, 5355, 0), true, new int[]{11818, 11820, 11822}, "High combat lvl and", "having a team is recommended"),
    KING_BLACK_DRAGON("King Black Dragon", TeleportType.BOSS_KILLING, new Position(2997, 3849, 0), false, new int[]{-1, 12653}, "This teleport is in 40+ wilderness", "High combat and advanced gear is recommended"),
    KALPHITE_QUEEN("Kalphite Queen", TeleportType.BOSS_KILLING, new Position(3507, 9494, 0), false, new int[]{12654, 2513, 7981}, "High combat lvl and", "multiple attack types recommended"),
    ZULRAH("Zulrah", TeleportType.BOSS_KILLING, new Position(2206, 3056, 0), false, new int[]{12921, 12939, 12940}, "High range & magic is highly recommended", "Beware of her poisonous venom!"),
    KRAKEN("Kraken", TeleportType.BOSS_KILLING, new Position(2276, 10000, 0), false, new int[]{12004, 12655, 12004}, "Make sure to have high magic defence", "Kraken is stronger than usual!"),
    CRAZY_ARCH("Crazy Archaeologist", TeleportType.BOSS_KILLING, new Position(2966, 3698, 0), false, new int[]{}, "This teleport is in 23 wilderness", "Be careful, he's crazy!"),
    CHAOS_FANATIC("Chaos Fanatic", TeleportType.BOSS_KILLING, new Position(2982, 3832, 0), false, new int[]{-1, 11995}, "This teleport is in 40 wilderness", "This guy is a fanatic for chaos!"),
    CALLISTO("Callisto", TeleportType.BOSS_KILLING, new Position(3274, 3847, 0), false, new int[]{-1, 13178}, "This teleport is in 41 wilderness & is a multi-zone", "Be cautious for pkers!"),
    SCORPIA("Scorpia", TeleportType.BOSS_KILLING, new Position(3233, 3944, 0), false, new int[]{-1, 13181}, "This teleport is in 53 wilderness & is a multi-zone", "Watch out for pkers!"),
    VETION("Vet'ion", TeleportType.BOSS_KILLING, new Position(3217, 3781, 0), false, new int[]{13179, 12601, 13180}, "This teleport is in 33 wilderness & is a multi-zone", "Watch out for pkers!"),
    CORPOREAL_BEAST("Corporeal Beast", TeleportType.BOSS_KILLING, new Position(2967, 4383, 2), false, new int[]{}, "Having a high combat level is recommended", "Bringing a team is also advisable"),
    DAGGANOTHS("Dagannoth", TeleportType.BOSS_KILLING, new Position(1912, 4367, 0), false, new int[]{6731, 6737, 6733}, "", ""),
    LIZARD_SHAMAN("Lizard Shaman", TeleportType.BOSS_KILLING, new Position(1454, 3690, 0), false, new int[]{-1, 13576}, "This teleport is safe!", ""),
    CERBERUS("Cerberus", TeleportType.BOSS_KILLING, new Position(1310, 1268, 0), false, new int[]{13227, 13229, 13231}, "The king of the hell hounds.", "Level 91 slayer is required to fight this beast."),
    VORKATH("Vorkath", TeleportType.BOSS_KILLING, new Position(2276, 4036, 0), false, new int[]{11286, 21992, 22006}, "Vorkath, the Dragon King.", "Do you have what it takes to defeat him?"),
    SKOTIZO("Skotizo", TeleportType.BOSS_KILLING, new Position(2272, 5662, 0), false, new int[]{}, "", ""),

//    CERBERUS("Cerberus", TeleportType.BOSS_KILLING, new Position(2872, 9847, 0), 1500, false, "", ""),
//    LIZARDMAN_SHAMAN("Lizardman Shaman", TeleportType.BOSS_KILLING, new Position(1495, 3700, 0), 1500, false, "", ""),
//    GIANT_MOLE("Giant Mole", TeleportType.BOSS_KILLING, new Position(1761, 5186, 0), 1500, false, "", ""),;;
    ;

    /** The name of the teleport. */
    private final String name;

    /** The type of the teleport. */
    private final TeleportType type;

    /** The position of the teleport. */
    private final Position position;

    /** If the teleport is a special case. */
    private final boolean special;

    /** The item display of the teleport. */
    private final int[] items;

    /** Creates a new <code>Teleport<code>. */
    Teleport(String name, TeleportType type, Position position, boolean special, int[] items, String... strings) {
        this.name = name;
        this.type = type;
        this.position = position;
        this.special = special;
        this.items = items;
        this.strings = strings;
    }

    /** The information strings of the teleport. */
    private final String[] strings;

    /** Gets the name of the teleport. */
    public String getName() {
        return name;
    }

    /** Gets the type of the teleport. */
    public TeleportType getType() {
        return type;
    }

    /** Gets the position of the teleport. */
    public Position getPosition() {
        return position;
    }

    /** Gets the npc display of the itemcontainer. */
    public int[] getDisplay() {
        return items;
    }


    /** If the teleport is a special case. */
    public boolean isSpecial() {
        return special;
    }

    /** Gets the information strings of the teleport. */
    public String[] getStrings() {
        return strings;
    }

    @Override
    public String toString() {
        return "Name: " + name + " - Type: " + type + " - Special: " + special;
    }
}
