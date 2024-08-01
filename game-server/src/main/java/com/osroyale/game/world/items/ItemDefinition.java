package com.osroyale.game.world.items;

import com.google.common.collect.LinkedListMultimap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.osroyale.Config;
import com.osroyale.game.world.entity.combat.attack.FightType;
import com.osroyale.game.world.entity.combat.ranged.RangedAmmunition;
import com.osroyale.game.world.entity.combat.ranged.RangedWeaponDefinition;
import com.osroyale.game.world.entity.combat.ranged.RangedWeaponType;
import com.osroyale.game.world.entity.combat.weapon.WeaponInterface;
import com.osroyale.game.world.entity.mob.MobAnimation;
import com.osroyale.game.world.items.containers.equipment.EquipmentType;
import com.osroyale.util.parser.GsonParser;
import com.osroyale.util.parser.JsonSaver;
import org.jire.tarnishps.defs.ItemDefLoader;

import java.util.*;

import static com.osroyale.game.world.entity.combat.CombatConstants.*;

/**
 * Represents all of an in-game Item's attributes.
 *
 * @author Michael | Chex
 * @author Daniel | Obey
 */
public class ItemDefinition {

    /**
     * An array of item definitions.
     */
    public static ItemDefinition[] DEFINITIONS;
    private static final Map<FightType, Integer> EMPTY = new HashMap<>();
    public static final ItemDefinition DEFAULT = new ItemDefinition(1, "null");

    private final int id;
    private final String name;
    private String destroyMessage;
    private boolean destroyable;
    private boolean stackable;
    private boolean twoHanded;
    private boolean tradeable;
    private int stand_animation;
    private int attack_anim;
    private int walk_animation;
    private int run_animation;
    private Map<FightType, Integer> attack_animations;
    private int block_animation;
    private int unnotedId;
    private int notedId;
    private int street_value;
    private int base_value;
    private int highAlch;
    private int lowAlch;
    private double weight;
    private EquipmentType equipmentType;
    private WeaponInterface weaponInterface;
    private RangedWeaponDefinition rangedDefinition;
    private int[] requirements;
    private int[] bonuses;

    public ItemDefinition(int id, String name) {
        this.id = id;
        this.name = name;
        this.destroyMessage = null;
        this.destroyable = false;
        this.stackable = false;
        this.tradeable = true;
        this.twoHanded = false;
        this.unnotedId = id;
        this.notedId = id;
        this.street_value = 0;
        this.base_value = 0;
        this.highAlch = 0;
        this.lowAlch = 0;
        this.weight = 0;
        this.stand_animation = MobAnimation.PLAYER_STAND;
        this.walk_animation = MobAnimation.PLAYER_WALK;
        this.run_animation = MobAnimation.PLAYER_RUN;
        this.attack_animations = EMPTY;
        this.block_animation = -1;
        this.equipmentType = EquipmentType.NOT_WIELDABLE;
        this.weaponInterface = null;
        this.rangedDefinition = null;
        this.requirements = EMPTY_REQUIREMENTS;
        this.bonuses = EMPTY_BONUSES;
    }

