package com.osroyale.content.activity.impl.godwars;

import com.osroyale.content.activity.ActivityListener;
import com.osroyale.game.world.entity.combat.CombatType;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;

import static com.osroyale.content.activity.impl.godwars.GodwarsActivity.*;

/**
 * The combat listener for the god wars activity.
 *
 * @author Daniel
 */
public class GodwarsListener extends ActivityListener<GodwarsActivity> {

    GodwarsListener(GodwarsActivity activity) {
        super(activity);
    }

    @Override
    public boolean canAttack(Mob attacker, Mob defender) {
        System.out.println(defender);
        if (!attacker.isPlayer() && !defender.isNpc()) {
            return super.canAttack(attacker, defender);
        }

        Npc npc = defender.getNpc();

        if (npc.id == 3166 || npc.id == 3167 || npc.id == 3168 || npc.id == 3172 || npc.id == 3176 || npc.id == 3183) {
            if (attacker.getStrategy().getCombatType().equals(CombatType.MELEE)) {
                attacker.getPlayer().message("You can't attack Armadyl with melee!");
                return false;
            }
        }

        return super.canAttack(attacker, defender);
    }

    @Override
    public void onKill(Mob attacker, Mob defender, Hit hit) {
        if (defender.isNpc()) {
            Npc npc = defender.getNpc();
            if (npc.npcAssistant.isArmadyl()) {
                activity.increment(ARMADYL);
            } else if (npc.npcAssistant.isBandos()) {
                activity.increment(BANDOS);
            } else if (npc.npcAssistant.isSaradomin()) {
                activity.increment(SARADOMIN);
            } else if (npc.npcAssistant.isZamorak()) {
                activity.increment(ZAMORAK);
            }
        }
    }
}
