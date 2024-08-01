package com.osroyale.content.preloads;


import com.osroyale.content.skill.impl.magic.Spellbook;
import com.osroyale.game.world.items.Item;

public interface Preload {

    String title();

    Spellbook spellbook();

    Item[] equipment();

    Item[] inventory();

    int[] skills();

}