    public static GsonParser createParser() {
        return new GsonParser("def/item/item_definitions", false) {

            @Override
            public void initialize(int size) {
                DEFINITIONS = new ItemDefinition[Config.ITEM_DEFINITION_LIMIT];
            }

            @Override
            protected void parse(JsonObject data) {
                int id = data.get("id").getAsInt();
                String name = data.get("name").getAsString();

                ItemDefinition definition = new ItemDefinition(id, name);

                if (data.has("destroyable")) {
                    definition.destroyable = data.get("destroyable").getAsBoolean();
                }

                if (data.has("destroy-message")) {
                    definition.destroyable = true;
                    definition.destroyMessage = data.get("destroy-message").getAsString();
                }

                if (data.has("stackable")) {
                    definition.stackable = data.get("stackable").getAsBoolean();
                }

                if (data.has("tradeable")) {
                    definition.tradeable = data.get("tradeable").getAsBoolean();
                }

                if (data.has("two-handed")) {
                    definition.twoHanded = data.get("two-handed").getAsBoolean();
                }

                if (data.has("noted-id")) {
                    definition.notedId = data.get("noted-id").getAsInt();
                }

                if (data.has("unnoted-id")) {
                    definition.unnotedId = data.get("unnoted-id").getAsInt();
                }

                if (data.has("street-value")) {
                    definition.street_value = data.get("street-value").getAsInt();
                }

                if (data.has("base-value")) {
                    definition.base_value = data.get("base-value").getAsInt();
                }

                if (data.has("high-alch")) {
                    definition.highAlch = data.get("high-alch").getAsInt();
                }

                if (data.has("low-alch")) {
                    definition.lowAlch = data.get("low-alch").getAsInt();
                }

                if (data.has("weight")) {
                    definition.weight = data.get("weight").getAsDouble();
                }

                if (data.has("equipment-slot")) {
                    definition.equipmentType = EquipmentType.valueOf(data.get("equipment-slot").getAsString());
                }

                if (data.has("weapon-interface")) {
                    definition.weaponInterface = WeaponInterface.valueOf(data.get("weapon-interface").getAsString());
                }

                for (int index = 0; index < SKILL_REQUIREMENT_CONFIG_FIELD_NAMES.length; index++) {
                    String skillRequirement = SKILL_REQUIREMENT_CONFIG_FIELD_NAMES[index];
                    if (data.has(skillRequirement)) {
                        if (definition.requirements == EMPTY_REQUIREMENTS) {
                            definition.requirements = new int[EMPTY_REQUIREMENTS.length];
                        }
                        definition.requirements[index] = data.get(skillRequirement).getAsInt();
                    }
                }

                for (int index = 0; index < BONUS_CONFIG_FIELD_NAMES.length; index++) {
                    String bonusName = BONUS_CONFIG_FIELD_NAMES[index];
                    if (data.has(bonusName)) {
                        if (definition.bonuses == EMPTY_BONUSES) {
                            definition.bonuses = new int[EMPTY_BONUSES.length];
                        }
                        definition.bonuses[index] = data.get(bonusName).getAsInt();
                    }
                }

                if (data.has("ranged-type") && data.has("allowed")) {
                    RangedWeaponType type = builder.fromJson(data.get("ranged-type"), RangedWeaponType.class);
                    RangedAmmunition[] allowed = builder.fromJson(data.get("allowed"), RangedAmmunition[].class);
                    definition.rangedDefinition = new RangedWeaponDefinition(type, allowed);
                }

                if (data.has("stand-animation")) {
                    definition.stand_animation = data.get("stand-animation").getAsInt();
                }

                if (data.has("walk-animation")) {
                    definition.walk_animation = data.get("walk-animation").getAsInt();
                }

                if (data.has("run-animation")) {
                    definition.run_animation = data.get("run-animation").getAsInt();
                }

                if (data.has("attack-animations")) {
                    JsonObject object = (JsonObject) data.get("attack-animations");
                    definition.attack_animations = new HashMap<>(object.entrySet().size());

                    for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
                        FightType fightType = FightType.valueOf(entry.getKey());
                        int animation = entry.getValue().getAsInt();

                        definition.attack_animations.put(fightType, animation);
                    }
                }

                if (data.has("block-animation")) {
                    definition.block_animation = data.get("block-animation").getAsInt();
                }

                DEFINITIONS[id] = definition;
            }

            @Override
            protected void onEnd() {
                ItemDefLoader.load();
            }
        };
    }

    public static ItemDefinition[] create(String path) {
        ItemDefinition[] definitions = new ItemDefinition[Config.ITEM_DEFINITION_LIMIT];

        new GsonParser(path) {

            @Override
            public void initialize(int size) {
                createParser().run();
            }

            @Override
            protected void parse(JsonObject data) {
                int id = data.get("id").getAsInt();
                String name = data.get("name").getAsString();

                ItemDefinition definition = definitions[id] = new ItemDefinition(id, name);
                ItemDefinition old = DEFINITIONS[id];

                if (data.has("destroyable")) {
                    definition.destroyable = data.get("destroyable").getAsBoolean();
                }

                if (data.has("destroy-message")) {
                    definition.destroyable = true;
                    definition.destroyMessage = data.get("destroy-message").getAsString();
                }

                if (data.has("stackable")) {
                    definition.stackable = data.get("stackable").getAsBoolean();
                }

                if (data.has("tradeable")) {
                    definition.tradeable = data.get("tradeable").getAsBoolean();
                }

                if (data.has("two-handed")) {
                    definition.twoHanded = data.get("two-handed").getAsBoolean();
                }

                if (data.has("noted-id")) {
                    definition.notedId = data.get("noted-id").getAsInt();
                }

                if (data.has("unnoted-id")) {
                    definition.unnotedId = data.get("unnoted-id").getAsInt();
                }

                if (data.has("street-value")) {
                    definition.street_value = data.get("street-value").getAsInt();
                } else if (old != null) {
                    definition.street_value = old.street_value;
                }

                if (data.has("base-value")) {
                    definition.base_value = data.get("base-value").getAsInt();
                }

                if (data.has("high-alch")) {
                    definition.highAlch = data.get("high-alch").getAsInt();
                }

                if (data.has("low-alch")) {
                    definition.lowAlch = data.get("low-alch").getAsInt();
                }

                if (data.has("weight")) {
                    definition.weight = data.get("weight").getAsDouble();
                }

                if (old != null && old.equipmentType != EquipmentType.NOT_WIELDABLE) {
                    definition.equipmentType = old.equipmentType;
                } else if (data.has("equipment-slot")) {
                    definition.equipmentType = EquipmentType.valueOf(data.get("equipment-slot").getAsString());
                }

                for (int index = 0; index < BONUS_CONFIG_FIELD_NAMES.length; index++) {
                    String bonusName = BONUS_CONFIG_FIELD_NAMES[index];
                    if (data.has(bonusName)) {
                        if (definition.bonuses == EMPTY_BONUSES) {
                            definition.bonuses = new int[EMPTY_BONUSES.length];
                        }
                        definition.bonuses[index] = data.get(bonusName).getAsInt();
                    }
                }

                if (old != null) {
                    definition.weaponInterface = old.weaponInterface;
                    definition.requirements = old.requirements;
                    definition.rangedDefinition = old.rangedDefinition;
                    definition.stand_animation = old.stand_animation;
                    definition.walk_animation = old.walk_animation;
                    definition.run_animation = old.run_animation;
                    definition.attack_animations = old.attack_animations;
                    definition.block_animation = old.block_animation;
                }
            }
        }.run();

        return definitions;
    }

    public static ItemDefinition[] fromClientDump(String path) {
        ItemDefinition[] definitions = new ItemDefinition[Config.ITEM_DEFINITION_LIMIT];

        new GsonParser(path) {
            @Override
            protected void parse(JsonObject data) {
                int id = data.get("id").getAsInt();
                String name = data.get("name").getAsString();

                ItemDefinition definition = definitions[id] = new ItemDefinition(id, name);

                if (data.has("stackable")) {
                    definition.stackable = data.get("stackable").getAsBoolean();
                }
                if (data.has("destroyable")) {
                    definition.destroyable = data.get("destroyable").getAsBoolean();
                }
                if (data.has("noted-id")) {
                    definition.notedId = data.get("noted-id").getAsInt();
                }
                if (data.has("unnoted-id")) {
                    definition.unnotedId = data.get("unnoted-id").getAsInt();
                }
                if (data.has("base-value")) {
                    definition.base_value = data.get("base-value").getAsInt();
                    definition.lowAlch = (int) (definition.base_value * 0.40);
                    definition.highAlch = (int) (definition.base_value * 0.60);
                }
                if (data.has("equipment-type")) {
                    definition.equipmentType = EquipmentType.valueOf(data.get("equipment-type").getAsString());
                }
            }
        }.run();

        return definitions;
    }

    public static void merge(String clientDumpFilePath, String wikiDumpPath, String dumpPath) {
        new GsonParser(wikiDumpPath) {
            LinkedListMultimap<String, JsonObject> multimap = LinkedListMultimap.create();

            @Override
            protected void parse(JsonObject data) {
                String name = data.get("name").getAsString();

                if (name.isEmpty())
                    return;

                multimap.put(name, data);
            }

            @Override
            protected void onEnd() {
                ItemDefinition[] definitions = fromClientDump(clientDumpFilePath);

                for (ItemDefinition definition : definitions) {
                    if (definition == null || definition.name.equals("null") || definition.name.isEmpty())
                        continue;

                    if (!multimap.containsKey(definition.name))
                        continue;

                    List<JsonObject> value = multimap.get(definition.name);
                    JsonObject object = value.get(value.size() - 1);

                    if (value.size() > 1)
                        multimap.remove(definition.name, object);

                    if (object.has("destroy-message"))
                        definition.destroyMessage = object.get("destroy-message").getAsString();

                    if (object.has("tradable"))
                        definition.tradeable = object.get("tradable").getAsBoolean();

                    if (object.has("weight"))
                        definition.weight = object.get("weight").getAsDouble();

                    if (definition.isNoted()) {
                        continue;
                    }

                    if (object.has("two-handed"))
                        definition.twoHanded = object.get("two-handed").getAsBoolean();

                    if (object.has("equipment-type"))
                        definition.equipmentType = EquipmentType.valueOf(object.get("equipment-type").getAsString());

                    for (int index = 0; index < BONUS_CONFIG_FIELD_NAMES.length; index++) {
                        String bonusName = BONUS_CONFIG_FIELD_NAMES[index];
                        if (object.has(bonusName)) {
                            if (definition.bonuses == EMPTY_BONUSES) {
                                definition.bonuses = new int[EMPTY_BONUSES.length];
                            }
                            definition.bonuses[index] = object.get(bonusName).getAsInt();
                        }
                    }
                }

                DEFINITIONS = definitions;
                dump(dumpPath);
            }

        }.run();
    }

    public static void dump(ItemDefinition[] definitions, String path) {
        JsonSaver saver = new JsonSaver();

        for (ItemDefinition definition : definitions) {
            if (definition == null) continue;
            if (definition.name.equals("null")) continue;

            saver.current().addProperty("id", definition.id);
            saver.current().addProperty("name", definition.name);

            if (definition.destroyMessage != null) {
                saver.current().addProperty("destroy-message", definition.destroyMessage);
            } else if (definition.destroyable) {
                saver.current().addProperty("destroyable", true);
            }

            if (definition.stackable) {
                saver.current().addProperty("stackable", true);
            }

            if (!definition.tradeable) {
                saver.current().addProperty("tradeable", false);
            }

            if (definition.twoHanded) {
                saver.current().addProperty("two-handed", true);
            }

            if (definition.unnotedId != definition.id) {
                saver.current().addProperty("unnoted-id", definition.unnotedId);
            }

            if (definition.notedId != definition.id) {
                saver.current().addProperty("noted-id", definition.notedId);
            }

            if (definition.lowAlch > 1) {
                saver.current().addProperty("low-alch", definition.lowAlch);
            }

            if (definition.highAlch > 1) {
                saver.current().addProperty("high-alch", definition.highAlch);
            }

            if (definition.base_value > 1) {
                saver.current().addProperty("base-value", definition.base_value);
            }

            if (definition.street_value > 1) {
                saver.current().addProperty("street-value", definition.street_value);
            }

            if (definition.weight != 0) {
                saver.current().addProperty("weight", definition.weight);
            }

            if (definition.stand_animation != MobAnimation.PLAYER_STAND) {
                saver.current().addProperty("stand-animation", definition.stand_animation);
            }

            if (definition.walk_animation != MobAnimation.PLAYER_WALK) {
                saver.current().addProperty("walk-animation", definition.walk_animation);
            }

            if (definition.run_animation != MobAnimation.PLAYER_RUN) {
                saver.current().addProperty("run-animation", definition.run_animation);
            }

            if (definition.attack_animations != EMPTY) {
                saver.current().add("attack-animations", saver.serializer().toJsonTree(definition.attack_animations));
            }

            if (definition.block_animation != -1) {
                saver.current().addProperty("block-animation", definition.block_animation);
            }

            if (definition.equipmentType != EquipmentType.NOT_WIELDABLE) {
                saver.current().addProperty("equipment-slot", definition.equipmentType.name());
            }

            if (definition.weaponInterface != null && definition.weaponInterface != WeaponInterface.UNARMED) {
                saver.current().addProperty("weapon-interface", definition.weaponInterface.name());
            }

            for (int index = 0; index < SKILL_REQUIREMENT_CONFIG_FIELD_NAMES.length; index++) {
                if (definition.requirements[index] > 0)
                    saver.current().addProperty(SKILL_REQUIREMENT_CONFIG_FIELD_NAMES[index], definition.requirements[index]);
            }

            for (int index = 0; index < BONUS_CONFIG_FIELD_NAMES.length; index++) {
                if (definition.bonuses[index] != 0)
                    saver.current().addProperty(BONUS_CONFIG_FIELD_NAMES[index], definition.bonuses[index]);
            }

            if (definition.rangedDefinition != null) {
                saver.current().addProperty("ranged-type", definition.rangedDefinition.getType().name());
                saver.current().add("allowed", saver.serializer().toJsonTree(definition.rangedDefinition.getAllowed()));
            }

            saver.split();
        }

        saver.publish("./data/" + path + ".json");
    }

    public static void dump(String path) {
        JsonSaver saver = new JsonSaver();

        for (ItemDefinition definition : DEFINITIONS) {
            if (definition == null) continue;
            if (definition.name.equals("null")) continue;

            saver.current().addProperty("id", definition.id);
            saver.current().addProperty("name", definition.name);

            if (definition.destroyMessage != null) {
                saver.current().addProperty("destroy-message", definition.destroyMessage);
            } else if (definition.destroyable) {
                saver.current().addProperty("destroyable", true);
            }

            if (definition.stackable) {
                saver.current().addProperty("stackable", true);
            }

            if (!definition.tradeable) {
                saver.current().addProperty("tradeable", false);
            }

            if (definition.twoHanded) {
                saver.current().addProperty("two-handed", true);
            }

            if (definition.unnotedId != definition.id) {
                saver.current().addProperty("unnoted-id", definition.unnotedId);
            }

            if (definition.notedId != definition.id) {
                saver.current().addProperty("noted-id", definition.notedId);
            }

            if (definition.lowAlch > 1) {
                saver.current().addProperty("low-alch", definition.lowAlch);
            }

            if (definition.highAlch > 1) {
                saver.current().addProperty("high-alch", definition.highAlch);
            }

            if (definition.base_value > 1) {
                saver.current().addProperty("base-value", definition.base_value);
            }

            if (definition.street_value > 1) {
                saver.current().addProperty("street-value", definition.street_value);
            }

            if (definition.weight != 0) {
                saver.current().addProperty("weight", definition.weight);
            }

            if (definition.stand_animation != MobAnimation.PLAYER_STAND) {
                saver.current().addProperty("stand-animation", definition.stand_animation);
            }

            if (definition.walk_animation != MobAnimation.PLAYER_WALK) {
                saver.current().addProperty("walk-animation", definition.walk_animation);
            }

            if (definition.run_animation != MobAnimation.PLAYER_RUN) {
                saver.current().addProperty("run-animation", definition.run_animation);
            }

            if (definition.attack_animations != EMPTY) {
                saver.current().add("attack-animations", saver.serializer().toJsonTree(definition.attack_animations));
            }

            if (definition.block_animation != -1) {
                saver.current().addProperty("block-animation", definition.block_animation);
            }

            if (definition.equipmentType != EquipmentType.NOT_WIELDABLE) {
                saver.current().addProperty("equipment-slot", definition.equipmentType.name());
            }

            if (definition.weaponInterface != null && definition.weaponInterface != WeaponInterface.UNARMED) {
                saver.current().addProperty("weapon-interface", definition.weaponInterface.name());
            }

            for (int index = 0; index < SKILL_REQUIREMENT_CONFIG_FIELD_NAMES.length; index++) {
                if (definition.requirements[index] > 0)
                    saver.current().addProperty(SKILL_REQUIREMENT_CONFIG_FIELD_NAMES[index], definition.requirements[index]);
            }

            for (int index = 0; index < BONUS_CONFIG_FIELD_NAMES.length; index++) {
                if (definition.bonuses[index] != 0)
                    saver.current().addProperty(BONUS_CONFIG_FIELD_NAMES[index], definition.bonuses[index]);
            }

            if (definition.rangedDefinition != null) {
                saver.current().addProperty("ranged-type", definition.rangedDefinition.getType().name());
                saver.current().add("allowed", saver.serializer().toJsonTree(definition.rangedDefinition.getAllowed()));
            }

            saver.split();
        }

        saver.publish("./data/" + path + ".json");
    }

    /**
     * Gets an item definition.
     *
     * @param id The definition's item id.
     * @return The item definition for the item id, or null if the item id is
     * out of bounds.
     */
    public static ItemDefinition get(int id) {
        if (id < 0 || id >= DEFINITIONS.length) {
            return DEFAULT;
        }

        if (DEFINITIONS[id] == null) {
            return DEFINITIONS[id] = new ItemDefinition(id, "null");
        }


        return DEFINITIONS[id];
    }

    /**
     * Gets the item id.
     *
     * @return The item id.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the item name.
     *
     * @return The item name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the item destroy message.
     *
     * @return The item destroy message.
     */
    public String getDestroyMessage() {
        return destroyMessage;
    }

    /**
     * Gets the item note state.
     *
     * @return {@code True} if the item is noted;
     */
    public boolean isNoted() {
        return unnotedId != id;
    }

    /**
     * Gets the item notability state.
     *
     * @return {@code} if the item can be turned into a note.
     */
    public boolean isNoteable() {
        return notedId != id;
    }

    /**
     * Gets the noted id.
     *
     * @return The noted id of this item if this item is un-noted, or the
     * original item id if it is already un-noted.
     */
    public int getNotedId() {
        return notedId;
    }

    /**
     * Gets the un-noted id.
     *
     * @return The un-noted id of this item if this item is noted, or the
     * original item id if it is already un-noted.
     */
    public int getUnnotedId() {
        return unnotedId;
    }

    /**
     * Gets the item stackability state.
     *
     * @return {@code True} if the item can be stacked.
     */
    public boolean isStackable() {
        return stackable;
    }

    /**
     * Gets the item destroyable state.
     *
     * @return {@code true} if the item can be destroyed
     */
    public boolean isDestroyable() {
        return destroyMessage != null;
    }

    /**
     * Gets the item tradability state.
     *
     * @return {@code True} if the item is tradable.
     */
    public boolean isTradeable() {
        return tradeable;
    }

    /**
     * Gets the item value.
     *
     * @return The value.
     */
    public int getStreetValue() {
        return street_value;
    }

    /**
     * Gets the item value.
     *
     * @return The value.
     */
    public int getBaseValue() {
        return base_value;
    }

    /**
     * Gets the item value.
     *
     * @return The value.
     */
    public int getValue() {
        return street_value > 0 ? street_value : base_value;
    }

    /**
     * Gets the high alchemy item value.
     *
     * @return The value.
     */
    public int getHighAlch() {
        return getValue() / 2;
    }

    /**
     * Gets the low alchemy item value.
     *
     * @return The value.
     */
    public int getLowAlch() {
        return getValue() / 5;
//        return lowAlch;
    }

    public EquipmentType getEquipmentType() {
        return equipmentType;
    }

    public int[] getRequirements() {
        return requirements;
    }

    public int[] getBonuses() {
        return bonuses;
    }

    /**
     * Gets the item equipability state.
     *
     * @return {@code True} if the item is equipable.
     */
    public boolean isEquipable() {
        return equipmentType != EquipmentType.NOT_WIELDABLE;
    }

    public boolean isTwoHanded() {
        return twoHanded;
    }

    /**
     * Gets the item weapon state.
     *
     * @return {@code True} if the item is a weapon.
     */
    public boolean isWeapon() {
        return EquipmentType.WEAPON.equals(equipmentType);
    }

    /**
     * Gets the item's weight.
     *
     * @return The item weight.
     */
    public double getWeight() {
        return weight;
    }

    public int getStandAnimation() {
        return stand_animation;
    }

    public int getWalkAnimation() {
        return walk_animation;
    }

    public int getRunAnimation() {
        return run_animation;
    }

    public OptionalInt getAttackAnimation(FightType fightType) {
        if (attack_animations == EMPTY) {
            return OptionalInt.empty();
        }

        return OptionalInt.of(attack_animations.get(fightType));
    }

    public OptionalInt getBlockAnimation() {
        return block_animation == -1 ? OptionalInt.empty() : OptionalInt.of(block_animation);
    }

    public Optional<RangedWeaponDefinition> getRangedDefinition() {
        return Optional.ofNullable(rangedDefinition);
    }

    public WeaponInterface getWeaponInterface() {
        return weaponInterface;
    }

    public void setEquipmentType(EquipmentType equipmentType) {
        this.equipmentType = equipmentType;
    }

    public boolean isPotion() {
        return this.name != null && (this.name.toLowerCase().contains("potion") || this.name.toLowerCase().contains("brew") || this.name.toLowerCase().contains("sanfew") || this.name.toLowerCase().contains("restore"));
    }

    public static ItemDefinition[] getDEFINITIONS() {
        return DEFINITIONS;
    }

    public static void setDEFINITIONS(ItemDefinition[] DEFINITIONS) {
        ItemDefinition.DEFINITIONS = DEFINITIONS;
    }

    public void setDestroyMessage(String destroyMessage) {
        this.destroyMessage = destroyMessage;
    }

    public void setDestroyable(boolean destroyable) {
        this.destroyable = destroyable;
    }

    public void setStackable(boolean stackable) {
        this.stackable = stackable;
    }

    public void setTwoHanded(boolean twoHanded) {
        this.twoHanded = twoHanded;
    }

    public void setTradeable(boolean tradeable) {
        this.tradeable = tradeable;
    }

    public int getStand_animation() {
        return stand_animation;
    }

    public void setStand_animation(int stand_animation) {
        this.stand_animation = stand_animation;
    }

    public int getAttack_anim() {
        return attack_anim;
    }

    public void setAttack_anim(int attack_anim) {
        this.attack_anim = attack_anim;
    }

    public int getWalk_animation() {
        return walk_animation;
    }

    public void setWalk_animation(int walk_animation) {
        this.walk_animation = walk_animation;
    }

    public int getRun_animation() {
        return run_animation;
    }

    public void setRun_animation(int run_animation) {
        this.run_animation = run_animation;
    }

    public Map<FightType, Integer> getAttack_animations() {
        return attack_animations;
    }

    public void setAttack_animations(Map<FightType, Integer> attack_animations) {
        this.attack_animations = attack_animations;
    }

    public int getBlock_animation() {
        return block_animation;
    }

    public void setBlock_animation(int block_animation) {
        this.block_animation = block_animation;
    }

    public void setUnnotedId(int unnotedId) {
        this.unnotedId = unnotedId;
    }

    public void setNotedId(int notedId) {
        this.notedId = notedId;
    }

    public int getStreet_value() {
        return street_value;
    }

    public void setStreet_value(int street_value) {
        this.street_value = street_value;
    }

    public int getBase_value() {
        return base_value;
    }

    public void setBase_value(int base_value) {
        this.base_value = base_value;
    }

    public void setHighAlch(int highAlch) {
        this.highAlch = highAlch;
    }

    public void setLowAlch(int lowAlch) {
        this.lowAlch = lowAlch;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setWeaponInterface(WeaponInterface weaponInterface) {
        this.weaponInterface = weaponInterface;
    }

    public void setRangedDefinition(RangedWeaponDefinition rangedDefinition) {
        this.rangedDefinition = rangedDefinition;
    }

    public void setRequirements(int[] requirements) {
        this.requirements = requirements;
    }

    public void setBonuses(int[] bonuses) {
        this.bonuses = bonuses;
    }
}
