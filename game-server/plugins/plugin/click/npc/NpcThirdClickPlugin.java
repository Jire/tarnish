package plugin.click.npc;

import com.osroyale.content.dialogue.impl.RoyalKingDialogue;
import com.osroyale.content.skill.impl.slayer.SlayerOfferings;
import com.osroyale.game.event.impl.NpcClickEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;

public class NpcThirdClickPlugin extends PluginContext {

    @Override
    protected boolean thirdClickNpc(Player player, NpcClickEvent event) {
        switch (event.getNpc().id) {
            case 5523:
                player.dialogueFactory.sendDialogue(new RoyalKingDialogue(1));
                break;
            case 6797:
                SlayerOfferings.offer(player);
                break;
            case 311:
                player.playerAssistant.claimIronmanArmour();
                break;
        }
        return false;
    }

}
