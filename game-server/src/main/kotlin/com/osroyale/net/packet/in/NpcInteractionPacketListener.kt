package com.osroyale.net.packet.`in`

import com.osroyale.game.world.entity.mob.data.PacketType
import com.osroyale.game.world.entity.mob.player.Player
import com.osroyale.net.codec.ByteModification
import com.osroyale.net.codec.ByteOrder
import com.osroyale.net.packet.ClientPackets
import com.osroyale.net.packet.GamePacket
import com.osroyale.net.packet.PacketListener
import com.osroyale.net.packet.PacketListenerMeta
import org.jire.tarnishps.event.npc.*

/**
 * The [GamePacket] responsible for the different options while clicking
 * an npc.
 *
 * @author Daniel | Obey
 * @author Jire
 */
@PacketListenerMeta(
    ClientPackets.ATTACK_NPC,
    ClientPackets.MAGIC_ON_NPC,
    ClientPackets.NPC_ACTION_1,
    ClientPackets.NPC_ACTION_2,
    ClientPackets.NPC_ACTION_3,
    ClientPackets.NPC_ACTION_4
)
class NpcInteractionPacketListener : PacketListener {

    override fun handlePacket(player: Player, packet: GamePacket) {
        if (player.locking.locked(PacketType.CLICK_NPC)) return

        val event: NpcClickEvent = when (packet.opcode) {
            ClientPackets.ATTACK_NPC -> AttackNpcEvent(
                packet.readShort(false, ByteModification.ADD)
            )

            ClientPackets.MAGIC_ON_NPC -> MagicOnNpcEvent(
                packet.readShort(ByteOrder.LE, ByteModification.ADD),
                packet.readShort(ByteModification.ADD)
            )

            ClientPackets.NPC_ACTION_1 -> FirstNpcOptionEvent(
                packet.readShort(ByteOrder.LE)
            )

            ClientPackets.NPC_ACTION_2 -> SecondNpcOptionEvent(
                packet.readShort(ByteOrder.LE, ByteModification.ADD)
            )

            ClientPackets.NPC_ACTION_3 -> NpcOptionEvent(
                packet.readShort(), 3
            )

            ClientPackets.NPC_ACTION_4 -> NpcOptionEvent(
                packet.readShort(ByteOrder.LE), 4
            )

            else -> return
        }

        player.events.interact(player, event)
    }

}
