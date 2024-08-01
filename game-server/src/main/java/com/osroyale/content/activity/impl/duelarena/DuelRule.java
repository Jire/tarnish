package com.osroyale.content.activity.impl.duelarena;

import com.google.common.collect.ImmutableSet;
import com.osroyale.content.activity.Activity;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.ItemDefinition;
import com.osroyale.game.world.items.containers.equipment.Equipment;
import com.osroyale.net.packet.out.SendConfig;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.net.packet.out.SendString;
import com.osroyale.net.packet.out.SendToggle;

import java.util.EnumSet;
import java.util.Optional;

public enum DuelRule {
    NO_RANGED(31034, 631, new RuleCondition()  {
        @Override
        public boolean canSelect(Player player, DuelRules rules) {
            if (rules.contains(DuelRule.NO_MELEE) && rules.contains(DuelRule.NO_MAGIC)) {
                player.send(new SendMessage("You must have at least one combat type checked."));
                return false;
            }
            return true;
        }

    }),

    NO_MELEE(31035, 632, new RuleCondition() {
        @Override
        public boolean canSelect(Player player, DuelRules rules) {
            if (rules.contains(DuelRule.NO_MAGIC) && rules.contains(DuelRule.NO_RANGED)) {
                player.send(new SendMessage("You must have at least one combat type checked."));
                return false;
            }

            if (rules.contains(DuelRule.NO_WEAPON) && rules.contains(DuelRule.NO_MAGIC)) {
                player.message("You can't have No Melee, No Magic, Nothing in your Right Hand, how would you fight?");
                return false;
            }

            if (rules.contains(DuelRule.NO_FORFEIT)) {
                player.send(new SendMessage("You can't have No Forfeit and No Melee, you could run out of ammo."));
                return false;
            }

            if (rules.contains(DuelRule.ONLY_FUN_WEAPONS)) {
                player.send(new SendMessage("You cannot select no melee when only fun weapons selected."));
                return false;
            }

            if (rules.contains(DuelRule.ONLY_WHIP_DDS)) {
                player.send(new SendMessage("You cannot use whip and dragon dagger if melee is off."));
                return false;
            }
            return true;
        }
    }),

    NO_MAGIC(31036, 633, new RuleCondition() {
        @Override
        public boolean canSelect(Player player, DuelRules rules) {
            if (rules.contains(DuelRule.NO_MELEE) && rules.contains(DuelRule.NO_RANGED)) {
                player.send(new SendMessage("You must have at least one combat type checked."));
                return false;
            }

            if (rules.contains(DuelRule.NO_WEAPON) && rules.contains(DuelRule.NO_MELEE)) {
                player.message("You can't have No Melee, No Magic, Nothing in your Right Hand, how would you fight?");
                return false;
            }
            return true;
        }
    }),
    NO_SPECIAL_ATTACK(31037, 634, (player, rules) -> true),
    ONLY_FUN_WEAPONS(31038, 635, new RuleCondition() {
        @Override
        public boolean canSelect(Player player, DuelRules rules) {
            if (rules.contains(DuelRule.NO_WEAPON)) {
                player.send(new SendMessage("You cannot select only fun weapons when no weapons selected."));
                return false;
            }
            return true;
        }
    }),
    NO_FORFEIT(31039, 636, new RuleCondition() {
        @Override
        public boolean canSelect(Player player, DuelRules rules) {
            if (rules.contains(DuelRule.NO_MELEE)) {
                player.send(new SendMessage("You can't have No Forfeit and No Melee, you could run out of ammo."));
                return false;
            }

            if (rules.contains(DuelRule.OBSTACLES)) {
                player.send(new SendMessage("You cannot have no-movement and obstacles on at the same time."));
                return false;
            }
            return true;
        }
    }),
    NO_PRAYER(31040, 637, (player, rules) -> true),
    NO_DRINKS(31041, 638, (player, rules) -> true),
    NO_FOOD(31042, 639, (player, rules) -> true),
    NO_MOVEMENT(31043, 640, new RuleCondition() {
        @Override
        public boolean canSelect(Player player, DuelRules rules) {
            if (rules.contains(DuelRule.OBSTACLES)) {
                player.send(new SendMessage("You cannot have no-movement and obstacles on at the same time."));
                return false;
            }
            return true;
        }
    }),
    OBSTACLES(31044, 641,  new RuleCondition() {
        @Override
        public boolean canSelect(Player player, DuelRules rules) {
            if (rules.contains(DuelRule.NO_MOVEMENT)) {
                player.send(new SendMessage("You cannot have no-movement and obstacles on at the same time."));
                return false;
            }
            if (rules.contains(DuelRule.OBSTACLES)) {
                player.message("Obstacles are permanently disabled.");
                return false;
            }
            player.message("Obstacles are permanently disabled.");
            return false;
        }
    }),
    ONLY_WHIP_DDS(31056, 642, new RuleCondition() {
        @Override
        public boolean canSelect(Player player, DuelRules rules) {
            if (rules.contains(DuelRule.NO_WEAPON)) {
                player.send(new SendMessage("You cannot select only whip and dds when no weapons selected."));
                return false;
            }
            if (rules.contains(DuelRule.LOCK_ITEMS)) {
                player.send(new SendMessage("You cannot select only whip and dds when lock items is selected."));
                return false;
            }
            return true;
        }
    }),
    LOCK_ITEMS(31059, 643, new RuleCondition() {
        @Override
        public boolean canSelect(Player player, DuelRules rules) {
            if (rules.contains(DuelRule.ONLY_WHIP_DDS)) {
                player.send(new SendMessage("You cannot select lock items when only whip and dds is selected."));
                return false;
            }
            return true;
        }
    }),
    NO_HEAD(13813, 16384, Equipment.HELM_SLOT, (player, rules) -> true),
    NO_CAPE(13814, 32768, Equipment.CAPE_SLOT, (player, rules) -> true),
    NO_NECKLACE(13815, 65536, Equipment.AMULET_SLOT, (player, rules) -> true),
    NO_AMMO(13816, 134217728, Equipment.ARROWS_SLOT, (player, rules) -> true),
    NO_WEAPON(13817, 131072, Equipment.WEAPON_SLOT, new RuleCondition() {
        @Override
        public boolean canSelect(Player player, DuelRules rules) {
            if (rules.contains(DuelRule.NO_MELEE) && rules.contains(DuelRule.NO_MAGIC)) {
                player.message("You can't have No Melee, No Magic, Nothing in your Right Hand, how would you fight?");
                return false;
            }

            if (rules.contains(DuelRule.ONLY_FUN_WEAPONS)) {
                player.send(new SendMessage("You cannot select no weapons when only fun weapons selected."));
                return false;
            }

            if (rules.contains(DuelRule.ONLY_WHIP_DDS)) {
                player.send(new SendMessage("You cannot select no weapons when only whip and dds only is selected."));
                return false;
            }
            return true;
        }
    }),
    NO_BODY(13818, 262144, Equipment.CHEST_SLOT,(player, rules) -> true),
    NO_SHIELD(13819, 524288, Equipment.SHIELD_SLOT, (player, rules) -> true),
    NO_LEGS(13820, 2097152, Equipment.LEGS_SLOT, (player, rules) -> true),
    NO_GLOVES(13823, 8388608, Equipment.HANDS_SLOT, (player, rules) -> true),
    NO_BOOTS(13822, 16777216, Equipment.FEET_SLOT, (player, rules) -> true),
    NO_RINGS(13821, 67108864, Equipment.RING_SLOT, (player, rules) -> true);

