package com.osroyale.content.gambling;

import com.osroyale.content.gambling.impl.FiftyFive;
import com.osroyale.content.gambling.impl.FlowerPoker;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.Direction;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.ItemDefinition;
import com.osroyale.game.world.items.containers.ItemContainer;
import com.osroyale.game.world.object.CustomGameObject;
import com.osroyale.game.world.pathfinding.TraversalMap;
import com.osroyale.game.world.position.Boundary;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.packet.out.*;
import com.osroyale.util.Utility;

import java.util.ArrayList;
import java.util.Objects;

public class GambleManager {

    /**
     * The main gambling interface
     */
    private final int INTERFACE_ID = 44750;
    /**
     * Interface id for the player's inventory
     */
    private final int INVENTORY_ID = 44775;
    /**
     * The config used for selecting the game type
     */
    private final int CONFIG = 444;

    public static final Boundary GAMBLING_ZONE = new Boundary(3148, 3476, 3181, 3505);

    /**
     * The current stage of the gamble
     */
    protected GambleStage stage = GambleStage.NONE;

    public GambleStage getStage() {
        return stage;
    }

    public void setStage(GambleStage stage) {
        this.stage = stage;
    }

    /**
     * The other player within this gamble
     */
    protected Player other;

    public Player getOther() {
        return other;
    }

    public void setOther(Player other) {
        this.other = other;
    }

    /**
     * Checks if the player has confirmed the gamble
     */
    protected boolean confirmed;

    public boolean hasConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    /**
     * The gamble type going on
     */
    protected GambleType type = GambleType.NONE;

    public GambleType getType() {
        return type;
    }

    public void setType(GambleType type) {
        this.type = type;
    }

    /**
     * The current gamble going on between the two players
     */
    protected Gamble game;

    public Gamble getGame() {
        return game;
    }

    public void setGame(Gamble game) {
        this.game = game;
    }

    /**
     * The container with gambled items of the player
     */
    protected ItemContainer container = new ItemContainer(18, ItemContainer.StackPolicy.STANDARD);

    public ItemContainer getContainer() {
        return container;
    }

    protected ArrayList<CustomGameObject> gameFlowers = new ArrayList<CustomGameObject>();

    public ArrayList<CustomGameObject> getGameFlowers() {
        return gameFlowers;
    }


    protected ArrayList<Flowers> flowers = new ArrayList<Flowers>();

    public ArrayList<Flowers> getFlowers() {
        return flowers;
    }

    /**
     * Checks if the player is allowed to gamble or not
     * @param player
     * @param requiredStage
     * @return
     */
    public boolean canGamble(Player player, GambleStage requiredStage) {

        if(!Boundary.isIn(player, GAMBLING_ZONE)) return false;

        if (Objects.nonNull(requiredStage) && !player.getGambling().getStage().equals(requiredStage)) return false;

        return true;
    }

    /**
     * Handles sending a request to another player
     * @param player
     * @param other
     */
    public void sendRequest(Player player, Player other) {

        if (PlayerRight.isIronman(player)) {
            player.message("You can not gamble as you are an iron man.");
            return;
        }

        if (PlayerRight.isIronman(other)) {
            player.message(other.getName() + " can not gamble as they are an iron man.");
            return;
        }
        if(player.playTime < 6000) {
            player.message("You must have at least 1 hour of play time to gamble.");
            return;
        }

        Player requested = other.getGambling().getOther();

        if (!Objects.isNull(requested)) {
            System.out.println("Accept...");
            acceptRequest(player, other);
            return;
        }

        player.message("You've sent a gamble invite to " + Utility.capitalizeSentence(other.getName()) + ".");
        other.message(Utility.capitalizeSentence(player.getName()) + ":gamblereq:");
        player.send(new SendRemoveInterface());
        player.getGambling().setOther(other);
        player.getGambling().setStage(GambleStage.SENDING_OFFER);
        System.out.println("send request...");
    }

    /**
     * Handles accepting a request
     * @param player
     * @param other
     */
    public void acceptRequest(Player player, Player other) {
        if(player == other) return;

        if (!canGamble(player, GambleStage.NONE) || !canGamble(other, GambleStage.SENDING_OFFER)) return;

        player.getGambling().setStage(GambleStage.SENDING_OFFER);
        player.getGambling().setOther(other);

        if (other.getGambling().getOther() != null && player.getIndex() == other.getGambling().getOther().getIndex()) {
            System.out.println("accepted request...");
            open(other, player);
            open(player, other);
        }
    }

