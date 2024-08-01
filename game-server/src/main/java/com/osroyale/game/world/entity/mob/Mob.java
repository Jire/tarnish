package com.osroyale.game.world.entity.mob;

import com.osroyale.content.activity.Activity;
import com.osroyale.content.activity.ActivityType;
import com.osroyale.content.wintertodt.Wintertodt;
import com.osroyale.fs.cache.decoder.AnimationDefinition;
import com.osroyale.fs.cache.decoder.AnimationDefinitionDecoder;
import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.action.ActionManager;
import com.osroyale.game.task.impl.ForceMovementTask;
import com.osroyale.game.world.Interactable;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.Entity;
import com.osroyale.game.world.entity.EntityType;
import com.osroyale.game.world.entity.combat.Combat;
import com.osroyale.game.world.entity.combat.CombatUtil;
import com.osroyale.game.world.entity.combat.PoisonType;
import com.osroyale.game.world.entity.combat.attack.listener.CombatListener;
import com.osroyale.game.world.entity.combat.attack.listener.CombatListenerManager;
import com.osroyale.game.world.entity.combat.effect.CombatEffectType;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.strategy.CombatStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcMeleeStrategy;
import com.osroyale.game.world.entity.mob.data.LockType;
import com.osroyale.game.world.entity.mob.movement.Movement;
import com.osroyale.game.world.entity.mob.movement.waypoint.CombatWaypoint;
import com.osroyale.game.world.entity.mob.movement.waypoint.FollowWaypoint;
import com.osroyale.game.world.entity.mob.movement.waypoint.WalkToWaypoint;
import com.osroyale.game.world.entity.mob.movement.waypoint.Waypoint;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.npc.NpcAssistant;
import com.osroyale.game.world.entity.mob.npc.NpcUtility;
import com.osroyale.game.world.entity.mob.npc.definition.NpcDefinition;
import com.osroyale.game.world.entity.mob.player.ForceMovement;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.relations.ChatMessage;
import com.osroyale.game.world.entity.mob.prayer.PrayerBook;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.entity.skill.SkillManager;
import com.osroyale.game.world.object.GameObject;
import com.osroyale.game.world.pathfinding.TraversalMap;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.packet.out.SendPoison;
import com.osroyale.util.MutableNumber;
import com.osroyale.util.Stopwatch;
import com.osroyale.util.Utility;
import com.osroyale.util.generic.BooleanInterface;
import com.osroyale.util.generic.GenericAttributes;

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static com.osroyale.game.world.entity.combat.CombatConstants.EMPTY_BONUSES;

/**
 * Handles the mob class.
 *
 * @author Daniel
 * @author Chex
 */
public abstract class Mob extends Entity {

    public boolean pathfinderProjectiles = false;
    private int listIndex;
    public int id = -1;
    private int transformId;
    private boolean dead;
    public boolean regionChange;
    public boolean positionChange;
    public boolean forceWalking;
    public boolean teleporting;
    public boolean inTeleport;
    public boolean teleportRegion;
    public boolean blockFace;
    public boolean blockInteract;
    public String forceChat;
    public Mob interactingWith;
    public Hit firstHit;
    public Hit secondHit;
    public Position lastPosition;
    public Position teleportTarget;
    public Position facePosition;
    public Activity activity;
    private Optional<Animation> animation = Optional.empty();
    public transient long nextAnimation;
    private Optional<Graphic> graphic = Optional.empty();
    public List<Mob> followers = new LinkedList<>();
    public ForceMovement forceMovement;
    public final EnumSet<UpdateFlag> updateFlags = EnumSet.noneOf(UpdateFlag.class);
    public final GenericAttributes attributes = new GenericAttributes();
    public final SkillManager skills = new SkillManager(this);
    public final SkillManager skills_copy = new SkillManager(this);
    public final Movement movement = new Movement(this);
    public MobAnimation mobAnimation = new MobAnimation(updateFlags);
    public ActionManager action = new ActionManager();
    protected Waypoint cachedWaypoint;
    public PrayerBook prayer = new PrayerBook();
    private int[] bonuses = EMPTY_BONUSES;
    private final MutableNumber poisonDamage = new MutableNumber();
    private final MutableNumber venomDamage = new MutableNumber();
    public final Locking locking = new Locking(this);
    public final Stopwatch freezeImmunity = Stopwatch.start();
    private PoisonType poisonType;
    public Stopwatch damageImmunity = Stopwatch.start();

