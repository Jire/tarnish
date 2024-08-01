package org.jire.tarnishps.task

import org.jctools.queues.MessagePassingQueue
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Jire
 */
object MessagePassingQueues {

    private val logger: Logger =
        LoggerFactory.getLogger(MessagePassingQueue::class.java)

    /**
     * Used to drain from a `MessagePassingQueue`.
     */
    fun interface Drainer<T> {

        /**
         * @return `true` if the queue should continue being drained, `false` otherwise.
         */
        fun continueDraining(e: T): Boolean

    }

    /**
     * @return `false` if it was broken due to [Drainer.continueDraining]
     */
    @JvmStatic
    @JvmOverloads
    fun <T> drain(
        queue: MessagePassingQueue<T>,
        limit: Int = queue.capacity(),
        consumer: Drainer<T>
    ): Boolean {
        var i = 0
        var next: T
        while (i < limit) {
            next = queue.poll() ?: break

            if (!consumer.continueDraining(next)) {
                logger.warn("Breaking at task! (size={})", queue.size())
                return false
            }

            i++
        }
        return true
    }

}