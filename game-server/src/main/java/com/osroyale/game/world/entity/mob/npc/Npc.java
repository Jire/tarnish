package com.osroyale.game.world.entity.mob.npc;

import com.osroyale.content.activity.Activity;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.EntityType;
import com.osroyale.game.world.entity.combat.Combat;
import com.osroyale.game.world.entity.combat.strategy.CombatStrategy;
import com.osroyale.game.world.entity.mob.Direction;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.data.PacketType;
import com.osroyale.game.world.entity.mob.npc.definition.NpcDefinition;
import com.osroyale.game.world.position.Area;
import com.osroyale.game.world.position.Position;
import com.osroyale.game.world.region.Region;
import com.osroyale.util.RandomUtils;
import com.osroyale.util.Utility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents a non-player character in the in-game world.
 *
 * @author Daniel | Obey
 * @author Michael | Chex
 */
public class Npc extends Mob {
    private static final Logger logger = LogManager.getLogger();
    private int sequence;
    public boolean walk;
    public boolean canAttack;
    public Mob owner;
    public final Direction faceDirection;
    public final Position spawnPosition;
    public NpcDefinition definition;
    private CombatStrategy<Npc> strategy;
    public final NpcAssistant npcAssistant = new NpcAssistant(this);
    private final Combat<Npc> combat = new Combat<>(this);
    private int walkCooldown;
    public Position[] boundaries;
    public int walkingRadius;
    public final AtomicInteger atomicPlayerCount = new AtomicInteger(0);

    public Npc(int id, Position position) {
        this(id, position, 0, Mob.DEFAULT_INSTANCE, Direction.SOUTH);
    }

    public Npc(int id, int instance, Position position) {
        super(position);
        this.id = id;
        this.instance = instance;
        this.faceDirection = Direction.SOUTH;
        this.spawnPosition = position;
        this.walkingRadius = 0;
    }

    public Npc(int id, Position position, int walkingRadius, int instance, Direction direction) {
        super(position);
        this.id = id;
        this.instance = instance;
        this.faceDirection = direction;
        this.spawnPosition = position.copy();
        this.walkingRadius = walkingRadius;
    }

    public Npc(int id, Position position, int walkingRadius, Direction direction) {
        super(position);
        this.id = id;
        this.faceDirection = direction;
        this.spawnPosition = position.copy();
        this.walkingRadius = walkingRadius;
    }


    @Override
    public void sequence() {
        try {
            action.sequence();

            if (sequence % 110 == 0) {
                for (int index = 0; index <= 6; index++) {
                    skills.regress(index);
                }
            }

            if (definition.isAttackable() && (definition.isAggressive() || Area.inWilderness(this))) {
                getCombat().checkAggression(spawnPosition);
            }

            if (!isDead() && !locking.locked(PacketType.MOVEMENT) && combat.getDefender() == null && !combat.inCombat() && walk) {
                if (walkCooldown > 0) {
                    walkCooldown--;
                } else {
                    Position target = Utility.randomElement(boundaries);
                    walk(target);
                    walkCooldown = RandomUtils.inclusive(10, 30);
                }
            }

            if (definition.isAttackable()) {
                getCombat().tick();
            }
        } catch (Exception ex) {
            logger.error(String.format("error npc.sequence(): %s", this), ex);
        }
        sequence++;
    }

    @Override
    public void register() {
        if (!isRegistered() && !World.getNpcs().contains(this)) {
            int w = walkingRadius * width();
            int l = walkingRadius * length();

            walk = walkingRadius != 0;
            boundaries = Utility.getInnerBoundaries(spawnPosition.transform(-w, -l), w * 2, l * 2);
            setRegistered(World.getNpcs().add(this));
            setPosition(getPosition());
            npcAssistant.login();
        }
    }

    @Override
    public void unregister() {
        if (isRegistered()) {
            World.getNpcs().remove((Npc) destroy());
        }
    }

    @Override
    public void addToRegion(Region region) {
        region.addNpc(this);
    }

    @Override
    public void removeFromRegion(Region region) {
        region.removeNpc(this);
    }

    @Override
    public void onStep() {
    }

    @Override
    public void appendDeath() {
        if (inActivity()) {
            Activity.forActivity(this, activity -> activity.onDeath(this));
            return;
        }
        World.schedule(new NpcDeath(this));
    }

    @Override
    public Combat<Npc> getCombat() {
        return combat;
    }

    @Override
    public CombatStrategy<Npc> getStrategy() {
        return strategy;
    }

    @Override
    public String getName() {
        return definition == null ? "Unknown" : definition.getName();
    }

    @Override
    public int getBonus(int index) {
        return definition.getBonuses()[index];
    }

    @Override
    public int[] getBonuses() {
        return definition.getBonuses();
    }

    @Override
    public EntityType getType() {
        return EntityType.NPC;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIndex(), definition);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Npc) {
            Npc other = (Npc) obj;
            return id == other.id && getIndex() == other.getIndex() && spawnPosition.equals(other.spawnPosition);
        }
        return obj == this;
    }

    @Override
    public String toString() {
        return String.format("Npc[name=" + getName() + " index=%d id=%d registered=%s instance=%s %s]", getIndex(), id, isRegistered(), instance, getPosition());
    }

    int getDeathTime() {
        return definition.getDeathTimer();
    }

    public boolean isAutoRetaliate() {
        return definition.isRetaliate();
    }

    public void setStrategy(CombatStrategy<Npc> strategy) {
        this.strategy = strategy;
    }

    @Override
    public int getPriorityIndex() {
        return -getListIndex();
    }

    /**
     * Pyromancer data
     */
    public int pyroHealth = 24;
    public boolean pyroSnowAttack;
}
