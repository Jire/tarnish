package com.osroyale.content.skill.impl.crafting;

import com.osroyale.Config;
import com.osroyale.content.skillcape.SkillCape;
import com.osroyale.content.activity.randomevent.RandomEventHandler;
import com.osroyale.content.store.Store;
import com.osroyale.net.packet.out.SendInputAmount;
import com.osroyale.net.packet.out.SendChatBoxInterface;
import com.osroyale.net.packet.out.SendItemOnInterfaceZoom;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.net.packet.out.SendString;
import com.osroyale.game.Animation;
import com.osroyale.game.action.Action;
import com.osroyale.game.action.policy.WalkablePolicy;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.content.event.impl.*;
import com.osroyale.content.skill.impl.crafting.impl.*;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.object.GameObject;
import com.osroyale.util.Utility;

import java.util.HashMap;

/**
 * Handles the crafting skill.
 *
 * @author Daniel
 */
public class Crafting extends Skill {

    /** The craftable key. */
    private static final String CRAFTABLE_KEY = "CRAFTABLE_KEY";

    /**  The craftable map. */
    private final static HashMap<Integer, Craftable> CRAFTABLES = new HashMap<>();

    /** The leather armour data. */
    private static final Object[][] LEATHER_ARMOR_IDS = {
            {8635, 1, Leather.LEATHER_BODY}, {8634, 5, Leather.LEATHER_BODY}, {8633, 10, Leather.LEATHER_BODY},
            {8638, 1, Leather.LEATHER_GLOVES}, {8637, 5, Leather.LEATHER_GLOVES}, {8636, 10, Leather.LEATHER_GLOVES},
            {8641, 1, Leather.LEATHER_BOOTS}, {8640, 5, Leather.LEATHER_BOOTS}, {8639, 10, Leather.LEATHER_BOOTS},
            {8644, 1, Leather.LEATHER_VANBRACES}, {8643, 5, Leather.LEATHER_VANBRACES}, {8642, 10, Leather.LEATHER_VANBRACES},
            {8647, 1, Leather.LEATHER_CHAPS}, {8646, 5, Leather.LEATHER_CHAPS}, {8645, 10, Leather.LEATHER_CHAPS},
            {8650, 1, Leather.LEATHER_COIF}, {8649, 5, Leather.LEATHER_COIF}, {8648, 10, Leather.LEATHER_COIF},
            {8653, 1, Leather.LEATHER_COWL}, {8652, 5, Leather.LEATHER_COWL}, {8651, 10, Leather.LEATHER_COWL},
    };

    public Crafting(int level, double experience) {
        super(Skill.CRAFTING, level, experience);
    }

    public static void addCraftable(Craftable craftable) {
        if (CRAFTABLES.put(craftable.getWith().getId(), craftable) != null) {
            System.out.println("[Crafting] Conflicting item values: " + craftable.getWith().getId() + " Type: " + craftable.getName());
        }
    }

    private static Craftable getCraftable(int use, int with) {
        return CRAFTABLES.get(use) == null ? CRAFTABLES.get(with) : CRAFTABLES.get(use);
    }

    @Override
    protected boolean clickObject(Player player, ObjectInteractionEvent event) {
        if (event.getObject().getId() == 4309 && event.getOpcode() == 1) {
            Spinning.open(player);
            return true;
        }

        if (event.getObject().getId() == 11601 && event.getOpcode() == 0) {
            Jewellery.open(player);
            return true;
        }

        return false;
    }

    @Override
    protected boolean clickNpc(Player player, NpcInteractionEvent event) {
        if (event.getNpc().id == 5811) {
            if (event.getOpcode() == 0) {
                Tanning.open(player);
            } else if (event.getOpcode() == 1) {
                Store.STORES.get("Crafting Store").open(player);
            }
            return true;
        }

        return false;
    }

    @Override
    protected boolean useItem(Player player, ItemOnObjectInteractionEvent event) {
        Item item = event.getItem();
        GameObject object = event.getObject();

        if (object.getId() == 16469 && (item.getId() == 1783 || item.getId() == 1781)) {
            Glass.craft(player, Glass.GlassData.MOLTEN_GLASS, 28);
            return true;
        }

        return false;
    }

