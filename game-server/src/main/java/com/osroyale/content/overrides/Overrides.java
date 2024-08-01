package com.osroyale.content.overrides;

import com.osroyale.game.world.entity.combat.attack.FightStyle;
import com.osroyale.game.world.entity.combat.attack.FightType;
import com.osroyale.game.world.entity.combat.weapon.WeaponInterface;
import com.osroyale.game.world.entity.mob.UpdateFlag;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.containers.equipment.Equipment;
import com.osroyale.game.world.items.containers.equipment.EquipmentType;
import com.osroyale.game.world.position.Area;
import com.osroyale.net.packet.out.*;

import java.util.*;

public class Overrides {

    private final Player player;
    public Overrides(Player player) {
        this.player = player;
    }

    public record OverridePreset(Item... items) {}
    public Map<String, OverridePreset> presets = new HashMap<>();

    public List<Integer> allOverrides = new ArrayList<>();
    public Map<Integer, Item> currentOverrides = new HashMap<>();

    final Map<Integer, Integer> buttons = new HashMap<>();
    final Map<Integer, String> presetButtons = new HashMap<>();

    EquipmentType filter = EquipmentType.NOT_WIELDABLE;
    String filterName = "All";
    boolean managingOverrides = false;

    public void openInterface() {
        if (Area.inWilderness(player)) {
            player.message("You can't do this in the wilderness.");
            return;
        }
        filter = EquipmentType.NOT_WIELDABLE;
        filterName = "All";
        drawText();
        loadContainers();
        player.interfaceManager.open(OVERRIDE_INTERFACE);
    }

    private void drawText() {
        wipeInterface();
        player.send(new SendString(filterName + " Overrides", 60113));

        if (filterName.equals("Preset")) {
            drawPresets();
            return;
        }

        int slot = 0;
        for (int i = 0; i < allOverrides.size(); i++) {
            final var equipmentSlot = new Item(allOverrides.get(i)).getDefinition().getEquipmentType().getNewItemDefName();
            if (!Objects.equals(equipmentSlot, filter.getNewItemDefName()) && filter != EquipmentType.NOT_WIELDABLE) {
                continue;
            }

            final var itemName = new Item(allOverrides.get(i)).getName();
            player.send(new SendString(itemName, 60146 + (slot * 2)));
            buttons.put(-5391 + (slot * 2), allOverrides.get(i));
            slot++;
        }
    }

    private void drawPresets() {
        for (int i = 0; i < presets.size(); i++) {
            final var presetName = presets.keySet().toArray()[i].toString();
            player.send(new SendString(presetName, 60146 + (i * 2)));
            presetButtons.put(-5391 + (i * 2), presetName);
        }
    }

    private void wipeInterface() {
        for (int i = 0; i < allOverrides.size() + 1; i++) {
            player.send(new SendString("", 60146 + (i * 2)));
        }
        buttons.clear();
        presetButtons.clear();
    }

    public void addOverride(int itemId) {
        final var item = new Item(itemId);
        final var canEquipItem = item.isEquipable();
        if (!canEquipItem) {
            player.dialogueFactory.sendItem("Keepsake Overrides", "You can only keepsake equipable items.", itemId).execute();
            return;
        }
        final var unallowedSlot = item.getEquipmentType() == EquipmentType.RING || item.getEquipmentType() == EquipmentType.ARROWS;
        if (unallowedSlot) {
            player.dialogueFactory.sendItem("Keepsake Overrides", "You cannot keepsake this item.", itemId).execute();
            return;
        }
        if (allOverrides.contains(itemId)) {
            player.dialogueFactory.sendItem("Keepsake Overrides", "You already have this as an override.", itemId).execute();
            return;
        }
        if (allOverrides.size() > 44) {
            player.dialogueFactory.sendItem("Keepsake Overrides", "You can only have 45 overrides.", itemId).execute();
            return;
        }
        if (Area.inWilderness(player)) {
            player.message("You can't do this in the wilderness.");
            return;
        }

        player.dialogueFactory.sendStatement(
                "Are you sure you want to keepsake the " + item.getName() + "?", "You will @red@not@bla@ be able to reclaim it.").sendOption(
                "Yes, keepsake the " + item.getName(), () -> {
                    allOverrides.add(itemId);
                    player.inventory.remove(2399, 1);
                    player.inventory.remove(itemId, 1);
                },
                "Nevermind", () -> player.dialogueFactory.clear()).execute();
    }

