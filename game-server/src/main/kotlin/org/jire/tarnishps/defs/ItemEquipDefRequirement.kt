package org.jire.tarnishps.defs

/**
 * @author Jire
 */
data class ItemEquipDefRequirement(
    val attack: Int,
    val defence: Int,
    val strength: Int,
    val hitpoints: Int,
    val ranged: Int,
    val prayer: Int,
    val magic: Int,
    val cooking: Int,
    val woodcutting: Int,
    val fletching: Int,
    val fishing: Int,
    val firemaking: Int,
    val crafting: Int,
    val smithing: Int,
    val mining: Int,
    val herblore: Int,
    val agility: Int,
    val thieving: Int,
    val slayer: Int,
    val farming: Int,
    val runecrafting: Int,
    val hunter: Int,
) {

    fun toArray() = intArrayOf(
        attack,
        defence,
        strength,
        hitpoints,
        ranged,
        prayer,
        magic,
        cooking,
        woodcutting,
        fletching,
        fishing,
        firemaking,
        crafting,
        smithing,
        mining,
        herblore,
        agility,
        thieving,
        slayer,
        farming,
        runecrafting,
        hunter,
    )

}
