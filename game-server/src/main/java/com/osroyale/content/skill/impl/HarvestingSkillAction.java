package com.osroyale.content.skill.impl;

import com.google.common.base.Preconditions;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.game.action.Action;
import com.osroyale.game.action.policy.WalkablePolicy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.content.skill.SkillAction;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.position.Position;
import com.osroyale.util.RandomGen;
import com.osroyale.util.StringUtils;

import java.util.Optional;

/**
 * Holds functionality for skills such as woodcutting and mining.
 *
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 19-12-2016.
 */
public abstract class HarvestingSkillAction extends SkillAction {

    /**
     * The double onReward types.
     */
    public enum DoubleReward {
        NONE, ITEM, EXPERIENCE, ALL
    }

    /**
     * The factor boost that determines the success rate for harvesting based on
     * skill level. The higher the number the less frequently harvest will be
     * obtained. A value higher than {@code 99} or lower than {@code 0} will
     * throw an {@link IllegalStateException}.
     */
    private static final int SUCCESS_FACTOR = 10;

    /**
     * The random generator instance that will generate random numbers.
     */
    private final RandomGen random = new RandomGen();

    /**
     * Creates a new {@link Action} randomevent.
     *
     * @param mob      {@link #mob}.
     * @param position {@link #position}.
     * @param instant  {@link #instant}.
     */
    public HarvestingSkillAction(Mob mob, Optional<Position> position, boolean instant) {
        super(mob, position, instant);
    }

    /**
     * Creates a new {@link Action} randomevent.
     *
     * @param mob      {@link #mob}.
     * @param position {@link #position}.
     * @param delay    {@link #delay}.
     * @param instant  {@link #instant}.
     */
    public HarvestingSkillAction(Mob mob, Optional<Position> position, int delay, boolean instant) {
        super(mob, position, delay, instant);
    }

    /**
     * The method executed upon harvest of the items.
     *
     * @param items   the items being harvested.
     * @param success determines if the harvest was successful or not.
     */
    public void onHarvest(Item[] items, boolean success) {

    }

    /**
     * If mob will get a double onReward.
     *
     * @return If mob will get a double onReward.
     */
    public DoubleReward doubleReward() {
        return DoubleReward.NONE;
    }

    /**
     * The success factor for the harvest. The higher the number means the more
     * frequently harvest will be obtained.
     *
     * @return the success factor.
     */
    public abstract double successFactor();

    /**
     * The items to be removed upon a successful harvest.
     *
     * @return the items to be removed.
     */
    public abstract Optional<Item[]> removeItems();

    /**
     * The items to be harvested upon a successful harvest.
     *
     * @return the items to be harvested.
     */
    public abstract Item[] harvestItems();

    @Override
    public final boolean canRun() {
        if (getMob().isPlayer()) {
            Player player = getMob().getPlayer();
            if (getMob().isPlayer() && getMob().getPlayer().inventory.remaining() < 1) {
                getMob().getPlayer().send(new SendMessage("You do not have any space left in your inventory."));
                return false;
            }
            if (removeItems().isPresent()) {
                Item[] remove = removeItems().get();

                if (!player.inventory.containsAll(remove)) {
                    for (Item item : remove) {
                        if (!player.inventory.contains(item)) {
                            player.send(new SendMessage("You don't have " + StringUtils.appendIndefiniteArticle(item.getName() + ".")));
                            break;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onExecute() {
        Preconditions.checkState(SUCCESS_FACTOR >= 0 && SUCCESS_FACTOR <= 99, "Invalid success factor for harvesting!");
        int factor = (getMob().skills.getSkills()[skill()].getLevel() / SUCCESS_FACTOR);
        double boost = (factor * 0.01);
        if (random.success(successFactor() + boost)) {
            Optional<Item[]> removeItems = removeItems();
            Item[] harvestItems = harvestItems();

            if (getMob().isPlayer()) {
                removeItems.ifPresent(getMob().getPlayer().inventory::removeAll);
                if (harvestItems != null)
                    getMob().getPlayer().inventory.addAll(harvestItems);
                if (experience() != -1)
                    getMob().skills.addExperience(skill(), experience());
                onHarvest(harvestItems, true);

                if (doubleReward() == DoubleReward.ITEM) {
                    getMob().getPlayer().inventory.addAll(harvestItems);
                } else if (doubleReward() == DoubleReward.EXPERIENCE) {
                    getMob().skills.addExperience(skill(), experience());
                } else if (doubleReward() == DoubleReward.ALL) {
                    getMob().getPlayer().inventory.addAll(harvestItems);
                    getMob().skills.addExperience(skill(), experience());
                }
            }
        } else {
            onHarvest(null, false);
        }
    }

    /**
     * Determines if this action is prioritized.
     * <p>When making an action prioritized, the next action will be ignored
     * if not queued.</p>
     *
     * @return {@code true} if this action is prioritized, {@code false} otherwise.
     */
    @Override
    public boolean prioritized() {
        return false;
    }

    /**
     * Gets the WalkablePolicy of this action.
     *
     * @return The walkable policy of this action.
     */
    @Override
    public WalkablePolicy getWalkablePolicy() {
        return WalkablePolicy.NON_WALKABLE;
    }
}