    /**
     * Constructs a new <code>Mob</code>.
     */
    public Mob(Position position) {
        super(position);
        this.lastPosition = position.copy();
    }

    public Mob(Position position, boolean visible) {
        super(position, visible);
        this.lastPosition = position.copy();
    }

    /**
     * Sets the mob's forced chat.
     */
    public void speak(String forceChat) {
        if (forceChat == null || forceChat.isEmpty() || forceChat.length() > ChatMessage.CHARACTER_LIMIT)
            return;
        this.forceChat = forceChat;
        this.updateFlags.add(UpdateFlag.FORCED_CHAT);
    }

    public void animate(int animation) {
        animate(animation, false);
    }

    public void animate(int animation, boolean override) {
        animate(new Animation(animation), override);
    }

    /**
     * Plays an animation.
     */
    public void animate(Animation animation) {
        animate(animation, false);
    }

    public void graphic(int graphic) {
        graphic(new Graphic(graphic));
    }

    /**
     * Plays a graphic.
     */
    public void graphic(Graphic graphic) {
        graphic(graphic, false);
    }

    /**
     * Plays an animation.
     */
    public void animate(Animation animation, final boolean override) {
        final long now = System.currentTimeMillis();
        if (!override && (nextAnimation > now || updateFlags.contains(UpdateFlag.ANIMATION))) {
            return;
        }

        final Optional<Animation> result = Optional.ofNullable(animation);
        animation = result.orElse(Animation.RESET);

        final AnimationDefinition definition =
                animation == Animation.RESET
                        ? null
                        : AnimationDefinitionDecoder.definitions.get(animation.getId());
        if (definition != null) {
            nextAnimation = now + definition.durationTime + 600;
        }
        this.animation = result;
        this.updateFlags.add(UpdateFlag.ANIMATION);
    }

    /**
     * Plays a graphic.
     */
    public void graphic(Graphic graphic, boolean override) {
        Optional<Graphic> result = Optional.ofNullable(graphic);
        graphic = result.orElse(Graphic.RESET);

        if (!this.graphic.isPresent() || override || this.graphic.get().compareTo(graphic) > 0) {
            this.graphic = result;
            this.updateFlags.add(UpdateFlag.GRAPHICS);
        }
    }

    public static boolean pathfinderProjectiles(Mob source) {
        final String name = source.getName().toLowerCase();
        return "imp".equals(name) || name.contains("impling");
    }

    public void transform(int transformId) {
        transform(transformId, false);
    }

    /**
     * Transforms the mob.
     */
    public void transform(int transformId, boolean reload) {
        this.transformId = transformId;
        this.id = transformId;
        this.updateFlags.add(UpdateFlag.TRANSFORM);
        this.updateFlags.add(UpdateFlag.APPEARANCE);

        if (isNpc()) {
            NpcDefinition definition = NpcDefinition.get(id);
            final Npc npc = getNpc();
            npc.definition = definition;
            npc.pathfinderProjectiles = Mob.pathfinderProjectiles(npc);
            setWidth(definition.getSize());
            setLength(definition.getSize());
            setBonuses(definition.getBonuses());
            mobAnimation.setNpcAnimations(definition);

            if (reload) {
                Combat<Npc> combat = getNpc().getCombat();
                CombatListener<Npc> listener = CombatListenerManager.NPC_LISTENERS.get(getNpc().id);

                if (listener != null) {
                    combat.addListener(listener);
                }

                getNpc().npcAssistant.reloadSkills();
                getNpc().setStrategy(NpcUtility.STRATEGIES.getOrDefault(getNpc().id, () -> NpcAssistant.loadStrategy(getNpc()).orElse(NpcMeleeStrategy.get())).get());
            }
        }
    }

    /**
     * Resets the mob after an update.
     */
    public final void reset() {
        resetAnimation();
        resetGraphic();
    }

    /**
     * Resets the waypoint
     */
    public final void resetWaypoint() {
        if (cachedWaypoint != null && cachedWaypoint.isRunning()) {
            cachedWaypoint.cancel();
        }
    }

    public void forceMove(int animation, int x, int y) {
        forceMove(0, 0, animation, 0, 0, 0, new Position(x, y), Direction.NORTH);
    }

    public void forceMove(int delay, int animation, int startSpeed, int endSpeed, Position offset, Direction direction) {
        forceMove(delay, 0, animation, startSpeed, endSpeed, offset, direction);
    }

