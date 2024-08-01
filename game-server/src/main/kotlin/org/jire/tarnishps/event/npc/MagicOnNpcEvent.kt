package org.jire.tarnishps.event.npc

import com.osroyale.content.skill.impl.hunter.net.impl.Butterfly
import com.osroyale.content.skill.impl.hunter.net.impl.Impling
import com.osroyale.game.world.entity.combat.magic.CombatSpell
import com.osroyale.game.world.entity.mob.npc.Npc
import com.osroyale.game.world.entity.mob.player.Player
import com.osroyale.net.packet.out.SendMessage

/**
 * @author Jire
 */
class MagicOnNpcEvent(
    override val slot: Int,
    val spell: Int
) : NpcClickEvent {

    override fun handleNpc(player: Player, npc: Npc) {
        val definition = CombatSpell.get(spell) ?: return
        if (player.spellbook != definition.spellbook) return

        if (!npc.definition.isAttackable

            && !Impling.forId(npc.id).isPresent
            && !Butterfly.forId(npc.id).isPresent
        ) {
            player.send(SendMessage("This npc can not be attacked!"))
            return
        }

        player.setSingleCast(definition)

        if (!player.combat.attack(npc)) {
            player.setSingleCast(null)
            player.resetFace()
        }
    }

}