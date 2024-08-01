package org.jire.tarnishps.event.widget

import com.osroyale.game.world.entity.mob.player.Player
import com.osroyale.util.Utility
import kotlin.jvm.optionals.getOrNull

/**
 * @author Jire
 */
class InputStringEvent(val inputLong: Long) : WidgetEvent {

    override fun handle(player: Player) {
        val input = Utility.longToString(inputLong).replace('_', ' ')
        player.enterInputListener.getOrNull()?.accept(input)
    }

}