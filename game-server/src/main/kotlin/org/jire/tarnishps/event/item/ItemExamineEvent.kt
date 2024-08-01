package org.jire.tarnishps.event.item

import com.osroyale.game.world.entity.mob.player.Player
import com.osroyale.net.packet.out.SendMessage
import org.jire.tarnishps.defs.ItemDefLoader
import org.jire.tarnishps.event.Event

/**
 * @author Jire
 */
class ItemExamineEvent(val itemId: Int) : Event {

    override fun handle(player: Player) {
        val itemDef = ItemDefLoader.map[itemId] ?: return
        val examine = itemDef.examine
        if ("null" != examine) {
            player.send(SendMessage(examine))
        }
    }

}