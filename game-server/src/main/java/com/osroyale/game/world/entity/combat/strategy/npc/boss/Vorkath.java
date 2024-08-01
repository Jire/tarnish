package com.osroyale.game.world.entity.combat.strategy.npc.boss;

import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.Projectile;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.task.TickableTask;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.combat.Combat;
import com.osroyale.game.world.entity.combat.CombatType;
import com.osroyale.game.world.entity.combat.CombatUtil;
import com.osroyale.game.world.entity.combat.attack.FightType;
import com.osroyale.game.world.entity.combat.hit.CombatHit;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.hit.Hitsplat;
import com.osroyale.game.world.entity.combat.projectile.CombatProjectile;
import com.osroyale.game.world.entity.combat.strategy.CombatStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.MultiStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcMagicStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcMeleeStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcRangedStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.impl.DragonfireStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.data.LockType;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.object.CustomGameObject;
import com.osroyale.game.world.pathfinding.TraversalMap;
import com.osroyale.game.world.position.Area;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.packet.out.SendWidget;
import com.osroyale.util.RandomUtils;
import com.osroyale.util.Utility;

import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import static com.osroyale.game.world.entity.combat.CombatUtil.createStrategyArray;

/**
 * The combat definition for Vorkath.
 *
 * @author Daniel
 */
public class Vorkath extends MultiStrategy {
    private static final NpcMeleeStrategy MELEE = NpcMeleeStrategy.get();
    private static final MagicAttack MAGIC = new MagicAttack();
    private static final RangedAttack RANGED = new RangedAttack();
    private static final FireballAttack FIREBALL = new FireballAttack();

    private static final DragonfireStrategy DRAGONFIRE = new DragonfireAttack();
    private static final DragonfireStrategy PINK_DRAGONFIRE = new PinkDragonfireAttack();
    private static final DragonfireStrategy VENOM_DRAGONFIRE = new VenomDragonfireAttack();

    private static final VenomSpecial VENOM_SPECIAL = new VenomSpecial();
    private static final FrozenSpecial FROZEN_SPECIAL = new FrozenSpecial();

    private static final CombatStrategy<Npc>[] FULL_STRATEGIES = createStrategyArray(MELEE, DRAGONFIRE, MAGIC, FIREBALL, RANGED, VENOM_DRAGONFIRE, PINK_DRAGONFIRE);
    private static final CombatStrategy<Npc>[] NON_MELEE = createStrategyArray(FIREBALL, DRAGONFIRE, MAGIC, RANGED, VENOM_DRAGONFIRE, PINK_DRAGONFIRE);
    private final CombatStrategy<Npc>[] SPECIALS = createStrategyArray(VENOM_SPECIAL, FROZEN_SPECIAL);

    private final Deque<CombatStrategy<Npc>> strategyQueue = new LinkedList<>();
    private int specialIndex;

    public Vorkath() {
        currentStrategy = MELEE;
    }

    @Override
    public void init(Npc attacker, Mob defender) {
        if (strategyQueue.isEmpty()) {
            for (int index = 0; index < 6; index++) {
                strategyQueue.add(RandomUtils.random(FULL_STRATEGIES));
            }
            strategyQueue.add(SPECIALS[specialIndex++ % SPECIALS.length]);
        }
        currentStrategy = strategyQueue.poll();
    }

    @Override
    public boolean canAttack(Npc attacker, Mob defender) {
        if (currentStrategy == MELEE && !MELEE.canAttack(attacker, defender)) {
            currentStrategy = RandomUtils.random(NON_MELEE);
        }
        return currentStrategy.canAttack(attacker, defender);
    }

    @Override
    public boolean withinDistance(Npc attacker, Mob defender) {
        if (currentStrategy == MELEE && !MELEE.withinDistance(attacker, defender)) {
            currentStrategy = RandomUtils.random(NON_MELEE);
        }
        return currentStrategy.canAttack(attacker, defender);
    }

    private static class VenomSpecial extends NpcMagicStrategy {
        VenomSpecial() {
            super(CombatProjectile.getDefinition("EMPTY"));
        }

        @Override
        public int getAttackDelay(Npc attacker, Mob defender, FightType fightType) {
            return 30;
        }

        @Override
        public void start(Npc attacker, Mob defender, Hit[] hits) {
            World.schedule(2, () -> {
                attacker.animate(new Animation(7957, UpdatePriority.HIGH));
                List<Position> boundaries = TraversalMap.getTraversableTiles(attacker.getPosition().transform(-7, -7), 30, 30);
                Collections.shuffle(boundaries);

                World.schedule(1, () -> {

                    Projectile projectile = new Projectile(1483, 10, 85, 20, 0);
                    projectile.send(attacker, defender.getPosition());
                    World.schedule(AcidTask(defender, defender.getPosition().copy()));


                    for (int index = 0; index < 40; index++) {
                        Position position = boundaries.get(index);
                        projectile.send(attacker, position);
                        World.schedule(AcidTask(defender, position));
                    }

                    final Projectile projectile2 = new Projectile(1482, 15, 95, 15, 15);

                    World.schedule(dragonFireTickableTask(attacker, defender, projectile2));
                });
            });
        }

