package com.osroyale.game.world.entity.combat.strategy.player;

import com.osroyale.content.itemaction.impl.CrawsBow;
import com.osroyale.content.prestige.PrestigePerk;
import com.osroyale.game.Animation;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.world.entity.combat.CombatImpact;
import com.osroyale.game.world.entity.combat.CombatType;
import com.osroyale.game.world.entity.combat.attack.FightType;
import com.osroyale.game.world.entity.combat.effect.impl.CombatPoisonEffect;
import com.osroyale.game.world.entity.combat.hit.CombatHit;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.ranged.RangedAmmunition;
import com.osroyale.game.world.entity.combat.ranged.RangedWeaponType;
import com.osroyale.game.world.entity.combat.strategy.basic.RangedStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.UpdateFlag;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.containers.equipment.Equipment;
import com.osroyale.game.world.items.ground.GroundItem;
import com.osroyale.game.world.position.Area;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.RandomUtils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class PlayerRangedStrategy extends RangedStrategy<Player> {

    private static final PlayerRangedStrategy INSTANCE = new PlayerRangedStrategy();

    protected PlayerRangedStrategy() {
    }

    @Override
    public boolean canAttack(Player attacker, Mob defender) {
        Item weapon = attacker.equipment.get(Equipment.WEAPON_SLOT);

        if (weapon == null) {
            attacker.getCombat().reset();
            return false;
        }

        if(weapon.getId() == CrawsBow.CRAWS_CHARGED_ID && attacker.crawsBowCharges < 1) {
            attacker.send(new SendMessage("Your Craw's bow is out of charges!"));
            attacker.getCombat().reset();
            return false;
        }

        if (attacker.rangedDefinition == null) {
            return false;
        }

        int bowsWithNoArrowsRequired [] = {22550, 25865, 25867, 25884, 25886, 25890, 25892, 25894, 25896, 25888};
        Item ammo = attacker.equipment.get(attacker.rangedDefinition.getSlot());
        if (ammo != null && attacker.rangedAmmo != null && ammo.getAmount() >= attacker.rangedAmmo.getRemoval()) {
            if (attacker.rangedDefinition.isValid(attacker.rangedAmmo)) {
                return true;
            }
            attacker.send(new SendMessage("You can't use this ammunition with this weapon."));
        } else if
        (attacker.equipment.contains(bowsWithNoArrowsRequired)) {
            return true;
        } else {
            attacker.send(new SendMessage("You need some ammunition to use this weapon!"));
        }
        attacker.getCombat().reset();
        return false;
    }

    protected void sendStuff(Player attacker, Mob defender) {
        int id = attacker.equipment.get(attacker.rangedDefinition.getSlot()).getId();
        Animation animation = attacker.rangedAmmo.getAnimation(id).orElse(getAttackAnimation(attacker, defender));
        attacker.animate(animation, true);
        attacker.rangedAmmo.getStart(id).ifPresent(attacker::graphic);
        attacker.rangedAmmo.sendProjectile(attacker, defender);
    }

    @Override
    public void start(Player attacker, Mob defender, Hit[] hits) {
        if (attacker.isSpecialActivated()) {
            attacker.getCombatSpecial().drain(attacker);
        }

        if (attacker.getCombat().getDefender() == defender) {
            sendStuff(attacker, defender);

            int id = attacker.equipment.get(attacker.rangedDefinition.getSlot()).getId();
            if (attacker.rangedAmmo.getEffect(id).isPresent()) {
                List<Hit> extra = new LinkedList<>();
                for (Hit hit : hits) {
                    Predicate<CombatImpact> filter = effect -> effect.canAffect(attacker, defender, hit);
                    Consumer<CombatImpact> execute = effect -> effect.impact(attacker, defender, hit, extra);
                    attacker.rangedAmmo.getEffect(id).filter(filter).ifPresent(execute);
                }
                if (!defender.isPlayer() || !PlayerRight.isIronman(attacker)) {
                    if (extra.isEmpty()) {
                        Collections.addAll(extra, hits);
                        addCombatExperience(attacker, extra.toArray(new Hit[0]));
                    } else {
                        addCombatExperience(attacker, hits);
                    }
                }
            } else if (!defender.isPlayer() || !PlayerRight.isIronman(attacker)) {
                addCombatExperience(attacker, hits);
            }
        }
    }

    @Override
    public void attack(Player attacker, Mob defender, Hit hit) {
        removeAmmunition(attacker, defender, attacker.rangedDefinition.getType());
        if (hit.getDamage() > 1 && attacker.rangedDefinition != null) {
            Item item = attacker.equipment.get(attacker.rangedDefinition.getSlot());
            CombatPoisonEffect.getPoisonType(item).ifPresent(defender::poison);
        }

        Item weapon = attacker.equipment.get(Equipment.WEAPON_SLOT);
        if(weapon != null) {
            switch(weapon.getId()) {
                case CrawsBow.CRAWS_CHARGED_ID -> {
                    attacker.crawsBowCharges--;
                }
            }
        }
    }

    @Override
    public void hit(Player attacker, Mob defender, Hit hit) {
        if (attacker.rangedAmmo != null && attacker.rangedDefinition != null) {
            int id = attacker.equipment.retrieve(attacker.rangedDefinition.getSlot()).map(Item::getId).orElse(-1);
            attacker.rangedAmmo.getEnd(id).ifPresent(defender::graphic);
        }
    }

    @Override
    public Animation getAttackAnimation(Player attacker, Mob defender) {
        int animation = attacker.getCombat().getFightType().getAnimation();

        if (attacker.equipment.hasShield()) {
            Item weapon = attacker.equipment.getShield();
            FightType fightType = attacker.getCombat().getFightType();
            animation = weapon.getAttackAnimation(fightType).orElse(animation);
        }

        if (attacker.equipment.hasWeapon()) {
            Item weapon = attacker.equipment.getWeapon();
            FightType fightType = attacker.getCombat().getFightType();
            animation = weapon.getAttackAnimation(fightType).orElse(animation);
        }

        if (attacker.overrides.hasOverride(Equipment.WEAPON_SLOT)) {
            final var item = attacker.overrides.get(Equipment.WEAPON_SLOT);
            animation = attacker.overrides.getFightType(item).getAnimation();
        }

        return new Animation(animation, UpdatePriority.HIGH);
    }

    @Override
    public int getAttackDelay(Player attacker, Mob defender, FightType fightType) {
        return attacker.getCombat().getFightType().getDelay();
    }

    @Override
    public int getAttackDistance(Player attacker, FightType fightType) {
        return fightType.getDistance();
    }

    @Override
    public CombatHit[] getHits(Player attacker, Mob defender) {
        RangedAmmunition ammo = attacker.rangedAmmo;
        CombatHit[] hits = new CombatHit[ammo.getRemoval()];
        for (int index = 0; index < hits.length; index++) {
            hits[index] = nextRangedHit(attacker, defender);
        }
        return hits;
    }

    @Override
    public CombatType getCombatType() {
        return CombatType.RANGED;
    }

    private void removeAmmunition(Player attacker, Mob defender, RangedWeaponType type) {
        Item next = attacker.equipment.get(type.getSlot());
        if (next == null) return;

        if (attacker.rangedAmmo.isDroppable()) {
            Item dropItem = new Item(next.getId());

            boolean canBreak = !dropItem.getName().contains("arrow") || !attacker.prestige.hasPerk(PrestigePerk.ARROWHEAD);

            if (!canBreak || RandomUtils.success(0.80)) {
                if (Equipment.hasAssembler(attacker) /* AKA 100% */) {
                    return;
                }

                if (Equipment.hasAccumulator(attacker) && RandomUtils.success(0.90)) {
                    return;
                }

                if (Equipment.hasAttractor(attacker) && RandomUtils.success(0.80)) {
                    return;
                }

                Position dropPoisition = defender.getPosition();

                if (Area.inKraken(attacker) || Area.inZulrah(attacker)) {
                    dropPoisition = attacker.getPosition();
                }

                GroundItem.create(attacker, dropItem, dropPoisition);
            }
        }
        final int bowsWithNoArrowsRequired [] = {22550, 25865, 25867, 25884, 25886, 25890, 25892, 25894, 25896, 25888};
        if (attacker.equipment.contains(bowsWithNoArrowsRequired)) {
            return;
        } else {
            next.decrementAmount();
        }


        if (next.getAmount() == 0 && !attacker.equipment.contains(bowsWithNoArrowsRequired)) {
            attacker.send(new SendMessage("That was the last of your ammunition!"));
            next = null;
        }

        attacker.equipment.set(type.getSlot(), next, false);
        attacker.equipment.refresh();
        attacker.updateFlags.add(UpdateFlag.APPEARANCE);
    }

    public static PlayerRangedStrategy get() {
        return INSTANCE;
    }

}
