package com.osroyale.content.skill.impl.mining;

import com.google.common.collect.ImmutableSet;
import com.osroyale.game.world.entity.mob.player.Player;

import java.util.Optional;

/**
 * Represents types of axes.
 * 
 * @author Graham Edgecombe
 */
public enum PickaxeData {
	BRONZE_PICKAXE(1265, 1, 625, 0.05),
	IRON_PICKAXE(1267, 1, 626, 0.08),
	STEEL_PICKAXE(1269, 6, 627, 0.12),
	MITHRIL_PICKAXE(1273, 21, 629, 0.16),
	ADAMANT_PICKAXE(1271, 31, 628, 0.2),
	RUNE_PICKAXE(1275, 41, 624, 0.25),
	INFERNO_ADZE(13243, 75, 4483, 0.55),
	THIRD_AGE_PICKAXE(20014, 90, 7283, 0.6),
	DRAGON_PICKAXE(11920, 61, 7139, 0.6),
	DRAGON_OR_PICKAXE(12797, 61, 643, 0.6);

	/**
	 * Caches our enum values.
	 */
	private static final ImmutableSet<PickaxeData> VALUES  = ImmutableSet.copyOf(values());

	/** The id. */
	public final int id;

	/** The level. */
	public final int level;

	/** The animation. */
	public final int animation;

	/**
	 * The speed of this pickaxe.
	 */
	public final double speed;

	/**
	 * Creates the axe.
	 * 
	 * @param id
	 *            The id.
	 * @param level
	 *            The required level.
	 * @param animation
	 *            The animation id.
	 */
	PickaxeData(int id, int level, int animation, double speed) {
		this.id = id;
		this.level = level;
		this.animation = animation;
		this.speed = speed;
	}

	/**
	 * Gets the definition for this pickaxe.
	 * @param player the identifier to check for.
	 * @return an optional holding the {@link PickaxeData} value found,
	 * {@link Optional#empty} otherwise.
	 */
	public static Optional<PickaxeData> getBestPickaxe(Player player) {
		return VALUES.stream().filter(def -> player.equipment.contains(def.id) || player.inventory.contains(def.id)).findAny();
	}
}