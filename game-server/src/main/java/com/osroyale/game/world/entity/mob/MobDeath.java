package com.osroyale.game.world.entity.mob;

import com.osroyale.game.task.Task;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.combat.CombatConstants;
import com.osroyale.net.packet.out.SendScreenshot;

/**
 * The parent class that handles the death process for all mobs.
 *
 * @param <T> the type of mob the death process is being executed for.
 * @author lare96 <http://github.com/lare96>
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public abstract class MobDeath<T extends Mob> extends Task {

    /** The mob. */
    protected final T mob;

    /** The counter that will determine which part of the death process we are on. */
    private int counter;

    /** Creates a new {@link MobDeath}. */
    public MobDeath(T mob, int delay) {
        super(true, delay);
        this.mob = mob;
    }

    /** The part of the death process where the character is prepared for the rest of the death tick. */
    public abstract void preDeath(Mob killer);

    /** The main part of the death process where the killer is found for the character. */
    public abstract void death();

    /** The last part of the death process where the character is reset. */
    public abstract void postDeath(Mob killer);


    @Override
    public void onSchedule() {
        mob.setDead(true);
        mob.locking.lock();
        mob.getCombat().reset();
        mob.getPoisonDamage().set(0);
        mob.resetFace();
        mob.getCombat().resetTimers(-CombatConstants.COMBAT_LOGOUT_COOLDOWN);
    }

    @Override
    public final void execute() {
        Mob killer = mob.getCombat().getDamageCache().calculateProperKiller().orElse(null);
        switch (++counter) {
            case 1:
                if (killer != null) {
                    killer.getCombat().preKill(mob, mob.getCombat().getDamageCache().lastHit);
                    if (killer.isPlayer()) {
                        World.sendKillFeed(killer, mob);
                    }
                }

                if (killer != null && /*(!killer.isPlayer() || !mob.isPlayer()) &&*/ killer.getCombat().isAttacking(mob)) {
                    mob.getCombat().preDeath(killer, mob.getCombat().getDamageCache().lastHit);
                    if (!mob.isDead()) {
                        cancel();
                        return;
                    }
                    killer.getCombat().resetTimers(-CombatConstants.COMBAT_LOGOUT_COOLDOWN);
                    killer.getCombat().reset();
                }
                break;
            case 2:
                if (killer != null) {
                    if (killer.isPlayer() && mob.isPlayer() && killer.getPlayer().settings.screenshotKill) {
                        killer.getPlayer().send(new SendScreenshot());
                    }
                }
                preDeath(killer);
                break;
            case 3:
                death();
                postDeath(killer);
                if (killer != null) {
                    killer.getCombat().onKill(mob, mob.getCombat().getDamageCache().lastHit);
                    mob.getCombat().onDeath(killer, mob.getCombat().getDamageCache().lastHit);
                }
                mob.getCombat().getDamageCache().clear();
                break;
            case 4:
                this.cancel();
                break;
        }
    }

    @Override
    public void onCancel(boolean logout) {
//        mob.walkingQueue.setLock(false);
        mob.setDead(false);
        mob.locking.unlock();
    }
}