package plugin.click.npc;

import com.osroyale.content.dialogue.DialogueFactory;
import com.osroyale.content.dialogue.impl.RoyalKingDialogue;
import com.osroyale.content.skill.impl.slayer.SlayerTab;
import com.osroyale.content.store.Store;
import com.osroyale.content.store.impl.RecipeForDisasterStore;
import com.osroyale.game.event.impl.NpcClickEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;

public class NpcSecondClickPlugin extends PluginContext {

    @Override
    protected boolean secondClickNpc(Player player, NpcClickEvent event) {
        final int id = event.getNpc().id;
        switch (id) {
            case 6526:
                new RecipeForDisasterStore().open(player);
                break;
            case 5523:
                player.dialogueFactory.sendDialogue(new RoyalKingDialogue(2));
                break;
            case 3089:
            case 1634:
            case 1633:
            case 1613:
                player.bank.open();
            break;
            case 2148:
            case 2149:
            case 2150:
            case 2151:
                player.tradingPost.openOverviewInterface();
                break;
            case 7481:
                Store.STORES.get("Tarnish Vote Store").open(player);
                break;

            case 1603:
                Store.STORES.get("Kolodion's Arena Store").open(player);
                break;

//            case 506:
//            case 507:
//            case 513: // falador female
//            case 512: // falador male
//            case 1032:
//                Store.STORES.get("The General Store").open(player);
//                break;

            case 311: {
                DialogueFactory factory = player.dialogueFactory;

                if (!PlayerRight.isIronman(player) && !PlayerRight.isDeveloper(player)) {
                    factory.sendStatement("You do not have any permission to use this store!");
                    factory.execute();
                    return true;
                }

                factory.sendOption("Ironman Store", () -> {
                    Store.STORES.get("Ironman General Store").open(player);
                }, "Skilling Tools", () -> {
                    Store.STORES.get("Ironman Skilling Tools").open(player);
                }, "Herblore Supplies", () -> {
                    Store.STORES.get("Herblore Supplies").open(player);
                });

                factory.execute();
                break;
            }

		/* Nieve */
            case 6797:
                player.slayer.open(SlayerTab.MAIN);
                break;

        /* Zeke */
            case 527:
                Store.STORES.get("Zeke's Superior Scimitars").open(player);
                break;
        }
        return false;
    }

}
