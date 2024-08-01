package org.jire.tarnishps.event.npc

import com.osroyale.game.event.impl.ItemOnNpcEvent
import com.osroyale.game.plugin.PluginManager
import com.osroyale.game.world.World
import com.osroyale.game.world.entity.mob.player.Player
import com.osroyale.net.packet.out.SendMessage
import kotlin.jvm.optionals.getOrNull

/**
 * @author Jire
 */
class ItemOnNpcEvent(
    val itemId: Int,
    val index: Int,
    val slot: Int
) : NpcEvent {

    override fun handle(player: Player) {
        val used = player.inventory[slot] ?: return
        if (!used.matchesId(itemId)) return

        val npc = World.getNpcBySlot(index).getOrNull() ?: return
        if (!npc.isValid) return

        val position = npc.position
        val region = World.getRegions().getRegion(position)
        if (!region.containsNpc(position.height, npc)) return

        player.walkTo(npc) {
            player.face(position)
            if (!PluginManager.getDataBus().publish(player, ItemOnNpcEvent(npc, used, slot))) {
                player.send(SendMessage("Nothing interesting happens."))
            }
        }
    }

}