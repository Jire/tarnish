package plugin.click.object;

import com.osroyale.Config;
import com.osroyale.content.activity.Activity;
import com.osroyale.content.activity.ActivityType;
import com.osroyale.content.activity.impl.CerberusActivity;
import com.osroyale.content.activity.impl.VorkathActivity;
import com.osroyale.content.activity.impl.fightcaves.FightCaves;
import com.osroyale.content.activity.impl.kraken.KrakenActivity;
import com.osroyale.content.activity.impl.pestcontrol.PestControlLobby;
import com.osroyale.content.activity.impl.warriorguild.WarriorGuild;
import com.osroyale.content.activity.impl.zulrah.ZulrahActivity;
import com.osroyale.content.combat.cannon.CannonManager;
import com.osroyale.content.dialogue.impl.WellOfGoodwillDialogue;
import com.osroyale.content.shootingstar.ShootingStar;
import com.osroyale.content.skill.impl.magic.Spellbook;
import com.osroyale.content.skill.impl.magic.teleport.Teleportation;
import com.osroyale.content.skill.impl.slayer.SlayerTask;
import com.osroyale.content.store.Store;
import com.osroyale.content.store.impl.PersonalStore;
import com.osroyale.content.teleport.TeleportHandler;
import com.osroyale.game.Animation;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.action.impl.DoorAction;
import com.osroyale.game.action.impl.LadderAction;
import com.osroyale.game.action.impl.LeverAction;
import com.osroyale.game.action.impl.RevShortcutAction;
import com.osroyale.game.event.impl.ObjectClickEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.task.impl.ChopVineTask;
import com.osroyale.game.task.impl.ObjectReplacementEvent;
import com.osroyale.game.task.impl.SteppingStoneTask;
import com.osroyale.game.world.InterfaceConstants;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.combat.CombatType;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.hit.HitIcon;
import com.osroyale.game.world.entity.combat.hit.Hitsplat;
import com.osroyale.game.world.entity.combat.magic.Autocast;
import com.osroyale.game.world.entity.combat.strategy.player.special.CombatSpecial;
import com.osroyale.game.world.entity.mob.Direction;
import com.osroyale.game.world.entity.mob.data.LockType;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.object.GameObject;
import com.osroyale.game.world.object.ObjectDirection;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.packet.out.SendCameraReset;
import com.osroyale.net.packet.out.SendInputMessage;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.net.packet.out.SendRunEnergy;
import com.osroyale.util.RandomUtils;
import com.osroyale.util.Utility;
import plugin.itemon.RottenTomatoePlugin;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class ObjectFirstClickPlugin extends PluginContext {
    int LADDER_EMOTE = 828;

    public void animateAndMovePlayer(Player player, int x, int y, int z) {
        player.animate(LADDER_EMOTE);
        World.schedule(2, () -> player.move(new Position(x, y, z)));
    }

    @Override
    protected boolean firstClickObject(Player player, ObjectClickEvent event) {
        final GameObject object = event.getObject();
        final int id = object.getId();

        switch (id) {

            case 41229:
            case 41228:
            case 41227:
            case 41226:
            case 41225:
            case 41224:
            case 41223:
            case 41021:
                ShootingStar.mine(player, object);
                break;

            /**
             * Wilderness Slayer Cave
             */
            case 40388:
                player.move(new Position(3385, 10052, 0));
                break;
            case 40389:
                player.move(new Position(3260, 3663, 0));
                break;
            case 40390:
                player.move(new Position(3406, 10145, 0));
                break;
            case 40391:
                player.move(new Position(3293, 3749, 0));
                break;
            /**
             * Catacombs
             */
            case 28892: //Hillgiants to ankous
                int targetX = player.facePosition.getX() == 1646 ? 1648 : 1646;
                int targetY = player.facePosition.getX() == 1646 ? 10009 : 10000;

                player.animate(746, true);

                World.schedule(1, () -> {
                    player.move(new Position(targetX, targetY, 0));
                    player.animate(748, true);
                });
                break;

            /**
             * Revenant Caves Teleports
             */
            case 40386: //boomerang coord
                player.move(new Position(3188, 10130, 0));
                break;
            case 43868: //south lava maze exit
                player.move(new Position(3012, 3848, 0));
                break;
            case 31556: //lava maze entrance
                player.move(new Position(3241, 10233, 0));
                break;
            case 31558: //level 17 exit ferox enclave
                player.move(new Position(3102, 3656, 0));
                break;
            case 28857://Woodcutting guild
            case 28858://Woodcutting guild
                int yPos = object.getY();
                int targetFloor = object.getId() == 28857 ? 1 : 0;
                if (yPos == 3483 || yPos == 3493) {
                    if(object.getId() == 28857 || (object.getId() == 28858 && object.getHeight() == 1)) {
                        player.animate(LADDER_EMOTE);
                        World.schedule(2, () -> player.move(new Position(1574, yPos, targetFloor)));
                    }
                }
                break;
            case 11834://Jad Exit
                if(object.getId() == 11834 && object.getX() == 2412 && object.getY() == 5118) {
                    player.dialogueFactory.sendStatement("Would you like to leave the fight caves?", "You will lose all your progress!").sendOption("Yes", () -> {
                        player.move(new Position(2438, 5168, 0));
                        player.message("You have exited the fight caves and lost your progress.");
                    }, "Nevermind", () -> player.dialogueFactory.clear()).execute();
                }
                break;
            case 31555: //level 17 entrance lava maze
                player.move(new Position(3197, 10056, 0));
                player.send(new SendMessage("You have entered the Revenant cave", true));
                break;

            /**
             * Revenant cave pillars.
             */
            case 31561:
                Direction dir = Direction.NONE;
                int shortcutLevel = 0;
                int xp = 0;

                if(object.getX() == 3241 && object.getY() == 10145) {
                    if(player.getX() == 3239) dir = Direction.EAST;
                    else if(player.getX() == 3243) dir = Direction.WEST;
                    shortcutLevel = 89;
                    xp = 15;
                } else if(object.getX() == 3180 && object.getY() == 10209) {
                    if(player.getY() == 10211) dir = Direction.SOUTH;
                    else if(player.getY() == 10207) dir = Direction.NORTH;
                    shortcutLevel = 65;
                    xp = 3;
                } else if(object.getX() == 3220 && object.getY() == 10086) {
                    if(player.getY() == 10088) dir = Direction.SOUTH;
                    else if(player.getY() == 10084) dir = Direction.NORTH;
                    shortcutLevel = 65;
                    xp = 3;
                } else if(object.getX() == 3202 && object.getY() == 10196) {
                    if(player.getX() == 3200) dir = Direction.EAST;
                    else if(player.getX() == 3204) dir = Direction.WEST;
                    shortcutLevel = 75;
                    xp = 5;
                } else if(object.getX() == 3200 && object.getY() == 10136) {
                    if(player.getX() == 3198) dir = Direction.EAST;
                    else if(player.getX() == 3202) dir = Direction.WEST;
                    shortcutLevel = 75;
                    xp = 5;
                }

                if (player.skills.getLevel(Skill.AGILITY) < shortcutLevel) {
                    player.message("You need an agility level of "+shortcutLevel+" to use this shortcut.");
                    return true;
                }

                if(dir != Direction.NONE) {
                    player.action.execute(new RevShortcutAction(player, dir, xp));
                    player.locking.lock(3);
                }
                break;

            case 16510:
                if (player.skills.getLevel(Skill.AGILITY) < 80) {
                    player.message("You need an agility level of 80 to use this shortcut.");
                    return true;
                }
                if (player.getPosition().getX() < object.getPosition().getX()) {
                    player.move(new Position(player.getX() + 2, player.getY(), player.getHeight()));
                } else if (player.getPosition().getX() > object.getPosition().getX()) {
                    player.move(new Position(player.getX() - 2, player.getY(), player.getHeight()));
                }
                break;
            /**
             * Trading post
             */
            case 13607:
                if (player.getCombat().inCombat()) {
                    player.message("You cannot open this while in combat.");
                    break;
                }
                if (PlayerRight.isIronman(player)) {
                    player.message("You can not use the Trading Post as an Ironman.");
                    break;
                }
                player.tradingPost.openOverviewInterface();
                break;
            /**
             * Brimhaven Dungeon
             */
            case 21722:
                if (player.getCombat().inCombat()) {
                    player.message("You cannot teleport while in combat.");
                }
                player.move(new Position(2643, 9594, 2));
                break;
            case 26880:
                if (player.getCombat().inCombat()) {
                    player.message("You cannot teleport while in combat.");
                }
                player.animate(LADDER_EMOTE);
                player.move(new Position(2669, 9583, 2));
                break;
            case 26882:
                if (player.getCombat().inCombat()) {
                    player.message("You cannot teleport while in combat.");
                }
                player.animate(LADDER_EMOTE);
                player.move(new Position(2674, 9583, 0));
                break;
            case 21724:
                if (player.getCombat().inCombat()) {
                    player.message("You cannot teleport while in combat.");
                }
                player.move(new Position(2650, 9591, 0));
                break;
            case 29087:
                Store.STORES.get("The LMS Store").open(player);
                break;
            /**
             * Taverly Dungeon
             */
            case 28849:
                if (player.getCombat().inCombat()) {
                    player.message("You cannot teleport while in combat.");
                }
                if(player.getX() == 2936 && player.getY() == 9810 && player.getHeight() == 0) {
                player.move(new Position(2935, 9810, 0));
                 }
                if ( player.getX() == 2935 && player.getY() == 9810 && player.getHeight() == 0) {
                    player.move(new Position(2936, 9810, 0));
                }

                break;
            /**
             * Vorkath
             */
            case 31990: {
                if (!player.inActivity(ActivityType.VORKATH)) {
                    player.move(new Position(2272, 4052));
                    player.send(new SendCameraReset());
                    player.locking.lock(7);

                    World.schedule(2, () -> {
                        Position end = new Position(2272, 4054);
                        int modX = end.getX() - player.getPosition().getX();
                        int modY = end.getY() - player.getPosition().getY();
                        Position destination = Position.create(modX, modY);
                        Direction direction = Direction.getFollowDirection(player.getPosition(), end);
                        player.locking.lock(10);
                        player.forceMove(5, 839, 23, 60, destination, direction);
                        World.schedule(4, () -> {
                            VorkathActivity.create(player);
                            player.playerAssistant.moveCamera(new Position(2269, 4062), 5);
                        });
                    });
                }
                return true;
            }
            case 1521:
            case 1524:{
                if (player.locking.locked(LockType.FREEZE)) {
                    player.send(new SendMessage("You cannot do this while frozen.", true));
                    break;
                }
                if (player.getPosition().matches(2958, 3821)) {
                    player.action.execute(new DoorAction(player, object, new Position(2957, 3821), Direction.EAST));
                } else if (player.getPosition().matches(2957, 3821)) {
                    player.action.execute(new DoorAction(player, object, new Position(2958, 3821), Direction.EAST));
                } else if (player.getPosition().matches(2957, 3820)) {
                    player.action.execute(new DoorAction(player, object, new Position(2958, 3820), Direction.EAST));
                } else if (player.getPosition().matches(2958, 3820)) {
                    player.action.execute(new DoorAction(player, object, new Position(2957, 3820), Direction.EAST));
                }
            }
            break;
            case 27215: {
                if (!player.itemDelay.elapsed(2, TimeUnit.SECONDS)) {
                    return true;
                }

                CombatType type = player.getStrategy().getCombatType();
                int maxHit = player.playerAssistant.getMaxHit(player, type);

                HitIcon hitIcon = HitIcon.MELEE;
                if (type == CombatType.MAGIC) {
                    hitIcon = HitIcon.MAGIC;
                } else if (type == CombatType.RANGED) {
                    hitIcon = HitIcon.RANGED;
                }
                player.animate(player.getStrategy().getAttackAnimation(player, player));
                player.writeFakeDamage(new Hit(maxHit, Hitsplat.CRITICAL, hitIcon));
                player.itemDelay.reset();
                break;
            }

            case 2873:
                player.dialogueFactory.sendStatement("Would you like to purchase a god cape for 25,000 coins?").sendOption("Yes", () -> {
                    if (!player.completedMageArena) {
                        player.send(new SendMessage("You must complete Mage's Arena minigame!"));
                        return;
                    }
                    if (player.inventory.contains(new Item(995, 25000))) {
                        player.inventory.remove(new Item(995, 25000));
                        player.inventory.addOrDrop(new Item(2412));
                        return;
                    }
                    player.send(new SendMessage("You do not have enough coins to do this!"));
                }, "No", () -> player.dialogueFactory.clear()).execute();
                return true;

            case 2874:
                player.dialogueFactory.sendStatement("Would you like to purchase a god cape for 25,000 coins?").sendOption("Yes", () -> {
                    if (!player.completedMageArena) {
                        player.send(new SendMessage("You must complete Mage's Arena minigame!"));
                        return;
                    }
                    if (player.inventory.contains(new Item(995, 25000))) {
                        player.inventory.remove(new Item(995, 25000));
                        player.inventory.addOrDrop(new Item(2414));
                        return;
                    }
                    player.send(new SendMessage("You do not have enough coins to do this!"));
                }, "No", () -> player.dialogueFactory.clear()).execute();
                return true;

            case 2875:
                player.dialogueFactory.sendStatement("Would you like to purchase a god cape for 25,000 coins?").sendOption("Yes", () -> {
                    if (!player.completedMageArena) {
                        player.send(new SendMessage("You must complete Mage's Arena minigame!"));
                        return;
                    }
                    if (player.inventory.contains(new Item(995, 25000))) {
                        player.inventory.remove(new Item(995, 25000));
                        player.inventory.addOrDrop(new Item(2413));
                        return;
                    }
                    player.send(new SendMessage("You do not have enough coins to do this!"));
                }, "No", () -> player.dialogueFactory.clear()).execute();
                return true;

                case 2878:
                player.move(new Position(2509, 4689));
                break;

                case 2879:
                player.move(new Position(2542, 4718));
                break;
            case 33354:
                TeleportHandler.open(player);
                break;
            case 10060:
            case 10061:
            case 30389:
                System.out.println("clicked!!!!!!!!!!!!!");
                player.bank.open();
                break;

                /*
                * Runecrafting teles
                 */
            case 29476:
                player.move(new Position(2281, 4837));
                System.out.println("clicked");
                break;
            case 29749:
                if (player.pvpInstance) {
                    player.message("You can not enter this while in a PvP instance");
                    return true;
                }

                //Skill Area:
                if (player.getY() == 3483) {
                    if (player.skills.getTotalLevel() < 800 && !PlayerRight.isDonator(player)) {
                        player.dialogueFactory.sendStatement("You need a total skill level of 800", "to enter this skilling area!");
                        player.dialogueFactory.execute();
                        return true;
                    }
                    player.walk(new Position(player.getX(), 3481), true);
                    player.face(Direction.NORTH);
                    return true;
                } else if (player.getY() == 3481) {
                    player.walk(new Position(player.getX(), 3483), true);
                    player.face(Direction.SOUTH);
                    return true;
                }

                if (player.pet != null) {
                    player.dialogueFactory.sendNpcChat(player.pet.id, "I'm sorry #name,", "but I can not enter this arena with you!").execute();
                    return true;
                }

                if (player.getCombat().isUnderAttack()) {
                    player.message("You can't do this until you are fully out of combat!");
                    return true;
                }

                /*if (Area.inEventArena(player)) {
                    player.damageImmunity.reset(3_000);
                }*/

                player.locking.lock(1);
                player.prayer.resetProtection();

                //Event Area:
                if (player.getX() == 3084) {
                    player.walk(new Position(3082, player.getY()), true);
                    player.face(Direction.WEST);
                } else if (player.getX() == 3082) {
                    player.walk(new Position(3084, player.getY()), true);
                    player.face(Direction.EAST);
                } else if (player.getY() == 3515) {
                    player.walk(new Position(player.getX(), 3513), true);
                    player.face(Direction.SOUTH);
                } else if (player.getY() == 3513) {
                    player.walk(new Position(player.getX(), 3515), true);
                    player.face(Direction.NORTH);
                } else if (player.getX() == 3073) {
                    player.walk(new Position(3075, player.getY()), true);
                    player.face(Direction.WEST);
                } else if (player.getX() == 3075) {
                    player.walk(new Position(3073, player.getY()), true);
                    player.face(Direction.EAST);
                } else if (player.getY() == 3504) {
                    player.walk(new Position(player.getX(), 3506), true);
                    player.face(Direction.SOUTH);
                } else if (player.getY() == 3506) {
                    player.walk(new Position(player.getX(), 3504), true);
                    player.face(Direction.NORTH);
                }
                break;

            case 26762: {
                Position end = null;
                player.face(new Position(object.getX(), object.getY()));
                player.animate(844);
                if (player.getPosition().equals(new Position(3233, 3938)) || player.getPosition().equals(new Position(3232, 3938))) {
                    end = new Position(3233, 10332);
                } else if (player.getPosition().equals(new Position(3233, 3950)) || player.getPosition().equals(new Position(3232, 3950))) {
                    end = new Position(3232, 10351);
                } else if (player.getPosition().equals(new Position(3242, 3948)) || player.getPosition().equals(new Position(3243, 3948))) {
                    end = new Position(3243, 10351);
                }
                Position finalEnd = end;
                if (finalEnd != null) {
                    player.move(finalEnd);
                    player.getCombat().reset();
                    player.getCombat().clearIncoming();
                }
                break;
            }
            case 26763: {
                Position end = null;
                player.face(new Position(object.getX(), object.getY()));
                player.animate(844);
                if (player.getPosition().equals(new Position(3233, 10332))) {
                    end = new Position(3233, 3938);
                } else if (player.getPosition().equals(new Position(3232, 10351))) {
                    end = new Position(3233, 3950);
                } else if (player.getPosition().equals(new Position(3243, 10351))) {
                    end = new Position(3242, 3948);
                }
                Position finalEnd = end;
                if (finalEnd != null) {
                    player.move(finalEnd);
                    player.getCombat().reset();
                    player.getCombat().clearIncoming();
                }
                break;
            }

            case 26760: {
                boolean isSuperDonator = PlayerRight.isSuper(player);

                if (!isSuperDonator && !player.inventory.contains(995, 5000)) {
                    player.dialogueFactory.sendStatement("You need 5,000 coins to do this!").execute();
                    return true;
                }

                Position destination = null;
                Direction direction = null;

                if (player.getY() < 3944) {
                    player.movement.walkTo(new Position(player.getX(), 3944));
                } else if (player.getY() > 3945) {
                    player.movement.walkTo(new Position(player.getX(), 3945));
                }

                if (player.getY() == 3945) {
                    direction = Direction.SOUTH;
                    destination = new Position(player.getX(), 3944);
                } else if (player.getY() == 3944) {
                    direction = Direction.NORTH;
                    destination = new Position(player.getX(), 3945);
                }

                if (direction != null) {
                    player.getCombat().reset();
                    player.face(direction);
                    player.locking.lock(1, LockType.MASTER_WITH_MOVEMENT);
                    if (!isSuperDonator)
                        player.inventory.remove(995, 5000);
                    player.movement.walkTo(destination);
                }
                break;
            }

            case 3192:
                player.interfaceManager.open(InterfaceConstants.DUEL_SCOREBOARD);
                break;

            case 6948:
                player.interfaceManager.open(InterfaceConstants.DEPOSIT_BOX);
                break;

            case 2631:
                if (!player.inventory.contains(1591)) {
                    player.dialogueFactory.sendStatement("Looks like I need a key to unlock this.", "Maybe if I kill that guard I can get the key.").execute();
                    break;
                }
                if (player.getPosition().matches(2931, 9690)) {
                    player.move(new Position(2931, 9689));
                } else if (player.getPosition().matches(2931, 9689)) {
                    player.move(new Position(2931, 9690));
                }
                break;



            case 2623: {
                boolean hasKey = player.inventory.contains(1590);
                if (player.getPosition().matches(2923, 9803)) {
                    player.action.execute(new DoorAction(player, object, new Position(2924, 9803), Direction.EAST, player1 -> hasKey, "You need a dusty key to unlock this door."));
                } else if (player.getPosition().matches(2924, 9803)) {
                    player.action.execute(new DoorAction(player, object, new Position(2923, 9803), Direction.WEST, player1 -> hasKey, "You need a dusty key to unlock this door."));
                }
                break;
            }

            case 1815://WILDERNESS ARDY LEVER
                player.action.execute(new LeverAction(player, object, new Position(2561, 3311, 0), Direction.WEST));
                break;

            case 1814://ARDY WILDERNESS LEVER
                player.action.execute(new LeverAction(player, object, new Position(3153, 3923, 0), Direction.WEST));
                break;

            case 5959://MAGE BANK LEVER
                player.action.execute(new LeverAction(player, object, new Position(2539, 4712, 0), Direction.WEST));
                break;

            case 9706://MAGE BANK LEVER
                player.action.execute(new LeverAction(player, object, new Position(3105, 3951, 0), Direction.WEST));
                break;

            case 9707://MAGE BANK LEVER
                player.action.execute(new LeverAction(player, object, new Position(3105, 3956, 0), Direction.NORTH));
                break;

            case 5960://MAGE BANK LEVER
                player.action.execute(new LeverAction(player, object, new Position(3090, 3956, 0), Direction.SOUTH));
                break;

            case 18987://KING BLACK DRAGON LADDER
                player.action.execute(new LadderAction(player, object, new Position(3069, 10257, 0)));
                break;

            case 18988://KING BLACK DRAGON LADDER
                player.action.execute(new LadderAction(player, object, new Position(3017, 3850, 0)));
                break;

            case 1816://KING BLACK DRAGON LEVER
                player.action.execute(new LeverAction(player, object, new Position(2271, 4680, 0), Direction.SOUTH));
                break;

            case 1817://KING BLACK DRAGON LEVER
                player.action.execute(new LeverAction(player, object, new Position(3067, 10253, 0), Direction.SOUTH));
                break;

            case 29635://OURNIA LADDER
                player.action.execute(new LadderAction(player, object, new Position(3015, 5629, 0)));
                break;

            case 29636://OURNIA LADDER
                player.action.execute(new LadderAction(player, object, new Position(2453, 3231, 0)));
                break;

            case 16537:// SLAYER CHAIN
                if(player.getHeight() == 0) {
                    player.action.execute(new LadderAction(player, object, new
                            Position(player.getX(), player.getY(), 1)));
                } else {
                    player.action.execute(new LadderAction(player, object, new
                            Position(player.getX(), player.getY(), 2)));
                } break;

            case 16538:// SLAYER CHAIN
                if (player.getHeight() == 1) {
                    player.action.execute(new LadderAction(player, object, new
                            Position(player.getX(), player.getY(), 0)));
                } else {
                    player.action.execute(new LadderAction(player, object, new
                            Position(player.getX(), player.getY(), 1)));
                } break;

            case 2111://SLAYER DOOR
                if (player.getY() <= object.getY()) {
                    player.action.execute(new DoorAction(player, object, new Position(3429, 3536, 0), Direction.NORTH));
                } else {
                    player.action.execute(new DoorAction(player, object, new Position(3429, 3535, 0), Direction.SOUTH));
                }
                break;


            case 2108://SLAYER DOOR
                if (player.getY() <= object.getY()) {
                    player.action.execute(new DoorAction(player, object, new Position(3428, 3536, 0), Direction.NORTH));
                } else {
                    player.action.execute(new DoorAction(player, object, new Position(3428, 3535, 0), Direction.SOUTH));
                }
                break;

            case 2102://SLAYER DOOR
                if (player.getY() <= object.getY()) {
                    player.action.execute(new DoorAction(player, object, new Position(3426, 3556, 1), Direction.NORTH));
                } else {
                    player.action.execute(new DoorAction(player, object, new Position(3426, 3555, 1), Direction.SOUTH));
                }
                break;

            case 2104://SLAYER DOOR
                if (player.getY() <= object.getY()) {
                    player.action.execute(new DoorAction(player, object, new Position(3427, 3556, 1), Direction.NORTH));
                } else {
                    player.action.execute(new DoorAction(player, object, new Position(3427, 3555, 1), Direction.SOUTH));
                }
                break;

            case 2100://SLAYER DOOR
                if (player.getY() <= object.getY()) {
                    player.action.execute(new DoorAction(player, object, new Position(3445, 3555, 2), Direction.NORTH));
                } else {
                    player.action.execute(new DoorAction(player, object, new Position(3445, 3554, 2), Direction.SOUTH));
                }
                break;

            case 21738:
                if (player.skills.getLevel(Skill.AGILITY) < 12) {
                    player.send(new SendMessage("You need level 12 agility or more to use this."));
                    break;
                }

                if (player.getPosition().getX() == 2649 && player.getPosition().getY() == 9562) {
                    World.schedule(new SteppingStoneTask(player, object) {
                        @Override
                        public void onExecute() {
                            if (tick == 0) {
                                player.face(object.getPosition());
                            } else if (tick == 1) {
                                player.forceMove(0, 1, 741, 15, 30, new Position(0, -1), Direction.SOUTH);
                            } else if (tick == 3) {
                                player.forceMove(0, 1, 741, 15, 30, new Position(0, -1), Direction.SOUTH);
                            } else if (tick == 5) {
                                player.forceMove(0, 1, 741, 15, 30, new Position(-1, 0), Direction.WEST);
                            } else if (tick == 7) {
                                player.forceMove(0, 1, 741, 15, 30, new Position(-1, 0), Direction.WEST);
                            } else if (tick == 9) {
                                player.forceMove(0, 1, 741, 15, 30, new Position(0, -1), Direction.SOUTH);
                            } else if (tick == 11) {
                                player.forceMove(0, 1, 741, 15, 30, new Position(0, -1), Direction.SOUTH);
                            } else if (tick == 13) {
                                player.forceMove(0, 1, 741, 15, 30, new Position(0, -1), Direction.SOUTH);
                            } else if (tick >= 15) {
                                cancel();
                            }
                        }
                    });
                }

                break;

            // brimhaven log balance
            case 20882:
                if (player.skills.getLevel(Skill.AGILITY) < 30) {
                    player.send(new SendMessage("You need at least level 30 agility to use this."));
                    break;
                }
                break;
            // brimhaven go upstairs
            case 21725:
                player.move(new Position(2636, 9510, 2));
                break;

            // brimhaven go downstairs
            case 21726:
                player.move(new Position(2636, 9517));
                break;

            case 21739:
                if (player.skills.getLevel(Skill.AGILITY) < 12) {
                    player.send(new SendMessage("You need level 12 agility or more to use this."));
                    break;
                }

                if (player.getPosition().getX() == 2647 && player.getPosition().getY() == 9557) {
                    World.schedule(new SteppingStoneTask(player, object) {
                        @Override
                        public void onExecute() {
                            if (tick == 0) {
                                player.face(object.getPosition());
                            } else if (tick == 1) {
                                player.forceMove(0, 1, 741, 15, 30, new Position(0, 1), Direction.NORTH);
                            } else if (tick == 3) {
                                player.forceMove(0, 1, 741, 15, 30, new Position(0, 1), Direction.NORTH);
                            } else if (tick == 5) {
                                player.forceMove(0, 1, 741, 15, 30, new Position(0, 1), Direction.NORTH);
                            } else if (tick == 7) {
                                player.forceMove(0, 1, 741, 15, 30, new Position(1, 0), Direction.EAST);
                            } else if (tick == 9) {
                                player.forceMove(0, 1, 741, 15, 30, new Position(1, 0), Direction.EAST);
                            } else if (tick == 11) {
                                player.forceMove(0, 1, 741, 15, 30, new Position(0, 1), Direction.NORTH);
                            } else if (tick == 13) {
                                player.forceMove(0, 1, 741, 15, 30, new Position(0, 1), Direction.NORTH);
                            } else if (tick >= 15) {
                                cancel();
                            }
                        }
                    });
                }
                break;

            case 29993: // fremmy slayer duneon steps
                if (player.getPosition().matches(2703, 9989)) {
                    player.move(new Position(2703, 9991));
                } else if (player.getPosition().matches(2703, 9991)) {
                    player.move(new Position(2703, 9989));
                }
                break;

            case 30198:
                if (player.getPosition().getX() == 2684 && player.getPosition().getY() == 9436) {
                    player.move(new Position(2697, 9436));
                } else if (player.getPosition().getX() == 2697 && player.getPosition().getY() == 9436) {
                    player.move(new Position(2684, 9436));
                }
                break;

            case 21731:
            case 21732:
            case 21733:
            case 21734:
            case 21735:
                World.schedule(new ChopVineTask(player, object, 5));
                break;

            /* Slayer staircase */
            case 2114:
                player.move(new Position(player.getX(), player.getY(), 1));
                player.send(new SendMessage("You climb up the staircase."));
                break;
            case 2118:
                player.move(new Position(3438, 3538, 0));
                player.send(new SendMessage("You climb down the staircase."));
                break;
            case 2120:
                player.move(new Position(3412, 3540, 1));
                player.send(new SendMessage("You climb down the staircase."));
                break;
            case 2119:
                player.move(new Position(3417, 3540, 2));
                player.send(new SendMessage("You climb up the staircase."));
                break;
            case 537: //Kraken Entrance
                if(player.slayer.getTask() == SlayerTask.KRAKEN) {
                    player.move(new Position(2280, 10022, 0));
                    KrakenActivity.create(player);
                } else {
                    player.send(new SendMessage("You need a slayer task to enter this cave."));
                    break;
                }
            case 10068: //Zulrah Entrance
                    player.move(new Position(2268, 3069, 0));
                    World.schedule(8, () -> ZulrahActivity.create(player));
                    break;
            case 23104: //Cerberus Entrance
                if(player.slayer.getTask() == SlayerTask.CERBERUS || player.slayer.getTask() == SlayerTask.HELLHOUNDS) {
                    player.move(new Position(1240, 1243, 0));
                    World.schedule(8, () -> CerberusActivity.create(player));
                } else {
                    player.send(new SendMessage("You need a slayer task to enter this cave."));
                    break;
                }
            /* Webs */
            case 733: {
                Item weapon = player.equipment.getWeapon();

                if (weapon == null) {
                    player.message("You need a weapon to cut this web!");
                    return true;
                }
                player.animate(new Animation(390, UpdatePriority.HIGH));
                if (RandomUtils.success(1 / 3.0)) {
                    break;
                }
                World.schedule(new ObjectReplacementEvent(object, 50));
                player.send(new SendMessage("You have cut the web apart!"));
                break;
            }

            case 1:
                object.transform(11833);
                break;
            case 18989:
                player.face(new Position(object.getX(), object.getY()));
                player.animate(828);
                player.move(new Position(3018, 10248));
                break;
            case 18990:
                player.face(new Position(object.getX(), object.getY()));
                player.animate(828);
                player.move(new Position(3069, 3855));
                break;

            /* Pest control */
            case 14314:
                player.move(new Position(2657, 2639));
                break;
            case 14315:
//                player.message("Pest control currently disabled");
                PestControlLobby.joinBoat(player);
                break;

            /* Warrior Guild */
            case 24318:
                int attack_strength = (player.skills.getMaxLevel(Skill.ATTACK) + player.skills.getMaxLevel(Skill.STRENGTH));
                if (player.getPosition().equals(new Position(2876, 3546, 0))) {
                    player.move(new Position(2877, 3546));
                    Activity.forActivity(player, it -> it.onRegionChange(player));
                    player.face(Direction.EAST);
                    break;
                }
                if (attack_strength >= 130) {
                    player.move(new Position(2876, 3546));
                    WarriorGuild.create(player);
                    player.face(Direction.WEST);
                    break;
                }
                player.dialogueFactory.sendStatement("You need a combined level of 130 in both your attack", "and strength level to enter the Warrior's guild.").execute();
                break;

            /* Fight cave */
            case 11833:
                player.locking.lock();
                player.send(new SendMessage("Entering the fight caves..."));
                World.schedule(3, () -> {
                    FightCaves.create(player);
                    player.locking.unlock();
                });
                break;

            /* Clan Bank */
            case 11338:
                player.dialogueFactory.sendStatement("Clan bank is currently not accessible!", "This is intended for future content!").execute();
                break;

            /* Well */
            case 884:
                player.dialogueFactory.sendDialogue(new WellOfGoodwillDialogue());
                break;

            /* Player owned shops. */
            case 26043:
                player.message("Personal stores are currently disabled!");
//                if (PlayerRight.isIronman(player)) {
//                    player.send(new SendMessage("As an iron man you may not access player owned stores!"));
//                    return true;
//                }
//                PersonalStore.openMenu(player);
                break;

            /* Management machine */
            case 11546:
                if (PlayerRight.isModerator(player)) {
                }
                break;
            case 30920:
            case 30924:
                player.dialogueFactory.sendOption("Verdant Valley", () -> {
                    Teleportation.teleport(player, new Position(3760, 3758, 0));
                        }, "Mushroom Forest", () -> {
                    Teleportation.teleport(player, new Position(3676, 3871, 0));
                        }, "Tar Swamp", () -> {
                    Teleportation.teleport(player, new Position(3676, 3871, 0));
                        }, "Nevermind", () -> { player.dialogueFactory.clear();
                        }).execute();
                break;
           /* Altar of the occult */
            case 575:
                player.dialogueFactory.sendOption("Modern", () -> {
                    Autocast.reset(player);
                    player.animate(new Animation(645));
                    player.spellbook = Spellbook.MODERN;
                    player.interfaceManager.setSidebar(Config.MAGIC_TAB, player.spellbook.getInterfaceId());
                    player.send(new SendMessage("You are now using the " + player.spellbook.name().toLowerCase() + " spellbook."));
                }, "Ancient", () -> {
                    Autocast.reset(player);
                    player.animate(new Animation(645));
                    player.spellbook = Spellbook.ANCIENT;
                    player.interfaceManager.setSidebar(Config.MAGIC_TAB, player.spellbook.getInterfaceId());
                    player.send(new SendMessage("You are now using the " + player.spellbook.name().toLowerCase() + " spellbook."));
                }, "Lunar", () -> {
                    Autocast.reset(player);
                    player.animate(new Animation(645));
                    player.spellbook = Spellbook.LUNAR;
                    player.interfaceManager.setSidebar(Config.MAGIC_TAB, player.spellbook.getInterfaceId());
                    player.send(new SendMessage("You are now using the " + player.spellbook.name().toLowerCase() + " spellbook."));
                }).execute();
                break;

            /* Ornate Restoration Pool */
            case 29241:
                if (player.getCombat().inCombat()) {
                    player.message("You can't do this whilst in combat!");
                    return true;
                }

                if (player.hasPvPTimer) {
                    player.message("You can't do this with an active PvP timer!");
                    return true;
                }

                int duration = PlayerRight.isDonator(player) ? 1 : 2;
                if (!player.restoreDelay.elapsed(duration, TimeUnit.MINUTES)) {
                    player.send(new SendMessage("You can only do this once every " + duration + " minutes! Time Passed: " + Utility.getTime(player.restoreDelay.elapsedTime())));
                    return true;
                }
                player.animate(7305);
                player.unpoison();
                player.unvenom();
                player.playerAssistant.resetEffects();
                player.playerAssistant.setWidget();
                player.runEnergy = 100;
                player.teleblockTimer.set(0);
                player.send(new SendRunEnergy());
                player.skills.restoreAll();
                CombatSpecial.restore(player, 100);
                player.send(new SendMessage("Your health & special attack have been restored!"));
                player.restoreDelay.reset();
            break;

            /* Donator deposit. */
            case 26254:
                player.donatorDeposit.open();
                break;

            /* Construction. */
            case 15478:
                player.dialogueFactory.sendOption("Enter your house", () -> {
                    player.dialogueFactory.onAction(player.house::enter);
                }, "Enter friend's house", () -> {
                    player.dialogueFactory.onAction(() -> {
                        player.send(new SendInputMessage("Enter your friend's name", 10, player.house::enter));
                    });
                }, "Enter clan house", () -> {

                }, "Nevermind", player.interfaceManager::close).execute();
                break;
            case 4525:
                player.house.leave();
                break;

            /* Dwarf cannon. */
            case 6:
                CannonManager.load(player);
                break;

            /* Wilderness ditch. */
            case 23271: {
                ObjectDirection oDir = object.getDirection();
                Direction direction = (oDir == ObjectDirection.SOUTH || oDir == ObjectDirection.NORTH)
                        ? (player.getX() < 2996 ? Direction.EAST : Direction.WEST)
                        : (player.getY() < 3522 ? Direction.NORTH : Direction.SOUTH);
                Position offSet = new Position(
                        direction.getDirectionX() * 3,
                        direction.getDirectionY() * 3);
                player.forceMove(3, 6132, 33, 60, offSet, direction);
                break;
            }

            /* Skeletal wyvern. */
            case 10596:
                if (player.getY() == 9562) {
                    player.move(new Position(player.getX(), 9555));
                } else if (player.getY() == 9555) {
                    player.move(new Position(player.getX(), 9562));
                }
                break;

                /* Dagganoths */
            case 10229:
                player.action.execute(new LadderAction(player, object, new Position(1912, 4367, 0)));
                break;

            case 3831:
                player.action.execute(new LadderAction(player, object, new Position(2900, 4449, 0)));
                break;

            case 677:
                player.move(new Position(player.getX() < 2974 ? 2974 : 2970, player.getY(), player.getHeight()));
                break;

            case 535:
                player.move(new Position(2376, player.getY(), player.getHeight()));
                break;

            case 536:
                player.move(new Position(2379, player.getY(), player.getHeight()));
                break;

            case 26366:
            case 26365:
            case 26364:
            case 26363:

                int length = PlayerRight.isDonator(player) ? 5 : 10;
                if (!player.godwarsDelay.elapsed(length, TimeUnit.MINUTES)) {
                    player.dialogueFactory.sendStatement("You can only do this once every " + length + " minutes!", "Time Passed: " + Utility.getTime(player.godwarsDelay.elapsedTime())).execute();
                    return true;
                }

                if (player.skills.getLevel(Skill.PRAYER) < player.skills.getMaxLevel(Skill.PRAYER)) {
                    player.animate(new Animation(645));
                    player.skills.setLevel(Skill.PRAYER, player.skills.getMaxLevel(Skill.PRAYER));
                    player.send(new SendMessage("You recharge your prayer points."));
                    player.godwarsDelay.reset();
                } else {
                    player.send(new SendMessage("Your prayer is already full."));
                }
                break;

            /* Prayer altar. */
            case 409:
            case 411:
            case 7812:
                if (player.skills.getLevel(Skill.PRAYER) < player.skills.getMaxLevel(Skill.PRAYER)) {
                    player.animate(new Animation(645));
                    player.skills.setLevel(Skill.PRAYER, player.skills.getMaxLevel(Skill.PRAYER));
                    player.send(new SendMessage("You recharge your prayer points."));
                } else {
                    player.send(new SendMessage("Your prayer is already full."));
                }
                break;

            /* Ancient altar. */
            case 6552: {
                player.animate(new Animation(645));
                Spellbook book = player.spellbook == Spellbook.MODERN || player.spellbook == Spellbook.LUNAR ? Spellbook.ANCIENT : Spellbook.MODERN;
                Autocast.reset(player);
                player.spellbook = book;
                player.interfaceManager.setSidebar(Config.MAGIC_TAB, book.getInterfaceId());
                player.send(new SendMessage("You are now using the " + book.name().toLowerCase() + " spellbook."));
            }
            break;

            /* Lunar altar. */
            case 410: {
                player.animate(new Animation(645));
                Spellbook book = player.spellbook == Spellbook.MODERN || player.spellbook == Spellbook.ANCIENT ? Spellbook.LUNAR : Spellbook.MODERN;
                Autocast.reset(player);
                player.spellbook = book;
                player.interfaceManager.setSidebar(Config.MAGIC_TAB, book.getInterfaceId());
                player.send(new SendMessage("You are now using the " + book.name().toLowerCase() + " spellbook."));
            }
            break;

            /* Grand exchange. */
            case 26044:
                PersonalStore.openPanel(player);
                break;

            case 24452:
                if (!PlayerRight.isModerator(player)) {
                    player.dialogueFactory.sendStatement("Only privileged staff members of Tarnish can access this!").execute();
                    return true;
                }

                player.dialogueFactory.sendStatement("Please enter the name of the player you would to manage", "They must be online for this to work!").onAction(() -> {
                    player.dialogueFactory.clear();
                    player.send(new SendInputMessage("Enter the name of the player you would like to manage", 15, input -> {
                        Optional<Player> other = World.search(input);
                        if (!other.isPresent()) {
                            player.dialogueFactory.sendStatement("The player you searched for either does not exist or", "is not online!").execute();
                            return;
                        }
                        RottenTomatoePlugin.open(player, other.get());
                    }));
                }).execute();

                return true;

            default:
                return false;

        }
        return true;
    }

}
