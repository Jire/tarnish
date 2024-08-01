package com.osroyale.net.packet.out;

import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.OutgoingPacket;
import com.osroyale.net.packet.PacketType;

/**
 * Sends the entity feed.
 *
 * @author Daniel
 */
public final class SendEntityFeed extends OutgoingPacket {

    private static final int OPCODE = 175;

    public static final int NO_OPPONENT = -1;
    public static final int OPPONENT_OFFSET_NPC = 2048;

    public static int getOpponent(final Mob mob) {
        return mob.getIndex()
                + (!mob.isPlayer()
                ? SendEntityFeed.OPPONENT_OFFSET_NPC : 0);
    }

    private final int opponentId;

    private final int hp;
    private final int maxHp;

    public SendEntityFeed(final int opponentId,
                          final int hp, final int maxHp) {
        super(OPCODE, PacketType.FIXED, 6);
        this.opponentId = opponentId;
        this.hp = hp;
        this.maxHp = maxHp;
    }

    public SendEntityFeed(final Mob mob) {
        this(
                getOpponent(mob),
                mob.getCurrentHealth(),
                mob.getMaximumHealth()
        );
    }

    public SendEntityFeed(final int hp, final int maxHp) {
        this(NO_OPPONENT, hp, maxHp);
    }

    public SendEntityFeed() {
        this(0, 0);
    }

    @Override
    public boolean encode(final Player player) {
        builder.writeShort(opponentId)
                .writeShort(hp)
                .writeShort(maxHp);
        return true;
    }

}
