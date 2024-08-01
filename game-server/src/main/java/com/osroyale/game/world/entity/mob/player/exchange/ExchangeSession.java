package com.osroyale.game.world.entity.mob.player.exchange;

import com.google.common.collect.ImmutableSet;
import com.osroyale.content.activity.ActivityType;
import com.osroyale.content.gambling.GambleStage;
import com.osroyale.game.task.impl.DuelNotificationTask;
import com.osroyale.game.task.impl.SessionRemovalNotificationTask;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.containers.ItemContainer;
import com.osroyale.game.world.items.containers.inventory.Inventory;
import com.osroyale.game.world.position.Position;

import java.util.*;
import java.util.function.Consumer;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 23-1-2017.
 */
public abstract class ExchangeSession {

    /** The collection of sessions */
    public static final Set<ExchangeSession> SESSIONS = new HashSet<>();

    /** The controller of this session. */
    public final Player player;

    /** The other player in this session. */
    public final Player other;

    /** The type of this exchange session. */
    public final ExchangeSessionType type;

    /** The items which are in this exchange session. */
    public final Map<Player, ItemContainer> item_containers = new HashMap<>();

    /**
     * The attachment to the session stage, this will more than likely be a player object
     * that will be attached when they have confirmed a certain stage.
     */
    private Object attachment;

    /** Constructs a new {@link ExchangeSession}. */
    public ExchangeSession(Player player, Player other, ExchangeSessionType type) {
        this.player = player;
        this.other = other;
        this.type = type;
        forEach(p -> item_containers.put(p, new ItemContainer(28, ItemContainer.StackPolicy.STANDARD)));
    }

    /** Determines if the player can offer or withdraw items. */
    public boolean canOffer = true;

    /** Determines if this exchange has been finalized. */
    public boolean finalized;

    /** The method invoked when a player requests the {@code other} player. */
    public abstract boolean onRequest();

    /** The method invoked when a PLAYER accepts a certain exchange component. */
    public abstract void accept(Player player, String COMPONENT);

    /** Checks if the item can be added to the container. */
    public abstract boolean canAddItem(Player player, Item item, int slot);

    /** Checks if the item can be removed from the container. */
    public abstract boolean canRemoveItem(Player player, Item item, int slot);

    /** The method invoked when a button is clicked. */
    public abstract boolean onButtonClick(Player player, int button);

    /** Updates the main components of the itemcontainer. */
    public abstract void updateMainComponents(String component);

    /** Updates the itemcontainer when an item is offered or removed. */
    public abstract void updateOfferComponents();

    /** Any functionality that should be handled when the itemcontainer closes. */
    public abstract void onReset();

    /** Attempts to start an exchange session. */
    public boolean request() {
        if (inAnySession()) {
            player.exchangeSession.reset();
            return false;
        }
      /*  if (PlayerRight.isAdministrator(player) && !PlayerRight.isOwner(player) || !PlayerRight.isDeveloper(player)) {
            player.message("You can not exchange as an administrator.");
            return false;
        }
        if (PlayerRight.isAdministrator(other) && !PlayerRight.isOwner(other) || !PlayerRight.isDeveloper(other)) {
            player.message("You can not exchange an administrator.");
            return false;
        }*/

        if (getSession(other).isPresent() && getSession(other).get().inAnySession()) {
            player.message("This player is currently is a " + type.name + " with another player.");
            return false;
        }
        if (Objects.equals(player, other)) {
            player.message("You cannot " + type.name + " with yourself.");
            return false;
        }

        if(player.getGambling().getStage().equals(GambleStage.PLACING_BET) || player.getGambling().getStage().equals(GambleStage.IN_PROGRESS)) {
            return false;
        }

        if(other.getGambling().getStage().equals(GambleStage.PLACING_BET) || other.getGambling().getStage().equals(GambleStage.IN_PROGRESS)) {
            return false;
        }

        if (PlayerRight.isIronman(player) && !PlayerRight.isOwner(other)) {
            player.message("You can not " + type.name + " as you are an iron man.");
            return false;
        }
        if (PlayerRight.isIronman(other) && !PlayerRight.isOwner(player)) {
            player.message(other.getName() + " can not be " + type.name + "d as they are an iron man.");
            return false;
        }
        if (player.exchangeSession.requested_players.contains(other)) {
            player.message("You have already sent a request to this player.");
            return false;
        }
        if (player.locking.locked()) {
            player.message("You cannot send a " + type.name + " request right now.");
            return false;
        }
        if (other.locking.locked()) {
            player.message(other.getName() + " is currently busy.");
            return false;
        }
        if (player.playerAssistant.busy()) {
            player.message("Please finish what you are doing before you do that.");
            return false;
        }
        if (other.playerAssistant.busy()) {
            player.message(other.getName() + " is currently busy.");
            return false;
        }
        if (player.inActivity(ActivityType.DUEL_ARENA)) {
            player.message("You can not do that whilst in a duel!");
            return false;
        }
        if (other.inActivity(ActivityType.DUEL_ARENA)) {
            other.message("You can not do that whilst in a duel!");
            return false;
        }
        if (!onRequest()) {
            return false;
        }
        if(this.type == ExchangeSessionType.DUEL && !other.getPosition().inLocation(new Position(3311, 3201), new Position(3394, 3287), true)) {
            player.exchangeSession.reset();
            return false;
        }
        return SESSIONS.add(this);
    }

