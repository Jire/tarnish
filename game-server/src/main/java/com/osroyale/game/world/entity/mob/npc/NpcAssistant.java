package com.osroyale.game.world.entity.mob.npc;

import com.osroyale.game.task.impl.ForceChatEvent;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.combat.Combat;
import com.osroyale.game.world.entity.combat.CombatType;
import com.osroyale.game.world.entity.combat.attack.listener.CombatListener;
import com.osroyale.game.world.entity.combat.attack.listener.CombatListenerManager;
import com.osroyale.game.world.entity.combat.projectile.CombatProjectile;
import com.osroyale.game.world.entity.combat.strategy.CombatStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcMagicStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcMeleeStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcRangedStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.definition.NpcDefinition;
import com.osroyale.util.parser.impl.NpcForceChatParser;

import java.util.Optional;

/**
 * Method handles small methods for npcs that do not have any parent class.
 *
 * @author Daniel
 */
public class NpcAssistant {

    /** The npc. */
    private Npc npc;

    /** Creates a new <code>NpcAssistant<code> */
    NpcAssistant(Npc npc) {
        this.npc = npc;
    }

    /** Handles initializing all the npc assistant methods on login. */
    public void login() {
        Combat<Npc> combat = npc.getCombat();
        NpcDefinition definition = NpcDefinition.get(npc.id);
        npc.definition = definition;
        npc.pathfinderProjectiles = Mob.pathfinderProjectiles(npc);

        npc.setWidth(definition.getSize());
        npc.setLength(definition.getSize());
        npc.setBonuses(definition.getBonuses());
        npc.mobAnimation.setNpcAnimations(definition);
        reloadSkills();

        CombatListener<Npc> listener = CombatListenerManager.NPC_LISTENERS.get(npc.id);
        if (listener != null) {
            combat.addListener(listener);
        }

        npc.setStrategy(NpcUtility.STRATEGIES.getOrDefault(npc.id, () -> loadStrategy(npc).orElse(NpcMeleeStrategy.get())).get());
        setEvent();
    }

    public void reloadSkills() {
        for (int index = 0; index < npc.definition.getSkills().length; index++) {
            npc.skills.setNpcMaxLevel(index, npc.definition.getSkills()[index]);
        }
    }

    public static boolean isDragon(int id) {
        for (int dragon : NpcUtility.DRAGONS) if (id == dragon) return true;
        return false;
    }

    public static Optional<CombatStrategy<Npc>> loadStrategy(Npc npc) {
        if (!npc.definition.getCombatAttackData().isPresent()) {
            return Optional.empty();
        }

        NpcDefinition.CombatAttackData data = npc.definition.getCombatAttackData().get();
        CombatType type = data.type;

        switch (type) {
            case RANGED: {
                CombatProjectile definition = data.getDefinition();
                if (definition == null) {
                    throw new AssertionError("Could not find ranged projectile for Mob[id=" + npc.id + ", name=" + npc.getName() + "]");
                }
                return Optional.of(new NpcRangedStrategy(definition));
            }
            case MAGIC: {
                CombatProjectile definition = data.getDefinition();
                if (definition == null) {
                    throw new AssertionError("Could not find magic projectile for Mob[id=" + npc.id + ", name=" + npc.getName() + "]");
                }
                return Optional.of(new NpcMagicStrategy(definition));
            }
            case MELEE:
                return Optional.of(NpcMeleeStrategy.get());
        }
        return Optional.empty();
    }

    /** Handles the npc respawning. */
    void respawn() {
        npc.setVisible(true);
        npc.skills.restoreAll();
        npc.getCombat().reset();
    }

    /** Handles setting all the events for the npc. */
    private void setEvent() {
        if (NpcForceChatParser.FORCED_MESSAGES.containsKey(npc.spawnPosition)) {
            NpcForceChatParser.ForcedMessage forcedMessage = NpcForceChatParser.FORCED_MESSAGES.get(npc.spawnPosition);
            if (forcedMessage.getId() == npc.id) {
                World.schedule(new ForceChatEvent(npc, forcedMessage));
            }
        }
    }

    public boolean isBoss() {
        for (int id : NpcUtility.BOSSES) {
            if (npc.id == id)
                return true;
        }
        return false;
    }

    public boolean isBandos() {
        for (int id : NpcUtility.BANDOS) {
            if (npc.id == id)
                return true;
        }
        return false;
    }

    public boolean isArmadyl() {
        for (int id : NpcUtility.ARMADYL) {
            if (npc.id == id)
                return true;
        }
        return false;
    }

    public boolean isSaradomin() {
        for (int id : NpcUtility.SARADOMIN) {
            if (npc.id == id)
                return true;
        }
        return false;
    }

    public boolean isZamorak() {
        for (int id : NpcUtility.ZAMORAK) {
            if (npc.id == id)
                return true;
        }
        return false;
    }
}
