package plugin.command;


import com.osroyale.Config;
import com.osroyale.content.DropDisplay;
import com.osroyale.content.Waypoints;
import com.osroyale.content.skill.impl.magic.teleport.Teleportation;
import com.osroyale.content.triviabot.TriviaBot;
import com.osroyale.game.plugin.extension.CommandExtension;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.entity.mob.player.command.Command;
import com.osroyale.game.world.entity.mob.player.command.CommandParser;
import com.osroyale.game.world.position.Area;
import com.osroyale.net.packet.out.*;
import com.osroyale.util.Utility;
import com.osroyale.util.tools.LogPrinter;
import com.osroyale.util.tools.LogReader;

import java.util.*;

public class PlayerCommandPlugin extends CommandExtension {

    @Override
    protected void register() {
        commands.add(new Command("proxy") {
            @Override
            public void execute(Player player, CommandParser parser) {
                player.send(new SendMessage("Your host is \"" + player.getSession().get().getHost() + "\""));
            }
        });
        commands.add(new Command("commands", "command") {
            @Override
            public void execute(Player player, CommandParser parser) {
                player.send(new SendString("<str>Commands List</str>", 37103));
                player.send(new SendString("", 37107));
                for (int i = 0; i < 50; i++) {
                    player.send(new SendString("", i + 37111));
                }
                final Set<String> set = new HashSet<>();
                int count = 0;
                for (CommandExtension extension : extensions) {
                    if (!extension.canAccess(player)) {
                        continue;
                    }
                    final String clazzName = extension.getClass().getSimpleName().replace("CommandPlugin", "");
                    player.send(new SendString(clazzName + " Commands", count + 37111));
                    count++;
                    for (Map.Entry<String, Command> entry : extension.multimap.entries()) {
                        if (count >= 100) {
                            break;
                        }
                        if (set.contains(entry.getKey())) {
                            continue;
                        }
                        final Command command = entry.getValue();
                        final StringBuilder builder = new StringBuilder();
                        for (int i = 0; i < command.getNames().length; i++) {
                            String name = command.getNames()[i];
                            builder.append("::");
                            builder.append(name);
                            if (i < command.getNames().length - 1) {
                                builder.append(", ");
                            }
                        }
                        player.send(new SendString(builder.toString(), count + 37111));
                        set.addAll(Arrays.asList(command.getNames()));
                        count++;
                    }
                }
                player.send(new SendScrollbar(37100, count * 22));
                player.interfaceManager.open(37100);

            }
        });

        commands.add(new Command("setwp") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (parser.hasNext()) {
                    final String type = parser.nextString().toLowerCase();
                    Waypoints.setWaypoint(player, type);
                }
            }
        });
        commands.add(new Command("wp") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (parser.hasNext()) {
                    final String type = parser.nextString().toLowerCase();
                    if(!player.waypoints.containsKey(type)) {
                        player.send(new SendMessage("You do not have a waypoint by this name."));
                        return;
                    }
                        Teleportation.teleport(player, player.waypoints.get(type));
                        player.send(new SendMessage("You have teleported to waypoint @red@" + type + "."));

                }
            }
        });
        commands.add(new Command("listwp") {
            @Override
            public void execute(Player player, CommandParser parser) {
                Waypoints.getWaypoints(player);
            }
        });

        commands.add(new Command("removewp") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (parser.hasNext()) {
                    final String type = parser.nextString().toLowerCase();
                    if(!player.waypoints.containsKey(type)) {
                        player.dialogueFactory.sendStatement("You don't have a waypoint by the name of " +type).execute();
                        return;
                    }
                  Waypoints.removeWaypoint(player, type);
                }
            }
        });

        commands.add(new Command("referral") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (!parser.hasNext()) {
                    player.send(new SendMessage("Please enter command as ::referral toplist"));
                    return;
                }

                StringBuilder toplist = new StringBuilder(parser.nextString());
                if (LogReader.alreadyClaimedReferral(player)) {
                    player.send(new SendMessage("You have already claimed a referral"));
                } else {
                    LogPrinter.printReferralLog(player);
                    player.inventory.add(995, 10000000);
                    player.send(new SendMessage("You have claimed your referral! Welcome to Tarnish!"));
                    LogPrinter.printToplist(player, toplist.toString());
                }
            }
        });
        commands.add(new Command("ref") {
            @Override
            public void execute(Player player, CommandParser parser) {
                StringBuilder name = new StringBuilder(parser.nextString());
                if (LogReader.alreadyClaimedReferral(player)) {
                    player.send(new SendMessage("You have already claimed a referral"));
                } else {
                    LogPrinter.printReferralLog(player);
                    player.inventory.add(995, 10000000);
                    player.send(new SendMessage("You have claimed your referral! Welcome to Tarnish!"));
                   // LogPrinter.printToplist(player, String.valueOf(name));
                }
            }
        });

        commands.add(new Command("vote") {
            @Override
            public void execute(Player player, CommandParser parser) {
                player.send(new SendURL("https://tarnishps.everythingrs.com/services/store"));
                player.message("Type ::voted to claim your reward!");
            }
        });
        commands.add(new Command("networth", "nw") {
            @Override
            public void execute(Player player, CommandParser parser) {
                long netWorthValue = player.playerAssistant.networth();
                String networthFormat = Utility.formatDigits(netWorthValue);

                if (netWorthValue >= Integer.MAX_VALUE) {
                    player.speak("My current networth is too large to calculate.");
                } else {
                    player.speak("My current networth is: " + networthFormat);
                }
            }
        });
        commands.add(new Command("hiscores") {
            @Override
            public void execute(Player player, CommandParser parser) {
                player.send(new SendURL("https://tarnishps.everythingrs.com/services/hiscores"));
                player.message("");
            }
        });

        commands.add(new Command("donate", "store", "webstore") {
            @Override
            public void execute(Player player, CommandParser parser) {
                player.send(new SendURL("https://tarnishps.everythingrs.com/services/store"));
                player.message("Type ::donated or ::claim to claim your reward!");
            }
        });
     commands.add(new Command("voted", "claimvote", "claimvotes") {
                         @Override
                         public void execute(Player player, CommandParser command) {
                             player.send(new SendMessage("Temporarily disabled until EverythingRS has been replaced."));
                             /*Vote.service.execute(new Runnable() {
                                 @Override
                                 public void run() {
                                     try {

                                         for (int i = 1; i < 3; i++) {
                                             Vote[] reward = Vote.reward(
                                                     "V48tgd7OxwrvPMmZZbjkVt6qTYB2MrZ5sw6PD4DBJnM83zepnUlIT8DiFoND3BYEQjqxPamH",
                                                     player.getUsername(), String.valueOf(i), String.valueOf(Byte.MAX_VALUE));

                                             if (reward[0].message != null) {
                                                 player.send(new SendMessage(reward[0].message));
                                                 return;
                                             }
                                             player.inventory.add(new Item(reward[0].reward_id, reward[0].give_amount));
                                             AchievementHandler.activate(player, AchievementKey.VOTE);
                                             player.send(new SendMessage("Thank you for supporting Tarnish!"));
                                         }
                                     } catch (Exception e) {
                                         player.send(new SendMessage("Api Services are currently offline. Please check back shortly"));
                                         e.printStackTrace();
                                     }
                                 }

                             });*/

                         }

                     });
        commands.add(new Command("donated", "claim") {
            @Override
            public void execute(Player player, CommandParser parser) {
                player.send(new SendMessage("Temporarily disabled until EverythingRS has been replaced."));
                /*new Thread(() -> {
                    try {
                        Donation[] donations = Donation.donations(
                                "V48tgd7OxwrvPMmZZbjkVt6qTYB2MrZ5sw6PD4DBJnM83zepnUlIT8DiFoND3BYEQjqxPamH", player.getName());
                        if (donations.length == 0) {
                            player.send(new SendMessage("You currently don't have any items waiting. You must donate first!"));
                            return;
                        }
                        if (donations[0].message != null) {
                            player.send(new SendMessage(donations[0].message));
                            return;
                        }
                        for (Donation donate : donations) {
                            player.inventory.add(new Item(donate.product_id, donate.product_amount));
                        }
                        player.send(new SendMessage("Thank you for donating!"));
                    } catch (Exception e) {
                        player.send(new SendMessage("Api Services are currently offline. Please check back shortly"));
                        e.printStackTrace();
                    }
                }).start();*/
            }
        });


        commands.add(new Command("staff", "staffonline", "staffon") {
            @Override
            public void execute(Player player, CommandParser parser) {
                List<Player> staffs = World.getStaff();
                int length = staffs.size() < 25 ? 25 : staffs.size();

                player.send(new SendString("", 37113));
                player.send(new SendString("", 37107));
                player.send(new SendString("Tarnish Online Staff", 37103));
                player.send(new SendScrollbar(37110, length * 20));

                for (int index = 0, string = 37111; index < length; index++, string++) {
                    if (index < staffs.size()) {
                        Player staff = staffs.get(index);
                        player.send(new SendString(PlayerRight.getCrown(staff) + " " + staff.getName() + "    (<col=255>" + staff.right.getName() + "</col>)", string));
                    } else {
                        player.send(new SendString("", string));
                    }
                }

                player.send(new SendItemOnInterface(37199));
                player.interfaceManager.open(37100);
            }
        });

        commands.add(new Command("home") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (player.getCombat().isUnderAttackByPlayer()) {
                    player.message("You can not teleport whilst in combat!");
                    return;
                }

                Teleportation.teleport(player, Config.DEFAULT_POSITION);
            }
        });

        commands.add(new Command("kdr") {
            @Override
            public void execute(Player player, CommandParser parser) {
                player.speak("My Kill Death Ratio: " + player.playerAssistant.kdr());
            }
        });

        commands.add(new Command("players") {
                         @Override
                         public void execute(Player player, CommandParser parser) {
                             List<Player> staffs = new ArrayList<>();
                             for (Player other : World.getPlayers()) {
                                 staffs.add(other);
                             }
                             int length = staffs.size() < 25 ? 25 : staffs.size();
                             player.send(new SendString("", 37113));
                             player.send(new SendString("", 37107));
                             player.send(new SendString("Tarnish Online Players", 37103));
                             player.send(new SendScrollbar(37110, length * 20));
                             for (int index = 0, string = 37111; index < length; index++, string++) {
                                 if (index < staffs.size()) {
                                     Player staff = staffs.get(index);
                                     player.send(new SendString(PlayerRight.getCrown(staff) + " " + staff.getName() + "    (<col=255>" + staff.right.getName() + "</col>)", string));
                                 } else {
                                     player.send(new SendString("", string));
                                 }
                             }
                             player.send(new SendItemOnInterface(37199));
                             player.interfaceManager.open(37100);
                         }
                     });
        commands.add(new Command("drops") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (Area.inWilderness(player) || player.getCombat().inCombat() || Area.inDuelArena(player) || player.playerAssistant.busy()) {
                    player.message("You can not check drops at this current moment.");
                    return;
                }

                if (player.pvpInstance && Area.inPvP(player)) {
                    player.message("You must be in a safe zone to do this!");
                    return;
                }
                DropDisplay.open(player);
            }
        });

        commands.add(new Command("empty", "emptyinventory", "clearinventory") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (Area.inWilderness(player) || player.getCombat().inCombat() || Area.inDuelArena(player) || player.playerAssistant.busy()) {
                    player.message("You can not clear your inventory at this current moment.");
                    return;
                }

                if (player.pvpInstance && Area.inPvP(player)) {
                    player.message("You must be in a safe zone to do this!");
                    return;
                }


                if (player.inventory.isEmpty()) {
                    player.message("You have nothing to empty!");
                    return;
                }

                String networth = Utility.formatDigits(player.playerAssistant.networth(player.inventory));
                player.dialogueFactory.sendStatement("Are you sure you want to clear your inventory? ",
                        "Container worth: <col=255>" + networth + " </col>coins.");
                player.dialogueFactory.sendOption("Yes", () -> {
                    player.inventory.clear(true);
                    player.dialogueFactory.clear();
                }, "Nevermind", () -> player.dialogueFactory.clear());
                player.dialogueFactory.execute();
            }
        });

        commands.add(new Command("ans", "answer", "answers") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (parser.hasNext()) {
                    StringBuilder answer = new StringBuilder();
                    while (parser.hasNext()) {
                        answer.append(parser.nextString()).append(" ");
                    }
                    TriviaBot.answer(player, answer.toString().trim());
                }
            }
        });
    }

    @Override
    public boolean canAccess(Player player) {
        return true;
    }

}
