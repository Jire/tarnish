package plugin.click.object.wintertodt;

import com.osroyale.game.event.impl.ObjectClickEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.object.GameObject;
import com.osroyale.game.world.position.Position;
import com.osroyale.content.wintertodt.Wintertodt;

public class WintertodtEntrancePlugin extends PluginContext {

    @Override
    protected boolean firstClickObject(Player player, ObjectClickEvent event) {
        final GameObject gameObject = event.getObject();
        final int objectId = gameObject.getId();

        if(objectId != 29322) return false;

        boolean entering = player.getY() < 3966;

        if (entering)
            player.move(new Position(1630, 3982, 0));
        else {
            if(player.wintertodtPoints > 0) {
                player.dialogueFactory.sendOption("Leave and lose all progress.", () -> {
                    player.move(new Position(1630, 3958, 0));
                    player.wintertodtPoints = 0;
                }, "Stay.", () -> player.dialogueFactory.clear());
                player.dialogueFactory.execute();
            } else {
                player.move(new Position(1630, 3958, 0));
                player.wintertodtPoints = 0;
            }
        }

        return true;
    }

    @Override
    protected boolean secondClickObject(Player player, ObjectClickEvent event) {
        final GameObject gameObject = event.getObject();
        final int objectId = gameObject.getId();

        if(objectId != 29322) return false;

        player.dialogueFactory.sendStatement("The Wintertodt has "+(Wintertodt.health / (Wintertodt.MAX_HP / 100))+"% energy left. There are "+Wintertodt.region.getPlayers(0).size()+" players within", "the prison.");
        player.dialogueFactory.execute();

        return true;
    }

}
