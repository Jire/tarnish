package plugin.click.button;

import com.osroyale.content.dialogue.Dialogue;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;

import java.util.concurrent.TimeUnit;

public class DialogueButtonPlugin extends PluginContext {

    @Override
    protected boolean onClick(Player player, int button) {
        if (Dialogue.isDialogueButton(button) /*&& player.optionDialogue.isPresent()*/) {
            switch (button) {
                case 2461:
                case 2471:
                case 2482:
                case 2494:
                    player.dialogueFactory.executeOption(0, player.optionDialogue);
                    return true;
                case 2495:
                case 2462:
                case 2472:
                case 2483:
                    player.dialogueFactory.executeOption(1, player.optionDialogue);
                    return true;
                case 2496:
                case 2473:
                case 2484:
                    player.dialogueFactory.executeOption(2, player.optionDialogue);
                    break;
                case 2497:
                case 2485:
                    player.dialogueFactory.executeOption(3, player.optionDialogue);
                    return true;
                case 2498:
                    player.dialogueFactory.executeOption(4, player.optionDialogue);
                    return true;
            }
        }

        if (player.chatBoxItemDialogue != null) {
            if (player.chatBoxItemDialogue.clickButton(button)) {
                player.chatBoxItemDialogue = null;
                return true;
            }
        }
        return false;
    }
}
