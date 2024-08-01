package org.jire.tarnishps.defs

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.osroyale.game.world.entity.mob.npc.definition.NpcDefinition
import com.osroyale.game.world.entity.skill.Skill
import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import java.io.File

/**
 * @author Jire
 */
object MonsterDefLoader {

    @JvmField
    val map: Int2ObjectMap<MonsterDef> = Int2ObjectOpenHashMap()

    @JvmStatic
    @JvmOverloads
    fun load(
        gson: Gson = GsonBuilder()
            .setPrettyPrinting()
            .create()
    ) {
        for (file in File("data/def/monsters-json/").listFiles()!!) {
            if (file.extension != "json") continue
            val id = file.nameWithoutExtension.toIntOrNull() ?: continue
            val monsterDef = gson.fromJson(file.bufferedReader(), MonsterDef::class.java)
            map.put(id, monsterDef)
            //println(monsterDef)

           NpcDefinition.DEFINITIONS[id]?.run {
               combatLevel = monsterDef.combatLevel

               skills[Skill.HITPOINTS] = monsterDef.hitpoints
               skills[Skill.ATTACK] = monsterDef.attackLevel
               skills[Skill.STRENGTH] = monsterDef.strengthLevel
               skills[Skill.DEFENCE] = monsterDef.defenceLevel
               skills[Skill.MAGIC] = monsterDef.magicLevel
               skills[Skill.RANGED] = monsterDef.rangedLevel

               attackDelay = monsterDef.attackSpeed
               isAggressive = monsterDef.aggressive
               isPoisonImmunity = monsterDef.immunePoison
               isVenomImmunity = monsterDef.immuneVenom

               bonuses = intArrayOf(
                   0, 0, 0, 0, 0,

                   monsterDef.defenceStab,
                   monsterDef.defenceSlash,
                   monsterDef.defenceCrush,
                   monsterDef.defenceMagic,
                   monsterDef.defenceRanged,

                   monsterDef.strengthBonus,
                   monsterDef.rangedBonus,
                   monsterDef.magicBonus,

                   0
               )
           }
        }
    }

    @JvmStatic
    fun main(args: Array<String>) = load()

}