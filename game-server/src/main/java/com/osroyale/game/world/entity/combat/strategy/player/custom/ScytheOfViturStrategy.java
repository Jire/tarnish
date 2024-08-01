package com.osroyale.game.world.entity.combat.strategy.player.custom;

import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.world.entity.combat.CombatType;
import com.osroyale.game.world.entity.combat.CombatUtil;
import com.osroyale.game.world.entity.combat.attack.FightType;
import com.osroyale.game.world.entity.combat.hit.CombatHit;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.strategy.basic.MeleeStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.containers.equipment.Equipment;
import com.osroyale.game.world.position.Position;

public class ScytheOfViturStrategy extends MeleeStrategy<Player> {

    private static final ScytheOfViturStrategy INSTANCE = new ScytheOfViturStrategy();

    private static final Animation ANIMATION = new Animation(8056, UpdatePriority.HIGH);
    private static final Graphic GRAPHIC = new Graphic(506);

    @Override
    public boolean canAttack(Player attacker, Mob defender) {
        Item weapon = attacker.equipment.get(Equipment.WEAPON_SLOT);

        if (weapon == null) {
            attacker.getCombat().reset();
            return false;
        }

        return true;
    }

    @Override
    public void start(Player attacker, Mob defender, Hit[] hits) {
        super.start(attacker, defender, hits);
        attacker.animate(ANIMATION, true);

        CombatUtil.areaAction(attacker, 3, 1, other -> hitEvent(attacker, defender, other));
        //attacker.graphic(GRAPHIC);
    }

    @Override
    public void attack(Player attacker, Mob defender, Hit hit) { }

    @Override
    public Animation getAttackAnimation(Player attacker, Mob defender) {
        return ANIMATION;
    }

    @Override
    public CombatType getCombatType() {
        return CombatType.MELEE;
    }

    @Override
    public int getAttackDistance(Player attacker, FightType fightType) {
        return 1;
    }

    @Override
    public CombatHit[] getHits(Player attacker, Mob defender) {
        return new CombatHit[]{ nextMeleeHit(attacker, defender) };
    }

    @Override
    public int getAttackDelay(Player attacker, Mob defender, FightType fightType) {
        return attacker.getCombat().getFightType().getDelay();
    }

    public static ScytheOfViturStrategy get() {
        return INSTANCE;
    }

    private void hitEvent(Player attacker, Mob defender, Mob other) {
        if (!CombatUtil.canBasicAttack(attacker, other)) {
            return;
        }

        if (attacker.equals(other) || defender.equals(other)) {
            return;
        }

        if(!Position.isWithinDiagonalDistance(attacker, other, 1) || defender.getPosition().equals(other.getPosition())) {
            return;
        }

        CombatHit hit = nextMeleeHit(attacker, other);
        other.damage(hit);
        other.getCombat().attack(attacker);
    }
}