    @Override
    protected boolean useItem(Player player, ItemOnItemInteractionEvent event) {
        Item first = event.getFirst();
        Item second = event.getSecond();

        if (Stringing.useItem(player, first, second)) {
            return true;
        }

        if (first.getId() == 1785 && second.getId() == 1775 || first.getId() == 1775 && second.getId() == 1785) {
            Glass.open(player);
            return true;
        }

        if ((first.getId() == 1733 && second.getId() == 1741) || (first.getId() == 1741 && second.getId() == 1733)) {
            player.attributes.set(CRAFTABLE_KEY, "HIDE");
            player.interfaceManager.open(2311);
            return true;
        }

        Craftable craftable = getCraftable(first.getId(), second.getId());

        if (craftable == null) {
            return false;
        }

        if (!craftable.getUse().equalIds(first) && !craftable.getUse().equalIds(second)) {
            player.message("You need to use this with " + Utility.getAOrAn(craftable.getUse().getName()) + " " + craftable.getUse().getName().toLowerCase() + " to craft this item.");
            return true;
        }

        switch (craftable.getCraftableItems().length) {

            case 1:
                player.attributes.set(CRAFTABLE_KEY, craftable);
                player.send(new SendString("\\n \\n \\n \\n" + craftable.getCraftableItems()[0].getProduct().getName(), 2799));
                player.send(new SendItemOnInterfaceZoom(1746, 170, craftable.getCraftableItems()[0].getProduct().getId()));
                player.send(new SendChatBoxInterface(4429));
                return true;
            case 2:
                player.attributes.set(CRAFTABLE_KEY, craftable);
                player.send(new SendItemOnInterfaceZoom(8869, 170, craftable.getCraftableItems()[0].getProduct().getId()));
                player.send(new SendItemOnInterfaceZoom(8870, 170, craftable.getCraftableItems()[1].getProduct().getId()));
                player.send(new SendString("\\n \\n \\n \\n".concat(craftable.getCraftableItems()[0].getProduct().getName().replace("d'hide ", "")), 8874));
                player.send(new SendString("\\n \\n \\n \\n".concat(craftable.getCraftableItems()[1].getProduct().getName().replace("d'hide ", "")), 8878));
                player.send(new SendChatBoxInterface(8866));
                return true;
            case 3:
                player.attributes.set(CRAFTABLE_KEY, craftable);
                player.send(new SendItemOnInterfaceZoom(8883, 170, craftable.getCraftableItems()[0].getProduct().getId()));
                player.send(new SendItemOnInterfaceZoom(8884, 170, craftable.getCraftableItems()[1].getProduct().getId()));
                player.send(new SendItemOnInterfaceZoom(8885, 170, craftable.getCraftableItems()[2].getProduct().getId()));
                player.send(new SendString("\\n \\n \\n \\n".concat(craftable.getCraftableItems()[0].getProduct().getName().replace("d'hide ", "")), 8889));
                player.send(new SendString("\\n \\n \\n \\n".concat(craftable.getCraftableItems()[1].getProduct().getName().replace("d'hide ", "")), 8893));
                player.send(new SendString("\\n \\n \\n \\n".concat(craftable.getCraftableItems()[2].getProduct().getName().replace("d'hide ", "")), 8897));
                player.send(new SendChatBoxInterface(8880));
                return true;
            case 4:
                player.attributes.set(CRAFTABLE_KEY, craftable);
                player.send(new SendItemOnInterfaceZoom(8902, 170, craftable.getCraftableItems()[0].getProduct().getId()));
                player.send(new SendItemOnInterfaceZoom(8903, 170, craftable.getCraftableItems()[1].getProduct().getId()));
                player.send(new SendItemOnInterfaceZoom(8904, 170, craftable.getCraftableItems()[2].getProduct().getId()));
                player.send(new SendItemOnInterfaceZoom(8905, 170, craftable.getCraftableItems()[3].getProduct().getId()));
                player.send(new SendString("\\n \\n \\n \\n".concat(craftable.getCraftableItems()[0].getProduct().getName().replace("d'hide ", "")), 8909));
                player.send(new SendString("\\n \\n \\n \\n".concat(craftable.getCraftableItems()[1].getProduct().getName().replace("d'hide ", "")), 8913));
                player.send(new SendString("\\n \\n \\n \\n".concat(craftable.getCraftableItems()[2].getProduct().getName().replace("d'hide ", "")), 8917));
                player.send(new SendString("\\n \\n \\n \\n".concat(craftable.getCraftableItems()[3].getProduct().getName().replace("d'hide ", "")), 8921));
                player.send(new SendChatBoxInterface(8899));
                return true;
            case 5:
                player.attributes.set(CRAFTABLE_KEY, craftable);
                player.send(new SendItemOnInterfaceZoom(8941, 170, craftable.getCraftableItems()[0].getProduct().getId()));
                player.send(new SendItemOnInterfaceZoom(8942, 170, craftable.getCraftableItems()[1].getProduct().getId()));
                player.send(new SendItemOnInterfaceZoom(8943, 170, craftable.getCraftableItems()[2].getProduct().getId()));
                player.send(new SendItemOnInterfaceZoom(8944, 170, craftable.getCraftableItems()[3].getProduct().getId()));
                player.send(new SendItemOnInterfaceZoom(8945, 170, craftable.getCraftableItems()[4].getProduct().getId()));
                player.send(new SendString("\\n \\n \\n \\n".concat("Body"), 8949));
                player.send(new SendString("\\n \\n \\n \\n".concat("Chaps"), 8953));
                player.send(new SendString("\\n \\n \\n \\n".concat("Vambraces"), 8957));
                player.send(new SendString("\\n \\n \\n \\n".concat("Bandana"), 8961));
                player.send(new SendString("\\n \\n \\n \\n".concat("Boots"), 8965));
                player.send(new SendChatBoxInterface(8938));
                return true;

            default:
                return false;
        }
    }

