package org.jire.tarnishps.event.npc

import com.osroyale.game.world.entity.mob.player.Player
import com.osroyale.net.packet.out.SendMessage
import org.jire.tarnishps.defs.MonsterDefLoader

/**
 * @author Jire
 */
class NpcExamineEvent(val npcId: Int) : NpcEvent {

    override fun handle(player: Player) {
        val monsterDef = MonsterDefLoader.map[npcId] ?: return
        val examine = monsterDef.examine
        if ("null" != examine) {
            player.send(SendMessage(examine))
        }
    }

}