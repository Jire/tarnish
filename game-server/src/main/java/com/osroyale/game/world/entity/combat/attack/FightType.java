package com.osroyale.game.world.entity.combat.attack;

import com.osroyale.game.world.items.containers.equipment.Equipment;

/**
 * The enumerated type whose elements represent the fighting types.
 *
 * @author lare96 <http://github.com/lare96>
 */
public enum FightType {
    STAFF_BASH(401, 5, 1, 43, 0, 6137, Equipment.CRUSH_OFFENSE, FightStyle.ACCURATE),
    STAFF_POUND(406, 5, 1, 43, 1, 6136, Equipment.CRUSH_OFFENSE, FightStyle.AGGRESSIVE),
    STAFF_FOCUS(406, 5, 1, 43, 2, 6135, Equipment.CRUSH_OFFENSE, FightStyle.DEFENSIVE),

    NIGHTMARE_STAFF_BASH(4505, 5, 1, 43, 0, 6137, Equipment.CRUSH_OFFENSE, FightStyle.ACCURATE),
    NIGHTMARE_STAFF_POUND(4505, 5, 1, 43, 1, 6136, Equipment.CRUSH_OFFENSE, FightStyle.AGGRESSIVE),
    NIGHTMARE_STAFF_FOCUS(4505, 5, 1, 43, 2, 6135, Equipment.CRUSH_OFFENSE, FightStyle.DEFENSIVE),

    WARHAMMER_POUND(401, 6, 1, 43, 0, 433, Equipment.CRUSH_OFFENSE, FightStyle.ACCURATE),
    WARHAMMER_PUMMEL(401, 6, 1, 43, 1, 432, Equipment.CRUSH_OFFENSE, FightStyle.AGGRESSIVE),
    WARHAMMER_BLOCK(401, 6, 1, 43, 2, 431, Equipment.CRUSH_OFFENSE, FightStyle.DEFENSIVE),

    BULWARK_POUND(7511, 7, 1, 43, 0, 433, Equipment.CRUSH_OFFENSE, FightStyle.ACCURATE),
    BULWARK_PUMMEL(7511, 7, 1, 43, 1, 432, Equipment.CRUSH_OFFENSE, FightStyle.AGGRESSIVE),
    BULWARK_BLOCK(7511, 7, 1, 43, 2, 431, Equipment.CRUSH_OFFENSE, FightStyle.DEFENSIVE),

    GRANITE_MAUL_POUND(1665, 6, 1, 43, 0, 1665, Equipment.CRUSH_OFFENSE, FightStyle.ACCURATE),
    GRANITE_MAUL_PUMMEL(1665, 6, 1, 43, 1, 1665, Equipment.CRUSH_OFFENSE, FightStyle.AGGRESSIVE),
    GRANITE_MAUL_BLOCK(1665, 6, 1, 43, 2, 1665, Equipment.CRUSH_OFFENSE, FightStyle.DEFENSIVE),

    ELDER_MAUL_POUND(7516, 6, 1, 43, 0, 1665, Equipment.CRUSH_OFFENSE, FightStyle.ACCURATE),
    ELDER_MAUL_PUMMEL(7516, 6, 1, 43, 1, 1665, Equipment.CRUSH_OFFENSE, FightStyle.AGGRESSIVE),
    ELDER_MAUL_BLOCK(7516, 6, 1, 43, 2, 1665, Equipment.CRUSH_OFFENSE, FightStyle.DEFENSIVE),

    SCYTHE_REAP(414, 7, 1, 43, 0, 782, Equipment.SLASH_OFFENSE, FightStyle.ACCURATE),
    SCYTHE_CHOP(382, 7, 1, 43, 1, 784, Equipment.STAB_OFFENSE, FightStyle.AGGRESSIVE),
    SCYTHE_JAB(2066, 7, 1, 43, 2, 785, Equipment.CRUSH_OFFENSE, FightStyle.CONTROLLED),
    SCYTHE_BLOCK(382, 7, 1, 43, 3, 783, Equipment.SLASH_OFFENSE, FightStyle.DEFENSIVE),

