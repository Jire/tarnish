package com.osroyale.game.world.entity.combat.weapon;

import com.osroyale.Config;
import com.osroyale.game.world.entity.combat.attack.FightStyle;
import com.osroyale.game.world.entity.combat.attack.FightType;
import com.osroyale.game.world.entity.combat.magic.Autocast;
import com.osroyale.game.world.entity.combat.strategy.player.special.CombatSpecial;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.net.packet.out.SendConfig;
import com.osroyale.net.packet.out.SendString;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.Optional;

/**
 * The enumerated type whose elements represent the weapon interfaces.
 *
 * @author lare96 <http://github.com/lare96>
 */
public enum WeaponInterface {
    UNARMED(5855, "unarmed",5857, 7737, 7749, 7761, new FightType[]{
            FightType.UNARMED_PUNCH,
            FightType.UNARMED_KICK,
            FightType.UNARMED_BLOCK
    }),

    DART(4446, "thrown", 4449, 7637, 7649, 7661, new FightType[]{
            FightType.DART_ACCURATE,
            FightType.DART_RAPID,
            FightType.DART_LONGRANGE
    }),

    STAFF(6103, "staff", 6132, 6104, 6117, 6129, new FightType[]{
            FightType.STAFF_BASH,
            FightType.STAFF_POUND,
            FightType.STAFF_FOCUS
    }),

    MAGIC_STAFF(328, "staff", 355, 340, 18566, 18569, new FightType[]{
            FightType.STAFF_BASH,
            FightType.STAFF_POUND,
            FightType.STAFF_FOCUS
    }),
    NIGHTMARE_STAFF(328, "staff", 355, 340, 18566, 18569, new FightType[]{
            FightType.NIGHTMARE_STAFF_BASH,
            FightType.NIGHTMARE_STAFF_POUND,
            FightType.NIGHTMARE_STAFF_FOCUS
    }),


    WARHAMMER(425, "blunt", 428, 7473, 7474, 7486, new FightType[]{
            FightType.WARHAMMER_POUND,
            FightType.WARHAMMER_PUMMEL,
            FightType.WARHAMMER_BLOCK
    }),

    SCYTHE(776, "scythe", 779, -1, -1, -1, new FightType[]{
            FightType.SCYTHE_REAP,
            FightType.SCYTHE_CHOP,
            FightType.SCYTHE_JAB,
            FightType.SCYTHE_BLOCK
    }),
    VITUR(776, "scythe", 779, -1, -1, -1, new FightType[]{
            FightType.VITUR_REAP,
            FightType.VITUR_CHOP,
            FightType.VITUR_JAB,
            FightType.VITUR_BLOCK
    }),


    BATTLEAXE(1698, "weapon", 1701, 7498, 7499, 7511, new FightType[]{
            FightType.BATTLEAXE_CHOP,
            FightType.BATTLEAXE_HACK,
            FightType.BATTLEAXE_SMASH,
            FightType.BATTLEAXE_BLOCK
    }),

    CROSSBOW(1764, "crossbow", 1767, 7548, 7549, 7561, new FightType[]{
            FightType.CROSSBOW_ACCURATE,
            FightType.CROSSBOW_RAPID,
            FightType.CROSSBOW_LONGRANGE
    }),
    ZARYTE_CROSSBOW(1764, "crossbow", 1767, 7548, 7549, 7561, new FightType[]{
            FightType.ZARYTE_ACCURATE,
            FightType.ZARYTE_RAPID,
            FightType.ZARYTE_LONGRANGE
    }),

    SHORTBOW(1764, "bow", 1767, 7548, 7549, 7561, new FightType[]{
            FightType.SHORTBOW_ACCURATE,
            FightType.SHORTBOW_RAPID,
            FightType.SHORTBOW_LONGRANGE
    }),

    LONGBOW(1764, "bow", 1767, 7548, 7549, 7561, new FightType[]{
            FightType.LONGBOW_ACCURATE,
            FightType.LONGBOW_RAPID,
            FightType.LONGBOW_LONGRANGE
    }),

