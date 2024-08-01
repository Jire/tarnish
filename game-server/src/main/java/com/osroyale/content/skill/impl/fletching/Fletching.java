package com.osroyale.content.skill.impl.fletching;

import com.osroyale.Config;
import com.osroyale.content.achievement.AchievementHandler;
import com.osroyale.content.achievement.AchievementKey;
import com.osroyale.content.activity.randomevent.RandomEventHandler;
import com.osroyale.content.clanchannel.content.ClanTaskKey;
import com.osroyale.content.event.impl.ClickButtonInteractionEvent;
import com.osroyale.content.event.impl.ItemOnItemInteractionEvent;
import com.osroyale.content.skill.impl.firemaking.FiremakingData;
import com.osroyale.content.skill.impl.fletching.impl.Arrow;
import com.osroyale.content.skill.impl.fletching.impl.Carvable;
import com.osroyale.content.skill.impl.fletching.impl.Stringable;
import com.osroyale.content.skillcape.SkillCape;
import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.action.Action;
import com.osroyale.game.action.policy.WalkablePolicy;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.net.packet.out.*;
import com.osroyale.util.Utility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

/**
 * Handles the fletching skill.
 *
 * @author Daniel
 */
public class Fletching extends Skill {

    private static final Logger logger = LogManager.getLogger();

    private static final String FLETCHABLE_KEY = "FLETCHABLE_KEY";

    private final static HashMap<Integer, Fletchable> FLETCHABLES = new HashMap<>();

    public Fletching(int level, double experience) {
        super(Skill.FLETCHING, level, experience);
    }

    public static void addFletchable(Fletchable fletchable) {
        if (FLETCHABLES.put(fletchable.getWith().getId(), fletchable) != null) {
            System.out.println("[Fletching] Conflicting item values: " + fletchable.getWith().getId() + " Type: " + fletchable.getClass().getSimpleName());
        }
    }

    private Fletchable getFletchable(int use, int with) {
        return FLETCHABLES.getOrDefault(use, FLETCHABLES.get(with));
    }

    @Override
    protected double modifier() {
        return Config.FLETCHING_MODIFICATION;
    }

    @Override
    protected boolean useItem(Player player, ItemOnItemInteractionEvent event) {
        Item first = event.getFirst();
        Item second = event.getSecond();

        if (FiremakingData.forId(first.getId()).isPresent() && FiremakingData.forId(second.getId()).isPresent()) {
            return false;
        }

        Fletchable fletchable = getFletchable(first.getId(), second.getId());

        if (fletchable == null || first.getId() == 590 || second.getId() == 590 || first.getId() == 8792 || second.getId() == 8792) {
            return false;
        }

        if (!fletchable.getUse().equalIds(first) && !fletchable.getUse().equalIds(second)) {
            player.message("You need to use this with " + Utility.getAOrAn(fletchable.getUse().getName()) + " " + fletchable.getUse().getName().toLowerCase() + " to fletch this item.");
            return true;
        }

        String prefix = fletchable.getWith().getName().split(" ")[0];

        switch (fletchable.getFletchableItems().length) {

            case 1:
                player.attributes.set(FLETCHABLE_KEY, fletchable);
                player.send(new SendString("\\n \\n \\n \\n" + fletchable.getFletchableItems()[0].getProduct().getName(), 2799));
                player.send(new SendItemOnInterfaceZoom(1746, 170, fletchable.getFletchableItems()[0].getProduct().getId()));
                player.send(new SendChatBoxInterface(4429));
                return true;
            case 2:
                player.attributes.set(FLETCHABLE_KEY, fletchable);
                player.send(new SendItemOnInterfaceZoom(8869, 170, fletchable.getFletchableItems()[0].getProduct().getId()));
                player.send(new SendItemOnInterfaceZoom(8870, 170, fletchable.getFletchableItems()[1].getProduct().getId()));
                player.send(new SendString("\\n \\n \\n \\n".concat(prefix + " Short Bow"), 8874));
                player.send(new SendString("\\n \\n \\n \\n".concat(prefix + " Long Bow"), 8878));
                player.send(new SendChatBoxInterface(8866));
                return true;
            case 3:
                player.attributes.set(FLETCHABLE_KEY, fletchable);
                player.send(new SendItemOnInterfaceZoom(8883, 170, fletchable.getFletchableItems()[0].getProduct().getId()));
                player.send(new SendItemOnInterfaceZoom(8884, 170, fletchable.getFletchableItems()[1].getProduct().getId()));
                player.send(new SendItemOnInterfaceZoom(8885, 170, fletchable.getFletchableItems()[2].getProduct().getId()));
                player.send(new SendString("\\n \\n \\n \\n".concat("Arrow heads"), 8889));
                player.send(new SendString("\\n \\n \\n \\n".concat(prefix + " Short Bow"), 8893));
                player.send(new SendString("\\n \\n \\n \\n".concat(prefix + " Long Bow"), 8897));
                player.send(new SendChatBoxInterface(8880));
                return true;
            case 4:
                player.attributes.set(FLETCHABLE_KEY, fletchable);
                player.send(new SendItemOnInterfaceZoom(8902, 170, fletchable.getFletchableItems()[0].getProduct().getId()));
                player.send(new SendItemOnInterfaceZoom(8903, 170, fletchable.getFletchableItems()[1].getProduct().getId()));
                player.send(new SendItemOnInterfaceZoom(8904, 170, fletchable.getFletchableItems()[2].getProduct().getId()));
                player.send(new SendItemOnInterfaceZoom(8905, 170, fletchable.getFletchableItems()[3].getProduct().getId()));

                String[] productNames;
                if (fletchable == Carvable.AMETHYST) {
                    productNames = new String[]{"Arrowtips", "Bolt tips", "Javelin heads", "Dart tips"};
                } else {
                    productNames = new String[]{"Arrow Shafts", "Short Bow", "Long Bow", "Crossbow Stock"};
                }

                player.send(new SendString("\\n \\n \\n \\n".concat(productNames[0]), 8909));
                player.send(new SendString("\\n \\n \\n \\n".concat(productNames[1]), 8913));
                player.send(new SendString("\\n \\n \\n \\n".concat(productNames[2]), 8917));
                player.send(new SendString("\\n \\n \\n \\n".concat(productNames[3]), 8921));
                player.send(new SendChatBoxInterface(8899));
                return true;
            default:
                return false;
        }
    }

