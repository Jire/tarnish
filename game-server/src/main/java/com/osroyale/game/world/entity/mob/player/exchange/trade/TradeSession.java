package com.osroyale.game.world.entity.mob.player.exchange.trade;

import com.osroyale.game.event.impl.log.TradeLogEvent;
import com.osroyale.game.world.InterfaceConstants;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.entity.mob.player.exchange.ExchangeCompletionType;
import com.osroyale.game.world.entity.mob.player.exchange.ExchangeSession;
import com.osroyale.game.world.entity.mob.player.exchange.ExchangeSessionType;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.containers.pricechecker.PriceType;
import com.osroyale.net.packet.out.SendItemOnInterface;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.net.packet.out.SendMinimapState;
import com.osroyale.net.packet.out.SendString;
import com.osroyale.util.MessageColor;
import com.osroyale.util.Stopwatch;
import com.osroyale.util.Utility;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 25-1-2017.
 */
public final class TradeSession extends ExchangeSession {

    /**
     * The stopwatch which blocks the player from further continueing the
     * session if a second hasn't passed after the last item has been added.
     */
    private final Stopwatch lastOfferModification = Stopwatch.start();

    /**
     * Constructs a new {@link ExchangeSession}.
     *
     * @param player the player this exchange session is for.
     * @param other  the other player in this exchange session.
     */
    public TradeSession(Player player, Player other) {
        super(player, other, ExchangeSessionType.TRADE);
    }

    @Override
    public boolean onRequest() {
        if(player.playTime < 6000) {
            player.send(new SendMessage("You must have over one hour of play time to trade."));
            return false;
        }
        if(player.getUsername().contains("nicholas") || player.getUsername().contains("muntuna")) {
            player.send(new SendMessage("You cannot trade as a " +player.getPlayer().right));
            return false;
        }
        this.player.face(other.getPosition());
        this.player.exchangeSession.requested_players.add(other);
        if (!other.exchangeSession.requested_players.contains(player)) {
            player.send(new SendMessage("Sending trade request..."));
            other.send(new SendMessage(player.getName() + ":tradereq:", MessageColor.PURPLE));
            return false;
        }
        SESSIONS.add(this);
        this.forEach(p -> {
            p.exchangeSession.resetRequests();
            p.send(new SendMinimapState(SendMinimapState.MinimapState.UNCLICKABLE));
            p.attributes.set("TRADE_KEY", true);
        });
        updateMainComponents("FIRST_SCREEN");
        return true;
    }

    @Override
    public boolean canAddItem(Player player, Item item, int slot) {
        return true;
    }

    @Override
    public boolean canRemoveItem(Player player, Item item, int slot) {
        return true;
    }

    @Override
    public boolean onButtonClick(Player p, int button) {
        switch (button) {

            case -32513:
                if (!player.interfaceManager.isInterfaceOpen(InterfaceConstants.FIRST_TRADE_SCREEN)) {
                    return false;
                }

                depositeAll(p);
                return true;

            case -32510:
                if (!player.interfaceManager.isInterfaceOpen(InterfaceConstants.FIRST_TRADE_SCREEN)) {
                    return false;
                }

                withdrawAll(p);
                return true;

            case -32524:
            case -32530:
            case -32324:
            case -32332:
                p.exchangeSession.reset(ExchangeSessionType.TRADE);
                return true;

            case -32527:
                if (!player.interfaceManager.isInterfaceOpen(InterfaceConstants.FIRST_TRADE_SCREEN)) {
                    return false;
                }

                accept(p, "OFFER_ITEMS");
                return true;

            case -32327:
                if (!player.interfaceManager.isInterfaceOpen(InterfaceConstants.SECOND_TRADE_SCREEN)) {
                    return false;
                }

                accept(p, "CONFIRM_DECISION");
                return true;

        }
        return false;
    }

    @Override
    public void accept(Player player, String component) {
        Player other = getOther(player);
        switch (component) {
            case "OFFER_ITEMS":
                if (!lastOfferModification.elapsed(1_000)) {
                    player.send(new SendString("@red@Trade has been modified!", 33030));
                    return;
                }
                if (!player.inventory.hasCapacityFor(item_containers.get(other).toArray())) {
                    player.send(new SendMessage("You don't have enough free slots for this many items.", MessageColor.RED));
                    break;
                }

                if (!other.inventory.hasCapacityFor(item_containers.get(player).toArray())) {
                    String username = other.getName();
                    player.send(new SendMessage(username + " doesn't have enough free slots for this many items", MessageColor.RED));
                    break;
                }
                if (hasAttachment() && getAttachment() != player) {
                    this.setAttachment(null);
                    updateMainComponents("SECOND_SCREEN");
                    return;
                }
                setAttachment(player);
                player.send(new SendString("Waiting for other player...", 33029));
                other.send(new SendString("Other player has accepted", 33029));
                break;
            case "CONFIRM_DECISION":
                if (hasAttachment() && getAttachment() != player) {
                    this.setAttachment(null);
                    accept(player, "FINALIZE");
                    return;
                }
                setAttachment(player);
                other.send(new SendString("Other player has accepted.", 33202));
                player.send(new SendString("Waiting for other player...", 33202));
                break;

            case "FINALIZE":
                if (other.isRegistered() && player.isRegistered()) {

                    final Item[] playerItems = this.item_containers.get(player).toArray();
                    final Item[] otherItems = this.item_containers.get(other).toArray();

                    player.inventory.addAll(otherItems);
                    other.inventory.addAll(playerItems);

                    World.getDataBus().publish(new TradeLogEvent(player, playerItems, other, otherItems));

                    forEach(p -> p.send(new SendMessage("Trade successfully completed with " + this.getOther(p).getName(), MessageColor.RED)));
                    finalize(ExchangeCompletionType.DISPOSE);
                }
                break;

        }
    }

