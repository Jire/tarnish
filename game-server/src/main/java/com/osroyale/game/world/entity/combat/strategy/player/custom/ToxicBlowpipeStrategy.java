package com.osroyale.game.world.entity.combat.strategy.player.custom;

import com.osroyale.game.Animation;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.world.entity.combat.CombatType;
import com.osroyale.game.world.entity.combat.attack.FightType;
import com.osroyale.game.world.entity.combat.effect.impl.CombatPoisonEffect;
import com.osroyale.game.world.entity.combat.hit.CombatHit;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.ranged.RangedAmmunition;
import com.osroyale.game.world.entity.combat.strategy.basic.RangedStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.containers.equipment.Equipment;
import com.osroyale.game.world.items.ground.GroundItem;
import com.osroyale.game.world.position.Area;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.RandomUtils;

public class ToxicBlowpipeStrategy extends RangedStrategy<Player> {

    private static final ToxicBlowpipeStrategy INSTANCE = new ToxicBlowpipeStrategy();

    @Override
    public boolean canAttack(Player attacker, Mob defender) {
        Item weapon = attacker.equipment.get(Equipment.WEAPON_SLOT);

        if (weapon == null) {
            attacker.getCombat().reset();
            return false;
        }

        Item ammo = attacker.blowpipeDarts;

        if (ammo == null) {
            return false;
        }

        RangedAmmunition ammu = RangedAmmunition.find(null, ammo);
        if (ammu == null || !ammu.isDart()) {
            attacker.message("Your blowpipe is not using darts for ammunition!");
            return false;
        }

        if (ammo.getAmount() >= 1) {
            if (attacker.blowpipeScales >= 3) {
                return true;
            }
            attacker.send(new SendMessage("Your blowpipe requires more scales!"));
        } else {
            attacker.send(new SendMessage("You need some ammunition to use this weapon!"));
        }

        attacker.getCombat().reset();
        return false;
    }

    @Override
    public void start(Player attacker, Mob defender, Hit[] hits) {
        if (attacker.isSpecialActivated()) {
            attacker.getCombatSpecial().drain(attacker);
        }

        if (attacker.getCombat().getDefender() == defender) {
            attacker.animate(getAttackAnimation(attacker, defender), true);
            attacker.rangedAmmo.sendProjectile(attacker, defender);

            if (!defender.isPlayer() || !PlayerRight.isIronman(attacker)) {
                addCombatExperience(attacker, hits);
            }
        }
    }

    @Override
    public void attack(Player attacker, Mob defender, Hit hit) {
        Item darts = attacker.blowpipeDarts;
        removeAmmunition(attacker, defender);

        if (hit.getDamage() > 1) {
            if (RandomUtils.success(0.25)) {
                defender.venom();
            } else {
                CombatPoisonEffect.getPoisonType(darts).ifPresent(defender::poison);
            }
        }
    }

    @Override
    public Animation getAttackAnimation(Player attacker, Mob defender) {
        int animation = attacker.getCombat().getFightType().getAnimation();
        return new Animation(animation, UpdatePriority.HIGH);
    }

    @Override
    public int getAttackDelay(Player attacker, Mob defender, FightType fightType) {
        int delay = attacker.getCombat().getFightType().getDelay();
        if (defender.isNpc())
            return delay - 1;
        return delay;
    }

    @Override
    public int getAttackDistance(Player attacker, FightType fightType) {
        return fightType.getDistance();
    }

    @Override
    public CombatHit[] getHits(Player attacker, Mob defender) {
        return new CombatHit[]{nextRangedHit(attacker, defender)};
    }

    @Override
    public CombatType getCombatType() {
        return CombatType.RANGED;
    }

    private void removeAmmunition(Player attacker, Mob defender) {
        Item darts = attacker.blowpipeDarts;
        attacker.blowpipeScales -= 1.5f;
        if (RandomUtils.success(0.80)) {
            if (Equipment.hasAssembler(attacker)) {//ok maybe that will work lol
                return;
            }

            else if (Equipment.hasAccumulator(attacker) && RandomUtils.success(0.90)) {
                return;
            }

            if (Equipment.hasAttractor(attacker) && RandomUtils.success(0.80)) {
                return;
            }

            Position dropPoisition = defender.getPosition();

            if (Area.inKraken(attacker) || Area.inZulrah(attacker)) {
                dropPoisition = attacker.getPosition();
            }
            darts.decrementAmount();
            GroundItem.create(attacker, darts.createWithAmount(1), dropPoisition);
        }

        if (darts.getAmount() == 0) {
            attacker.send(new SendMessage("That was the last of your ammunition!"));
            attacker.blowpipeDarts = null;
        }

        if (attacker.blowpipeScales == 0) {
            attacker.send(new SendMessage("Your blowpipe has run out of charges!"));
        }
    }

    public static ToxicBlowpipeStrategy get() {
        return INSTANCE;
    }

}