    DARK_BOW(1764, "bow", 1767, 7548, 7549, 7561, new FightType[]{
            FightType.DARK_BOW_ACCURATE,
            FightType.DARK_BOW_RAPID,
            FightType.DARK_BOW_LONGRANGE
    }),

    COMPOSITE_BOW(1764, "bow", 1767, 7548, 7549, 7561, new FightType[]{
            FightType.LONGBOW_ACCURATE,
            FightType.LONGBOW_RAPID,
            FightType.LONGBOW_LONGRANGE
    }),

    DAGGER(2276, "stab_sword", 2279, 7562, 7574, 7586, new FightType[]{
            FightType.DAGGER_STAB,
            FightType.DAGGER_LUNGE,
            FightType.DAGGER_SLASH,
            FightType.DAGGER_BLOCK
    }),

    SWORD(2276, "slash_sword", 2279, 7562, 7574, 7586, new FightType[]{
            FightType.SWORD_STAB,
            FightType.SWORD_LUNGE,
            FightType.SWORD_SLASH,
            FightType.SWORD_BLOCK
    }),

    SCIMITAR(2423, "slash_sword", 2426, 7587, 7599, 7611, new FightType[]{
            FightType.SCIMITAR_CHOP,
            FightType.SCIMITAR_SLASH,
            FightType.SCIMITAR_LUNGE,
            FightType.SCIMITAR_BLOCK
    }),

    LONGSWORD(2423, "slash_sword", 2426, 7587, 7599, 7611, new FightType[]{
            FightType.LONGSWORD_CHOP,
            FightType.LONGSWORD_SLASH,
            FightType.LONGSWORD_LUNGE,
            FightType.LONGSWORD_BLOCK
    }),

    MACE(3796, "spiked", 3799, 7623, 7624, 7636, new FightType[]{
            FightType.MACE_POUND,
            FightType.MACE_PUMMEL,
            FightType.MACE_SPIKE,
            FightType.MACE_BLOCK
    }),
    VIGGORA_MACE(3796, "spiked", 3799, 7623, 7624, 7636, new FightType[]{
            FightType.VIGGORA_MACE_POUND,
            FightType.VIGGORA_MACE_PUMMEL,
            FightType.VIGGORA_MACE_SPIKE,
            FightType.VIGGORA_MACE_BLOCK
    }),

    INQUISITOR_MACE(3796, "spiked", 3799, 7623, 7624, 7636, new FightType[]{
            FightType.INQUISITOR_MACE_POUND,
            FightType.INQUISITOR_MACE_PUMMEL,
            FightType.INQUISITOR_MACE_SPIKE,
            FightType.INQUISITOR_MACE_BLOCK
    }),

    KNIFE(4446, "thrown", 4449, 7637, 7649, 7661, new FightType[]{
            FightType.KNIFE_ACCURATE,
            FightType.KNIFE_RAPID,
            FightType.KNIFE_LONGRANGE
    }),

    SPEAR(4679, "spear", 4682, 7662, 7674, 7686, new FightType[]{
            FightType.SPEAR_LUNGE,
            FightType.SPEAR_SWIPE,
            FightType.SPEAR_POUND,
            FightType.SPEAR_BLOCK
    }),
    HUNTER_LANCE(4679, "spear", 4682, 7662, 7674, 7686, new FightType[]{
            FightType.HUNTER_LANCE_LUNGE,
            FightType.HUNTER_LANCE_SWIPE,
            FightType.HUNTER_LANCE_POUND,
            FightType.HUNTER_LANCE_BLOCK
    }),

    TWO_HANDED_SWORD(4705, "2h_sword", 4708, 7687, 7699, 7711, new FightType[]{
            FightType.TWOHANDEDSWORD_CHOP,
            FightType.TWOHANDEDSWORD_SLASH,
            FightType.TWOHANDEDSWORD_SMASH,
            FightType.TWOHANDEDSWORD_BLOCK
    }),

