package org.jire.tarnishps

import java.util.concurrent.locks.LockSupport

/**
 * @author Jire
 */
object Threads {

    @JvmStatic
    @JvmOverloads
    fun preciseSleep(
        totalNanos: Long,
        busyWaitNanos: Long = 1_000_000 // 1ms of busy-waiting
    ) {
        val start = System.nanoTime()
        val sleepUntil = start + totalNanos - busyWaitNanos

        // Phase 1: Passive sleep (park)
        while (System.nanoTime() < sleepUntil) {
            val remaining = sleepUntil - System.nanoTime()
            if (remaining > 1_000_000) {
                LockSupport.parkNanos(remaining - 500_000) // sleep with 0.5ms headroom
            } else {
                Thread.yield() // let scheduler breathe
            }
        }

        // Phase 2: Spin-wait for precision
        while (System.nanoTime() - start < totalNanos) {
            Thread.onSpinWait()
        }
    }

}
