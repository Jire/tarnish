package com.osroyale.util.parser.old.defs;

import com.osroyale.game.world.items.SkillRequirement;
import com.osroyale.game.world.items.containers.equipment.EquipmentType;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an in-game equipped item.
 *
 * @author Daniel | Obey
 */
public class EquipmentDefinition {

    /* The id of the item */
    private final int id;

    /* The name of the item */
    private final String name;

    /* The type of equipment also called slot */
    private final EquipmentType type;

    /* The requirements to equip this item */
    private final SkillRequirement[] requirements;

    /* The bonuses for this item */
    private final int[] bonuses;

    /**
     * Creates a new {@link EquipmentDefinition}.
     *
     * @param id           The id of the item being equipped.
     * @param name         The name of the item.
     * @param type         The slot of the item.
     * @param requirements The requirements of the item.
     * @param bonuses      The item bonuses.
     */
    public EquipmentDefinition(int id, String name, EquipmentType type, SkillRequirement[] requirements, int[] bonuses) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.requirements = requirements;
        this.bonuses = bonuses;
    }

    /** @return the bonuses */
    public int[] getBonuses() {
        return bonuses;
    }

    /** @return the id */
    public int getId() {
        return id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /** @return the requirements */
    public SkillRequirement[] getRequirements() {
        return requirements;
    }

    /** @return the type */
    public EquipmentType getType() {
        return type;
    }

    /** @return the equipment slot */
    public int getSlot() {
        return getType().getSlot();
    }

    /* The equipment definitions stored on startup that are mapped to their item ids */
    public static final Map<Integer, EquipmentDefinition> EQUIPMENT_DEFINITIONS = new HashMap<>();

    public static EquipmentDefinition get(int id) {
        return EQUIPMENT_DEFINITIONS.get(id);
    }

}