    /**
     * The button for this rule.
     */
    private final int button;

    /**
     * The value for the config.
     */
    private final int value;

    /**
     * The equipment slot for this rule.
     */
    private final int slot;

    /**
     * The condition for this rule.
     */
    private final RuleCondition condition;

    /**
     * Creates a new {@link DuelRule}.
     *
     * @param button    The button for this rule.
     * @param value     The config id for this rule.
     * @param condition The condition for this rule.
     */
    DuelRule(int button, int value, RuleCondition condition) {
        this(button, value, -1, condition);
    }

    /**
     * Creates a new {@link DuelRule}.
     *
     * @param button    The button for this rule.
     * @param value     The config id for this rule.
     * @param slot      The equipment slot for this rule.
     * @param condition The condition for this rule.
     */
    DuelRule(int button, int value, int slot, RuleCondition condition) {
        this.button = button;
        this.value = value;
        this.slot = slot;
        this.condition = condition;
    }

    public static final ImmutableSet<DuelRule> EQUIPMENT_RULES = ImmutableSet.of(NO_HEAD, NO_CAPE, NO_NECKLACE, NO_AMMO, NO_WEAPON, NO_BODY, NO_SHIELD, NO_LEGS, NO_GLOVES, NO_BOOTS, NO_RINGS);

    public static Optional<DuelRule> forButton(int button) {
        for (DuelRule rules : values()) {
            if (rules.getButtonId() == button) {
                return Optional.of(rules);
            }
        }
        return Optional.empty();
    }

    public boolean canSet(Player player) {
        if (!Activity.search(player, DuelArenaActivity.class).isPresent()) {
            return false;
        }

        DuelArenaActivity activity = Activity.search(player, DuelArenaActivity.class).get();

        final DuelRules rules = activity.rules;

        if (!condition.canSelect(player, rules)) {
            return false;
        }

        Player other = activity.opponent;

        if (inventorySlotsRequired(player, rules, this) > player.inventory.getFreeSlots()) {
            return false;
        }

        if (inventorySlotsRequired(other, rules, this) > other.inventory.getFreeSlots()) {
            return false;
        }

        return true;
    }

    public static void validateEquipmentRules(Player player) {
        if (Activity.search(player, DuelArenaActivity.class).isPresent()) {
            DuelArenaActivity activity = Activity.search(player, DuelArenaActivity.class).get();

            for (DuelRule rule : DuelRule.EQUIPMENT_RULES) {
                if (activity.rules.contains(rule) && !rule.canSet(player)) {
                    activity.rules.unflag(rule);
                }
            }

            player.send(new SendToggle(286, activity.rules.getValue()));
            activity.opponent.send(new SendToggle(286, activity.rules.getValue()));
        }
    }

