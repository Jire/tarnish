package org.jire.tarnishps.defs

import com.google.gson.annotations.SerializedName

/**
 * @author Jire
 */
data class ItemEquipWeaponDef(
    @SerializedName("attack_speed") val attackSpeed: Int,
    @SerializedName("weapon_type") val weaponType: String,
    val stances: Array<ItemEquipWeaponStanceDef>
)
