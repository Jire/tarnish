package com.osroyale.game.world.entity.mob.prayer;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.osroyale.game.world.entity.combat.attack.listener.CombatListener;
import com.osroyale.game.world.entity.combat.attack.listener.other.prayer.AuguryListener;
import com.osroyale.game.world.entity.combat.attack.listener.other.prayer.ChivalryListener;
import com.osroyale.game.world.entity.combat.attack.listener.other.prayer.PietyListener;
import com.osroyale.game.world.entity.combat.attack.listener.other.prayer.RigourListener;
import com.osroyale.game.world.entity.combat.attack.listener.other.prayer.attack.ClarityOfThoughtListener;
import com.osroyale.game.world.entity.combat.attack.listener.other.prayer.attack.ImprovedReflexesListener;
import com.osroyale.game.world.entity.combat.attack.listener.other.prayer.attack.IncredibleReflexesListener;
import com.osroyale.game.world.entity.combat.attack.listener.other.prayer.defence.RockSkinListener;
import com.osroyale.game.world.entity.combat.attack.listener.other.prayer.defence.SteelSkinListener;
import com.osroyale.game.world.entity.combat.attack.listener.other.prayer.defence.ThickSkinListener;
import com.osroyale.game.world.entity.combat.attack.listener.other.prayer.magic.MysticLoreListener;
import com.osroyale.game.world.entity.combat.attack.listener.other.prayer.magic.MysticMightListener;
import com.osroyale.game.world.entity.combat.attack.listener.other.prayer.magic.MysticWillListener;
import com.osroyale.game.world.entity.combat.attack.listener.other.prayer.ranged.EagleEyeListener;
import com.osroyale.game.world.entity.combat.attack.listener.other.prayer.ranged.HawkEyeListener;
import com.osroyale.game.world.entity.combat.attack.listener.other.prayer.ranged.SharpListener;
import com.osroyale.game.world.entity.combat.attack.listener.other.prayer.strength.BurstOfStrengthListener;
import com.osroyale.game.world.entity.combat.attack.listener.other.prayer.strength.SuperhumanStrengthListener;
import com.osroyale.game.world.entity.combat.attack.listener.other.prayer.strength.UltimateStrengthListener;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.net.packet.out.SendConfig;
import com.osroyale.net.packet.out.SendMessage;

import java.util.*;

/**
 * Handles the prayer book.
 *
 * @author Michael
 * @author Daniel
 */
public enum Prayer {
    THICK_SKIN("Thick Skin", 1, 3, 83, 630, 5609, new ThickSkinListener(), Type.DEFENSIVE),
    BURST_OF_STRENGTH("Burst of Strength", 4, 3, 84, 631, 5610, new BurstOfStrengthListener(), Type.AGGRESSIVE),
    CLARITY_OF_THOUGHT("Clarity of Thought", 7, 3, 85, 632, 5611, new ClarityOfThoughtListener(), Type.OFFENSIVE),
    SHARP_EYE("Sharp Eye", 8, 3, 700, 633, 19812, new SharpListener(), Type.OFFENSIVE, Type.AGGRESSIVE),
    MYSTIC_WILL("Mystic Will", 9, 3, 701, 634, 19814, new MysticWillListener(), Type.OFFENSIVE, Type.AGGRESSIVE),
    ROCK_SKIN("Rock Skin", 10, 6, 86, 635, 5612, new RockSkinListener(), Type.DEFENSIVE),
    SUPERHUMAN_STRENGTH("Superhuman Strength", 13, 6, 87, 636, 5613, new SuperhumanStrengthListener(), Type.AGGRESSIVE),
    IMPROVED_REFLEXES("Improved Reflexes", 16, 6, 88, 637, 5614, new ImprovedReflexesListener(), Type.OFFENSIVE),
    RAPID_RESTORE("Rapid Restore", 19, 1, 89, 638, 5615, Type.NORMAL),
    RAPID_HEAL("Rapid Heal", 22, 2, 90, 639, 5616, Type.NORMAL),
    PROTECT_ITEM("Protect Item", 25, 2, 91, 640, 5617, Type.NORMAL),
    HAWK_EYE("Hawk Eye", 26, 6, 702, 641, 19816, new HawkEyeListener(), Type.OFFENSIVE, Type.AGGRESSIVE),
    MYSTIC_LORE("Mystic Lore", 27, 6, 703, 642, 19818, new MysticLoreListener(), Type.OFFENSIVE, Type.AGGRESSIVE),
    STEEL_SKIN("Steel Skin", 28, 12, 92, 643, 5618, new SteelSkinListener(), Type.DEFENSIVE),
    ULTIMATE_STRENGTH("Ultimate Strength", 31, 12, 93, 644, 5619, new UltimateStrengthListener(), Type.AGGRESSIVE),
    INCREDIBLE_REFLEXES("Incredible Reflexes", 34, 12, 94, 645, 5620, new IncredibleReflexesListener(), Type.OFFENSIVE),
    PROTECT_FROM_MAGIC("Protect from Magic", 37, 12, 95, 646, 5621, Type.OVERHEAD),
    PROTECT_FROM_RANGE("Protect from Range", 40, 12, 96, 647, 5622, Type.OVERHEAD),
    PROTECT_FROM_MELEE("Protect from Melee", 43, 12, 97, 648, 5623, Type.OVERHEAD),
    EAGLE_EYE("Eagle Eye", 44, 12, 704, 649, 19821, new EagleEyeListener(), Type.OFFENSIVE, Type.AGGRESSIVE),
    MYSTIC_MIGHT("Mystic Might", 45, 12, 705, 650, 19823, new MysticMightListener(), Type.OFFENSIVE, Type.AGGRESSIVE),
    RETRIBUTION("Retribution", 46, 3, 98, 651, 683, Type.OVERHEAD),
    REDEMPTION("Redemption", 49, 6, 99, 652, 684, Type.OVERHEAD),
    SMITE("Smite", 52, 18, 100, 653, 685, Type.OVERHEAD),
    PRESERVE("Preserve", 55, 3, 708, 654, 28001, Type.NORMAL),
    CHIVALRY("Chivalry", 60, 24, 706, 655, 19825, new ChivalryListener(), Type.OFFENSIVE, Type.AGGRESSIVE, Type.DEFENSIVE),
    PIETY("Piety", 70, 24, 707, 656, 19827, new PietyListener(), Type.OFFENSIVE, Type.AGGRESSIVE, Type.DEFENSIVE),
    RIGOUR("Rigour", 74, 24, 710, 657, 28004, new RigourListener(), Type.OFFENSIVE, Type.DEFENSIVE, Type.AGGRESSIVE),
    AUGURY("Augury", 77, 24, 712, 658, 28007, new AuguryListener(), Type.OFFENSIVE, Type.DEFENSIVE, Type.AGGRESSIVE);

