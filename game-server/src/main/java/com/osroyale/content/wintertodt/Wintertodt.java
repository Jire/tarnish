package com.osroyale.content.wintertodt;

import com.osroyale.Config;
import com.osroyale.content.collectionlog.CollectionLog;
import com.osroyale.content.collectionlog.CollectionLogData;
import com.osroyale.content.skill.impl.woodcutting.AxeData;
import com.osroyale.content.wintertodt.actions.*;
import com.osroyale.game.task.Task;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.mob.Direction;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.object.CustomGameObject;
import com.osroyale.game.world.object.GameObject;
import com.osroyale.game.world.position.Position;
import com.osroyale.game.world.region.Region;
import com.osroyale.net.packet.out.SendConfig;
import com.osroyale.net.packet.out.SendRemoveObject;
import com.osroyale.net.packet.out.SendString;
import com.osroyale.util.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Wintertodt {

    private static final int DEFAULT_DELAY = 10;

    /**
     * Region of the wintertodt game
     */
    public static final int REGION_ID = 6462;

    /**
     * Game data
     */
    private static boolean started = false;
    public static final int MAX_HP = 3500;
    public static int health = 0;
    private static int startDelay = DEFAULT_DELAY;
    private static int pyroSpeechDelay;
    public static Region region;

    /**
     * Item ids
     */
    public static final int BRUMA_ROOT = 20695;
    public static final int BRUMA_KINDLING = 20696;
    public static final int REJUV_POT_UNF = 20697;
    public static final int REJUV_POT_4 = 20699;
    public static final int REJUV_POT_3 = 20700;
    public static final int REJUV_POT_2 = 20701;
    public static final int REJUV_POT_1 = 20702;
    public static final int BRUMA_HERB = 20698;

    /**
     * Npc ids
     */
    private static final String[] PYROMANCER_DEAD_TEXT = {"My flame burns low.", "Mummy!", "I think I'm dying.", "We are doomed.", "Ugh, help me!"};

    public static final int PYROMANCER = 7371;
    public static final int INCAPACITATED_PYROMANCER = 7372;
    private static final int FLAME = 7373;

    /**
     * Game object ids
     */
    private static final int SNOW_EFFECT_ID = 26690;
    private static final int ACTIVE_STORM_ID = 29308;
    private static final int INACTIVE_STORM_ID = 29309;
    public static final int EMPTY_BRAZIER_ID = 29312;
    public static final int BROKEN_BRAZIER_ID = 29313;
    public static final int BURNING_BRAZIER_ID = 29314;
    private static CustomGameObject WINTERTODT;

    /**
     * List of all minigame items
     */
    private static final List<Integer> GAME_ITEMS = Arrays.asList(BRUMA_ROOT, BRUMA_KINDLING, BRUMA_HERB, REJUV_POT_UNF, REJUV_POT_4, REJUV_POT_3, REJUV_POT_2, REJUV_POT_1);

    /**
     * List of all warm clothing
     */

    private static final List<Integer> WARM_CLOTHING = Arrays.asList(
            //Santa outfits
            1050, 12887, 12888, 12889, 12890, 12891, 12892, 12893, 12894, 12895, 12896, 13343, 13344,
            //Bunny outfit
            23448, 13663, 13664, 13665, 13182,
            //Clue hunter outfit
            19689, 19691, 19693, 19695, 19697,
            //Polar camo
            10065, 10066,
            //Wood camo
            10053, 10055,
            //Jungle camo
            10057, 10059,
            //Desert camo
            10061, 10063,
            //Larupia
            10045, 10043, 10041,
            //Graahk
            10051, 10049, 10047,
            //Kyatt
            10039, 10037, 10035,
            //Bomber
            9945, 9944,
            //Yakhide armour
            10822, 10824,
            //Pyromancer
            20708, 20706, 20704, 20710,
            //Chicken outfit
            11021, 11020, 11022, 11019,
            //Evil chicken
            20439, 20436, 20442, 20434,
            //Bearhead
            4502,
            //Fire tiara
            5537,
            //Lumberjack hat
            10941,
            //Firemaking hood
            9806,
            //Fire cape max hood
            13330,
            //Infernal max hood
            21282,
            //Scarfs
            6857, 6859, 6861, 6863, 9470, 21314,
            //Gloves of silence
            10075,
            //Fremennik gloves
            3799,
            //Warm gloves
            20712,
            //Firemaking cape
            9804, 9805,
            //Max cape
            13280, 13329, 21284, 21285, 13337, 20760, 21898, 24855, 21776, 21780, 21778, 13331, 13333, 13335,
            //Fire cape
            6570,
            //Obsidian cape
            6568, 20050,
            //Weapons
            1387, 1393, 3053, 11787, 11998, 1401, 3054, 11789, 12000, 13241, 13242, 13243, 13244, 21031, 21033, 12773, 20056, 20720,
            //Shields
            20714, 20716, 7053
            //TODO: Add the last few (https://i.imgur.com/Pif4I0l.png)
    );

    /**
     * Brazier data
     */
    public static final Brazier[] BRAZIERS = {
            new Brazier(World.getRegions().getRegion(new Position(1620, 3997, 0)).getGameObject(29312, new Position(1620, 3997, 0)), new Npc(7371, new Position(1619, 3996, 0), 0, Direction.NORTH_EAST), 2, 2, Direction.NORTH_EAST), // sw
            new Brazier(World.getRegions().getRegion(new Position(1620, 4015, 0)).getGameObject(29312, new Position(1620, 4015, 0)), new Npc(7371, new Position(1619, 4018, 0), 0, Direction.SOUTH_EAST), 2, 0, Direction.SOUTH_EAST), // nw
            new Brazier(World.getRegions().getRegion(new Position(1638, 4015, 0)).getGameObject(29312, new Position(1638, 4015, 0)), new Npc(7371, new Position(1641, 4018, 0), 0, Direction.SOUTH_WEST), 0, 0, Direction.SOUTH_WEST), // ne
            new Brazier(World.getRegions().getRegion(new Position(1638, 3997, 0)).getGameObject(29312, new Position(1638, 3997, 0)), new Npc(7371, new Position(1641, 3996, 0), 0, Direction.NORTH_WEST), 0, 2, Direction.NORTH_WEST) // se
    };

    public static boolean isActive() {
        return startDelay <= 0;
    }

    public static void init() {
        region = World.getRegions().getRegion(new Position(1627, 4004, 0));
        pyroSpeechDelay = 8;
        startDelay = DEFAULT_DELAY;
        WINTERTODT = new CustomGameObject(INACTIVE_STORM_ID, new Position(1627, 4004, 0));
        WINTERTODT.register();
        started = false;

        for(Brazier brazier : BRAZIERS)
            brazier.getPyromancer().register();

        World.schedule(new Task(1) {
            @Override
            public void execute() {
                if(startDelay > 0)
                    startDelay--;

                if(startDelay <= 0 && !started) start();

                update();
            }
        });

        World.schedule(new Task(2) {
            @Override
            public void execute() {
                if(!isActive()) return;

                pyroSpeechDelay--;
                applyColdDamage();
                extinguishBraziers();
                doMagicAttack();
                attackPyromancers();
                pyromancerText();
                dealDamage();
            }
        });
    }

    private static void start() {
        WINTERTODT.transform(ACTIVE_STORM_ID);

        for (Brazier brazier : BRAZIERS) {
            //brazier.getObject().transform(EMPTY_BRAZIER_ID);
            brazier.getObject().unregister();
            brazier.setObject(EMPTY_BRAZIER_ID);
            brazier.getObject().register();
            if (!brazier.isPyromancerAlive())
                brazier.getPyromancer().transform(PYROMANCER);
        }

        health = MAX_HP;
        started = true;
        update();
    }

    private static void update() {
        region.getPlayers(0).forEach(Wintertodt::send);
    }

    public static void send(Player player) {
        player.send(new SendString("Wintertodt's Energy: "+(health / (MAX_HP / 100))+"%", 41553));
        player.send(new SendString(isActive() ? "" : "The Wintertodt returns in: " + ((startDelay * 600) / 1000) + " secs", 41554));
        player.send(new SendString("<col="+(player.wintertodtPoints >= 500 ? "FFFFFF" : "FF0000")+">Points\\n" + player.wintertodtPoints, 41556));

        //Sends the pyromancer config stuff
        player.send(new SendConfig(2224, BRAZIERS[0].isPyromancerAlive() ? 0 : 1));
        player.send(new SendConfig(2222, BRAZIERS[1].isPyromancerAlive() ? 0 : 1));
        player.send(new SendConfig(2223, BRAZIERS[2].isPyromancerAlive() ? 0 : 1));
        player.send(new SendConfig(2225, BRAZIERS[3].isPyromancerAlive() ? 0 : 1));
        //Sends the brazier config stuff
        player.send(new SendConfig(2228, BRAZIERS[0].getBrazierState()));
        player.send(new SendConfig(2226, BRAZIERS[1].getBrazierState()));
        player.send(new SendConfig(2227, BRAZIERS[2].getBrazierState()));
        player.send(new SendConfig(2229, BRAZIERS[3].getBrazierState()));

        if (player.interfaceManager.getWalkable() != 41550)
            player.interfaceManager.openWalkable(41550);
    }

    private static void applyColdDamage() {
        region.getPlayers(0).forEach(player -> {
            if(player.getPosition().getY() <= 3987 || Utility.random(25) != 0) return;

            player.damage(new Hit(getColdDamage(player)));
            player.message("The cold of the Wintertodt seeps into your bones.");

            if(player.action.getCurrentAction() instanceof WintertodtAction)
                player.action.getCurrentAction().cancel();
        });
    }

    /**
     * Handles extinguishing the braziers
     */
    private static void extinguishBraziers() {
        for (Brazier brazier : BRAZIERS) {
            int roll = Utility.random(health + 1500) / 10;
            if(brazier.getObject().getId() == BURNING_BRAZIER_ID && roll == 10) {
                if(brazier.hasSnowStorm()) continue;

                if(Utility.random(health < (MAX_HP / 2) ? 2 : 3) == 1)
                    breakBrazier(brazier);
                else {
                    //Graphic (502, 115, 0)
                    //brazier.getObject().transform(EMPTY_BRAZIER_ID);
                    brazier.getObject().unregister();
                    brazier.setObject(EMPTY_BRAZIER_ID);
                    brazier.getObject().register();
                }
            }
        }
    }

    /**
     * Handles breaking the brazier
     */
    private static void breakBrazier(Brazier brazier) {
        if(brazier.getObject().getId() == BROKEN_BRAZIER_ID) return;

        List<GameObject> objects = Arrays.asList(
                new CustomGameObject(SNOW_EFFECT_ID, new Position(brazier.getObject().getPosition().getX() + 1, brazier.getObject().getPosition().getY(), 0)),
                new CustomGameObject(SNOW_EFFECT_ID, new Position(brazier.getObject().getPosition().getX(), brazier.getObject().getPosition().getY() + 1, 0)),
                new CustomGameObject(SNOW_EFFECT_ID, new Position(brazier.getObject().getPosition().getX() + 1, brazier.getObject().getPosition().getY() + 1, 0)),
                new CustomGameObject(SNOW_EFFECT_ID, new Position(brazier.getObject().getPosition().getX() + 2, brazier.getObject().getPosition().getY() + 1, 0)),
                new CustomGameObject(SNOW_EFFECT_ID, new Position(brazier.getObject().getPosition().getX() + 1, brazier.getObject().getPosition().getY() + 2, 0))
        );

        for(GameObject gameObject : objects)
            gameObject.register();

        brazier.setSnowStorm(true);

        World.schedule(new Task(4) {
            @Override
            public void execute() {

                for(GameObject gameObject : objects) {
                    gameObject.unregister();
                    region.getPlayers(0).forEach(players -> {
                        players.send(new SendRemoveObject(new CustomGameObject(gameObject.getId(), gameObject.getPosition())));
                    });
                }

                brazier.setSnowStorm(false);

                if(isActive()) {
                    //Graphio (502, 90, 0)
                    //brazier.getObject().transform(BROKEN_BRAZIER_ID);
                    brazier.getObject().unregister();
                    brazier.setObject(Wintertodt.BROKEN_BRAZIER_ID);
                    brazier.getObject().register();
                    region.getPlayers(0).forEach(player -> {
                        if(Utility.goodDistance(brazier.getObject().getPosition().getX() + 1, brazier.getObject().getPosition().getY() + 1, player.getPosition().getX(), player.getPosition().getY(), 2)) {
                            player.message("The brazier is broken and shrapnel damages you.");
                            player.damage(new Hit(Utility.random(getBrazierAttackDamage(player))));
                        }
                    });
                }
                this.cancel();
            }
        });
    }

    /**
     * How much damage the brazier does to a player when it breaks
     */
    public static int getBrazierAttackDamage(Player player) {
        return (int) ((10.0 - getWarmItemsWorn(player)) * (Skill.getLevelForExperience(player.skills.get(Skill.HITPOINTS).getExperience()) + 1) / Skill.getLevelForExperience(player.skills.get(Skill.FIREMAKING).getExperience())) * 2;
    }

    /**
     * Handles doing the magic attack by the bnoss
     */
    private static void doMagicAttack() {
        if(Utility.random(25) != 1 || region.getPlayers(0).size() == 0) return;

        Player player = region.getPlayers(0).toArray(new Player[0])[Utility.random(region.getPlayers(0).size())];
        if (player.getPosition().getY() <= 3987) return;

        int baseX = player.getPosition().getX();
        int baseY = player.getPosition().getY();
        List<GameObject> snowAttacks = new ArrayList<>();

        snowAttacks.add(new CustomGameObject(SNOW_EFFECT_ID, new Position(baseX, baseY, 0)));
        if(region.getObjects(new Position(baseX + 1, baseY + 1, 0)).size() == 0)
            snowAttacks.add(new CustomGameObject(SNOW_EFFECT_ID, new Position(baseX + 1, baseY + 1, 0)));
        if(region.getObjects(new Position(baseX + 1, baseY - 1, 0)).size() == 0)
            snowAttacks.add(new CustomGameObject(SNOW_EFFECT_ID, new Position(baseX + 1, baseY - 1, 0)));
        if(region.getObjects(new Position(baseX - 1, baseY + 1, 0)).size() == 0)
            snowAttacks.add(new CustomGameObject(SNOW_EFFECT_ID, new Position(baseX - 1, baseY + 1, 0)));
        if(region.getObjects(new Position(baseX - 1, baseY - 1, 0)).size() == 0)
            snowAttacks.add(new CustomGameObject(SNOW_EFFECT_ID, new Position(baseX - 1, baseY - 1, 0)));

        for(GameObject gameObject : snowAttacks)
            gameObject.register();

        World.schedule(new Task(4) {
            int index = 0;
            @Override
            public void execute() {
                for(GameObject gameObject : snowAttacks) {
                    gameObject.transform(index == 0 ? 29325 : 29324);
                    index++;
                }

                region.getPlayers(0).forEach(players -> {
                    if(Utility.goodDistance(baseX, baseY, players.getX(), player.getY(), 1)) {
                        players.message("The freezing cold attack of the Wintertodt's magic hits you.");
                        players.damage(new Hit(1 + (getAreaAttackDamage(players) - 1)));
                    }
                });
                this.cancel();
            }
        });

        World.schedule(new Task(14) {
            @Override
            public void execute() {
                for(GameObject gameObject : snowAttacks) {
                    gameObject.unregister();
                    region.getPlayers(0).forEach(players -> {
                        players.send(new SendRemoveObject(new CustomGameObject(gameObject.getId(), gameObject.getPosition())));
                    });
                }
                this.cancel();
            }
        });
    }

    /**
     * Handles the formula on how much the area attack can do
     */
    public static int getAreaAttackDamage(Player player) {
        return (int) ((10.0 - getWarmItemsWorn(player)) * (Skill.getLevelForExperience(player.skills.get(Skill.HITPOINTS).getExperience()) + 1) / Skill.getLevelForExperience(player.skills.get(Skill.FIREMAKING).getExperience()) * 3);
    }

    /**
     * Handles attacking the pyromancers
     */
    private static void attackPyromancers() {
        List<Npc> pyros = Arrays.stream(BRAZIERS).filter(Brazier::isPyromancerAlive).map(Brazier::getPyromancer).collect(Collectors.toList());
        if (!pyros.isEmpty() && Utility.random(pyros.size() * 30) == pyros.size()) {
            Npc pyro = pyros.get(Utility.random(pyros.size()));
            if(pyro.pyroHealth <= 0) return;

            if(pyro.pyroSnowAttack) {
                System.out.println("already a snow attack going for this pyro...");
                return;
            }

            pyro.pyroSnowAttack = true;
            GameObject snow = new CustomGameObject(SNOW_EFFECT_ID, pyro.getPosition());
            snow.register();

            World.schedule(new Task(4) {
                @Override
                public void execute() {
                    int damage = 6 + Utility.random(4);
                    if(damage > pyro.pyroHealth)
                        damage = pyro.pyroHealth;
                    pyro.pyroHealth -= damage;
                    pyro.writeFakeDamage(new Hit(damage));
                    if(pyro.pyroHealth <= 0) {
                        pyro.pyroHealth = 0;
                        pyro.transform(INCAPACITATED_PYROMANCER);
                    }
                    pyro.pyroSnowAttack = false;
                    region.getPlayers(0).forEach(players -> {
                        players.send(new SendRemoveObject(new CustomGameObject(snow.getId(), snow.getPosition())));
                    });
                    snow.unregister();
                    this.cancel();
                }
            });
        }
    }

    /**
     * Handles the pyromancer speech
     */
    private static void pyromancerText() {
        if(pyroSpeechDelay <= 0) {
            pyroSpeechDelay = 8;
            for (Brazier brazier : BRAZIERS) {
                if (!brazier.isPyromancerAlive())
                    brazier.getPyromancer().speak(PYROMANCER_DEAD_TEXT[Utility.random(PYROMANCER_DEAD_TEXT.length - 1)]);
                else if (brazier.getObject().getId() == EMPTY_BRAZIER_ID)
                    brazier.getPyromancer().speak("Light this brazier!");
                else if (brazier.getObject().getId() == BROKEN_BRAZIER_ID)
                    brazier.getPyromancer().speak("Fix this brazier!");
                else if (Utility.random(4) == 1)
                    brazier.getPyromancer().speak("Yemalo shi cardito!");

                if (brazier.isPyromancerAlive() && brazier.getObject().getId() == BURNING_BRAZIER_ID && Utility.random(3) == 1)
                    brazier.getPyromancer().animate(4432);
            }
        }
    }

    /**
     * Handles dealing damage to the boss
     */
    private static void dealDamage() {
        int damage = 0;
        for (Brazier brazier : BRAZIERS) {
            if (brazier.isPyromancerAlive() && brazier.getObject().getId() == BURNING_BRAZIER_ID) {
                shootFlame(brazier);
                damage += 5;
            }
        }

        if (damage > 0) {
            health -= damage;
            if (health <= 0) {
                health = 0;
                death();
            }
        } else
            health = Math.min(MAX_HP, health + 5);
    }

    /**
     * Handles spawning the flame that goes to the middle
     */
    public static void shootFlame(Brazier brazier) {
        Npc flame = new Npc(FLAME, new Position(brazier.getObject().getPosition().getX() + brazier.getFlameOffsetX(), brazier.getObject().getPosition().getY() + brazier.getFlameOffsetY(), brazier.getObject().getPosition().getHeight()), 0, brazier.direction);
        flame.movement.simplePath(new Position(1630, 4007));
        flame.register();
        flame.action.execute(new FlameWalk(flame));
    }

    /**
     * Handles the death of the boss
     */
    private static void death() {
        started = false;
        startDelay = DEFAULT_DELAY;

        WINTERTODT.transform(INACTIVE_STORM_ID);

        for (Brazier brazier : BRAZIERS) {
            brazier.getPyromancer().speak("We can rest for a time.");
            brazier.getPyromancer().transform(PYROMANCER);
            brazier.getPyromancer().pyroSnowAttack = false;
            brazier.getPyromancer().pyroHealth = 24;
            brazier.getPyromancer().animate(65535);
            //brazier.getObject().transform(EMPTY_BRAZIER_ID);
            brazier.getObject().unregister();
            brazier.setObject(EMPTY_BRAZIER_ID);
            brazier.getObject().register();

        }
        region.getPlayers(0).forEach(Wintertodt::award);
    }

    /**
     * Handles awarding players with crates
     */
    private static void award(Player player) {
        removeGameItems(player);

        if (player.wintertodtPoints >= 500) {
            CollectionLog.increaseCounter(player, CollectionLogData.WINTERTODT);
            player.skills.addExperience(Skill.FIREMAKING, (Skill.getLevelForExperience(player.skills.get(Skill.FIREMAKING).getExperience()) * 100) * Config.FIREMAKING_MODIFICATION);
            int crates = player.wintertodtPoints / 500;
            if (crates > 1)
                player.message("You have gained " + crates + " supply crates!");
            else
                player.message("You have gained a supply crate!");
            player.inventory.add(new Item(20703, crates));
            player.inventory.refresh();
        } else player.message("You did not earn enough points to be worthy of a gift from the citizens of Kourend this time.");

        player.wintertodtPoints = 0;
    }

    /**
     * Handles removes the items within the wintertodt game
     */
    public static void removeGameItems(Player player) {
        int slot = 0;
        for (Item item : player.inventory.getItems()) {
            if (item != null && GAME_ITEMS.contains(item.getId())) {
                player.inventory.set(slot, null, false);
            }
            slot++;
        }
        player.inventory.refresh();
    }

    /**
     * The amount of damagae the player gets from cold
     */
    public static int getColdDamage(Player player) {
        return (int) ((16.0 - getWarmItemsWorn(player) - (2 * getBraziersLit())) * (Skill.getLevelForExperience(player.skills.get(Skill.HITPOINTS).getExperience()) + 1) / Skill.getLevelForExperience(player.skills.get(Skill.FIREMAKING).getExperience()));
    }

    /**
     * How much warm clothing the player is wearing
     */
    public static int getWarmItemsWorn(Player player) {
        int warmClothing = 0;
        for(int id : WARM_CLOTHING) {
            if(player.equipment.contains(id)) warmClothing++;
            if(warmClothing >= 4) break;
        }
        return warmClothing;
    }

    /**
     * How many braziers are currently lit
     */
    public static int getBraziersLit() {
        int count = 0;
        for (Brazier b : BRAZIERS)
            if (b.getObject().getId() == BURNING_BRAZIER_ID)
                count++;
        return Math.min(count, 3);
    }

    /**
     * Handles chopping the root
     */
    public static void chopRoot(Player player) {
        if(!isActive()) {
            player.message("There's no need to do that at this time.");
            return;
        }

        AxeData axeData = AxeData.getDefinition(player).orElse(null);

        if(axeData == null) {
            player.message("You do not have an axe which you have the woodcutting level to use.");
            return;
        }

        if(player.inventory.getFreeSlots() <= 0) {
            player.message("You have no space for that.");
            return;
        }

        player.action.execute(new ChopRoots(player));
    }

    /**
     * Handles feeding the brazier
     */
    public static void feedBrazier(Player player, GameObject gameObject) {
        if(!isActive()) {
            player.message("There's no need to do that at this time.");
            return;
        }

        Brazier brazier = null;

        for(Brazier b : BRAZIERS) {
            if(b.getObject().getId() == gameObject.getId() && b.getObject().getPosition() == gameObject.getPosition()) {
                brazier = b;
                break;
            }
        }

        if(brazier == null) {
            System.out.println("Brazier has not been found...");
            return;
        }

        player.animate(832);
        player.action.execute(new FeedBrazier(player, brazier));
    }

    /**
     * Handles fixing the brazier
     */
    public static void fixBrazier(Player player, GameObject gameObject) {
        if(!isActive()) {
            player.message("There's no need to do that at this time.");
            return;
        }

        Brazier brazier = null;

        for(Brazier b : BRAZIERS) {
            if(b.getObject().getId() == gameObject.getId() && b.getObject().getPosition() == gameObject.getPosition()) {
                brazier = b;
                break;
            }
        }

        if(brazier == null) {
            System.out.println("Brazier has not been found...");
            return;
        }

        if(!brazier.isPyromancerAlive()) {
            player.message("Heal the Pyromancer before fixing the brazier.");
            return;
        }

        if(!player.inventory.contains(2347)) {
            player.message("You need a hammer to fix this brazier.");
            return;
        }

        player.animate(3676);
        player.action.execute(new FixBrazier(player, brazier));
    }

    /**
     * Handles fetching the kindling
     */
    public static void fletch(Player player) {
        if(!isActive()) {
            player.message("There's no need to do that at this time.");
            return;
        }

        if(!player.inventory.contains(BRUMA_ROOT)) return;

        player.animate(1248);
        player.action.execute(new FletchKindling(player, player.inventory.computeAmountForId(BRUMA_ROOT)));
    }

    /**
     * Handles lighting a brazier
     */
    public static void lightBrazier(Player player, GameObject gameObject) {
        if(!isActive()) {
            player.message("There's no need to do that at this time.");
            return;
        }

        Brazier brazier = null;

        for(Brazier b : BRAZIERS) {
            if(b.getObject().getId() == gameObject.getId() && b.getObject().getPosition() == gameObject.getPosition()) {
                brazier = b;
                break;
            }
        }

        if(brazier == null) {
            System.out.println("Brazier has not been found...");
            return;
        }

        if(!brazier.isPyromancerAlive()) {
            player.message("Heal the Pyromancer before lighting the brazier.");
            return;
        }

        if(!player.inventory.contains(590) && !player.equipment.contains(20720)) {
            player.message("You need a tinderbox or Bruma Torch to light that brazier.");
            return;
        }

        player.animate(733);
        player.action.execute(new LightBrazier(player, brazier));
    }

    /**
     * Handles mixing the potion
     */
    public static void mixHerb(Player player) {
        if(!isActive()) {
            player.message("There's no need to do that at this time.");
            return;
        }

        int herbs = player.inventory.computeAmountForId(BRUMA_HERB);
        int pots = player.inventory.computeAmountForId(REJUV_POT_UNF);
        int min = Utility.min(herbs, pots);

        if(min == 0) return;

        player.animate(363);
        player.action.execute(new MixHerb(player, min));
    }

    /**
     * Handles taking a bruma herb
     */
    public static void takeHerb(Player player) {
        if(!isActive()) {
            player.message("There's no need to do that at this time.");
            return;
        }

        if(player.inventory.getFreeSlots() <= 0) {
            player.message("You have no space for that.");
            return;
        }

        player.animate(2282);
        player.action.execute(new PickHerb(player));
    }

    /**
     * Handles healing the pyromancer
     */
    public static void healPyromancer(Player player, Npc npc, int slot) {
        if(!isActive()) {
            player.message("There's no need to do that at this time.");
            return;
        }

        Brazier brazier = null;

        for(Brazier b : BRAZIERS) {
            if(b.getPyromancer().getPosition() == npc.getPosition() && b.getPyromancer().id == npc.id) {
                brazier = b;
                break;
            }
        }

        if(brazier == null) {
            System.out.println("Brazier has not been found...");
            return;
        }

        Item itemUsed = player.inventory.get(slot);

        if(itemUsed.getId() != REJUV_POT_4 && itemUsed.getId() != REJUV_POT_3 && itemUsed.getId() != REJUV_POT_2 && itemUsed.getId() != REJUV_POT_1) {
            System.out.println("not a rejuv potion");
            return;
        }

        Item newPot = new Item(player.inventory.getId(slot) + 1);
        if(newPot.getId() > REJUV_POT_1) newPot = null;

        player.inventory.set(slot, newPot, true);

        brazier.getPyromancer().transform(PYROMANCER);
        brazier.getPyromancer().pyroHealth = 24;
        brazier.getPyromancer().pyroSnowAttack = false;

        addPoints(player, 30);
    }

    /**
     * Handles adding wintertodt points to a player
     */
    public static void addPoints(Player player, int amount) {
        int old = player.wintertodtPoints;
        player.wintertodtPoints += amount;
        if (old < 500 && player.wintertodtPoints >= 500) {
            player.message("You have helped enough to earn a supply crate. Further work will go towards better rewards.");
        }
        send(player);
    }
}