        private TickableTask dragonFireTickableTask(Mob attacker, Mob defender, Projectile projectile) {
            return new TickableTask(false, 1) {

                private boolean possible() {
                    return attacker != null
                            && !attacker.isDead()
                            && !defender.isDead()
                            && !defender.inTeleport
                            && !defender.locking.locked()
                            && Area.inVorkath(defender);
                }

                @Override
                public void onCancel(boolean logout) {
                    super.onCancel(logout);

                    if (!logout && attacker != null) {
                        final Combat<? extends Mob> combat = attacker.getCombat();
                        if (combat != null) {
                            combat.clearDamageQueue();
                        }
                    }
                }

                @Override
                protected void tick() {
                    if (!possible()) {
                        cancel();
                        return;
                    }

                    if (tick > 27) {
                        cancel();
                        return;
                    }

                    if (tick < 3) {
                        return;
                    }

                    Position position = defender.getPosition().copy();
                    projectile.send(attacker, position);

                    World.schedule(3, () -> {
                        if (!possible()) {
                            final Combat<? extends Mob> combat = defender.getCombat();
                            if (combat != null) {
                                combat.clearDamageQueue();
                            }
                            return;
                        }

                        World.sendGraphic(new Graphic(131, UpdatePriority.HIGH), position, attacker.instance);

                        if (defender.getPosition().equals(position)) {
                            defender.damage(new Hit(Utility.random(30)));
                        }
                    });
                }
            };
        }

        private TickableTask AcidTask(Mob defender, Position position) {
            return new TickableTask(false, 2) {
                private CustomGameObject object;

                @Override
                protected void tick() {
                    if (tick == 1) {
                        object = new CustomGameObject(32000, defender.instance, position);
                        object.register();
                    } else if (tick == 13) {
                        object.unregister();
                        cancel();
                        return;
                    }

                    if (defender.getPosition().equals(position) && Area.inVorkath(defender)) {
                        defender.writeDamage(new Hit(Utility.random(1, 10)));
                    }
                }
            };
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            CombatHit combatHit = nextMagicHit(attacker, defender, 0);
            combatHit.setAccurate(true);
            combatHit.setDamage(-1);
            return new CombatHit[]{combatHit};
        }
    }

    private static class RangedAttack extends NpcRangedStrategy {

        RangedAttack() {
            super(CombatProjectile.getDefinition("Vorkath Ranged"));
        }

        @Override
        public Animation getAttackAnimation(Npc attacker, Mob defender) {
            return new Animation(7952, UpdatePriority.HIGH);
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            return new CombatHit[]{nextRangedHit(attacker, defender, 32)};
        }
    }

    private static class MagicAttack extends NpcMagicStrategy {

        MagicAttack() {
            super(CombatProjectile.getDefinition("Vorkath Magic"));
        }

        @Override
        public Animation getAttackAnimation(Npc attacker, Mob defender) {
            return new Animation(7952, UpdatePriority.HIGH);
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            return new CombatHit[]{nextRangedHit(attacker, defender, 32)};
        }
    }

    private static class FrozenSpecial extends NpcMagicStrategy {
        private final Projectile PROJECTILE = new Projectile(1470, 5, 85, 30, 30, 40);

        FrozenSpecial() {
            super(CombatProjectile.getDefinition("Vorkath Frozen Special"));
        }

        @Override
        public Animation getAttackAnimation(Npc attacker, Mob defender) {
            return new Animation(7952, UpdatePriority.HIGH);
        }

        @Override
        public void hit(Npc attacker, Mob defender, Hit hit) {
            defender.graphic(new Graphic(369));
            defender.locking.lock(LockType.FREEZE);

            attacker.blockInteract = true;
            attacker.resetFace();
            attacker.face(new Position(2277, 4057));
            attacker.animate(new Animation(7960, UpdatePriority.HIGH));
            PROJECTILE.send(attacker, new Position(2277, 4057));

            World.schedule(4, () -> {
                attacker.blockInteract = false;
                Npc zombie = new Npc(8063, defender.instance, new Position(2277, 4057)) {
                    @Override
                    public void appendDeath() {
                        super.appendDeath();
                        defender.locking.unlock();
                        defender.getPlayer().send(new SendWidget(SendWidget.WidgetType.FROZEN, 0));
                    }
                };
                zombie.instance = defender.instance;
                zombie.register();
                zombie.walkTo(defender, () -> {
                    World.sendGraphic(new Graphic(1460, true), zombie.getPosition(), defender.instance);
                    defender.damage(new Hit(60 * zombie.getCurrentHealth() / zombie.getMaximumHealth()));
                    defender.locking.unlock();
                    defender.getPlayer().send(new SendWidget(SendWidget.WidgetType.FROZEN, 0));
                    zombie.unregister();
                });
            });
        }

