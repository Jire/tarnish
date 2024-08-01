package com.osroyale.content.activity.impl.duelarena;

import com.osroyale.content.activity.Activity;
import com.osroyale.content.activity.ActivityDeathType;
import com.osroyale.content.activity.ActivityType;
import com.osroyale.content.activity.panel.ActivityPanel;
import com.osroyale.content.activity.panel.Activity_Panel;
import com.osroyale.content.consume.FoodData;
import com.osroyale.content.event.impl.ObjectInteractionEvent;
import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.world.entity.Entity;
import com.osroyale.game.world.entity.combat.CombatType;
import com.osroyale.game.world.entity.combat.strategy.CombatStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.exchange.ExchangeSessionType;
import com.osroyale.game.world.entity.mob.player.exchange.duel.StakeSession;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.containers.ItemContainer;
import com.osroyale.game.world.items.containers.equipment.Equipment;
import com.osroyale.game.world.items.containers.equipment.EquipmentType;
import com.osroyale.game.world.items.containers.pricechecker.PriceType;
import com.osroyale.game.world.pathfinding.TraversalMap;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.packet.out.SendEntityHintArrow;
import com.osroyale.net.packet.out.SendItemOnInterface;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.net.packet.out.SendString;
import com.osroyale.util.Utility;

import java.util.Objects;
import java.util.Optional;

import static com.osroyale.game.world.entity.combat.attack.FormulaFactory.rollDefensive;
import static com.osroyale.game.world.entity.combat.attack.FormulaFactory.rollOffensive;

/**
 * The duel arena activity.
 *
 * @author Michael | Chex
 */
public class DuelArenaActivity extends Activity {

    /** The mid-points of all three duel arenas. */
    private static final Position[] ARENAS = {
            new Position(3345, 3251),
            new Position(3378, 3251),
            new Position(3345, 3213),
            new Position(3379, 3213)
    };

    /** The mid-points of all three obstacle duel arenas. */
    private static final Position[] OBSTACLE_ARENAS = {
            new Position(3376, 3251),
            new Position(3345, 3231),
            new Position(3376, 3213)
    };

    /** The south-west tile of the hospital bed area. */
    private static final Position HOSPITAL_BEDS_SOUTHWEST = new Position(3361, 3272);

    /** The duel rules for this duel session. */
    public final DuelRules rules = new DuelRules();

    /** The duel stake session (holds items). */
    private final StakeSession session;

    /** The player requesting the duel. */
    final Player player;

    /** The player accepting the duel. */
    final Player opponent;

    /** The player activity panel. */
    private DuelPanel playerPanel;

    /** The opponent activity panel. */
    private DuelPanel opponentPanel;

    /** The combat listener for the duel. */
    private final DuelArenaListener listener = new DuelArenaListener(this);

    /** Whether or not the reward has been given. */
    private boolean rewarded;

    /** Whether or not the duel has started. */
    public boolean started;

    /** The duel outcome. */
    private Player winner, loser;

    public boolean hasLoser;

    private DuelArenaActivity(StakeSession session, Player player, Player opponent) {
        super(1, Entity.DEFAULT_INSTANCE);
        this.session = session;
        this.player = player;
        this.opponent = opponent;
    }

    public static DuelArenaActivity create(StakeSession session, Player player, Player opponent) {
        DuelArenaActivity duel = new DuelArenaActivity(session, player, opponent);
        duel.pause();
        duel.add(player);
        duel.add(opponent);
        duel.playerPanel = new DuelPanel(duel, player);
        duel.opponentPanel = new DuelPanel(duel, opponent);
        return duel;
    }

    @Override
    public void sequence() {
        super.sequence();

        if (isPaused()) {
            return;
        }

        if (!session.accepted) {
            player.exchangeSession.reset(ExchangeSessionType.DUEL);
            opponent.exchangeSession.reset(ExchangeSessionType.DUEL);
            removeAll(player, opponent);
            return;
        }

        if (getTicks() < 0) {
            return;
        }

        if (getTicks() == 0) {
            if (started) {
                finishCooldown();
                return;
            }

            player.speak("FIGHT!");
            opponent.speak("FIGHT!");
            started = true;

            // 10 minutes, has to be x2 because 2 players sequence this activity
            cooldown(2000);
        } else if (!started && getTicks() <= 6 && getTicks() % 2 == 0) {
            if (getTicks() == 6) {
                playerPanel.open();
                opponentPanel.open();
            }

            player.speak(String.valueOf(getTicks() / 2));
            opponent.speak(String.valueOf(getTicks() / 2));
        }
    }