    PICKAXE(5570, "pickaxe", 5573, 7723, 7724, 7736, new FightType[]{
            FightType.PICKAXE_SPIKE,
            FightType.PICKAXE_IMPALE,
            FightType.PICKAXE_SMASH,
            FightType.PICKAXE_BLOCK
    }),

    CLAWS(7762, "claw", 7765, 7788, 7800, 7812, new FightType[]{
            FightType.CLAWS_CHOP,
            FightType.CLAWS_SLASH,
            FightType.CLAWS_LUNGE,
            FightType.CLAWS_BLOCK
    }),

    HALBERD(8460, "polearm", 8463, 8481, 8493, 8505, new FightType[]{
            FightType.HALBERD_JAB,
            FightType.HALBERD_SWIPE,
            FightType.HALBERD_FEND
    }),

    WHIP(12290, "weapon", 12293, 12322, 12323, 12335, new FightType[]{
            FightType.WHIP_FLICK,
            FightType.WHIP_LASH,
            FightType.WHIP_DEFLECT
    }),

    THROWNAXE(4446, "thrown", 4449, 7637, 7649, 7661, new FightType[]{
            FightType.THROWNAXE_ACCURATE,
            FightType.THROWNAXE_RAPID,
            FightType.THROWNAXE_LONGRANGE
    }),

    CHINCHOMPA(24055, "chinchompas", 24056, -1, -1, -1, new FightType[]{
            FightType.SHORT_FUSE,
            FightType.MEDIUM_FUSE,
            FightType.LONG_FUSE
    }),

    SALAMANDER(24074, "salamander", 24075, -1, -1, -1, new FightType[]{
            FightType.SCORCH,
            FightType.FLARE,
            FightType.BLAZE
    }),

    TRIDENT(4446, "powered_staff", 4449, 7637, 7649, 7661, new FightType[]{
            FightType.TRIDENT_ACCURATE,
            FightType.TRIDENT_RAPID,
            FightType.TRIDENT_LONGRANGE
    }),
    SHADOW(4446, "powered_staff", 4449, 7637, 7649, 7661, new FightType[]{
            FightType.SHADOW_ACCURATE,
            FightType.SHADOW_RAPID,
            FightType.SHADOW_LONGRANGE
    }),

    BLOWPIPE(4446, "thrown", 4449, 7637, 7649, 7661, new FightType[]{
            FightType.BLOWPIPE_ACCURATE,
            FightType.BLOWPIPE_RAPID,
            FightType.BLOWPIPE_LONGRANGE
    }),

    BULWARK(425, "bulwark", 428, 7473, 7474, 7486, new FightType[]{
            FightType.BULWARK_POUND,
            FightType.BULWARK_PUMMEL,
            FightType.BULWARK_BLOCK
    }),

    GRANITE_MAUL(425, "blunt", 428, 7473, 7474, 7486, new FightType[]{
            FightType.GRANITE_MAUL_POUND,
            FightType.GRANITE_MAUL_PUMMEL,
            FightType.GRANITE_MAUL_BLOCK
    }),

    ELDER_MAUL(425, "blunt", 428, 7473, 7474, 7486, new FightType[]{
            FightType.ELDER_MAUL_POUND,
            FightType.ELDER_MAUL_PUMMEL,
            FightType.ELDER_MAUL_BLOCK
    }),

    GREATAXE(1698, "axe", 1701, 7498, 7499, 7591, new FightType[]{
            FightType.GREATAXE_CHOP,
            FightType.GREATAXE_HACK,
            FightType.GREATAXE_SMASH,
            FightType.GREATAXE_BLOCK
    }),

    KARIL_CROSSBOW(1764, "crossbow", 1767, 7548, 7549, 7561, new FightType[]{
            FightType.KARIL_CROSSBOW_ACCURATE,
            FightType.KARIL_CROSSBOW_RAPID,
            FightType.KARIL_CROSSBOW_LONGRANGE
    }),

    BALLISTA(1764, "crossbow", 1767, 7548, 7549, 7561, new FightType[]{
            FightType.BALLISTA_ACCURATE,
            FightType.BALLISTA_RAPID,
            FightType.BALLISTA_LONGRANGE
    }),

