package com.osroyale.net.packet.`in`

import com.osroyale.content.event.impl.FirstObjectClick
import com.osroyale.content.event.impl.SecondObjectClick
import com.osroyale.content.event.impl.ThirdObjectClick
import com.osroyale.game.world.entity.mob.data.PacketType
import com.osroyale.game.world.entity.mob.player.Player
import com.osroyale.net.codec.ByteModification
import com.osroyale.net.codec.ByteOrder
import com.osroyale.net.packet.ClientPackets
import com.osroyale.net.packet.GamePacket
import com.osroyale.net.packet.PacketListener
import com.osroyale.net.packet.PacketListenerMeta
import org.jire.tarnishps.event.`object`.ObjectOptionEvent

/**
 * The `GamePacket` responsible for clicking various options of an in-game
 * object.
 *
 * @author Daniel | Obey
 * @author Jire
 */
@PacketListenerMeta(
    ClientPackets.FIRST_CLICK_OBJECT,
    ClientPackets.SECOND_CLICK_OBJECT,
    ClientPackets.THIRD_CLICK_OBJECT
)
class ObjectInteractionPacketListener : PacketListener {

    override fun handlePacket(player: Player, packet: GamePacket) {
        if (player.locking.locked(PacketType.CLICK_OBJECT)) return

        val event: ObjectOptionEvent = when (packet.opcode) {
            ClientPackets.FIRST_CLICK_OBJECT -> {
                val x = packet.readShort(ByteOrder.LE, ByteModification.ADD)
                val id = packet.readShort(false)
                val y = packet.readShort(false, ByteModification.ADD)
                ObjectOptionEvent(1, id, x, y) { FirstObjectClick(it) }
            }

            ClientPackets.SECOND_CLICK_OBJECT -> {
                val id = packet.readShort(ByteOrder.LE, ByteModification.ADD)
                val y = packet.readShort(ByteOrder.LE)
                val x = packet.readShort(false, ByteModification.ADD)
                ObjectOptionEvent(2, id, x, y) { SecondObjectClick(it) }
            }

            ClientPackets.THIRD_CLICK_OBJECT -> {
                val x = packet.readShort(ByteOrder.LE)
                val y = packet.readShort(false)
                val id = packet.readShort(false, ByteOrder.LE, ByteModification.ADD)
                ObjectOptionEvent(3, id, x, y) { ThirdObjectClick(it) }
            }

            else -> return
        }
        player.events.interact(player, event)
    }

}