package com.osroyale.game.action;

import com.osroyale.game.action.policy.WalkablePolicy;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.Mob;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;

/**
 * The class which manages {@link Action}s executed by mobs.
 *
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class ActionManager {

    /**  A queue of {@link Action} object. */
    private final Queue<Action<?>> queuedActions = new ArrayDeque<>();

    /** The current action.  */
    private Action<?> currentAction = null;

    /** Sequences the pending actions, and as soon as the current action  has stopped, it executes the head of the queue.  */
    public void sequence() {
        if (queuedActions.isEmpty() || (currentAction != null && currentAction.isRunning())) {
            return;
        }

        execute(this.currentAction = queuedActions.poll(), true);
    }

    /** Queues the specified {@code action}.  */
    public <A extends Action<?>> void queue(A action) {
        queuedActions.add(action);
    }

    public <A extends Action<?>> void execute(A action) {
        execute(action, true);
    }

    /**  Adds an <code>Action</code> to the queue.  */
    public <A extends Action<?>> void execute(A action, boolean override) {
        //if current action is priorized stop.
        if (currentAction != null && currentAction.isRunning() && currentAction.prioritized()) {
            return;
        }

        //if there is already an action active and we don't want to override
        //we ignore.
        //if u don't want to override but you also dont want to ignore
        //queue the action.
        if (currentAction != null && currentAction.isRunning() && !override) {
            return;
        }

        //if there is an action active, make sure we stop it before running
        //another action.
        if (currentAction != null && currentAction.isRunning()) {
            currentAction.cancel();
        }

        //finally submit the action.
        World.schedule(currentAction = action);
    }

    /** Cancels all the queued actions by stopping them and removing all elements from the queue.  */
    private void cancelQueuedActions() {
        queuedActions.forEach(Action::cancel);
        queuedActions.clear();
    }

    /** Cancels the current {@link Action} for the underlying {@link Mob}. */
    public void cancel() {
        if (currentAction != null && currentAction.isRunning() && currentAction.prioritized()) {
            currentAction.cancel();
            currentAction = null;
        }
    }

    /**  Resets all the actions for the underlying {@link Mob}. */
    public void reset() {
        cancel();
        cancelQueuedActions();
    }

    /** Purges actions in the queue with a <code>WalkablePolicy</code> of <code>NON_WALKABLE</code>.  */
    public void clearNonWalkableActions() {
        final Action<?> currentAction = this.currentAction;
        if (currentAction != null) {
            if (!currentAction.cancellableInProgress()) {
                return;
            }
            if (currentAction.getWalkablePolicy() == WalkablePolicy.NON_WALKABLE) {
                currentAction.cancel();
                this.currentAction = null;
            }
        }

        for (Action<?> actionEvent : queuedActions) {
            if (actionEvent.getWalkablePolicy() != WalkablePolicy.WALKABLE) {
                actionEvent.cancel();
                queuedActions.remove(actionEvent);
            }
        }
    }

    /** Purges actions in the queue with a <code>WalkablePolicy</code> of <code>NON_WALKABLE</code>. */
    public void cancel(String name) {
        if (currentAction != null && Objects.equals(currentAction.getName(), name)) {
            currentAction.cancel();
            currentAction = null;
        }

        for (Action<?> actionEvent : queuedActions) {
            if (Objects.equals(actionEvent.getName(), name)) {
                actionEvent.cancel();
                queuedActions.remove(actionEvent);
            }
        }
    }

    /** Gets the current action. */
    public Action<?> getCurrentAction() {
        return currentAction;
    }

    @Override
    public String toString() {
        return String.format("ActionManager[size=%s, action=%s]", queuedActions.size(), currentAction);
    }


}
