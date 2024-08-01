package com.osroyale.content.event.impl;

import com.osroyale.content.event.InteractionEvent;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 12-2-2017.
 */
public class ItemContainerInteractionEvent extends InteractionEvent {

    public final int id;

    public final int interfaceId;

    public final int removeSlot;

    public final int removeId;

    public ItemContainerInteractionEvent(int id, int interfaceId, int removeSlot, int removeId) {
        super(InteractionType.ITEM_CONTAINER_INTERACTION_EVENT);
        this.id = id;
        this.interfaceId = interfaceId;
        this.removeSlot = removeSlot;
        this.removeId = removeId;
    }
}