    /**
     * Handles opening the interface
     * @param player
     * @param other
     */
    public void open(Player player, Player other) {
        player.getGambling().setConfirmed(false);
        player.getGambling().setStage(GambleStage.PLACING_BET);
        System.out.println("Open the interface for ["+String.format(player.getUsername())+"] and ["+String.format(other.getUsername())+"]...");

        player.send(new SendInventoryInterface(INTERFACE_ID, INVENTORY_ID));
        player.send(new SendItemOnInterface(44770, player.getGambling().getContainer().toArray()));
        player.send(new SendItemOnInterface(44771, other.getGambling().getContainer().toArray()));
        player.send(new SendItemOnInterface(INVENTORY_ID + 1, player.inventory.getItems()));

        player.send(new SendString("" + String.format(player.getUsername()), 44768));
        player.send(new SendString("" + String.format(other.getUsername()), 44769));
        player.send(new SendConfig(CONFIG, 0));
    }

    /**
     * Handles accepting the gamble
     * @param player
     */
    public void accept(Player player) {
        Player other = player.getGambling().getOther();

        if (other == null) return;

        if(player.getGambling().getType() == null || player.getGambling().getType().equals(GambleType.NONE)) {
            player.message("You need to select a game first.");
            return;
        }

        if (System.currentTimeMillis() - player.getLastModification() < 5_000) {
            player.message("@red@Something was changed in the last 5 seconds, you cannot accept yet.");
            return;
        }

        if (player.getGambling().getType() != other.getGambling().getType()) return;

        if(player.getGambling().hasConfirmed()) return;

        player.getGambling().setConfirmed(true);

        player.message("You have accepted the gamble with "+String.format(other.getUsername())+".");
        other.message(String.format(player.getUsername()) + " has accepted the gamble.");

        System.out.println("player: " + String.format(player.getUsername()));
        System.out.println("other: " + String.format(other.getUsername()));

        if (other.getGambling().hasConfirmed()) {
            Gamble game = getGame(other, player, other.getGambling().getType());

            player.getGambling().setGame(game);
            other.getGambling().setGame(game);

            player.getGambling().setStage(GambleStage.SENDING_OFFER);

            player.getGambling().setOther(other);

            System.out.println("Both confirmed start the game...");

            start(player);
        }
    }

    /**
     * Checks if the position that was selected is suitable for a game of flower poker
     * @param player
     * @param position
     * @return
     */
    public boolean canPlayFlowerPokerAtPositon(Player player, Position position) {
        //Checks if the spot its checking has a object placed there
        if (!TraversalMap.isTraversable(new Position(position.getX() + 1, position.getY()), Direction.SOUTH, player.width())) return false;

        boolean canPlay = true;
        for(int index = 0; index < 5; index++) {
            if (!TraversalMap.isTraversable(position, Direction.SOUTH, player.width())) {
                canPlay = false;
                break;
            }
            position = position.create(position.getX(), position.getY() + Direction.SOUTH.getDirectionY());
        }
        return canPlay;
    }

    /**
     * Handles declining the gamble
     * @param player
     */
    public void decline(Player player) {
        Player other = player.getGambling().getOther();

        for(Item item : player.getGambling().getContainer()) {
            if(item == null || item.getId() == -1) continue;

            if(player.inventory.hasCapacityFor(item))
                player.inventory.add(item);
            else {
                player.bank.add(item);
                player.message("@red@You had no room for the "+item.getAmount()+" x "+item.getDefinition().getName()+", its been sent to your bank.");
            }
        }

        player.getGambling().getContainer().clear();
        player.inventory.refresh();
        player.bank.refresh();

        if(other != null) {

            for(Item item : other.getGambling().getContainer()) {
                if(item == null || item.getId() == -1) continue;

                if(other.inventory.hasCapacityFor(item))
                    other.inventory.add(item);
                else {
                    other.bank.add(item);
                    other.message("@red@You had no room for the "+item.getAmount()+" x "+item.getDefinition().getName()+", its been sent to your bank.");
                }
            }

            other.getGambling().getContainer().clear();
            other.inventory.refresh();
            other.bank.refresh();

            reset(other);
        }

        reset(player);
    }

