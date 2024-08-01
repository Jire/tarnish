package com.osroyale.content.skill.impl.crafting.impl;

import com.osroyale.Config;
import com.osroyale.net.packet.out.SendInputAmount;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.game.Animation;
import com.osroyale.game.action.Action;
import com.osroyale.game.action.policy.WalkablePolicy;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.ItemDefinition;
import com.osroyale.util.Utility;

import java.util.Arrays;
import java.util.Optional;

/**
 * Handles the glass crafting.
 *
 * @author Daniel
 */
public class Glass {

    /** Holds all the glass data.  */
    public enum GlassData {
        VIAL(229, 33, 35.0D, 1775),
        LIGHT_ORB(10973, 87, 70.0D, 1775),
        BEER_GLASS(1919, 1, 17.5D, 1775),
        CANDLE_LANTERN(4527, 4, 19.0D, 1775),
        OIL_LAMP(4522, 12, 25.0D, 1775),
        FISHBOWL(6667, 42, 42.5D, 1775),
        LANTERN_LENS(4542, 49, 55.0D, 1775),
        MOLTEN_GLASS(1775, 1, 20.0D, 1781),
        UNPOWDERED_ORD(567, 46, 52.5D, 1775);

        /** The glass product.  */
        private final int product;

        /**  The level required. */
        private final int level;

        /**  The experienced rewarded.  */
        private final double experience;

        /** The material required. */
        private final int material;

        /** Constructs a new <code>GlassData</code>. */
        GlassData(int product, int level, double experience, int material) {
            this.product = product;
            this.level = level;
            this.experience = experience;
            this.material = material;
        }

        /** Gets the glass data based on the material item. */
        public static Optional<GlassData> forGlass(int item) {
            return Arrays.stream(values()).filter(g -> g.material == item).findFirst();
        }
    }

    /** Handles opening the glass crafting interface. */
    public static void open(Player player) {
        player.interfaceManager.open(11462);
    }

    /** Handles clicking on the interface. */
    public static boolean click(Player player, int button) {
        switch (button) {
            /** Vial */
            case 11474: craft(player, GlassData.VIAL, 1); return true;
            case 11473: craft(player, GlassData.VIAL, 5); return true;
            case 11472: craft(player, GlassData.VIAL, 10); return true;
            case 11471:
                player.send(new SendInputAmount("Enter the amount you would like to craft", 2, input ->
                        craft(player, GlassData.VIAL, Integer.parseInt(input))
                ));
                return true;
            /** Orb */
            case 12396: craft(player, GlassData.UNPOWDERED_ORD, 1); return true;
            case 12395: craft(player, GlassData.UNPOWDERED_ORD, 5); return true;
            case 12394: craft(player, GlassData.UNPOWDERED_ORD, 10); return true;
            case 11475:
                player.send(new SendInputAmount("Enter the amount you would like to craft", 2, input ->
                        craft(player, GlassData.UNPOWDERED_ORD, Integer.parseInt(input))
                ));
                return true;
            /** Beer glass */
            case 12400: craft(player, GlassData.BEER_GLASS, 1); return true;
            case 12399: craft(player, GlassData.BEER_GLASS, 5); return true;
            case 12398: craft(player, GlassData.BEER_GLASS, 10); return true;
            case 12397:
                player.send(new SendInputAmount("Enter the amount you would like to craft", 2, input ->
                        craft(player, GlassData.BEER_GLASS, Integer.parseInt(input))
                ));
                return true;
            /** Candle lantern */
            case 12404: craft(player, GlassData.CANDLE_LANTERN, 1); return true;
            case 12403: craft(player, GlassData.CANDLE_LANTERN, 5); return true;
            case 12402: craft(player, GlassData.CANDLE_LANTERN, 10); return true;
            case 12401:
                player.send(new SendInputAmount("Enter the amount you would like to craft", 2, input ->
                        craft(player, GlassData.CANDLE_LANTERN, Integer.parseInt(input))
                ));
                return true;
            /** Oil lamp */
            case 12408: craft(player, GlassData.OIL_LAMP, 1); return true;
            case 12407: craft(player, GlassData.OIL_LAMP, 5); return true;
            case 12406: craft(player, GlassData.OIL_LAMP, 10); return true;
            case 12405:
                player.send(new SendInputAmount("Enter the amount you would like to craft", 2, input ->
                        craft(player, GlassData.OIL_LAMP, Integer.parseInt(input))
                ));
                return true;
            /** Fishbowl */
            case 6203: craft(player, GlassData.FISHBOWL, 1); return true;
            case 6202: craft(player, GlassData.FISHBOWL, 5); return true;
            case 6201: craft(player, GlassData.FISHBOWL, 10); return true;
            case 6200:
                player.send(new SendInputAmount("Enter the amount you would like to craft", 2, input ->
                        craft(player, GlassData.FISHBOWL, Integer.parseInt(input))
                ));
                return true;
            /** Lantern lens */
            case 12412: craft(player, GlassData.LANTERN_LENS, 1); return true;
            case 12411: craft(player, GlassData.LANTERN_LENS, 5); return true;
            case 12410: craft(player, GlassData.LANTERN_LENS, 10); return true;
            case 12409:
                player.send(new SendInputAmount("Enter the amount you would like to craft", 2, input ->
                        craft(player, GlassData.LANTERN_LENS, Integer.parseInt(input))
                ));
                return true;
        }
        return false;
    }