    @Override
    protected boolean clickButton(Player player, ClickButtonInteractionEvent event) {
        if (player.attributes.get(FLETCHABLE_KEY, Fletchable.class) == null) {
            return false;
        }

        Fletchable fletchable = player.attributes.get(FLETCHABLE_KEY, Fletchable.class);

        int button = event.getButton();

        switch (button) {

		/* Option 1 - Make all */
            case 1747:
                start(player, fletchable, 0, 15);
                return true;

		/* Option 1 - Make 1 */
            case 2799:
            case 8909:
            case 8874:
            case 8889:
                start(player, fletchable, 0, 1);
                return true;

		/* Option 1 - Make 5 */
            case 2798:
            case 8908:
            case 8873:
            case 8888:
                start(player, fletchable, 0, 5);
                return true;

		/* Option 1 - Make 10 */
            case 8907:
            case 8872:
            case 8887:
                start(player, fletchable, 0, 10);
                return true;
                //TODO: Make X limited to 150.

		/* Option 1 - Make X */
            case 1748:
            case 8906:
            case 8871:
            case 8886:
            case 6212:
                try {
                    player.send(new SendInputAmount("Enter amount", 3, input ->
                            start(player, fletchable, 0, Integer.parseInt(input))));

                } catch (Exception ex) {
                    logger.error(String.format("player=%s error fletching option1: make-x", player.getName()), ex);
                }
                return true;

		/* Option 2 - Make 1 */
            case 8913:
            case 8878:
            case 8893:
                start(player, fletchable, 1, 1);
                return true;

		/* Option 2 - Make 5 */
            case 8912:
            case 8877:
            case 8892:
                start(player, fletchable, 1, 5);
                return true;

		/* Option 2 - Make 10 */
            case 8911:
            case 8876:
            case 8891:
                start(player, fletchable, 1, 10);
                return true;

		/* Option 2 - Make X */
            case 8910:
            case 8875:
            case 8890:
                try {
                    player.send(new SendInputAmount("Enter amount", 2, input -> start(player, fletchable, 1, Integer.parseInt(input))));
                } catch (Exception ex) {
                    logger.error(String.format("player=%s error fletching option2: make-x", player.getName()), ex);
                }
                return true;

		/* Option 3 - Make 1 */
            case 8917:
            case 8897:
                start(player, fletchable, 2, 1);
                return true;

		/* Option 3 - Make 5 */
            case 8916:
            case 8896:
                start(player, fletchable, 2, 5);
                return true;

		/* Option 3 - Make 10 */
            case 8915:
            case 8895:
                start(player, fletchable, 2, 10);
                return true;

		/* Option 3 - Make X */
            case 8914:
            case 8894:
                try {
                    player.send(new SendInputAmount("Enter amount", 2, input -> start(player, fletchable, 2, Integer.parseInt(input))));
                } catch (Exception ex) {
                    logger.error(String.format("player=%s error fletching option3: make-x", player.getName()), ex);
                }
                return true;

		/* Option 4 - Make 1 */
            case 8921:
                start(player, fletchable, 3, 1);
                return true;

		/* Option 4 - Make 5 */
            case 8920:
                start(player, fletchable, 3, 5);
                return true;

		/* Option 4 - Make 10 */
            case 8919:
                start(player, fletchable, 3, 10);
                return true;

		/* Option 4 - Make X */
            case 8918:
                try {

                    player.send(new SendInputAmount("Enter amount", 2, input -> start(player, fletchable, 3, Integer.parseInt(input))));
                } catch (Exception ex) {
                    logger.error(String.format("player=%s error fletching option3: make-x", player.getName()), ex);
                }
                return true;

            default:
                return false;
        }
    }