    private void deleteOverride(Item item) {
        if (!allOverrides.contains(item.getId())) {
            player.message("Error deleting override.");
            return;
        }

        allOverrides.remove(Integer.valueOf(item.getId()));

        //Remove override if it is currently equipped
        if (currentOverrides.containsValue(item)) {
            removeOverride(item.getId());
        }

        //Remove preset if it contains the deleted override
        for (int i = 0; i < presets.size(); i++) {
            final var preset = presets.get(presets.keySet().toArray()[i].toString());
            if (Arrays.stream(preset.items).anyMatch(presetItem -> presetItem != null && presetItem.getId() == item.getId())) {
                presets.remove(presets.keySet().toArray()[i].toString());
                player.message("A preset has been deleted as it contained a deleted override.");
            }
        }

        drawText();
    }

    public boolean hasOverride(int slot) {
        if (Area.inWilderness(player)) {
            return false;
        }
        if (Area.inDuelArena(player)) {
            return false;
        }
        if (!currentOverrides.containsKey(slot)) {
            return false;
        }

        if (slot == Equipment.WEAPON_SLOT) {
            return hasWeaponOverride();
            /*if (player.equipment.get(Equipment.WEAPON_SLOT) != null) {
                final var wornWeapon = player.equipment.get(Equipment.WEAPON_SLOT).getRangedDefinition();
                final var overrideWeapon = currentOverrides.get(Equipment.WEAPON_SLOT).getRangedDefinition();
                return (wornWeapon.isPresent() || overrideWeapon.isEmpty()) && (overrideWeapon.isPresent() || wornWeapon.isEmpty());
            } else {
                return true;
            }*/
        }

        if (slot == Equipment.SHIELD_SLOT) {
            return hasShieldOverride();
           /* if (player.equipment.get(Equipment.WEAPON_SLOT) != null) {
                final var wornWeapon = player.equipment.get(Equipment.WEAPON_SLOT);
                if (currentOverrides.containsKey(Equipment.WEAPON_SLOT)) {
                    if (wornWeapon.getRangedDefinition().isPresent() && wornWeapon.isTwoHanded() && currentOverrides.get(Equipment.WEAPON_SLOT).getRangedDefinition().isEmpty()) {
                        return false;
                    }
                    return !currentOverrides.get(Equipment.WEAPON_SLOT).isTwoHanded();
                }
                return !wornWeapon.isTwoHanded();
            } else if (currentOverrides.containsKey(Equipment.WEAPON_SLOT)) {
                return !currentOverrides.get(Equipment.WEAPON_SLOT).isTwoHanded();
            } else {
                return true;
            }*/
        }

        return currentOverrides.containsKey(slot);
    }

    private boolean hasWeaponOverride() {
        final var hasWeapon = player.equipment.get(Equipment.WEAPON_SLOT) != null;
        final var weaponIs2h = hasWeapon && player.equipment.get(Equipment.WEAPON_SLOT).isTwoHanded();
        final var weaponIsRanged = hasWeapon && player.equipment.get(Equipment.WEAPON_SLOT).getRangedDefinition().isPresent();

        final var hasOverride = currentOverrides.containsKey(Equipment.WEAPON_SLOT);
        final var overrideIs2h = hasOverride && currentOverrides.get(Equipment.WEAPON_SLOT).isTwoHanded();
        final var overrideIsRanged = hasOverride && currentOverrides.get(Equipment.WEAPON_SLOT).getRangedDefinition().isPresent();

        if (hasWeapon) {
            if (weaponIs2h && !overrideIs2h || overrideIs2h && !weaponIs2h) {
                return false;
            }
            if (weaponIsRanged && !overrideIsRanged || overrideIsRanged && !weaponIsRanged) {
                return false;
            }
        }

        return hasOverride;
    }

