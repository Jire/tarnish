package com.osroyale.content.skill.impl.smithing;

import com.google.common.collect.ImmutableMap;
import com.osroyale.Config;
import com.osroyale.content.skill.impl.ProducingSkillAction;
import com.osroyale.content.skill.impl.slayer.SlayerUnlockable;
import com.osroyale.content.skillcape.SkillCape;
import com.osroyale.game.Animation;
import com.osroyale.game.action.policy.WalkablePolicy;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.object.GameObject;
import com.osroyale.net.packet.out.SendItemOnInterfaceSlot;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.net.packet.out.SendString;
import com.osroyale.util.StringUtils;

import java.util.Optional;

/**
 * Holds functionality for creating items on an anvil.
 *
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class SmithingArmour extends ProducingSkillAction {

    private static final ImmutableMap<Integer, SmithingTable[]> TABLE = ImmutableMap.<Integer, SmithingTable[]>builder().put(2349, SmithingTable.BronzeTable.values()).put(2351, SmithingTable.IronTable.values()).put(2353, SmithingTable.SteelTable.values()).put(2359, SmithingTable.MithrilTable.values()).put(2361, SmithingTable.AdamantTable.values()).put(2363, SmithingTable.RuniteTable.values()).build();
    private static final int[][] FRAME_DATA = {{1125, 1094, 0, 1119}, {1126, 1091, 0, 1120}, {1109, 1098, 0, 1121}, {1127, 1102, 0, 1122}, {1128, 1107, 0, 1123}, {1124, 1085, 1, 1119}, {1129, 1093, 1, 1120}, {1110, 1099, 1, 1121}, {1113, 1103, 1, 1122}, {1130, 1108, 1, 1123}, {1116, 1087, 2, 1119}, {1118, 1083, 2, 1120}, {1111, 1100, 2, 1121}, {1114, 1104, 2, 1122}, {1131, 1106, 2, 1123}, {1089, 1086, 3, 1119}, {1095, 1092, 3, 1120}, {1112, 1101, 3, 1121}, {1115, 1105, 3, 1122}, {1132, 1096, 3, 1123}, {1090, 1088, 4, 1119}, {8428, 8429, 4, 1120}, {11459, 11461, 4, 1121}, {13357, 13358, 4, 1122}, {1135, 1134, 4, 1123},};

    /** The definition for this table. */
    private final SmithingTable definition;

    /**  The amount being created.  */
    private int amount;

    /** Constructs a new {@link Smithing} skill.  */
    private SmithingArmour(Player player, SmithingTable definition, int amount) {
        super(player, Optional.empty(), SkillCape.isEquipped(player, SkillCape.SMITHING) ? 2 : 4, true);
        this.definition = definition;
        this.amount = amount;
    }

    /** Attempts to forge the item clicked for the specified {@code player}. */
    static boolean forge(Player player, int interfaceId, int slot, int amount) {
        if (player.attributes.get("smithing_equipment") == null) {
            return false;
        }
        SmithingTable[] values = TABLE.get(((Item) player.attributes.get("smithing_equipment")).getId());

        if (values == null) {
            return false;
        }

        SmithingTable table = null;

        for (int i = 0; i < FRAME_DATA.length; i++) {
            if (FRAME_DATA[i][3] == interfaceId && slot == FRAME_DATA[i][2]) {
                if (i >= values.length)
                    break;
                table = values[i];
                break;
            }
        }

        if (table == null) {
            return false;
        }

        if(table.getProduced().getId() == 2 && !player.slayer.getUnlocked().contains(SlayerUnlockable.CANNON_BALLS)) {
            player.message("You need to unlock the ability to make cannon balls first.");
            return false;
        }
        if(table.getProduced().getId() == 2 && !player.inventory.contains(4)) {
            player.message("You need a mould to make cannon balls.");
            return false;
        }

        SmithingArmour smithing = new SmithingArmour(player, table, amount);
        smithing.start();
        return true;
    }

    /** Opens the smithing itemcontainer and sets all the values for  the specified {code player}.  */
    static boolean openInterface(Player player, Item item, GameObject object) {
        if (object.getDefinition().getId() != 2097 && object.getDefinition().getId() != 2672) {
            return false;
        }

        if (!player.inventory.containsAny(2347, 2949)) {
            player.send(new SendMessage("You need a hammer to forge items."));
            return true;
        }

        if (item.getId() == 11286) {
            if (!player.inventory.containsAll(11286, 1540)) {
                player.send(new SendMessage("You need a dragonic visage and an anti-dragonfire shield."));
                return true;
            }
            if (!player.skills.get(Skill.SMITHING).reqLevel(85)) {
                player.send(new SendMessage("You need a level of 85 smithing to do this."));
                return true;
            }
            player.animate(new Animation(898));
            player.skills.addExperience(Skill.SMITHING, 350 * Config.SMITHING_MODIFICATION);
            player.inventory.removeAll(new Item(11286), new Item(1540));
            player.inventory.add(new Item(11283));
            return true;
        }

        if (item.getId() == 22006) {
            if (!player.inventory.containsAll(22006, 1540)) {
                player.send(new SendMessage("You need a skeletal visage and an anti-dragonfire shield."));
                return true;
            }
            if (!player.skills.get(Skill.SMITHING).reqLevel(90)) {
                player.send(new SendMessage("You need a level of 90 smithing to do this."));
                return true;
            }
            player.animate(new Animation(898));
            player.skills.addExperience(Skill.SMITHING, 2000 * Config.SMITHING_MODIFICATION);
            player.inventory.removeAll(new Item(22006), new Item(1540));
            player.inventory.add(new Item(22003));
            return true;
        }

        //Godsword blade creation.
        if (item.getId() == 11794 || item.getId() == 11796 || item.getId() == 11798) {
            if (!player.inventory.containsAll(11794, 11796, 11798)) {
                player.send(new SendMessage("You do not have all the godsword shards."));
                return true;
            }
            if (!player.skills.get(Skill.SMITHING).reqLevel(80)) {
                player.send(new SendMessage("You need a level of 80 smithing to do this."));
                return true;
            }
            player.animate(new Animation(898));
            player.skills.addExperience(Skill.SMITHING, 200);
            player.inventory.removeAll(new Item(11794), new Item(11796), new Item(11798));
            player.inventory.add(new Item(11798));
            return true;
        }

        SmithingTable[] values = TABLE.get(item.getId());

        if (values == null) {
            return false;
        }

        SmithingTable table = null;

        for (int i = 0; i < FRAME_DATA.length; i++) {
            if (i >= values.length) {
                player.send(new SendString("", FRAME_DATA[i][0]));
                player.send(new SendString("", FRAME_DATA[i][1]));
                player.send(new SendItemOnInterfaceSlot(FRAME_DATA[i][3], new Item(-1, 0), FRAME_DATA[i][2]));
                continue;
            }

            table = values[i];

            if (table == null || table.getBar() == null) {
                return false;
            }

            final boolean has_bar = player.inventory.computeAmountForId(table.getBar().getId()) >= table.getBarsRequired();
            final String bar_color = !has_bar ? "@red@" : "@gre@";
            final String name_color = player.skills.get(Skill.SMITHING).getMaxLevel() >= table.getLevelRequirement() && has_bar ? "@whi@" : "@bla@";

            player.send(new SendString(bar_color + table.getBarsRequired() + " Bar" + (table.getBarsRequired() != 1 ? "s" : ""), FRAME_DATA[i][0]));
            player.send(new SendString(name_color + StringUtils.capitalize(table.getName().toLowerCase()), FRAME_DATA[i][1]));
            player.send(new SendItemOnInterfaceSlot(FRAME_DATA[i][3], table.getProduced(), FRAME_DATA[i][2]));
        }
        if (table == null || table.getBar() == null) {
            return false;
        }
        player.attributes.set("smithing_equipment", table.getBar());
        player.interfaceManager.open(994);
        return true;
    }

    @Override
    public void onProduce(boolean success) {
        if (success) {
            getMob().getPlayer().animate(new Animation(898));
            amount--;
            if (amount < 1)
                this.cancel();
        }
    }

    @Override
    public void onCancel(boolean logout) {
        getMob().attributes.remove("smithing_equipment");
    }

    @Override
    public Optional<Item[]> removeItem() {
        return Optional.of(new Item[]{new Item(definition.getBar().getId(), definition.getBarsRequired())});
    }

    @Override
    public Optional<Item[]> produceItem() {
        return Optional.of(new Item[]{definition.getProduced()});
    }

    @Override
    public boolean canInit() {
        if (!getMob().skills.get(Skill.SMITHING).reqLevel(definition.getLevelRequirement())) {
            getMob().getPlayer().send(new SendMessage("You need a smithing level of " + definition.getLevelRequirement() + " to smith " + StringUtils.appendIndefiniteArticle(definition.getName())));
            return false;
        }
        return true;
    }

    @Override
    public void init() {
        getMob().getPlayer().interfaceManager.close();
    }

    @Override
    public Optional<SkillAnimation> animation() {
        return Optional.empty();
    }

    @Override
    public double experience() {
        return definition.getExperience() * Config.SMITHING_MODIFICATION;
    }

    @Override
    public int skill() {
        return Skill.SMITHING;
    }

    @Override
    public boolean prioritized() {
        return false;
    }

    @Override
    public WalkablePolicy getWalkablePolicy() {
        return WalkablePolicy.NON_WALKABLE;
    }

    @Override
    public String getName() {
        return "smithing_armour";
    }
}
