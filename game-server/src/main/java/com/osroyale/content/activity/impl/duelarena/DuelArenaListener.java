package com.osroyale.content.activity.impl.duelarena;

import com.osroyale.content.activity.ActivityListener;
import com.osroyale.game.world.entity.combat.CombatType;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.net.packet.out.SendMessage;

public class DuelArenaListener extends ActivityListener<DuelArenaActivity> {

    DuelArenaListener(DuelArenaActivity activity) {
        super(activity);
    }

    @Override
    public void start(Mob attacker, Mob defender, Hit[] hits) {
        System.out.println(defender);
        if (activity.hasLoser) {
            return;
        }

        super.start(attacker, defender, hits);
    }

    @Override
    public void onDamage(Mob defender, Hit hit) {
        if (activity.hasLoser) {
            hit.setDamage(0);
        }
    }

    @Override
    public void preKill(Mob attacker, Mob defender, Hit hit) {
        activity.hasLoser = true;
        attacker.damageImmunity.reset();
    }

    @Override
    public boolean canAttack(Mob attacker, Mob defender) {
        Player player = activity.player;
        Player opponent = activity.opponent;

        if (player == null || opponent == null || !attacker.isPlayer() || !defender.isPlayer()) {
            return false;
        }

        if (!attacker.equals(player) && !attacker.equals(opponent)) {
            attacker.getPlayer().send(new SendMessage("You are not in this duel."));
            return false;
        }

        if (!defender.equals(player) && !defender.equals(opponent)) {
            attacker.getPlayer().send(new SendMessage(defender.getName() + " is not your opponent!"));
            return false;
        }

        if (!activity.started) {
            attacker.getPlayer().send(new SendMessage("The duel hasn't started yet!"));
            return false;
        }

        if (activity.rules.contains(DuelRule.ONLY_FUN_WEAPONS) && !DuelUtils.hasFunWeapon(attacker.getPlayer(), attacker.getPlayer().equipment.getWeapon())) {
            attacker.getPlayer().send(new SendMessage("You can only use fun weapons!"));
            return false;
        }

        if (activity.rules.contains(DuelRule.ONLY_WHIP_DDS)) {
            if (attacker.getPlayer().equipment.hasWeapon()) {
                Item weapon = attacker.getPlayer().equipment.getWeapon();
                String name = weapon.getName().toLowerCase();

                if (!name.contains("dragon dagger") && !name.contains("abyssal whip") && !name.contains("abyssal tentacle")) {
                    attacker.getPlayer().send(new SendMessage("You can only use a whip or dragon dagger!"));
                    return false;
                }
            }
        }

        CombatType combatType = attacker.getPlayer().getStrategy().getCombatType();

        if (combatType == CombatType.MELEE && activity.rules.contains(DuelRule.NO_MELEE)) {
            attacker.getPlayer().send(new SendMessage("You cannot use melee in the duel arena."));
            return false;
        }

        if (combatType == CombatType.RANGED && activity.rules.contains(DuelRule.NO_RANGED)) {
            attacker.getPlayer().send(new SendMessage("You cannot use ranged in the duel arena."));
            return false;
        }

        if (combatType == CombatType.MAGIC && activity.rules.contains(DuelRule.NO_MAGIC)) {
            attacker.getPlayer().send(new SendMessage("You cannot use magic in the duel arena."));
            return false;
        }

        return super.canAttack(attacker, defender);
    }
}