    @Override
    protected void start() {
        unequipItems(player);
        unequipItems(opponent);

        player.playerAssistant.restore();
        opponent.playerAssistant.restore();

        if (rules.contains(DuelRule.OBSTACLES)) {
            player.move(TraversalMap.getRandomTraversableTile(OBSTACLE_ARENAS[0].transform(-3, -3), 3, 3));
            opponent.move(TraversalMap.getRandomTraversableTile(OBSTACLE_ARENAS[0].transform(3, -3), 3, 3));
        } else if (rules.contains(DuelRule.NO_MOVEMENT)) {
            Position position = ARENAS[0].transform(-9, -7);
            position = TraversalMap.getRandomTraversableTile(position, 18, 9);
            if (position == null) {
                finishCooldown();
                return;
            }
            player.move(position);
            position = TraversalMap.getRandomNonDiagonal(position);
            if (position == null) {
                finishCooldown();
                return;
            }
            opponent.move(position);
            player.face(opponent);
            opponent.face(player);
        } else {
            player.move(TraversalMap.getRandomTraversableTile(ARENAS[0].transform(-3, -3), 3, 3));
            opponent.move(TraversalMap.getRandomTraversableTile(ARENAS[0].transform(3, -3), 3, 3));
        }

        player.send(new SendEntityHintArrow(opponent, false));
        opponent.send(new SendEntityHintArrow(player, false));
        cooldown(8);
    }

    @Override
    public void finish() {
        if (rewarded) {
            return;
        }

        player.playerAssistant.restore();
        opponent.playerAssistant.restore();

        player.move(TraversalMap.getRandomTraversableTile(HOSPITAL_BEDS_SOUTHWEST, 14, 7));
        player.send(new SendEntityHintArrow(opponent, true));

        opponent.move(TraversalMap.getRandomTraversableTile(HOSPITAL_BEDS_SOUTHWEST, 14, 7));
        opponent.send(new SendEntityHintArrow(player, true));

        if (winner == null || loser == null) {
            player.message("The duel was a stalemate!");
            opponent.message("The duel was a stalemate!");
            reward(player, opponent, true);
            rewarded = true;
            removeAll(player, opponent);

            playerPanel.close();
            opponentPanel.close();
            return;
        }

        winner.message("You are victorious!");
        loser.message("You lost!");

        reward(winner, loser, false);
        rewarded = true;
        removeAll(player, opponent);

        playerPanel.close();
        opponentPanel.close();

        player.animate(Animation.RESET, true);
        player.graphic(Graphic.RESET, true);

        opponent.animate(Animation.RESET, true);
        opponent.graphic(Graphic.RESET, true);
    }

    @Override
    public void cleanup() {}

    @Override
    public void onDeath(Mob mob) {
        hasLoser = true;

        if (!session.accepted) {
            player.exchangeSession.reset(ExchangeSessionType.DUEL);
            opponent.exchangeSession.reset(ExchangeSessionType.DUEL);
            removeAll(player, opponent);
            return;
        }

        finishCooldown();
        if (mob.equals(player)) {
            winner = opponent;
            loser = player;
        } else if (mob.equals(opponent)) {
            winner = player;
            loser = opponent;
        }
    }

    @Override
    public void onLogout(Player player) {
        if (!session.accepted) {
            player.exchangeSession.reset(ExchangeSessionType.DUEL);
            opponent.exchangeSession.reset(ExchangeSessionType.DUEL);
            removeAll(player, opponent);
            return;
        }

        finishCooldown();
        if (player.equals(this.player)) {
            winner = opponent;
            loser = this.player;
        } else if (player.equals(opponent)) {
            winner = this.player;
            loser = opponent;
        }
    }

    @Override
    public boolean canLogout(Player player) {
        if (!session.accepted) {
            player.exchangeSession.reset(ExchangeSessionType.DUEL);
            opponent.exchangeSession.reset(ExchangeSessionType.DUEL);
            removeAll(player, opponent);
            return true;
        }

        player.send(new SendMessage("You cannot logout in the duel arena!"));
        return false;
    }

