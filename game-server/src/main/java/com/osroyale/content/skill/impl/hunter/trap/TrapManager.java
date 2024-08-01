package com.osroyale.content.skill.impl.hunter.trap;

import com.osroyale.game.Animation;
import com.osroyale.game.task.impl.HunterTask;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.object.CustomGameObject;
import com.osroyale.game.world.object.GameObject;
import com.osroyale.game.world.position.Position;
import com.osroyale.util.Utility;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TrapManager {

    public static List<Trap> traps = new CopyOnWriteArrayList<Trap>();

    public static List<Npc> HUNTER_NPC_LIST = new CopyOnWriteArrayList<Npc>();

    private static final int[] exps = {3254, 3744, 6041, 8811, 10272};

    public static void register(final Trap trap) {
        trap.getGameObject().register();
        traps.add(trap);
        if (trap.getOwner() != null)
            trap.getOwner().trapsLaid++;
    }

    public static void deregister(Trap trap) {
        trap.getGameObject().unregister();
        traps.remove(trap);
        if (trap.getOwner() != null)
            trap.getOwner().trapsLaid--;
    }


    public static boolean canLay(Player client) {
        if (!goodArea(client)) {
            client.message("You need to be in a hunting area to lay a trap.");
            return false;
        }
//        if (!client.getClickDelay().elapsed(2000))
//            return false;
        for (final Trap trap : traps) {
            if (trap == null)
                continue;
            if (trap.getGameObject().getPosition().getX() == client.getPosition().getX() && trap.getGameObject().getPosition().getY() == client.getPosition().getY()) {
                client.message("There is already a trap here, please place yours somewhere else.");
                return false;
            }
        }

        int x = client.getPosition().getX();
        int y = client.getPosition().getY();

        for (final Npc npc : HUNTER_NPC_LIST) {
            if (npc == null || !npc.isVisible())
                continue;
            if (x == npc.getPosition().getX() && y == npc.getPosition().getY() || x == npc.getX() && y == npc.getY()) {
                client.message("You cannot place your trap right here, try placing it somewhere else.");
                return false;
            }
        }
        if (client.trapsLaid >= getMaximumTraps(client)) {
            client.message("You can only have a max of " + getMaximumTraps(client) + " traps setup at once.");
            return false;
        }
        return true;
    }


    public static void handleRegionChange(Player client) {
        if (client.trapsLaid > 0) {
            for (final Trap trap : traps) {
                if (trap == null)
                    continue;
                if (trap.getOwner() != null && trap.getOwner().getUsername().equals(client.getUsername()) && !trap.getGameObject().getPosition().isWithinDistance(client.getPosition(), 50)) {
                    deregister(trap);
                    client.message("You didn't watch over your trap well enough, it has collapsed.");
                }
            }
        }
    }

    public static boolean goodArea(Player client) {
        int x = client.getPosition().getX();
        int y = client.getPosition().getY();
        return x >= 2758 && x <= 2965 && y >= 2880 && y <= 2954 || x >= 2300 && x <= 2320 && y >= 9790 && y <= 9800;
    }

    public static int getMaximumTraps(Player client) {
        return client.skills.getLevel(Skill.HUNTER) / 20 + 1;
    }



      public static int getObjectIDByNPCID(int npcId) {
        switch (npcId) {
            case 5549:
                return 9373;
            case 5551:
                return 9377;
            case 5552:
                return 9379;
            case 5550:
                return 9375;
            case 5548:
                return 9348;
        }
        return 0;
    }

    public static Trap getTrapForGameObject(final GameObject object) {
        for (final Trap trap : traps) {
            if (trap == null)
                continue;
            if (trap.getGameObject().getPosition().equals(object.getPosition()))
                return trap;
        }
        return null;
    }

    public static void dismantle(Player client, GameObject trap) {
        if (trap == null)
            return;
        final Trap theTrap = getTrapForGameObject(trap);
        if (theTrap != null && theTrap.getOwner() == client) {
            deregister(theTrap);
            if (theTrap instanceof SnareTrap)
                client.inventory.add(10006, 1);
            else if (theTrap instanceof BoxTrap) {
                client.inventory.add(10008, 1);
                client.animate(new Animation(827));
            }
            client.message("You dismantle the trap..");
        } else
            client.message("You cannot dismantle someone else's trap.");
    }

    public static void layTrap(Player client, Trap trap) {
        int id = 10006;
        if (trap instanceof BoxTrap) {
            id = 10008;
            if (client.skills.getLevel(Skill.HUNTER) < 60) {
                client.message("You need a Hunter level of at least 60 to lay this trap.");
                return;
            }
        }
        if (!client.inventory.contains(id))
            return;
        if (canLay(client)) {
            register(trap);
//            client.getClickDelay().reset();
            client.takeStep();
            client.face(trap.getGameObject());
            client.animate(new Animation(827));
            if (trap instanceof SnareTrap) {
                client.message("You set up a bird snare..");
                client.inventory.remove(10006, 1);
            } else if (trap instanceof BoxTrap) {
                if (client.skills.getLevel(Skill.HUNTER) < 27) {
                    client.message("You need a Hunter level of at least 27 to do this.");
                    return;
                }
                client.message("You set up a box trap..");
                client.inventory.remove(10008, 1);
            }
            HunterTask.intialize();
        }
    }

    public static int requiredLevel(int npcType) {
        int levelToReturn = 1;
        if (npcType == 5072)
            levelToReturn = 19;
        else if (npcType == 5074)
            levelToReturn = 11;
        else if (npcType == 5075)
            levelToReturn = 5;
        else if (npcType == 5076)
            levelToReturn = 9;
        else if (npcType == 5079)
            levelToReturn = 53;
        else if (npcType == 5080)
            levelToReturn = 63;
        return levelToReturn;
    }

    public static boolean isHunterNPC(int npc) {
        return npc >= 5072 && npc <= 5080;
    }

    public static void lootTrap(Player client, GameObject trap) {
        if (trap != null) {
            client.face(trap.getPosition());
            final Trap theTrap = getTrapForGameObject(trap);
            if (theTrap != null) {
                if (theTrap.getOwner() != null)
                    if (theTrap.getOwner() == client) {
                        if (theTrap instanceof SnareTrap) {
                            client.inventory.add(10006, 1);
                            client.inventory.add(526, 1);
                            if (theTrap.getGameObject().getId() == 19180) {
                                client.inventory.add(10088, 20 + Utility.random(30));
                                client.inventory.add(9978, 1);
                                client.message("You've succesfully caught a crimson swift.");
                                client.skills.addExperience(Skill.HUNTER, exps[0]);
                            } else if (theTrap.getGameObject().getId() == 19184) {
                                client.inventory.add(10090, 20 + Utility.random(30));
                                client.inventory.add(9978, 1);
                                client.message("You've succesfully caught a Golden Warbler.");
                                client.skills.addExperience(Skill.HUNTER, exps[1]);
                            } else if (theTrap.getGameObject().getId() == 19186) {
                                client.inventory.add(10091, 20 + Utility.random(50));
                                client.inventory.add(9978, 1);
                                client.message("You've succesfully caught a Copper Longtail.");
                                client.skills.addExperience(Skill.HUNTER, exps[2]);
                            } else if (theTrap.getGameObject().getId() == 19182) {
                                client.inventory.add(10089, 20 + Utility.random(30));
                                client.inventory.add(9978, 1);
                                client.message("You've succesfully caught a Cerulean Twitch.");
                                client.skills.addExperience(Skill.HUNTER, (exps[3]));
                            } else if (theTrap.getGameObject().getId() == 19178) {
                                client.inventory.add(10087, 20 + Utility.random(30));
                                client.inventory.add(9978, 1);
                                client.message("You've succesfully caught a Tropical Wagtail.");
                                client.skills.addExperience(Skill.HUNTER, exps[4]);
                            }
                        } else if (theTrap instanceof BoxTrap) {
                            client.inventory.add(10008, 1);
                            if (theTrap.getGameObject().getId() == 19191) {
                                client.inventory.add(10033, 1);
                                client.skills.addExperience(Skill.HUNTER, exps[6]);
                                client.message("You've succesfully caught a chinchompa!");
                            } else if (theTrap.getGameObject().getId() == 19189) {
                                client.inventory.add(10034, 1);
                                client.skills.addExperience(Skill.HUNTER, exps[7]);
                                client.message("You've succesfully caught a red chinchompa!");
                            }
                        }
                        deregister(theTrap);
                        client.animate(new Animation(827));
                    } else
                        client.message("This is not your trap.");
            }
        }
    }

    public static void catchNPC(Trap trap, Npc npc) {
        if (trap.getTrapState().equals(Trap.TrapState.CAUGHT))
            return;
        if (trap.getOwner() != null) {
            if (trap.getOwner().skills.getLevel(Skill.HUNTER) < requiredLevel(npc.id)) {
                trap.getOwner().message("You failed to catch the animal because your Hunter level is too low.");
                trap.getOwner().message("You need atleast " + requiredLevel(npc.id) + " Hunter to catch this animal");
                return;
            }
            deregister(trap);
            if (trap instanceof SnareTrap) {
                GameObject object = new CustomGameObject(getObjectIDByNPCID(npc.id), new Position(trap.getGameObject().getPosition().getX(), trap.getGameObject().getPosition().getY()));
                register(new SnareTrap(object, Trap.TrapState.CAUGHT, 100, trap.getOwner()));
            } else {
                GameObject object = new CustomGameObject(getObjectIDByNPCID(npc.id), new Position(trap.getGameObject().getPosition().getX(), trap.getGameObject().getPosition().getY()));
                register(new BoxTrap(object, Trap.TrapState.CAUGHT, 100, trap.getOwner()));
            }
            HUNTER_NPC_LIST.remove(npc);
            npc.setVisible(false);
            npc.appendDeath();
        }
    }

    public static boolean hasLarupia(Player client) {
        return false;
//        return client.equipment.getItems()[Equipment.HEAD_SLOT].getId() == 10045 && client.getEquipment().getItem()[Equipment.BODY_SLOT].getId() == 10043 && client.getEquipment().getItem()[Equipment.LEG_SLOT].getId() == 10041;
    }

    public static void handleLogout(Player p) {
        if (p.trapsLaid > 0) {
            for (Trap trap : traps) {
                if (trap != null) {
                    if (trap.getOwner() != null && trap.getOwner().getUsername().equals(p.getUsername())) {
                        deregister(trap);
                        if (trap instanceof SnareTrap)
                            p.inventory.add(10006, 1);
                        else if (trap instanceof BoxTrap) {
                            p.inventory.add(10008, 1);
                            p.animate(new Animation(827));
                        }
                    }
                }
            }
        }
    }
}
