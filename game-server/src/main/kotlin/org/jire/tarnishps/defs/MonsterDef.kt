package org.jire.tarnishps.defs

import com.google.gson.annotations.SerializedName

/**
 * @author Jire
 */
data class MonsterDef(
    val id: Int,
    val name: String,
    @SerializedName("last_updated") val lastUpdated: String,
    val incomplete: Boolean,
    val members: Boolean,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("combat_level") val combatLevel: Int,
    val size: Int,
    val hitpoints: Int,
    @SerializedName("max_hit") val maxHit: Int,
    @SerializedName("attack_type") val attackType: Array<String>,
    @SerializedName("attack_speed") val attackSpeed: Int,
    val aggressive: Boolean,
    val poisonous: Boolean,
    val venomous: Boolean,
    @SerializedName("immune_poison") val immunePoison: Boolean,
    @SerializedName("immune_venom") val immuneVenom: Boolean,
    val attributes: Array<String>,
    val category: Array<String>,
    @SerializedName("slayer_monster") val slayerMonster: Boolean,
    @SerializedName("slayer_level") val slayerLevel: Int,
    @SerializedName("slayer_xp") val slayerXp: Double,
    @SerializedName("slayer_masters") val slayerMasters: Array<String>,
    val duplicate: Boolean,
    val examine: String,
    @SerializedName("wiki_name") val wikiName: String,
    @SerializedName("wiki_url") val wikiURL: String,
    @SerializedName("attack_level") val attackLevel: Int,
    @SerializedName("strength_level") val strengthLevel: Int,
    @SerializedName("defence_level") val defenceLevel: Int,
    @SerializedName("magic_level") val magicLevel: Int,
    @SerializedName("ranged_level") val rangedLevel: Int,
    @SerializedName("attack_bonus") val attackBonus: Int,
    @SerializedName("strength_bonus") val strengthBonus: Int,
    @SerializedName("attack_magic") val attackMagic: Int,
    @SerializedName("magic_bonus") val magicBonus: Int,
    @SerializedName("attack_ranged") val attackRanged: Int,
    @SerializedName("ranged_bonus") val rangedBonus: Int,
    @SerializedName("defence_stab") val defenceStab: Int,
    @SerializedName("defence_slash") val defenceSlash: Int,
    @SerializedName("defence_crush") val defenceCrush: Int,
    @SerializedName("defence_magic") val defenceMagic: Int,
    @SerializedName("defence_ranged") val defenceRanged: Int,
    val drops: Array<DropDef>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MonsterDef

        if (id != other.id) return false
        if (name != other.name) return false
        if (lastUpdated != other.lastUpdated) return false
        if (incomplete != other.incomplete) return false
        if (members != other.members) return false
        if (releaseDate != other.releaseDate) return false
        if (combatLevel != other.combatLevel) return false
        if (size != other.size) return false
        if (hitpoints != other.hitpoints) return false
        if (maxHit != other.maxHit) return false
        if (!attackType.contentEquals(other.attackType)) return false
        if (attackSpeed != other.attackSpeed) return false
        if (aggressive != other.aggressive) return false
        if (poisonous != other.poisonous) return false
        if (venomous != other.venomous) return false
        if (immunePoison != other.immunePoison) return false
        if (immuneVenom != other.immuneVenom) return false
        if (!attributes.contentEquals(other.attributes)) return false
        if (!category.contentEquals(other.category)) return false
        if (slayerMonster != other.slayerMonster) return false
        if (slayerLevel != other.slayerLevel) return false
        if (slayerXp != other.slayerXp) return false
        if (!slayerMasters.contentEquals(other.slayerMasters)) return false
        if (duplicate != other.duplicate) return false
        if (examine != other.examine) return false
        if (wikiName != other.wikiName) return false
        if (wikiURL != other.wikiURL) return false
        if (attackLevel != other.attackLevel) return false
        if (strengthLevel != other.strengthLevel) return false
        if (defenceLevel != other.defenceLevel) return false
        if (magicLevel != other.magicLevel) return false
        if (rangedLevel != other.rangedLevel) return false
        if (attackBonus != other.attackBonus) return false
        if (strengthBonus != other.strengthBonus) return false
        if (attackMagic != other.attackMagic) return false
        if (magicBonus != other.magicBonus) return false
        if (attackRanged != other.attackRanged) return false
        if (rangedBonus != other.rangedBonus) return false
        if (defenceStab != other.defenceStab) return false
        if (defenceSlash != other.defenceSlash) return false
        if (defenceCrush != other.defenceCrush) return false
        if (defenceMagic != other.defenceMagic) return false
        if (defenceRanged != other.defenceRanged) return false
        return drops.contentEquals(other.drops)
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + lastUpdated.hashCode()
        result = 31 * result + incomplete.hashCode()
        result = 31 * result + members.hashCode()
        result = 31 * result + releaseDate.hashCode()
        result = 31 * result + combatLevel
        result = 31 * result + size
        result = 31 * result + hitpoints
        result = 31 * result + maxHit
        result = 31 * result + attackType.contentHashCode()
        result = 31 * result + attackSpeed
        result = 31 * result + aggressive.hashCode()
        result = 31 * result + poisonous.hashCode()
        result = 31 * result + venomous.hashCode()
        result = 31 * result + immunePoison.hashCode()
        result = 31 * result + immuneVenom.hashCode()
        result = 31 * result + attributes.contentHashCode()
        result = 31 * result + category.contentHashCode()
        result = 31 * result + slayerMonster.hashCode()
        result = 31 * result + slayerLevel
        result = 31 * result + slayerXp.hashCode()
        result = 31 * result + slayerMasters.contentHashCode()
        result = 31 * result + duplicate.hashCode()
        result = 31 * result + examine.hashCode()
        result = 31 * result + wikiName.hashCode()
        result = 31 * result + wikiURL.hashCode()
        result = 31 * result + attackLevel
        result = 31 * result + strengthLevel
        result = 31 * result + defenceLevel
        result = 31 * result + magicLevel
        result = 31 * result + rangedLevel
        result = 31 * result + attackBonus
        result = 31 * result + strengthBonus
        result = 31 * result + attackMagic
        result = 31 * result + magicBonus
        result = 31 * result + attackRanged
        result = 31 * result + rangedBonus
        result = 31 * result + defenceStab
        result = 31 * result + defenceSlash
        result = 31 * result + defenceCrush
        result = 31 * result + defenceMagic
        result = 31 * result + defenceRanged
        result = 31 * result + drops.contentHashCode()
        return result
    }
}
