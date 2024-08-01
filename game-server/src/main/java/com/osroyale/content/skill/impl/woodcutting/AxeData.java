package com.osroyale.content.skill.impl.woodcutting;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;

import java.util.Optional;


/**
 * Represents types of axes.
 *
 * @author Daniel
 */
public enum AxeData {
    BRONZE(1351, 1, 879, 0.1),
    IRON(1349, 1, 877, 0.2),
    STEEL(1353, 6, 875, 0.3),
    BLACK(1361, 6, 873, 0.4),
    MITHRIL(1355, 21, 871, 0.5),
    ADAMANT(1357, 31, 869, 0.6),
    RUNE(1359, 41, 867, 0.75),
    THIRD_AGE(20011, 90, 7264, 0.80),
    DRAGON(6739, 61, 2846, 0.85),
    INFERNAL(13241, 61, 2117, 0.85);

    /**
     * Caches our enum values.
     */
    private static final ImmutableSet<AxeData> VALUES = ImmutableSortedSet.copyOf(values()).descendingSet();

    /**
     * The id.
     */
    public final int id;

    /**
     * The level.
     */
    public final int level;

    /**
     * The animation.
     */
    public final int animation;

    /**
     * The speed of cutting logs.
     */
    public final double speed;

    /**
     * Creates the axe.
     *
     * @param id        The id.
     * @param level     The required level.
     * @param animation The animation id.
     * @param speed     {@link #speed}.
     */
    AxeData(int id, int level, int animation, double speed) {
        this.id = id;
        this.level = level;
        this.animation = animation;
        this.speed = speed;
    }

    /**
     * Gets the definition for this hatchet.
     * @param player the identifier to check for.
     * @return an optional holding the {@link AxeData} value found,
     * {@link Optional#empty} otherwise.
     */
    public static Optional<AxeData> getDefinition(Player player) {
        if (player.equipment.hasWeapon()) {
            Optional<AxeData> result = VALUES.stream().filter(it -> player.equipment.contains(it.id) && player.skills.getLevel(Skill.WOODCUTTING) >= it.level).findFirst();

            if (result.isPresent()) {
                return result;
            }
        }
        return VALUES.stream().filter(def -> player.inventory.contains(def.id) && player.skills.getLevel(Skill.WOODCUTTING) >= def.level).findAny();
    }


}
