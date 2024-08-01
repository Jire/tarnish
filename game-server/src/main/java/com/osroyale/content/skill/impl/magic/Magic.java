package com.osroyale.content.skill.impl.magic;

import com.osroyale.game.world.items.Item;
import com.osroyale.util.Items;

public class Magic {

    public static final Item[] UNALCHABLES = {new Item(Items.COINS), new Item(Items.SURVIVAL_TOKEN), new Item(Items.BLOOD_MONEY), new Item(Items.IRON_FULL_HELM), new Item(Items.IRON_PLATEBODY), new Item(Items.IRON_PLATELEGS),
    new Item(Items.IRON_KITESHIELD), new Item(Items.STEEL_FULL_HELM), new Item(Items.STEEL_PLATEBODY), new Item(Items.STEEL_PLATELEGS), new Item(Items.STEEL_KITESHIELD),
    new Item(Items.BLACK_FULL_HELM), new Item(Items.BLACK_PLATEBODY), new Item(Items.BLACK_PLATELEGS), new Item(Items.BLACK_KITESHIELD), new Item(Items.MITHRIL_FULL_HELM),
    new Item(Items.MITHRIL_PLATEBODY), new Item(Items.MITHRIL_PLATELEGS), new Item(Items.MITHRIL_KITESHIELD), new Item(Items.ADAMANT_FULL_HELM), new Item(Items.ADAMANT_PLATEBODY), new Item(Items.ADAMANT_PLATELEGS), new Item(Items.ADAMANT_KITESHIELD), new Item(Items.RUNE_FULL_HELM), new Item(Items.RUNE_PLATEBODY), new Item(Items.RUNE_PLATELEGS),
    new Item(Items.RUNE_KITESHIELD), new Item(Items.DRAGON_FULL_HELM), new Item(Items.PROSELYTE_SALLET), new Item(Items.PROSELYTE_HAUBERK), new Item(Items.PROSELYTE_CUISSE), new Item(Items.PROSELYTE_TASSET),
    new Item(Items.ROCK_SHELL_HELM), new Item(Items.ROCK_SHELL_PLATE), new Item(Items.ROCK_SHELL_LEGS), new Item(Items.BERSERKER_HELM), new Item(Items.WARRIOR_HELM), new Item(Items.FARSEER_HELM), new Item(Items.ARCHER_HELM), new Item(Items.HELM_OF_NEITIZNOT),
    new Item(Items.IRON_BOOTS), new Item(Items.RUNE_BOOTS), new Item(Items.ANTI_DRAGON_SHIELD), new Item(Items.BRONZE_SCIMITAR), new Item(Items.BRONZE_2H_SWORD),
    new Item(Items.IRON_SCIMITAR), new Item(Items.IRON_2H_SWORD), new Item(Items.STEEL_SCIMITAR), new Item(Items.STEEL_2H_SWORD), new Item(Items.BLACK_SCIMITAR), new Item(Items.BLACK_2H_SWORD),
    new Item(Items.MITHRIL_SCIMITAR), new Item(Items.MITHRIL_2H_SWORD), new Item(Items.ADAMANT_SCIMITAR), new Item(Items.ADAMANT_2H_SWORD), new Item(Items.RUNE_SCIMITAR), new Item(Items.RUNE_2H_SWORD),
    new Item(Items.DRAGON_SCIMITAR), new Item(Items.DRAGON_2H_SWORD), new Item(Items.DRAGON_LONGSWORD), new Item(Items.DRAGON_DAGGER_P_PLUS_PLUS_), new Item(Items.DRAGON_MACE), new Item(Items.DRAGON_BATTLEAXE),
    new Item(Items.GRANITE_MAUL), new Item(Items.TZHAAR_KET_OM), new Item(Items.AMULET_OF_ACCURACY), new Item(Items.AMULET_OF_POWER), new Item(Items.AMULET_OF_GLORY), new Item(Items.BRONZE_DART), new Item(Items.MITHRIL_DART), new Item(Items.RUNE_DART),
    new Item(Items.COIF), new Item(Items.LEATHER_BODY), new Item(Items.LEATHER_CHAPS), new Item(Items.LEATHER_VAMBRACES), new Item(Items.GREEN_DHIDE_BODY), new Item(Items.GREEN_DHIDE_CHAPS), new Item(Items.GREEN_DHIDE_VAMB),
    new Item(Items.BLUE_DHIDE_BODY), new Item(Items.BLUE_DHIDE_CHAPS), new Item(Items.BLUE_DHIDE_VAMB), new Item(Items.RED_DHIDE_BODY), new Item(Items.RED_DHIDE_CHAPS), new Item(Items.RED_DHIDE_VAMB), new Item(Items.BLACK_DHIDE_BODY),
    new Item(Items.BLACK_DHIDE_CHAPS), new Item(Items.BLACK_DHIDE_VAMB),  new Item(Items.SNAKESKIN_BOOTS), new Item(Items.SHORTBOW), new Item(Items.MAPLE_SHORTBOW), new Item(Items.MAGIC_SHORTBOW), new Item(Items.BRONZE_ARROW), new Item(Items.IRON_ARROW), new Item(Items.ADAMANT_ARROW), new Item(Items.RUNE_ARROW),
    new Item(Items.BRONZE_KNIFE), new Item(Items.IRON_KNIFE), new Item(Items.RUNE_KNIFE), new Item(Items.DORGESHUUN_CROSSBOW), new Item(Items.BONE_BOLTS), new Item(Items.IRON_CROSSBOW), new Item(Items.RUNE_CROSSBOW), new Item(Items.STEEL_BOLTS), new Item(Items.MITHRIL_BOLTS),
    new Item(Items.RUNITE_BOLTS), new Item(Items.RUBY_BOLTS_E_), new Item(Items.DIAMOND_BOLTS_E_), new Item(Items.DRAGON_JAVELIN), new Item(Items.AVAS_ACCUMULATOR), new Item(Items.WRATH_RUNE), new Item(Items.AIR_BATTLESTAFF), new Item(Items.WATER_BATTLESTAFF), new Item(Items.EARTH_BATTLESTAFF), new Item(Items.FIRE_BATTLESTAFF),
    new Item(Items.ANCIENT_STAFF), new Item(Items.MYSTIC_HAT_DARK_), new Item(Items.MYSTIC_ROBE_TOP_DARK_), new Item(Items.MYSTIC_ROBE_BOTTOM_DARK_), new Item(Items.MYSTIC_GLOVES_DARK_), new Item(Items.MYSTIC_BOOTS_DARK_), new Item(Items.MYSTIC_HAT_LIGHT_), new Item(Items.MYSTIC_ROBE_TOP_LIGHT_), new Item(Items.MYSTIC_ROBE_BOTTOM_LIGHT_),
    new Item(Items.MYSTIC_GLOVES_LIGHT_), new Item(Items.MYSTIC_BOOTS_LIGHT_), new Item(Items.MYSTIC_HAT), new Item(Items.MYSTIC_ROBE_TOP), new Item(Items.MYSTIC_ROBE_BOTTOM), new Item(Items.MYSTIC_GLOVES), new Item(Items.MYSTIC_BOOTS), new Item(Items.MYSTIC_HAT), new Item(Items.MYSTIC_ROBE_TOP),
    new Item(Items.ENCHANTED_TOP), new Item(Items.ENCHANTED_HAT), new Item(Items.ENCHANTED_ROBE), new Item(Items.AMULET_OF_STRENGTH), new Item(Items.AMULET_OF_DEFENCE), new Item(Items.AMULET_OF_ACCURACY), new Item(Items.AMULET_OF_MAGIC), new Item(Items.AMULET_OF_POWER), new Item(Items.AMULET_OF_GLORY_4), new Item(Items.PHOENIX_NECKLACE),
    new Item(Items.RING_OF_RECOIL), new Item(Items.RING_OF_LIFE), new Item(Items.HOLY_BOOK), new Item(Items.UNHOLY_BOOK), new Item(Items.BOOK_OF_BALANCE), new Item(Items.CLIMBING_BOOTS), new Item(Items.GHOSTLY_HOOD), new Item(Items.GHOSTLY_ROBE), new Item(Items.GHOSTLY_ROBE_2), new Item(Items.GHOSTLY_CLOAK), new Item(Items.GHOSTLY_BOOTS),
    new Item(Items.MONKS_ROBE), new Item(Items.MONKS_ROBE_TOP), new Item(Items.BLUE_SKIRT), new Item(Items.BLUE_WIZARD_HAT), new Item(Items.BLUE_WIZARD_ROBE)};


    public static final int[] BONES = {526, 528, 530, 534};
}
