package com.osroyale.game.action.impl;

import com.osroyale.Config;
import com.osroyale.content.skill.impl.prayer.AshData;
import com.osroyale.content.skillcape.SkillCape;
import com.osroyale.content.achievement.AchievementHandler;
import com.osroyale.content.achievement.AchievementKey;
import com.osroyale.content.skill.SkillAction;
import com.osroyale.content.skill.impl.prayer.BoneData;
import com.osroyale.game.Animation;
import com.osroyale.game.action.policy.WalkablePolicy;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.net.packet.out.SendMessage;

import java.util.Optional;

public final class ScatterAshAction extends SkillAction {
    private final int slot;
    private final Item item;
    private final AshData ashes;

    public ScatterAshAction(Player player, AshData ashes, int slot) {
        super(player, Optional.empty(), true);
        this.slot = slot;
        this.ashes = ashes;
        this.item = player.inventory.get(slot);
    }

    @Override
    public boolean canInit() {
        return getMob().skills.getSkills()[skill()].stopwatch.elapsed(1200);
    }

    @Override
    public void init() {

    }

    @Override
    public void onExecute() {
        getMob().animate(new Animation(2295));
        Player player = getMob().getPlayer();
        player.inventory.remove(item, slot, true);
        player.skills.addExperience(skill(), experience());
        player.send(new SendMessage("You scatter the ashes."));

        cancel();
    }

    @Override
    public void onCancel(boolean logout) {
        getMob().skills.getSkills()[skill()].stopwatch.reset();
    }

    @Override
    public Optional<SkillAnimation> animation() {
        return Optional.empty();
    }

    @Override
    public double experience() {
        double exp = (ashes.getExperience() * Config.PRAYER_MODIFICATION);
        if (SkillCape.isEquipped(getMob().getPlayer(), SkillCape.PRAYER)) {
            exp *= 2.0;
        }
        return exp;
    }

    @Override
    public int skill() {
        return Skill.PRAYER;
    }

    @Override
    public String getName() {
        return "Ash scatter";
    }

    @Override
    public boolean prioritized() {
        return false;
    }

    @Override
    public WalkablePolicy getWalkablePolicy() {
        return WalkablePolicy.NON_WALKABLE;
    }
}