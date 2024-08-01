package com.osroyale.game.world.entity.combat.strategy.npc.boss;

import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.task.Task;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.combat.CombatType;
import com.osroyale.game.world.entity.combat.CombatUtil;
import com.osroyale.game.world.entity.combat.attack.FightType;
import com.osroyale.game.world.entity.combat.hit.CombatHit;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.hit.HitIcon;
import com.osroyale.game.world.entity.combat.projectile.CombatProjectile;
import com.osroyale.game.world.entity.combat.strategy.npc.MultiStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcMagicStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcMeleeStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.position.Position;
import com.osroyale.game.world.region.RegionManager;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.RandomUtils;
import com.osroyale.util.Utility;

import java.util.Arrays;
import java.util.LinkedList;

/** @author Daniel */
public class Vetion extends MultiStrategy {
    private static Earthquake EARTHQUAKE = new Earthquake();
    private static Magic MAGIC = new Magic();

    private Npc pet1, pet2;
    private boolean spawnPets, secondTrans;

    public Vetion() {
        currentStrategy = MAGIC;
    }

    @Override
    public boolean withinDistance(Npc attacker, Mob defender) {
        if (!currentStrategy.withinDistance(attacker, defender)) {
            currentStrategy = MAGIC;
        }
        return currentStrategy.withinDistance(attacker, defender);
    }

    @Override
    public boolean canAttack(Npc attacker, Mob defender) {
        if (!currentStrategy.withinDistance(attacker, defender)) {
            currentStrategy = MAGIC;
        }
        return currentStrategy.canAttack(attacker, defender);
    }

    @Override
    public boolean canOtherAttack(Mob attacker, Npc defender) {
        if (pet1 != null || pet2 != null) {
            if (attacker.isPlayer()) {
                attacker.getPlayer().message("You must kill his hellhounds before dealing damage!");
            }
            return false;
        }
        return currentStrategy.canOtherAttack(attacker, defender);
    }

    @Override
    public void onDeath(Mob attacker, Npc defender, Hit hit) {
        if (pet1 != null) pet1.unregister();
        if (pet2 != null) pet2.unregister();
    }

    @Override
    public void preDeath(Mob attacker, Npc defender, Hit hit) {
        super.preDeath(attacker, defender, hit);

        if (secondTrans) {
            return;
        }

        secondTrans = true;
        spawnPets = false;
        defender.setDead(false);
        defender.heal(255);
        defender.transform(6612);
        defender.speak("Do it again!");
        defender.getCombat().attack(attacker);

        World.schedule(new Task(500) {
            @Override
            public void execute() {
                cancel();
                if (defender.isDead())
                    return;
                defender.transform(6611);
                defender.getCombat().reset();
                secondTrans = false;
                spawnPets = false;
            }
        });
    }

    @Override
    public void finishIncoming(Mob attacker, Npc defender) {
        if (spawnPets || defender.getCurrentHealth() > defender.getMaximumHealth() / 2) {
            return;
        }

        Position[] innerBoundary = Utility.getInnerBoundaries(defender.getPosition().transform(-2, -2), defender.width() + 4, defender.length() + 4);
        defender.speak(defender.id == 6611 ? "Kill, my pets!" : "Bahh! Go, dogs!!");
        spawnPets = true;

        if (!secondTrans) {
            pet1 = new Npc(6613, RandomUtils.random(innerBoundary)) {
                @Override
                public void appendDeath() {
                    super.appendDeath();
                    pet1 = null;
                }
            };
            pet2 = new Npc(6613, RandomUtils.random(innerBoundary)) {
                @Override
                public void appendDeath() {
                    super.appendDeath();
                    pet2 = null;
                }
            };
        } else {
            pet1 = new Npc(6614, RandomUtils.random(innerBoundary)) {
                @Override
                public void appendDeath() {
                    super.appendDeath();
                    pet1 = null;
                }
            };
            pet2 = new Npc(6614, RandomUtils.random(innerBoundary)) {
                @Override
                public void appendDeath() {
                    super.appendDeath();
                    pet2 = null;
                }
            };
        }
        pet1.register();
        pet2.register();
        pet1.definition.setRespawnTime(-1);
        pet2.definition.setRespawnTime(-1);
        pet1.speak("GRRRRRRRRRRRR");
        pet2.speak("GRRRRRRRRRRRR");
        pet1.getCombat().attack(attacker);
        pet2.getCombat().attack(attacker);
    }

    @Override
    public void block(Mob attacker, Npc defender, Hit hit, CombatType combatType) {
        currentStrategy.block(attacker, defender, hit, combatType);
        defender.getCombat().attack(attacker);
    }

    @Override
    public void finishOutgoing(Npc attacker, Mob defender) {
        currentStrategy.finishOutgoing(attacker, defender);
        if (!NpcMeleeStrategy.get().withinDistance(attacker, defender)) {
            currentStrategy = MAGIC;
        } else if (RandomUtils.success(0.25)) {
            currentStrategy = EARTHQUAKE;
        } else {
            currentStrategy = NpcMeleeStrategy.get();
        }
    }

    @Override
    public int getAttackDelay(Npc attacker, Mob defender, FightType fightType) {
        return attacker.definition.getAttackDelay();
    }

    private static final class Earthquake extends NpcMeleeStrategy {
        private static final Animation ANIMATION = new Animation(9974, UpdatePriority.HIGH);

        @Override
        public void hit(Npc attacker, Mob defender, Hit hit) {
            RegionManager.forNearbyPlayer(defender, 11, player -> {
                player.send(new SendMessage("Vet'ion pummels the ground sending a shattering earthquake shockwave through you."));
                if (player.equals(defender)) {
                    hit.setDamage(25 + RandomUtils.inclusive(20));
                    return;
                }
                player.damage(new Hit(25 + RandomUtils.inclusive(20), HitIcon.MELEE));
            });
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

    private static final class Magic extends NpcMagicStrategy {
        private static final Animation ANIMATION = new Animation(9969, UpdatePriority.HIGH);

        public Magic() {
            super(CombatProjectile.getDefinition("Vet'ion"));
        }

        @Override
        public void start(Npc attacker, Mob defender, Hit[] hits) {
            Position[] bounds = Utility.getInnerBoundaries(defender.getPosition().transform(-1, -1), defender.width() + 2, defender.length() + 2);
            LinkedList<Position> positions = new LinkedList<>(Arrays.asList(bounds));
            for (int index = 0; index < 3; index++) {
                Position bound = index == 0 ? defender.getPosition() : RandomUtils.random(positions);
                positions.remove(bound);
                combatProjectile.getProjectile().ifPresent(projectile -> World.sendProjectile(attacker, bound, projectile));
                World.sendGraphic(new Graphic(775, 100), bound, attacker.instance);
                World.schedule(CombatUtil.getHitDelay(attacker, defender, CombatType.MAGIC) + 2, () -> RegionManager.forNearbyPlayer(bound, 0, player -> {
                    player.speak("OUCH!");
                    player.damage(new Hit(RandomUtils.inclusive(45), HitIcon.MAGIC));
                }));
            }
        }

        @Override
        public void hit(Npc attacker, Mob defender, Hit hit) {
            hit.setAccurate(false);
        }

        @Override
        public Animation getAttackAnimation(Npc attacker, Mob defender) {
            return ANIMATION;
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            return new CombatHit[]{nextMagicHit(attacker, defender)};
        }
    }

}