    public void forceMove(int delay, int delay2, int animation, int startSpeed, int endSpeed, Position offset, Direction direction) {
        forceMove(delay, delay2, animation, 0, startSpeed, endSpeed, offset, direction);
    }

    /**
     * Creates a force movement action for an entity.
     */
    public void forceMove(int delay, int delay2, int animation, int animationDelay, int startSpeed, int endSpeed, Position offset, Direction direction) {
        ForceMovement movement = new ForceMovement(getPosition().copy(), offset, startSpeed, endSpeed, direction);
        World.schedule(new ForceMovementTask(this, delay, delay2, movement, new Animation(animation, animationDelay)));
    }

    /**
     * Sets the mob interacting with another mob.
     */
    public void interact(Mob mob) {
        if (blockInteract) {
            return;
        }
        this.interactingWith = mob;
        this.updateFlags.add(UpdateFlag.INTERACT);
    }

    /**
     * Sets the client update flag to face a certain direction.
     */
    public void face(GameObject object) {
        if (blockFace)
            return;
        if (object == null || object.getPosition().equals(facePosition))
            return;
        this.facePosition = object.getPosition();
        this.updateFlags.add(UpdateFlag.FACE_COORDINATE);
    }

    public void face(Mob mob) {
        face(mob.getPosition());
    }

    /**
     * Sets the client update flag to face a certain direction.
     */
    public void face(Position position) {
        if (blockFace)
            return;
        if (!position.equals(facePosition)) {
            this.facePosition = position;
            this.updateFlags.add(UpdateFlag.FACE_COORDINATE);
        }
    }

    /**
     * Sets the client update flag to face a certain direction.
     */
    public void face(Direction direction) {
        if (blockFace)
            return;
        Position position = getPosition().transform(direction.getFaceLocation());
        if (!position.equals(facePosition)) {
            this.facePosition = position;
            this.updateFlags.add(UpdateFlag.FACE_COORDINATE);
        }
    }

    /**
     * Resets the mob's face location.
     */
    public void resetFace() {
        if (blockFace || interactingWith == null)
            return;
        interactingWith = null;
        this.updateFlags.add(UpdateFlag.INTERACT);
    }

    /**
     * Moves the mob to a set position.
     */
    public void move(Position position) {
        if (regionChange)
            return;
        if (isPlayer() && !getPlayer().interfaceManager.isClear())
            getPlayer().interfaceManager.close(false);
        setPosition(position);
        if (Utility.isRegionChange(position, lastPosition)) {
            regionChange = true;
        } else {
            positionChange = true;
        }
        teleportRegion = true;
        getCombat().reset();
        resetFace();
        locking.lock(599, TimeUnit.MILLISECONDS, LockType.MASTER);
        onStep();
    }

    public void walk(Position position) {
        walk(position, false);
    }

    public void walk(Position destination, boolean ignoreClip) {
        if (ignoreClip) {
            movement.walk(destination);
        } else {
            movement.simplePath(destination);
        }
    }

    public void runTo(Position destination) {
        movement.dijkstraPath(destination);
    }

    public void walkTo(Position position) {
        getCombat().reset();
        walkTo(position, () -> { /* Do nothing on arrival */ });
    }

    public void walkTo(Position position, Runnable onDestination) {
        Interactable interactable = Interactable.create(position);
        walkTo(interactable, onDestination);
    }

    public void walkExactlyTo(Position position) {
        walkExactlyTo(position, () -> {
        });
    }

    public void walkExactlyTo(Position position, Runnable onDestination) {
        Interactable interactable = Interactable.create(position, 0, 0);
        walkTo(interactable, onDestination);
    }

    public void walkTo(Interactable target, Runnable onDestination) {
        walkTo(target, true, onDestination);
    }

    public void walkTo(Interactable target, boolean clearAction, Runnable onDestination) {
        Waypoint waypoint = new WalkToWaypoint(this, target, onDestination);

        if (cachedWaypoint == null || (!cachedWaypoint.isRunning() || !waypoint.equals(cachedWaypoint))) {
            resetWaypoint();
            getCombat().reset();
            movement.reset();

            if (clearAction) {
                action.clearNonWalkableActions();
            }

            World.schedule(cachedWaypoint = waypoint);
        }
    }

    public void follow(Mob target) {
        Waypoint waypoint = new FollowWaypoint(this, target);
        if (cachedWaypoint == null || (!cachedWaypoint.isRunning() || !waypoint.equals(cachedWaypoint))) {
            resetWaypoint();
            movement.reset();
            action.clearNonWalkableActions();
            World.schedule(cachedWaypoint = waypoint);
        }
    }

