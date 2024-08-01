package com.osroyale.game.world.entity.combat.strategy.npc.boss;

import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.Projectile;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.task.Task;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.combat.CombatType;
import com.osroyale.game.world.entity.combat.hit.CombatHit;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.hit.HitIcon;
import com.osroyale.game.world.entity.combat.projectile.CombatProjectile;
import com.osroyale.game.world.entity.combat.strategy.CombatStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.MultiStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcMagicStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcMeleeStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcRangedStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.prayer.Prayer;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.position.Area;
import com.osroyale.game.world.position.Position;
import com.osroyale.util.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.osroyale.game.world.entity.combat.CombatUtil.createStrategyArray;
import static com.osroyale.game.world.entity.combat.CombatUtil.randomStrategy;

public class Cerberus extends MultiStrategy {
    private static final MagicAttack MAGIC = new MagicAttack();
    private static final RangedAttack RANGED = new RangedAttack();
    private static final LavaAttack LAVA = new LavaAttack();
    private static final GhostAttack GHOST = new GhostAttack();
    private static final CombatStrategy<Npc>[] FULL_STRATEGIES = createStrategyArray(NpcMeleeStrategy.get(), MAGIC, RANGED);

    public Cerberus() {
        currentStrategy = MAGIC;
    }

    @Override
    public boolean canAttack(Npc attacker, Mob defender) {
        int random = Utility.random(5, 20);

        boolean spawnedLava = attacker.attributes.is("CERBERUS_LAVA");
        boolean spawnedGhost = attacker.attributes.is("CERBERUS_GHOST");

        if (attacker.getCurrentHealth() <= 200 && !spawnedLava && random >= 17) {
            currentStrategy = LAVA;
        } else if (attacker.getCurrentHealth() <= 400 && !spawnedGhost && random > 14) {
            currentStrategy = GHOST;
        } else {
            currentStrategy = randomStrategy(FULL_STRATEGIES);
        }
        return currentStrategy.canAttack(attacker, defender);
    }

    private static class GhostAttack extends NpcMagicStrategy {
        GhostAttack() {
            super(CombatProjectile.getDefinition("EMPTY"));
        }

        @Override
        public void hit(Npc attacker, Mob defender, Hit hit) {
        }

        @Override
        public void block(Mob attacker, Npc defender, Hit hit, CombatType combatType) {
        }

        private void special(Npc ghost, Mob other, Npc attacker) {
            Projectile projectile = new Projectile(0, 10, 30, 20, 20);
            Hit hit = new Hit(30);
            Prayer overhead = null;

            ghost.face(other);
            ghost.animate(4504);

            switch (ghost.id) {
                case 5867://RANGED
                    overhead = Prayer.PROTECT_FROM_RANGE;
                    projectile.setId(1230);
                    hit.setIcon(HitIcon.RANGED);
                    break;
                case 5868://MAGIC
                    overhead = Prayer.PROTECT_FROM_MAGIC;
                    projectile.setId(127);
                    hit.setIcon(HitIcon.MAGIC);
                    break;
                case 5869://MELEE
                    overhead = Prayer.PROTECT_FROM_MELEE;
                    projectile.setId(1248);
                    hit.setIcon(HitIcon.MELEE);
                    break;
            }

            Prayer finalOverhead = overhead;

            projectile.send(ghost, other);
            World.schedule(1, () -> {
                if (attacker.isDead()) {
                    return;
                }
                if (other.getPlayer().equipment.contains(12817)) {
                    hit.setDamage(22);
                } else if (other.prayer.isActive(finalOverhead)) {
                    int drain = other.getPlayer().equipment.contains(12821) ? 15 : 30;
                    Skill skill = other.skills.get(Skill.PRAYER);
                    skill.modifyLevel(level -> level - drain, 0, skill.getLevel());
                    other.skills.refresh(Skill.PRAYER);
                    hit.setDamage(0);
                }

                if (hit.getDamage() != 0) {
                    other.writeDamage(hit);
                }
            });
        }