    VITUR_REAP(8056, 5, 1, 43, 0, 782, Equipment.SLASH_OFFENSE, FightStyle.ACCURATE),
    VITUR_CHOP(8056, 5, 1, 43, 1, 784, Equipment.STAB_OFFENSE, FightStyle.AGGRESSIVE),
    VITUR_JAB(8056, 5, 1, 43, 2, 785, Equipment.CRUSH_OFFENSE, FightStyle.CONTROLLED),
    VITUR_BLOCK(8056, 5, 1, 43, 3, 783, Equipment.SLASH_OFFENSE, FightStyle.DEFENSIVE),

    BATTLEAXE_CHOP(401, 6, 1, 43, 0, 1704, Equipment.SLASH_OFFENSE, FightStyle.ACCURATE),
    BATTLEAXE_HACK(401, 6, 1, 43, 1, 1707, Equipment.SLASH_OFFENSE, FightStyle.AGGRESSIVE),
    BATTLEAXE_SMASH(401, 6, 1, 43, 2, 1706, Equipment.CRUSH_OFFENSE, FightStyle.AGGRESSIVE),
    BATTLEAXE_BLOCK(401, 6, 1, 43, 3, 1705, Equipment.SLASH_OFFENSE, FightStyle.DEFENSIVE),

    GREATAXE_CHOP(2062, 6, 1, 43, 0, 1704, Equipment.SLASH_OFFENSE, FightStyle.ACCURATE),
    GREATAXE_HACK(2062, 6, 1, 43, 1, 1707, Equipment.SLASH_OFFENSE, FightStyle.AGGRESSIVE),
    GREATAXE_SMASH(2066, 6, 1, 43, 2, 1706, Equipment.CRUSH_OFFENSE, FightStyle.AGGRESSIVE),
    GREATAXE_BLOCK(2062, 6, 1, 43, 3, 1705, Equipment.SLASH_OFFENSE, FightStyle.DEFENSIVE),

    CROSSBOW_ACCURATE(4230, 6, 7, 43, 0, 1772, Equipment.RANGED_OFFENSE, FightStyle.ACCURATE),
    CROSSBOW_RAPID(4230, 5, 7, 43, 1, 1771, Equipment.RANGED_OFFENSE, FightStyle.AGGRESSIVE),
    CROSSBOW_LONGRANGE(4230, 6, 9, 43, 2, 1770, Equipment.RANGED_OFFENSE, FightStyle.DEFENSIVE),
    ZARYTE_ACCURATE(9166, 6, 7, 43, 0, 1772, Equipment.RANGED_OFFENSE, FightStyle.ACCURATE),
    ZARYTE_RAPID(9166, 5, 7, 43, 1, 1771, Equipment.RANGED_OFFENSE, FightStyle.AGGRESSIVE),
    ZARYTE_LONGRANGE(9166, 6, 9, 43, 2, 1770, Equipment.RANGED_OFFENSE, FightStyle.DEFENSIVE),

    KARIL_CROSSBOW_ACCURATE(2075, 6, 7, 43, 0, 1772, Equipment.RANGED_OFFENSE, FightStyle.ACCURATE),
    KARIL_CROSSBOW_RAPID(2075, 5, 7, 43, 1, 1771, Equipment.RANGED_OFFENSE, FightStyle.AGGRESSIVE),
    KARIL_CROSSBOW_LONGRANGE(2075, 6, 9, 43, 2, 1770, Equipment.RANGED_OFFENSE, FightStyle.DEFENSIVE),

    BALLISTA_ACCURATE(7218, 7, 10, 43, 0, 1772, Equipment.RANGED_OFFENSE, FightStyle.ACCURATE),
    BALLISTA_RAPID(7218, 6, 10, 43, 1, 1771, Equipment.RANGED_OFFENSE, FightStyle.AGGRESSIVE),
    BALLISTA_LONGRANGE(7218, 7, 10, 43, 2, 1770, Equipment.RANGED_OFFENSE, FightStyle.DEFENSIVE),

