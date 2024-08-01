package com.osroyale.game.world.entity.combat.attack.listener.other;

import com.osroyale.game.Graphic;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.world.entity.combat.CombatType;
import com.osroyale.game.world.entity.combat.CombatUtil;
import com.osroyale.game.world.entity.combat.attack.listener.SimplifiedListener;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.hit.HitIcon;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.prayer.Prayer;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.position.Area;
import com.osroyale.util.RandomUtils;

public class PrayerListener extends SimplifiedListener<Mob> {

    private static final PrayerListener INSTANCE = new PrayerListener();

    private PrayerListener() {}

    @Override
    public void hit(Mob attacker, Mob defender, Hit hit) {
        if (attacker.prayer.isActive(Prayer.SMITE)) {
            defender.skills.get(Skill.PRAYER).removeLevel(hit.getDamage() / 4);
            defender.skills.refresh(Skill.PRAYER);
        }
    }

    @Override
    public void block(Mob attacker, Mob defender, Hit hit, CombatType combatType) {
        int health = defender.getCurrentHealth() - hit.getDamage();
        if (defender.prayer.isActive(Prayer.REDEMPTION) && health < defender.getMaximumHealth() / 10) {
            Skill skill = defender.skills.get(Skill.PRAYER);
            int amount = skill.getLevel() / 4;
            defender.heal(amount);
            defender.graphic(new Graphic(436, UpdatePriority.HIGH));
            if (defender.isPlayer())
                defender.getPlayer().playerAssistant.drainPrayer(skill.getLevel());
        }
    }

    @Override
    public void preDeath(Mob attacker, Mob defender, Hit hit) {
        if (defender.prayer.isActive(Prayer.RETRIBUTION)) {
            int max = defender.skills.getMaxLevel(Skill.PRAYER) / 4;
            defender.graphic(new Graphic(437, UpdatePriority.HIGH));

            if (!Area.inMulti(defender)) {
                attacker.damage(new Hit(RandomUtils.inclusive(max), HitIcon.NONE));
            } else {
                CombatUtil.areaAction(defender, 3 * 3, 1, other -> {
                    int damage = RandomUtils.inclusive(max);
                    other.damage(new Hit(damage, HitIcon.NONE));
                });
            }
        }
    }

    public static PrayerListener get() {
        return INSTANCE;
    }

}
