package com.osroyale.game.world.entity.combat.strategy.npc.boss;

import com.osroyale.content.bloodmoney.BloodMoneyChest;
import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.Projectile;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.combat.CombatType;
import com.osroyale.game.world.entity.combat.CombatUtil;
import com.osroyale.game.world.entity.combat.attack.FightType;
import com.osroyale.game.world.entity.combat.hit.CombatHit;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.projectile.CombatProjectile;
import com.osroyale.game.world.entity.combat.strategy.CombatStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.MultiStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcMagicStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcMeleeStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcRangedStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.data.LockType;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.prayer.Prayer;
import com.osroyale.game.world.position.Position;
import com.osroyale.util.RandomUtils;
import com.osroyale.util.Utility;

import static com.osroyale.game.world.entity.combat.CombatUtil.createStrategyArray;
import static com.osroyale.game.world.entity.combat.CombatUtil.randomStrategy;

public class StoneGuardian extends MultiStrategy {

    private static final Integer[] GUARDIAN_IDS = {8064, 8065, 8066};
    private final static Transform TRANSFORM = new Transform();
    private final static NpcMeleeStrategy MELEE = NpcMeleeStrategy.get();
    private final static Magic MAGIC = new Magic();
    private final static Ranged RANGED = new Ranged();
    private static final BarrageAttack BARRAGE = new BarrageAttack();
    private final static PrayerAttack PRAYER = new PrayerAttack();
    private final static BoomAttack BOOM = new BoomAttack();

    private static int nextForm(int id) {
        return RandomUtils.randomExclude(GUARDIAN_IDS, id);
    }

    @Override
    public boolean canAttack(Npc attacker, Mob defender) {
        if (!attacker.attributes.has("STONE_GUARDIAN_ATTACK")) {
            attacker.attributes.put("STONE_GUARDIAN_ATTACK", 0);
        }

        if (!attacker.attributes.has("STONE_GUARDIAN_BLOCK")) {
            attacker.attributes.put("STONE_GUARDIAN_BLOCK", 0);
        }
        int attackCount = attacker.attributes.get("STONE_GUARDIAN_ATTACK", Integer.class);
        int blockCount = attacker.attributes.get("STONE_GUARDIAN_BLOCK", Integer.class);

        if (attackCount >= 10 || blockCount >= 10) {
            currentStrategy = TRANSFORM;
            return true;
        }

        switch (attacker.id) {
            case 8064: {
                CombatStrategy<Npc>[] SPECIALS = createStrategyArray(MELEE, PRAYER, BARRAGE, BOOM);
                currentStrategy = randomStrategy(SPECIALS);
                break;
            }
            case 8065: {
                CombatStrategy<Npc>[] SPECIALS = createStrategyArray(RANGED, PRAYER, BARRAGE, BOOM);
                currentStrategy = randomStrategy(SPECIALS);
                break;
            }
            case 8066: {
                CombatStrategy<Npc>[] SPECIALS = createStrategyArray(MAGIC, PRAYER, BARRAGE, BOOM);
                currentStrategy = randomStrategy(SPECIALS);
                break;
            }
        }

        return currentStrategy.canAttack(attacker, defender);
    }

    @Override
    public void preDeath(Mob attacker, Npc defender, Hit hit) {
        defender.speak("NOOOOOOOOOO!!!");
        super.preDeath(attacker, defender, hit);
        World.schedule(5, () -> {
            defender.unregister();
            BloodMoneyChest.guardian = null;
        });
    }

    @Override
    public void hitsplat(Npc attacker, Mob defender, Hit hit) {
        super.hitsplat(attacker, defender, hit);
        int count = attacker.attributes.get("STONE_GUARDIAN_ATTACK", Integer.class);
        attacker.attributes.put("STONE_GUARDIAN_ATTACK", count + 1);
    }

    @Override
    public void block(Mob attacker, Npc defender, Hit hit, CombatType combatType) {
        super.block(attacker, defender, hit, combatType);

        if (!attacker.attributes.has("STON_GUARDIAN_BLOCK")) {
            attacker.attributes.put("STON_GUARDIAN_BLOCK", 0);
            return;
        }

        int count = attacker.attributes.get("STON_GUARDIAN_BLOCK", Integer.class);
        attacker.attributes.put("STON_GUARDIAN_BLOCK", count + 1);

    }

    @Override
    public int modifyAccuracy(Npc attacker, Mob defender, int roll) {
        return roll + 10000;
    }

    @Override
    public int modifyDefensive(Mob attacker, Npc defender, int roll) {
        return roll + 500;
    }

    /** The transform strategy. */
    private static class Transform extends NpcMagicStrategy {
        Transform() {
            super(CombatProjectile.getDefinition("EMPTY"));
        }

        @Override
        public void hit(Npc attacker, Mob defender, Hit hit) {
        }

        @Override
        public void attack(Npc attacker, Mob defender, Hit hit) {
        }

        @Override
        public void start(Npc attacker, Mob defender, Hit[] hits) {
            attacker.speak("AH!!! Give me a minute here...");
            attacker.animate(new Animation(7924, UpdatePriority.VERY_HIGH));
            attacker.graphic(new Graphic(1237, UpdatePriority.VERY_HIGH));
            attacker.attributes.set("STONE_GUARDIAN_ATTACK", 0);
            attacker.attributes.set("STONE_GUARDIAN_BLOCK", 0);
            attacker.damageImmunity.reset(3_000);

            World.schedule(5, () -> {
                attacker.animate(new Animation(7927, UpdatePriority.VERY_HIGH));
                attacker.speak("That feels better!");
                attacker.transform(nextForm(attacker.id));
            });
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            CombatHit hit = nextMagicHit(attacker, defender, -1);
            hit.setAccurate(false);
            return new CombatHit[]{hit};
        }