    @Override
    public boolean canDrinkPotions(Player player) {
        if (!session.accepted) {
            player.exchangeSession.reset(ExchangeSessionType.DUEL);
            opponent.exchangeSession.reset(ExchangeSessionType.DUEL);
            removeAll(player, opponent);
            return true;
        }

        if (rules.contains(DuelRule.NO_DRINKS)) {
            player.send(new SendMessage("Potions have been disabled!"));
            return false;
        }
        return true;
    }

    @Override
    public boolean canUsePrayer(Player player) {
        if (!session.accepted) {
            player.exchangeSession.reset(ExchangeSessionType.DUEL);
            opponent.exchangeSession.reset(ExchangeSessionType.DUEL);
            removeAll(player, opponent);
            return true;
        }

        if (rules.contains(DuelRule.NO_PRAYER)) {
            player.send(new SendMessage("Prayer has been disabled!"));
            return false;
        }

        return true;
    }

    @Override
    public boolean canEat(Player player, FoodData foodType) {
        if (!session.accepted) {
            player.exchangeSession.reset(ExchangeSessionType.DUEL);
            opponent.exchangeSession.reset(ExchangeSessionType.DUEL);
            removeAll(player, opponent);
            return true;
        }

        if (rules.contains(DuelRule.NO_FOOD)) {
            player.send(new SendMessage("Food has been disabled!"));
            return false;
        }
        return true;
    }

    @Override
    public boolean canUseSpecial(Player player) {
        if (!session.accepted) {
            player.exchangeSession.reset(ExchangeSessionType.DUEL);
            opponent.exchangeSession.reset(ExchangeSessionType.DUEL);
            removeAll(player, opponent);
            return true;
        }

        if (rules.contains(DuelRule.NO_SPECIAL_ATTACK)) {
            player.send(new SendMessage("Special attacks have been disabled!"));
            return false;
        }
        return true;
    }

    @Override
    public boolean canEquipItem(Player player, Item item, EquipmentType type) {
        if (!session.accepted) {
            player.exchangeSession.reset(ExchangeSessionType.DUEL);
            opponent.exchangeSession.reset(ExchangeSessionType.DUEL);
            removeAll(player, opponent);
            return true;
        }

        if (type == null) {
            return false;
        }

        if (rules.contains(DuelRule.LOCK_ITEMS)) {
            player.send(new SendMessage("Item switching has been disabled!"));
            return false;
        }

        if (rules.contains(DuelRule.ONLY_WHIP_DDS)) {
            final String name = item.getName() == null ? "null" : item.getName().toLowerCase();
            if (!name.contains("dragon dagger") && !name.contains("abyssal whip") && !name.contains("abyssal tentacle")) {
                player.send(new SendMessage("You can only use a whip or dragon dagger!"));
                return false;
            }
        }

        if (rules.contains(DuelRule.ONLY_FUN_WEAPONS) && !DuelUtils.hasFunWeapon(player, item)) {
            player.send(new SendMessage("You can only use fun weapons!"));
            return false;
        }

        if (rules.contains(DuelRule.NO_WEAPON) && type.getSlot() == Equipment.WEAPON_SLOT) {
            player.send(new SendMessage("Weapons have been disabled!"));
            return false;
        }

        if (rules.contains(DuelRule.NO_SHIELD) && type.getSlot() == Equipment.SHIELD_SLOT) {
            player.send(new SendMessage("Shields have been disabled!"));
            return false;
        }

        if (rules.contains(DuelRule.NO_AMMO) && type.getSlot() == Equipment.ARROWS_SLOT) {
            player.send(new SendMessage("Ammo has been disabled!"));
            return false;
        }

        if (rules.contains(DuelRule.NO_HEAD) && type.getSlot() == Equipment.HEAD_SLOT) {
            player.send(new SendMessage("Helms have been disabled!"));
            return false;
        }

        if (rules.contains(DuelRule.NO_CAPE) && type.getSlot() == Equipment.CAPE_SLOT) {
            player.send(new SendMessage("Capes have been disabled!"));
            return false;
        }

        if (rules.contains(DuelRule.NO_NECKLACE) && type.getSlot() == Equipment.AMULET_SLOT) {
            player.send(new SendMessage("Amulets hae been disabled!"));
            return false;
        }

        if (rules.contains(DuelRule.NO_BODY) && type.getSlot() == Equipment.CHEST_SLOT) {
            player.send(new SendMessage("Chest items have been disabled!"));
            return false;
        }

        if (rules.contains(DuelRule.NO_LEGS) && type.getSlot() == Equipment.LEGS_SLOT) {
            player.send(new SendMessage("Leg items have been disabled!"));
            return false;
        }

        if (rules.contains(DuelRule.NO_GLOVES) && type.getSlot() == Equipment.HANDS_SLOT) {
            player.send(new SendMessage("Gloves have been disabled!"));
            return false;
        }

        if (rules.contains(DuelRule.NO_BOOTS) && type.getSlot() == Equipment.FEET_SLOT) {
            player.send(new SendMessage("Boots have been disabled!"));
            return false;
        }

        if (rules.contains(DuelRule.NO_RINGS) && type.getSlot() == Equipment.RING_SLOT) {
            player.send(new SendMessage("Rings have been disabled!"));
            return false;
        }

        return true;
    }

