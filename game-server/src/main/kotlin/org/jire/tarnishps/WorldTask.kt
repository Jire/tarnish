package org.jire.tarnishps

import com.osroyale.game.task.Task
import com.osroyale.game.world.World

/**
 * @author Jire
 */
object WorldTask {

    inline fun schedule(delay: Int = 1, crossinline execute: () -> Unit) {
        if (delay > 0) {
            World.schedule(object : Task(delay) {
                override fun execute() {
                    cancel()
                    execute()
                }
            })
        } else execute()
    }

    @JvmStatic
    fun schedule(delay: Int = 1, execute: Runnable) {
        if (delay > 0) {
            World.schedule(object : Task(delay) {
                override fun execute() {
                    cancel()
                    execute.run()
                }
            })
        } else execute.run()
    }

}