    @Override
    protected boolean clickButton(Player player, ClickButtonInteractionEvent event) {
        if (Tanning.click(player, event.getButton())) {
            return true;
        }

        if (Glass.click(player, event.getButton())) {
            return true;
        }

        if (!player.attributes.has(CRAFTABLE_KEY)) {
            return false;
        }

        int button = event.getButton();

        if (button == 2422) {
            return false;
        }

        if (String.valueOf(player.attributes.getObject(CRAFTABLE_KEY)).equals("HIDE")) {
            for (Object[] i : LEATHER_ARMOR_IDS) {
                if ((int) i[0] == button) {
                    player.interfaceManager.close();
                    start(player, (Craftable) i[2], 0, (int) i[1]);
                    return true;
                }
            }
        }

        Craftable craftable = player.attributes.get(CRAFTABLE_KEY, Craftable.class);

        switch (button) {

            /* Option 1 - Make all */
            case 1747:
                start(player, craftable, 0, player.inventory.computeAmountForId(craftable.getWith().getId()));
                return true;

            /* Option 1 - Make 1 */
            case 2799:
            case 8909:
            case 8874:
            case 8889:
            case 8949:
                start(player, craftable, 0, 1);
                return true;

            /* Option 1 - Make 5 */
            case 2798:
            case 8908:
            case 8873:
            case 8888:
            case 8948:
                start(player, craftable, 0, 5);
                return true;

            /* Option 1 - Make 10 */
            case 1748:
            case 8907:
            case 8872:
            case 8887:
            case 8947:
                start(player, craftable, 0, 10);
                return true;

            /* Option 1 - Make X */
            case 8906:
            case 8871:
            case 8886:
            case 6212:
            case 8946:
                player.send(new SendInputAmount("Enter amount", 10, input -> start(player, craftable, 0, Integer.parseInt(input))));
                return true;

            /* Option 2 - Make 1 */
            case 8913:
            case 8878:
            case 8893:
            case 8953:
                start(player, craftable, 1, 1);
                return true;

            /* Option 2 - Make 5 */
            case 8912:
            case 8877:
            case 8892:
            case 8952:
                start(player, craftable, 1, 5);
                return true;

            /* Option 2 - Make 10 */
            case 8911:
            case 8876:
            case 8891:
            case 8951:
                start(player, craftable, 1, 10);
                return true;

            /* Option 2 - Make X */
            case 8910:
            case 8875:
            case 8890:
            case 8950:
                player.send(new SendInputAmount("Enter amount", 10, input -> start(player, craftable, 1, Integer.parseInt(input))));
                return true;

            /* Option 3 - Make 1 */
            case 8917:
            case 8897:
            case 8957:
                start(player, craftable, 2, 1);
                return true;

            /* Option 3 - Make 5 */
            case 8916:
            case 8896:
            case 8956:
                start(player, craftable, 2, 5);
                return true;

            /* Option 3 - Make 10 */
            case 8915:
            case 8895:
            case 8955:
                start(player, craftable, 2, 10);
                return true;

            /* Option 3 - Make X */
            case 8914:
            case 8894:
            case 8954:
                player.send(new SendInputAmount("Enter amount", 10, input -> start(player, craftable, 2, Integer.parseInt(input))));
                return true;

            /* Option 4 - Make 1 */
            case 8921:
            case 8961:
                start(player, craftable, 3, 1);
                return true;

            /* Option 4 - Make 5 */
            case 8920:
            case 8960:
                start(player, craftable, 3, 5);
                return true;

            /* Option 4 - Make 10 */
            case 8919:
            case 8959:
                start(player, craftable, 3, 10);
                return true;

            /* Option 4 - Make X */
            case 8918:
            case 8958:
                player.send(new SendInputAmount("Enter amount", 10, input -> start(player, craftable, 3, Integer.parseInt(input))));
                return true;

            /* Option 5 - Make 1 */
            case 8965:
                start(player, craftable, 4, 1);
                return true;

            /* Option 5 - Make 5 */
            case 8964:
                start(player, craftable, 4, 5);
                return true;

            /* Option 5 - Make 10 */
            case 8963:
                start(player, craftable, 4, 10);
                return true;

            /* Option 5 - Make X */
            case 8962:
                player.send(new SendInputAmount("Enter amount", 10, input -> start(player, craftable, 4, Integer.parseInt(input))));
                return true;

            default:
                return false;
        }
    }

