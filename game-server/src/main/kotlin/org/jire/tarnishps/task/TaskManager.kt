package org.jire.tarnishps.task

import com.osroyale.game.task.Task
import it.unimi.dsi.fastutil.objects.Object2BooleanMap
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap
import org.jctools.queues.MessagePassingQueue
import org.jctools.queues.MpscArrayQueue
import org.jctools.queues.SpscArrayQueue
import org.jire.tarnishps.task.MessagePassingQueues.drain
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Used to schedule [Task]s.
 *
 * @author Jire
 */
class TaskManager {

    /**
     * Tasks which are pending to be scheduled to be
     * added to [pending] tasks during processing.
     */
    private val scheduled: MessagePassingQueue<Task> = MpscArrayQueue(CAPACITY)

    /**
     * The pending tasks, which are first added to
     * the [active] tasks during processing.
     */
    private val pending: MessagePassingQueue<Task> = SpscArrayQueue(CAPACITY)

    /**
     * The active tasks, which will be processed in the current tick.
     */
    private val active: MessagePassingQueue<Task> = SpscArrayQueue(CAPACITY)

    /**
     * Set of attachment keys to `logout` state used
     * for cancelling tasks by their [Task.getAttachment].
     */
    private val cancelAttachments: Object2BooleanMap<Any> = Object2BooleanOpenHashMap(CAPACITY)

    /**
     * @param task The [Task] to schedule.
     *
     * @return whether the task was successfully added to the [scheduled] queue.
     */
    fun schedule(task: Task?): Boolean {
        task ?: return false

        val added = scheduled.offer(task)
        if (!added) {
            logger.warn("Unable to add task to `scheduled` ({} size)", scheduled.size())
        }

        return added
    }

    fun process(): Boolean {
        val drainedScheduled = drain(scheduled, CAPACITY) { task ->
            try {
                if (!task.canSchedule()) return@drain true

                task.isRunning = true
                task.beforeSchedule()

                /* task was cancelled inside [Task.beforeSchedule] */
                if (task.isRunning) {
                    if (!pending.offer(task)) return@drain false

                    task.onSchedule()

                    if (task.isInstant) {
                        task.baseExecute()
                    }
                }
            } catch (e: Exception) {
                logger.error("Failed to process scheduled task", e)
            }
            true
        }
        if (!drainedScheduled) return false

        val drainedPending = drain(pending, CAPACITY) { task ->
            active.offer(task)
        }
        if (!drainedPending) return false

        val drainedActive = drain(active, CAPACITY) { task ->
            /* Check to make sure task isn't cancelled */
            if (checkCancelled(task)) return@drain true

            try {
                task.process()

                !task.isRunning || pending.offer(task)
            } catch (e: Exception) {
                logger.error("Failed to process active task", e)

                try {
                    task.cancel()
                } catch (e: Exception) {
                    logger.error("Failed to cancel active task", e)
                }
            }
            true
        }
        if (!drainedActive) return false

        /* Clear attachments so next tick they won't be still there */
        val cancelAttachments = cancelAttachments
        synchronized(cancelAttachments) {
            if (cancelAttachments.isNotEmpty()) { /* avoids clear object allocation if empty */
                cancelAttachments.clear()
            }
        }

        return true
    }

    private fun checkCancelled(task: Task): Boolean {
        val attachment = task.attachment ?: return false

        val logout: Boolean
        synchronized(cancelAttachments) {
            if (!cancelAttachments.containsKey(attachment)) return false
            logout = cancelAttachments.getBoolean(attachment)
        }

        task.cancel(logout)
        return true
    }

    @JvmOverloads
    fun cancel(
        attachment: Any?, logout: Boolean = false
    ): Boolean {
        attachment ?: return false

        synchronized(cancelAttachments) {
            return cancelAttachments.put(attachment, logout)
        }
    }

    private companion object {

        /**
         * The capacity of the tasks queue.
         */
        private const val CAPACITY = (2000 /* players */ + 20000 /* NPCs */) * 256

        /**
         * Used for logging via SLF4J API.
         */
        private val logger: Logger = LoggerFactory.getLogger(TaskManager::class.java)

    }

}
