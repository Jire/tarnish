package org.jire.tarnishps.event

import com.osroyale.game.world.entity.mob.player.Player

/**
 * @author Jire
 */
class Events {

    var logOut = false
    private var widget: Event? = null
    private var interact: Event? = null
    var loadRegion = false

    fun widget(player: Player, event: Event) {
        if (event.canHandle(player)) {
            widget = event
        }
    }

    fun interact(player: Player, event: Event) {
        if (event.canHandle(player)) {
            interact = event
        }
    }

    fun process(player: Player) {
        if (logOut) {
            player.logout()
            return
        }
        if (loadRegion) player.loadRegion()
        widget?.handle(player)
        interact?.handle(player)
    }

    fun reset() {
        logOut = false
        widget = null
        interact = null
        loadRegion = false
    }

}