package com.osroyale.content.gambling.impl;

import com.osroyale.content.gambling.Flowers;
import com.osroyale.content.gambling.Gamble;
import com.osroyale.content.gambling.GambleStage;
import com.osroyale.game.task.Task;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.object.CustomGameObject;
import com.osroyale.game.world.position.Position;
import com.osroyale.util.Utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.osroyale.content.gambling.GambleManager.GAMBLING_ZONE;

public class FlowerPoker extends Gamble {

    public FlowerPoker(Player host, Player opponent) {
        super(host, opponent);
    }

    @Override
    public String toString() {
        return "Flower Poker";
    }

    @Override
    public void gamble() {
        getHost().getGambling().getFlowers().clear();
        getOpponent().getGambling().getFlowers().clear();

        getHost().getGambling().getGameFlowers().clear();
        getOpponent().getGambling().getGameFlowers().clear();

        for (int i = 0; i < 5; i++) {
            Flowers hostFlower = Flowers.values()[Utility.random(Flowers.values().length - 1)];
            Flowers opponentFlower = Flowers.values()[Utility.random(Flowers.values().length - 1)];

            while (hostFlower == Flowers.BLACK || hostFlower == Flowers.WHITE) {
                hostFlower = Flowers.values()[Utility.random(Flowers.values().length - 1)];
            }

            while (opponentFlower == Flowers.BLACK || hostFlower == Flowers.WHITE) {
                opponentFlower = Flowers.values()[Utility.random(Flowers.values().length - 1)];
            }

            getHost().getGambling().getFlowers().add(hostFlower);
            getOpponent().getGambling().getFlowers().add(opponentFlower);
        }
        final Ranking hostResult = getRank(getHost());
        final Ranking opponentResult = getRank(getOpponent());

        System.out.println("Looking for place to play");
        boolean[] canPlay = { false, false };
        Position positionOne, positionTwo;
        do {
            int maxX = GAMBLING_ZONE.getMaximumX() - GAMBLING_ZONE.getMinimumX();
            int maxY = GAMBLING_ZONE.getMaximumY() - GAMBLING_ZONE.getMinimumY();
            System.out.println("maxX: " + maxX + ", " + maxY);
            positionOne = new Position(GAMBLING_ZONE.getMinimumX() + Utility.random(maxX), GAMBLING_ZONE.getMinimumY() + Utility.random(maxY));
            positionTwo = positionOne.create(positionOne.getX() + 1, positionOne.getY());
            canPlay[0] = getHost().getGambling().canPlayFlowerPokerAtPositon(getHost(), positionTwo);
            canPlay[1] = getHost().getGambling().canPlayFlowerPokerAtPositon(getHost(), positionOne);

            System.out.println(canPlay[0] + " - " + canPlay[1]);
        } while(canPlay[0] == false || canPlay[1] == false);

        System.out.println("Found a place to play");

        Position finalPositionOne = positionOne;
        Position finalPositionTwo = positionTwo;
        World.schedule(new Task(1) {
            int time = 0;

            @Override
            public void execute() {
                if(getHost().getGambling().getStage() != GambleStage.IN_PROGRESS) {
                    cancel();
                    return;
                }
                switch (time) {
                    case 0:
                        getHost().move(finalPositionOne);
                        getOpponent().move(finalPositionTwo);
                        break;
                    case 1:
                        plant(this);
                        break;
                    case 29:
                        getHost().speak("I got " + hostResult.name().toLowerCase().replaceAll("_", " "));
                        getOpponent().speak("I got " + opponentResult.name().toLowerCase().replaceAll("_", " "));
                        break;
                    case 30:
                        getHost().getGambling().finish(getHost(), getOpponent(), hostResult.ordinal(), opponentResult.ordinal());
                        cancel();
                        break;
                }
                time++;
            }
        });
    }

