package com.osroyale.content.skill.impl.magic.spell.impl;

import com.osroyale.content.skill.impl.magic.Spellbook;
import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.position.Position;
import com.osroyale.util.Items;

public enum MagicTeleports {

    CATHERBY_TELEPORT("Catherby",30250, Spellbook.LUNAR, Animation.LUNAR_TELE, Graphic.LUNAR_TELE, new Item[]{new Item(Items.ASTRAL_RUNE, 3), new Item(Items.LAW_RUNE, 3), new Item(Items.WATER_RUNE, 10)}, new Position(2814, 3436, 0), 87, 67.5, -1),
    MOONCLAN_TELEPORT("Moonclan",30064, Spellbook.LUNAR, Animation.LUNAR_TELE, Graphic.LUNAR_TELE, new Item[]{new Item(Items.ASTRAL_RUNE, 2), new Item(Items.LAW_RUNE, 1), new Item(Items.EARTH_RUNE, 2)}, new Position(2114, 3914, 0), 69, 66, -1),
    OURANIA_TELEPORT("Ourania", 30083, Spellbook.LUNAR, Animation.LUNAR_TELE, Graphic.LUNAR_TELE, new Item[]{new Item(Items.ASTRAL_RUNE, 2), new Item(Items.LAW_RUNE, 1), new Item(Items.EARTH_RUNE, 6)}, new Position(2465, 3249, 0), 71, 69, -1),
    BARBARIAN_TELEPORT("Barbarian",30138, Spellbook.LUNAR, Animation.LUNAR_TELE, Graphic.LUNAR_TELE, new Item[]{new Item(Items.ASTRAL_RUNE, 2), new Item(Items.LAW_RUNE, 2), new Item(Items.FIRE_RUNE, 3)}, new Position(2541, 3570, 0), 75, 76, -1),
    FISHING_TELEPORT("Fishing",30226, Spellbook.LUNAR, Animation.LUNAR_TELE, Graphic.LUNAR_TELE, new Item[]{new Item(Items.ASTRAL_RUNE, 3), new Item(Items.LAW_RUNE, 3), new Item(Items.WATER_RUNE, 10)}, new Position(2809, 3435, 0), 85, 89, -1),
    ICE_PLATEAU_TELEPORT("Ice Plateau",30266, Spellbook.LUNAR, Animation.LUNAR_TELE, Graphic.LUNAR_TELE, new Item[]{new Item(Items.ASTRAL_RUNE, 3), new Item(Items.LAW_RUNE, 3), new Item(Items.WATER_RUNE, 8)}, new Position(2971, 3936, 0), 89, 96, 53),
    KHAZARD_TELEPORT("Khazard",30162, Spellbook.LUNAR, Animation.LUNAR_TELE, Graphic.LUNAR_TELE, new Item[]{new Item(Items.ASTRAL_RUNE, 2), new Item(Items.LAW_RUNE, 2), new Item(Items.WATER_RUNE, 4)}, new Position(2650, 3158, 0), 78, 80, -1),
    WATERBIRTH_TELEPORT("Waterbirth", 30106, Spellbook.LUNAR, Animation.LUNAR_TELE, Graphic.LUNAR_TELE, new Item[]{new Item(Items.ASTRAL_RUNE, 2), new Item(Items.LAW_RUNE, 1), new Item(Items.WATER_RUNE, 1)}, new Position(2521, 3756, 0), 72, 71, -1),
    APE_ATOLL_TELEPORT("Ape Atoll", 18470, Spellbook.MODERN, Animation.NORMAL_TELE, Graphic.NORMAL_TELE, new Item[]{new Item(Items.FIRE_RUNE, 2), new Item(Items.WATER_RUNE, 2), new Item(Items.BANANA, 1), new Item(Items.LAW_RUNE, 2)}, new Position(2744, 2754, 0), 64, 74, -1),
    ARDOUGNE_TELEPORT("Ardougne", 1540, Spellbook.MODERN, Animation.NORMAL_TELE, Graphic.NORMAL_TELE, new Item[]{new Item(Items.WATER_RUNE, 2), new Item(Items.LAW_RUNE, 2)}, new Position(2757, 3478, 0), 51, 61, -1),
    CAMELOT_TELEPORT("Camelot",1174, Spellbook.MODERN, Animation.NORMAL_TELE, Graphic.NORMAL_TELE, new Item[]{new Item(Items.AIR_RUNE, 5), new Item(Items.LAW_RUNE, 1)}, new Position(2757, 3478, 0), 45, 55.5, -1),
    FALADOR_TELEPORT("Falador",1170, Spellbook.MODERN, Animation.NORMAL_TELE, Graphic.NORMAL_TELE, new Item[]{new Item(Items.WATER_RUNE, 1), new Item(Items.AIR_RUNE, 3), new Item(Items.LAW_RUNE, 1)}, new Position(2966, 3380, 0), 37, 48, -1),
    KOUREND_TELEPORT("Kourend",-25406, Spellbook.MODERN, Animation.NORMAL_TELE, Graphic.NORMAL_TELE, new Item[]{new Item(Items.FIRE_RUNE, 5), new Item(Items.WATER_RUNE, 4), new Item(Items.SOUL_RUNE, 2), new Item(Items.LAW_RUNE, 2)}, new Position(1639, 3760, 0), 69, 81.5, -1),
    LUMBRIDGE_TELEPORT("Lumbridge",1167, Spellbook.MODERN, Animation.NORMAL_TELE, Graphic.NORMAL_TELE, new Item[]{new Item(Items.EARTH_RUNE, 1), new Item(Items.AIR_RUNE, 3), new Item(Items.LAW_RUNE, 1)}, new Position(3222, 3218, 0), 31, 41, -1),
    TROLLHEIM_TELEPORT("Trollheim",7455, Spellbook.MODERN, Animation.NORMAL_TELE, Graphic.NORMAL_TELE, new Item[]{new Item(Items.FIRE_RUNE, 2), new Item(Items.LAW_RUNE, 2)}, new Position(2891, 3677, 0), 61, 68, -1),
    VARROCK_TELEPORT("Varrock",1164, Spellbook.MODERN, Animation.NORMAL_TELE, Graphic.NORMAL_TELE, new Item[]{new Item(Items.FIRE_RUNE, 1), new Item(Items.AIR_RUNE, 3), new Item(Items.LAW_RUNE, 1)}, new Position(3210, 3424, 0), 25, 35.5, -1),
    WATCHTOWER_TELEPORT("Watchtower",1541, Spellbook.MODERN, Animation.NORMAL_TELE, Graphic.NORMAL_TELE, new Item[]{new Item(Items.EARTH_RUNE, 2), new Item(Items.LAW_RUNE, 2)}, new Position(2583, 3096, 0), 58, 68, -1);