    private boolean hasShieldOverride() {
        final var hasWeapon = player.equipment.get(Equipment.WEAPON_SLOT) != null;
        final var weaponIs2h = hasWeapon && player.equipment.get(Equipment.WEAPON_SLOT).isTwoHanded();

        final var hasOverrideWeapon = currentOverrides.containsKey(Equipment.WEAPON_SLOT);
        final var overrideIs2h = hasOverrideWeapon && currentOverrides.get(Equipment.WEAPON_SLOT).isTwoHanded();

        if (weaponIs2h) {
            return false;
        }
        if (overrideIs2h) {
            return false;
        }

        return currentOverrides.containsKey(Equipment.SHIELD_SLOT);
    }

    public Item get(int slot) {
        return currentOverrides.get(slot);
    }

    private void createPreset(String presetName) {
        if (presets.containsKey(presetName)) {
            player.message("You already have a preset named " + presetName + ".");
            return;
        }
        Item[] presetItems = new Item[11];
        currentOverrides.forEach((k, v) -> presetItems[k] = v);

        presets.put(presetName, new OverridePreset(presetItems));
        drawText();
    }

    private void loadPreset(String presetName) {
        if (!presets.containsKey(presetName)) {
            player.message("Error loading preset: " + presetName);
            return;
        }

        removeAllOverrides();

        for (Item item : presets.get(presetName).items) {
            if (item == null) {
                continue;
            }
            equipOverride(item);
        }
    }

    private void renamePreset(String currentName, String newName) {
        if (!presets.containsKey(currentName)) {
            player.message("Error renaming preset: " + currentName);
            return;
        }
        if (presets.containsKey(newName)) {
            player.message("You already have a preset named " + newName + ".");
            return;
        }

        presets.put(newName, presets.get(currentName));
        presets.remove(currentName);
        player.message("Preset renamed to " + newName + ".");
        drawText();
    }

    private void deletePreset(String presetName) {
        if (!presets.containsKey(presetName)) {
            player.message("Error deleting preset: " + presetName);
            return;
        }

        presets.remove(presetName);
        player.message("Preset deleted.");
        drawText();
    }

    public FightType getFightType(Item item) {
        WeaponInterface weapon = item == null ? null : item.getWeaponInterface();
        if (weapon == null) weapon = WeaponInterface.UNARMED;

        FightType[] oldTypes = player.getWeapon().getFightTypes();
        FightType[] newTypes = weapon.getFightTypes();
        FightType result = null;

        for (int index = 0; index < oldTypes.length; index++) {
            if (newTypes.length == index) {
                break;
            }

            if (newTypes[index].getStyle().equals(FightStyle.DEFENSIVE)
                    && player.getCombat().getFightType().getStyle().equals(FightStyle.DEFENSIVE)) {
                result = newTypes[index];
                break;
            }

            if (oldTypes[index] == player.getCombat().getFightType()) {
                boolean oldControlled = oldTypes[index].getStyle().equals(FightStyle.CONTROLLED);
                boolean newControlled = newTypes[index].getStyle().equals(FightStyle.CONTROLLED);
                if (newControlled != oldControlled) {
                    continue;
                }
                result = newTypes[index];
                break;
            }
        }

        if (result == null) {
            if (player.getCombat().getFightType().getStyle().equals(FightStyle.DEFENSIVE)) {
                result = newTypes[newTypes.length - 1];
            } else {
                result = newTypes[1];
            }
        }
        return result;
    }

