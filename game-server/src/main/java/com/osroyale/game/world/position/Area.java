package com.osroyale.game.world.position;

import com.osroyale.game.world.Interactable;
import com.osroyale.game.world.entity.Entity;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.position.impl.SquareArea;

/**
 * Handles checking if mobs are in a certain area.
 *
 * @author Daniel
 */
public abstract class Area {

    private static final Area[] SAFEZONE = { 
            new SquareArea("Edgeville Bank", 3091, 3488, 3098, 3499),
            new SquareArea("Edgeville Bank", 3091, 3507, 3100, 3513) };

    private static final Area DZ = new SquareArea("Donator Zone", 2462, 10422, 2493, 10463);
    private static final Area SDZ = new SquareArea("Super Donator Zone", 2398, 10404, 2444, 10440);
    /** The General Graardor room. */
    private static final Area BANDOS_ROOM = new SquareArea("General Graardor room", 2864, 5351, 2876, 5369, 2);
    private static final Area SARADOMIN_ROOM = new SquareArea("Saradomin room", 2889, 5258, 2907, 5275, 0);
    private static final Area ZAMORAK_ROOM = new SquareArea("Zamorak room", 2918, 5318, 2936, 5331, 2);
    private static final Area ARMADYL_ROOM = new SquareArea("Armadyl room", 2824, 5296, 2842, 5308, 2);
    public static final Boundary NEX = new Boundary(2900, 5183, 2943, 5227);

    public static final Area[] INFERNO = { new SquareArea("Arena Zone", 2243, 5314, 2300, 5372) };
    //            new SquareArea("Inferno", 2243, 5372, 2300, 5314),

    /** The General Graardor room. */
    private static final Area RFD_MINIGAME = new SquareArea("RFD Minigame", 1889, 5345, 1910, 5366, 2);

    /** The collection of areas that resemble the wilderness resource area. */
    public static final Area[] WILDERNESS_RESOURCE = { new SquareArea("Wilderness Resource", 3174, 3924, 3196, 3944) };

    /** The collection of areas that resemble the zulrah area. */
    private static final Area[] ZULRAH = { new SquareArea("Zulrah", 2235, 3050, 2288, 3083) };

    /** The collection of areas that resemble the king black dragon area. */
    private static final Area[] KBD = { new SquareArea("King Black Dragon lair", 2256, 4680, 2287, 4711) };

    private static final Area[] LMS_LOBBY = { new SquareArea("LMS Lobby", 3138, 3639, 3145, 3645) };

    private static final Area[] LMS_BUILDING = { new SquareArea("LMS Lobby", 3140, 3632, 3144, 3638) };

    private static final Area[] FEROX_ENCLAVE = {
            new SquareArea(3125, 3618, 3143, 3639),
            new SquareArea(3123, 3622, 3124, 3632),
            new SquareArea(3144, 3627, 3153, 3640),
            new SquareArea(3138, 3640, 3145, 3645),
            new SquareArea(3148, 3641, 3155, 3646),
            new SquareArea(3130, 3617, 3139, 3617),
            new SquareArea(3153, 3636, 3155, 3640),
            new SquareArea(3153, 3627, 3155, 3633),
            new SquareArea(3143, 3618, 3146, 3626),
            new SquareArea(3146, 3622, 3151, 3626)
    };

    /** The collection of areas that resemble the kraken area. */
    private static final Area[] KRAKEN = { new SquareArea("Kraken Cave", 2268, 10022, 2292, 10046) };
    private static final Area[] LMS_GAME = {new SquareArea("LMS Game", 3392, 5760, 3519, 5893) };
    private static final Area[] PEST_CONTROL_GAME = { new SquareArea("Pest Control Game", 2622, 2558, 2693, 2627) };

    private static final Area[] EVENT_ARENA = { new SquareArea("Event Arena", 3082, 3506, 3089, 3513) };

    private static final Area[] BARROWS = { 
            new SquareArea("Barrows", 3533, 3261, 3585, 3328),
            new SquareArea("Barrows Underground", 3523, 9672, 3591, 9725, 3) };

    private static final Area[] CATACOMBS = { new SquareArea("Catacombs", 1591, 9982, 1736, 10111) };

    /** The collection of areas that resemble the duel arena. */
    private static final Area[] DUEL_ARENA_LOBBY = { 
            new SquareArea("Lobby", 3353, 3263, 3388, 3287),
            new SquareArea("Lobby Altar", 3374, 3280, 3379, 3286),
            new SquareArea("Lobby east bank", 3380, 3267, 3388, 3273) };

