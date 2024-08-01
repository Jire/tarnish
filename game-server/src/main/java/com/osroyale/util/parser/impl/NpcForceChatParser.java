package com.osroyale.util.parser.impl;

import com.google.gson.JsonObject;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.position.Position;
import com.osroyale.util.Utility;
import com.osroyale.util.parser.GsonParser;
import org.jire.tarnishps.OldToNew;

import java.util.HashMap;
import java.util.Map;


/**
 * Parses through the npc spawn file and creates {@link Npc}s on startup.
 *
 * @author Daniel | Obey
 */
public class NpcForceChatParser extends GsonParser {

    /**
     * The map containing all the forced messages.
     */
    public static final Map<Position, ForcedMessage> FORCED_MESSAGES = new HashMap<>();

    /**
     * Constructs a new <code>NpcForceChatParser</code>.
     */
    public NpcForceChatParser() {
        super("def/npc/npc_force_chat", false);
    }

    @Override
    protected void parse(JsonObject data) {
        int id = data.get("id").getAsInt();
        int newId = OldToNew.get(id);
        if (newId != -1) {
            id = newId;
        }

        final Position position = builder.fromJson(data.get("position"), Position.class);
        final int interval = data.get("interval").getAsInt();

        final MessageType type = MessageType.valueOf(data.get("type").getAsString());

        String[] messages = new String[]{};

        if (data.has("messages")) {
            messages = builder.fromJson(data.get("messages"), String[].class);
        }

        FORCED_MESSAGES.put(position, new ForcedMessage(id, interval, messages, type));
    }

    /**
     * The forced message class.
     */
    public static class ForcedMessage {
        /**
         * The npc id.
         */
        private final int id;

        /**
         * The interval at which the message will be performed.
         */
        private final int interval;

        /**
         * The array of messages the npc will perform.
         */
        private final String[] messages;

        /**
         * The message type.
         */
        private final MessageType type;

        /**
         * The next message.
         */
        private int next = 0;

        /**
         * Constructs a new <code>ForcedMessage</code>.
         *
         * @param id The npc id.
         * @param interval The interval at which the npc will perform the message.
         * @param messages The messages the npc will be forced to perform.
         * @param type     The type of message.
         */
        public ForcedMessage(int id, int interval, String[] messages, MessageType type) {
            this.id = id;
            this.interval = interval;
            this.messages = messages;
            this.type = type;
        }

        public int getId() { return id; }

        public int getInterval() {
            return interval;
        }

        public String[] getMessages() {
            return messages;
        }

        public MessageType getType() {
            return type;
        }

        public String nextMessage() {
            switch (type) {
                case NORMAL:
                    if (next >= messages.length) {
                        next = 0;
                    }
                    return messages[next++];
                case RANDOM:
                    return messages[Utility.random(messages.length)];
                default:
                    throw new IllegalArgumentException("Unhandled type: " + type + ".");
            }
        }
    }

    /**
     * The enum of message types.
     */
    private enum MessageType {
        RANDOM,
        NORMAL
    }
}