    public boolean craft(Player player, int index, int amount) {
        Craftable craftable = player.attributes.get(CRAFTABLE_KEY);
        return start(player, craftable, index, amount);
    }

    public boolean start(Player player, Craftable craftable, int index, int amount) {
        if (craftable == null) {
            return false;
        }

        player.attributes.remove(CRAFTABLE_KEY);

        CraftableItem item = craftable.getCraftableItems()[index];

        player.interfaceManager.close();

        if (player.skills.getLevel(Skill.CRAFTING) < item.getLevel()) {
            player.dialogueFactory.sendStatement("<col=369>You need a Crafting level of " + item.getLevel() + " to do that.").execute();
            return true;
        }

        if (!player.inventory.containsAll(craftable.getIngredients(index))) {
            Item requiredItem = craftable.getCraftableItems()[index].getRequiredItem();
            Item product = craftable.getCraftableItems()[index].getProduct();
            String productAmount = "";

            if (product.getName().contains("vamb")) {
                productAmount = " pair of";
            } else if (!product.getName().endsWith("s")) {
                productAmount = " " + Utility.getAOrAn(product.getName());
            }

            player.send(new SendMessage("You need " + requiredItem.getAmount() + " piece" + (requiredItem.getAmount() > 1 ? "s" : "") + " of " + requiredItem.getName().toLowerCase() + " to make" + productAmount + " " + product.getName().toLowerCase() + "."));
            return true;
        }

        player.action.execute(craft(player, craftable, item, index, amount), true);
        return true;
    }

    private Action<Player> craft(Player player, Craftable craftable, CraftableItem item, int index, int amount) {
        return new Action<Player>(player, 2, true) {
            int iterations = 0;

            @Override
            public void execute() {
                player.animate(new Animation(craftable.getAnimation()));
                player.skills.addExperience(Skill.CRAFTING, item.getExperience() * Config.CRAFTING_MODIFICATION);

                final boolean saveMaterial = SkillCape.isEquipped(player, SkillCape.CRAFTING) && Utility.random(1, 3) == 1;
                if (!saveMaterial) {
                    player.inventory.removeAll(craftable.getIngredients(index));
                }
                player.inventory.addOrDrop(item.getProduct());
                player.playerAssistant.activateSkilling(1);
                RandomEventHandler.trigger(player);

                if (craftable.getProductionMessage() != null) {
                    player.send(new SendMessage(craftable.getProductionMessage()));
                }

//				if (craftable.getName() == "Gem") {
//					AchievementHandler.activateTask(player, AchievementList.CUT_250_GEMS, 1);
//				}

                if (++iterations == amount) {
                    cancel();
                    return;
                }

                if (!player.inventory.containsAll(craftable.getIngredients(index))) {
                    cancel();
                    player.send(new SendMessage("<col=369>You have run out of materials."));
                    return;
                }
            }

            @Override
            public String getName() {
                return "Crafting";
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
