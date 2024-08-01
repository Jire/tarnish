package com.osroyale.game.event.bus;

import com.osroyale.game.event.Event;
import com.osroyale.game.event.listener.EventListener;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * <h2>A Databus implementation for sending events.</h2>
 * <p>
 * The difference between a DataBus and publish/subscribe model is that.
 * A DataBus is a form of publish/subscribe, the only difference is that
 * the bus decouples the publisher and subscriber.
 *
 * This pattern is different from the observer pattern in the sense that
 * the observer pattern only allows for a one-to-many relationship wheres
 * the DataBus allows for a many-to-many relationship.
 *
 * {@link EventListener}'s must subscribe to the bus
 * in order to receive {@link Event}'s send by the bus.
 * {@link EventListener}'s can then decide which {@link Event}'s
 * they are interested in.
 * </p>
 *
 *  @see <a href="http://wiki.c2.com/?DataBusPattern">DataBusPattern</a>
 *
 * @author nshusa
 */
public final class DataBus {

    /**
     * The collection of listeners that are subscribed to this bus.
     */
    private static final Set<EventListener> listeners = new LinkedHashSet<>();

    /**
     * The singleton object.
     */
    private static final DataBus INSTANCE = new DataBus();

    /**
     * Prevent instantiation
     */
    private DataBus() {

    }

    /**
     * Gets the singleton object.
     */
    public static DataBus getInstance() {
        return INSTANCE;
    }

    /**
     * Subscribes an {@link EventListener} to this bus.
     *
     * If a listener is subscribed to this bus,
     * then the listener can choose which {@link Event}'s its interested in.
     *
     * @param listener
     *  The listener to subscribe to this bus.
     */
    public void subscribe(EventListener listener) {
        listeners.add(listener);
    }

    /**
     * Unsubscribe an {@link EventListener} from this bus.
     *
     * If a listener is no longer subscribed to this bus then
     * the listener will no longer receive any events
     * sent from the bus.
     *
     * @param listener
     *  The listener to unsubscribe from this bus.
     */
    public void unsubscribe(EventListener listener) {
        listeners.remove(listener);
    }

    /**
     * Sends an {@link Event} to all subscribed listeners.
     *
     * The listener's will choose which event.
     * they are interested in.
     *
     * @param event
     *      The event that will be sent to all listeners.
     */
    public void publish(Event event) {
        listeners.forEach(it -> it.accept(event));
    }

}
