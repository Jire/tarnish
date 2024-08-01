package plugin.click.item;

import com.osroyale.game.Animation;
import com.osroyale.game.event.impl.ItemClickEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.task.impl.ObjectPlacementEvent;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.Direction;
import com.osroyale.game.world.entity.mob.data.LockType;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.object.CustomGameObject;
import com.osroyale.game.world.pathfinding.TraversalMap;
import com.osroyale.game.world.position.Area;
import com.osroyale.game.world.position.Position;
import com.osroyale.util.chance.Chance;
import com.osroyale.util.chance.WeightedChance;

import java.util.Arrays;

public class MithrilSeedPlugin extends PluginContext {

    private static final Chance<FlowerData> FLOWERS = new Chance<>(Arrays.asList(
            new WeightedChance<>(10.78, FlowerData.ASSORTED),
            new WeightedChance<>(14.08, FlowerData.RED),
            new WeightedChance<>(15.3, FlowerData.BLUE),
            new WeightedChance<>(14.65, FlowerData.YELLOW),
            new WeightedChance<>(14.85, FlowerData.PURPLE),
            new WeightedChance<>(15.39, FlowerData.ORANGE),
            new WeightedChance<>(14.66, FlowerData.MIXED),
            new WeightedChance<>(.06, FlowerData.WHITE),
            new WeightedChance<>(.23, FlowerData.BLACK)
    ));

    public enum FlowerData {
        ASSORTED(2980, 2460),
        RED(2981, 2462),
        BLUE(2982, 2464),
        YELLOW(2983, 2466),
        PURPLE(2984, 2468),
        ORANGE(2985, 2470),
        MIXED(2986, 2472),
        WHITE(2987, 2474),
        BLACK(2988, 2476);

        public final int object;
        public final int item;

        FlowerData(int object, int item) {
            this.object = object;
            this.item = item;
        }
    }

    @Override
    protected boolean firstClickItem(Player player, ItemClickEvent event) {
        if (event.getItem().getId() != 299) {
            return false;
        }

        if (Area.inEdgeville(player) || Area.inSuperDonatorZone(player) || Area.inRegularDonatorZone(player)|| Area.inWilderness(player)) {
            player.message("You are prohibited from planting a seed in this area!");
            return false;
        }

        if (player.getRegion().containsObject(player.getPosition())) {
            player.message("You can't plant a seed here!");
            return true;
        }

        Position walkTo = player.getPosition();
        FlowerData flower = FLOWERS.next();
        CustomGameObject object = new CustomGameObject(flower.object, player.instance, walkTo);

        player.inventory.remove(299, 1);
        player.locking.lock(LockType.MASTER_WITH_MOVEMENT);

        if (TraversalMap.isTraversable(player.getPosition(), Direction.WEST, player.width())) {
            walkTo = walkTo.west();
        } else if (TraversalMap.isTraversable(player.getPosition(), Direction.EAST, player.width())) {
            walkTo = walkTo.east();
        } else if (TraversalMap.isTraversable(player.getPosition(), Direction.SOUTH, player.width())) {
            walkTo = walkTo.north();
        } else if (TraversalMap.isTraversable(player.getPosition(), Direction.NORTH, player.width())) {
            walkTo = walkTo.south();
        }

        World.schedule(new ObjectPlacementEvent(object, 100));

        if (!player.getPosition().equals(walkTo)) {
            player.movement.walkTo(walkTo);
        }

        World.schedule(2, () -> {
            player.face(object);
            player.locking.unlock();
            player.dialogueFactory.sendOption("Pick", () -> {
                player.animate(new Animation(827));
                object.unregister();
                player.inventory.addOrDrop(new Item(flower.item, 1));
            }, "Leave", () -> player.dialogueFactory.clear());
            player.dialogueFactory.execute();
        });
        return true;
    }
}
