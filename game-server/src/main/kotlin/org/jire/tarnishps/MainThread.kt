package org.jire.tarnishps

import net.openhft.affinity.AffinityLock
import net.openhft.affinity.CpuLayout
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Looping main thread with core-affinity and highly accurate cycle delay.
 *
 * @author Jire
 *
 * @property cycleNanos Interval of a cycle in nanoseconds.
 * @property minSleepNanos The minimum amount of nanoseconds to consider actually sleeping the thread.
 * @property priority Thread priority.
 * @property layout CPU layout.
 * @property targetCpuId Target CPU ID, starting at 0. Hyper-threads are considered CPUs.
 */
abstract class MainThread
@JvmOverloads
constructor(
    name: String,

    private val cycleNanos: Long = 600 * 1000 * 1000, // 600ms
    private val minSleepNanos: Long = 1 * 1000 * 1000, // 1ms

    priority: Int = MAX_PRIORITY,

    layout: CpuLayout = AffinityLock.cpuLayout(),
    private val targetCpuId: Int = (layout.cpus() - 1 downTo 0)
        .firstOrNull { layout.threadId(it) == 0 } // pick only physical cores
        ?: 0 // fallback to CPU 0
) : Thread(name) {

    @Volatile
    var running = false

    init {
        this.priority = priority
    }

    /**
     * Performs a cycle.
     *
     * Assumed not to throw exceptions. If one is thrown, the thread will stop.
     */
    abstract fun cycle()

    override fun run() {
        val affinityLock: AffinityLock? =
            // only acquire lock if we have at least 2 processors (threads)
            if (System.getProperty("os.arch") != "aarch64" && Runtime.getRuntime().availableProcessors() > 1) {
                logger.debug("Locking main thread to CPU ID {}", targetCpuId)
                AffinityLock.acquireLock(targetCpuId)
            } else null
        try {
            running = true
            while (running && !interrupted()) {
                val startTime = System.nanoTime()

                cycle()

                val endTime = System.nanoTime()

                val elapsedNanos = endTime - startTime
                val sleepNanos = cycleNanos - elapsedNanos
                if (sleepNanos >= minSleepNanos) {
                    Threads.preciseSleep(sleepNanos)
                }
            }
        } finally {
            affinityLock?.release()
        }
    }

    override fun start() {
        super.start()

        started()
    }

    /**
     * Called when the start is started.
     */
    open fun started() {}

    private companion object {
        private val logger: Logger = LoggerFactory.getLogger(MainThread::class.java)
    }

}
