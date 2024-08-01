package com.osroyale.content.skill.impl.firemaking;

import com.osroyale.Config;
import com.osroyale.content.activity.randomevent.RandomEventHandler;
import com.osroyale.content.clanchannel.content.ClanTaskKey;
import com.osroyale.content.pet.PetData;
import com.osroyale.content.pet.Pets;
import com.osroyale.content.prestige.PrestigePerk;
import com.osroyale.content.skill.impl.DestructionSkillAction;
import com.osroyale.content.skillcape.SkillCape;
import com.osroyale.game.Animation;
import com.osroyale.game.task.Task;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.ground.GroundItem;
import com.osroyale.game.world.object.CustomGameObject;
import com.osroyale.game.world.position.Area;
import com.osroyale.game.world.region.Region;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.RandomUtils;
import com.osroyale.util.Utility;

import java.util.Optional;

/**
 * Handles the firemaking action.
 *
 * @author Daniel
 */
public final class FiremakingAction extends DestructionSkillAction {
    private final Item item;
    private final FiremakingData firemaking;

    FiremakingAction(Player player, Item item, FiremakingData firemaking) {
        super(player, Optional.empty(), false);
        this.item = item;
        this.firemaking = firemaking;
    }

    @Override
    public boolean canInit() {
        if (getMob().skills.getMaxLevel(Skill.FIREMAKING) < firemaking.getLevel()) {
            getMob().getPlayer().message("You need a firemaking level of " + firemaking.getLevel() + " to light this log!");
            return false;
        }
        if (Area.inEdgeville(getMob()) || Area.inWilderness(getMob())) {
            getMob().getPlayer().message("You can not burn a fire here! ");
            return false;
        }
        Region region = getMob().getRegion();
        if (region.containsObject(getMob().getPosition())) {
            getMob().getPlayer().send(new SendMessage("You can't light a fire here!"));
            return false;
        }
        return true;
    }

    @Override
    public Optional<SkillAnimation> animation() {
        return Optional.empty();
    }

    @Override
    public double experience() {
        return firemaking.getExperience() * Config.FIREMAKING_MODIFICATION;
    }

    @Override
    public int skill() {
        return Skill.FIREMAKING;
    }

    @Override
    public void init() {
        if (getMob().isPlayer()) {
            getMob().getPlayer().send(new SendMessage("You attempt to light the log."));
        }
        getMob().animate(new Animation(733));
    }

    @Override
    public double successFactor() {
        return 100 - firemaking.getLevel();
    }

    @Override
    public void onDestruct(boolean success) {
        if (success) {
            Player player = (Player) getMob();
            final boolean saveLog = SkillCape.isEquipped(player, SkillCape.FIREMAKING) && Utility.random(1, 3) == 1;
            if (!saveLog) {
                player.inventory.remove(item);
            }
            player.takeStep();

            if (player.prestige.hasPerk(PrestigePerk.FLAME_ON) && RandomUtils.success(.25)) {
                player.inventory.remove(firemaking.getLog(), 1);
                player.skills.addExperience(Skill.FIREMAKING, experience());
            }

            RandomEventHandler.trigger(player);
            player.playerAssistant.activateSkilling(1);
            Pets.onReward(player, PetData.PHOENIX.getItem(), 8562);
            CustomGameObject object = new CustomGameObject(5249, player.getPosition());
            object.register();

            if (firemaking == FiremakingData.WILLOW_LOG) {
                player.forClan(channel -> channel.activateTask(ClanTaskKey.BURN_WILLOW_LOG, getMob().getName()));
            }

            World.schedule(new Task(180) {
                @Override
                public void execute() {
                    GroundItem.createGlobal(player, new Item(592), object.getPosition());
                    object.unregister();
                    cancel();
                }
            });
        }
    }

    @Override
    public void onCancel(boolean logout) {
        Player player = (Player) getMob();
        player.animate(new Animation(65535));
    }

    @Override
    public String getName() {
        return "Firemaking";
    }

    @Override
    public Item destructItem() {
        return item;
    }
}
