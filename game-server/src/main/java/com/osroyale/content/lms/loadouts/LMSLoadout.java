package com.osroyale.content.lms.loadouts;

import com.osroyale.content.skill.impl.magic.Spellbook;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.containers.ItemContainer;

public abstract class LMSLoadout {

    public static int[] equipmentOrder = { 0, 1, 2, 13, 3, 4, 5, 7, 9, 10, 12 };

    protected ItemContainer equipmentSetup = new ItemContainer(14, ItemContainer.StackPolicy.STANDARD);

    public ItemContainer getEquipmentSetup() {
        return equipmentSetup;
    }

    protected ItemContainer inventorySetup = new ItemContainer(28, ItemContainer.StackPolicy.STANDARD);

    public ItemContainer getInventorySetup() {
        return inventorySetup;
    }

    public void setup() {
        for(int index = 0; index < LMSLoadout.equipmentOrder.length; index++)
            equipmentSetup.set(LMSLoadout.equipmentOrder[index], new Item(getEquipment()[index], LMSLoadout.equipmentOrder[index] == 13 ? 5000 : 1), false);

        for(int index = 0; index < getInventory().length; index++) {
            int itemId = getInventory()[index];
            if(itemId == -1) continue;

            inventorySetup.set(index, new Item(itemId), false);
        }
    }

    protected abstract int getAttackLevel();

    protected abstract int getStrengthLevel();

    protected abstract int getDefenceLevel();

    protected abstract int getHitpointsLevel();

    protected abstract int getRangedLevel();

    protected abstract int getPrayerLevel();

    protected abstract int getMagicLevel();

    public abstract int[] getEquipment();

    public abstract int[] getInventory();

    public abstract Spellbook getSpellbook();

    public abstract int[][] getRunePouchRunes();

    public abstract int[] getOffensiveItem();

    public abstract int[] getDefensiveItem();

    public abstract int[] getOffensiveItemUpgrades();

    public int[] getSkills() {
        return new int[] { getAttackLevel(), getDefenceLevel(), getStrengthLevel(), getHitpointsLevel(), getRangedLevel(), getPrayerLevel(), getMagicLevel() };
    }

}