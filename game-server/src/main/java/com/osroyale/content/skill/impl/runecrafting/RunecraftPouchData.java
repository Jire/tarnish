package com.osroyale.content.skill.impl.runecrafting;

import com.osroyale.game.world.entity.mob.player.Player;

/**
 * Holds the runecrafting pouch data.
 *
 * @author Daniel
 */
public enum RunecraftPouchData {
    SMALL_POUCH(5509, 1, 3, -1) {
        @Override
        public int deposit(Player player, int amount) {
            if (player.smallPouch >= capacity) {
                player.message("Your small pouch is already filled to it's capacity!");
                return 0;
            }

            int essence = player.inventory.computeAmountForId(7936);

            if (amount > essence)
                amount = essence;

            if (player.smallPouch + amount > capacity)
                amount = capacity - player.smallPouch;

            return player.smallPouch += amount;
        }

        @Override
        public int withdraw(Player player) {
            if (player.smallPouch == 0) {
                player.message("You have no rune essences store in your small pouch!");
                return 0;
            }
            if (player.inventory.getFreeSlots() < player.smallPouch){
                player.message("Please free up some inventory spaces before doing this!");
                return 0;
            }
            int amount = player.smallPouch;
            empty(player);
            return amount;
        }

        @Override
        public void empty(Player player) {
            player.smallPouch = 0;
        }
    },
    MEDIUM_POUCH(5510, 25, 6, 9) {
        public int deposit(Player player, int amount) {
            if (player.mediumPouch >= capacity) {
                player.message("Your medium pouch is already filled to it's capacity!");
                return 0;
            }

            int essence = player.inventory.computeAmountForId(7936);

            if (amount > essence)
                amount = essence;

            if (player.mediumPouch + amount > capacity)
                amount = capacity - player.mediumPouch;

            return player.mediumPouch += amount;
        }

        @Override
        public int withdraw(Player player) {
            if (player.mediumPouch == 0) {
                player.message("You have no rune essences store in your medium pouch!");
                return 0;
            }
            if (player.inventory.getFreeSlots() < player.mediumPouch){
                player.message("Please free up some inventory spaces before doing this!");
                return 0;
            }
            int amount = player.mediumPouch;
            empty(player);
            return amount;
        }

        @Override
        public void empty(Player player) {
            player.mediumPouch = 0;
        }
    },
    LARGE_POUCH(5512, 50, 9, 18) {
        public int deposit(Player player, int amount) {
            if (player.largePouch >= capacity) {
                player.message("Your large pouch is already filled to it's capacity!");
                return 0;
            }

            int essence = player.inventory.computeAmountForId(7936);

            if (amount > essence)
                amount = essence;

            if (player.largePouch + amount > capacity)
                amount = capacity - player.largePouch;

            return player.largePouch += amount;
        }

        @Override
        public int withdraw(Player player) {
            if (player.largePouch == 0) {
                player.message("You have no rune essences store in your large pouch!");
                return 0;
            }
            if (player.inventory.getFreeSlots() < player.largePouch){
                player.message("Please free up some inventory spaces before doing this!");
                return 0;
            }
            int amount = player.largePouch;
            empty(player);
            return amount;
        }

        @Override
        public void empty(Player player) {
            player.largePouch = 0;
        }
    },
    GIANT_POUCH(5514, 75, 12, 50) {
        public int deposit(Player player, int amount) {
            if (player.giantPouch >= capacity) {
                player.message("Your giant pouch is already filled to it's capacity!");
                return 0;
            }

            int essence = player.inventory.computeAmountForId(7936);

            if (amount > essence)
                amount = essence;

            if (player.giantPouch + amount > capacity)
                amount = capacity - player.giantPouch;

            return player.giantPouch += amount;
        }

        @Override
        public int withdraw(Player player) {
            if (player.giantPouch == 0) {
                player.message("You have no rune essences store in your giant pouch!");
                return 0;
            }
            if (player.inventory.getFreeSlots() < player.giantPouch){
                player.message("Please free up some inventory spaces before doing this!");
                return 0;
            }
            int amount = player.giantPouch;
            empty(player);
            return amount;
        }

        @Override
        public void empty(Player player) {
            player.giantPouch = 0;
        }
    };

    /** The item identification of the runecrafting pouch. */
    public final int item;

    /** The level required to use the runecrafting pouch. */
    public final int level;

    /** The capacity that the runecrafting pouch can hold. */
    public final int capacity;

    /** The amount of uses before the runecrafting pouch will decay. */
    public final int decay;

    /** What happens to the runecrafting pouch when a player fills it. */
    public abstract int deposit(Player player, int amount);

    /** What happens to the runecrafting pouch when a player empties it. */
    public abstract int withdraw(Player player);

    /** Empties the runecrafting pouch. */
    public abstract void empty(Player player);

    /** Constructs a new <code>RunecraftPouchData</code>. */
    RunecraftPouchData(int item, int level, int capacity, int decay) {
        this.item = item;
        this.level = level;
        this.capacity = capacity;
        this.decay = decay;
    }

    /** Gets the runecrafting pouch data based on the item identification. */
    public static RunecraftPouchData forItem(int item) {
        for (RunecraftPouchData pouch : values()) {
            if (pouch.item == item)
                return pouch;
        }
        return null;
    }
}