    /** The name of the prayer. */
    public final String name;

    /** The required prayer level. */
    public final int level;

    /** The prayer drain rate. */
    private final int drainRate;

    /** The prayer config id. */
    private final int config;

    /** The quick-prayer config id. */
    private final int qConfig;

    /** The button id. */
    private final int button;

    /** The listener. */
    private final CombatListener<Mob> listener;

    /** The prayer type. */
    private final Type[] types;

    /**
     * Constructs a new default prayer book.
     *
     * @param name      The prayer name.
     * @param level     The required prayer level.
     * @param drainRate The prayer's drain rate.
     * @param config    The prayer's config id.
     * @param button    The button id.
     */
    Prayer(String name, int level, int drainRate, int config, int qConfig, int button, Type... types) {
        this.name = name;
        this.level = level;
        this.drainRate = drainRate;
        this.config = config;
        this.qConfig = qConfig;
        this.button = button;
        this.listener = null;
        this.types = types;
    }

    /**
     * Constructs a new default prayer book.
     *
     * @param name      The prayer name.
     * @param level     The required prayer level.
     * @param drainRate The prayer's drain rate.
     * @param config    The prayer's config id.
     * @param button    The button id.
     */
    Prayer(String name, int level, int drainRate, int config, int qConfig, int button, CombatListener<Mob> listener, Type... types) {
        this.name = name;
        this.level = level;
        this.drainRate = drainRate;
        this.config = config;
        this.qConfig = qConfig;
        this.button = button;
        this.listener = listener;
        this.types = types;
    }