    DRAGON_DAGGER(2276, "stab_sword", 2279, 7562, 7574, 7586, new FightType[]{
            FightType.DRAGON_DAGGER_STAB,
            FightType.DRAGON_DAGGER_LUNGE,
            FightType.DRAGON_DAGGER_SLASH,
            FightType.DRAGON_DAGGER_BLOCK
    }),

    FLAIL(3796, "spiked", 3799, 7623, 7624, 7636, new FightType[]{
            FightType.FLAIL_POUND,
            FightType.FLAIL_PUMMEL,
            FightType.FLAIL_SPIKE,
            FightType.FLAIL_BLOCK
    }),

    GODSWORD_SWORD(4705, "2h_sword", 4708, 7687, 7699, 7711, new FightType[]{
            FightType.GODSWORD_CHOP,
            FightType.GODSWORD_SLASH,
            FightType.GODSWORD_SMASH,
            FightType.GODSWORD_BLOCK
    }),
    SAELDOR_SWORD(2423, "slash_sword", 2426, 7587, 7599, 7611, new FightType[]{
            FightType.SAELDOR_STAB,
            FightType.SAELDOR_SLASH,
            FightType.SAELDOR_CRUSH,
            FightType.SAELDOR_BLOCK
    }),
    OSMUM_SWORD(2276, "stab_sword", 2279, 7562, 7574, 7586, new FightType[]{
            FightType.FANG_STAB,
            FightType.FANG_LUNGE,
            FightType.FANG_SLASH,
            FightType.FANG_BLOCK
    }),
    GHRAZI_SWORD(2276, "stab_sword", 2279, 7562, 7574, 7586, new FightType[]{
            FightType.GHRAZI_STAB,
            FightType.GHRAZI_LUNGE,
            FightType.GHRAZI_SLASH,
            FightType.GHRAZI_BLOCK
    }),



    SARADOMIN_SWORD(4705, "2h_sword", 4708, 7687, 7699, 7711, new FightType[]{
            FightType.SARADOMIN_CHOP,
            FightType.SARADOMIN_SLASH,
            FightType.SARADOMIN_SMASH,
            FightType.SARADOMIN_BLOCK
    }),

    BLUDGEN(4705, "bludgeon", 4708, 7687, 7699, 7711, new FightType[]{
            FightType.BLUDGEN_CHOP,
            FightType.BLUDGEN_SLASH,
            FightType.BLUDGEN_SMASH,
            FightType.BLUDGEN_BLOCK
    });

    /**
     * The identification of the interface that will be displayed.
     */
    private final int id;

    private final String newDefName;

    /**
     * The identification of the line the weapon name will be displayed on.
     */
    private final int nameLine;

    /**
     * The fight types that correspond with this interface.
     */
    private final FightType[] fightTypes;

    /**
     * The identification of the special bar for this interface.
     */
    private final int specialButton;

    /**
     * The identification of the special bar for this interface.
     */
    private final int specialBar;

    /**
     * The identification of the special meter for this interface.
     */
    private final int specialMeter;

    /**
     * Creates a new {@link WeaponInterface}.
     *
     * @param id         the identification of the interface that will be
     *                   displayed.
     * @param nameLine   the identification of the line the weapon name will
     *                   be displayed on.
     * @param fightTypes the fight types that correspond with this interface.
     */
    WeaponInterface(int id, String newDefName, int nameLine, int specialButton, int specialBar, int specialMeter, FightType[] fightTypes) {
        this.id = id;
        this.newDefName = newDefName;
        this.nameLine = nameLine;
        this.fightTypes = fightTypes;
        this.specialButton = specialButton;
        this.specialBar = specialBar;
        this.specialMeter = specialMeter;
    }

    /**
     * The method executed when weapon {@code item} is equipped or unequipped
     * that assigns a weapon interface to {@code player}.
     *
     * @param player the player equipping the item.
     * @param item   the item the player is equipping, or {@code null} if a
     *               weapon was unequipped.
     */