    /** The collection of areas that resemble multi-combat zones. */
    private static final Area[] MULTI_COMBAT_ZONES = { 
            new SquareArea("Lizard shamans", 1408, 3692, 1457, 3729),
            new SquareArea("Al-kahrid Warrior", 3281, 3158, 3304, 3178),
            new SquareArea("Smoke devils", 2378, 9421, 2429, 9465),
            new SquareArea("Rock Crab", 2660, 3711, 2743, 3739),
            new SquareArea("Skotizo", 2255, 5646, 2290, 5680),
            new SquareArea("Wilderness Resource", 3174, 3924, 3196, 3944),
            new SquareArea("Start of Varrock Wilderness", 3134, 3525, 3327, 3607),
            new SquareArea("North of GE, near gravestones", 3190, 3648, 3327, 3839),
            new SquareArea("Near Chaos Elemental", 3200, 3840, 3390, 3967),
            new SquareArea("Near wilderness agility course", 2992, 3912, 3007, 3967),
            new SquareArea("West wilderness altar", 2946, 3816, 2959, 3831),
            new SquareArea("Deep wilderness 1", 3008, 3856, 3199, 3903),
            new SquareArea("Near wilderness castle", 3008, 3600, 3071, 3711),
            new SquareArea("North of varrock lumbermill", 3072, 3608, 3327, 3647),
            new SquareArea("Pest control", 2624, 2550, 2690, 2619),
            new SquareArea("Fight caves", 2371, 5062, 2422, 5117),
            new SquareArea("Fight arena", 2896, 3595, 2927, 3630),
            new SquareArea("Inferno", 2243, 5314, 2300, 5372),
            new SquareArea("Scorpia lair", 3218, 10329, 3248, 10353),
            new SquareArea("Corporeal Beast lair", 2964, 4360, 3001, 4399),
            new SquareArea("Revenant Cave", 3168, 10047, 3290, 10143),
            new SquareArea("Catacombs of Kourend 1", 1588, 9982, 1747, 10068),//2 to leave out brutals
            new SquareArea("Catacombs of Kourend 2", 1641, 9982, 1734, 10110),
            new SquareArea("Dagannoth lair", 2890, 4425, 2942, 4466) };

    private static final SquareArea[] DUEL_ARENAS = {
            new SquareArea("Arena", 3327, 3203, 3389, 3290)
    };

    private static final SquareArea[] DUEL_OBSTICLE_ARENAS = { 
            new SquareArea("South East Obstacle Arena", 3362, 3205, 3390, 3221),
            new SquareArea("West Obstacle Arena", 3331, 3224, 3359, 3240),
            new SquareArea("North East Obstacle Arena", 3362, 3243, 3390, 3259) };

    /** The collection of areas that resemble the wilderness area. */
    private static final Area[] WILDERNESS = { 
            new SquareArea("Wilderness 1", 2941, 3525, 3392, 3966),
            new SquareArea("Wilderness 2", 2941, 9920, 3392, 10366),
            new SquareArea("Wilderness 3", 2250, 4672, 2296, 4721) };

    public static boolean inEdgeville(Interactable entity) {
        return inArea(entity, new Position(3075, 3457, 0), new Position(3116, 3518, 0));
    }

    public static boolean inCerberus(Interactable entity) {
        return inArea(entity, new Position(1217, 1223, 0), new Position(1262, 1264, 0));
    }

    public static boolean inFightCaves(Interactable entity) {
        return inArea(entity, new Position(2368, 5057, 0), new Position(2428, 5120, 0));
    }

    public static boolean inGodwars(Interactable entity) {
        return inArea(entity, new Position(2810, 5250, entity.getHeight()), new Position(2950, 5370, entity.getHeight()));
    }
    public static boolean inWarriorGuild(Interactable entity) {
        return inArea(entity, new Position(2838, 3535, 0), new Position(2875, 3555, 0));
    }

    public static boolean inCyclops(Interactable entity) {
        return inArea(entity, new Position(2847, 3532, 2), new Position(2876, 3542, 2)) ||
                inArea(entity, new Position(2838, 3543, 2), new Position(2877, 3577, 2));
    }