    /** Handles crafting the glass.  */
    public static void craft(Player player, GlassData glass, int amount) {
        if (player.skills.getMaxLevel(Skill.CRAFTING) < glass.level) {
            player.send(new SendMessage("You need a crafting level of " + glass.level + " to craft this!"));
            return;
        }

        if (!player.inventory.contains(1785) && glass != GlassData.MOLTEN_GLASS) {
            player.send(new SendMessage("You need a glassblowing pipe to do this!"));
            return;
        }

        if (!player.inventory.contains(glass.material)) {
            String name = ItemDefinition.get(glass.material).getName();
            player.send(new SendMessage("You need " + Utility.getAOrAn(name) + " " + name + " to do this!"));
            return;
        }

        player.interfaceManager.close();
        player.action.execute(blow(player, glass, amount), true);
    }

    /** Handles blowing the glass data. */
    private static Action<Player> blow(Player player, GlassData glass, int amount) {
        return new Action<Player>(player, 3, true) {
            int ticks = 0;

            @Override
            public void execute() {
                boolean moltenGlass = glass == GlassData.MOLTEN_GLASS;

                if (moltenGlass && (!player.inventory.contains(1783) || !player.inventory.contains(1781))) {
                    player.send(new SendMessage("You need a bucket of sand and soda ash to make molten glass!"));
                    cancel();
                    return;
                }

                if (!player.inventory.contains(1785) && !moltenGlass) {
                    player.send(new SendMessage("You need a glassblowing pipe to do this!"));
                    cancel();
                    return;
                }

                if (!player.inventory.contains(glass.material)) {
                    String name = ItemDefinition.get(glass.material).getName();
                    player.send(new SendMessage("You need " + Utility.getAOrAn(name) + " " + name + " to do this!"));
                    cancel();
                    return;
                }

                player.inventory.remove(glass.material, 1);

                if (moltenGlass) {
                    player.inventory.replace(1783, 1925, true);
                }

                String name = ItemDefinition.get(glass.product).getName();
                player.animate(new Animation(moltenGlass ? 899 : 884));
                player.inventory.add(new Item(glass.product));
                player.skills.addExperience(Skill.CRAFTING, glass.experience * Config.CRAFTING_MODIFICATION);
                player.send(new SendMessage("You make " + Utility.getAOrAn(name) + " " + name + "."));

                if (++ticks == amount) {
                    cancel();
                    return;
                }
            }

            @Override
            public String getName() {
                return "Glass";
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
