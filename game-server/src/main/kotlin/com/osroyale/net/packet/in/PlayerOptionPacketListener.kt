package com.osroyale.net.packet.`in`

import com.osroyale.content.ProfileViewer
import com.osroyale.game.world.entity.mob.player.Player
import com.osroyale.game.world.entity.mob.player.exchange.duel.StakeSession
import com.osroyale.game.world.entity.mob.player.exchange.trade.TradeSession
import com.osroyale.net.codec.ByteModification
import com.osroyale.net.codec.ByteOrder
import com.osroyale.net.packet.ClientPackets
import com.osroyale.net.packet.GamePacket
import com.osroyale.net.packet.PacketListener
import com.osroyale.net.packet.PacketListenerMeta
import org.jire.tarnishps.event.player.MagicOnPlayerEvent
import org.jire.tarnishps.event.player.PlayerEvent.Companion.walkTo

/**
 * The [GamePacket]s responsible interacting with other players.
 *
 * @author Daniel | Obey
 * @author Jire
 */
@PacketListenerMeta(
    ClientPackets.TRADE_REQUEST,
    ClientPackets.TRADE_ANSWER,
    ClientPackets.CHALLENGE_PLAYER,
    ClientPackets.FOLLOW_PLAYER,
    ClientPackets.MAGIC_ON_PLAYER,
    ClientPackets.ATTACK_PLAYER,
    ClientPackets.GAMBLE_PLAYER
)
class PlayerOptionPacketListener : PacketListener {

    override fun handlePacket(player: Player, packet: GamePacket) {
        val event = when (packet.opcode) {
            ClientPackets.GAMBLE_PLAYER -> walkTo(packet.readShort()) {
                player.gambling.sendRequest(player, it)
            }

            128 -> walkTo(packet.readShort()) {
                player.exchangeSession.request(StakeSession(player, it))
            }

            153 -> walkTo(packet.readShort(ByteOrder.LE)) {
                player.combat.attack(it)
            }

            73 -> walkTo(packet.readShort(ByteOrder.LE)) {
                player.follow(it)
            }

            139 -> walkTo(packet.readShort(ByteOrder.LE)) {
                player.exchangeSession.request(TradeSession(player, it))
            }

            39 -> walkTo(packet.readShort(ByteOrder.LE)) {
                ProfileViewer.open(player, it)
            }

            ClientPackets.MAGIC_ON_PLAYER -> MagicOnPlayerEvent(
                packet.readShort(ByteModification.ADD),
                packet.readShort(ByteOrder.LE)
            )

            else -> return
        }

        player.events.interact(player, event)
    }

}
