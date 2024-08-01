package org.jire.tarnishps

/**
 * @author Jire
 */
object Threads {

    fun preciseSleep(
        totalNanos: Long,
        targetBusyWaitingNanos: Long = 100_000_000
    ) {
        val startTime = System.nanoTime()

        // sleeping
        val sleepNanos = totalNanos - targetBusyWaitingNanos - 1_000_000 // extra millisecond because sleep expected
        if (sleepNanos > 0) {
            while (System.nanoTime() - startTime < sleepNanos) {
                Thread.sleep(0)
            }
        }

        // busy-waiting
        while (System.nanoTime() - startTime < totalNanos) {
            Thread.onSpinWait()
        }
    }

}
