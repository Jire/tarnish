package plugin.click.item;

import com.osroyale.content.achievement.AchievementHandler;
import com.osroyale.content.achievement.AchievementKey;
import com.osroyale.content.combat.cannon.Cannon;
import com.osroyale.content.combat.cannon.CannonManager;
import com.osroyale.content.skill.impl.magic.teleport.Teleportation;
import com.osroyale.content.skill.impl.woodcutting.BirdsNest;
import com.osroyale.content.store.Store;
import com.osroyale.content.tittle.PlayerTitle;
import com.osroyale.game.action.impl.SpadeAction;
import com.osroyale.game.event.impl.ItemClickEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.prayer.Prayer;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.packet.out.SendInputMessage;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.net.packet.out.SendString;
import com.osroyale.util.Stopwatch;
import com.osroyale.util.Utility;

import java.util.concurrent.TimeUnit;

public class ItemFirstClickPlugin extends PluginContext {
    public static final String[] BAD_STRINGS = {
            "fag", "f4g", "faggot", "nigger", "fuck", "bitch", "whore", "slut",
            "gay", "lesbian", "scape", ".net", ".org", "vagina", "dick", "cock",
            "penis", "hoe", "soulsplit", "owner", "moderator", "mod", "admin",
            "administrator", "server", "support", "server support", "supporter",
            "owner", "settings", "youtuber", "youtube", "ikov", "retard", "cunt",
            "pussy", "g ay", "ga y", "g a y", "h o e",
    };

    private static boolean containsBadString(String input) {
        input = input.toLowerCase();
        for(String s : BAD_STRINGS) {
            if(input.contains(s)) {
                return true;
            }
        }
        return false;
    }


    @Override
    protected boolean firstClickItem(Player player, ItemClickEvent event) {
        switch (event.getItem().getId()) {
            case 25527://Stardust
                Store.STORES.get("Stardust Store").open(player);
                break;
            case 12791:
                player.runePouch.open();
                break;
         /*   case 80:
                int charges = player.whipCharges;
                System.out.println(charges);
                charges--;
                System.out.println(charges);
                break;*/
            case 6:
                CannonManager.drop(player, new Cannon(player.getName(), player.getPosition()));
                break;
            case 6806: {
                    player.send(new SendInputMessage("Enter the title", 15, input -> {
                        if (containsBadString(input)) {
                            player.send(new SendMessage("You tried setting an inappropriate title. Try again."));
                        } else {
                        player.playerTitle = PlayerTitle.create(input, player.playerTitle.getColor());
                        player.inventory.remove(6806);
                        player.send(new SendMessage("Re-log for your title to take into effect."));
                        }
                    }));
                 }
            break;
            case 12938: {
                player.inventory.remove(12938, 1);
                Teleportation.teleport(player, new Position(2206, 3056, 0));
                player.message("You have teleported to Zul-andra");
                break;
            }
            case 405: {
                int coins = Utility.random(50000, 75000);
                player.inventory.remove(405, 1);
                player.inventory.add(995, coins);
                player.message(true, "You found " + Utility.formatDigits(coins) + " coins inside of the casket!");
                AchievementHandler.activate(player, AchievementKey.CASKET);
                break;
            }
            case 4447: {
                player.clanChannel.addExperience(500_000);
                player.inventory.remove(4447,1);
                player.message(true, "You have added 500,000 experience to your clan channel.");
                break;
            }
            case 2994: {
                player.inventory.remove(2994, 1);
                player.message(true, "You have added a new title to your collection.");
                Stopwatch.start();
                if(Stopwatch.start().elapsed(50, TimeUnit.MINUTES)) {
                    player.experienceRate /= 2;
                }
                break;
            }
            case 11187: {
                player.inventory.remove(11187, 1);
                player.message(true, "You have added 1,000,000 experience to your clan channel.");
                player.clanChannel.addExperience(250_000);
                break;
            }
            case 21034:
                if (player.unlockedPrayers.contains(Prayer.RIGOUR)) {
                    player.dialogueFactory.sendStatement("You already have this prayer unlocked!").execute();
                    return true;
                }

                player.inventory.remove(event.getItem());
                player.unlockedPrayers.add(Prayer.RIGOUR);
                player.dialogueFactory.sendStatement("You have learned the Rigour prayer!").execute();
                break;

            case 21079:
                if (player.unlockedPrayers.contains(Prayer.AUGURY)) {
                    player.dialogueFactory.sendStatement("You already have this prayer unlocked!").execute();
                    return true;
                }

                player.inventory.remove(event.getItem());
                player.unlockedPrayers.add(Prayer.AUGURY);
                player.dialogueFactory.sendStatement("You have learned the Augury prayer!").execute();
                break;


            case 2528:
                player.send(new SendString("Genie's Experience Lamp", 2810));
                player.send(new SendString("", 2831));
                player.interfaceManager.open(2808);
                break;

            /* Spade */
            case 952:
                player.action.execute(new SpadeAction(player), true);
                break;

			/* Birds nest. */
            case 5070:
            case 5071:
            case 5072:
            case 5073:
            case 5074:
                BirdsNest.search(player, event.getItem().getId());
                break;

            default:
                return false;

        }
        return true;
    }

}