        @Override
        public void start(Npc attacker, Mob defender, Hit[] hits) {
            if (!defender.isPlayer()) {
                return;
            }

            List<Npc> ghosts = new ArrayList<>(3);
            Player player = defender.getPlayer();

            ghosts.add(new Npc(5867, player.instance, Position.create(0, 0)));
            ghosts.add(new Npc(5868, player.instance, Position.create(0, 0)));
            ghosts.add(new Npc(5869, player.instance, Position.create(0, 0)));
            Collections.shuffle(ghosts);
            ghosts.get(0).setPosition(new Position(1239, 1265));
            ghosts.get(1).setPosition(new Position(1240, 1265));
            ghosts.get(2).setPosition(new Position(1241, 1265));
            ghosts.get(0).register();
            ghosts.get(1).register();
            ghosts.get(2).register();
            attacker.animate(4494);
            attacker.speak("Aaarrrooooooooo");
            attacker.attributes.set("CERBERUS_GHOST", Boolean.TRUE);

            World.schedule(new Task(1) {

                private boolean possible() {
                    return attacker != null
                            && !attacker.isDead()
                            && defender != null
                            && !defender.isDead()
                            && !defender.inTeleport
                            && !defender.locking.locked()
                            && Area.inCerberus(defender);
                }

                int count = 0;

                @Override
                public void execute() {
                    if (!possible()) {
                        cancel();
                        return;
                    }

                    if (count == 0) {
                        ghosts.get(0).walk(new Position(ghosts.get(0).getX(), ghosts.get(0).getY() - 9), true);
                        ghosts.get(1).walk(new Position(ghosts.get(1).getX(), ghosts.get(1).getY() - 9), true);
                        ghosts.get(2).walk(new Position(ghosts.get(2).getX(), ghosts.get(2).getY() - 9), true);
                    } else if (count == 13) {
                        special(ghosts.get(0), player, attacker);
                    } else if (count == 16) {
                        ghosts.get(0).walk(new Position(ghosts.get(0).getX(), ghosts.get(0).getY() + 9), true);
                        special(ghosts.get(1), player, attacker);
                    } else if (count == 19) {
                        ghosts.get(1).walk(new Position(ghosts.get(1).getX(), ghosts.get(1).getY() + 9), true);
                        special(ghosts.get(2), player, attacker);
                    } else if (count == 21) {
                        ghosts.get(2).walk(new Position(ghosts.get(2).getX(), ghosts.get(2).getY() + 9), true);
                    } else if (count == 40) {
                        cancel();
                    }
                    count++;
                }

                @Override
                public void onCancel(boolean logout) {
                    for (Npc ghost : ghosts) {
                        ghost.unregister();
                    }
                    attacker.attributes.set("CERBERUS_GHOST", Boolean.FALSE);
                }
            });
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            return new CombatHit[]{};
        }
    }

    private static class LavaAttack extends NpcMagicStrategy {
        LavaAttack() {
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
            Position[] boundaries = Utility.getInnerBoundaries(defender.getPosition(), 5, 5);
            List<Position> positions = Arrays.asList(boundaries);
            Collections.shuffle(positions);

            attacker.speak("Grrrrrrrrrrrrrr");
            attacker.animate(new Animation(4493, UpdatePriority.VERY_HIGH));
            attacker.attributes.set("CERBERUS_LAVA", Boolean.TRUE);

            World.schedule(lavaTask(attacker, defender, defender.getPosition()));
            World.schedule(lavaTask(attacker, defender, positions.get(0)));
            World.schedule(lavaTask(attacker, defender, positions.get(1)));
        }

        private Task lavaTask(Npc attacker, Mob defender, Position position) {
            return new Task(1) {
                int count = 0;

                @Override
                public void execute() {
                    if (count == 0) {
                        World.sendGraphic(new Graphic(1246), position, attacker.instance);
                    } else if (count == 19) {
                        World.sendGraphic(new Graphic(1247), position, attacker.instance);
                        attacker.attributes.set("CERBERUS_LAVA", Boolean.FALSE);
                        cancel();
                        return;
                    }
                    if (count % 2 == 0 && defender.getPosition().isWithinDistance(position, 1)) {
                        int damage = defender.getPosition().equals(position) ? 15 : 7;
                        defender.writeDamage(new Hit(damage));
                    }
                    count++;
                }
            };
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            return new CombatHit[]{};
        }
    }

    private static class MagicAttack extends NpcMagicStrategy {
        MagicAttack() {
            super(CombatProjectile.getDefinition("Cerberus magic"));
        }

        @Override
        public Animation getAttackAnimation(Npc attacker, Mob defender) {
            return new Animation(4490, UpdatePriority.HIGH);
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            CombatHit combatHit = nextMagicHit(attacker, defender, 29);
            combatHit.setAccurate(true);
            return new CombatHit[]{combatHit};
        }
    }

    private static class RangedAttack extends NpcRangedStrategy {
        RangedAttack() {
            super(CombatProjectile.getDefinition("Cerberus ranged"));
        }

        @Override
        public Animation getAttackAnimation(Npc attacker, Mob defender) {
            return new Animation(4490, UpdatePriority.HIGH);
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            CombatHit combatHit = nextMagicHit(attacker, defender, 29);
            combatHit.setAccurate(true);
            return new CombatHit[]{combatHit};
        }
    }

}
