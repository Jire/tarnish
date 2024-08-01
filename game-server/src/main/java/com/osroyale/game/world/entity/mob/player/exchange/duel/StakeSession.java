package com.osroyale.game.world.entity.mob.player.exchange.duel;

import com.osroyale.content.activity.Activity;
import com.osroyale.content.activity.impl.duelarena.DuelArenaActivity;
import com.osroyale.content.activity.impl.duelarena.DuelRule;
import com.osroyale.content.activity.impl.duelarena.DuelUtils;
import com.osroyale.game.world.InterfaceConstants;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.entity.mob.player.exchange.ExchangeCompletionType;
import com.osroyale.game.world.entity.mob.player.exchange.ExchangeSession;
import com.osroyale.game.world.entity.mob.player.exchange.ExchangeSessionType;
import com.osroyale.game.world.items.Item;
import com.osroyale.net.packet.out.*;
import com.osroyale.util.MessageColor;
import com.osroyale.util.Utility;

import java.util.EnumSet;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * The exchange session where two players can agree to a stake. <p> The agreed
 * items will be kept in this session until the duel is over.
 *
 * @author nshusa
 */
public final class StakeSession extends ExchangeSession {

    private DuelArenaActivity activity;
    public boolean accepted;

    public StakeSession(Player owner, Player other) {
        super(owner, other, ExchangeSessionType.DUEL);
    }

    @Override
    public boolean onRequest() {
//        if (true) {
//            DuelArenaActivity2.create(this, player, other).cooldown(-1);
//            return true;
//        }
        if(player.playTime < 6000) {
            player.message("You must have at least 1 hour of play time before duelling.");
            return false;
        }
        if(other.playTime < 6000) {
            player.message("The other player must have at least 1 hour of play time before duelling.");
            return false;
        }
        // face player they are requesting to duel
        player.face(other.getPosition());
        player.exchangeSession.requested_players.add(other);

        if (!other.exchangeSession.requested_players.contains(player)) {
            player.send(new SendMessage("Sending duel request..."));
            other.send(new SendMessage(player.getName() + ":duelreq:", MessageColor.BRONZE));
            return false;
        }

        SESSIONS.add(this);

        activity = DuelArenaActivity.create(this, player, other);

        forEach(player -> {
            player.exchangeSession.resetRequests();
            player.send(new SendMinimapState(SendMinimapState.MinimapState.UNCLICKABLE));
            player.send(new SendConfig(655, 0)); // stake removal notification
            // reset duel rule configs
            IntStream.range(631, 644).forEach(i -> player.send(new SendConfig(i, 0)));
            player.send(new SendToggle(286, 0));
            player.attributes.set("DUEL_KEY", true);
        });
        updateMainComponents("FIRST_SCREEN");
        return true;
    }