    @Override
    public void updateMainComponents(String component) {
        switch (component) {
            case "FIRST_SCREEN":
                updateOfferComponents();
                break;
            case "SECOND_SCREEN":
                forEach(p -> {
                    Player recipient = p.getName().equals(player.getName()) ? this.other : this.player;

                    p.send(new SendString("<col=65535>Are you sure you want to make this trade?", 33202));
                    p.send(new SendItemOnInterface(InterfaceConstants.INVENTORY_INTERFACE, p.inventory.toArray()));

                    p.send(new SendString(getItemNames(p, this.item_containers.get(p).toArray()), 33221));
                    p.send(new SendString(getItemNames(recipient, this.item_containers.get(recipient).toArray()), 33251));
                    p.send(new SendString("<col=65535>Trading With:", 33207));
                    p.send(new SendString(String.format("%s <col=65535>%s", PlayerRight.getCrown(recipient), Utility.formatName(recipient.getName())), 33208));
                    p.interfaceManager.openInventory(InterfaceConstants.SECOND_TRADE_SCREEN, 3213);
                });
                break;
        }
    }

    @Override
    public void updateOfferComponents() {
        this.lastOfferModification.reset();
        this.setAttachment(null);
        forEach(p -> {
            Player recipient = p.getName().equals(player.getName()) ? this.other : this.player;
            int remaining = recipient.inventory.remaining();

            this.item_containers.get(p).refresh(p, InterfaceConstants.PLAYER_TRADE_CONTAINER);
            this.item_containers.get(p).refresh(recipient, InterfaceConstants.OTHER_TRADE_CONTAINER);

            p.send(new SendItemOnInterface(3322, p.inventory.toArray()));
            p.send(new SendString(String.format("Trading with: %s %s", PlayerRight.getCrown(recipient), Utility.formatName(recipient.getName())), 33002));
            p.send(new SendString(Utility.formatName(recipient.getName()), 33003));
            p.send(new SendString("has " + remaining + " free", 33004));
            p.send(new SendString("inventory spaces", 33005));

            // p = current player being looped
            // recipient = the other player (not the player being looped)
            // don't use player and other object because those are the cached values.
//            long difference = this.item_containers.get(p).containerValue(PriceType.VALUE)  - this.item_containers.get(recipient).containerValue(PriceType.VALUE);
//            System.out.println("difference = " + difference);FIXME
//            p.send(new SendString(difference == 0 ? "<col=ffffff> Absolutely nothing!" : "<col=ffffff> " + difference, 33018));

            p.send(new SendString(this.item_containers.get(p).containerValue(PriceType.VALUE) == 0 ? "Nothing." : Utility.formatDigits(this.item_containers.get(p).containerValue(PriceType.VALUE)) + " gp", 33019));
            p.send(new SendString(this.item_containers.get(recipient).containerValue(PriceType.VALUE) == 0 ? "Nothing." : Utility.formatDigits(this.item_containers.get(recipient).containerValue(PriceType.VALUE)) + " gp", 33020));

            p.send(new SendString("", 33029));
            p.send(new SendString("", 33030));

            p.interfaceManager.openInventory(InterfaceConstants.FIRST_TRADE_SCREEN, 3321);
        });
    }

    @Override
    public void onReset() {
        forEach(p -> {
            p.attributes.set("TRADE_KEY", false);
            p.interfaceManager.close();
            p.resetFace();
            p.send(new SendMinimapState(SendMinimapState.MinimapState.NORMAL));
        });
    }

    /**
     * Determines and returns the text for {@code items} that will be displayed
     * on the confirm trade screen.
     */
    private String getItemNames(Player player, Item[] items) {
        String tradeItems = "Absolutely nothing!";
        String tradeAmount;
        int count = 0;
        for (Item item : items) {
            if (item == null || tradeItems.contains(item.getName())) {
                continue;
            }
            int amount = this.item_containers.get(player).computeAmountForId(item.getId());
            tradeAmount = item.isStackable() ? amount >= 1000 && amount < 1000000 ? "@cya@" + (amount / 1000) + "K @whi@" + "(" + amount + ")" : amount >= 1000000 ? "@gre@" + (amount / 1000000) + " " + "million @whi@(" + amount + ")" : "" + amount : "(x" + amount + ")";
            tradeItems = count == 0 ? item.getName() : tradeItems + "\\n" + item.getName();
            tradeItems = tradeItems + (item.isStackable() ? " x " : " ") + tradeAmount;
            count++;
        }
        return tradeItems;
    }
}
