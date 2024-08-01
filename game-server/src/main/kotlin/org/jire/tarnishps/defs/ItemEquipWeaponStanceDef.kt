package org.jire.tarnishps.defs

import com.google.gson.annotations.SerializedName

/**
 * @author Jire
 */
data class ItemEquipWeaponStanceDef(
    @SerializedName("combat_style") val combatStyle: String,
    @SerializedName("attack_type") val attackType: String,
    @SerializedName("attack_style") val attackStyle: String,
    val experience: String,
    val boosts: String?
)