    public static boolean inkolodionArena(Entity entity) {
        return entity.instance != Mob.DEFAULT_INSTANCE && inArea(entity, new Position(3092, 3921, entity.getHeight()), new Position(3117, 3947, entity.getHeight()));
    }
    public static boolean inSuperDonatorZone(Interactable entity) {
        return SDZ.inArea(entity.getPosition());
    }
    public static boolean inRegularDonatorZone(Interactable entity) {
        return DZ.inArea(entity.getPosition());
    }

    public static boolean inGnomeCourse(Interactable entity) {
        return inArea(entity, new Position(2464, 3412, entity.getHeight()), new Position(2490, 3444, entity.getHeight()));
    }

    public static boolean inBarbarianCourse(Interactable entity) {
        return inArea(entity, new Position(2529, 3542, entity.getHeight()), new Position(2553, 3559, entity.getHeight()));
    }

    public static boolean inSeersCourse(Interactable entity) {
        return inArea(entity, new Position(2695, 3455, entity.getHeight()), new Position(2740, 3500, entity.getHeight()));
    }

    public static boolean inArdougneCourse(Interactable entity) {
        return inArea(entity, new Position(2642, 3285, entity.getHeight()), new Position(2681, 3331, entity.getHeight()));
    }

    public static boolean inWildernessCourse(Interactable entity) {
        return inArea(entity, new Position(2989, 3933, entity.getHeight()), new Position(3006, 3964, entity.getHeight()));
    }

    public static boolean inWildernessResource(Interactable entity) {
        return inArea(entity, new Position(3714, 3924, entity.getHeight()), new Position(3196, 3944, entity.getHeight()));
    }

    public static boolean inBarrowsChamber(Interactable entity) {
        return inArea(entity, new Position(3547, 9690, entity.getHeight()), new Position(3556, 9699, entity.getHeight()));
    }

    public static boolean inVorkath(Interactable entity) {
        return inArea(entity, new Position(2257, 4053, entity.getHeight()), new Position(2288, 4079, entity.getHeight()));
    }

    public static boolean inKingBlackDragon(Interactable entity) {
        for (Area zone : KBD) {
            if (zone.inArea(entity.getPosition())) {
                return true;
            }
        }
        return false;
    }

    public static boolean inFeroxEnclave(Interactable entity) {
        for(Area zone : FEROX_ENCLAVE) {
            if(zone.inArea(entity.getPosition())) {
                return true;
            }
        }
        return false;
    }
    public static boolean inCatacombs(Interactable entity) {
        for(Area zone : CATACOMBS) {
            if(zone.inArea(entity.getPosition())) {
                return true;
            }
        }
        return false;
    }


    public static boolean inLMSLobby(Interactable entity) {
        for(Area zone : LMS_LOBBY) {
            if(zone.inArea(entity.getPosition())) {
                return true;
            }
        }
        return false;
    }

    public static boolean inLMSBuilding(Interactable entity) {
        for(Area zone : LMS_BUILDING) {
            if(zone.inArea(entity.getPosition())) {
                return true;
            }
        }
        return false;
    }


    public static boolean inRFD(Interactable entity) {
        return RFD_MINIGAME.inArea(entity.getPosition());
    }

    public static boolean inKraken(Interactable entity) {
        for (Area zone : KRAKEN) {
            if (zone.inArea(entity.getPosition())) {
                return true;
            }
        }
        return false;
    }
    public static boolean inInferno(Entity entity) {
        for (Area zone1 : INFERNO) {
            if (zone1.inArea(entity.getPosition())) {
                return true;
            }
        }
        return false;
    }

    public static boolean inBarrows(Entity entity) {
        for (Area zone : BARROWS) {
            if (zone.inArea(entity.getPosition())) {
                return true;
            }
        }
        return false;
    }

    public static boolean inMulti(Entity entity) {
        for (Area zone : MULTI_COMBAT_ZONES) {
            if (zone.inArea(entity.getPosition())) {
                return true;
            }
        }
        return /*inEventArena(entity) ||*/ inVorkath(entity) || inkolodionArena(entity) || inKraken(entity) || inKingBlackDragon(entity) || inGodwars(entity) || inZulrah(entity);
    }

    public static boolean inWilderness(Position position) {
        for (Area wild : WILDERNESS) {
            if (wild.inArea(position)) {
                return true;
            }
        }
        return false;
    }

