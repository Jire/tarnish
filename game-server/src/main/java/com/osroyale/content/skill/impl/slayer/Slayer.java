package com.osroyale.content.skill.impl.slayer;

import com.osroyale.Config;
import com.osroyale.content.achievement.AchievementHandler;
import com.osroyale.content.achievement.AchievementKey;
import com.osroyale.content.skillcape.SkillCape;
import com.osroyale.content.store.StoreItem;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.net.packet.out.SendString;
import com.osroyale.util.Utility;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Handles the slayer skill.
 *
 * @author Daniel
 */
public class Slayer {
    /** The player instance. */
    private Player player;

    /** The slayer points. */
    private int points;

    /** The slayer task. */
    private SlayerTask task;

    /** The slayer task assigned amount. */
    private int assigned;

    /** The current slayer task amount. */
    private int amount;

    /** The total tasks assigned. */
    private int totalAssigned;

    /** The total tasks completed. */
    private int totalCompleted;

    /** The total tasks cancelled. */
    private int totalCancelled;

    /** The total points accumulated. */
    private int totalPoints;

    /** The list of all blocked slayer tasks. */
    private List<SlayerTask> blocked = new LinkedList<>();

    /** The Set of all unlockable slayer perks. */
    private Set<SlayerUnlockable> unlocked = new HashSet<>(SlayerUnlockable.values().length);

    /** Constructs a new <code>Slayer<code>. */
    public Slayer(Player player) {
        this.player = player;
    }

    /** Opens the slayer itemcontainer. */
    public void open(SlayerTab tab) {
        SlayerTab.refresh(player, tab);
        player.attributes.set("SLAYER_KEY", tab);
        player.interfaceManager.open(tab.getIdentification());
    }

    /** Assigns a slayer task to the player. */
    public void assign(TaskDifficulty difficulty) {
        if (task != null) {
            player.message("You currently have a task assigned!");
            return;
        }

        SlayerTask toAssign = SlayerTask.assign(player, difficulty);

        if (toAssign == null) {
            player.dialogueFactory.sendNpcChat(6797, "There are no tasks available for you!", "This can be because you do not have a valid", "slayer level or you haven't unlocked any tasks.");
            return;
        }

        int assignment = 0;
        if (toAssign.getDifficulty() == TaskDifficulty.EASY) {
            assignment = Utility.random(35, 75);
        } else if (toAssign.getDifficulty() == TaskDifficulty.MEDIUM) {
            assignment = Utility.random(45, 80);
        } else if (toAssign.getDifficulty() == TaskDifficulty.HARD) {
            assignment = Utility.random(60, 100);
        } else if (toAssign.getDifficulty() == TaskDifficulty.BOSS) {
            assignment = Utility.random(30, 50);
        }

        totalAssigned++;
        task = toAssign;
        amount = assignment;
        SlayerTab.refresh(player, player.attributes.get("SLAYER_KEY", SlayerTab.class));
        player.message("You have been assigned " + amount + "x " + task.getName() + ".");
    }

    /** Cancel's the current assigned slayer task. */
    public boolean cancel(boolean requiresCost) {
        if (task == null) {
            player.message("You do not have a task to cancel.");
            return false;
        }

        if (requiresCost) {
            int cost = PlayerRight.isDonator(player) ? 20 : 25;
            if (points < cost) {
                player.message("You need " + cost + " slayer points to cancel a task.");
                return false;
            }
            points -= cost;
        }
        task = null;
        amount = 0;
        totalCancelled++;
        SlayerTab.refresh(player, player.attributes.get("SLAYER_KEY", SlayerTab.class));
        player.message("Your task has been cancelled.");
        return true;
    }

    /** Blocks the current assigned slayer task. */
    public void block() {
        if (task == null) {
            player.message("You do not have a task to block.");
            return;
        }

        if (points < 100) {
            player.message("You need 100 slayer points to block a task.");
            return;
        }

        if (blocked.size() >= 5) {
            player.message("You can only block up to 5 tasks.");
            return;
        }

        if (blocked.contains(task)) {
            player.message("This task is already blocked... but how did you get it again? mmm...");
            return;
        }

        blocked.add(task);
        points -= 100;
        task = null;
        amount = 0;
        totalCancelled++;
        SlayerTab.refresh(player, player.attributes.get("SLAYER_KEY", SlayerTab.class));
        player.message("Your task has been blocked.");
    }

    /** Unblocks the slayer task. */
    public void unblock(int index) {
        if (!blocked.isEmpty() && index < blocked.size()) {
            SlayerTask task = blocked.get(index);
            blocked.remove(task);
            SlayerTab.refresh(player, player.attributes.get("SLAYER_KEY", SlayerTab.class));
            player.message("You have unblocked the task " + task.getName() + ".");
            return;
        }
        player.message("There is no task to unblock.");
    }