    SHORTBOW_ACCURATE(426, 4, 7, 43, 0, 1772, Equipment.RANGED_OFFENSE, FightStyle.ACCURATE),
    SHORTBOW_RAPID(426, 3, 7, 43, 1, 1771, Equipment.RANGED_OFFENSE, FightStyle.AGGRESSIVE),
    SHORTBOW_LONGRANGE(426, 4, 9, 43, 2, 1770, Equipment.RANGED_OFFENSE, FightStyle.DEFENSIVE),

    LONGBOW_ACCURATE(426, 6, 9, 43, 0, 1772, Equipment.RANGED_OFFENSE, FightStyle.ACCURATE),
    LONGBOW_RAPID(426, 5, 9, 43, 1, 1771, Equipment.RANGED_OFFENSE, FightStyle.AGGRESSIVE),
    LONGBOW_LONGRANGE(426, 6, 10, 43, 2, 1770, Equipment.RANGED_OFFENSE, FightStyle.DEFENSIVE),

    DARK_BOW_ACCURATE(426, 9, 9, 43, 0, 1772, Equipment.RANGED_OFFENSE, FightStyle.ACCURATE),
    DARK_BOW_RAPID(426, 8, 9, 43, 1, 1771, Equipment.RANGED_OFFENSE, FightStyle.AGGRESSIVE),
    DARK_BOW_LONGRANGE(426, 9, 10, 43, 2, 1770, Equipment.RANGED_OFFENSE, FightStyle.DEFENSIVE),

    DAGGER_STAB(412, 4, 1, 43, 0, 2282, Equipment.STAB_OFFENSE, FightStyle.ACCURATE),
    DAGGER_LUNGE(412, 4, 1, 43, 1, 2285, Equipment.STAB_OFFENSE, FightStyle.AGGRESSIVE),
    DAGGER_SLASH(395, 4, 1, 43, 2, 2284, Equipment.STAB_OFFENSE, FightStyle.AGGRESSIVE),
    DAGGER_BLOCK(412, 4, 1, 43, 3, 2283, Equipment.STAB_OFFENSE, FightStyle.DEFENSIVE),
    GHRAZI_STAB(8145, 4, 1, 43, 0, 2282, Equipment.STAB_OFFENSE, FightStyle.ACCURATE),
    GHRAZI_LUNGE(8145, 4, 1, 43, 1, 2285, Equipment.STAB_OFFENSE, FightStyle.AGGRESSIVE),
    GHRAZI_SLASH(390, 4, 1, 43, 2, 2284, Equipment.STAB_OFFENSE, FightStyle.AGGRESSIVE),
    GHRAZI_BLOCK(8145, 4, 1, 43, 3, 2283, Equipment.STAB_OFFENSE, FightStyle.DEFENSIVE),

    DRAGON_DAGGER_STAB(402, 4, 1, 43, 0, 2282, Equipment.STAB_OFFENSE, FightStyle.ACCURATE),
    DRAGON_DAGGER_LUNGE(402, 4, 1, 43, 1, 2285, Equipment.STAB_OFFENSE, FightStyle.AGGRESSIVE),
    DRAGON_DAGGER_SLASH(395, 4, 1, 43, 2, 2284, Equipment.STAB_OFFENSE, FightStyle.AGGRESSIVE),
    DRAGON_DAGGER_BLOCK(402, 4, 1, 43, 3, 2283, Equipment.STAB_OFFENSE, FightStyle.DEFENSIVE),