    public static boolean inWilderness(Interactable entity) {
        if (entity == null) return false;
        if (inKingBlackDragon(entity)) return false;
        if (inFeroxEnclave(entity) || inLMSLobby(entity) || inLMSBuilding(entity)) return false;
        for (Area wild : WILDERNESS) {
            if (wild.inArea(entity.getPosition())) {
                return true;
            }
        }
        return false;
    }

    public static boolean inDuelArenaLobby(Entity entity) {
        for (Area zone : DUEL_ARENA_LOBBY) {
            if (zone.inArea(entity.getPosition())) {
                return true;
            }
        }
        return false;
    }

    public static boolean inDuelObsticleArena(Interactable entity) {
        for (Area zone : DUEL_OBSTICLE_ARENAS) {
            if (zone.inArea(entity.getPosition())) {
                return true;
            }
        }
        return false;
    }

    public static boolean inDuelArena(Interactable entity) {
        for (Area zone : DUEL_ARENAS) {
            if (zone.inArea(entity.getPosition())) {
                return true;
            }
        }
        return false;
    }

    public static boolean inZulrah(Interactable entity) {
        for (Area zone : ZULRAH) {
            if (zone.inArea(entity.getPosition())) {
                return true;
            }
        }
        return false;
    }

    public static boolean inPestControl(Interactable entity) {
        for (Area zone : PEST_CONTROL_GAME) {
            if (zone.inArea(entity.getPosition())) {
                return true;
            }
        }
        return false;
    }
    public static boolean inLMSGame(Interactable entity) {
        for (Area zone : LMS_GAME) {
            if (zone.inArea(entity.getPosition())) {
                return true;
            }
        }
        return false;
    }

   /* public static boolean inEventArena(Interactable entity) {
        for (Area zone : EVENT_ARENA) {
            if (zone.inArea(entity.getPosition())) {
                return true;
            }
        }
        return false;
    }
*/
    public static boolean inPvP(Interactable entity) {
        boolean pvp = false;
        for (Area zone : SAFEZONE) {
            if (zone.inArea(entity.getPosition())) {
                pvp = true;
                break;
            }
        }
        return !pvp;
    }

    public static boolean inBandos(Mob mob) {
        return BANDOS_ROOM.inArea(mob.getPosition());
    }

    private static boolean inSaradomin(Mob mob) {
        return SARADOMIN_ROOM.inArea(mob.getPosition());
    }

    private static boolean inArmadyl(Mob mob) {
        return ARMADYL_ROOM.inArea(mob.getPosition());
    }

    private static boolean inZamorak(Mob mob) {
        return ZAMORAK_ROOM.inArea(mob.getPosition());
    }

    public static boolean inGodwarsChambers(Mob mob) {
        return inBandos(mob) || inSaradomin(mob) || inArmadyl(mob) || inZamorak(mob);
    }
    public static boolean inRestrictedArea(Mob mob) {
        return inGodwarsChambers(mob) || inCerberus(mob) || inBarbarianCourse(mob)
                || inCyclops(mob) || inDuelArena(mob) || inDuelArenaLobby(mob) || inFightCaves(mob) || inDuelObsticleArena(mob)
                || inGnomeCourse(mob) || inGodwars(mob) || inGodwarsChambers(mob) || inInferno(mob) || inKingBlackDragon(mob)
                || inkolodionArena(mob) || inKraken(mob) || inLMSBuilding(mob) || inPestControl(mob)  || inRFD(mob)
                || inVorkath(mob) || inVorkath(mob) || inWilderness(mob) || inZulrah(mob) || inLMSGame(mob) || inLMSLobby(mob);
         }


    private static boolean inArea(Interactable interactable, Position bottomLeft, Position topRight) {
        return (interactable.getPosition().getX() >= bottomLeft.getX())
                && (interactable.getPosition().getX() <= topRight.getX())
                && (interactable.getPosition().getY() >= bottomLeft.getY())
                && (interactable.getPosition().getY() <= topRight.getY());
    }

    public static boolean inAllArea(Interactable entity, Area... area) {
        for (Area zone : area) {
            if (!zone.inArea(entity.getPosition())) {
                return false;
            }
        }
        return true;
    }

    public static boolean inAnyArea(Interactable entity, Area... area) {
        for (Area zone : area) {
            if (zone.inArea(entity.getPosition())) {
                return true;
            }
        }
        return false;
    }


    public abstract boolean inArea(Position position);

    public abstract Position getRandomLocation();
}