    public static void showRules(DuelArenaActivity activity, Player player, Player other) {
        for (DuelRule rule : DuelRule.values()) {
            if (!EQUIPMENT_RULES.contains(rule)) {
                player.send(new SendConfig(rule.getValue(), activity.rules.contains(rule) ? 1 : 0));
                other.send(new SendConfig(rule.getValue(), activity.rules.contains(rule) ? 1 : 0));
            }
        }

        player.send(new SendToggle(286, activity.rules.getValue()));
        other.send(new SendToggle(286, activity.rules.getValue()));
    }

    public boolean set(Player player, boolean showMessage) {
        if (!Activity.search(player, DuelArenaActivity.class).isPresent()) {
            return false;
        }

        DuelArenaActivity activity = Activity.search(player, DuelArenaActivity.class).get();

        final DuelRules rules = activity.rules;

        boolean meets = condition.canSelect(player, rules);

        Player other = player.equals(activity.opponent) ? activity.player : activity.opponent;

        if (meets) {
            if (inventorySlotsRequired(player, rules, this) > player.inventory.getFreeSlots()) {
                if (showMessage) {
                    player.send(new SendMessage("You do not have enough inventory space."));
                }
                return false;
            }

            if (inventorySlotsRequired(other, rules, this) > other.inventory.getFreeSlots()) {
                if (showMessage) {
                    player.send(new SendMessage("The other player does not have enough space."));
                }
                return false;
            }

            rules.alternate(this);

            if ((this.getButtonId() >= 31034 && this.getButtonId() <= 31056) || this.getButtonId() == 31059) {
                player.send(new SendConfig(this.getValue(), rules.contains(this) ? 1 : 0));
                other.send(new SendConfig(this.getValue(), rules.contains(this) ? 1 : 0));
            } else {
                player.send(new SendToggle(286, rules.getValue()));
                other.send(new SendToggle(286, rules.getValue()));
            }

            player.send(new SendString("", 31009));
            other.send(new SendString("", 31009));
        }
        return meets;
    }

    static int inventorySlotsRequired(Player player, DuelRules rules, DuelRule rule) {
        int required = 0;

        EnumSet<DuelRule> tempRules = EnumSet.copyOf(rules.getFlags());
        tempRules.add(rule);

        if (player.equipment.hasHead()) {
            if (tempRules.contains(DuelRule.NO_HEAD)) {
                required++;
            }
        }

        if (player.equipment.hasAmulet()) {
            if (tempRules.contains(DuelRule.NO_NECKLACE)) {
                required++;
            }
        }

        if (player.equipment.hasWeapon()) {
            Item item = player.equipment.get(Equipment.WEAPON_SLOT);
            if (item != null) {
                String name = item.getName().toLowerCase();

                if (tempRules.contains(DuelRule.NO_WEAPON)) {
                    required++;
                }

                if (tempRules.contains(DuelRule.ONLY_WHIP_DDS)) {
                    if (!(name.contains("whip") || name.contains("abyssal tentacle") || name.contains("dragon dagger"))) {
                        required++;
                    }
                }
            }
        }

        if (player.equipment.hasCape()) {
            if (tempRules.contains(DuelRule.NO_CAPE)) {
                required++;
            }
        }

        if (player.equipment.hasAmmo()) {
            if (tempRules.contains(DuelRule.NO_AMMO)) {
                required++;
            }
        }

        if (player.equipment.hasChest()) {
            if (tempRules.contains(DuelRule.NO_BODY)) {
                required++;
            }
        }

        if (tempRules.contains(DuelRule.NO_SHIELD)) {

            if (player.equipment.hasShield()) {
                required++;
            }

            Item weapon = player.equipment.get(Equipment.WEAPON_SLOT);

            if (weapon != null) {

                ItemDefinition def = ItemDefinition.get(weapon.getId());

                if (def != null && def.isTwoHanded()) {
                    required++;
                }

            }
        }

        if (player.equipment.hasLegs()) {
            if (tempRules.contains(DuelRule.NO_LEGS)) {
                required++;
            }
        }

        if (player.equipment.hasHands()) {
            if (tempRules.contains(DuelRule.NO_GLOVES)) {
                required++;
            }
        }

        if (player.equipment.hasFeet()) {
            if (tempRules.contains(DuelRule.NO_BOOTS)) {
                required++;
            }
        }

        if (player.equipment.hasRing()) {
            if (tempRules.contains(DuelRule.NO_RINGS)) {
                required++;
            }
        }

        tempRules.clear();
        return required;
    }

    /**
     * @return the buttonId
     */
    public int getButtonId() {
        return button;
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * @return the slot
     */
    public int getSlot() {
        return slot;
    }

    /**
     * @return the condition
     */
    public RuleCondition getCondition() {
        return condition;
    }

    public interface RuleCondition {

        boolean canSelect(final Player player, final DuelRules rules);

    }


}