    /** Attempts to deposit an item to the container. */
    public final boolean add(Player player, int slot, int amount) {
        Item invItem = player.inventory.get(slot);
        if (invItem == null) {
            return false;
        }
        if (!Item.valid(invItem) || !player.inventory.contains(invItem.getId())) {
            return false;
        }
        if (!canOffer) {
            return false;
        }
        if (!inSession(player, type)) {
            return false;
        }
        if (!canAddItem(player, invItem, slot)) {
            return false;
        }
        if (!invItem.isTradeable() && !PlayerRight.isOwner(player)) {
            player.message("You can't offer this item.");
            return false;
        }
        Item item = new Item(invItem.getId(), amount);
        int count = player.inventory.computeAmountForId(item.getId());
        if (item.getAmount() > count) {
            item.setAmount(count);
        }
        if (item_containers.get(player).add(item)) {
            player.inventory.remove(item);
            updateOfferComponents();
            return true;
        }
        return false;
    }

    /** Attempts to withdraw an item from the container. */
    public final boolean remove(Player player, int slot, int amount) {
        Item containerItem = this.item_containers.get(player).get(slot);
        if (player == null || containerItem == null) {
            return false;
        }
        if (!Item.valid(containerItem) || !this.item_containers.get(player).contains(containerItem.getId())) {
            return false;
        }
        if (!canOffer) {
            return false;
        }
        if (!inSession(player, type)) {
            return false;
        }
        if (!canRemoveItem(player, containerItem, slot)) {
            return false;
        }
        Item item = new Item(containerItem.getId(), amount == -1 ? this.item_containers.get(player).computeAmountForId(containerItem.getId()) : amount);
        int count = item_containers.get(player).computeAmountForId(item.getId());
        if (item.getAmount() > count) {
            item.setAmount(count);
        }
        if (item_containers.get(player).remove(item)) {
            player.inventory.add(item);
            item_containers.get(player).shift();

            if (this.player.equals(player)) {
                World.schedule(this.type == ExchangeSessionType.DUEL ? new DuelNotificationTask(other) : new SessionRemovalNotificationTask(other));
            } else if (player.equals(other)) {
                World.schedule(this.type == ExchangeSessionType.DUEL ? new DuelNotificationTask(this.player) : new SessionRemovalNotificationTask(this.player));
            }

            updateOfferComponents();
            return true;
        }
        return false;
    }

    /** Assigns an attachment to this stage object */
    public void setAttachment(Object attachment) {
        this.attachment = attachment;
    }

    /** Retrieves the attachment object to this class */
    public Object getAttachment() {
        return attachment;
    }

    /** Determines if the trade stage has an attachment */
    public boolean hasAttachment() {
        return Objects.nonNull(attachment);
    }

    /** Determines if the player is a session with {@code other} which matches the {@code type}. */
    public static boolean inSession(Player player, Player other, ExchangeSessionType type) {
        return SESSIONS.stream().anyMatch(session -> allMatch(session, player.getName(), other.getName()) && session.type.equals(type));
    }