    SWORD_STAB(412, 4, 1, 43, 0, 2282, Equipment.STAB_OFFENSE, FightStyle.ACCURATE),
    SWORD_LUNGE(412, 4, 1, 43, 1, 2285, Equipment.STAB_OFFENSE, FightStyle.AGGRESSIVE),
    SWORD_SLASH(395, 4, 1, 43, 2, 2284, Equipment.SLASH_OFFENSE, FightStyle.AGGRESSIVE),
    SWORD_BLOCK(412, 4, 1, 43, 3, 2283, Equipment.STAB_OFFENSE, FightStyle.DEFENSIVE),

    FANG_STAB(9471, 5, 1, 43, 0, 2429, Equipment.STAB_OFFENSE, FightStyle.ACCURATE),
    FANG_LUNGE(9471, 5, 1, 43, 1, 2285, Equipment.STAB_OFFENSE, FightStyle.AGGRESSIVE),
    FANG_SLASH(390, 5, 1, 43, 2, 2284, Equipment.SLASH_OFFENSE, FightStyle.AGGRESSIVE),
    FANG_BLOCK(9471, 5, 1, 43, 3, 2283, Equipment.STAB_OFFENSE, FightStyle.DEFENSIVE),

    SCIMITAR_CHOP(390, 4, 1, 43, 0, 2429, Equipment.SLASH_OFFENSE, FightStyle.ACCURATE),
    SCIMITAR_SLASH(390, 4, 1, 43, 1, 2432, Equipment.SLASH_OFFENSE, FightStyle.AGGRESSIVE),
    SCIMITAR_LUNGE(386, 4, 1, 43, 2, 2431, Equipment.STAB_OFFENSE, FightStyle.CONTROLLED),
    SCIMITAR_BLOCK(390, 4, 1, 43, 3, 2430, Equipment.SLASH_OFFENSE, FightStyle.DEFENSIVE),

    LONGSWORD_CHOP(390, 5, 1, 43, 0, 2429, Equipment.SLASH_OFFENSE, FightStyle.ACCURATE),
    LONGSWORD_SLASH(390, 5, 1, 43, 1, 2432, Equipment.SLASH_OFFENSE, FightStyle.AGGRESSIVE),
    LONGSWORD_LUNGE(386, 5, 1, 43, 2, 2431, Equipment.STAB_OFFENSE, FightStyle.CONTROLLED),
    LONGSWORD_BLOCK(390, 5, 1, 43, 3, 2430, Equipment.SLASH_OFFENSE, FightStyle.DEFENSIVE),

    MACE_POUND(401, 5, 1, 43, 0, 3802, Equipment.CRUSH_OFFENSE, FightStyle.ACCURATE),
    MACE_PUMMEL(401, 5, 1, 43, 1, 3805, Equipment.CRUSH_OFFENSE, FightStyle.AGGRESSIVE),
    MACE_SPIKE(400, 5, 1, 43, 2, 3804, Equipment.STAB_OFFENSE, FightStyle.CONTROLLED),
    MACE_BLOCK(401, 5, 1, 43, 3, 3803, Equipment.CRUSH_OFFENSE, FightStyle.DEFENSIVE),


    VIGGORA_MACE_POUND(245, 5, 1, 43, 0, 3802, Equipment.CRUSH_OFFENSE, FightStyle.ACCURATE),
    VIGGORA_MACE_PUMMEL(245, 5, 1, 43, 1, 3805, Equipment.CRUSH_OFFENSE, FightStyle.AGGRESSIVE),
    VIGGORA_MACE_SPIKE(245, 5, 1, 43, 2, 3804, Equipment.STAB_OFFENSE, FightStyle.CONTROLLED),
    VIGGORA_MACE_BLOCK(245, 5, 1, 43, 3, 3803, Equipment.CRUSH_OFFENSE, FightStyle.DEFENSIVE),



    INQUISITOR_MACE_POUND(4503, 5, 1, 43, 0, 3802, Equipment.CRUSH_OFFENSE, FightStyle.ACCURATE),

