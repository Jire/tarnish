package org.jire.tarnishps

import it.unimi.dsi.fastutil.ints.Int2IntMap
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap
import java.io.File

/**
 * @author Jire
 */
object OldToNew {

    private lateinit var map: Int2IntMap

    @JvmStatic
    fun load() {
        val lines = File("data/def/npc/oldtonew.txt").readLines()
        map = Int2IntOpenHashMap(lines.size)
        lines.forEach {
            if (it.indexOf('=') != -1) {
                val split = it.split('=')
                val old = split[0].toInt()
                val new = split[1].toInt()
                map.put(old, new)
            }
        }
    }
    //315=308
    @JvmStatic
    operator fun get(oldId: Int) = map.getOrDefault(oldId, -1)

}