    private void plant(Task gambleTask) {
        World.schedule(new Task(1) {
            int time = 0;
            int type = 0;
            Flowers hostFlower = getHost().getGambling().getFlowers().get(type);
            Flowers opponentFlower = getOpponent().getGambling().getFlowers().get(type);

            Player[] players = new Player[] { getHost(), getOpponent() };

            @Override
            public void execute() {
                if(getHost().getGambling().getStage() != GambleStage.IN_PROGRESS) {
                    cancel();
                    return;
                }

                switch (time) {
                    case 1:
                        for(Player player : players)
                            player.animate(827);
                        break;
                    case 2:
                        hostFlower = getHost().getGambling().getFlowers().get(type);
                        opponentFlower = getOpponent().getGambling().getFlowers().get(type);

                        if (hostFlower == null || opponentFlower == null) {
                            cancel();
                            break;
                        }

                        for(Player player : players) {
                            Flowers flowers = player.equals(getHost()) ? hostFlower : opponentFlower;

                            final CustomGameObject gameFlower = new CustomGameObject(flowers.getId(), player.getPosition().copy());
                            gameFlower.register();
                            player.getGambling().getGameFlowers().add(gameFlower);

                            player.movement.walkTo(player.getPosition().south());
                        }

                        break;
                    case 3:
                        for(Player player : players) {
                            Flowers flowers = player.equals(getHost()) ? hostFlower : opponentFlower;
                            player.speak("I planted a " + Utility.formatText(flowers.name().toLowerCase()) + " flower.");
                        }
                        break;
                    case 4:

                        if(hostFlower.name().equalsIgnoreCase("WHITE") || hostFlower.name().equalsIgnoreCase("BLACK") ||
                           opponentFlower.name().equalsIgnoreCase("WHITE") || opponentFlower.name().equalsIgnoreCase("BLACK")) {
                            getHost().getGambling().finish(getHost(), getOpponent(), 0, 0);
                            gambleTask.cancel();
                            cancel();
                            break;
                        }

                        if (type == 4) {
                            cancel();
                            break;
                        }

                        time = 0;
                        type++;
                        break;
                }
                time++;
            }
        });
    }

    private enum Ranking {
        NOTHING,
        PAIR,
        TWO_PAIR,
        THREE_OF_KIND,
        FULL_HOUSE,
        FOUR_OF_KIND,
        ROYAL_KIND,
    }

    private static Ranking getRank(Player player) {
        ArrayList<Flowers> flowers = new ArrayList<Flowers>();
        flowers.addAll(player.getGambling().getFlowers());
        Collections.sort(flowers);
        Map<Integer, Integer> pairs = getPairs(flowers);
        for (int i = 0; i < pairs.size(); i++) {
            if (pairs.get(i) == null) {
                continue;
            }
            if (pairs.get(i).intValue() == 5) {
                return Ranking.ROYAL_KIND;
            }
        }
        if (pairs.size() == 2) {
            if ((pairs.get(0).intValue() == 3 && pairs.get(1).intValue() == 2) || (pairs.get(1).intValue() == 3 && pairs.get(0).intValue() == 2)) {
                return Ranking.FULL_HOUSE;
            }
        }
        int totalPairs = 0;
        for (int i = 0; i < pairs.size(); i++) {
            if (pairs.get(i) == null) {
                continue;
            }
            if (pairs.get(i).intValue() == 3) {
                return Ranking.THREE_OF_KIND;
            }
            if (pairs.get(i).intValue() == 2) {
                totalPairs++;
            }
        }
        if (totalPairs == 2) {
            return Ranking.TWO_PAIR;
        }
        if (totalPairs == 1) {
            return Ranking.PAIR;
        }
        return Ranking.NOTHING;
    }

    private static Map<Integer, Integer> getPairs(ArrayList<Flowers> list) {
        Map<Integer, Integer> finalPairs = new HashMap<Integer, Integer>();
        int[] pairs = new int[14];
        for (Flowers flower : list) {
            pairs[flower.ordinal()]++;
        }
        int slot = 0;
        for (int i = 0; i < pairs.length; i++) {
            if (pairs[i] >= 2) {
                finalPairs.put(slot, pairs[i]);
                slot++;
            }
        }
        return finalPairs;
    }

}
