package org.jire.tarnishps.defs

import com.google.gson.annotations.SerializedName

/**
 * @author Jire
 */
data class ItemDef(
    val id: Int,
    val name: String,
    @SerializedName("last_updated") val lastUpdated: String,
    val incomplete: Boolean,
    val members: Boolean,
    val tradeable: Boolean,
    @SerializedName("tradeable_on_ge") val tradeableOnGe: Boolean,
    val stackable: Boolean,
    val stacked: Int,
    val noted: Boolean,
    val noteable: Boolean,
    @SerializedName("linked_id_item") val linkedIdItem: Int,
    @SerializedName("linked_id_noted") val linkedIdNoted: Int,
    @SerializedName("linked_id_placeholder") val linkedIdPlaceholder: Int,
    val placeholder: Boolean,
    val equipable: Boolean,
    @SerializedName("equipable_by_player") val equipableByPlayer: Boolean,
    @SerializedName("equipable_weapon") val equipableWeapon: Boolean,
    val cost: Int,
    @SerializedName("lowalch") val lowAlch: Int,
    @SerializedName("highalch") val highAlch: Int,
    val weight: Double,
    @SerializedName("buy_limit") val buyLimit: Int,
    @SerializedName("quest_item") val questItem: Boolean,
    @SerializedName("release_date") val releaseDate: String,
    val duplicate: Boolean,
    val examine: String,
    val icon: String,
    @SerializedName("wiki_name") val wikiName: String,
    @SerializedName("wiki_url") val wikiURL: String,
    val equipment: ItemEquipDef?,
    val weapon: ItemEquipWeaponDef?
)