    public boolean fletch(Player player, int index, int amount) {
        Fletchable fletchable = player.attributes.get(FLETCHABLE_KEY);

        if (fletchable == null) {
            return false;
        }

        return start(player, fletchable, index, amount);

    }

    public boolean start(Player player, Fletchable fletchable, int index, int amount) {
        if (fletchable == null) {
            return false;
        }

        player.attributes.remove(FLETCHABLE_KEY);
        FletchableItem item = fletchable.getFletchableItems()[index];
        player.interfaceManager.close();

        if (player.skills.getLevel(Skill.FLETCHING) < item.getLevel()) {
            player.dialogueFactory.sendStatement("<col=369>You need a Fletching level of " + item.getLevel() + " to do that.").execute();
            return true;
        }

        if (!(player.inventory.containsAll(fletchable.getIngredients()))) {
            String firstName = fletchable.getUse().getName().toLowerCase();
            String secondName = fletchable.getWith().getName().toLowerCase();

            if (fletchable.getUse().getAmount() > 1 && !firstName.endsWith("s")) {
                firstName = firstName.concat("s");
            }

            if (fletchable.getWith().getAmount() > 1 && !secondName.endsWith("s")) {
                secondName = secondName.concat("s");
            }

            if (fletchable.getUse().getAmount() == 1 && firstName.endsWith("s")) {
                firstName = firstName.substring(0, firstName.length() - 1);
            }

            if (fletchable.getWith().getAmount() == 1 && secondName.endsWith("s")) {
                secondName = secondName.substring(0, secondName.length() - 1);
            }

            final String firstAmount;

            if (fletchable.getUse().getAmount() == 1) {
                firstAmount = Utility.getAOrAn(fletchable.getUse().getName());
            } else {
                firstAmount = String.valueOf(fletchable.getUse().getAmount());
            }

            final String secondAmount;

            if (fletchable.getWith().getAmount() == 1) {
                secondAmount = Utility.getAOrAn(fletchable.getWith().getName());
            } else {
                secondAmount = String.valueOf(fletchable.getWith().getAmount());
            }

            String firstRequirement = firstAmount + " " + firstName;
            String secondRequirement = secondAmount + " " + secondName;
            player.send(new SendMessage("You need " + firstRequirement + " and " + secondRequirement + " to do that."));
            return true;
        }

        player.action.execute(fletch(player, fletchable, item, amount), true);
        return true;
    }

    private Action<Player> fletch(Player player, Fletchable fletchable, FletchableItem item, int amount) {
        return new Action<Player>(player, 2, true) {
            int iterations = 0;

            @Override
            public void execute() {
                ++iterations;
                player.animate(new Animation(fletchable.getAnimation()));
                player.graphic(new Graphic(fletchable.getGraphics()));
                final boolean saveMaterial = SkillCape.isEquipped(player, SkillCape.FLETCHING) && Utility.random(1, 3) == 1;

                if (fletchable == Carvable.AMETHYST) {
                    player.skills.addExperience(Skill.CRAFTING, item.getExperience() * modifier());
                } else {
                    player.skills.addExperience(Skill.FLETCHING, item.getExperience() * modifier());
                }

                if (!saveMaterial) {
                    player.inventory.removeAll(fletchable.getIngredients());
                }

                player.inventory.addOrDrop(item.getProduct());
                player.playerAssistant.activateSkilling(1);
                RandomEventHandler.trigger(player);

                if (fletchable.getProductionMessage() != null) {
                    player.send(new SendMessage(fletchable.getProductionMessage()));
                }

                if (fletchable == Stringable.MAGIC_SHORTBOWS) {
                    getMob().forClan(channel -> channel.activateTask(ClanTaskKey.MAGIC_SHORTBOW, getMob().getName()));
                } else if (fletchable == Stringable.YEW_SHORTBOWS) {
                    getMob().forClan(channel -> channel.activateTask(ClanTaskKey.YEW_SHORTBOW, getMob().getName()));
                } else if (fletchable instanceof Arrow) {
                    AchievementHandler.activate(player, AchievementKey.FLETCHING);
                }

                if (iterations == amount || iterations > 28 || !(player.inventory.containsAll(fletchable.getIngredients()))) {
                    cancel();
                    if (!(player.inventory.containsAll(fletchable.getIngredients()))) {
                        player.send(new SendMessage("<col=369>You have run out of materials."));
                    }
                    return;
                }
            }


            @Override
            public String getName() {
                return "Fletching";
            }

            @Override
            public boolean prioritized() {
                return false;
            }

            @Override
            public WalkablePolicy getWalkablePolicy() {
                return WalkablePolicy.NON_WALKABLE;
            }
        };
    }
}
