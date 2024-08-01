package com.osroyale.game.world.items.ground;

import com.osroyale.game.event.impl.PickupItemEvent;
import com.osroyale.game.event.impl.log.DropItemLogEvent;
import com.osroyale.game.plugin.PluginManager;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.Entity;
import com.osroyale.game.world.entity.EntityType;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.position.Position;
import com.osroyale.game.world.region.Region;
import com.osroyale.net.packet.out.SendGroundItem;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.net.packet.out.SendRemoveGroundItem;

import java.util.Objects;

/**
 * Represents a single Ground item on the world map.
 *
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @author Daniel
 * @author Michael | Chex
 * @since 27-12-2016.
 */
public final class GroundItem extends Entity {

    /** The item that represents this ground item. */
    public final Item item;

    /** The optional player whom owns this item. */
    public final Player player;

    /** The randomevent for ground item. */
    public final GroundItemEvent event = new GroundItemEvent(this);

    /** The policy for this ground item. */
    public GroundItemPolicy policy;

    public boolean canIronMenPickThisItemUp = true;

    /** Creates a new {@link GroundItem} object for a {@code player} and an {@code item}. */
    public static void createGlobal(Player player, Item item) {
        GroundItem groundItem = new GroundItem(player, item, player.getPosition());
        groundItem.policy = GroundItemPolicy.GLOBAL;
        groundItem.instance = player.instance;
        groundItem.register();
        World.getDataBus().publish(new DropItemLogEvent(player, groundItem));
    }

    /** Creates a new {@link GroundItem} object for a {@code player} and an {@code item}. */
    public static void createGlobal(Player player, Item item, Position position) {
        GroundItem groundItem = new GroundItem(player, item, position);
        groundItem.policy = GroundItemPolicy.GLOBAL;
        groundItem.instance = player.instance;
        groundItem.register();
    }

    /** Creates a new {@link GroundItem} object for a {@code player} and an {@code item}. */
    public static GroundItem create(Player player, Item item) {
        GroundItem groundItem = new GroundItem(player, item, player.getPosition());
        groundItem.policy = GroundItemPolicy.ONLY_OWNER;
        groundItem.instance = player.instance;
        groundItem.register();
        World.getDataBus().publish(new DropItemLogEvent(player, groundItem));
        return groundItem;
    }

    /** Creates a new {@link GroundItem} object for a {@code player}, an {@code item} and {@code position}. */
    public static GroundItem create(Player player, Item item, Position position) {
        GroundItem groundItem = new GroundItem(player, item, position);
        groundItem.policy = GroundItemPolicy.ONLY_OWNER;
        groundItem.instance = player.instance;
        groundItem.register();
        return groundItem;
    }

    /** Constructs a new {@code GroundItem} object for a {@code player}, an {@code item}, and a {@code position}. */
    private GroundItem(Player player, Item item, Position position) {
        super(position);
        this.item = item;
        this.player = player;
        this.instance = player.instance;
    }

    public boolean canSee(Player other) {
        if (item.isTradeable() && policy.equals(GroundItemPolicy.GLOBAL) && instance == other.instance) {
            return true;
        }
        return player.usernameLong == other.usernameLong;
    }

    /** Attempts to pick the specified {@code item} up. */
    public static void pickup(Player player, Item item, Position position) {
        GroundItem result = position.getRegion().getGroundItem(item.getId(), position);

        if (result == null) {
            return;
        }

        if (PlayerRight.isIronman(player) && (!result.canIronMenPickThisItemUp || result.player.usernameLong != player.usernameLong)) {
            player.send(new SendMessage("As an iron man you may not pick up this item."));
            return;
        }

        if (!player.inventory.hasCapacityFor(item)) {
            player.send(new SendMessage("You don't have enough inventory space."));
            return;
        }

        result.event.cancel();
        player.inventory.add(result.item);
        PluginManager.getDataBus().publish(player, new PickupItemEvent(result));
    }

    @Override
    public void register() {
        if (isRegistered()) {
            return;
        }

        setRegistered(true);
        setPosition(getPosition());
    }

    @Override
    public void unregister() {
        if (!isRegistered()) {
            return;
        }

        setRegistered(false);
        removeFromRegion(getRegion());
    }

    @Override
    public void addToRegion(Region region) {
        if (item.getId() == 11283) {
            item.setId(11284);
            player.dragonfireCharges = 0;
        }
     /*   System.out.println("item id: "+item.getId());
        System.out.println("item amt: "+item.getAmount());*/
        GroundItem groundItem = region.getGroundItem(item.getId(), getPosition());
        if (groundItem != null && groundItem.item.isStackable()
                && policy.equals(groundItem.policy) && (policy == GroundItemPolicy.GLOBAL || player.usernameLong == groundItem.player.usernameLong)) {
            groundItem.event.cancel();
            item.incrementAmountBy(groundItem.item.getAmount());
        }
        Region[] regions = World.getRegions().getSurroundingRegions(getPosition());
        for (Region reg : regions) {
            reg.getPlayers(getHeight()).forEach(player -> {
                if (canSee(player)) {
                    player.send(new SendGroundItem(this));
                }
            });
        }

        region.addGroundItem(this);
        World.schedule(event);
    }

    @Override
    public void removeFromRegion(Region region) {
        Region[] regions = World.getRegions().getSurroundingRegions(getPosition());
        for (Region reg : regions) {
            reg.getPlayers(getHeight()).forEach(player -> {
                if (canSee(player)) {
                    player.send(new SendRemoveGroundItem(this));
                }
            });
        }
        region.removeGroundItem(this);
    }

    @Override
    public String getName() {
        return item.getName();
    }

    @Override
    public EntityType getType() {
        return EntityType.GROUND_ITEM;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof GroundItem) {
            GroundItem other = (GroundItem) obj;
            return other.getPosition().equals(getPosition())
                && other.item.equals(item)
                && player.usernameLong == other.player.usernameLong;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPosition(), getIndex());
    }

    @Override
    public String toString() {
        return "GroundItem[owner=" + player.getUsername() + ", position=" + getPosition() + ", index=" + getIndex() + ", item=" + item.getName() + "]";
    }
}
