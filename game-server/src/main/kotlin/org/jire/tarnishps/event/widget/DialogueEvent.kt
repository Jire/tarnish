package org.jire.tarnishps.event.widget

import com.osroyale.game.world.entity.mob.player.Player

/**
 * @author Jire
 */
object DialogueEvent : WidgetEvent {

    override fun handle(player: Player) {
        player.dialogueFactory.execute()
    }

}