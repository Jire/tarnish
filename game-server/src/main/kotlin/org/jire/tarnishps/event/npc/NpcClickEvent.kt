package org.jire.tarnishps.event.npc

import com.osroyale.game.world.World
import com.osroyale.game.world.entity.mob.data.PacketType
import com.osroyale.game.world.entity.mob.npc.Npc
import com.osroyale.game.world.entity.mob.player.Player
import kotlin.jvm.optionals.getOrNull

/**
 * @author Jire
 */
interface NpcClickEvent : NpcEvent {

    val slot: Int

    override fun canHandle(player: Player) = super.canHandle(player)
            && !player.locking.locked(PacketType.CLICK_NPC)

    override fun handle(player: Player) {
        val npc = World.getNpcBySlot(slot).getOrNull() ?: return
        if (!npc.isValid) return

        val position = npc.position
        val region = World.getRegions().getRegion(position)
        if (!region.containsNpc(position.height, npc)) return

        handleNpc(player, npc)
    }

    fun handleNpc(player: Player, npc: Npc) {}

}