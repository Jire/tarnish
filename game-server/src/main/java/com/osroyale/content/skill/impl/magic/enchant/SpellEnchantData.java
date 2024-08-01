package com.osroyale.content.skill.impl.magic.enchant;

public enum SpellEnchantData {
    SAPPHIRE_RING(1637, 2550, 20, 7, 18, 719, 114, 1),
    SAPPHIRE_AMULET(1694, 1727, 24, 7, 18, 719, 114, 1),
    SAPPHIRE_NECKLACE(1656, 3853, 22, 7, 18, 719, 114, 1),

    EMERALD_RING(1639, 2552, 27, 27, 37, 719, 114, 2),
    EMERALD_AMULET(1696, 1729, 31, 27, 37, 719, 114, 2),
    EMERALD_NECKLACE(1658, 5521, 29, 27, 37, 719, 114, 2),

    RUBY_RING(1641, 2568, 34, 47, 59, 720, 115, 3),
    RUBY_AMULET(1698, 1725, 50, 47, 59, 720, 115, 3),
    RUBY_NECKLACE(1660, 11194, 40, 47, 59, 720, 115, 3),

    DIAMOND_RING(1643, 2570, 43, 57, 67, 720, 115, 4),
    DIAMOND_AMULET(1700, 1731, 70, 57, 67, 720, 115, 4),
    DIAMOND_NECKLACE(1662, 11090, 56, 57, 67, 720, 115, 4),

    DRAGONSTONE_RING(1645, 2572, 55, 68, 78, 721, 116, 5),
    DRAGONSTONE_AMULET(1702, 1712, 80, 68, 78, 721, 116, 5),
    DRAGONSTONE_NECKLACE(1664, 11105, 72, 68, 78, 721, 116, 5),

    ONYX_RING(6575, 6583, 67, 87, 97, 721, 452, 6),
    ONYX_AMULET(6581, 6585, 90, 87, 97, 721, 452, 6),
    ONYX_NECKLACE(6577, 11128, 82, 87, 97, 721, 452, 6),

    ZENYTE_RING(19538, 19550, 89, 93, 110, 721, 452, 7),
    ZENYTE_AMULET(19541, 19553, 98, 98, 110, 721, 452, 7),
    ZENYTE_BRACELET(19492, 19544, 95, 93, 110, 721, 452, 7),
    ZENYTE_NECKLACE(19535, 19547, 92, 93, 110, 721, 452, 7);

    private final int unenchanted;
    private final int enchanted;
    private final int magicLevel;
    private final int craftingLevel;
    private final int magicExperience;
    private final int animation;
    private final int graphic;
    private final int enchantmentLevel;

    SpellEnchantData(int unenchanted, int enchanted, int craftingLevel, int magicLevel, int magicExperience, int animation, int graphic, int enchantmentLevel) {
        this.unenchanted = unenchanted;
        this.enchanted = enchanted;
        this.craftingLevel = craftingLevel;
        this.magicLevel = magicLevel;
        this.magicExperience = magicExperience;
        this.animation = animation;
        this.graphic = graphic;
        this.enchantmentLevel = enchantmentLevel;
    }

    public int getUnenchanted() {
        return unenchanted;
    }

    public int getEnchanted() {
        return enchanted;
    }

    public int getCraftingLevel() {
        return craftingLevel;
    }

    public int getMagicLevel() {
        return magicLevel;
    }

    public int getXp() {
        return magicExperience;
    }

    public int getAnimation() {
        return animation;
    }

    public int getGFX() {
        return graphic;
    }

    public int getLevel() {
        return enchantmentLevel;
    }

    public static SpellEnchantData forId(int itemID) {
        for (SpellEnchantData data : values()) {
            if (data.getUnenchanted() == itemID)
                return data;
        }
        return null;
    }
}