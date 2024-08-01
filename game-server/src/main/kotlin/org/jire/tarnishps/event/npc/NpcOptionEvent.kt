package org.jire.tarnishps.event.npc

import com.osroyale.content.event.EventDispatcher
import com.osroyale.content.event.InteractionEvent
import com.osroyale.game.action.impl.NpcFaceAction
import com.osroyale.game.event.Event
import com.osroyale.game.event.impl.NpcClickEvent
import com.osroyale.game.plugin.PluginManager
import com.osroyale.game.world.entity.mob.npc.Npc
import com.osroyale.game.world.entity.mob.player.Player

/**
 * @author Jire
 */
open class NpcOptionEvent(
    override val slot: Int,
    val option: Int
) : org.jire.tarnishps.event.npc.NpcClickEvent {

    override fun handleNpc(player: Player, npc: Npc) {
        player.walkTo(npc) {
            npc.action.execute(createAction(player, npc), true)
            val interactionEvent = createInteractionEvent(npc)
            if (interactionEvent == null
                || !EventDispatcher.execute(player, interactionEvent)
            ) {
                publishToPluginManager(player, npc)
            }
        }
    }

    open fun createAction(player: Player, npc: Npc) = NpcFaceAction(npc, player.position, option)

    open fun createInteractionEvent(npc: Npc): InteractionEvent? = null

    fun publishToPluginManager(player: Player, npc: Npc) =
        PluginManager.getDataBus().publish(player, createEvent(npc))

    open fun createEvent(npc: Npc): Event = NpcClickEvent(option + 1, npc)

}