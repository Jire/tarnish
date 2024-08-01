package plugin.click.button;

import com.osroyale.content.StarterKit;
import com.osroyale.content.clanchannel.channel.ClanChannelHandler;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.combat.strategy.player.special.CombatSpecial;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.entity.mob.player.persist.PlayerSerializer;
import com.osroyale.util.Utility;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static com.osroyale.content.StarterKit.refresh;

public class StarterKitButtonPlugin extends PluginContext {

    private static final int[] STARTER_BUTTON = {-20528, -20524, -20527, -20523, -20526, -20522, -20525, -20521, -20520};

    public static boolean isButton(int button) {
        for (int b : STARTER_BUTTON) {
            if (button == b)
                return true;
        }
        return false;
    }

    @Override
    protected boolean onClick(Player player, int button) {
        if (!player.interfaceManager.isInterfaceOpen(45000)) {
            return false;
        }
        switch (button) {
            case -20528:
            case -20524:
                refresh(player, StarterKit.KitData.NORMAL);
                return true;
            case -20527:
            case -20523:
                refresh(player, StarterKit.KitData.IRONMAN);
                return true;
            case -20526:
            case -20522:
                refresh(player, StarterKit.KitData.ULTIMATE_IRONMAN);
                return true;
            case -20525:
            case -20521:
                refresh(player, StarterKit.KitData.HARDCORE_IRONMAN);
                return true;
            case -20520:
                confirm(player);
                return true;
        }
        return false;
    }

    /** Handles the confirmation of the starter kit. */
    private static void confirm(Player player) {
        if (player.buttonDelay.elapsed(1, TimeUnit.SECONDS)) {
            StarterKit.KitData kit = player.attributes.get("STARTER_KEY", StarterKit.KitData.class);
            String name = Utility.formatEnum(kit.name());

            Arrays.stream(kit.getItems()).forEach(player.inventory::add);

            if (kit.getRight() != PlayerRight.PLAYER) {
                player.settings.acceptAid = false;
            }

          /*  if (kit.getRight() == PlayerRight.PLAYER) {
                player.bank.clear();
               // player.bank.addAll(Config.STARTER_BANK);
              //  System.arraycopy(Config.STARTER_BANK_AMOUNT, 0, player.bank.tabAmounts, 0, Config.STARTER_BANK_AMOUNT.length);
                player.bank.shift();
            }*/

            player.clanTag = "Help";
            player.setVisible(true);
            player.newPlayer = false;
            player.needsStarter = false;
            player.right = kit.getRight();
            player.playerAssistant.setSidebar(false);
            player.message("<col=3559a5>Tarnish: </col>You will now be playing as " + Utility.getAOrAn(name) + " <col=3559a5>" + name + "</col> player.");
            player.message("<col=3559a5>Tarnish: </col>If you have any questions please visit our forums or contact a staff member.");
            player.locking.unlock();
            player.runEnergy = 100;
            CombatSpecial.restore(player, 100);
            player.buttonDelay.reset();
            player.interfaceManager.close();

            PlayerSerializer.save(player);
            ClanChannelHandler.connect(player, "tarnish");
            World.sendMessage("<col=FF0000>" + player.getName() + " is a new player and has just joined!");
        }
    }
}
