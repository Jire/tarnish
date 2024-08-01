package com.osroyale.game.world.entity.mob.npc;

import com.osroyale.game.world.entity.combat.strategy.CombatStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.boss.*;
import com.osroyale.game.world.entity.combat.strategy.npc.boss.armadyl.FlightKilisa;
import com.osroyale.game.world.entity.combat.strategy.npc.boss.armadyl.FlockleaderGeerin;
import com.osroyale.game.world.entity.combat.strategy.npc.boss.armadyl.KreeArra;
import com.osroyale.game.world.entity.combat.strategy.npc.boss.armadyl.WingmanSkree;
import com.osroyale.game.world.entity.combat.strategy.npc.boss.dagannoths.DagannothPrime;
import com.osroyale.game.world.entity.combat.strategy.npc.boss.dagannoths.DagannothRex;
import com.osroyale.game.world.entity.combat.strategy.npc.boss.dagannoths.DagannothSupreme;
import com.osroyale.game.world.entity.combat.strategy.npc.boss.dagannoths.Spinolyp;
import com.osroyale.game.world.entity.combat.strategy.npc.boss.kril.Balfrug;
import com.osroyale.game.world.entity.combat.strategy.npc.boss.kril.Tstanon;
import com.osroyale.game.world.entity.combat.strategy.npc.boss.kril.Zakln;
import com.osroyale.game.world.entity.combat.strategy.npc.boss.magearena.Derwen;
import com.osroyale.game.world.entity.combat.strategy.npc.boss.magearena.Justiciar;
import com.osroyale.game.world.entity.combat.strategy.npc.boss.magearena.Porazdir;
import com.osroyale.game.world.entity.combat.strategy.npc.boss.scorpia.Scorpia;
import com.osroyale.game.world.entity.combat.strategy.npc.boss.scorpia.ScorpiaGuardian;
import com.osroyale.game.world.entity.combat.strategy.npc.boss.scorpia.ScorpiaOffspring;
import com.osroyale.game.world.entity.combat.strategy.npc.boss.skotizo.Skotizo;
import org.jire.tarnishps.kalphitequeen.KalphiteQueen;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Utility class for npcs
 *
 * @author Daniel
 */
public class NpcUtility {

    static final int[] BOSSES = {
            239,   /* King Black Dragon */
            6618,  /* Crazy Archeologist */
            319,   /* Corporeal Beast */
            6619,  /* Chaos Fanatic */
            2042,  /* Zulrah */
            2043,  /* Zulrah */
            2044,  /* Zulrah */
            3127,  /* Tz-tok Jad */
            6503,  /* Callisto */
            2267,  /* Dagannoth Rex */
            2266,  /* Dagannoth Prime */
            2265,  /* Dagganoth Supreme */
            6615,  /* Scorpia */
            6611,  /* Vet'ion */
            7286,  /* Skotizo */
            6504,  /* Venenatis */
            7700, /* JalTokJad */
            3162,  /* Kree'arra */
            5862,  /* Cerberus */
            2205,  /* Commander Zilyana */
            8060, /* Vorkath */
            KalphiteQueen.PHASE2_ID // only phase 2, since "killing" phase 1 doesn't count
    };

    static final int[] DRAGONS = {
        /* Bronze */  270, 271,
        /* Iron */    272, 273,
        /* Steel */   139, 274, 275,
        /* Mithril */ 2919,
        /* Green */ 260, 261, 262, 263, 264,
        /* Red */   247, 248, 249, 250, 251, 7274,
        /* Blue */  265, 4385, 5878, 5879, 5880, 5881, 5882, 7273,
        /* Black */ 239, 252, 253, 254, 255, 256, 257, 258, 259, 2642, 6500, 6501, 6502, 6636, 6652, 7275
    };

    static final int[] BANDOS = {2242, 2243, 2244, 2245, 2235, 2240};

    static final int[] SARADOMIN = {2209, 2210, 2211, 2212};

    static final int[] ARMADYL = {3166, 3167, 3168, 3172, 3183, 3176};

    static final int[] ZAMORAK = {3159, 3160, 3161, 3134, 3139, 3140, 3138, 3135, 3133,};

    public static final Map<Integer, Supplier<CombatStrategy<Npc>>> STRATEGIES = new HashMap<>() {{
        put(239, KingBlackDragonStrategy::new);
        put(6618, CrazyArchaeologist::new);
        put(319, CorporealBeast::new);
        put(6619, ChaosFanatic::new);
        put(2042, Zulrah::new);
        put(3127, TzTokJad::new);
        put(6503, Callisto::new);
        put(7147, DemonicGorillas::new);
        put(7148, DemonicGorillas::new);
        put(7149, DemonicGorillas::new);
        put(8610, Wyrm::new);
        put(8611, Wyrm::new);
        put(2267, DagannothRex::new);
        put(2266, DagannothPrime::new);
        put(2265, DagannothSupreme::new);
        put(5947, Spinolyp::new);
        put(6615, Scorpia::new);
        put(6616, ScorpiaOffspring::new);
        put(6617, ScorpiaGuardian::new);
        put(6611, Vetion::new);
        put(7286, Skotizo::new);
        put(6504, Venenatis::new);
        put(3130, Tstanon::new);
        put(3131, Zakln::new);
        put(3132, Balfrug::new);
        put(3162, KreeArra::new);
        put(3163, WingmanSkree::new);
        put(3164, FlockleaderGeerin::new);
        put(3165, FlightKilisa::new);
        put(6766, LizardShaman::new);
        put(6767, LizardShaman::new);
        put(5862, Cerberus::new);
        put(8060, Vorkath::new);
        put(8064, StoneGuardian::new);
        put(8065, StoneGuardian::new);
        put(8066, StoneGuardian::new);
        put(7859, Derwen::new);
        put(7858, Justiciar::new);
        put(7860, Porazdir::new);
        put(8609, Hydra::new);
        put(KalphiteQueen.PHASE1_ID, KalphiteQueen::new);
        put(KalphiteQueen.PHASE2_ID, KalphiteQueen::new);
    }};
}
