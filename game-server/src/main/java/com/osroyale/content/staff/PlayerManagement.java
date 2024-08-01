package com.osroyale.content.staff;

import com.osroyale.Config;
import com.osroyale.content.dialogue.DialogueFactory;
import com.osroyale.game.world.InterfaceConstants;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.UpdateFlag;
import com.osroyale.game.world.entity.mob.npc.definition.NpcDefinition;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.entity.mob.player.persist.PlayerSerializer;
import com.osroyale.game.world.entity.mob.player.profile.ProfileRepository;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.containers.pricechecker.PriceType;
import com.osroyale.net.packet.out.SendInputAmount;
import com.osroyale.net.packet.out.SendInputMessage;
import com.osroyale.net.packet.out.SendItemOnInterface;
import com.osroyale.net.packet.out.SendString;
import com.osroyale.util.Utility;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.osroyale.game.world.entity.mob.player.PlayerRight.*;

/**
 * Handles the player management.
 *
 * @author Daniel
 */
public enum PlayerManagement implements ActionEffect {
    TELEPORT_TO(PlayerRight.getColor(MODERATOR) + "Teleport To") {
        @Override
        public void handle(Player player) {
            if (PlayerRight.isModerator(player) && player.managing.isPresent()) {
                Player other = player.managing.get();

                player.move(other.getPosition());
                player.instance = other.instance;
                player.pvpInstance = other.pvpInstance;
            }
        }
    },
    TELEPORT_TO_ME(PlayerRight.getColor(MODERATOR) + "Teleport To Me") {
        @Override
        public void handle(Player player) {
            if (PlayerRight.isModerator(player) && player.managing.isPresent()) {
                Player other = player.managing.get();

                other.move(player.getPosition());
                other.instance = player.instance;
                other.pvpInstance = player.pvpInstance;
                other.message("<col=FF0D5D>You have been force teleported to " + player.getName() + ".");
            }
        }
    },
    MOVE_HOME(PlayerRight.getColor(MODERATOR) + "Move Home") {
        @Override
        public void handle(Player player) {
            if (PlayerRight.isModerator(player) && player.managing.isPresent()) {
                Player other = player.managing.get();

                other.move(Config.DEFAULT_POSITION);
                other.instance = Mob.DEFAULT_INSTANCE;
                other.pvpInstance = false;
                other.message("<col=FF0D5D>You have been force teleported home by " + player.getName() + ".");
            }
        }
    },
    KICK(PlayerRight.getColor(MODERATOR) + "Kick") {
        @Override
        public void handle(Player player) {
            if (PlayerRight.isModerator(player) && player.managing.isPresent()) {
                Player other = player.managing.get();

                confirm(player, () -> {
                    World.kickPlayer(other);
                    player.message("<col=FF0D5D>You have force kicked: " + other.getName() + ".");
                });
            }
        }
    },
    JAIL(PlayerRight.getColor(MODERATOR) + "Jail") {
        @Override
        public void handle(Player player) {
            if (PlayerRight.isModerator(player) && player.managing.isPresent()) {
                Player other = player.managing.get();

                confirm(player, () -> {
                    DialogueFactory factory = player.dialogueFactory;
                    factory.sendOption("Jail by day", () -> {
                        factory.onAction(() -> player.send(new SendInputAmount("How long do you want this jail to last for?", 2, input -> {
                            other.punishment.jail(Integer.parseInt(input), TimeUnit.DAYS);
                            factory.clear();
                        })));
                    }, "Jail by hour", () -> {
                        factory.onAction(() -> player.send(new SendInputAmount("How long do you want this jail to last for?", 3, input -> {
                            other.punishment.jail(Integer.parseInt(input), TimeUnit.HOURS);
                            factory.clear();
                        })));
                    }, "Jail by minute", () -> {
                        factory.onAction(() -> player.send(new SendInputAmount("How long do you want this jail to last for?", 3, input -> {
                            other.punishment.jail(Integer.parseInt(input), TimeUnit.MINUTES);
                            factory.clear();
                        })));
                    }, "Jail forever", () -> {
                        factory.onAction(() -> {
                            other.punishment.jail(9999, TimeUnit.DAYS);
                            factory.clear();
                        });
                    });
                });
            }
        }
    },
    UNJAIL(PlayerRight.getColor(MODERATOR) + "Un-Jail") {
        @Override
        public void handle(Player player) {
            if (PlayerRight.isModerator(player) && player.managing.isPresent()) {
                Player other = player.managing.get();

                other.punishment.unJail();
                other.dialogueFactory.sendStatement("You have been unjailed!").execute();
                other.message("<col=FF0D5D>You have been unjailed by: " + player.getName());
                player.message("<col=FF0D5D>You have unjailed: " + other.getName());
            }
        }
    },
    MUTE(PlayerRight.getColor(MODERATOR) + "Mute") {
        @Override
        public void handle(Player player) {
            if (PlayerRight.isModerator(player) && player.managing.isPresent()) {
                Player other = player.managing.get();

                confirm(player, () -> {
                    DialogueFactory factory = player.dialogueFactory;
                    factory.sendOption("Mute by day", () -> {
                        factory.onAction(() -> player.send(new SendInputAmount("How long do you want this mute to last for?", 2, input -> {
                            other.punishment.mute(Integer.parseInt(input), TimeUnit.DAYS);
                            factory.clear();
                        })));
                    }, "Mute by hour", () -> {
                        factory.onAction(() -> player.send(new SendInputAmount("How long do you want this mute to last for?", 3, input -> {
                            other.punishment.mute(Integer.parseInt(input), TimeUnit.HOURS);
                            factory.clear();
                        })));
                    }, "Mute by minute", () -> {
                        factory.onAction(() -> player.send(new SendInputAmount("How long do you want this mute to last for?", 3, input -> {
                            other.punishment.mute(Integer.parseInt(input), TimeUnit.MINUTES);
                            factory.clear();
                        })));
                    }, "Mute forever", () -> {
                        factory.onAction(() -> {
                            other.punishment.mute(9999, TimeUnit.DAYS);
                            factory.clear();
                        });
                    });
                });
            }
        }
    },
    UNMUTE(PlayerRight.getColor(MODERATOR) + "Un-Mute") {
        @Override
        public void handle(Player player) {
            if (PlayerRight.isModerator(player) && player.managing.isPresent()) {
                Player other = player.managing.get();

                other.punishment.unmute();
                other.dialogueFactory.sendStatement("You have been unmuted!").execute();
                other.message("<col=FF0D5D>You have been unmuted by: " + player.getName());
                player.message("<col=FF0D5D>You have unmuted: " + other.getName());
            }
        }
    },
    CHECK_BANK(PlayerRight.getColor(MODERATOR) + "Check Bank") {
        @Override
        public void handle(Player player) {
            if (PlayerRight.isModerator(player) && player.managing.isPresent()) {
                Player other = player.managing.get();
                long totalValue = other.bank.containerValue(PriceType.VALUE);
                long vaultCoins = other.bankVault.coinsContainer;
                long vaultBM = other.bankVault.bloodMoneyContainer;

                player.interfaceManager.openInventory(60000, 5063);
                player.send(new SendItemOnInterface(InterfaceConstants.WITHDRAW_BANK, other.bank.tabAmounts, other.bank.toArray()));
                player.send(new SendItemOnInterface(InterfaceConstants.INVENTORY_STORE, other.inventory.toArray()));
                player.send(new SendString("", 60_079));
                player.dialogueFactory.sendInformationBox(other.getName() + "'s Bank:", "Bank Value: " + (totalValue), "Bank Vault (Coins): " + (vaultCoins), "Bank Vault (BM): " + (vaultBM)).execute();
            }
        }
    },
    LINKED_ACCOUNTS(PlayerRight.getColor(MODERATOR) + "Linked Accounts") {
        @Override
        public void handle(Player player) {
            if (PlayerRight.isModerator(player) && player.managing.isPresent()) {
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
        }
    },
    PROMOTE(PlayerRight.getColor(ADMINISTRATOR) + "Promote") {
        @Override
        public void handle(Player player) {
            if (PlayerRight.isAdministrator(player) && player.managing.isPresent()) {
                Player other = player.managing.get();

                player.dialogueFactory.sendOption("Helper", () -> {
                    other.right = PlayerRight.HELPER;
                    other.updateFlags.add(UpdateFlag.APPEARANCE);
                    other.dialogueFactory.sendStatement("You were promoted to helper by " + player.getName()).execute();
                    player.message("You have promoted " + other.getName() + " to helper.");
                }, "Moderator", () -> {
                    other.right = PlayerRight.MODERATOR;
                    other.updateFlags.add(UpdateFlag.APPEARANCE);
                    other.dialogueFactory.sendStatement("You were promoted to moderator by " + player.getName()).execute();
                    player.message("You have promoted " + other.getName() + " to moderator.");
                }, "Administrator", () -> {
                    other.right = PlayerRight.ADMINISTRATOR;
                    other.updateFlags.add(UpdateFlag.APPEARANCE);
                    other.dialogueFactory.sendStatement("You were promoted to administrator by " + player.getName()).execute();
                    player.message("You have promoted " + other.getName() + " to administrator.");
                }).execute();
            }
        }
    },
    DEMOTE(PlayerRight.getColor(ADMINISTRATOR) + "Demote") {
        @Override
        public void handle(Player player) {
            if (PlayerRight.isAdministrator(player) && player.managing.isPresent()) {
                Player other = player.managing.get();

                other.right = PlayerRight.PLAYER;
                other.updateFlags.add(UpdateFlag.APPEARANCE);
                other.dialogueFactory.sendStatement("You were demoted by " + player.getName()).execute();
                player.message("You have demoted " + other.getName() + ".");
            }
        }
    },
    COPY(PlayerRight.getColor(ADMINISTRATOR) + "Copy") {
        @Override
        public void handle(Player player) {
            if (PlayerRight.isAdministrator(player) && player.managing.isPresent()) {
                Player other = player.managing.get();

                player.playerAssistant.copy(other);
                player.updateFlags.add(UpdateFlag.APPEARANCE);
                player.message("<col=FF0D5D>You have copied " + other.getName() + ".");
            }
        }
    },
    COPY_ME(PlayerRight.getColor(OWNER) + "Copy Me") {
        @Override
        public void handle(Player player) {
            if (PlayerRight.isOwner(player) && player.managing.isPresent()) {
                Player other = player.managing.get();

                other.playerAssistant.copy(player);
                other.updateFlags.add(UpdateFlag.APPEARANCE);
                player.message("<col=FF0D5D>You have X-copied " + other.getName() + ".");
            }
        }
    },
    CLEAR(PlayerRight.getColor(OWNER) + "Clear Account") {
        @Override
        public void handle(Player player) {
            if (PlayerRight.isOwner(player) && player.managing.isPresent()) {
                Player other = player.managing.get();

                confirm(player, () -> {
                    other.bank.clear();
                    other.inventory.clear();

                    for (int index = 0; index < Skill.SKILL_COUNT; index++) {
                        other.skills.setLevel(index, index == 3 ? 10 : 1);
                        other.skills.setMaxLevel(index, index == 3 ? 10 : 1);
                    }

                    other.skills.setCombatLevel();
                    other.updateFlags.add(UpdateFlag.APPEARANCE);
                });
            }
        }
    },
    PNPCS(PlayerRight.getColor(OWNER) + "Transform") {
        @Override
        public void handle(Player player) {
            if (PlayerRight.isOwner(player) && player.managing.isPresent()) {
                Player other = player.managing.get();

                player.send(new SendInputAmount("Enter NPC ID:", 4, input -> {
                    int id = Integer.parseInt(input);
                    if (id == 0)
                        id = -1;
                    other.playerAssistant.transform(id);
                    player.message("<col=FF0D5D>You have turned " + other.getName() + " into " + NpcDefinition.get(id).getName() + ".");
                }));
            }
        }
    },
    CHANGE_USERNAME(PlayerRight.getColor(OWNER) + "Change Username") {
        @Override
        public void handle(Player player) {
            if (PlayerRight.isOwner(player) && player.managing.isPresent()) {
                Player other = player.managing.get();

                player.send(new SendInputMessage("Enter their new username", 12, input -> {
                    if (ProfileRepository.exist(input)) {
                        player.dialogueFactory.sendStatement("The name " + input + " is already taken.").execute();
                        return;
                    }
                    other.setUsername(input);
                    other.updateFlags.add(UpdateFlag.APPEARANCE);
                    PlayerSerializer.save(other);
                    player.dialogueFactory.sendStatement("You have successfully changed their username.").execute();
                }));
            }
        }
    },;


    public final String string;

    PlayerManagement(String string) {
        this.string = string;
    }

    public static void confirm(Player player, Runnable runnable) {
        DialogueFactory factory = player.dialogueFactory;
        factory.sendStatement("Are you sure you want to execute this action?");
        factory.sendOption("Confirm action", runnable, "Nevermind", factory::clear);
        factory.execute();
    }

    public static PlayerManagement forOrdinal(int ordinal) {
        for (PlayerManagement management : values()) {
            if (management.ordinal() == ordinal) {
                return management;
            }
        }
        return null;
    }
}