        @Override
        public int getAttackDelay(Npc attacker, Mob defender, FightType fightType) {
            return 8;
        }
    }

    private static class BoomAttack extends NpcMagicStrategy {
        BoomAttack() {
            super(CombatProjectile.getDefinition("EMPTY"));
        }

        @Override
        public void hit(Npc attacker, Mob defender, Hit hit) {
        }

        @Override
        public void attack(Npc attacker, Mob defender, Hit hit) {
        }

        @Override
        public void start(Npc attacker, Mob defender, Hit[] hits) {
            attacker.speak("BOOM!");
            attacker.animate(new Animation(7923, UpdatePriority.VERY_HIGH));
            Position target = defender.getPosition();
            Projectile projectile = new Projectile(1242, 10, 80, 45, 31);
            projectile.send(attacker, target);

            World.schedule(3, () -> {
                for (Player player : attacker.getRegion().getPlayers(attacker.getHeight())) {
                    if (player != null && !player.isDead() && player.getPosition().equals(target)) {
                        int damage = player.prayer.isActive(Prayer.PROTECT_FROM_MELEE) ? 30 : 60;
                        player.writeDamage(new Hit(Utility.random(15, damage)));
                        player.graphic(new Graphic(659, false, UpdatePriority.VERY_HIGH));
                    }
                }
            });
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            CombatHit hit = nextMagicHit(attacker, defender, -1);
            hit.setAccurate(false);
            return new CombatHit[]{hit};
        }

        @Override
        public int getAttackDelay(Npc attacker, Mob defender, FightType fightType) {
            return 4;
        }
    }

    /** The barrage strategy. */
    private static class BarrageAttack extends NpcMagicStrategy {
        BarrageAttack() {
            super(CombatProjectile.getDefinition("EMPTY"));
        }

        @Override
        public void hit(Npc attacker, Mob defender, Hit hit) {
        }

        @Override
        public void attack(Npc attacker, Mob defender, Hit hit) {
        }

        @Override
        public void start(Npc attacker, Mob defender, Hit[] hits) {
            attacker.speak("GET READY FOR A CHILL!!");
            attacker.animate(new Animation(7923, UpdatePriority.VERY_HIGH));
            Projectile projectile = new Projectile(1456, 10, 65, 45, 31);

            CombatUtil.areaAction(defender, mob -> {
                projectile.send(attacker, defender);

                World.schedule(2, () -> {
                    if (mob.isPlayer() && !mob.locking.locked(LockType.FREEZE)) {
                        mob.getPlayer().message(true, "The stone guardian has frozen you!");
                        mob.graphic(new Graphic(369, false, UpdatePriority.VERY_HIGH));
                        mob.damage(nextMagicHit(attacker, defender, 30));
                        mob.locking.lock(25, LockType.FREEZE);
                        mob.graphic(new Graphic(1457, true, UpdatePriority.VERY_HIGH));
                    } else {
                        mob.writeDamage(new Hit(0));
                    }
                });
            });
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            CombatHit hit = nextMagicHit(attacker, defender, -1);
            hit.setAccurate(false);
            return new CombatHit[]{hit};
        }

        @Override
        public int getAttackDelay(Npc attacker, Mob defender, FightType fightType) {
            return 4;
        }
    }

    /** The prayer strategy. */
    private static class PrayerAttack extends NpcMagicStrategy {
        PrayerAttack() {
            super(CombatProjectile.getDefinition("EMPTY"));
        }

        @Override
        public void hit(Npc attacker, Mob defender, Hit hit) {
        }

        @Override
        public void attack(Npc attacker, Mob defender, Hit hit) {
        }

        @Override
        public void start(Npc attacker, Mob defender, Hit[] hits) {
            attacker.speak("YOUR GODS CAN'T SAVE YOU!");
            attacker.animate(new Animation(7923, UpdatePriority.VERY_HIGH));
            Projectile projectile = new Projectile(1471, 10, 65, 45, 31);

            CombatUtil.areaAction(defender, mob -> {
                projectile.send(attacker, defender);

                World.schedule(2, () -> {
                    if (mob.isPlayer() && mob.prayer.hasOverhead()) {
                        mob.prayer.deactivateOverhead();
                        mob.writeDamage(new Hit(Utility.random(15, 20)));
                        mob.getPlayer().message("The stone guardian has deactivated your overhead prayers!");
                        mob.graphic(new Graphic(1473, true, UpdatePriority.VERY_HIGH));
                    } else {
                        mob.writeDamage(new Hit(0));
                    }
                });

            });
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            CombatHit hit = nextMagicHit(attacker, defender, -1);
            hit.setAccurate(false);
            return new CombatHit[]{hit};
        }

        @Override
        public int getAttackDelay(Npc attacker, Mob defender, FightType fightType) {
            return 4;
        }
    }

    /** The magic strategy. */
    private static class Magic extends NpcMagicStrategy {
        public Magic() {
            super(CombatProjectile.getDefinition("Stone guardian magic"));
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            CombatHit hit = nextMagicHit(attacker, defender, 20);
            hit.setAccurate(true);
            return new CombatHit[]{hit};
        }
    }

    /** The ranged strategy. */
    private static class Ranged extends NpcRangedStrategy {
        public Ranged() {
            super(CombatProjectile.getDefinition("Stone guardian ranged"));
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            CombatHit hit = nextMagicHit(attacker, defender, 20);
            hit.setAccurate(true);
            return new CombatHit[]{hit};
        }
    }
}