    /** Determines if the player is a session which matches the {@code type}. */
    public static boolean inSession(Player player, ExchangeSessionType type) {
        return getSession(player, type).isPresent();
    }

    /** Determines if the player is <b>any</b> session. */
    public boolean inAnySession() {
        return getSession(player).isPresent();
    }

    /** Deposites all items to the exchange session. */
    public void depositeAll(Player player) {
        Inventory inventory = player.inventory;
        if (inventory.isEmpty()) {
            player.message("There is nothing in your inventory to deposit!");
            return;
        }

        List<Item> transfer = new ArrayList<>();
        for (Item item : inventory.getItems()) {
            if (item == null || !item.isTradeable()) {
                continue;
            }

            transfer.add(item);
        }

        this.item_containers.get(player).addAll(transfer);
        inventory.removeAll(transfer);

        inventory.refresh();
        updateOfferComponents();
    }

    /** Withdraws all items from the exchange session. */
    protected void withdrawAll(Player player) {
        ItemContainer container = this.item_containers.get(player);

        if (container.isEmpty()) {
            player.message("There is nothing in this container to withdraw!");
            return;
        }
        player.inventory.addAll(container);
        container.clear();
        updateOfferComponents();
    }

    /** Finalizes the exchange session procedure for the specified {@code player} in a session. */
    protected void finalize(ExchangeCompletionType type) {
        final Optional<ExchangeSession> session = getSession(player);

        if (!session.isPresent() || session.get().finalized) {
            return;
        }

        session.get().finalized = true;

        switch (type) {
            case RESTORE:
                session.get().forEach(player -> {
                    ItemContainer items = item_containers.get(player);
                    items.forEach(player.inventory::add);
                    items.clear();
                });
                break;
            case DISPOSE:
                session.get().forEach(s -> item_containers.get(s).clear());
                break;
            case HALT:
                //nothing happens when halted, the session only gets finalized.
                break;
        }
        SESSIONS.remove(this);
        onReset();
    }

    /**
     * Gets the session if applicable the player is in.
     *
     * @param player the player to get the session for.
     * @return the session wrapped in an optional.
     */
    public static Optional<ExchangeSession> getSession(Player player) {
        return SESSIONS.stream().filter(s -> anyMatch(s, player.getName())).findAny();
    }

    /**
     * Gets the session if applicable the player is in.
     *
     * @param player the player to get the session for.
     * @return the session wrapped in an optional.
     */
    public static Optional<ExchangeSession> getSession(Player player, ExchangeSessionType type) {
        return SESSIONS.stream().filter(s -> s.type.equals(type) && anyMatch(s, player.getName())).findAny();
    }

    /**
     * Gets the session if applicable the player is in.
     *
     * @param player the player to get the session for.
     * @return the session wrapped in an optional.
     */
    public static Optional<ExchangeSession> getSession(Player player, Player other) {
        return SESSIONS.stream().filter(s -> allMatch(s, player.getName(), other.getName())).findAny();
    }

    /**
     * Gets the session if applicable the player is in.
     *
     * @param player the player to get the session for.
     * @return the session wrapped in an optional.
     */
    public static Optional<ExchangeSession> getSession(Player player, Player other, ExchangeSessionType type) {
        return SESSIONS.stream().filter(s -> allMatch(s, player.getName(), other.getName()) && s.type.equals(type)).findAny();
    }

    /**
     * Executes the specified {@code action} for every player in this session.
     *
     * @param action the action to execute.
     */
    public void forEach(Consumer<Player> action) {
        ImmutableSet.of(player, other).forEach(action);
    }

    /**
     * Gets the other player in the exchange session.
     *
     * @param p the player you <b>don't</b> want to get.
     * @return the other player.
     */
    public Player getOther(Player p) {
        return p.getName().equals(player.getName()) ? this.other : this.player;
    }

    private static boolean anyMatch(ExchangeSession session, String name) {
        return Arrays.stream(new String[]{session.player.getName(), session.other.getName()}).anyMatch(n -> n.equals(name));
    }

    private static boolean allMatch(ExchangeSession session, String name, String otherName) {
        return Arrays.stream(new String[]{session.player.getName(), session.other.getName()}).anyMatch(n -> n.equals(name) || n.equals(otherName));
    }
}
