package org.jire.tarnishps.event.player

import com.osroyale.game.world.entity.combat.magic.CombatSpell
import com.osroyale.game.world.entity.mob.player.Player

/**
 * @author Jire
 */
class MagicOnPlayerEvent(
    override val index: Int,
    val spell: Int
) : PlayerEvent {

    override fun handlePlayer(player: Player, other: Player) {
        val combatSpell = CombatSpell.get(spell) ?: return
        if (player.spellbook != combatSpell.spellbook) return

        player.setSingleCast(combatSpell)

        if (!player.combat.attack(other)) {
            player.setSingleCast(null)
            player.resetFace()
        }
    }

}