    private void unequipItems(Player player) {
        if (!session.accepted) {
            player.exchangeSession.reset(ExchangeSessionType.DUEL);
            opponent.exchangeSession.reset(ExchangeSessionType.DUEL);
            removeAll(player, opponent);
            return;
        }

        if (rules.contains(DuelRule.NO_WEAPON) && player.equipment.hasWeapon()) {
            player.equipment.unequip(Equipment.WEAPON_SLOT);
        }

        if (rules.contains(DuelRule.NO_HEAD) && player.equipment.hasHead()) {
            player.equipment.unequip(Equipment.HEAD_SLOT);
        }

        if (rules.contains(DuelRule.NO_CAPE) && player.equipment.hasCape()) {
            player.equipment.unequip(Equipment.CAPE_SLOT);
        }

        if (rules.contains(DuelRule.NO_NECKLACE) && player.equipment.hasAmulet()) {
            player.equipment.unequip(Equipment.AMULET_SLOT);
        }

        if (rules.contains(DuelRule.NO_AMMO) && player.equipment.hasAmmo()) {
            player.equipment.unequip(Equipment.ARROWS_SLOT);
        }

        if (rules.contains(DuelRule.NO_BODY) && player.equipment.hasChest()) {
            player.equipment.unequip(Equipment.CHEST_SLOT);
        }

        if (rules.contains(DuelRule.NO_SHIELD)) {
            if (player.equipment.hasWeapon()) {
                Item weapon = player.equipment.getWeapon();
                if (weapon.isTwoHanded()) {
                    player.equipment.unequip(Equipment.WEAPON_SLOT);
                }
            }

            if (player.equipment.hasShield()) {
                player.equipment.unequip(Equipment.SHIELD_SLOT);
            }
        }

        if (rules.contains(DuelRule.NO_LEGS) && player.equipment.hasLegs()) {
            player.equipment.unequip(Equipment.LEGS_SLOT);
        }

        if (rules.contains(DuelRule.NO_GLOVES) && player.equipment.hasHands()) {
            player.equipment.unequip(Equipment.HANDS_SLOT);
        }

        if (rules.contains(DuelRule.NO_BOOTS) && player.equipment.hasFeet()) {
            player.equipment.unequip(Equipment.FEET_SLOT);
        }

        if (rules.contains(DuelRule.NO_RINGS) && player.equipment.hasRing()) {
            player.equipment.unequip(Equipment.RING_SLOT);
        }

    }

    @Override
    protected boolean clickObject(Player player, ObjectInteractionEvent event) {
        switch(event.getObject().getId()) {
            case 3111:
            case 3113:
            case 3203:
                if (!session.accepted) {
                    player.exchangeSession.reset(ExchangeSessionType.DUEL);
                    opponent.exchangeSession.reset(ExchangeSessionType.DUEL);
                    removeAll(player, opponent);
                    return true;
                }

                if (rules.contains(DuelRule.NO_FORFEIT)) {
                    player.send(new SendMessage("Forfeiting has been disabled!"));
                    return true;
                }

                if (!started) {
                    player.send(new SendMessage("You can't forfeit yet!"));
                    return true;
                }

                finishCooldown();
                if (player.equals(this.player)) {
                    winner = opponent;
                    loser = this.player;
                } else if (player.equals(opponent)) {
                    winner = this.player;
                    loser = opponent;
                }
                return true;

        }
        return false;
    }

    @Override
    protected Optional<DuelArenaListener> getListener() {
        return Optional.of(listener);
    }

    @Override
    public ActivityType getType() {
        return ActivityType.DUEL_ARENA;
    }

    @Override
    public ActivityDeathType deathType() {
        return ActivityDeathType.SAFE;
    }

