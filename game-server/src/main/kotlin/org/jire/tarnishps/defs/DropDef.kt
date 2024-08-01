package org.jire.tarnishps.defs

/**
 * @author Jire
 */
data class DropDef(
    val id: Int,
    val name: String,
    val members: Boolean,
    val quantity: String,
    val noted: Boolean,
    val rarity: Double,
    val rolls: Int
)