    public static void execute(Player player, Item item) {
        execute(player, item, false);
    }

    public static void execute(Player player, Item item, boolean login) {
        WeaponInterface weapon = item == null ? null : item.getWeaponInterface();
        if (weapon == null) weapon = WeaponInterface.UNARMED;

        FightType[] oldTypes = player.getWeapon().fightTypes;
        FightType[] newTypes = weapon.fightTypes;
        FightType result = null;

        for (int index = 0; index < oldTypes.length; index++) {
            if (newTypes.length == index) {
                break;
            }

            if (newTypes[index].getStyle().equals(FightStyle.DEFENSIVE)
                    && player.getCombat().getFightType().getStyle().equals(FightStyle.DEFENSIVE)) {
                result = newTypes[index];
                break;
            }

            if (oldTypes[index] == player.getCombat().getFightType()) {
                boolean oldControlled = oldTypes[index].getStyle().equals(FightStyle.CONTROLLED);
                boolean newControlled = newTypes[index].getStyle().equals(FightStyle.CONTROLLED);

                if (newControlled != oldControlled) {
                    continue;
                }

                result = newTypes[index];
                break;
            }
        }

        if (result == null) {
            if (player.getCombat().getFightType().getStyle().equals(FightStyle.DEFENSIVE)) {
                result = newTypes[newTypes.length - 1];
            } else {
                result = newTypes[1];
            }
        }
        player.setWeapon(weapon);

        if(!login) {
            player.getCombat().setFightType(result);
            player.send(new SendConfig(result.getParent(), result.getChild()));
        }

        if (weapon == WeaponInterface.UNARMED) {
            defaultWeaponInterface(player);
            return;
        }

        CombatSpecial.assign(player);
        CombatSpecial.updateSpecialAmount(player);
        Autocast.reset(player);
        player.setSingleCast(null);

        player.send(new SendString(item.getName(), weapon.nameLine));
        player.interfaceManager.setSidebar(Config.ATTACK_TAB, weapon.id);
        player.equipment.updateAnimation();
    }

    private static void defaultWeaponInterface(Player player) {
        CombatSpecial.assign(player);
        CombatSpecial.updateSpecialAmount(player);
        Autocast.reset(player);
        player.setSingleCast(null);

        player.send(new SendString("<col=ff7000>Unarmed", WeaponInterface.UNARMED.nameLine));
        player.interfaceManager.setSidebar(Config.ATTACK_TAB, WeaponInterface.UNARMED.id);
        player.equipment.updateAnimation();
    }

    /**
     * Gets the identification of the interface that will be displayed.
     *
     * @return the identification of the interface.
     */
    public final int getId() {
        return id;
    }

    public String getNewDefName() {
        return newDefName;
    }

    /**
     * Gets the fight types that correspond with this interface.
     *
     * @return the fight types that correspond.
     */
    public final FightType[] getFightTypes() {
        return fightTypes;
    }

    /**
     * Gets the identification of the special bar for this interface.
     *
     * @return the identification of the special bar.
     */
    public final int getSpecialBar() {
        return specialBar;
    }

    /**
     * Gets the identification of the special meter for this interface.
     *
     * @return the identification of the special meter.
     */
    public final int getSpecialMeter() {
        return specialMeter;
    }

    public Optional<FightType> forFightButton(int button) {
        for (FightType type : fightTypes) {
            if (type.getButton() == button) {
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }

    public static boolean isSpecialButton(int button) {
        for (WeaponInterface weaponInterface : values()) {
            if (weaponInterface.specialButton == button) {
                return true;
            }
        }
        return false;
    }

    public static final WeaponInterface[] values = values();

    private static final Object2ObjectMap<String, WeaponInterface> newNameToValue
            = new Object2ObjectOpenHashMap<>(values.length);

    public static WeaponInterface forNewName(String newDefName) {
        return newNameToValue.get(newDefName);
    }

    static {
        for (WeaponInterface value : values) {
            newNameToValue.putIfAbsent(value.getNewDefName(), value);
        }
    }

}