    INQUISITOR_MACE_PUMMEL(4503, 5, 1, 43, 1, 3805, Equipment.CRUSH_OFFENSE, FightStyle.AGGRESSIVE),
    INQUISITOR_MACE_SPIKE(400, 5, 1, 43, 2, 3804, Equipment.STAB_OFFENSE, FightStyle.CONTROLLED),
    INQUISITOR_MACE_BLOCK(4503, 5, 1, 43, 3, 3803, Equipment.CRUSH_OFFENSE, FightStyle.DEFENSIVE),


    FLAIL_POUND(2062, 5, 1, 43, 0, 3802, Equipment.CRUSH_OFFENSE, FightStyle.ACCURATE),
    FLAIL_PUMMEL(2062, 5, 1, 43, 1, 3805, Equipment.CRUSH_OFFENSE, FightStyle.AGGRESSIVE),
    FLAIL_SPIKE(2062, 5, 1, 43, 2, 3804, Equipment.STAB_OFFENSE, FightStyle.CONTROLLED),
    FLAIL_BLOCK(2062, 5, 1, 43, 3, 3803, Equipment.CRUSH_OFFENSE, FightStyle.DEFENSIVE),

    KNIFE_ACCURATE(929, 3, 6, 43, 0, 4454, Equipment.RANGED_OFFENSE, FightStyle.ACCURATE),
    KNIFE_RAPID(929, 2, 6, 43, 1, 4453, Equipment.RANGED_OFFENSE, FightStyle.AGGRESSIVE),
    KNIFE_LONGRANGE(929, 3, 4, 43, 2, 4452, Equipment.RANGED_OFFENSE, FightStyle.DEFENSIVE),

    SPEAR_LUNGE(2080, 5, 1, 43, 0, 4685, Equipment.STAB_OFFENSE, FightStyle.CONTROLLED),
    SPEAR_SWIPE(2081, 5, 1, 43, 1, 4688, Equipment.SLASH_OFFENSE, FightStyle.CONTROLLED),
    SPEAR_POUND(2080, 5, 1, 43, 2, 4687, Equipment.CRUSH_OFFENSE, FightStyle.CONTROLLED),
    SPEAR_BLOCK(2082, 5, 1, 43, 3, 4686, Equipment.STAB_OFFENSE, FightStyle.DEFENSIVE),

    HUNTER_LANCE_LUNGE(8288, 5, 1, 43, 0, 4685, Equipment.STAB_OFFENSE, FightStyle.CONTROLLED),
    HUNTER_LANCE_SWIPE(8289, 5, 1, 43, 1, 4688, Equipment.SLASH_OFFENSE, FightStyle.CONTROLLED),
    HUNTER_LANCE_POUND(8290, 5, 1, 43, 2, 4687, Equipment.CRUSH_OFFENSE, FightStyle.CONTROLLED),
    HUNTER_LANCE_BLOCK(8288, 5, 1, 43, 3, 4686, Equipment.STAB_OFFENSE, FightStyle.DEFENSIVE),

    TWOHANDEDSWORD_CHOP(407, 7, 1, 43, 0, 4711, Equipment.SLASH_OFFENSE, FightStyle.ACCURATE),
    TWOHANDEDSWORD_SLASH(407, 7, 1, 43, 1, 4713, Equipment.SLASH_OFFENSE, FightStyle.AGGRESSIVE),
    TWOHANDEDSWORD_SMASH(406, 7, 1, 43, 2, 4714, Equipment.CRUSH_OFFENSE, FightStyle.AGGRESSIVE),
    TWOHANDEDSWORD_BLOCK(407, 7, 1, 43, 3, 4712, Equipment.SLASH_OFFENSE, FightStyle.DEFENSIVE),

    GODSWORD_CHOP(7046, 6, 1, 43, 0, 4711, Equipment.SLASH_OFFENSE, FightStyle.ACCURATE),
    GODSWORD_SLASH(7045, 6, 1, 43, 1, 4714, Equipment.SLASH_OFFENSE, FightStyle.AGGRESSIVE),
    GODSWORD_SMASH(7054, 6, 1, 43, 2, 4713, Equipment.CRUSH_OFFENSE, FightStyle.AGGRESSIVE),
    GODSWORD_BLOCK(7055, 6, 1, 43, 3, 4712, Equipment.SLASH_OFFENSE, FightStyle.DEFENSIVE),

