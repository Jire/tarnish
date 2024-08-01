package plugin.click.npc;

import com.osroyale.content.activity.impl.magearena.MageArena;
import com.osroyale.content.activity.impl.recipefordisaster.RecipeForDisaster;
import com.osroyale.content.activity.panel.ActivityPanel;
import com.osroyale.content.dialogue.DialogueFactory;
import com.osroyale.content.dialogue.Expression;
import com.osroyale.content.dialogue.impl.*;
import com.osroyale.content.pet.Pets;
import com.osroyale.content.skill.impl.thieving.Thieving;
import com.osroyale.content.store.Store;
import com.osroyale.content.store.impl.SkillcapeStore;
import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.event.impl.NpcClickEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.combat.strategy.player.special.CombatSpecial;
import com.osroyale.game.world.entity.mob.UpdateFlag;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.net.packet.out.SendRunEnergy;
import com.osroyale.net.packet.out.SendString;
import com.osroyale.util.Utility;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import static com.osroyale.content.pet.PetData.JAD;

public class NpcFirstClickPlugin extends PluginContext {

    @Override
    protected boolean firstClickNpc(Player player, NpcClickEvent event) {
        final int id = event.getNpc().id;
        System.out.println(id);
        switch (id) {
            case 1603:
                player.dialogueFactory.sendStatement("Would you like to play the Mage Arena minigame?").sendOption("Yes", () -> {
                    player.locking.lock();
                    World.schedule(5, () -> {
                        MageArena.create(player);
                        player.locking.unlock();
                    });
                }, "No", () -> player.dialogueFactory.clear()).execute();
                return true;
            case 1757:
                player.dialogueFactory.sendStatement("Would you like to leave the pest control minigame?").sendOption("Yes", () -> {
                    player.locking.lock();
                    World.schedule(2, () -> {
                        player.locking.unlock();
                        player.move(new Position(2659, 2649, 0));
                        player.animate(Animation.RESET, true);
                        player.graphic(Graphic.RESET, true);
                        ActivityPanel.clear(player);
                    });
                }, "No", () -> player.dialogueFactory.clear()).execute();
                return true;
            case 6526:
                player.dialogueFactory.sendStatement("Would you like to play the Recipe for disaster minigame?", "All gloves are unlocked by completing this minigame.").sendOption("Yes", () -> {
                    player.locking.lock();
                    World.schedule(5, () -> {
                        RecipeForDisaster.create(player);
                        player.locking.unlock();
                    });
                }, "No", () -> player.dialogueFactory.clear()).execute();
                return true;
            case 502:
                Store.STORES.get("Skilling Store Equipment").open(player);
            break;
            case 7987: {
                DialogueFactory factory = player.dialogueFactory;
                if (player.lostItems.isEmpty()) {
                    factory.sendNpcChat(7987, "You do not have any lost items to retrieve!").execute();
                    return true;
                }
                int amount = player.lostItems.size();
                int fee = 30;
                int cost = amount * fee;
                factory.sendNpcChat(7987, "You have " + amount + " items to claim. Each item will", "cost you " + Utility.formatDigits(fee) + " Blood money. Your total cost will be:", Utility.formatDigits(cost) + " bm.", "Would you like to proceed?");
                factory.sendOption("Yes", () -> {
                    int amount2 = player.lostItems.size();
                    boolean noSpace = false;
                    if (amount2 > player.inventory.getFreeSlots()) {
                        noSpace = true;
                        amount2 = player.inventory.getFreeSlots();
                    }
                    int cost2 = amount2 * fee;
                    if (!player.inventory.contains(13307, cost2)) {
                        factory.sendNpcChat(7987, "You do not have enough bm to do this!");
                        return;
                    }
                    Iterator<Item> iterator = player.lostItems.iterator();
                    while (iterator.hasNext()) {
                        Item item = iterator.next();
                        if (player.inventory.hasCapacityFor(item)) {
                            player.inventory.remove(13307, fee);
                            player.inventory.add(item);
                            iterator.remove();
                        } else {
                            break;
                        }
                    }
                    if (noSpace) {
                        factory.sendNpcChat(7987, "You did not have enough inventory spaces to", "claim the rest of your items!");
                        return;
                    }
                    factory.sendNpcChat(7987, "Pleasure doing business with you my friend!");
                }, "No", factory::clear);
                factory.execute();
                break;
            }

            case 4925:
                player.dialogueFactory.sendNpcChat(4925, "Oh you saved me! Take this as a reward!").sendItem("Reward", "Dusty Key", 1590).execute();
                player.inventory.add(1590, 1);
                break;

            case 7481:
                player.dialogueFactory.sendDialogue(new VoteDialogue());
                break;

            case 5789:
                player.send(new SendString("Agility Ticket Exchange", 8383));
                player.interfaceManager.open(8292);
                break;

            case 2186:
                Store.STORES.get("The Tzhaar Tokkul Store").open(player);
                break;

            case 2180: {
                DialogueFactory factory = player.dialogueFactory;
                factory.sendNpcChat(2180, "Would you like to sacrifice a firecape for a chance", "of getting the pet? Chances are 1/200.");
                factory.sendOption("Yes", () -> {
                    factory.onAction(() -> {
                        if (!player.inventory.contains(6570, 1)) {
                            factory.sendNpcChat(2180, "You must have a firecape to sacrifice first!").execute();
                            return;
                        }

                        player.inventory.remove(6570, 1);
                        if (!Pets.onReward(player, JAD)) {
                            factory.sendNpcChat(2180, "I'm sorry. The RNG was not on your side today!").execute();
                        } else {
                            factory.clear();
                        }
                    });
                }, "No", factory::clear);
                factory.execute();
                break;
            }

            /* Banker */
            case 394:
            case 395:
            case 2897:
            case 2898:
                player.bankPin.open();
                break;

            /* Mage bank */
           case 7417: {
               DialogueFactory factory = player.dialogueFactory;
               factory.sendNpcChat(id, "Hello adventurer!", "Are you interested in opening this bank", "for 10,000 gp?").sendOption("Yes", () -> {
                   factory.onAction(() -> {
                       if (!player.inventory.contains(new Item(995, 10_000))) {
                           factory.sendNpcChat(id, "You need 10,000 gp to open this bank!").execute();
                           return;
                       }


                       player.inventory.remove(995, 10_000);
                       player.bank.open();
                   });
               }, "No", () -> {
               }).execute();

           }
        break;



        /* Nurse sarah */
            case 1152:
                if (player.getCombat().inCombat()) {
                    player.message("You can't do this whilst in combat!");
                    return true;
                }

                if (player.hasPvPTimer) {
                    player.message("You can't do this with an active PvP timer!");
                    return true;
                }

                int length = PlayerRight.isDonator(player) ? 1 : 2;
                if (!player.restoreDelay.elapsed(length, TimeUnit.MINUTES)) {
                    player.dialogueFactory.sendNpcChat(id, "You can only do this once every " + length + " minutes!", "Time Passed: " + Utility.getTime(player.restoreDelay.elapsedTime())).execute();
                    return true;
                }
                player.unpoison();
                player.unvenom();
                player.playerAssistant.resetEffects();
                player.playerAssistant.setWidget();
                player.runEnergy = 100;
                player.teleblockTimer.set(0);
                player.send(new SendRunEnergy());
                player.skills.restoreAll();
                CombatSpecial.restore(player, 100);
                player.dialogueFactory.sendNpcChat(id, "Your health & special attack have been restored!").execute();
                player.restoreDelay.reset();
                break;

//            case 506:
//            case 507:
//            case 513: // falador female
//            case 512: // falador male
           case 2813:
             Store.STORES.get("The General Store").open(player);
                break;

            case 1199:
                Store.STORES.get("Chef's Choodle Oodle Store").open(player);
                break;

        /* Gnome trainer */
            case 6080:
                player.dialogueFactory.sendPlayerChat("Hello there.").sendNpcChat(id, "This isn't a grannies' tea party, let's see some sweat", "human. Go! Go! Go!").execute();
                break;

         /* Barbarian trainer */
            case 2153:
                player.dialogueFactory.sendNpcChat(id, "Haha welcome to my obstacle course. Have fun!").sendNpcChat(id, "The best way to train, is to go round the course in a", "clockwise direction.").execute();
                break;

        /* Potion decanting */
            case 1146: {
//                DialogueFactory factory = player.dialogueFactory;
//                factory.sendNpcChat(id, "Hello, I am Joe the chemist.", "How may I help you?");
//                factory.sendOption(
//                        "Decant all potions (50,000 RT)",
//                        () -> factory.onAction(() -> Decanting.decant(player)),
//
//                        "Nevermind",
//                        () -> factory.onAction(player.interfaceManager::close));
//                factory.execute();
                break;
            }
           /* case 311: {
                DialogueFactory factory = player.dialogueFactory;
                if (!PlayerRight.isIronman(player) && !PlayerRight.isDeveloper(player)) {
                    factory.sendNpcChat(id, "Sorry, but you need to be an Ironman to speak to me!").execute();
                    return true;
                }
                factory.sendNpcChat(id, "Welcome, my friend. How may I help you?");
                factory.sendOption("Claim armor", player.playerAssistant::claimIronmanArmour, "Open store", () -> {
                        Store.STORES.get("Skilling Store Equipment").open(player);
                }, "Reset rank", () -> {
                    factory.sendNpcChat(id, "Are you sure you want to reset your rank?");
                    factory.sendOption("Yes", () -> {
                        factory.onAction(() -> {
                            player.right = PlayerRight.PLAYER;
                            player.updateFlags.add(UpdateFlag.APPEARANCE);
                            player.dialogueFactory.sendNpcChat(id, "You are no longer an Ironman!", "Re-log for the changes to take effect.").execute();
                        });
                    }, "No", factory::clear);
                });
                factory.execute();
                break;
            }*/
            case 311: {
                DialogueFactory factory = player.dialogueFactory;
                if (!PlayerRight.isIronman(player) && !PlayerRight.isDeveloper(player)) {
                    factory.sendNpcChat(id, "Sorry, but you need to be an Ironman to speak to me!").execute();
                    return true;
                }
                factory.sendNpcChat(id, "Welcome, my friend. How may I help you?");
                factory.sendOption("Claim armor", player.playerAssistant::claimIronmanArmour, "Open general store", () -> {
                    Store.STORES.get("Ironman General Store").open(player);
                }, "Open skilling tools", () -> {
                    Store.STORES.get("Ironman Skilling Tools").open(player);
                }, "Open herblore supplies", () -> {
                    Store.STORES.get("Herblore Supplies").open(player);
                }, "Reset rank", () -> {
                    factory.sendNpcChat(id, "Are you sure you want to reset your rank?");
                    factory.sendOption("Yes", () -> {
                        factory.onAction(() -> {
                            player.right = PlayerRight.PLAYER;
                            player.updateFlags.add(UpdateFlag.APPEARANCE);
                            player.dialogueFactory.sendNpcChat(id, "You are no longer an Ironman!", "Re-log for the changes to take effect.").execute();
                        });
                    }, "No", factory::clear);
                });
                factory.execute();
                break;
            }
		/* Ironman shop */


		/* Melee shop */
            case 3216: {
                DialogueFactory factory = player.dialogueFactory;
                factory.sendOption("Weapons", () -> Store.STORES.get("Melee Weapons").open(player), "Armour", () -> Store.STORES.get("Melee Armour").open(player)).execute();
            }
            break;
            case 3213: {
                    Store.STORES.get("Henry's Fishing Supplies").open(player);
            }
            break;
		/* Ranged shop */
            case 3217: {
                DialogueFactory factory = player.dialogueFactory;
                factory.sendOption("View stores", () -> {
                    factory.sendOption("Melee Armour Store", () -> {
                        Store.STORES.get("Melee Armour").open(player);
                    }, "Melee Weapon Store", () -> {
                            Store.STORES.get("Melee Weapons").open(player);
                        },
                            "Range Store", () -> {
                        Store.STORES.get("Range Store").open(player);
                    }, "Magic Store", () -> {
                        Store.STORES.get("Magic Store").open(player);
                    });
                }, "Nevermind", factory::clear);
                factory.execute();
            }
            break;

		/* Magic shop */
            case 3218:
                Store.STORES.get("Magic Store").open(player);
                break;

		/* Pure shop */
            case 5440:
                Store.STORES.get("Pure Store").open(player);
                break;

        /* Wise old man */
            case 4306:
                new SkillcapeStore().open(player);
                break;

            case 6773:
                player.lostUntradeables.open();
                break;

		/* Skilling shop */
            case 5366, 43134:
                player.dialogueFactory.sendOption("Equipment", () -> {
                    player.dialogueFactory.onAction(() -> Store.STORES.get("Skilling Store Equipment").open(player));
                }, "Farming", () -> {
                    player.dialogueFactory.onAction(() -> Store.STORES.get("Farming Supplies").open(player));
                }, "Herblore", () -> {
                    player.dialogueFactory.onAction(() -> Store.STORES.get("Herblore Supplies").open(player));
                }, "Miscellaneous", () -> {
                    player.dialogueFactory.onAction(() -> Store.STORES.get("Miscellaneous Supplies").open(player));
                }).execute();
                break;

            /* Mac */
            case 6481: {
                DialogueFactory factory = player.dialogueFactory;
                if (!player.skills.isMaxed()) {
                    factory.sendNpcChat(id, "Please speak to me once you are maxed in all 20 skills.").execute();
                    return true;
                }

                factory.sendNpcChat(id, "Hello adventurer!", "Are you interested in purchasing a Max cape and", "hood for 2,500,000 gp?").sendOption("Yes", () -> {
                    factory.onAction(() -> {
                        if (!player.inventory.contains(new Item(995, 2_500_000))) {
                            factory.sendNpcChat(id, "You need 2,500,000 gp to purchase this cape!").execute();
                            return;
                        }
                        if (player.inventory.contains(new Item(13281))) {
                            factory.sendNpcChat(id, "You already have one cape in your inventory!").execute();
                            return;
                        }
                        if (player.bank.contains(new Item(13281))) {
                            factory.sendNpcChat(id, "You already have one cape in your bank!").execute();
                            return;
                        }
                        final Item[] ITEMS = {new Item(13281), new Item(13280)};
                        if (!player.inventory.hasCapacityFor(ITEMS)) {
                            factory.sendNpcChat(id, "You do not have enough inventory spaces for this!").execute();
                            return;
                        }

                        player.inventory.remove(995, 2_500_000);
                        player.inventory.addAll(ITEMS);
                        factory.sendNpcChat(id, "Enjoy your new cape!").execute();
                    });
                }, "No", () -> {
                    factory.onAction(player.interfaceManager::close);
                }).execute();
            }
            break;

		/* Construction dialogue */
            case 5419:
                player.dialogueFactory.sendDialogue(new ConstructionDialogue());
                break;

		/* King Royal dialogue */
            case 5523:
                player.dialogueFactory.sendDialogue(new RoyalKingDialogue(0));
                break;

		/* Clanmaster Dialogue. */
            case 3841:
                player.dialogueFactory.sendDialogue(new ClanmasterDialogue());
                break;

		/* Nieve Dialogue. */
            case 6797:
                player.dialogueFactory.sendDialogue(new NieveDialogue());
                break;

		/* Makeover mage */
            case 1307:
                player.interfaceManager.open(3559);
                break;

		/* Thieving goods merchant */
            case 3439:
                Thieving.exchange(player);
                break;

            case 1755:
                Store.STORES.get("The Pest Control Store").open(player);
                break;

            case 5919:
                Store.STORES.get("Grace's Graceful Store").open(player);
                break;

            case 306:
                player.dialogueFactory.sendNpcChat(306, "Hello #name, would you like to do the tutorial?");
                player.dialogueFactory.sendOption("Yes", () -> player.send(new SendMessage("Tutorial coming soon.")), "No", () -> player.dialogueFactory.clear());
                player.dialogueFactory.execute();
                break;

            default:
                player.dialogueFactory.sendNpcChat(id, Expression.ANNOYED, "Please go away I'm busy.").execute();
        }
        return false;
    }

}
