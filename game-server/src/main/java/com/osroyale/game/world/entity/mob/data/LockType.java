package com.osroyale.game.world.entity.mob.data;

import com.osroyale.game.Graphic;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.net.packet.out.SendWidget;

import java.util.concurrent.TimeUnit;

/**
 * Holds all the lock types.
 *
 * @author Daniel
 */
public enum LockType {
    MASTER(PacketType.values()) {
        @Override
        public boolean execute(Mob mob, int time, TimeUnit gUnit) {
            if (mob.isPlayer())
                mob.getPlayer().action.reset();
            return true;
        }
    },
    MASTER_WITH_MOVEMENT(PacketType.MASTER_WITH_MOVEMENT) {
        @Override
        public boolean execute(Mob mob, int time, TimeUnit gUnit) {
            if (mob.isPlayer())
                mob.getPlayer().action.reset();
            return true;
        }
    },
    MASTER_WITH_COMMANDS(PacketType.MASTER_WITH_COMMANDS) {
        @Override
        public boolean execute(Mob mob, int time, TimeUnit gUnit) {
            if (mob.isPlayer())
                mob.getPlayer().action.reset();
            return true;
        }
    },
    OBJECT(PacketType.CLICK_OBJECT) {
        @Override
        public boolean execute(Mob mob, int time, TimeUnit gUnit) {
            mob.movement.reset();
            return true;
        }
    },
    WALKING(PacketType.WALKING, PacketType.MOVEMENT) {
        @Override
        public boolean execute(Mob mob, int time, TimeUnit gUnit) {
            return true;
        }
    },
    STUN(PacketType.TELEPORT, PacketType.WALKING, PacketType.COMBAT, PacketType.PICKUP_ITEM, PacketType.WIELD_ITEM, PacketType.COMMANDS, PacketType.CLICK_BUTTON, PacketType.CLICK_NPC, PacketType.CLICK_OBJECT, PacketType.USE_ITEM, PacketType.INTERACT) {
        @Override
        public boolean execute(Mob mob, int time, TimeUnit gUnit) {
            if (mob.locking.locked(STUN))
                return false;
            mob.graphic(new Graphic(80, true));

            if (mob.isPlayer()) {
                Player player = mob.getPlayer();
                player.resetFace();
                player.getCombat().reset();
                player.send(new SendMessage("You have been stunned!"));
                player.send(new SendWidget(SendWidget.WidgetType.STUN, time));
            }
            return true;
        }
    },
    FREEZE(PacketType.WALKING, PacketType.MOVEMENT) {
        @Override
        public boolean execute(Mob mob, int time, TimeUnit gUnit) {
            if (mob.locking.locked(FREEZE)) {
                return false;
            }

            if (mob.isPlayer()) {
                Player player = mob.getPlayer();
//                player.resetFace();
//                player.getCombat().reset();
                player.send(new SendMessage("You've been frozen!", true));
                player.send(new SendWidget(SendWidget.WidgetType.FROZEN, time));
            }
            return true;
        }
    };

    /** The lock packet flag. */
    public final PacketType[] packets;

    /** Handles the execution of the lock. */
    public abstract boolean execute(Mob mob, int time, TimeUnit gUnit);

    /** Constructs a new <code>LockType</code>. */
    LockType(PacketType... packets) {
        this.packets = packets;
    }

    public boolean isLocked(PacketType type) {
        return isLocked(type, null, null);
    }

    public boolean isLocked(PacketType type, Mob mob, Object object) {
        for (PacketType packet : packets) {
            if (type == packet)
                return !packet.exception(mob, object);
        }
        return false;
    }
}
