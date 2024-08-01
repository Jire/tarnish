package org.jire.tarnishps

import net.openhft.affinity.AffinityLock
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Looping main thread with core-affinity and highly accurate cycle delay.
 *
 * @author Jire
 *
 * @property cycleNanos Interval of a cycle in nanoseconds.
 * @property minSleepNanos The minimum amount of nanoseconds to consider actually sleeping the thread.
 * @property targetCpuId Target CPU ID, starting at 0. Hyper-threads are considered CPUs.
 */
abstract class MainThread
@JvmOverloads
constructor(
    name: String,
    private val cycleNanos: Long = 600 * 1000 * 1000, // 600ms
    private val minSleepNanos: Long = 1 * 1000 * 1000, // 1ms
    private val targetCpuId: Int = 0,
    priority: Int = MAX_PRIORITY,
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
        var affinityLock: AffinityLock? = null
        // only acquire lock if we have at least 2 processors (threads)
        if (System.getProperty("os.arch") != "aarch64" && Runtime.getRuntime().availableProcessors() > 1) {
            try {
                affinityLock = AffinityLock.acquireLock(targetCpuId)
            } catch (e: Exception) {
                logger.error("Failed to acquire affinity lock", e)
            }
        }
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
