package org.jire.tarnishps.event.player

import com.osroyale.game.event.impl.ItemOnPlayerEvent
import com.osroyale.game.plugin.PluginManager
import com.osroyale.game.world.World
import com.osroyale.game.world.entity.mob.player.Player
import com.osroyale.net.packet.out.SendMessage
import org.jire.tarnishps.event.Event
import kotlin.jvm.optionals.getOrNull

/**
 * @author Jire
 */
class ItemOnPlayerEvent(
    val interfaceId: Int,
    val item: Int,
    val itemSlot: Int,
    val slot: Int
) : Event {

    override fun handle(player: Player) {
        val used = player.inventory[itemSlot] ?: return
        if (!used.matchesId(item)) return

        val other = World.getPlayerBySlot(slot).getOrNull() ?: return
        
        player.walkTo(other) {
            player.face(other.position)
            if (!PluginManager.getDataBus().publish(player, ItemOnPlayerEvent(other, used, itemSlot))) {
                player.send(SendMessage("Nothing interesting happens."))
            }
        }
    }

}