package com.osroyale.content.tittle;

import com.osroyale.content.achievement.AchievementHandler;
import com.osroyale.content.achievement.AchievementList;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.util.generic.BooleanInterface;

import java.util.Optional;

/**
 * Holds all the title data that can be redeemed using the title interface.
 *
 * @author Daniel
 */
public enum Title implements BooleanInterface<Player> {

    CADET(PlayerTitle.create("Cadet"), "Complete the achievement:", "Kill 10 players") {
        @Override
        public boolean activated(Player player) {
            return AchievementHandler.completed(player, AchievementList.KILL_10_PLAYERS);
        }
    },
    GLADIATOR(PlayerTitle.create("Gladiator"), "Complete the achievement:", "Kill 150 players") {
        @Override
        public boolean activated(Player player) {
            return AchievementHandler.completed(player, AchievementList.KILL_150_PLAYERS);
        }
    },
    COMMANDER(PlayerTitle.create("Commander"), "Complete the achievement:", "Kill 500 players") {
        @Override
        public boolean activated(Player player) {
            return AchievementHandler.completed(player, AchievementList.KILL_500_PLAYERS);
        }
    },
    WAR_CHIEF(PlayerTitle.create("War-chief", 0xD053DB), "Complete the achievement:", "Kill 1,000 players") {
        @Override
        public boolean activated(Player player) {
            return AchievementHandler.completed(player, AchievementList.KILL_1000_PLAYERS);
        }
    },
    BLOODHOUND(PlayerTitle.create("Bloodhound", 0xB32424), "Complete the achievement:", "Get a 25 killstreak") {
        @Override
        public boolean activated(Player player) {
            return AchievementHandler.completed(player, AchievementList.GET_A_25_KILLSTREAK);
        }
    },
    LEGEND(PlayerTitle.create("Legend", 0xC25F0E), "Complete the achievement:", "Vote 1,000 times") {
        @Override
        public boolean activated(Player player) {
            return AchievementHandler.completed(player, AchievementList.CLAIM_1000_VOTES);
        }
    },
    MASTER(PlayerTitle.create("Master"), "Achieve level 99 in all skills", "") {
        @Override
        public boolean activated(Player player) {
            return player.skills.isMaxed();
        }
    },
    GODLY(PlayerTitle.create("Godly", 0xC7B23E), "Achieve 200M experience in all", "available skills") {
        @Override
        public boolean activated(Player player) {
            return AchievementHandler.completed(player, AchievementList.MAX_200M_EXPERIENCE);
        }
    },
    THE_GREAT(PlayerTitle.create("The Great", 0xC7B23E), "Complete all available achievements", "") {
        @Override
        public boolean activated(Player player) {
            return AchievementHandler.completedAll(player);
        }
    },
//    IRONMAN("Ironman", "Be an iron man.", "") {
//        @Override
//        public boolean activated(Player player) {
//            return PlayerRight.isIronman(player);
//        }
//    },
//    DONATOR("Donator", "Be Normal donator", "") {
//        @Override
//        public boolean activated(Player player) {
//            return PlayerRight.isDonator(player);
//        }
//    },
//    SUPER("Super", "Be a Super donator", "") {
//        @Override
//        public boolean activated(Player player) {
//            return PlayerRight.isSuper(player);
//        }
//    },
//    EXTREME("Extreme", "Be a Extreme donator", "") {
//        @Override
//        public boolean activated(Player player) {
//            return PlayerRight.isExtreme(player);
//        }
//    },
//    ELITE("Elite", "Be a Elite donator", "") {
//        @Override
//        public boolean activated(Player player) {
//            return PlayerRight.isElite(player);
//        }
//    },
//    DOPE("King", "Be a King donator", "") {
//        @Override
//        public boolean activated(Player player) {
//            return PlayerRight.isKing(player);
//        }
//    },

    ;

    private final PlayerTitle title;
    private final String[] requirement;

    Title(PlayerTitle name, String... requirement) {
        this.title = name;
        this.requirement = requirement;
    }

    public PlayerTitle getTitle() {
        return title;
    }

    public String[] getRequirement() {
        return requirement;
    }

    public static Optional<Title> forOrdinal(int ordinal) {
        for (Title title : values()) {
            if (title.ordinal() == ordinal) {
                return Optional.of(title);
            }
        }
        return Optional.empty();
    }
}
