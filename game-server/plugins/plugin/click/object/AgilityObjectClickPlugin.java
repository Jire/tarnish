package plugin.click.object;

import com.osroyale.content.activity.randomevent.RandomEventHandler;
import com.osroyale.content.skill.impl.agility.Agility;
import com.osroyale.content.skill.impl.agility.obstacle.Obstacle;
import com.osroyale.content.skill.impl.agility.obstacle.ObstacleType;
import com.osroyale.game.event.impl.ObjectClickEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.Interactable;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;

public class AgilityObjectClickPlugin extends PluginContext {

    @Override
    protected boolean onClick(Player player, int button) {
        return Agility.clickButton(player, button);
    }

    @Override
    protected boolean firstClickObject(Player player, ObjectClickEvent event) {
        final Obstacle obstacle = Agility.obstacles.get(event.getObject().getPosition());

        if (obstacle == null) {
            return false;
        }

        player.walkTo(Interactable.create(obstacle.getStart(), 0, 0), false, () -> {
            if (player.attributes.get("AGILITY_FLAGS") == null) {
                player.attributes.set("AGILITY_FLAGS", 0);
            }
            if (obstacle.getType() == ObstacleType.ROPE_SWING || obstacle.getType() == ObstacleType.WILDERNESS_COURSE) {
                player.attributes.set("AGILITY_OBJ", event.getObject());
            }
            player.attributes.set("AGILITY_TYPE", obstacle.getType());
            World.schedule(1, () -> {
                player.face(event.getObject());
                obstacle.execute(player);
                RandomEventHandler.trigger(player);
            });
        });
        return true;
    }

}
