package plugin.command;

import com.osroyale.Config;
import com.osroyale.content.activity.randomevent.impl.MimeEvent;
import com.osroyale.content.bloodmoney.BloodMoneyChest;
import com.osroyale.content.dialogue.DialogueFactory;
import com.osroyale.content.tittle.PlayerTitle;
import com.osroyale.game.Graphic;
import com.osroyale.game.plugin.extension.CommandExtension;
import com.osroyale.game.task.Task;
import com.osroyale.game.world.Interactable;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.mob.UpdateFlag;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.npc.definition.NpcDefinition;
import com.osroyale.game.world.entity.mob.player.*;
import com.osroyale.game.world.entity.mob.player.command.Command;
import com.osroyale.game.world.entity.mob.player.command.CommandParser;
import com.osroyale.game.world.entity.mob.player.profile.ProfileRepository;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.position.Area;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.packet.out.SendInputAmount;
import com.osroyale.net.packet.out.SendInputMessage;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.net.packet.out.SendString;
import com.osroyale.util.StringUtils;
import com.osroyale.util.Utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OwnerCommandPlugin extends CommandExtension {
    @Override
    protected void register() {
        commands.add(new Command("unipmute") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (parser.hasNext()) {
                    final String name = parser.nextLine();
                    if (World.search(name).isPresent()) {
                        final Player target = World.search(name).get();
                        if (player == target) {
                            return;
                        }

                        if (IPMutedPlayers.unIpMute(target.lastHost)) {
                            player.send(new SendMessage("<col=ff0000>" + target.getUsername() + " has been un-ipmuted."));
                        }
                    } else {
                        player.send(new SendMessage("The player '" + name + "' either doesn't exist, or is offline."));
                    }
                }
            }
        });

        commands.add(new Command("ipmute") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (parser.hasNext()) {
                    final String name = parser.nextLine();
                    if (World.search(name).isPresent()) {
                        final Player target = World.search(name).get();
                        if (player == target) {
                            player.send(new SendMessage("You cannot ip-mute yourself."));
                            return;
                        }

                        for (Player p : World.getPlayers()) {
                            if (p == null) {
                                continue;
                            }

                            if (p.lastHost.equalsIgnoreCase(target.lastHost)) {
                                player.send(new SendMessage("<col=ff0000>" + p.getUsername() + " has been ip-muted."));
                            }
                        }

                        IPMutedPlayers.ipMute(target.lastHost);
                    } else {
                        player.send(new SendMessage("The player '" + name + "' either doesn't exist, or is offline."));
                    }
                }
            }
        });

        commands.add(new Command("ipban") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (parser.hasNext()) {
                    final String name = parser.nextLine();
                    if (World.search(name).isPresent()) {
                        final Player target = World.search(name).get();
                        if (player == target) {
                            player.send(new SendMessage("You cannot ip-ban yourself."));
                            return;
                        }

                        for (Player p : World.getPlayers()) {
                            if (p == null) {
                                continue;
                            }

                            if (p.lastHost.equalsIgnoreCase(target.lastHost)) {
                                player.send(new SendMessage("<col=ff0000>" + p.getUsername() + " has been ip-banned."));
                                p.logout(true);
                            }
                        }

                        IPBannedPlayers.ipBan(target.lastHost);
                    } else {
                        player.send(new SendMessage("The player '" + name + "' either doesn't exist, or is offline."));
                    }
                }
            }
        });

        commands.add(new Command("ban") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (parser.hasNext()) {
                    final String name = parser.nextLine();
                    if (World.search(name).isPresent()) {
                        final Player target = World.search(name).get();
                        if (player == target) {
                            player.send(new SendMessage("You cannot ban yourself."));
                            return;
                        }

                        final String username = StringUtils.capitalize(target.getUsername());
                        player.send(new SendMessage("<col=ff0000>" + username + " has been banned."));
                        target.logout(true);
                        BannedPlayers.ban(username);
                    } else {
                        player.send(new SendMessage("The player '" + name + "' either doesn't exist, or is offline."));
                    }
                }
            }
        });

        commands.add(new Command("unban") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (parser.hasNext()) {
                    final String username = parser.nextLine();

                    if (BannedPlayers.unban(username)) {
                        player.send(new SendMessage("<col=ff0000>" + username + " has been un-banned."));
                    }
                }
            }
        });

        commands.add(new Command("checkaccs") {
            @Override
            public void execute(Player player, CommandParser parser) {
                    Player other = player.managing.get();
                    List<String> list = ProfileRepository.getRegistry(other.lastHost);

                    if (!list.isEmpty()) {
                        for (int index = 0; index < 50; index++) {
                            String name = index >= list.size() ? "" : list.get(index);
                            player.send(new SendString(name, 37111 + index));
                        }

                        player.message("<col=FF0D5D>There are " + list.size() + " accounts linked to " + Utility.formatName(other.getName()) + ".");
                        player.send(new SendString("Profiles:\\n" + list.size(), 37107));
                        player.send(new SendString(other.getName(), 37103));
                        player.interfaceManager.open(37100);
                    }
            }
        });

        commands.add(new Command("bombs") {
            @Override
            public void execute(Player player, CommandParser parser) {
                Position center = new Position(player.getX() - 2, player.getY() - 2, player.getHeight());
                Interactable target = Interactable.create(center, 5, 5);
                Position[] boundaries = Utility.getInnerBoundaries(target);
                List<Position> list = new ArrayList<>();
                Collections.addAll(list, boundaries);

                for (int index = 0; index < list.size(); index++) {
                    int finalIndex = index;
                    World.schedule(index, () -> World.sendGraphic(new Graphic(659), list.get(finalIndex), player.instance));
                }
            }
        });
        commands.add(new Command("bloodmoneychest") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (!BloodMoneyChest.active) {
                    BloodMoneyChest.spawn();
                    BloodMoneyChest.stopwatch.reset();
                }
            }
        });

        commands.add(new Command("resetplayer") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (parser.hasNext()) {
                    StringBuilder name = new StringBuilder(parser.nextString());

                    while (parser.hasNext()) {
                        name.append(" ").append(parser.nextString());
                    }

                    World.search(name.toString()).ifPresent(other -> {
                        DialogueFactory factory = player.dialogueFactory;
                        factory.sendOption("Skills", () -> {
                            factory.onAction(() -> player.send(new SendInputAmount("Enter the skill id", 1, input -> {
                                int skill = Integer.parseInt(input);
                                if (skill > -1 && skill < Skill.SKILL_COUNT) {
                                    other.skills.setMaxLevel(skill, skill == 3 ? 10 : 1);
                                    other.skills.setCombatLevel();
                                    other.updateFlags.add(UpdateFlag.APPEARANCE);
                                    player.message(other.getName() + "'s " + Skill.getName(skill) + " was reset");
                                }
                            })));
                        }, "Inventory", () -> {
                            other.inventory.clear();
                            player.message(other.getName() + "'s inventory was cleared");
                            factory.clear();
                        }, "Equipment", () -> {
                            other.equipment.clear();
                            player.message(other.getName() + "'s equipment was cleared");
                            factory.clear();
                        }, "Bank", () -> {
                            other.bank.clear();
                            player.message(other.getName() + "'s bank was cleared");
                            factory.clear();
                        });
                        player.dialogueFactory.execute();
                    });
                } else {
                    player.message("Invalid command use; ::resetplayer settings");
                }
            }
        });

        commands.add(new Command("doubleexp") {
            @Override
            public void execute(Player player, CommandParser parser) {
                Config.DOUBLE_EXPERIENCE = !Config.DOUBLE_EXPERIENCE;
                World.sendMessage("<col=CF2192>Tarnish: </col>Double experience is now " + (Config.DOUBLE_EXPERIENCE ? "activated" : "de-activated") + ".");
            }
        });

        commands.add(new Command("wildplayers") {
            @Override
            public void execute(Player player, CommandParser parser) {
                for (Player other : World.getPlayers()) {
                    if (other != null && Area.inWilderness(other)) {
                        int level = other.wilderness;
                        player.message("<col=255>" + other.getName() + " (level " + other.skills.getCombatLevel() + ") is in wilderness level " + level + ".");
                    }
                }
            }
        });

        commands.add(new Command("settitle") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (parser.hasNext()) {
                    StringBuilder name = new StringBuilder(parser.nextString());

                    while (parser.hasNext()) {
                        name.append(" ").append(parser.nextString());
                    }

                    World.search(name.toString()).ifPresent(other -> {
                        player.send(new SendInputMessage("Enter the title", 15, input -> {
                            other.playerTitle = PlayerTitle.create(input, other.playerTitle.getColor());
                        }));
                    });

                } else {
                    player.message("Invalid command use; ::title settings");
                }
            }
        });

        commands.add(new Command("setpt", "setplaytime") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (parser.hasNext()) {
                    StringBuilder name = new StringBuilder(parser.nextString());

                    while (parser.hasNext()) {
                        name.append(" ").append(parser.nextString());
                    }

                    World.search(name.toString()).ifPresent(other -> {
                        player.send(new SendInputAmount("Enter the play time", 8, input -> {
                            other.playTime = Integer.parseInt(input);
                        }));
                    });

                } else {
                    player.message("Invalid command use; ::setpt settings");
                }
            }
        });

        commands.add(new Command("giveitem", "gi") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (parser.hasNext()) {
                    StringBuilder name = new StringBuilder(parser.nextString());

                    while (parser.hasNext()) {
                        name.append(" ").append(parser.nextString());
                    }

                    World.search(name.toString()).ifPresent(other -> {
                        player.send(new SendInputAmount("Enter the itemId", 5, input -> {
                            other.inventory.add(new Item(Integer.parseInt(input), 1));
                        }));
                    });

                } else {
                    player.message("Invalid command use; ::giveitem settings");
                }
            }
        });

        commands.add(new Command("giveexp", "giveexperience") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (parser.hasNext()) {
                    StringBuilder name = new StringBuilder(parser.nextString());

                    while (parser.hasNext()) {
                        name.append(" ").append(parser.nextString());
                    }
                    World.search(name.toString()).ifPresent(other -> {
                        player.send(new SendInputAmount("Enter the skillid", 5, input -> {
                            other.skills.addExperience(Integer.parseInt(input), 1_500_000);
                        }));
                    });

                } else {
                    player.message("Invalid command use; ::kill settings");
                }
            }
        });

        commands.add(new Command("kill") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (parser.hasNext()) {
                    StringBuilder name = new StringBuilder(parser.nextString());

                    while (parser.hasNext()) {
                        name.append(" ").append(parser.nextString());
                    }
                    World.search(name.toString()).ifPresent(other -> other.damage(new Hit(other.getCurrentHealth())));

                } else {
                    player.message("Invalid command use; ::kill settings");
                }
            }
        });


        commands.add(new Command("randomevent") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (parser.hasNext()) {
                    StringBuilder name = new StringBuilder(parser.nextString());

                    while (parser.hasNext()) {
                        name.append(" ").append(parser.nextString());
                    }
                    World.search(name.toString()).ifPresent(MimeEvent::create);

                } else {
                    player.message("Invalid command use; ::randomevent settings");
                }
            }
        });

        commands.add(new Command("alltome") {
            @Override
            public void execute(Player player, CommandParser parser) {
                Position position = player.getPosition().copy();
                World.getPlayers().forEach(players -> {
                    if (!players.isBot && !players.equals(player)) {
                        players.move(position);
                        players.send(new SendMessage("You have been mass teleported."));
                    }
                });
            }
        });

        commands.add(new Command("setrank", "giverank", "rank") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (parser.hasNext()) {
                    StringBuilder name = new StringBuilder(parser.nextString());

                    while (parser.hasNext()) {
                        name.append(" ").append(parser.nextString());
                    }

                    World.search(name.toString()).ifPresent(other -> {
                        DialogueFactory factory = player.dialogueFactory;
                        factory.sendOption("Ironman", () -> {
                            other.right = PlayerRight.IRONMAN;
                            player.message("You have promoted " + other.getName() + ": " + other.right.getName());
                            other.message("You have been promoted: " + other.right.getName());
                        }, "Ultimate Ironman", () -> {
                            other.right = PlayerRight.ULTIMATE_IRONMAN;
                            player.message("You have promoted " + other.getName() + ": " + other.right.getName());
                            other.message("You have been promoted: " + other.right.getName());
                        }, "Hardcore Ironman", () -> {
                            other.right = PlayerRight.HARDCORE_IRONMAN;
                            player.message("You have promoted " + other.getName() + ": " + other.right.getName());
                            other.message("You have been promoted: " + other.right.getName());
                        }, "Manager", () -> {
                            other.right = PlayerRight.MANAGER;
                            player.message("You have promoted " + other.getName() + ": " + other.right.getName());
                            other.message("You have been promoted: " + other.right.getName());
                        }, "Developer", () -> {
                            other.right = PlayerRight.DEVELOPER;
                            player.message("You have promoted " + other.getName() + ": " + other.right.getName());
                            other.message("You have been promoted: " + other.right.getName());
                        }).execute();
                    });
                } else {
                    player.message("Invalid command use; ::setrank settings");
                }
            }
        });

        commands.add(new Command("fight") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (parser.hasNext(2)) {
                    int one = parser.nextInt();
                    int two = parser.nextInt();
                    Position start = player.getPosition().copy();
                    if (NpcDefinition.get(one) == null || NpcDefinition.get(two) == null) {
                        player.send(new SendMessage("Definition for one or more of the monsters were null."));
                        return;
                    }
                    Npc boss1 = new Npc(one, new Position(start.getX() - 3, start.getY() + 3));
                    Npc boss2 = new Npc(two, new Position(start.getX() + 3, start.getY() + 3));
                    boss1.register();
                    boss2.register();
                    boss1.walk = false;
                    boss2.walk = false;
                    boss1.definition.setAggressive(false);
                    boss2.definition.setAggressive(false);
                    boss1.definition.setRespawnTime(-1);
                    boss2.definition.setRespawnTime(-1);
                    World.schedule(new Task(1) {
                        int count = 0;

                        @Override
                        public void execute() {
                            if (count == 0) {
                                boss1.interact(boss2);
                                boss1.speak("I will fight for you, " + player.getName() + "!");
                            } else if (count == 1) {
                                boss2.interact(boss1);
                                boss2.speak("But I will win for you, " + player.getName() + "!");
                            } else if (count == 3) {
                                boss1.speak("3");
                                boss2.speak("3");
                            } else if (count == 4) {
                                boss1.speak("2");
                                boss2.speak("2");
                            } else if (count == 5) {
                                boss1.speak("1");
                                boss2.speak("1");
                            } else if (count == 6) {
                                boss1.speak("Good luck " + boss2.getName() + "!");
                                boss2.speak("Good luck " + boss1.getName() + "!");
                            } else if (count > 7) {
                                cancel();
                            }
                            count++;
                        }

                        @Override
                        public void onCancel(boolean logout) {
                            boss1.getCombat().attack(boss2);
                            boss2.getCombat().attack(boss1);
                        }
                    });
                } else {
                    player.send(new SendMessage("Invalid command - ::fight 3080 3080"));
                }
            }
        });
    }

    @Override
    public boolean canAccess(Player player) {
        return player.right == PlayerRight.OWNER;
    }

}