    SARADOMIN_CHOP(7046, 4, 1, 43, 0, 4711, Equipment.SLASH_OFFENSE, FightStyle.ACCURATE),
    SARADOMIN_SMASH(7054, 4, 1, 43, 1, 4713, Equipment.CRUSH_OFFENSE, FightStyle.AGGRESSIVE),
    SARADOMIN_SLASH(7045, 4, 1, 43, 2, 4714, Equipment.SLASH_OFFENSE, FightStyle.AGGRESSIVE),
    SARADOMIN_BLOCK(7055, 4, 1, 43, 3, 4712, Equipment.SLASH_OFFENSE, FightStyle.DEFENSIVE),

    BLUDGEN_CHOP(7054, 4, 1, 43, 0, 4711, Equipment.SLASH_OFFENSE, FightStyle.ACCURATE),
    BLUDGEN_SLASH(7054, 4, 1, 43, 1, 4713, Equipment.SLASH_OFFENSE, FightStyle.AGGRESSIVE),
    BLUDGEN_SMASH(7054, 4, 1, 43, 2, 4714, Equipment.CRUSH_OFFENSE, FightStyle.AGGRESSIVE),
    BLUDGEN_BLOCK(7054, 4, 1, 43, 3, 4712, Equipment.SLASH_OFFENSE, FightStyle.DEFENSIVE),

    SAELDOR_STAB(390, 4, 1, 43, 0, 2429, Equipment.SLASH_OFFENSE, FightStyle.ACCURATE),
    SAELDOR_SLASH(390, 4, 1, 43, 1, 2432, Equipment.SLASH_OFFENSE, FightStyle.AGGRESSIVE),
    SAELDOR_CRUSH(386, 4, 1, 43, 2, 2431, Equipment.CRUSH_OFFENSE, FightStyle.CONTROLLED),
    SAELDOR_BLOCK(390, 4, 1, 43, 3, 2430, Equipment.SLASH_OFFENSE, FightStyle.DEFENSIVE),

   /* GHRAZI_STAB(390, 4, 1, 43, 0, 2282, Equipment.STAB_OFFENSE, FightStyle.ACCURATE),
    GHRAZI_LUNGE(8145, 4, 1, 43, 1, 2285, Equipment.STAB_OFFENSE, FightStyle.AGGRESSIVE),
    GHRAZI_SLASH(390, 4, 1, 43, 2, 2284, Equipment.SLASH_OFFENSE, FightStyle.AGGRESSIVE),
    GHRAZI_BLOCK(8145, 4, 1, 43, 3, 2430, Equipment.STAB_OFFENSE, FightStyle.DEFENSIVE),*/

    PICKAXE_SPIKE(400, 4, 1, 43, 0, 5567, Equipment.STAB_OFFENSE, FightStyle.ACCURATE),
    PICKAXE_IMPALE(400, 4, 1, 43, 1, 5579, Equipment.STAB_OFFENSE, FightStyle.AGGRESSIVE),
    PICKAXE_SMASH(401, 4, 1, 43, 2, 5578, Equipment.CRUSH_OFFENSE, FightStyle.AGGRESSIVE),
    PICKAXE_BLOCK(400, 4, 1, 43, 3, 5577, Equipment.STAB_OFFENSE, FightStyle.DEFENSIVE),

    CLAWS_CHOP(393, 4, 1, 43, 0, 7768, Equipment.SLASH_OFFENSE, FightStyle.ACCURATE),
    CLAWS_SLASH(393, 4, 1, 43, 1, 7771, Equipment.SLASH_OFFENSE, FightStyle.AGGRESSIVE),
    CLAWS_LUNGE(393, 4, 1, 43, 2, 7770, Equipment.STAB_OFFENSE, FightStyle.CONTROLLED),
    CLAWS_BLOCK(393, 4, 1, 43, 3, 7769, Equipment.SLASH_OFFENSE, FightStyle.DEFENSIVE),

