package plugin.click.button;

import com.osroyale.content.dialogue.DialogueFactory;
import com.osroyale.content.skill.impl.slayer.SlayerTab;
import com.osroyale.content.skill.impl.slayer.TaskDifficulty;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.out.SendString;

public class SlayerButtonPlugin extends PluginContext {

    @Override
    protected boolean onClick(Player player, int button) {
        if (button >= -18625 && button <= -18571) {
            player.slayer.confirm(button);
            return true;
        }

        switch (button) {
            case -18830:
            case -19233:
                player.slayer.open(SlayerTab.MAIN);
                return true;
            case -18829:
                player.slayer.open(SlayerTab.DUO);
                return true;
            case -18828:
            case -19130:
                player.send(new SendString("Go back", 46412));
                player.send(new SendString("Confirm", 46413));
                player.send(new SendString("Slayer Confirmation", 46402));
                player.slayer.open(SlayerTab.UNLOCK);
                return true;
            case -18827:
                player.slayer.open(SlayerTab.REWARD);
                return true;
            case -18795:
                player.slayer.open(SlayerTab.TASK);
                return true;
            case -18817: {
                DialogueFactory factory = player.dialogueFactory;

                factory.sendOption("Easy", () -> {
                    player.slayer.assign(TaskDifficulty.EASY);
                }, "Medium", () -> {
                    player.slayer.assign(TaskDifficulty.MEDIUM);
                }, "Hard", () -> {
                    player.slayer.assign(TaskDifficulty.HARD);
                }, "Bosses", () -> {
                    player.slayer.assign(TaskDifficulty.BOSS);
                });

                factory.execute();
                return true;
            }

            case -18814:
                DialogueFactory factory = player.dialogueFactory;

                factory.sendOption("Cancel task", () -> {
                    player.slayer.cancel(true);
                }, "Nevermind.", () -> {
                            player.dialogueFactory.clear();
                        });
                    factory.execute();
                    return true;
                case -18811:
                player.slayer.block();
                return true;
            case -18784:
                player.slayer.unblock(0);
                return true;
            case -18781:
                player.slayer.unblock(1);
                return true;
            case -18778:
                player.slayer.unblock(2);
                return true;
            case -18775:
                player.slayer.unblock(3);
                return true;
            case -18772:
                player.slayer.unblock(4);
                return true;
            case -19127:
                player.slayer.purchase();
                return true;
        }
        return false;
    }
}