    public boolean handleButtons(int button) {
        if (presetButtons.containsKey(button)) {
            final var presetName = presetButtons.get(button);
            if (managingOverrides) {
                managePresetDialogue(presetName);
                return true;
            }
            loadPreset(presetName);
            return true;
        }
        if (buttons.containsKey(button)) {
            final var item = new Item(buttons.get(button));
            if (managingOverrides) {
                manageItemDialogue(item);
                return true;
            }
            equipOverride(item);
            return true;
        }
        switch (button) {
            case INFO_BTN -> player.interfaceManager.open(INFO_INTERFACE);
            case INFO_CLOSE_BTN -> player.interfaceManager.close();
            case VIEW_ALL_BTN, INFO_GO_BACK_BTN, OPEN_OVERRIDE_BTN -> openInterface();
            case HEAD_SLOT_BTN -> setFilter(EquipmentType.HELM, "Head");
            case AMULET_SLOT_BTN -> setFilter(EquipmentType.AMULET, "Amulet");
            case BODY_SLOT_BTN -> setFilter(EquipmentType.BODY, "Body");
            case LEGS_SLOT_BTN -> setFilter(EquipmentType.LEGS, "Leg");
            case BOOTS_SLOT_BTN -> setFilter(EquipmentType.BOOTS, "Boot");
            case CAPE_SLOT_BTN -> setFilter(EquipmentType.CAPE, "Cape");
            case WEAPON_SLOT_BTN -> setFilter(EquipmentType.WEAPON, "Weapon");
            case GLOVES_SLOT_BTN -> setFilter(EquipmentType.GLOVES, "Glove");
            case SHIELD_SLOT_BTN -> setFilter(EquipmentType.SHIELD, "Shield");
            case VIEW_PRESET_BTN -> setFilter(EquipmentType.NOT_WIELDABLE, "Preset");
            case MANAGE_BTN -> manageModeDialogue();

            case SAVE_PRESET_BTN -> {
                if (presets.size() > 4) {
                    player.message("You already have the maximum amount of presets saved.");
                    return true;
                }
                player.send(new SendInputMessage("Enter name for preset:", 30, this::createPreset));
                return true;
            }
        }
        return false;
    }

    private void manageModeDialogue() {
        player.dialogueFactory.sendOption(
            "Toggle manage mode to " + (managingOverrides ? "@red@off@bla@" : "@gre@on@bla@"), () -> managingOverrides = !managingOverrides,

            "What is manage mode?", () -> player.dialogueFactory.sendStatement(
                    "Manage mode allows you to manage your overrides and presets.",
                    "When manage mode is on, clicking an override or preset will",
                    "open a menu allowing you to equip, delete, or rename it."),

            "Nevermind", () -> player.dialogueFactory.clear()).execute();
    }

    private void manageItemDialogue(Item item) {
        player.dialogueFactory.sendOption(
            "Equip <col=255>" + item.getName()+"</col>", () -> equipOverride(item),

            "@red@Delete " + item.getName(), () -> player.dialogueFactory.sendStatement(
                    "Are you sure you want to delete this override?", "The item will be @red@permanently lost@bla@.").sendOption(
                    "Yes, permanently delete " + item.getName(), () -> deleteOverride(item),
                    "Nevermind", () -> player.dialogueFactory.clear()),

            "Nevermind", () -> player.dialogueFactory.clear()).execute();
    }

    private void managePresetDialogue(String presetName) {
        player.dialogueFactory.sendOption(
                "Equip <col=255>" + presetName + "</col>", () -> loadPreset(presetName),

                "Rename preset", () -> {
                    player.dialogueFactory.clear();
                    player.dialogueFactory.onAction(() -> player.send(new SendInputMessage("Enter new preset name:", 30, input -> renamePreset(presetName, input))));
                },

                "@red@Delete " + presetName, () -> player.dialogueFactory.sendStatement(
                        "Are you sure you want to delete this preset?").sendOption(
                        "Yes, delete " + presetName, () -> deletePreset(presetName),
                        "Nevermind", () -> player.dialogueFactory.clear()),

                "Nevermind", () -> player.dialogueFactory.clear()).execute();
    }

    private void setFilter(EquipmentType equipmentType, String name) {
        filter = equipmentType;
        filterName = name;
        drawText();
    }

    public void removeOverride(int itemId) {
        player.send(toggleConfig(itemId, "on"));
        player.send(new SendItemOnInterface(determineContainer(itemId), new Item(-1)));

        final var equipmentType = new Item(itemId).getDefinition().getEquipmentType();
        currentOverrides.remove(equipmentType.getSlot());
        player.updateFlags.add(UpdateFlag.APPEARANCE);
        player.equipment.updateAnimation();
    }