    HALBERD_JAB(440, 7, 2, 43, 0, 8466, Equipment.STAB_OFFENSE, FightStyle.CONTROLLED),
    HALBERD_SWIPE(440, 7, 2, 43, 1, 8468, Equipment.SLASH_OFFENSE, FightStyle.AGGRESSIVE),
    HALBERD_FEND(440, 7, 2, 43, 2, 8467, Equipment.STAB_OFFENSE, FightStyle.DEFENSIVE),

    UNARMED_PUNCH(422, 4, 1, 43, 0, 5860, Equipment.CRUSH_OFFENSE, FightStyle.ACCURATE),
    UNARMED_KICK(423, 4, 1, 43, 1, 5862, Equipment.CRUSH_OFFENSE, FightStyle.AGGRESSIVE),
    UNARMED_BLOCK(422, 4, 1, 43, 2, 5861, Equipment.CRUSH_OFFENSE, FightStyle.DEFENSIVE),

    WHIP_FLICK(1658, 4, 1, 43, 0, 12298, Equipment.SLASH_OFFENSE, FightStyle.ACCURATE),
    WHIP_LASH(1658, 4, 1, 43, 1, 12297, Equipment.SLASH_OFFENSE, FightStyle.CONTROLLED),
    WHIP_DEFLECT(1658, 4, 1, 43, 2, 12296, Equipment.SLASH_OFFENSE, FightStyle.DEFENSIVE),

    THROWNAXE_ACCURATE(929, 5, 4, 43, 0, 4454, Equipment.RANGED_OFFENSE, FightStyle.ACCURATE),
    THROWNAXE_RAPID(929, 4, 4, 43, 1, 4453, Equipment.RANGED_OFFENSE, FightStyle.AGGRESSIVE),
    THROWNAXE_LONGRANGE(929, 5, 6, 43, 2, 4452, Equipment.RANGED_OFFENSE, FightStyle.DEFENSIVE),

    DART_ACCURATE(929, 3, 5, 43, 0, 4454, Equipment.RANGED_OFFENSE, FightStyle.ACCURATE),
    DART_RAPID(929, 2, 5, 43, 1, 4453, Equipment.RANGED_OFFENSE, FightStyle.AGGRESSIVE),
    DART_LONGRANGE(929, 3, 3, 43, 2, 4452, Equipment.RANGED_OFFENSE, FightStyle.DEFENSIVE),

    TRIDENT_ACCURATE(929, 4, 8, 43, 0, 791, Equipment.MAGIC_OFFENSE, FightStyle.ACCURATE),
    TRIDENT_RAPID(929, 4, 8, 43, 1, 791, Equipment.MAGIC_OFFENSE, FightStyle.AGGRESSIVE),
    TRIDENT_LONGRANGE(929, 4, 10, 43, 2, 791, Equipment.MAGIC_OFFENSE, FightStyle.DEFENSIVE),

    SHADOW_ACCURATE(9493, 10, 8, 43, 0, 4454, Equipment.MAGIC_OFFENSE, FightStyle.ACCURATE),
    SHADOW_RAPID(9493, 9, 8, 43, 1, 4453, Equipment.MAGIC_OFFENSE, FightStyle.AGGRESSIVE),
    SHADOW_LONGRANGE(9493, 10, 8, 43, 2, 4452, Equipment.MAGIC_OFFENSE, FightStyle.DEFENSIVE),

    BLOWPIPE_ACCURATE(5061, 4, 7, 43, 0, 4454, Equipment.RANGED_OFFENSE, FightStyle.ACCURATE),
    BLOWPIPE_RAPID(5061, 3, 7, 43, 1, 4453, Equipment.RANGED_OFFENSE, FightStyle.AGGRESSIVE),
    BLOWPIPE_LONGRANGE(5061, 4, 7, 43, 2, 4452, Equipment.RANGED_OFFENSE, FightStyle.DEFENSIVE),

