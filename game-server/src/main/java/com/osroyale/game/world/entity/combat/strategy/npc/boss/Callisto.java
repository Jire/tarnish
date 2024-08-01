package com.osroyale.game.world.entity.combat.strategy.npc.boss;

import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.Projectile;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.task.impl.ForceMovementTask;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.combat.CombatType;
import com.osroyale.game.world.entity.combat.attack.FightType;
import com.osroyale.game.world.entity.combat.hit.CombatHit;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.projectile.CombatProjectile;
import com.osroyale.game.world.entity.combat.strategy.CombatStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.MultiStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcMagicStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcMeleeStrategy;
import com.osroyale.game.world.entity.mob.Direction;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.player.ForceMovement;
import com.osroyale.game.world.pathfinding.path.SimplePathChecker;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.RandomUtils;
import com.osroyale.util.Utility;

import static com.osroyale.game.world.entity.combat.CombatUtil.createStrategyArray;
import static com.osroyale.game.world.entity.combat.CombatUtil.randomStrategy;
import static com.osroyale.game.world.entity.combat.projectile.CombatProjectile.getDefinition;

/** @author Daniel */
public class Callisto extends MultiStrategy {

    private static Roar ROAR = new Roar();
    private static Shockwave SHOCKWAVE = new Shockwave();
    private static Melee MELEE = new Melee();
    private static final CombatStrategy<Npc>[] FULL = createStrategyArray(MELEE, MELEE, MELEE, SHOCKWAVE);

    public Callisto() {
        currentStrategy = SHOCKWAVE;
    }

    @Override
    public boolean canAttack(Npc attacker, Mob defender) {
        if (!currentStrategy.withinDistance(attacker, defender)) {
            if (RandomUtils.success(0.10)) {
                currentStrategy = ROAR;
            } else {
                currentStrategy = SHOCKWAVE;
            }
        }
        return currentStrategy.canAttack(attacker, defender);
    }

    @Override
    public void block(Mob attacker, Npc defender, Hit hit, CombatType combatType) {
        currentStrategy.block(attacker, defender, hit, combatType);
        defender.getCombat().attack(attacker);
    }

    @Override
    public void finishOutgoing(Npc attacker, Mob defender) {
        currentStrategy.finishOutgoing(attacker, defender);
        if (RandomUtils.success(0.10)) {
            currentStrategy = ROAR;
        } else {
            currentStrategy = randomStrategy(FULL);
        }
    }

    @Override
    public int getAttackDelay(Npc attacker, Mob defender, FightType fightType) {
        return attacker.definition.getAttackDelay();
    }

    private static class Roar extends NpcMagicStrategy {
        Roar() {
            super(CombatProjectile.getDefinition("EMPTY"));
        }

        @Override
        public void start(Npc attacker, Mob defender, Hit[] hits) {
            attacker.animate(new Animation(4921, UpdatePriority.VERY_HIGH));
            World.sendProjectile(attacker, defender, new Projectile(395, 46, 80, 43, 31));
            World.schedule(1, () -> {
                if (defender.isPlayer()) {
                    Position current = defender.getPosition();
                    Position best = Utility.findBestInside(defender, attacker);
                    int dx = current.getX() - best.getX();
                    int dy = current.getY() - best.getY();

                    Direction opposite = Direction.getFollowDirection(attacker.getPosition(), defender.getPosition());
//                    for (int x = 1; x <= 2; x++) {
                        int y = dy / (dx == 0 ? dy : dx);
                        Position destination = current.transform(dx, y);
                        if (SimplePathChecker.checkLine(defender, destination))
                            current = destination;
//                    }
                    defender.damage(new Hit(Utility.random(1, 3)));
                    defender.interact(attacker);
                    defender.getPlayer().send(new SendMessage("Callisto's roar throws you backwards."));

                    Position offset = new Position(current.getX() - defender.getX(), current.getY() - defender.getY());
                    ForceMovement movement = new ForceMovement(defender.getPosition().copy(), offset, 33, 60, Direction.getOppositeDirection(opposite));

                    int anim = defender.mobAnimation.getWalk();
                    World.schedule(new ForceMovementTask(defender, 3, 0, movement, new Animation(1157, UpdatePriority.VERY_HIGH)) {
                        @Override
                        public void onSchedule() {
                            super.onSchedule();
                            defender.mobAnimation.setWalk(1157);
                            defender.locking.lock();
                        }

                        @Override
                        public void onCancel(boolean logout) {
                            super.onCancel(logout);
                            defender.mobAnimation.setWalk(anim);
                            defender.locking.unlock();
                        }
                    });
                }
            });
        }

        @Override
        public void hit(Npc attacker, Mob defender, Hit hit) {
        }

        @Override
        public void attack(Npc attacker, Mob defender, Hit hit) {
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            CombatHit hit = nextMagicHit(attacker, defender, 3);
            hit.setAccurate(true);
            return new CombatHit[]{hit};
        }
    }

    private static class Shockwave extends NpcMagicStrategy {
        private static final Animation ANIMATION = new Animation(4922, UpdatePriority.HIGH);

        Shockwave() {
            super(getDefinition("Callisto Shockwave"));
        }

        @Override
        public void hit(Npc attacker, Mob defender, Hit hit) {
            super.hit(attacker, defender, hit);
            if (defender.isPlayer()) {
                defender.getPlayer().send(new SendMessage("Callisto's fury sends an almighty shockwave through you."));
                defender.graphic(new Graphic(80, true));
            }
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            CombatHit hit = nextMagicHit(attacker, defender, 23);
            hit.setAccurate(true);
            return new CombatHit[]{hit};
        }

        @Override
        public Animation getAttackAnimation(Npc attacker, Mob defender) {
            return ANIMATION;
        }
    }

    private static final class Melee extends NpcMeleeStrategy {
        private static final Animation ANIMATION = new Animation(4925, UpdatePriority.HIGH);

        @Override
        public int getAttackDistance(Npc attacker, FightType fightType) {
            return 2;
        }

        @Override
        public Animation getAttackAnimation(Npc attacker, Mob defender) {
            return ANIMATION;
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            return new CombatHit[]{nextMeleeHit(attacker, defender)};
        }
    }
}