    @Override
    public void accept(Player player, String component) {
        Player other = getOther(player);
        switch (component) {
            case "FIRST_SCREEN":
                if (!player.inventory.hasCapacityFor(item_containers.get(other).toArray())) {
                    player.send(new SendMessage("You don't have enough free slots for this many items.", MessageColor.RED));
                    break;
                }

                if (!other.inventory.hasCapacityFor(item_containers.get(player).toArray())) {
                    String username = other.getName();
                    player.send(new SendMessage(username + " doesn't have enough free slots for this many items", MessageColor.RED));
                    break;
                }

                if (hasAttachment() && !getAttachment().equals(player)) {
                    setAttachment(null);
                    updateMainComponents("SECOND_SCREEN");
                    return;
                }

                setAttachment(player);
                player.send(new SendString("<col=ffffff>Waiting for other player...", 31009));
                other.send(new SendString("<col=ffffff>Other player has accepted", 31009));
                break;

            case "SECOND_SCREEN":
                if (hasAttachment() && !getAttachment().equals(player)) {
                    setAttachment(null);
                    accepted = true;
                    finalize(ExchangeCompletionType.HALT);
                    activity.setPause(false);
                    return;
                }

                setAttachment(player);
                player.send(new SendString("<col=ffffff>Waiting for other player...", 31526));
                other.send(new SendString("<col=ffffff>Other player has accepted", 31526));
                break;

        }
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
    public boolean onButtonClick(Player player, int button) {
        Optional<DuelRule> rule = DuelRule.forButton(button);
        rule.ifPresent(r -> r.set(player, true));

        switch (button) {
            case 31018:
            case 31008:
            case 31002:
            case 31523:
            case 31502:
                player.exchangeSession.reset(ExchangeSessionType.DUEL);
                return true;

            case 31061: // save
                if (!player.interfaceManager.isInterfaceOpen(InterfaceConstants.FIRST_DUEL_SCREEN)) {
                    return false;
                }

                if (activity != null) {
                    EnumSet<DuelRule> flags = activity.rules.getFlags();
                    player.attributes.put("duel_rules", flags);
                }
                return true;

            case 31065: // load
                if (!player.interfaceManager.isInterfaceOpen(InterfaceConstants.FIRST_DUEL_SCREEN)) {
                    return false;
                }

                if (player.attributes.has("duel_rules") && activity != null) {
                    EnumSet<DuelRule> flags = player.attributes.get("duel_rules");
                    activity.rules.reset();
                    flags.forEach(it -> it.set(player, true));
                    DuelRule.showRules(activity, this.player, other);
                }
                return true;

            case 31520:
                if (!player.interfaceManager.isInterfaceOpen(InterfaceConstants.SECOND_DUEL_SCREEN)) {
                    return false;
                }

                accept(player, "SECOND_SCREEN");
                return true;

            case 31015:
                if (!player.interfaceManager.isInterfaceOpen(InterfaceConstants.FIRST_DUEL_SCREEN)) {
                    return false;
                }

                accept(player, "FIRST_SCREEN");
                return true;

        }
        return false;
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

                    p.send(new SendString("Some worn items will be taken off.", 31505));
                    p.send(new SendString("Boosted stats will be restored.", 31506));
                    p.send(new SendString("Existing prayers will be stopped.", 31507));
                    IntStream.range(31509, 31520).forEach(i -> p.send(new SendString("", i))); // rules
                    showDuelRulesOnSecondScreen(p);
                    IntStream.range(31531, 31560).forEach(i -> p.send(new SendString("", i)));
                    IntStream.range(31561, 31589).forEach(i -> p.send(new SendString("", i)));
                    p.send(new SendString(DuelUtils.getItemNames(this.item_containers.get(p).toArray()), 31531));
                    p.send(new SendString(DuelUtils.getItemNames(this.item_containers.get(recipient).toArray()), 31561));
                    p.send(new SendString("", 31526));
                    p.send(new SendItemOnInterface(3322, this.item_containers.get(p).toArray()));
                    p.interfaceManager.open(InterfaceConstants.SECOND_DUEL_SCREEN);
                });
                break;
        }
    }

    @Override
    public void updateOfferComponents() {
        setAttachment(null);
        forEach(p -> {
            Player recipient = p.getName().equals(player.getName()) ? this.other : this.player;

            p.send(new SendItemOnInterface(3322, p.inventory.toArray()));
            item_containers.get(p).refresh(p, InterfaceConstants.PLAYER_STAKE_CONTAINER);
            item_containers.get(p).refresh(recipient, InterfaceConstants.OTHER_STAKE_CONTAINER);

            p.send(new SendString("Dueling with: " + PlayerRight.getCrown(recipient) + " " + Utility.formatName(recipient.getUsername()), 31005));
            p.send(new SendString("Opponent's combat level: <col=ff7000>" + other.skills.getCombatLevel(), 31006));

            p.send(new SendString("", 31009));
            p.interfaceManager.openInventory(InterfaceConstants.FIRST_DUEL_SCREEN, 3321);
        });
    }

    @Override
    public void onReset() {
        forEach(player -> {
            player.send(new SendMinimapState(SendMinimapState.MinimapState.NORMAL));
            player.resetFace();
            player.attributes.set("DUEL_KEY", false);
            if (!accepted) {
                activity.remove(player);
            }
        });
    }

    private void showDuelRulesOnSecondScreen(Player p) {
        Optional<DuelArenaActivity> result = Activity.search(p, DuelArenaActivity.class);

        if (!result.isPresent()) {
            return;
        }

        DuelArenaActivity activity = result.get();

        int count = 0;
        for (DuelRule rule : DuelRule.values()) {
            if (activity.rules.contains(rule)) {
                p.send(new SendString(DuelUtils.getRuleText(rule), 31509 + count));
                count++;
            }
        }
    }

}
