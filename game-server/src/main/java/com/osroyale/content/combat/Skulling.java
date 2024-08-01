package com.osroyale.content.combat;

import com.osroyale.Config;
import com.osroyale.content.lms.LMSGame;
import com.osroyale.game.task.impl.SkullRemoveTask;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.UpdateFlag;
import com.osroyale.game.world.entity.mob.player.Player;

import java.util.LinkedList;
import java.util.List;

/**
 * Handles the skulling class.
 *
 * @author Daniel
 */
public class Skulling {

    /** All the other player's this entity has attacked. */
    private final List<String> attacked = new LinkedList<>();

    /** The player of this class. */
    private Player player;

    /** Their skull icon identification */
    private SkullHeadIconType icon = SkullHeadIconType.NO_SKULL;

    private SkullRemoveTask skullRemoveTask;

    /**
     * Constructs a new <code>Skulling<code> class.
     *
     * @param player The player instance.
     */
    public Skulling(Player player) {
        this.player = player;
        this.skullRemoveTask = new SkullRemoveTask(player);
    }

    /**
     * Checks if player requires a skull upon attacking another player.
     *
     * @param opponent The opponent.
     */
    public void checkForSkulling(Player opponent) {
    /*    if (Area.inEventArena(opponent)) {
            return;
        }*/
        if (!attacked.contains(opponent.getName()) && !opponent.skulling.attacked.contains(player.getName())) {
            skull(opponent, SkullHeadIconType.WHITE_SKULL);
        }
    }

    public void skull() {
        skull(player, SkullHeadIconType.WHITE_SKULL);
    }

    public void skull(SkullHeadIconType icon) {
        skull(player, icon);
    }

    /**
     * Skulls the player and deposit's the attacked player into the list.
     *
     * @param attacking
     */
    public void skull(Player attacking, SkullHeadIconType icon) {
        if (attacking.inActivity()) {
            return;
        }

        if(LMSGame.inGameArea(attacking)) return;

        if (attacking != player) {
            attacked.add(attacking.getName());
        }

        this.icon = icon;
        player.updateFlags.add(UpdateFlag.APPEARANCE);

        if (!skullRemoveTask.isRunning()) {
            skullRemoveTask.setSkullTime(skullRemoveTask.getSkullTime() <= 0 ? Config.SKULL_TIME : skullRemoveTask.getSkullTime());
            World.schedule(skullRemoveTask);
        }
    }

    public boolean isSkulled() {
        return skullRemoveTask.isRunning();
    }

    /**
     * Unskulls the player.
     */
    public void unskull() {
        if (!isSkulled()) {
            return;
        }

        skullRemoveTask.setSkullTime(0);
        attacked.clear();
        icon = SkullHeadIconType.NO_SKULL;
        player.updateFlags.add(UpdateFlag.APPEARANCE);
    }

    public SkullRemoveTask getSkullRemoveTask() {
         return skullRemoveTask;
    }

    /**
     * Gets the skull icon.
     *
     * @return The skull icon.
     */
    public SkullHeadIconType getHeadIconType() {
        return icon;
    }

}
