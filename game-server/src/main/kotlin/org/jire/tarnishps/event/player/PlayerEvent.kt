package org.jire.tarnishps.event.player

import com.osroyale.game.world.World
import com.osroyale.game.world.entity.mob.data.PacketType
import com.osroyale.game.world.entity.mob.player.Player
import org.jire.tarnishps.event.Event
import kotlin.jvm.optionals.getOrNull

/**
 * @author Jire
 */
interface PlayerEvent : Event {

    val index: Int

    override fun canHandle(player: Player) = super.canHandle(player)
            && !player.locking.locked(PacketType.INTERACT)

    override fun handle(player: Player) {
        val other = World.getPlayerBySlot(index).getOrNull() ?: return
        if (!other.isValid) return

        val position = other.position
        val region = World.getRegions().getRegion(position)
        if (!region.containsPlayer(other.height, other)) return

        handlePlayer(player, other)
    }

    fun handlePlayer(player: Player, other: Player)

    companion object {
        inline fun walkTo(
            index: Int,
            crossinline handle: (other: Player) -> Unit
        ) = object : PlayerEvent {
            override val index = index
            override fun handlePlayer(player: Player, other: Player) = player.walkTo(other) {
                handle(other)
            }
        }
    }

}