package com.osroyale.net.packet;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.session.GameSession;

public abstract class OutgoingPacket {

    private final int opcode;
    private final PacketType type;
    protected final PacketBuilder builder;

    public OutgoingPacket(int opcode, int capacity) {
        this(opcode, PacketType.FIXED, capacity);
    }

    public OutgoingPacket(int opcode, PacketType type) {
        this.opcode = opcode;
        this.type = type;
        this.builder = PacketBuilder.alloc();
    }

    public OutgoingPacket(int opcode, PacketType type, int size) {
        this.opcode = opcode;
        this.type = type;
        this.builder = PacketBuilder.alloc(size, size);
    }

    protected abstract boolean encode(Player player);

    public void execute(Player player) {
        if (player.isBot) {
            builder.release();
            return;
        }
        if (!encode(player)) {
            builder.release();
            return;
        }

        if (!player.getSession().isPresent()) {
            builder.release();
            return;
        }

        GameSession session = player.getSession().get();
        session.queueServerPacket(builder.toPacket(opcode, type));
    }

}
