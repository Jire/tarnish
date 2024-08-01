package org.jire.tarnishps.event.item

import com.osroyale.content.event.EventDispatcher
import com.osroyale.content.event.impl.PickupItemInteractionEvent
import com.osroyale.game.world.entity.mob.player.Player
import com.osroyale.game.world.entity.mob.player.PlayerRight
import com.osroyale.game.world.items.Item
import com.osroyale.game.world.position.Position
import com.osroyale.net.packet.out.SendMessage

/**
 * @author Jire
 */
class PickupItemEvent(
    val id: Int,
    val x: Int, val y: Int
) : ItemEvent {

    override fun handle(player: Player) {
        val item = Item(id)
        val position = Position.create(x, y, player.height)

        if (EventDispatcher.execute(player, PickupItemInteractionEvent(item, position))) {
            if (PlayerRight.isDeveloper(player)) {
                player.send(
                    SendMessage(
                        String.format(
                            "[%s]: item=%d position=%s",
                            PickupItemInteractionEvent::class.java.simpleName, item.id, position.toString()
                        )
                    )
                )
            }
            return
        }

        player.pickup(item, position)
    }

}