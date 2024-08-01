package com.osroyale.game.world.entity.mob.npc.drop;

import com.google.gson.annotations.SerializedName;
import com.osroyale.game.world.items.Item;
import com.osroyale.util.RandomGen;

public final class NpcDrop implements Comparable<NpcDrop> {
    /** The item id of this drop. */
    @SerializedName(value="id", alternate={"item"})
    public int id;
    /** The chance this item is dropped. */
    public NpcDropChance type;

    public int getWeight() {
        return type.weight;
    }

    /** The alternate chance modifier to use over the {@code chance} rarity. */
    public final int chance;

    /** The minimum amount of this drop. */
    public final int minimum;

    /** The maximum amount of this drop. */
    public final int maximum;

    /** Constructs a new {@link NpcDrop}. */
    public NpcDrop(int id, NpcDropChance type, int chance, int minimum, int maximum) {
        this.id = id;
        this.type = type;
        this.chance = chance;
        this.minimum = minimum;
        this.maximum = maximum;
    }

    /** Converts this {@link NpcDrop} to an item. */
    public Item toItem(RandomGen gen) {
        return new Item(id, gen.inclusive(minimum, maximum));
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType(NpcDropChance type) {
        this.type = type;
    }

    @Override
    public int compareTo(NpcDrop other) {
        return type.ordinal() - other.type.ordinal();
    }
}