    SHORT_FUSE(2779, 4, 8, 443, 0, 24059, Equipment.RANGED_OFFENSE, FightStyle.ACCURATE),
    MEDIUM_FUSE(2779, 3, 8, 43, 2, 24060, Equipment.RANGED_OFFENSE, FightStyle.AGGRESSIVE),
    LONG_FUSE(2779, 4, 10, 43, 3, 24061, Equipment.RANGED_OFFENSE, FightStyle.DEFENSIVE),

    SCORCH(5247, 5, 1, 43, 0, 24078, Equipment.SLASH_OFFENSE, FightStyle.ACCURATE),
    FLARE(5247, 5, 1, 43, 2, 24079, Equipment.RANGED_OFFENSE, FightStyle.ACCURATE),
    BLAZE(5247, 5, 1, 43, 3, 24080, Equipment.MAGIC_OFFENSE, FightStyle.ACCURATE);

    /** The animation executed when this type is active. */
    private final int animation;

    /** The attack delay. */
    private final int delay;

    /** The attack distance. */
    private final int distance;

    /** The parent config identification. */
    private final int parent;

    /** The child config identification. */
    private final int child;

    /** The style button identification. */
    private final int button;

    /** The type of bonus this global attributes to. */
    private final int bonus;

    /** The style active when this type is active. */
    private final FightStyle style;

    /**
     * Creates a new {@link FightType}.
     *
     * @param animation the animation executed when this type is active
     * @param delay     the attack delay
     * @param parent    the parent config identification
     * @param child     the child config identification
     * @param bonus     the type of bonus this global will apply
     * @param style     the style active when this type is active
     */
    FightType(int animation, int delay, int distance, int parent, int child, int button, int bonus, FightStyle style) {
        this.animation = animation;
        this.delay = delay;
        this.distance = distance;
        this.parent = parent;
        this.child = child;
        this.button = button;
        this.bonus = bonus;
        this.style = style;
    }

    /**
     * Determines the corresponding bonus for this fight type.
     *
     * @return the corresponding.
     */
    public final int getCorrespondingBonus() {
        switch (getBonus()) {
            case Equipment.CRUSH_OFFENSE:
                return Equipment.CRUSH_DEFENCE;
            case Equipment.MAGIC_OFFENSE:
                return Equipment.MAGIC_DEFENSE;
            case Equipment.RANGED_OFFENSE:
                return Equipment.RANGED_DEFENSE;
            case Equipment.SLASH_OFFENSE:
                return Equipment.SLASH_DEFENSE;
            case Equipment.STAB_OFFENSE:
                return Equipment.STAB_DEFENSE;
            default:
                return Equipment.CRUSH_DEFENCE;
        }
    }

    /**
     * Gets the animation executed when this type is active.
     *
     * @return the animation executed
     */
    public final int getAnimation() {
        return animation;
    }

    /**
     * Gets the attack delay.
     *
     * @return the attack delay
     */
    public final int getDelay() {
        return delay;
    }

    /**
     * Gets the attack distance.
     * @return the attack distance
     */
    public int getDistance() {
        return distance;
    }

    /**
     * Gets the parent config identification.
     *
     * @return the parent config
     */
    public final int getParent() {
        return parent;
    }

    /**
     * Gets the child config identification.
     *
     * @return the child config
     */
    public final int getChild() {
        return child;
    }

    /**
     * Gets the type of bonus this global will apply
     *
     * @return the bonus type
     */
    public final int getBonus() {
        return bonus;
    }

    /**
     * Gets the style active when this type is active.
     *
     * @return the fighting style
     */
    public final FightStyle getStyle() {
        return style;
    }

    public int getButton() {
        return button;
    }
}