    private void removeAllOverrides() {
        while (!currentOverrides.isEmpty()) {
            removeOverride(currentOverrides.get(currentOverrides.keySet().iterator().next()).getId());
        }
    }

    private void equipOverride(Item item) {
        player.send(toggleConfig(item.getId(), "off"));
        player.send(new SendItemOnInterface(determineContainer(item.getId()), item));

        final var equipmentType = item.getDefinition().getEquipmentType();
        currentOverrides.remove(equipmentType.getSlot());
        currentOverrides.put(equipmentType.getSlot(), item);
        player.updateFlags.add(UpdateFlag.APPEARANCE);
        player.equipment.updateAnimation();
    }

    /* This is used to reload the equipment config buttons & item containers. This is because if a player closes
    * their client and reopens, the configs/items will not show even if they have overrides active. */
    private void loadContainers() {
        currentOverrides.forEach((slot, item) -> {
            player.send(toggleConfig(item.getId(), "off"));
            player.send(new SendItemOnInterface(determineContainer(item.getId()), item));
        });
    }

    private SendConfig toggleConfig(int itemId, String toggle) {
        final var configValue = toggle.equals("on") ? 0 : 1;
        final var equipmentSlot = new Item(itemId).getDefinition().getEquipmentType();
        return switch (equipmentSlot) {
            case HAT, HELM, MASK, FACE -> new SendConfig(1300, configValue);
            case CAPE -> new SendConfig(1301, configValue);
            case AMULET -> new SendConfig(1302, configValue);
            case WEAPON -> new SendConfig(1303, configValue);
            case BODY, TORSO -> new SendConfig(1304, configValue);
            case SHIELD -> new SendConfig(1305, configValue);
            case LEGS -> new SendConfig(1306, configValue);
            case BOOTS -> new SendConfig(1307, configValue);
            case GLOVES -> new SendConfig(1308, configValue);
            default -> null;
        };
    }

    private int determineContainer(int itemId) {
        final var equipmentSlot = new Item(itemId).getDefinition().getEquipmentType();
        return switch (equipmentSlot) {
            case HAT, HELM, MASK, FACE -> HEAD_CONTAINER;
            case CAPE -> CAPE_CONTAINER;
            case AMULET -> AMULET_CONTAINER;
            case WEAPON -> WEAPON_CONTAINER;
            case BODY, TORSO -> BODY_CONTAINER;
            case SHIELD -> SHIELD_CONTAINER;
            case LEGS -> LEGS_CONTAINER;
            case GLOVES -> GLOVES_CONTAINER;
            case BOOTS -> BOOTS_CONTAINER;
            default -> -1;
        };
    }

    //Constants - eliminate magic numbers
    final int HEAD_CONTAINER = 60131;
    final int BODY_CONTAINER = 60135;
    final int LEGS_CONTAINER = 60137;
    final int AMULET_CONTAINER = 60133;
    final int CAPE_CONTAINER = 60132;
    final int GLOVES_CONTAINER = 60138;
    final int BOOTS_CONTAINER = 60139;
    final int WEAPON_CONTAINER = 60134;
    final int SHIELD_CONTAINER = 60136;
    final int OVERRIDE_INTERFACE = 60106;
    final int INFO_INTERFACE = 60260;
    final int INFO_BTN = -5286;
    final int INFO_CLOSE_BTN = -5270;
    final int INFO_GO_BACK_BTN = -5267;
    final int MANAGE_BTN = -5395;
    final int HEAD_SLOT_BTN = -5415;
    final int AMULET_SLOT_BTN = -5413;
    final int BODY_SLOT_BTN = -5410;
    final int LEGS_SLOT_BTN = -5408;
    final int BOOTS_SLOT_BTN = -5407;
    final int CAPE_SLOT_BTN = -5414;
    final int WEAPON_SLOT_BTN = -5411;
    final int GLOVES_SLOT_BTN = -5406;
    final int SHIELD_SLOT_BTN = -5409;
    final int VIEW_PRESET_BTN = -5417;
    final int SAVE_PRESET_BTN = -5422;
    final int VIEW_ALL_BTN = -5428;
    final int OPEN_OVERRIDE_BTN = 27659;
}