    /**
     * Deposit a item into the gamble session
     * @param player
     * @param slot
     */
    public void deposit(Player player, int itemId, int slot, int amount) {
        Player other = player.getGambling().getOther();

        if(other == null) return;

        ItemDefinition def = ItemDefinition.get(itemId);

        if (!def.isTradeable()) {
            player.send(new SendMessage("This is item is untradeable!"));
            return;
        }

        if(!player.inventory.contains(itemId)) return;

        if(amount > player.inventory.computeAmountForId(itemId))
            amount = player.inventory.computeAmountForId(itemId);

        if(amount <= 0) return;

        Item item = new Item(itemId, amount);
        if(player.getGambling().getContainer().hasCapacityFor(item)) {
            player.inventory.remove(item, slot);
            player.getGambling().getContainer().add(item);
            player.inventory.refresh();
            player.inventory.refresh(player, INVENTORY_ID + 1);
        } else
            player.message("Not enough space.");

        player.setLastModification(System.currentTimeMillis());
        other.setLastModification(System.currentTimeMillis());

        player.send(new SendItemOnInterface(44770, player.getGambling().getContainer().toArray()));
        player.send(new SendItemOnInterface(44771, other.getGambling().getContainer().toArray()));

        other.send(new SendItemOnInterface(44770, other.getGambling().getContainer().toArray()));
        other.send(new SendItemOnInterface(44771, player.getGambling().getContainer().toArray()));
    }

    /**
     * Withdraw an item from the gamble session
     * @param player
     * @param slot
     */
    public void withdraw(Player player, int itemId, int slot, int amount) {
        Player other = player.getGambling().getOther();

        if(other == null) return;

        Item clickedItem = player.getGambling().getContainer().get(slot);

        if(clickedItem.getId() != itemId) return;

        if(amount > clickedItem.getAmount())
            amount = clickedItem.getAmount();

        if(!clickedItem.getDefinition().isStackable() && amount > player.inventory.getFreeSlots())
            amount = player.inventory.getFreeSlots();

        if(amount <= 0) return;

        player.getGambling().getContainer().remove(itemId, amount);
        player.inventory.add(itemId, amount);

        player.inventory.refresh();
        player.inventory.refresh(player, INVENTORY_ID + 1);

        player.setLastModification(System.currentTimeMillis());
        other.setLastModification(System.currentTimeMillis());

        player.send(new SendItemOnInterface(44770, player.getGambling().getContainer().toArray()));
        player.send(new SendItemOnInterface(44771, other.getGambling().getContainer().toArray()));

        other.send(new SendItemOnInterface(44770, other.getGambling().getContainer().toArray()));
        other.send(new SendItemOnInterface(44771, player.getGambling().getContainer().toArray()));
    }

    /**
     * Get thet game based on the gamble type
     * @param other
     * @param player
     * @param type
     * @return
     */
    private Gamble getGame(Player other, Player player, GambleType type) {
        switch (type) {
            case FIFTY_FIVE:
                return new FiftyFive(player, other);
            case FLOWER_POKER:
                return new FlowerPoker(player, other);
        }
        return null;
    }

    /**
     * Handles starting the gamble
     * @param player
     */
    private void start(Player player) {
        if (!canGamble(player, GambleStage.SENDING_OFFER)) return;

        Player other = player.getGambling().getOther();

        if (other == null) return;

        if (other.getGambling().hasConfirmed()) {
            if (player.getGambling().getGame() == null) return;

            if (player.getGambling().getGame().getHost() == null)  return;

            player.send(new SendRemoveInterface());
            other.send(new SendRemoveInterface());

            player.getGambling().setStage(GambleStage.IN_PROGRESS);
            other.getGambling().setStage(GambleStage.IN_PROGRESS);

            player.setGambleLock(true);
            other.setGambleLock(true);

            player.getGambling().getGame().gamble();
        }
    }

