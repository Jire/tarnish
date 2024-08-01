package org.jire.tarnishps.defs

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.osroyale.game.world.entity.combat.ranged.RangedAmmunition
import com.osroyale.game.world.entity.combat.ranged.RangedWeaponDefinition
import com.osroyale.game.world.entity.combat.ranged.RangedWeaponType
import com.osroyale.game.world.entity.combat.weapon.WeaponInterface
import com.osroyale.game.world.items.ItemDefinition
import com.osroyale.game.world.items.containers.equipment.EquipmentType
import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import java.io.File

/**
 * @author Jire
 */
object ItemDefLoader {

    @JvmField
    val map: Int2ObjectMap<ItemDef> = Int2ObjectOpenHashMap()

    @JvmStatic
    @JvmOverloads
    fun load(
        gson: Gson = GsonBuilder()
            .setPrettyPrinting()
            .create()
    ) {
        for (file in File("data/def/items-json/").listFiles()!!) {
            if (file.extension != "json") continue
            val id = file.nameWithoutExtension.toIntOrNull() ?: continue
            val itemDef = gson.fromJson(file.bufferedReader(), ItemDef::class.java)

            map.put(id, itemDef)

            val oldDef = ItemDefinition.DEFINITIONS[id]
            if (oldDef == null) {
                ItemDefinition.DEFINITIONS[id] = ItemDefinition(id, itemDef.name).apply {
                    updateFrom(itemDef)
                }
            } else oldDef.updateFrom(itemDef)
        }
    }

    private fun ItemDefinition.updateFrom(itemDef: ItemDef) {
        val name = itemDef.name

        val noted = itemDef.linkedIdItem != 0 && itemDef.linkedIdNoted == 0
        isStackable = itemDef.stackable || noted
        isTradeable = itemDef.tradeable
        if (noted) unnotedId = itemDef.linkedIdItem
        else {
            if (itemDef.linkedIdNoted != 0)
                notedId = itemDef.linkedIdNoted
        }

        base_value = itemDef.cost
        lowAlch = itemDef.lowAlch
        highAlch = itemDef.highAlch

        weight = itemDef.weight

        val equipment = itemDef.equipment
        if (equipment != null) {
            val equipmentSlot = equipment.slot
            isTwoHanded = equipmentSlot == "2h"

            bonuses = equipment.toBonusArray()

            val req = equipment.requirements
            if (req != null)
                requirements = req.toArray()

            if (equipmentType == null || equipmentType == EquipmentType.NOT_WIELDABLE) {
                if (isTwoHanded) this.equipmentType = EquipmentType.WEAPON
                else if (name.startsWith("hood", true)
                    || name.endsWith("hood", true)
                ) {
                    equipmentType = EquipmentType.FACE
                } else if (name.contains("full helm", true)
                    || name == "Masori mask"
                    || name == "Masori mask (f)"
                    || name == "Gas mask"
                ) {
                    equipmentType = EquipmentType.HELM
                } else if (name.contains("bloodbark helm", true)) {
                    equipmentType = EquipmentType.HAT
                } else if (name.contains("med helm", true)) {
                    equipmentType = EquipmentType.FACE
                } else if(name.contains("dragon hunter lance", true)) {
                    equipmentType = EquipmentType.WEAPON
                    stand_animation = 813
                    walk_animation = 1205
                    run_animation = 2563
                    weaponInterface = WeaponInterface.HUNTER_LANCE
                } else if(name.contains("sled", true)) {
                    equipmentType = EquipmentType.WEAPON
                    stand_animation = 1461
                    walk_animation = 1468
                    run_animation = 1467
                } else if(name.contains("Tumeken's shadow", true)) {
                    equipmentType = EquipmentType.WEAPON
                    stand_animation = 9494
                    walk_animation = 1703
                    run_animation = 1707
                    weaponInterface = WeaponInterface.SHADOW
                } else if (name.contains("Inquisitor's mace", true)) {
                    weaponInterface = WeaponInterface.INQUISITOR_MACE
                    equipmentType = EquipmentType.WEAPON
                } else if (name.contains("helm", true)) {
                    equipmentType =
                        if (name.startsWith("dharok", true)) {
                            EquipmentType.FACE
                        } else {
                            EquipmentType.HELM
                        }
                } else if (name.contains("mask", true)) {
                    equipmentType = EquipmentType.MASK
                } else {
                    val newEquipmentType = EquipmentType.forNewName(equipmentSlot)
                    if (newEquipmentType != null)
                        equipmentType = newEquipmentType
                }
            }
        }

        val weapon = itemDef.weapon
        if (weapon != null) {
            if (weaponInterface == null) {
                weaponInterface = WeaponInterface.forNewName(weapon.weaponType)
            }

            var replaceRangedDef = rangedDefinition.isEmpty
            if (!replaceRangedDef) {
                val def = rangedDefinition.get()
                replaceRangedDef = def.allowed == null || def.allowed.firstOrNull() == null
            }
            if (replaceRangedDef) {
                val type = RangedWeaponType.THROWN
                val allowed = RangedAmmunition.forItemId(id)
                if (allowed != null) {
                    setRangedDefinition(RangedWeaponDefinition(type, allowed))
                }
            }
        }
    }

    @JvmStatic
    fun main(args: Array<String>) = load()

}