    /** Streams for the prayer button.  */
    public static Optional<Prayer> forButton(int button) {
        return Arrays.stream(values()).filter(p -> p.getButton() == button).findAny();
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getDrainRate() {
        return drainRate;
    }

    public int getConfig() {
        return config;
    }

    public int getQConfig() {
        return qConfig;
    }

    public int getButton() {
        return button;
    }

    public Prayer.Type[] getTypes() {
        return types;
    }

    private static final Collection<Prayer> OFFENSIVE = ImmutableList.of(
            CLARITY_OF_THOUGHT, IMPROVED_REFLEXES, INCREDIBLE_REFLEXES,
            SHARP_EYE, HAWK_EYE, EAGLE_EYE,
            MYSTIC_WILL, MYSTIC_LORE, MYSTIC_MIGHT,
            CHIVALRY, PIETY, RIGOUR, AUGURY
    );

    private static final Collection<Prayer> AGGRESSIVE = ImmutableList.of(
            BURST_OF_STRENGTH, SUPERHUMAN_STRENGTH, ULTIMATE_STRENGTH,
            SHARP_EYE, HAWK_EYE, EAGLE_EYE,
            MYSTIC_WILL, MYSTIC_LORE, MYSTIC_MIGHT,
            CHIVALRY, PIETY, RIGOUR
    );

    private static final Collection<Prayer> DEFENSIVE = ImmutableList.of(
            THICK_SKIN, ROCK_SKIN, STEEL_SKIN,
            CHIVALRY, PIETY, RIGOUR, AUGURY
    );

    public static final Collection<Prayer> OVERHEAD = ImmutableList.of(
            PROTECT_FROM_MAGIC, PROTECT_FROM_RANGE, PROTECT_FROM_MELEE,
            RETRIBUTION, REDEMPTION, SMITE
    );

    /**
     * Checks if a prayer is of a certain type.
     *
     * @param type The type to check
     * @return {@code True} if the prayer is of thie given type.
     */
    boolean is(Type type) {
        return Arrays.stream(getTypes()).anyMatch(other -> type == other);
    }

    /**
     * Gets the prayers to disable after activating this prayer.
     *
     * @return A set of prayers to disable.
     */
    Set<Prayer> toDeactivate() {
        Type[] types = getTypes();
        if (types.length == 0) return EMPTY;
        Set<Prayer> deactivate = new HashSet<>();
        for (Type type : types) {
            deactivate.addAll(forType(type));
        }
        return deactivate;

    }
    public static double getBonus(Player player) {
        double bonus = 0;
        if(player.equipment.getId(0) == 25438)
            bonus += 0.4;
        if(player.equipment.getId(4) == 25434)
            bonus += 0.8;
        if(player.equipment.getId(7) == 25436)
            bonus += 0.6;
        if(player.equipment.getId(10) == 25440)
            bonus += 0.2;

        if(player.equipment.containsAll(25438, 25434, 25436, 25440))
            bonus = 2.5;

        return bonus;
    }


    public boolean canToggle(Player player) {
        if (player.skills.getLevel(Skill.PRAYER) == 0) {
            return false;
        }
        if (level > player.skills.getMaxLevel(Skill.PRAYER)) {
            return false;
        }
        if (this == CHIVALRY && player.skills.getMaxLevel(Skill.DEFENCE) < 60) {
            return false;
        }
        if ((this == PIETY || this == RIGOUR || this == AUGURY) && player.skills.getMaxLevel(Skill.DEFENCE) < 70) {
            return false;
        }
        if (this == PROTECT_ITEM && player.right.equals(PlayerRight.ULTIMATE_IRONMAN)) {
            return false;
        }
      /*  if ((this == PROTECT_FROM_MAGIC || this == PROTECT_FROM_MELEE || this == PROTECT_FROM_RANGE) && Area.inEventArena(player)) {
            return false;
        }*/
        return PlayerRight.isAdministrator(player) || (this != RIGOUR && this != AUGURY && this != PRESERVE) || player.unlockedPrayers.contains(this);
    }

    public void reset(Player player) {
        reset(player, null);
    }

    /**
     * Resets the configs for this prayer. The quick prayer icon will be
     * disabled and all active prayers will be reset.
     *
     * @param player  The player.
     * @param message The message to send, or null if none.
     */
    public void reset(Player player, String message) {
        player.prayer.reset();
        player.send(new SendConfig(config, 0));
        if (message != null) {
            player.send(new SendMessage(message));
        }
    }

    public Optional<CombatListener<Mob>> getListener() {
        return Optional.ofNullable(listener);
    }

    public int getHeadIcon() {
        switch (this) {
            case PROTECT_FROM_MAGIC:
                return 2;
            case PROTECT_FROM_RANGE:
                return 1;
            case PROTECT_FROM_MELEE:
                return 0;
            case RETRIBUTION:
                return 3;
            case REDEMPTION:
                return 5;
            case SMITE:
                return 4;
            default:
                return -1;
        }
    }

    Collection<Prayer> forType(Type type) {
        switch (type) {
            case OFFENSIVE:
                return OFFENSIVE;
            case DEFENSIVE:
                return DEFENSIVE;
            case AGGRESSIVE:
                return AGGRESSIVE;
            case OVERHEAD:
                return OVERHEAD;
            default:
                return EMPTY;
        }
    }

    static final Set<Prayer> EMPTY = ImmutableSet.of();

    enum Type {
        OFFENSIVE,
        DEFENSIVE,
        AGGRESSIVE,
        OVERHEAD,
        NORMAL
    }
}