        @Override
        public int getAttackDelay(Npc attacker, Mob defender, FightType fightType) {
            return 15;
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            CombatHit combatHit = nextMagicHit(attacker, defender, -1);
            combatHit.setAccurate(false);
            return new CombatHit[]{combatHit};
        }
    }

    private static class DragonfireAttack extends DragonfireStrategy {
        DragonfireAttack() {
            super(CombatProjectile.getDefinition("Metalic dragonfire"));
        }

        @Override
        public Animation getAttackAnimation(Npc attacker, Mob defender) {
            return new Animation(7952, UpdatePriority.HIGH);
        }

        @Override
        public int getAttackDistance(Npc attacker, FightType fightType) {
            return 10;
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            return new CombatHit[]{CombatUtil.generateDragonfire(attacker, defender, 85, true)};
        }
    }

    private static class VenomDragonfireAttack extends DragonfireStrategy {
        VenomDragonfireAttack() {
            super(CombatProjectile.getDefinition("Vorkath Venom Dragonfire"));
        }

        @Override
        public Animation getAttackAnimation(Npc attacker, Mob defender) {
            return new Animation(7952, UpdatePriority.HIGH);
        }

        @Override
        public int getAttackDistance(Npc attacker, FightType fightType) {
            return 10;
        }

        @Override
        public void hit(Npc attacker, Mob defender, Hit hit) {
            super.hit(attacker, defender, hit);
            if (hit.isAccurate()) {
                defender.venom();
            }
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            CombatHit combatHit = CombatUtil.generateDragonfire(attacker, defender, 75, true);
            combatHit.setHitsplat(Hitsplat.VENOM);
            return new CombatHit[]{combatHit};
        }
    }

    private static class PinkDragonfireAttack extends DragonfireStrategy {
        PinkDragonfireAttack() {
            super(CombatProjectile.getDefinition("Vorkath Pink Dragonfire"));
        }

        @Override
        public Animation getAttackAnimation(Npc attacker, Mob defender) {
            return new Animation(7952, UpdatePriority.HIGH);
        }

        @Override
        public void hitsplat(Npc attacker, Mob defender, Hit hit) {
            super.hitsplat(attacker, defender, hit);

            if (defender.isPlayer()) {
                defender.prayer.reset();
            }
        }

        @Override
        public int getAttackDistance(Npc attacker, FightType fightType) {
            return 10;
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            return new CombatHit[]{CombatUtil.generateDragonfire(attacker, defender, 80, true)};
        }
    }

    private static class FireballAttack extends DragonfireStrategy {
        private final Projectile PROJECTILE = new Projectile(1481, 5, 105, 30, 30, 40);
        private final Graphic GRAPHIC = new Graphic(1466, true);
        private Position position;

        FireballAttack() {
            super(CombatProjectile.getDefinition("EMPTY"));
        }

        @Override
        public Animation getAttackAnimation(Npc attacker, Mob defender) {
            return new Animation(7960, UpdatePriority.HIGH);
        }

        @Override
        public void start(Npc attacker, Mob defender, Hit[] hits) {
            super.start(attacker, defender, hits);
            position = defender.getPosition().copy();
        }

        @Override
        public void attack(Npc attacker, Mob defender, Hit hit) {
            PROJECTILE.send(attacker, position);
        }

        @Override
        public void hit(Npc attacker, Mob defender, Hit hit) {
            super.hitsplat(attacker, defender, hit);
            World.sendGraphic(GRAPHIC, position, defender.instance);
            hit.setAccurate(false);
            if (defender.getPosition().equals(position)) {
                hit.setAs(CombatUtil.generateDragonfire(attacker, defender, 150, true));
                hit.setAccurate(true);
            } else if (Utility.withinOctal(defender.getPosition(), defender.width(), defender.length(), position, 1, 1, 1)) {
                hit.setAs(CombatUtil.generateDragonfire(attacker, defender, 150, true));
                hit.modifyDamage(damage -> damage / 2);
                hit.setAccurate(true);
            }
        }

        @Override
        public int getAttackDistance(Npc attacker, FightType fightType) {
            return 10;
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            return new CombatHit[]{nextMagicHit(attacker, defender, -1, CombatUtil.getHitDelay(attacker, defender, CombatType.MAGIC) + 1, 1)};
        }
    }

}