    private void reward(Player player, Player loser, Item[] reward, long value, boolean stalemate) {
        player.send(new SendString("<col=E1981F>Total Value: " + Utility.formatDigits(value), 31709));
        player.send(new SendString(loser.getName(), 31706));
        player.send(new SendString(loser.skills.getCombatLevel(), 31707));
        player.send(new SendItemOnInterface(31708, reward));
        player.send(new SendString(stalemate ? "The duel was a stalemate!" : player.equals(loser) ? "You lost!" : "You are victorious!", 31705));
        player.interfaceManager.open(31700, false);
    }

    private void reward(Player winner, Player loser, boolean stalemate) {
        Objects.requireNonNull(winner);
        Objects.requireNonNull(loser);

        ItemContainer winnerContainer = session.item_containers.get(winner);
        Objects.requireNonNull(winnerContainer);

        ItemContainer loserContainer = session.item_containers.get(loser);
        Objects.requireNonNull(loserContainer);

        if (stalemate) {
            reward(loser, loser, loserContainer.toArray(), loserContainer.containerValue(PriceType.VALUE), true);
            reward(winner, loser, winnerContainer.toArray(), winnerContainer.containerValue(PriceType.VALUE), true);

            winner.inventory.addOrDrop(winnerContainer.toArray());
            loser.inventory.addOrDrop(loserContainer.toArray());
        } else {
            long value = 0;

            ItemContainer rewardContainer = new ItemContainer(28, ItemContainer.StackPolicy.STANDARD);

            rewardContainer.addAll(loserContainer.toArray());
            value += loserContainer.containerValue(PriceType.VALUE);
            reward(loser, loser, rewardContainer.toArray(), value, false);

            value += winnerContainer.containerValue(PriceType.VALUE);
            rewardContainer.addAll(winnerContainer.toArray());
            reward(winner, loser, rewardContainer.toArray(), value, false);

            winner.inventory.addOrDrop(rewardContainer.toArray());
        }
    }

    @Override
    public void update() {
        if (!started || getTicks() == FINISH || isPaused()) {
            return;
        }

        playerPanel.update();
        opponentPanel.update();
        ActivityPanel.update(playerPanel);
        ActivityPanel.update(opponentPanel);
    }

    private static class DuelPanel extends Activity_Panel {
        private final DuelArenaActivity activity;
        private final Player player;

        DuelPanel(DuelArenaActivity activity, Player player) {
            super(player, "Duel Arena");
            this.activity = activity;
            this.player = player;
        }

        public void update() {
            Player dueler = player.equals(activity.player) ? activity.player : activity.opponent;
            Player duelee = !player.equals(activity.player) ? activity.player : activity.opponent;

            if (!activity.started) {
                set(0, "Dueling with: <col=FF5500>" + Utility.formatName(duelee.getName()) + "</col>");
                setProgress(-1);
            } else {
                set(0, "Time remaining: <col=FF5500>" + Utility.getTime(activity.getTicks() / 2) + "</col>");
                set(2, "Your accuracy: <col=FF5500>" + accuracy(dueler, duelee) + "</col>");
                set(3, "Your max hit: <col=FF5500>" + dueler.playerAssistant.getMaxHit(duelee, dueler.getStrategy().getCombatType()) + "</col>");
                set(5, "Opponent's accuracy: <col=FF5500>" + accuracy(duelee, dueler) + "</col>");
                set(6, "Opponent's max hit: <col=FF5500>" + duelee.playerAssistant.getMaxHit(dueler, duelee.getStrategy().getCombatType()) + "</col>");
                setFooter("Opponent health:");
                setProgress((int) Utility.getPercentageAmount(duelee.getCurrentHealth(), duelee.getMaximumHealth()));
            }
            setItem(new Item(4049));
        }

        private String accuracy(Player dueler, Player duelee) {
            CombatStrategy<? super Player> strategy = dueler.getStrategy();
            CombatType type = strategy.getCombatType();

            dueler.getCombat().addModifier(strategy);
            double attackRoll = rollOffensive(dueler, duelee, type.getFormula());
            double defenceRoll = rollDefensive(dueler, duelee, type.getFormula()); //what is this exactly? this class duel arena? just a panel
            dueler.getCombat().removeModifier(strategy);

            double chance = attackRoll / (attackRoll + defenceRoll);
            double accuracy = (int) (chance * 10000) / 100.0;
            return String.valueOf(accuracy) + "%";
        }

    }

}