    public void attack(Mob target) {
        Waypoint waypoint = new CombatWaypoint(this, target);
        if (cachedWaypoint == null || (!cachedWaypoint.isRunning() || !waypoint.equals(cachedWaypoint))) {
            resetWaypoint();
            movement.reset();
            action.clearNonWalkableActions();
            World.schedule(cachedWaypoint = waypoint);
        }
    }

    protected void setWaypoint(Waypoint waypoint) {
        if (cachedWaypoint == null || (!cachedWaypoint.isRunning() || !waypoint.equals(cachedWaypoint))) {
            resetWaypoint();
            movement.reset();
            action.clearNonWalkableActions();
            World.schedule(cachedWaypoint = waypoint);
        }
    }

    public void damage(Hit... hits) {
        for (Hit hit : hits)
            getCombat().queueDamage(hit);
    }

    public void writeFakeDamage(Hit hit) {
        if (!updateFlags.contains(UpdateFlag.FIRST_HIT)) {
            firstHit = hit;
            updateFlags.add(UpdateFlag.FIRST_HIT);
        } else {
            secondHit = hit;
            updateFlags.add(UpdateFlag.SECOND_HIT);
        }
    }

    public void writeDamage(Hit hit) {
        if (isDead() || getCurrentHealth() < 1) {
            return;
        }

        if (!damageImmunity.finished()) {
            return;
        }

        getCombat().onDamage(hit);

        if (!updateFlags.contains(UpdateFlag.FIRST_HIT)) {
            firstHit = decrementHealth(hit);
            updateFlags.add(UpdateFlag.FIRST_HIT);
        } else {
            secondHit = decrementHealth(hit);
            updateFlags.add(UpdateFlag.SECOND_HIT);
        }
    }

    public Hit decrementHealth(Hit hit) {
        if (getCurrentHealth() - hit.getDamage() < 0)
            hit.modifyDamage(damage -> getCurrentHealth());
        skills.modifyLevel(level -> level - hit.getDamage(), Skill.HITPOINTS, 0, getCurrentHealth());
        skills.refresh(Skill.HITPOINTS);
        if (getCurrentHealth() < 1)
            appendDeath();

        return hit;
    }

    public void heal(int amount) {
        int health = getCurrentHealth();
        if (health >= getMaximumHealth())
            return;
        skills.modifyLevel(hp -> health + amount, Skill.HITPOINTS, 0, getMaximumHealth());
        skills.refresh(Skill.HITPOINTS);
    }

    /**
     * Applies poison with an intensity of {@code type} to the entity.
     */
    public void poison(PoisonType type) {
        poisonType = type;
        CombatUtil.effect(this, CombatEffectType.POISON);
    }

    /**
     * Applies venom to the entity.
     */
    public void venom() {
        CombatUtil.effect(this, CombatEffectType.VENOM);
    }

    public void setForceMovement(ForceMovement forceMovement) {
        this.forceMovement = forceMovement;
        if (forceMovement != null)
            this.updateFlags.add(UpdateFlag.FORCE_MOVEMENT);
    }

    public boolean inActivity() {
        return activity != null;
    }

    public boolean inActivity(ActivityType type) {
        return inActivity() && activity.getType() == type;
    }

    public void setActivity(Activity activity) {
        if (this.activity != null) {
            this.activity.cleanup();
        }
        this.activity = activity;
    }

    /**
     * Resets the teleport target.
     */
    public void clearTeleportTarget() {
        this.teleportTarget = null;
    }

    /**
     * Checks if mob requires an update.
     */
    public boolean isUpdateRequired() {
        return !updateFlags.isEmpty();
    }

    /**
     * Check if an entity is an npc.
     */
    public final boolean isNpc() {
        return getType() == EntityType.NPC;
    }

    /**
     * Check if an entity is an npc.
     */
    public final boolean isNpc(BooleanInterface<Npc> condition) {
        return getType() == EntityType.NPC && condition.activated(getNpc());
    }

    /**
     * Check if an entity is a player
     */
    public final boolean isPlayer() {
        return getType() == EntityType.PLAYER;
    }

    public final Npc getNpc() {
        return (Npc) this;
    }

    /**
     * Check if an entity is a player
     */
    public final boolean isPlayer(Function<Player, Boolean> condition) {
        return getType() == EntityType.PLAYER && condition.apply(getPlayer());
    }