    /** Activates killing a slayer npc. */
    public void activate(Npc npc, int killAmount) {
        if (task != null && task.valid(npc.getName())) {
            amount -= killAmount;
            if (amount <= 0) {
                int rewardPoints = SlayerTask.getPoints(task.getDifficulty());
                points += rewardPoints;
                player.skills.addExperience(Skill.SLAYER, SlayerTask.getCompletionExperience(task.getDifficulty()));
                task = null;
                amount = 0;
                totalCompleted++;
                player.message("Congratulations, you have completed your assigned task! You have earned " + rewardPoints + " slayer points!");
                AchievementHandler.activate(player, AchievementKey.SLAYER_TASKS);
                return;
            }
            player.skills.addExperience(Skill.SLAYER, npc.getMaximumHealth() * Config.SLAYER_MODIFICATION);
        }
    }

    /** Opens the confirm itemcontainer for purchasing a perk. */
    public void confirm(int button) {
        int index = Math.abs((-18625 - button) / 6);
        if (!SlayerUnlockable.get(index).isPresent())
            return;
        SlayerUnlockable unlockable = SlayerUnlockable.get(index).get();
        player.attributes.set("SLAYER_CONFIRM_KEY", unlockable);
        open(SlayerTab.CONFIRM);
    }

    /** Handles purchasing a slayer perk. */
    public void purchase() {
        SlayerUnlockable unlockable = player.attributes.get("SLAYER_CONFIRM_KEY", SlayerUnlockable.class);

        if (points < unlockable.getCost()) {
            open(SlayerTab.UNLOCK);
            player.message("You do not have enough points to purchase this!");
            return;
        }

        if (unlocked.contains(unlockable)) {
            open(SlayerTab.UNLOCK);
            player.message("You have already activated this perk!");
            return;
        }

        points -= unlockable.getCost();
        unlocked.add(unlockable);
        player.message("You have purchased the " + unlockable.getName() + " Slayer perk.");
        open(SlayerTab.UNLOCK);
    }

    private final static StoreItem[] STORE_ITEMS = {
            new StoreItem(4155, 1, 0),
            new StoreItem(11941, 1, 150),
            new StoreItem(11866, 1, 75),
            new StoreItem(4166, 1, 5),
            new StoreItem(4168, 1, 5),
            new StoreItem(4164, 1, 5),
            new StoreItem(4551, 1, 5),
            new StoreItem(8901, 1, 200),
            new StoreItem(10551, 1, 200),
            new StoreItem(11738, 1, 25),
            new StoreItem(13116, 1, 200),
            new StoreItem(25781, 1, 200)
    };

    static Item[] ITEMS = new Item[STORE_ITEMS.length];

    static {
        int index = 0;
        for (StoreItem storeItem : STORE_ITEMS) {
            ITEMS[index] = new Item(storeItem.getId(), storeItem.getAmount());
            index++;
        }
    }

    /** Handles purchasing items from the slayer shop. */
    public void store(int slot, int amount, boolean value) {
        if (slot < 0 && slot > STORE_ITEMS.length) {
            return;
        }

        StoreItem item = STORE_ITEMS[slot];
        int cost = item.getShopValue();

        if (value) {
            String price = cost == 0 ? "is free!" : "costs " + Utility.formatDigits(cost) + " slayer points.";
            player.message(item.getName() + " " + price);
            return;
        }

        if (player.inventory.remaining() == 0) {
            player.message("You do not have enough inventory space to buy that.");
            return;
        }

        if (!item.isStackable()) {
            if (amount > player.inventory.remaining()) {
                amount = player.inventory.remaining();
            }
        } else {
            amount *= item.getAmount();
        }

        int price = cost * amount;

        if (getPoints() < price) {
            player.message("You do not have enough slayer points to make this purchase!");
            return;
        }

        item.setAmount(amount);
        points -= price;
        player.inventory.add(item);
        player.send(new SendString(Utility.formatDigits(points) + "\\nPoints", 46714));
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public SlayerTask getTask() {
        return task;
    }

    public void setTask(SlayerTask task) {
        this.task = task;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAssigned() {
        return assigned;
    }

    public void setAssigned(int assigned) {
        this.assigned = assigned;
    }

    public int getTotalAssigned() {
        return totalAssigned;
    }

    public void setTotalAssigned(int totalAssigned) {
        this.totalAssigned = totalAssigned;
    }

    public int getTotalCompleted() {
        return totalCompleted;
    }

    public void setTotalCompleted(int totalCompleted) {
        this.totalCompleted = totalCompleted;
    }

    public int getTotalCancelled() {
        return totalCancelled;
    }

    public void setTotalCancelled(int totalCancelled) {
        this.totalCancelled = totalCancelled;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public List<SlayerTask> getBlocked() {
        return blocked;
    }

    public void setBlocked(List<SlayerTask> blocked) {
        this.blocked = blocked;
    }

    public Set<SlayerUnlockable> getUnlocked() {
        return unlocked;
    }

    public void setUnlocked(Set<SlayerUnlockable> unlocked) {
        this.unlocked = unlocked;
    }
}
