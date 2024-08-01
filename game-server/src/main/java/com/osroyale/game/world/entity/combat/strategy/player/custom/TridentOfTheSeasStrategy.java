package com.osroyale.game.world.entity.combat.strategy.player.custom;

import com.osroyale.Config;
import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.world.entity.combat.CombatType;
import com.osroyale.game.world.entity.combat.attack.FightType;
import com.osroyale.game.world.entity.combat.hit.CombatHit;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.projectile.CombatProjectile;
import com.osroyale.game.world.entity.combat.strategy.basic.MagicStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.containers.equipment.Equipment;
import com.osroyale.net.packet.out.SendMessage;

public class TridentOfTheSeasStrategy extends MagicStrategy<Player> {
    private static final TridentOfTheSeasStrategy INSTANCE = new TridentOfTheSeasStrategy();
    private static CombatProjectile PROJECTILE;

    static {
        try {
            PROJECTILE = CombatProjectile.getDefinition("Trident of the Seas");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean canAttack(Player attacker, Mob defender) {
        if (defender.isPlayer() && !PlayerRight.isOwner(defender.getPlayer())) {
            attacker.send(new SendMessage("You can't attack players with this weapon."));
            return false;
        }

        Item weapon = attacker.equipment.get(Equipment.WEAPON_SLOT);

        if (weapon == null) {
            attacker.getCombat().reset();
            return false;
        }

        if (weapon.getId() == 11907 && attacker.tridentSeasCharges < 1) {
            attacker.send(new SendMessage("Your trident is out of charges!"));
            attacker.getCombat().reset();
            return false;
        }

        return true;
    }

    @Override
    public void start(Player attacker, Mob defender, Hit[] hits) {
        PROJECTILE.getAnimation().ifPresent(animation -> attacker.animate(animation, true));
        PROJECTILE.getStart().ifPresent(attacker::graphic);
        int projectileDuration = PROJECTILE.sendProjectile(attacker, defender);
        final Graphic endGraphic = getEndGraphic(PROJECTILE.getEnd(), missed(hits), SPLASH, projectileDuration);
        if (endGraphic != null) {
            defender.graphic(endGraphic);
        }

        if (!defender.isPlayer() || !PlayerRight.isIronman(attacker)) {
            for (Hit hit : hits) {
                int exp = 2 * hit.getDamage();
                attacker.skills.addExperience(Skill.MAGIC, exp * Config.COMBAT_MODIFICATION);
                attacker.skills.addExperience(Skill.HITPOINTS, exp / 3 * Config.COMBAT_MODIFICATION);
            }
        }
    }

    @Override
    public void attack(Player attacker, Mob defender, Hit hit) {
        attacker.tridentSeasCharges--;
    }

    @Override
    public void hit(Player attacker, Mob defender, Hit hit) {
    }

    @Override
    public Animation getAttackAnimation(Player attacker, Mob defender) {
        int animation = attacker.getCombat().getFightType().getAnimation();
        return new Animation(animation, UpdatePriority.HIGH);
    }

    @Override
    public int getAttackDelay(Player attacker, Mob defender, FightType fightType) {
        return attacker.getCombat().getFightType().getDelay();
    }

    @Override
    public int getAttackDistance(Player attacker, FightType fightType) {
        return 10;
    }

    @Override
    public CombatHit[] getHits(Player attacker, Mob defender) {
        int max = 23;
        int level = attacker.skills.getLevel(Skill.MAGIC);
        if (level - 75 > 0) {
            max += (level - 75) / 3;
        }
        return new CombatHit[]{nextMagicHit(attacker, defender, max, PROJECTILE)};
    }

    @Override
    public CombatType getCombatType() {
        return CombatType.MAGIC;
    }

    public static TridentOfTheSeasStrategy get() {
        return INSTANCE;
    }

}
