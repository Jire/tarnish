package org.jire.tarnishps.defs

import com.google.gson.annotations.SerializedName

/**
 * @author Jire
 */
data class ItemEquipDef(
    @SerializedName("attack_stab") val attackStab: Int,
    @SerializedName("attack_slash") val attackSlash: Int,
    @SerializedName("attack_crush") val attackCrush: Int,
    @SerializedName("attack_magic") val attackMagic: Int,
    @SerializedName("attack_ranged") val attackRanged: Int,

    @SerializedName("defence_stab") val defenceStab: Int,
    @SerializedName("defence_slash") val defenceSlash: Int,
    @SerializedName("defence_crush") val defenceCrush: Int,
    @SerializedName("defence_magic") val defenceMagic: Int,
    @SerializedName("defence_ranged") val defenceRanged: Int,

    @SerializedName("melee_strength") val meleeStrength: Int,
    @SerializedName("ranged_strength") val rangedStrength: Int,

    @SerializedName("magic_damage") val magicDamage: Int,

    val prayer: Int,

    val slot: String,

    val requirements: ItemEquipDefRequirement?
) {

    fun toBonusArray() = intArrayOf(
        attackStab,
        attackSlash,
        attackCrush,
        attackMagic,
        attackRanged,

        defenceStab,
        defenceSlash,
        defenceCrush,
        defenceMagic,
        defenceRanged,

        meleeStrength,
        rangedStrength,
        magicDamage,
        prayer
    )

}