    /**
     * Handles finishing up a automated gamble
     * @param host
     * @param opponent
     * @param hostScore
     * @param opponentScore
     */
    public void finish(Player host, Player opponent, int hostScore, int opponentScore) {
        if (host.getGambling().getGame() == null)
            return;

        if (host.getGambling().getGame().getHost() == null)
            return;

        if (!World.getPlayers().contains(opponent) && World.getPlayers().contains(host)) {
            give(host.getGambling().getType(), 'H', host, opponent, false);
        } else if (World.getPlayers().contains(opponent) && !World.getPlayers().contains(host)) {
            give(host.getGambling().getType(), 'O', opponent, host, false);
        } else {
            if (hostScore == 55) {
                host.speak("The roll was a draw, rerolling...");
                opponent.speak("The roll was a draw, rerolling...");

                host.getGambling().setStage(GambleStage.IN_PROGRESS);
                opponent.getGambling().setStage(GambleStage.IN_PROGRESS);
                host.getGambling().getGame().gamble();
                return;
            }

            boolean hostWon = hostScore > opponentScore;
            give(host.getGambling().getType(), hostWon ? 'H' : 'O', hostWon ? host : opponent, hostWon ? opponent : host, false);
        }
    }

    /**
     * Handles giving the players the winnings or returning the items when its a draw
     * @param gambleType
     * @param winnerIdentifier
     * @param winner
     * @param loser
     * @param draw
     */
    public void give(GambleType gambleType, char winnerIdentifier, Player winner, Player loser, boolean draw) {

        if(draw) {

            winner.speak("It's a draw!");
            loser.speak("It's a draw!");

            removeFlowers(winner);
            removeFlowers(loser);

            winner.getGambling().setStage(GambleStage.IN_PROGRESS);
            loser.getGambling().setStage(GambleStage.IN_PROGRESS);

            winner.getGambling().getGame().gamble();

            return;
        } else {

            //Handle giving the winner the items
            for(Item item : winner.getGambling().getContainer()) {
                if(item == null || item.getId() == -1) continue;

                if(winner.inventory.hasCapacityFor(item))
                    winner.inventory.add(item);
                else {
                    winner.inventory.addOrDrop(item);
                    winner.bank.refresh();
                }
            }

            for(Item item : loser.getGambling().getContainer()) {
                if(item == null || item.getId() == -1) continue;

                if(winner.inventory.hasCapacityFor(item))
                    winner.inventory.add(item);
                else {
                    winner.inventory.addOrDrop(item);
                    winner.bank.refresh();
                }
            }

            send(gambleType, winnerIdentifier, winner, loser);

        }

        loser.getGambling().container.clear();
        winner.getGambling().container.clear();

        reset(loser);
        reset(winner);
    }

    /**
     * Handles sending the message for who won
     * @param gambleType
     * @param winnerIdentifier
     * @param winner
     * @param loser
     */
    private void send(GambleType gambleType, char winnerIdentifier, Player winner, Player loser) {
        switch(gambleType) {
            case FIFTY_FIVE -> {
                if (winnerIdentifier == 'H')
                    winner.speak("The roll was under 55, I have won the 55x2.");
                else
                    winner.speak("The roll was higher then 55, I have won the 55x2.");
            }
            default -> {
                winner.speak("I have won!");
                loser.speak("I have lost!");
            }
        }
    }

    /**
     * Handles setting the different game types
     * @param player
     * @param type
     */
    public void handleModeSelection(Player player, GambleType type) {
        Player other = player.getGambling().getOther();

        if(other == null) return;

        player.getGambling().setConfirmed(false);
        player.getGambling().setType(type);
        player.send(new SendConfig(CONFIG, type.ordinal()));
        player.setLastModification(System.currentTimeMillis());

        other.getGambling().setConfirmed(false);
        other.getGambling().setType(type);
        other.send(new SendConfig(CONFIG, type.ordinal()));
        other.setLastModification(System.currentTimeMillis());
    }

    /**
     * Handles resetting all the gambling variables for a player
     * @param player
     */
    public void reset(Player player) {

        if(player.getGambling().getStage().equals(GambleStage.NONE))
            return;

        player.getGambling().setConfirmed(false);
        player.getGambling().setGame(null);
        player.getGambling().setType(null);
        player.getGambling().setStage(GambleStage.NONE);
        player.setGambleLock(false);
        player.send(new SendRemoveInterface());
        player.send(new SendConfig(CONFIG, 0));
        removeFlowers(player);

        if(player.getGambling().getOther() != null)
            reset(player.getGambling().getOther());

        player.getGambling().setOther(null);
    }

    public void removeFlowers(Player player) {
        for(CustomGameObject flowers : player.getGambling().getGameFlowers())
            flowers.unregister();

        player.getGambling().getGameFlowers().clear();
        player.getGambling().getFlowers().clear();
    }
}
