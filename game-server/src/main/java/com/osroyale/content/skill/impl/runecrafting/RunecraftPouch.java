package com.osroyale.content.skill.impl.runecrafting;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;

/**
 * The runecraft pouch.
 *
 * @author Daniel
 */
public class RunecraftPouch {
    /** The player instance. */
    private final Player player;

    /** Constructs a new <code>RunecraftPouch</code>. */
    public RunecraftPouch(Player player) {
        this.player = player;
    }

    /** Deposits essence into the player's runecraft pouch. */
    public void deposit(RunecraftPouchData pouch) {
        if (player.skills.getMaxLevel(Skill.RUNECRAFTING) < pouch.level) {
            player.message("You need a runecrafting level of " + pouch.level + " to use this pouch!");
            return;
        }

        int inventory = player.inventory.computeAmountForId(7936);

        if (inventory == 0) {
            player.message("You have no rune essences in your inventory to fill this pouch!");
            return;
        }

        int essence = pouch.deposit(player, pouch.capacity);

        System.out.println(essence);


        if (essence != 0) {
            player.inventory.remove(7936, essence);
            player.message("You have filled your pouch with " + essence + " rune essences!");
        }
    }

    /** Withdraws essence from the player's rune essence pouch. */
    public void withdraw(RunecraftPouchData pouch) {
        int essence = pouch.withdraw(player);

        if (essence != 0) {
            player.inventory.add(7936, essence);
            player.message("You have emptied your pouch.");
        }
    }

    /** Handles player dying with a rune pouch. */
    public boolean death(Item item) {
        RunecraftPouchData pouch = RunecraftPouchData.forItem(item.getId());

        if (pouch == null)
            return false;

        pouch.empty(player);
        return true;
    }
}
