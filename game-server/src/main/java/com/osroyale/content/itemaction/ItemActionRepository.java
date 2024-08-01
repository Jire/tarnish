package com.osroyale.content.itemaction;

import com.osroyale.content.itemaction.impl.*;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;

import java.util.HashMap;

/**
 * Handles execution of the item actions.
 *
 * @author Daniel
 */
public class ItemActionRepository {

    /** Map of all the item actions. */
    public static HashMap<Integer, ItemAction> ACTIONS = new HashMap<>();

    /** Declares all the item actions. */
    public static void declare() {
        ACTIONS.put(12_931, new SerpentineHelm()); // charged
        ACTIONS.put(12_929, new SerpentineHelm()); // uncharged

        ACTIONS.put(13_196, new TanzaniteHelm()); // charged
        ACTIONS.put(13_197, new TanzaniteHelm()); // uncharged

        ACTIONS.put(13_198, new MagmaHelm()); // charged
        ACTIONS.put(13_199, new MagmaHelm()); // uncharged

        ACTIONS.put(11283, new DragonfireShield()); // charged
        ACTIONS.put(11284, new DragonfireShield()); // uncharged

        ACTIONS.put(12924, new ToxicBlowpipe()); // uncharged
        ACTIONS.put(12926, new ToxicBlowpipe()); // charged

        ACTIONS.put(11907, new TridentOfTheSeas()); // charged
        ACTIONS.put(12899, new TridentOfTheSwamp()); // charged


        ACTIONS.put((int) CrawsBow.CRAWS_UNCHARGED_ID, new CrawsBow()); // uncharged
        ACTIONS.put((int) CrawsBow.CRAWS_CHARGED_ID, new CrawsBow()); // charged

        ACTIONS.put((int) ViggorasChainmace.VIGGORAS_CHAINMACE_UNCHARGED_ID, new ViggorasChainmace()); // uncharged
        ACTIONS.put((int) ViggorasChainmace.VIGGORAS_CHAINMACE_CHARGED_ID, new ViggorasChainmace()); // charged

        ACTIONS.put((int) ThammaronsSceptre.THAMMARONS_SCEPTRE_UNCHARGED_ID, new ThammaronsSceptre()); // uncharged
        ACTIONS.put((int) ThammaronsSceptre.THAMMARONS_SCEPTRE_CHARGED_ID, new ThammaronsSceptre()); // charged

        ACTIONS.put((int) CelestialRing.UNCHARGED_RING, new CelestialRing()); // uncharged
        ACTIONS.put((int) CelestialRing.CHARGED_RING, new CelestialRing()); // charged

        ACTIONS.put(6831, new MimeBox());
        ACTIONS.put(6832, new DrillDemonBox());
        ACTIONS.put(12897, new ClanShowcaseBox());
        ACTIONS.put(6854, new ClanResourceBox());

//        ACTIONS.put(1704, new AmuletOfGlory());
//        ACTIONS.put(1706, new AmuletOfGlory());
//        ACTIONS.put(1708, new AmuletOfGlory());
//        ACTIONS.put(1710, new AmuletOfGlory());
//        ACTIONS.put(1712, new AmuletOfGlory());
//        ACTIONS.put(1714, new AmuletOfGlory());
//        ACTIONS.put(11978, new AmuletOfGlory());
//        ACTIONS.put(11978, new AmuletOfGlory());
//        ACTIONS.put(2566, new RingOfDueling());
//        ACTIONS.put(2564, new RingOfDueling());
//        ACTIONS.put(2562, new RingOfDueling());
//        ACTIONS.put(2560, new RingOfDueling());
//        ACTIONS.put(2558, new RingOfDueling());
//        ACTIONS.put(2556, new RingOfDueling());
//        ACTIONS.put(2554, new RingOfDueling());
//        ACTIONS.put(2552, new RingOfDueling());
    }

    public static boolean itemOnItem(Player player, Item first, Item second) {
        ItemAction action = ACTIONS.getOrDefault(first.getId(), ACTIONS.get(second.getId()));
        return action != null && action.itemOnItem(player, first, second);
    }

    public static boolean inventory(Player player, Item item, int opcode) {
        ItemAction action = ACTIONS.get(item.getId());
        return action != null && action.inventory(player, item, opcode);
    }

    public static boolean equipment(Player player, Item item, int opcode) {
        ItemAction action = ACTIONS.get(item.getId());
        return action != null && action.equipment(player, item, opcode);
    }

    public static boolean drop(Player player, Item item) {
        ItemAction action = ACTIONS.get(item.getId());
        return action != null && action.drop(player, item);
    }
}