    private final int buttonId;

    private final Spellbook spellbook;
    private final Item[] runes;

    private final Position position;

    private final int levelRequirement;
    private final Graphic graphics;
    private final Animation animation;
    private final double experience;

    private final int wildernessLevel;
    private final String name;

    MagicTeleports(String name, int buttonId, Spellbook spellbook, Animation animation, Graphic graphics, Item[] runes, Position position, int levelRequirement, double experience, int wildernessLevel) {
        this.name = name;
        this.buttonId = buttonId;
        this.spellbook = spellbook;
        this.animation = animation;
        this.graphics = graphics;
        this.runes = runes;
        this.position = position;
        this.levelRequirement = levelRequirement;
        this.experience = experience;
        this.wildernessLevel = wildernessLevel;
    }

    public static MagicTeleports forButtonId(int buttonId) {
        for (MagicTeleports spell : MagicTeleports.values()) {
            if (spell.getButtonId() == buttonId) {
                return spell;
            }
        }
        return null;
    }

    public int getButtonId() {
        return buttonId;
    }

    public Position getPosition() {
        return position;
    }

    public double getExperience() {
        return experience;
    }
    public String getName() {
        return name;
    }

    public int getLevelRequirement() {
        return levelRequirement;
    }

    public Item[] getRunes() {
        return runes;
    }

    public Animation getAnimation() {
        return animation;
    }
    public Graphic getGraphics() {
        return graphics;
    }

    public Spellbook getSpellbook() {
        return spellbook;
    }

    public int getWildernessLevel() {
        return wildernessLevel;
    }
}
