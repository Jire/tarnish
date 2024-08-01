package com.osroyale.game.world.entity.combat.strategy.player;

import com.osroyale.content.itemaction.impl.ThammaronsSceptre;
import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.world.entity.combat.CombatImpact;
import com.osroyale.game.world.entity.combat.CombatType;
import com.osroyale.game.world.entity.combat.attack.FightType;
import com.osroyale.game.world.entity.combat.hit.CombatHit;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.magic.CombatSpell;
import com.osroyale.game.world.entity.combat.magic.MagicRune;
import com.osroyale.game.world.entity.combat.strategy.basic.MagicStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.containers.equipment.Equipment;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.RandomUtils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static com.osroyale.game.world.items.containers.equipment.Equipment.WEAPON_SLOT;

public class PlayerMagicStrategy extends MagicStrategy<Player> {

    /** The magic spell definition. */
    private final CombatSpell spell;

    /** Constructs a new {@code SpellStrategy} from a {@link CombatSpell}. */
    public PlayerMagicStrategy(CombatSpell spell) {
        this.spell = spell;
    }

    @Override
    public boolean canAttack(Player attacker, Mob defender) {
        if (defender.isPlayer() && defender.getPlayer().isBot && !PlayerRight.isOwner(defender.getPlayer())) {
            attacker.message("You can't attack bots with magic.");
            return false;
        }

        if (spell == CombatSpell.TELE_BLOCK) {
            if (defender.isPlayer()) {
                if (defender.getPlayer().isTeleblocked()) {
                    attacker.message("This player is already affected by this spell!");
                    return false;
                }
            } else if (defender.isNpc()) {
                attacker.message("You can't teleblock a npc!");
                return false;
            }
        }

        if (spell == CombatSpell.CRUMBLE_UNDEAD) {
            if (defender.isPlayer()) {
                attacker.message("You can not use the crumble undead spell on a player!");
                return false;
            }

            if (defender.isNpc()) {
                String name = defender.getName().toLowerCase();

                if (!(name.contains("zombified") || name.contains("undead") || name.contains("zombie") || name.contains("skeleton") || name.contains("ghost") || name.contains("shade"))) {
                    attacker.message("This spell only affects skeletons, zombies, ghosts and shades");
                    return false;
                }
            }
        }

        if (/*PlayerRight.isDeveloper(attacker) ||*/ spell.canCast(attacker, defender)) {
            return true;
        }

        attacker.send(new SendMessage("You need some runes to cast this spell."));
        attacker.getCombat().reset();
        return false;
    }

    @Override
    public void start(Player attacker, Mob defender, Hit[] hits) {
        if (attacker.getCombat().getDefender() == defender) {
            attacker.animate(getAttackAnimation(attacker, defender), true);
            spell.getStart().ifPresent(attacker::graphic);

            int projectileDuration = spell.sendProjectile(attacker, defender);
            if (projectileDuration == 0) {
                final int distance = attacker.getPosition().getChebyshevDistance(defender.getPosition());
                final int duration = distance * 10;
                projectileDuration = 51 + duration;
            }
            // so this is special just for ice barrage/burst, the projectile does this.
            final Graphic endGraphic = getEndGraphic(spell.getEnd(), missed(hits), SPLASH, projectileDuration);
            if (endGraphic != null) {
                defender.graphic(endGraphic);
            }

            if (spell.getEffect().isPresent()) {
                List<Hit> extra = new LinkedList<>();
                for (Hit hit : hits) {
                    Predicate<CombatImpact> filter = effect -> effect.canAffect(attacker, defender, hit);
                    Consumer<CombatImpact> execute = effect -> effect.impact(attacker, defender, hit, extra);
                    spell.getEffect().filter(filter).ifPresent(execute);
                }

                if (!defender.isPlayer() || !PlayerRight.isIronman(attacker)) {
                    if (extra.isEmpty()) {
                        Collections.addAll(extra, hits);
                        addCombatExperience(attacker, spell.getBaseExperience(), extra.toArray(new Hit[0]));
                    } else {
                        addCombatExperience(attacker, spell.getBaseExperience(), hits);
                    }
                } else {
                    attacker.skills.addExperience(Skill.MAGIC, spell.getBaseExperience());
                }
            } else if (!defender.isPlayer() || !PlayerRight.isIronman(attacker)) {
                addCombatExperience(attacker, spell.getBaseExperience(), hits);
            } else {
                attacker.skills.addExperience(Skill.MAGIC, spell.getBaseExperience());
            }

            if ((!attacker.equipment.containsAny(11791, 12904) || !RandomUtils.success(0.125))) {
                MagicRune.remove(attacker, spell.getRunes());
            }

            if (attacker.isSingleCast()) {
                attacker.setSingleCast(null);
                attacker.getCombat().reset();
                attacker.interact(defender);
            }

            Item weapon = attacker.equipment.get(Equipment.WEAPON_SLOT);
            if(weapon != null) {
                switch(weapon.getId()) {
                    case ThammaronsSceptre.THAMMARONS_SCEPTRE_CHARGED_ID -> {
                        attacker.thammoranSceptreCharges--;
                    }
                }
            }
        }
    }

    @Override
    public void hit(Player attacker, Mob defender, Hit hit) {
        if (!attacker.isSingleCast() && !attacker.isAutocast()) {
            attacker.resetFace();
        }

        if (hit.isAccurate()) {
            if (attacker.equipment.retrieve(WEAPON_SLOT).filter(item -> item.getId() == 12904).isPresent() && RandomUtils.success(0.25)) {
                defender.venom();
            }
        }
    }

    @Override
    public void hitsplat(Player attacker, Mob defender, Hit hit) {
    }

    @Override
    public CombatHit[] getHits(Player attacker, Mob defender) {
        CombatHit combatHit = nextMagicHit(attacker, defender, spell.getCombatProjectile());

        if (spell == CombatSpell.CRUMBLE_UNDEAD && defender.getName().equalsIgnoreCase("zombified spawn")) {
            combatHit.setDamage(defender.getCurrentHealth());
        }

        return new CombatHit[]{combatHit};
    }

    @Override
    public int getAttackDelay(Player attacker, Mob defender, FightType fightType) {
        return 5;
    }

    @Override
    public int getAttackDistance(Player attacker, FightType fightType) {
        return 10;
    }

    @Override
    public Animation getAttackAnimation(Player attacker, Mob defender) {
        if (spell.getAnimation().isPresent()) {
            return spell.getAnimation().get();
        }

        FightType fightType = attacker.getCombat().getFightType();
        int animation = fightType.getAnimation();

        if (attacker.equipment.hasShield()) {
            Item weapon = attacker.equipment.getShield();
            animation = weapon.getAttackAnimation(fightType).orElse(animation);
        }

        if (attacker.equipment.hasWeapon()) {
            Item weapon = attacker.equipment.getWeapon();
            animation = weapon.getAttackAnimation(fightType).orElse(animation);
        }

        return new Animation(animation, UpdatePriority.HIGH);
    }

    @Override
    public CombatType getCombatType() {
        return CombatType.MAGIC;
    }

    public CombatSpell getSpell() {
        return spell;
    }
}