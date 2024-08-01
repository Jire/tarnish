package com.osroyale.game.world.entity.combat;

import com.osroyale.content.activity.Activity;
import com.osroyale.content.activity.ActivityType;
import com.osroyale.content.lms.LMSGame;
import com.osroyale.content.skill.impl.slayer.SlayerTask;
import com.osroyale.game.Animation;
import com.osroyale.game.Projectile;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.combat.effect.AntifireDetails;
import com.osroyale.game.world.entity.combat.effect.CombatEffect;
import com.osroyale.game.world.entity.combat.effect.CombatEffectType;
import com.osroyale.game.world.entity.combat.hit.CombatHit;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.hit.HitIcon;
import com.osroyale.game.world.entity.combat.hit.Hitsplat;
import com.osroyale.game.world.entity.combat.projectile.CombatProjectile;
import com.osroyale.game.world.entity.combat.strategy.CombatStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.data.PacketType;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.prayer.Prayer;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.containers.equipment.Equipment;
import com.osroyale.game.world.position.Area;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.RandomUtils;
import com.osroyale.util.Utility;

import java.util.List;
import java.util.function.Consumer;

/**
 * A collection of util methods and constants related to combat.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class CombatUtil {

    /**
     * The default constructor.
     *
     * @throws UnsupportedOperationException if this class is instantiated.
     */
    private CombatUtil() {
        throw new UnsupportedOperationException("This class cannot be instantiated!");
    }

    /**
     * Executes an action for mobs within a 3x3 square, including the source
     * {@code mob}.
     *
     * @param mob    the mob to generate an area for
     * @param action the action to apply to all mobs in the area
     */
    public static void areaAction(Mob mob, Consumer<Mob> action) {
        action.accept(mob);
        areaAction(mob, 3 * 3, 1, action);
    }

    /**
     * Sends an action to {@link Mob} instance which is within a {@code
     * distance}.
     *
     * @param action action consumer.
     */
    public static void areaAction(Mob mob, int max, int distance, Consumer<Mob> action) {
        if (!Area.inMulti(mob)) {
            return;
        }

        int added = 0;
        List<Player> players = World.getRegions().getLocalPlayers(mob);
        players.sort((first, second) -> {
            int firstD = Utility.getDistance(first, mob);
            int secondD = Utility.getDistance(second, mob);
            return firstD - secondD;
        });

        for (Player other : players) {
            if (other == null) continue;
            if (other.instance != mob.instance) continue;
            if (!Utility.withinViewingDistance(other, mob, distance)) continue;
            if (other.equals(mob)) continue;
            if (other.getCurrentHealth() <= 0 || other.isDead()) continue;
            if (!Area.inMulti(other)) continue;
            if (mob.isPlayer() && other.isPlayer() && (!Area.inWilderness(mob) || !Area.inWilderness(other)))
                continue;
            action.accept(other);
            if (++added == max) return;
        }

        List<Npc> npcs = World.getRegions().getLocalNpcs(mob);
        npcs.sort((first, second) -> {
            int firstD = Utility.getDistance(first, mob);
            int secondD = Utility.getDistance(second, mob);
            return firstD - secondD;
        });

        for (Npc other : npcs) {
            if (other == null) continue;
            if (other.instance != mob.instance) continue;
            if (!Utility.withinViewingDistance(other, mob, distance)) continue;
            if (other.equals(mob)) continue;
            if (other.getCurrentHealth() <= 0 || other.isDead()) continue;
            if (!Area.inMulti(other)) continue;
            if (!other.definition.isAttackable()) continue;
            action.accept(other);
            if (++added == max) return;
        }
    }

    /**
     * Gets the hit delay for the specified {@code type}.
     *
     * @param attacker the character doing the hit
     * @param defender the victim being hit
     * @param type     the combat type of this hit
     * @return the delay for the combat type
     */
    public static int getHitDelay(final Mob attacker, final Mob defender, final CombatType type) {
        if (CombatType.MELEE.equals(type)) return 0;

        final CombatStrategy<? super Mob> strategy = attacker.getStrategy();
        if (strategy != null) {
            final CombatProjectile combatProjectile = strategy.getCombatProjectile();
            if (combatProjectile != null) {
                final int serverTicks = combatProjectile.getServerTicks();
                if (serverTicks > 0) {
                    return serverTicks;
                }
            }
        }
        return getOldHitDelay(attacker, defender, type);
    }

    private static int getOldHitDelay(final Mob attacker, final Mob defender, final CombatType type) {
        final int distance = Math.min(10, Utility.getDistance(attacker, defender));
        return switch (type) {
            default -> 0;
            case RANGED -> Projectile.RANGED_DELAYS[distance] - 1;
            case MAGIC -> Projectile.MAGIC_DELAYS[distance] - 1;
        };
    }

    /**
     * Gets the hitsplat delay for the specified {@code type}.
     *
     * @param type the combat type of this hit
     * @return the delay for the combat type
     */
    public static int getHitsplatDelay(CombatType type) {
        return type.getHitsplatDelay();
    }

    static boolean validateMobs(Mob attacker, Mob defender) {
        if (!validate(attacker) || !validate(defender)) {
            attacker.getCombat().reset();
            return false;
        }

        if (attacker.instance != defender.instance) {
            attacker.getCombat().reset();
            return false;
        }

        if (!canAttack(attacker, defender)) {
            attacker.getCombat().reset();
            return false;
        }
        return true;
    }

    static boolean validate(Mob attacker, Mob defender) {
        if (!validate(attacker) || !validate(defender)) {
            attacker.getCombat().reset();
            return false;
        }

        if (attacker.instance != defender.instance) {
            attacker.getCombat().reset();
            return false;
        }

        if (!canBasicAttack(attacker, defender)) {
            attacker.getCombat().reset();
            return false;
        }
        return true;
    }

    /**
     * Applies the {@code effect} in any context.
     *
     * @param effect the effect that must be applied
     * @return {@code true} if it was successfully applied
     */
    public static boolean effect(Mob mob, CombatEffectType effect) {
        return CombatEffect.EFFECTS.get(effect).start(mob);
    }

    /**
     * Cancels the {@code effect} in any context.
     *
     * @param effect the effect that must be applied
     * @return {@code true} if it was successfully applied
     */
    public static boolean cancelEffect(Mob mob, CombatEffectType effect) {
        return CombatEffect.EFFECTS.get(effect).removeOn(mob);
    }

    public static boolean canAttack(Mob attacker, Mob defender) {
        if (attacker.isPlayer())
            return canAttack(attacker.getPlayer(), defender);
        return canAttack(attacker.getNpc(), defender);
    }

    private static boolean canAttack(Player attacker, Mob defender) {


        if (defender.isNpc() && !SlayerTask.canAttack(attacker, defender.getName())) {
            attacker.send(new SendMessage("You do not meet the slayer requirements to attack this npc!"));
            return false;
        }
        if (attacker.equals(defender)) {
            //attacker.send(new SendMessage("You can't attack yourself!"));
            return false;
        }
        if (defender.isNpc() && defender.getNpc().owner != null && !attacker.equals(defender.getNpc().owner)) {
            attacker.send(new SendMessage("You can't attack this npc!"));
            return false;
        }

        if (attacker.getCombat().isUnderAttack() && !attacker.getCombat().isUnderAttackBy(defender)) {
            if (!Area.inMulti(attacker) || !Area.inMulti(defender)) {
                attacker.send(new SendMessage("You are already under attack!"));
                return false;
            }
        }

        if (defender.getCombat().isUnderAttack() && !defender.getCombat().isUnderAttackBy(attacker)) {
            if (!Area.inMulti(attacker) || !Area.inMulti(defender)) {
                if (defender.isPlayer()) {
                    attacker.send(new SendMessage(defender.getName() + " is currently in combat and can not be attacked."));
                } else {
                    attacker.send(new SendMessage("This monster is already under attack!"));
                }
                return false;
            }
        }
        if (defender.isPlayer()) {
            if (attacker.locking.locked(PacketType.COMBAT)) {
                return false;
            }

            if (LMSGame.inGameArea(attacker) && LMSGame.canAttack(attacker, defender.getPlayer())) {
                return true;
            }

            if (Activity.evaluate(attacker, activity -> defender.activity == activity) && attacker.inActivity(ActivityType.DUEL_ARENA)) {
                return true;
            }

/*
            if (Area.inEventArena(attacker) && Area.inEventArena(defender)) {
                return true;
            }
*/

            int difference = Math.abs(attacker.skills.getCombatLevel() - defender.skills.getCombatLevel());

            if (attacker.pvpInstance) {
                if (!defender.getPlayer().pvpInstance) {
                    attacker.message(defender.getName() + " is not in a PvP instance.");
                    return false;
                }
                if (!Area.inPvP(attacker) && !attacker.hasPvPTimer) {
                    attacker.message("You must be in a PvP zone to attack " + defender.getName() + "!");
                    return false;
                }

                if (!Area.inPvP(defender) && !defender.getPlayer().hasPvPTimer) {
                    attacker.message(defender.getName() + " must be in a PvP zone for you to attack!");
                    return false;
                }

                if (difference > 10) {
                    if (!Area.inDuelObsticleArena(attacker)) {
                        attacker.message("Your combat level difference is too great!");
                        return false;
                    }
                }
                return true;
            }

            if (difference > attacker.wilderness) {
                if (!Area.inDuelObsticleArena(attacker)) {
                    attacker.send(new SendMessage("Your combat level difference is too great!"));
                    return false;
                }
            }

            if (!Area.inWilderness(attacker)) {
                attacker.send(new SendMessage("You need to be in the wilderness to attack " + Utility.formatName(defender.getName()) + "."));
                return false;
            }

            if (!Area.inWilderness(defender)) {
                attacker.send(new SendMessage(Utility.formatName(defender.getName()) + " must be in the wilderness for you to attack."));
                return false;
            }
        }
        return true;
    }

    private static boolean canAttack(Npc attacker, Mob defender) {
        if (attacker.equals(defender)) {
            return false;
        }
        if (defender.isNpc() && defender.getNpc().owner != null && !defender.getNpc().owner.equals(attacker)) {
            return false;
        }
        if (attacker.getCombat().isUnderAttack() && !attacker.getCombat().isUnderAttackBy(defender)) {
            if (!Area.inMulti(attacker) || !Area.inMulti(defender)) {
                return false;
            }
        }
        if (defender.getCombat().isUnderAttack() && !defender.getCombat().isUnderAttackBy(attacker)) {
            return Area.inMulti(attacker) && Area.inMulti(defender);
        }
        return true;
    }

    public static boolean canBasicAttack(Mob attacker, Mob defender) {
        if (attacker.equals(defender)) {
            return false;
        }
        if (defender.isNpc() && defender.getNpc().owner != null && !attacker.equals(defender.getNpc().owner)) {
            return false;
        }
        if (Activity.evaluate(attacker, activity -> defender.activity == activity) && attacker.inActivity(ActivityType.DUEL_ARENA)) {
            return true;
        }
        if (attacker.getCombat().isUnderAttack() && !attacker.getCombat().isUnderAttackBy(defender)) {
            if (!Area.inMulti(attacker) || !Area.inMulti(defender)) {
                return false;
            }
        }
        if (defender.getCombat().isUnderAttack() && !defender.getCombat().isUnderAttackBy(attacker)) {
            if (!Area.inMulti(attacker) || !Area.inMulti(defender)) {
                return false;
            }
        }
        if (attacker.isPlayer() && defender.isPlayer()) {
            int difference = Math.abs(attacker.skills.getCombatLevel() - defender.skills.getCombatLevel());

            if (attacker.getPlayer().pvpInstance || defender.getPlayer().pvpInstance) {
                if (!defender.getPlayer().pvpInstance) {
                    return false;
                }
                if (!attacker.getPlayer().pvpInstance) {
                    return false;
                }
                if (!Area.inPvP(attacker)) {
                    return false;
                }
                if (!Area.inPvP(defender)) {
                    return false;
                }
                return difference <= 10;
            }


            if (difference > attacker.getPlayer().wilderness) {
                return false;
            }
            if (!Area.inWilderness(attacker)) {
                return false;
            }
            return Area.inWilderness(defender);
        }
        return true;
    }

    private static boolean validate(Mob mob) {
        return mob != null && !mob.isDead() && mob.isVisible() && mob.isValid() && !mob.teleporting && !mob.inTeleport;
    }

    public static Animation getBlockAnimation(Mob mob) {
        int animation = 404;
        if (mob.isPlayer()) {
            final var shieldOverride = mob.getPlayer().overrides.hasOverride(Equipment.SHIELD_SLOT);
            if (mob.getPlayer().equipment.hasShield() || shieldOverride) {
                Item shield = shieldOverride ? mob.getPlayer().overrides.get(Equipment.SHIELD_SLOT) : mob.getPlayer().equipment.getShield();
                animation = shield.getBlockAnimation().orElse(424);
            } else if (mob.isPlayer()) {
                final var weaponOverride = mob.getPlayer().overrides.hasOverride(Equipment.WEAPON_SLOT);
                if (mob.getPlayer().equipment.hasWeapon() || weaponOverride) {
                    Item weapon = weaponOverride ? mob.getPlayer().overrides.get(Equipment.WEAPON_SLOT) : mob.getPlayer().equipment.getWeapon();
                    animation = weapon.getBlockAnimation().orElse(424);
                } else {
                    animation = 404;//TODO
                }
            }
        } else {
            Npc npc = mob.getNpc();
            animation = npc.definition.getBlockAnimation();
        }
        int delay = (int) mob.getCombat().lastBlocked.elapsedTime();
        if (delay < 600)
            return new Animation(animation, delay / 50, UpdatePriority.LOW);
        return new Animation(animation, UpdatePriority.LOW);
    }

    public static CombatHit generateDragonfire(Mob attacker, Mob defender, int max, boolean prayer) {
        int hitDelay = getHitDelay(attacker, defender, CombatType.MAGIC);
        int hitsplatDelay = 1;
        return generateDragonfire(attacker, defender, max, hitDelay, hitsplatDelay, prayer);
    }

    public static CombatHit generateDragonfire(Mob attacker, Mob defender, int max, int hitDelay, int hitsplatDelay, boolean prayer) {
        int damage;

        if (defender.isPlayer()) {
            Player player = defender.getPlayer();

            if (Equipment.isWearingDFS(player) && player.dragonfireCharges < 50) {
                if (player.equipment.getShield().getId() == 11284) {
                    player.equipment.set(Equipment.SHIELD_SLOT, new Item(11283), true);
                    player.equipment.refresh();
                }
                player.animate(6695);
                player.graphic(1164);
                player.dragonfireCharges++;
                player.getCombat().setCooldown(5);
                player.send(new SendMessage("Your dragonfire Shield Absorbs the Dragon breath."));
                player.face(attacker);
                damage = 0;
            } else {
                prayer &= player.prayer.isActive(Prayer.PROTECT_FROM_MAGIC);
                boolean shield = player.equipment.containsAny(1540, 11283, 21633);
                boolean potion = player.getAntifireDetails().isPresent();

                if (shield && potion) {
                    max -= 65;
                } else if (potion) {
                    AntifireDetails.AntifireType type = player.getAntifireDetails().get().getType();
                    max -= type.getReduction();
                    if (max <= 0) {
                        max = 0;
                    }
                } else if (shield) {
                    max -= 50;
                } else if (prayer) {
                    max -= 45;
                }

                if (max > 0) {
                    damage = RandomUtils.inclusive(max);
                } else {
                    damage = 0;
                }

                if (damage >= 15) {
                    player.send(new SendMessage("You are horribly burned by the dragonfire!", true));
                } else if (!shield && !potion && !prayer && damage < 9 && damage > 0) {
                    player.send(new SendMessage("You manage to resist some of the dragonfire.", true));
                }
            }
        } else {
            damage = max == 0 ? 0 : RandomUtils.inclusive(max);
        }

        Hit hit = new Hit(damage, Hitsplat.NORMAL, HitIcon.NONE, true);
        return new CombatHit(hit, hitDelay, hitsplatDelay);
    }

    public static CombatStrategy<Npc> randomStrategy(CombatStrategy<Npc>[] strategies) {
        return RandomUtils.random(strategies);
    }

    @SafeVarargs
    public static CombatStrategy<Npc>[] createStrategyArray(CombatStrategy<Npc>... strategies) {
        return strategies;
    }

    @SafeVarargs
    public static CombatStrategy<Npc>[] createStrategyArray(CombatStrategy<Npc>[] strategies,
                                                            CombatStrategy<Npc>... moreStrategies) {
        final CombatStrategy<Npc>[] array = new CombatStrategy[strategies.length + moreStrategies.length];
        System.arraycopy(strategies, 0, array, 0, strategies.length);
        System.arraycopy(moreStrategies, 0, array, strategies.length, moreStrategies.length);
        return array;
    }

}