    public void takeStep() {
        Position walkTo = getPosition();

        if (TraversalMap.isTraversable(getPosition(), Direction.WEST, width())) {
            walkTo = walkTo.west();
        } else if (TraversalMap.isTraversable(getPosition(), Direction.EAST, width())) {
            walkTo = walkTo.east();
        } else if (TraversalMap.isTraversable(getPosition(), Direction.SOUTH, width())) {
            walkTo = walkTo.north();
        } else if (TraversalMap.isTraversable(getPosition(), Direction.NORTH, width())) {
            walkTo = walkTo.south();
        }

        if (!getPosition().equals(walkTo)) {
            movement.walkTo(walkTo);
        }
    }

    public ForceMovement getForceMovement() {
        return forceMovement;
    }

    public void unpoison() {
        poisonDamage.set(0);
        poisonType = null;

        if (this instanceof Player) {
            Player player = (Player) this;
            player.send(new SendPoison(SendPoison.PoisonType.NO_POISON));
        }
    }

    public void unvenom() {
        venomDamage.set(0);

        if (this instanceof Player) {
            Player player = (Player) this;
            player.send(new SendPoison(SendPoison.PoisonType.NO_POISON));
        }
    }

    public final boolean isPoisoned() {
        return poisonDamage.get() > 0;
    }

    public final boolean isVenomed() {
        return venomDamage.get() > 0;
    }

    public final MutableNumber getPoisonDamage() {
        return poisonDamage;
    }

    public MutableNumber getVenomDamage() {
        return venomDamage;
    }

    public PoisonType getPoisonType() {
        return poisonType;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public final Player getPlayer() {
        return (Player) this;
    }

    public int getCurrentHealth() {
        if (isNpc()) {
            Npc npc = (Npc) this;
            switch (npc.id) {
                case Wintertodt.PYROMANCER, Wintertodt.INCAPACITATED_PYROMANCER -> {
                    return npc.pyroHealth;
                }
            }
        }
        return skills.getLevel(Skill.HITPOINTS);
    }

    public int getMaximumHealth() {
        if (isNpc()) {
            Npc npc = (Npc) this;
            switch (npc.id) {
                case Wintertodt.PYROMANCER, Wintertodt.INCAPACITATED_PYROMANCER -> {
                    return 24;
                }
            }
        }
        return skills.getMaxLevel(Skill.HITPOINTS);
    }

    public int[] getBonuses() {
        return bonuses;
    }

    public int getBonus(int index) {
        return bonuses[index];
    }

    public void setBonuses(int[] bonuses) {
        this.bonuses = bonuses;
    }

    public void appendBonus(int index, int amount) {
        if (bonuses == EMPTY_BONUSES)
            bonuses = new int[EMPTY_BONUSES.length];
        bonuses[index] += amount;
    }

    public void setBonus(int equipSlot, int bonus) {
        if (bonuses == EMPTY_BONUSES)
            bonuses = new int[EMPTY_BONUSES.length];
        bonuses[equipSlot] = bonus;
    }

    public int getListIndex() {
        return listIndex;
    }

    public void setListIndex(int listIndex) {
        this.listIndex = listIndex;
    }

    public Optional<Animation> getAnimation() {
        return animation;
    }

    public Optional<Graphic> getGraphic() {
        return graphic;
    }

    public void resetAnimation() {
        this.animation = Optional.empty();
    }

    public void resetGraphic() {
        this.graphic = Optional.empty();
    }

    /**
     * The method which is invoked every tick.
     */
    public abstract void sequence();

    /**
     * State of the mob's auto retaliate.
     */
    public abstract boolean isAutoRetaliate();

    /**
     * Handles the mob death.
     */
    protected abstract void appendDeath();

    /**
     * The combat strategy of the mob.
     */
    public abstract <T extends Mob> CombatStrategy<? super T> getStrategy();

    /**
     * The combat of the mob.
     */
    public abstract Combat<? extends Mob> getCombat();

    private boolean fixingInside;

    public boolean isFixingInside() {
        return fixingInside;
    }

    public void setFixingInside(boolean fixingInside) {
        this.fixingInside = fixingInside;
    }

    public int getPriorityIndex() {
        return getListIndex();
    }

    public boolean hasPriorityIndex(Mob other) {
        return getPriorityIndex() < other.getPriorityIndex();
    }

    public int getId() {
        return id;
    }

    public int getTransformId() {
        return transformId;
    }

}
