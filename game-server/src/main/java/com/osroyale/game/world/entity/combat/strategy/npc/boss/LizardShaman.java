package com.osroyale.game.world.entity.combat.strategy.npc.boss;

import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.task.Task;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.combat.CombatType;
import com.osroyale.game.world.entity.combat.CombatUtil;
import com.osroyale.game.world.entity.combat.hit.CombatHit;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.hit.HitIcon;
import com.osroyale.game.world.entity.combat.hit.Hitsplat;
import com.osroyale.game.world.entity.combat.projectile.CombatProjectile;
import com.osroyale.game.world.entity.combat.strategy.CombatStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.MultiStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcMagicStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcMeleeStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.position.Position;
import com.osroyale.util.Utility;

import static com.osroyale.game.world.entity.combat.CombatUtil.createStrategyArray;
import static com.osroyale.game.world.entity.combat.CombatUtil.randomStrategy;
import static com.osroyale.game.world.items.containers.equipment.Equipment.SHAYZIEN_PIECES;

public class LizardShaman extends MultiStrategy {
    private static final MagicAttack MAGIC = new MagicAttack();
    private static final AcidAttack ACID = new AcidAttack();
    private static final MinionsAttack MINION = new MinionsAttack();
    private static final JumpAttack JUMP = new JumpAttack();
    private static final CombatStrategy<Npc>[] FULL_STRATEGIES = createStrategyArray(NpcMeleeStrategy.get(), ACID, MAGIC, MAGIC, MAGIC, MINION, JUMP);
    private static final CombatStrategy<Npc>[] MAGIC_STRATEGIES = createStrategyArray(ACID, MAGIC, MAGIC, MAGIC, MINION);
    private static final CombatStrategy<Npc>[] BASIC_STRATEGIES = createStrategyArray(ACID, MAGIC, MAGIC, MAGIC, JUMP);

    public LizardShaman() {
        currentStrategy = NpcMeleeStrategy.get();
    }

    @Override
    public boolean canAttack(Npc attacker, Mob defender) {
        if (NpcMeleeStrategy.get().canAttack(attacker, defender)) {
            currentStrategy = randomStrategy(FULL_STRATEGIES);
        } else {
            currentStrategy = randomStrategy(MAGIC_STRATEGIES);
        }

        boolean spawned = attacker.attributes.is("LIZARD_SPAWNED");

        if (currentStrategy == MINION && spawned) {
            currentStrategy = randomStrategy(BASIC_STRATEGIES);
        }

        return currentStrategy.canAttack(attacker, defender);
    }

    private static class JumpAttack extends NpcMagicStrategy {
        JumpAttack() {
            super(CombatProjectile.getDefinition("EMPTY"));
        }

        @Override
        public void hit(Npc attacker, Mob defender, Hit hit) {
        }

        @Override
        public void block(Mob attacker, Npc defender, Hit hit, CombatType combatType) {
        }

        @Override
        public void start(Npc attacker, Mob defender, Hit[] hits) {
            Position position = defender.getPosition();
            attacker.animate(new Animation(7152, UpdatePriority.VERY_HIGH));

            World.schedule(new Task(1) {
                int count = 0;

                @Override
                public void execute() {
                    count++;

                    if (count == 2) {
                        attacker.setVisible(false);
                    } else if (count == 3) {
                        attacker.move(position);
                    } else if (count == 4) {
                        attacker.setVisible(true);
                        attacker.animate(new Animation(6946, UpdatePriority.VERY_HIGH));
                    } else if (count == 5) {
                        CombatUtil.areaAction(defender, actor -> {
                            if (actor.getPosition().equals(position)) {
                                actor.damage(new Hit(Utility.random(10, 15), HitIcon.MELEE));
                                actor.movement.reset();
                            }
                        });
                        cancel();
                    }
                }

                @Override
                public void onCancel(boolean logout) {
                    attacker.attack(defender);
                }
            });
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            return new CombatHit[]{};
        }
    }

    private static class MinionsAttack extends NpcMagicStrategy {
        MinionsAttack() {
            super(CombatProjectile.getDefinition("EMPTY"));
        }

        @Override
        public void hit(Npc attacker, Mob defender, Hit hit) {
        }

        @Override
        public void block(Mob attacker, Npc defender, Hit hit, CombatType combatType) {
        }

        @Override
        public void start(Npc attacker, Mob defender, Hit[] hits) {
            attacker.attributes.set("LIZARD_SPAWNED", Boolean.TRUE);
            for (int index = 0; index < 3; index++) {
                Npc minion = new Npc(6768, Utility.randomElement(attacker.boundaries));
                minion.register();
                minion.follow(defender);

                World.schedule(8, () -> {
                    World.sendGraphic(new Graphic(1295), minion.getPosition(), attacker.instance);

                    if (defender.getPosition().isWithinDistance(minion.getPosition(), 3)) {
                        defender.damage(new Hit(Utility.random(8) + 2));
                    }

                    minion.unregister();
                    attacker.attributes.set("LIZARD_SPAWNED", Boolean.FALSE);
                });
            }
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            return new CombatHit[]{};
        }
    }

    private static class MagicAttack extends NpcMagicStrategy {
        MagicAttack() {
            super(CombatProjectile.getDefinition("Shaman magic"));
        }

        @Override
        public Animation getAttackAnimation(Npc attacker, Mob defender) {
            return new Animation(7193, UpdatePriority.HIGH);
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            CombatHit combatHit = nextMagicHit(attacker, defender, 31);
            combatHit.setAccurate(true);
            return new CombatHit[]{combatHit};
        }
    }

    private static class AcidAttack extends NpcMagicStrategy {
        AcidAttack() {
            super(CombatProjectile.getDefinition("Shaman acid"));
        }

        @Override
        public Animation getAttackAnimation(Npc attacker, Mob defender) {
            return new Animation(7193, UpdatePriority.HIGH);
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            CombatHit combatHit = nextMagicHit(attacker, defender);
            combatHit.setHitsplat(Hitsplat.POISON);
            combatHit.setIcon(HitIcon.NONE);
            combatHit.setAccurate(false);
            int itemCount = 0;
            for (int index = 0; index < SHAYZIEN_PIECES.length; index++) {
                if (defender.getPlayer().equipment.contains(SHAYZIEN_PIECES[index][0])) {
                    itemCount++;
                }
            }
            int damage = (itemCount == SHAYZIEN_PIECES.length) ? 0 : Utility.random(25 - 5 * itemCount, 30 - 5 * itemCount);
            combatHit.setDamage(damage);
            return new CombatHit[]{combatHit};
        }
    }
}