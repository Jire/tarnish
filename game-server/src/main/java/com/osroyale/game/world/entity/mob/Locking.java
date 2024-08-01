package com.osroyale.game.world.entity.mob;

import com.osroyale.game.world.entity.mob.data.LockType;
import com.osroyale.game.world.entity.mob.data.PacketType;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class Locking {
    private final Mob mob;
    private LockType lock = null;
    private long lockTime = -1;

    Locking(Mob mob) {
        this.mob = mob;
    }

    /** Locks the mob indefinitely. */
    public void lock() {
        lock(Integer.MAX_VALUE, LockType.MASTER);
    }

    public void lock(int time) {
        lock(time, LockType.MASTER);
    }

    public void lock(LockType type) {
        lock(Integer.MAX_VALUE, type);
    }

    /** Locks the mob for a certain amount of time. */
    public void lock(int time, LockType type) {
        lock(time, TimeUnit.SECONDS, type);
    }

    /** Locks the mob for a certain amount of time. */
    public void lock(int time, TimeUnit gUnit, LockType type) {
        long start = System.currentTimeMillis();
        long timer = TimeUnit.MILLISECONDS.convert(time, gUnit);

        if (Long.MAX_VALUE - start <= timer)
            timer = (Long.MAX_VALUE - start);

        if (type.execute(mob, time, gUnit)) {
            mob.movement.reset();
            lock = type;
            lockTime = start + timer;
        }
    }

    /** Checks if the mob is locked. */
    public boolean locked() {
        if (mob.isDead())
            return true;
        boolean state = lock != null && lockTime - System.currentTimeMillis() >= 0;
        if (!state) unlock();
        return state;
    }

    public void status() {
        System.out.println();
        System.out.println("Lock Status");
        System.out.println("Locked: " + locked(LockType.WALKING));
        System.out.println("Lock Time: " + lockTime);
        System.out.println("Lock: " + lock);
        if (lock == null) {
            System.out.println("Lock Packet: null");
        } else {
            System.out.println("Lock Packet: ");
            Arrays.stream(lock.packets).forEach(System.out::println);
        }

        System.out.println();

    }

    /** Checks if the mob is locked by a certain type. */
    public boolean locked(LockType type) {
        return locked() && lock == type;
    }

    public boolean locked(PacketType packet) {
        return locked() && lock.isLocked(packet);
    }

    public boolean locked(PacketType packet, Object object) {
        return locked() && lock.isLocked(packet, mob, object);
    }

    /** Unlocks the mob. */
    public void unlock() {
        lock = null;
        lockTime = 0;
        mob.freezeImmunity.reset();
    }

    public LockType getLock() {
        return lock;
    }
}
