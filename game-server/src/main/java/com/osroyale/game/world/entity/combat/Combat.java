package com.osroyale.game.world.entity.combat;

import com.osroyale.content.bot.PlayerBot;
import com.osroyale.game.task.Task;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.combat.attack.FightStyle;
import com.osroyale.game.world.entity.combat.attack.FightType;
import com.osroyale.game.world.entity.combat.attack.listener.CombatListener;
import com.osroyale.game.world.entity.combat.formula.CombatFormula;
import com.osroyale.game.world.entity.combat.hit.CombatData;
import com.osroyale.game.world.entity.combat.hit.CombatHit;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.strategy.CombatStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.position.Position;
import com.osroyale.game.world.region.Region;
import com.osroyale.net.packet.out.SendEntityFeed;
import com.osroyale.util.Stopwatch;
import com.osroyale.util.Utility;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class Combat<T extends Mob> {
    private final T attacker;

    private CombatTarget target;
    private Mob lastAggressor, lastVictim;

    private final Stopwatch lastAttacked = Stopwatch.start();
    public final Stopwatch lastBlocked = Stopwatch.start();

    private FightType fightType;

    private CombatType combatType;
    private FightStyle fightStyle;

    private final CombatFormula<T> formula = new CombatFormula<>();
    private final Deque<CombatListener<? super T>> listeners = new ArrayDeque<>();
    private final Deque<CombatListener<? super T>> pendingAddition = new ArrayDeque<>();
    private final Deque<CombatListener<? super T>> pendingRemoval = new ArrayDeque<>();

    private final CombatDamage damageCache = new CombatDamage();
    private final Deque<CombatData<T>> combatQueue = new ArrayDeque<>();
    private final Deque<Hit> damageQueue = new ArrayDeque<>();

    private final int[] hitsplatCooldowns = new int[4];
    private int cooldown;

    public Combat(T attacker) {
        this.attacker = attacker;
        this.target = new CombatTarget(attacker);
        fightType = FightType.UNARMED_PUNCH;
    }

    public boolean attack(Mob defender) {
        if (attacker.isPlayer()) {
            Player player = attacker.getPlayer();
            if (!player.interfaceManager.isMainClear() || !player.interfaceManager.isDialogueClear()) {
                player.interfaceManager.close();
            }
        }

        if (!canAttack(defender, attacker.getStrategy())) {
            attacker.face(defender.getPosition());
            attacker.movement.reset();
            target.resetTarget();
            return false;
        }

        target.setTarget(defender);
        attacker.attack(defender);
        return true;
    }

    public void tick() {
        updateListeners();

        performChecks(target.getTarget());

        if (!checkDistances(target.getTarget()))
            reset();

        if (cooldown > 0) {
            cooldown--;
        }

        if (target.getTarget() != null) {
            attacker.interact(target.getTarget());

            if (cooldown == 0) {
                CombatStrategy<? super T> strategy = attacker.getStrategy();
                submitStrategy(target.getTarget(), strategy);
            }
        }


        while (!combatQueue.isEmpty()) {
            CombatData<T> data = combatQueue.poll();
            World.schedule(hitTask(data));
        }

        for (int index = 0, sent = 0; index < hitsplatCooldowns.length; index++) {
            if (hitsplatCooldowns[index] > 0) {
                hitsplatCooldowns[index]--;
            } else if (sent < 2 && sendNextHitsplat()) {
                hitsplatCooldowns[index] = 2;
                sent++;
            }
        }

        final Mob targetMob = target.getTarget();
        if (targetMob != null && attacker.isPlayer() && isAttacking(targetMob)) {
            attacker.getPlayer().send(
                    new SendEntityFeed(targetMob));
        }
    }

    private boolean checkDistances(Mob defender) {
        return defender != null && Utility.withinDistance(attacker, defender, Region.VIEW_DISTANCE);
    }

    private boolean sendNextHitsplat() {
        if (damageQueue.isEmpty()) {
            return false;
        }

        if (attacker.getCurrentHealth() <= 0) {
            damageQueue.clear();
            return false;
        }

        Hit hit = damageQueue.poll();
        attacker.writeDamage(hit);
        return true;
    }

    private void updateListeners() {
        if (!pendingAddition.isEmpty()) {
            for (Iterator<CombatListener<? super T>> iterator = pendingAddition.iterator(); iterator.hasNext(); ) {
                CombatListener<? super T> next = iterator.next();
                addModifier(next);
                listeners.add(next);
                iterator.remove();
            }
        }

        if (!pendingRemoval.isEmpty()) {
            for (Iterator<CombatListener<? super T>> iterator = pendingRemoval.iterator(); iterator.hasNext(); ) {
                CombatListener<? super T> next = iterator.next();
                removeModifier(next);
                listeners.remove(next);
                iterator.remove();
            }
        }
    }

    public void performChecks(final Mob defender) {
        if (defender == null) return;

        for (final CombatListener<? super T> listener : listeners) {
            listener.performChecks(attacker, defender);
        }
        final CombatStrategy<? super T> strategy = attacker.getStrategy();
        if (strategy != null) {
            strategy.performChecks(attacker, defender);
        }
    }

    public void submitStrategy(Mob defender, CombatStrategy<? super T> strategy) {
        if (!canAttack(defender, strategy)) {
            return;
        }

        if (!checkWithin(attacker, defender, strategy)) {
            return;
        }

        attacker.interact(target.getTarget());

        formula.add(strategy);
        init(defender, strategy);
        cooldown(strategy.getAttackDelay(attacker, defender, fightType));
        submitHits(defender, strategy, strategy.getHits(attacker, defender));
        formula.remove(strategy);

        if (attacker.isPlayer() && defender.isPlayer()) {
            attacker.getPlayer().skulling.checkForSkulling(defender.getPlayer());
        }
    }

    public void submitHits(Mob defender, CombatHit... hits) {
        CombatStrategy<? super T> strategy = attacker.getStrategy();
        addModifier(strategy);
        init(defender, strategy);
        submitHits(defender, strategy, hits);
        removeModifier(strategy);
    }

    private void submitHits(Mob defender, CombatStrategy<? super T> strategy, CombatHit... hits) {
        start(defender, strategy, hits);
        for (int index = 0; index < hits.length; index++) {
            boolean last = index == hits.length - 1;
            CombatHit hit = hits[index];
            CombatData<T> data = new CombatData<>(attacker, defender, hit, strategy, last);
            attack(defender, hit, strategy);
            combatQueue.add(data);
        }
    }

    public void queueDamage(Hit hit) {
        if (attacker.getCurrentHealth() <= 0) {
            return;
        }

        if (damageQueue.size() > 10) {
            attacker.decrementHealth(hit);
            return;
        }

        damageQueue.add(hit);
    }

    public void clearDamageQueue() {
        damageQueue.clear();
    }

    public void cooldown(int delay) {
        if (cooldown < delay)
            cooldown = delay;
    }

    public void setCooldown(int delay) {
        cooldown = delay;
    }

    public boolean canAttack(Mob defender) {
        if (!CombatUtil.validate(attacker, defender)) {
            return false;
        }
        if (!CombatUtil.canBasicAttack(attacker, defender)) {
            return false;
        }
        if (!attacker.getStrategy().canAttack(attacker, defender)) {
            return false;
        }
        for (CombatListener<? super T> listener : listeners) {
            if (!listener.canAttack(attacker, defender)) {
                return false;
            }
        }
        return defender.getCombat().canOtherAttack(attacker);
    }

    private boolean canAttack(Mob defender, CombatStrategy<? super T> strategy) {
        if (!CombatUtil.validateMobs(attacker, defender)) {
            return false;
        }
        if (!CombatUtil.canAttack(attacker, defender)) {
            return false;
        }
        if (!strategy.canAttack(attacker, defender)) {
            return false;
        }
        for (CombatListener<? super T> listener : listeners) {
            if (!listener.canAttack(attacker, defender)) {
                return false;
            }
        }
        return defender.getCombat().canOtherAttack(attacker);
    }

    private boolean canOtherAttack(Mob attacker) {
        T defender = this.attacker;
        for (CombatListener<? super T> listener : listeners) {
            if (!listener.canOtherAttack(attacker, defender)) {
                return false;
            }
        }
        return defender.getStrategy().canOtherAttack(attacker, defender);
    }

    private void init(Mob defender, CombatStrategy<? super T> strategy) {
        strategy.init(attacker, defender);
        listeners.forEach(listener -> listener.init(attacker, defender));
    }

    private void start(Mob defender, CombatStrategy<? super T> strategy, Hit... hits) {
        if (!CombatUtil.validateMobs(attacker, defender)) {
            combatQueue.removeIf(_hit -> _hit.getDefender() == defender);
            if (defender.inTeleport)
                defender.getCombat().damageQueue.clear();
            reset();
            return;
        }

        defender.getCombat().lastBlocked.reset();
        defender.getCombat().lastAggressor = attacker;
        strategy.start(attacker, defender, hits);
        Hit[] finalHits = hits;
        listeners.forEach(listener -> listener.start(attacker, defender, finalHits));
    }

    private void attack(Mob defender, Hit hit, CombatStrategy<? super T> strategy) {
        if (!CombatUtil.validateMobs(attacker, defender)) {
            combatQueue.removeIf(_hit -> _hit.getDefender() == defender);
            if (defender.inTeleport)
                defender.getCombat().damageQueue.clear();
            reset();
            return;
        }

        lastVictim = defender;
        lastAttacked.reset();
        attacker.action.reset();

        strategy.attack(attacker, defender, hit);
        listeners.forEach(listener -> listener.attack(attacker, defender, hit));
    }

    private void block(Mob attacker, Hit hit, CombatType combatType) {
        T defender = this.attacker;
        lastBlocked.reset();
        lastAggressor = attacker;
        defender.action.reset();
        listeners.forEach(listener -> listener.block(attacker, defender, hit, combatType));
        defender.getStrategy().block(attacker, defender, hit, combatType);
        defender.getCombat().retaliate(attacker);
    }

    private void hit(Mob defender, Hit hit, CombatStrategy<? super T> strategy) {
        if (!CombatUtil.validateMobs(attacker, defender)) {
            combatQueue.removeIf(_hit -> _hit.getDefender() == defender);
            if (defender.inTeleport)
                defender.getCombat().damageQueue.clear();
            reset();
            return;
        }

        strategy.hit(attacker, defender, hit);
        listeners.forEach(listener -> listener.hit(attacker, defender, hit));

        if (!strategy.getCombatType().equals(CombatType.MAGIC) && hit.getDamage() > -1 && defender.id != 8060) {
            defender.animate(CombatUtil.getBlockAnimation(defender));
        }

        if (defender.getCurrentHealth() - hit.getDamage() > 0) {
            defender.getCombat().block(attacker, hit, strategy.getCombatType());
        }
    }

    private void hitsplat(Mob defender, Hit hit, CombatStrategy<? super T> strategy) {
        if (!CombatUtil.validateMobs(attacker, defender)) {
            combatQueue.removeIf(_hit -> _hit.getDefender() == defender);
            if (defender.inTeleport)
                defender.getCombat().damageQueue.clear();
            reset();
            return;
        }

        strategy.hitsplat(attacker, defender, hit);
        listeners.forEach(listener -> listener.hitsplat(attacker, defender, hit));

        if (!strategy.getCombatType().equals(CombatType.MAGIC) || hit.isAccurate()) {
            if (defender.getCurrentHealth() > 0 && hit.getDamage() >= 0) {
                defender.getCombat().queueDamage(hit);
                defender.getCombat().damageCache.add(attacker, hit);
            }
        }
    }

    private void retaliate(Mob attacker) {
        T defender = this.attacker;

        if (attacker.isPlayer()) {
            Player player = attacker.getPlayer();

            if (!player.interfaceManager.isMainClear() || !player.interfaceManager.isDialogueClear()) {
                player.interfaceManager.close();
            }

            if (defender.isPlayer() && defender.getPlayer().isBot) {
                ((PlayerBot) defender.getPlayer()).retaliate(player);
            }
        }

        if (defender.isPlayer()) {
            Player player = defender.getPlayer();

            if (!player.damageImmunity.finished()) {
                return;
            }

            if (!player.interfaceManager.isMainClear() || !player.interfaceManager.isDialogueClear()) {
                player.interfaceManager.close();
            }
        }

        if (target.getTarget() != null && !target.isTarget(attacker)) {
            return;
        }

        if (!CombatUtil.canAttack(attacker, defender)) {
            return;
        }

        if (defender.isAutoRetaliate() && (defender.isNpc() || defender.movement.isMovementDone())) {
            attack(attacker);
        }
    }

    public void preDeath(Mob attacker, Hit hit) {
        if (attacker != null) {
            T defender = this.attacker;
            defender.getStrategy().preDeath(attacker, defender, hit);
            listeners.forEach(listener -> listener.preDeath(attacker, defender, hit));
            reset();
        }
    }

    public void preKill(Mob defender, Hit hit) {
        if (attacker != null) {
            defender.getCombat().listeners.forEach(listener -> listener.preKill(attacker, defender, hit));
        }
    }

    public void onDamage(Hit hit) {
        if (attacker != null) {
            listeners.forEach(listener -> listener.onDamage(attacker, hit));
        }
    }

    public void onKill(Mob defender, Hit hit) {
        if (attacker != null) {
            if (attacker.isPlayer() && attacker.getPlayer().getCombatSpecial() != null) {
                attacker.getPlayer().getCombatSpecial().getStrategy().onKill(attacker.getPlayer(), defender, hit);
            }
            listeners.forEach(listener -> listener.onKill(attacker, defender, hit));
        }
    }

    public void onDeath(Mob attacker, Hit hit) {
        if (attacker != null) {
            T defender = this.attacker;
            listeners.forEach(listener -> listener.onDeath(attacker, defender, hit));
            defender.movement.reset();
            defender.resetWaypoint();
            reset();
        }
    }

    private void finishOutgoing(Mob defender, CombatStrategy<? super T> strategy) {
        strategy.finishOutgoing(attacker, defender);
        listeners.forEach(listener -> listener.finishOutgoing(attacker, defender));
        defender.getCombat().finishIncoming(attacker);
    }

    private void finishIncoming(Mob attacker) {
        T defender = this.attacker;
        defender.getStrategy().finishIncoming(attacker, defender);
        listeners.forEach(listener -> listener.finishIncoming(attacker, defender));
    }

    public void reset(boolean force) {
        if (force || target.getTarget() != null) {
            target.resetTarget();
            attacker.resetFace();
            attacker.movement.reset();
            attacker.resetWaypoint();
        }
    }

    public void reset() {
        reset(false);
    }

    public void addModifier(FormulaModifier<? super T> modifier) {
        formula.add(modifier);
    }

    public void removeModifier(FormulaModifier<? super T> modifier) {
        formula.remove(modifier);
    }

    public void addFirst(FormulaModifier<? super T> modifier) {
        formula.addFirst(modifier);
    }

    public void removeFirst() {
        formula.removeFirst();
    }

    public void addListener(CombatListener<? super T> listener) {
        if (listeners.contains(listener) || pendingAddition.contains(listener)) {
            return;
        }
//        System.out.println("[@Combat] added listener: " + listener.getClass().getSimpleName());
        pendingAddition.add(listener);
    }

    public void removeListener(CombatListener<? super T> listener) {
        if (pendingRemoval.contains(listener)) {
            return;
        }
//        System.out.println("[@Combat] removed listener: " + listener.getClass().getSimpleName());
        pendingRemoval.add(listener);
    }

    public void clearIncoming() {
        combatQueue.clear();
    }

    public boolean inCombat() {
        return isAttacking() || isUnderAttack();
    }

    public boolean inCombatWith(Mob mob) {
        return isAttacking(mob) || isUnderAttackBy(mob);
    }

    public boolean isAttacking() {
        return target.getTarget() != null && !hasPassed(lastAttacked, CombatConstants.COMBAT_TIMER_COOLDOWN);
    }

    public boolean isUnderAttack() {
        return lastAggressor != null && !hasPassed(lastBlocked, CombatConstants.COMBAT_TIMER_COOLDOWN);
    }

    public boolean isAttacking(Mob defender) {
        return defender != null && defender.equals(lastVictim) && !hasPassed(lastAttacked, CombatConstants.COMBAT_TIMER_COOLDOWN);
    }

    public boolean isUnderAttackBy(Mob attacker) {
        return attacker != null && attacker.equals(lastAggressor) && !hasPassed(lastBlocked, CombatConstants.COMBAT_TIMER_COOLDOWN);
    }

    public boolean isUnderAttackByPlayer() {
        return lastAggressor != null && lastAggressor.isPlayer() && !hasPassed(lastBlocked, CombatConstants.COMBAT_TIMER_COOLDOWN);
    }

    public boolean isUnderAttackByNpc() {
        return lastAggressor != null && lastAggressor.isNpc() && !hasPassed(lastBlocked, CombatConstants.COMBAT_TIMER_COOLDOWN);
    }

    public int getCooldown() {
        return cooldown;
    }

    public FightType getFightType() {
        return fightType;
    }

    public CombatType getCombatType() {
        return combatType;
    }

    public void setFightType(FightType type) {
        this.fightType = type;
        this.fightStyle = type.getStyle();
    }

    public void setCombatType(CombatType type) {
        this.combatType = type;
    }

    public FightStyle getFightStyle() {
        return fightStyle;
    }

    public Mob getDefender() {
        return target.getTarget();
    }

    public CombatDamage getDamageCache() {
        return damageCache;
    }

    public void compare(Mob mob, int level, Position spawn) {
        target.compare(mob, level, spawn);
    }

    public void checkAggression(Position spawn) {
        target.checkAggression(spawn);
    }

    public boolean isLastAggressor(Mob mob) {
        return mob.equals(lastAggressor);
    }

    public Mob getLastVictim() {
        return lastVictim;
    }

    public Mob getLastAggressor() {
        return lastAggressor;
    }

    private Task hitTask(CombatData<T> data) {
        return new Task(data.getHitDelay()) {
            @Override
            public void execute() {
                hit(data.getDefender(), data.getHit(), attacker.getStrategy()/*data.getStrategy()*/);
                World.schedule(hitsplatTask(data));
                cancel();
            }
        };
    }

    private Task hitsplatTask(CombatData<T> data) {
        return new Task(data.getHitsplatDelay()) {
            @Override
            public void execute() {
                hitsplat(data.getDefender(), data.getHit(), data.getStrategy());
                if (data.isLastHit())
                    finishOutgoing(data.getDefender(), data.getStrategy());
                cancel();
            }
        };
    }

    private boolean hasPassed(Stopwatch timer, int delay) {
        return timer.elapsed(delay, TimeUnit.MILLISECONDS);
    }

    public boolean hasPassed(int delay) {
        return elapsedTime() - delay >= 0;
    }

    public long elapsedTime() {
        long attacked = lastAttacked.elapsedTime();
        long blocked = lastBlocked.elapsedTime();
        return Math.max(attacked, blocked);
    }

    public void resetTimers(int millis) {
        lastAttacked.reset(millis, TimeUnit.MILLISECONDS);
        lastBlocked.reset(millis, TimeUnit.MILLISECONDS);
    }

    public boolean checkWithin(T attacker, Mob defender, CombatStrategy<? super T> strategy) {
        if (strategy == null || Utility.inside(attacker, defender)) {
            return false;
        }
        if (!strategy.withinDistance(attacker, defender)) {
            return false;
        }
        for (CombatListener<? super T> listener : listeners) {
            if (!listener.withinDistance(attacker, defender))
                return false;
        }
        return true;
    }

    public int modifyAttackLevel(Mob defender, int level) {
        return formula.modifyAttackLevel(attacker, defender, level);
    }

    public int modifyStrengthLevel(Mob defender, int level) {
        return formula.modifyStrengthLevel(attacker, defender, level);
    }

    public int modifyDefenceLevel(Mob attacker, int level) {
        return formula.modifyDefenceLevel(attacker, this.attacker, level);
    }

    public int modifyRangedLevel(Mob defender, int level) {
        return formula.modifyRangedLevel(attacker, defender, level);
    }

    public int modifyMagicLevel(Mob defender, int level) {
        return formula.modifyMagicLevel(attacker, defender, level);
    }

    public int modifyAccuracy(Mob defender, int roll) {
        return formula.modifyAccuracy(attacker, defender, roll);
    }

    public int modifyDefensive(Mob attacker, int roll) {
        return formula.modifyDefensive(attacker, this.attacker, roll);
    }

    public int modifyDamage(Mob defender, int damage) {
        return formula.modifyDamage(attacker, defender, damage);
    }

    public int modifyOffensiveBonus(Mob defender, int bonus) {
        return formula.modifyOffensiveBonus(attacker, defender, bonus);
    }

    public int modifyAggresiveBonus(Mob defender, int bonus) {
        return formula.modifyAggressiveBonus(attacker, defender, bonus);
    }

    public int modifyDefensiveBonus(Mob attacker, int bonus) {
        return formula.modifyDefensiveBonus(attacker, this.attacker, bonus);
    }

}
