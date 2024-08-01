package org.jire.tarnishps.event.npc

import com.osroyale.game.world.entity.mob.npc.Npc
import com.osroyale.game.world.entity.mob.player.Player

/**
 * @author Jire
 */
class AttackNpcEvent(override val slot: Int) : NpcClickEvent {

    override fun handleNpc(player: Player, npc: Npc) {
        player.combat.attack(npc)
    }

}