package org.jire.tarnishps.event.widget

import com.osroyale.game.world.InterfaceConstants
import com.osroyale.game.world.entity.mob.player.Player

/**
 * @author Jire
 */
class MoveItemEvent(
    val interfaceId: Int,
    val inserting: Int,
    val fromSlot: Int,
    val toSlot: Int
) : WidgetEvent {

    override fun handle(player: Player) {
        when (interfaceId) {
            InterfaceConstants.INVENTORY_INTERFACE,
            InterfaceConstants.INVENTORY_STORE ->
                player.inventory.swap(
                    fromSlot,
                    toSlot
                )

            InterfaceConstants.WITHDRAW_BANK ->
                player.bank.moveItem(inserting, fromSlot, toSlot)

            else -> println("Unkown Item movement itemcontainer id: $interfaceId")
        }
    }

}