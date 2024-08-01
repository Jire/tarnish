package com.osroyale.content.skill.impl.thieving;

import com.osroyale.Config;
import com.osroyale.game.Animation;
import com.osroyale.game.action.Action;
import com.osroyale.game.action.policy.WalkablePolicy;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.mob.data.LockType;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.position.Area;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.Utility;

/**
 * Handles the pickpocketing action.
 *
 * @author Daniel
 */
public final class PickpocketAction extends Action<Player> {
    /** The pickpocket data. */
    private final PickpocketData pickpocket;

    /** The npc being pickpocketed. */
    private final Npc npc;

    /** Constructs a new <code>PickpocketData</code>. */
    public PickpocketAction(Player player, Npc npc, PickpocketData pickpocket) {
        super(player, 3);
        this.npc = npc;
        this.pickpocket = pickpocket;
    }

    /** The failure rate for pickpocketing. */
    private int failureRate(Player player) {
        double f1 = pickpocket.getLevel() / 10;
        double f2 = 100 / ((player.skills.getMaxLevel(Skill.THIEVING) + 1) - pickpocket.getLevel());
        return (int) Math.floor((f2 + f1) / 2);
    }

    @Override
    public void execute() {
        boolean failed = Utility.random(100) < failureRate(getMob());

        if (failed) {
            npc.interact(getMob());
            npc.face(npc.faceDirection);
            npc.animate(new Animation(422));
            npc.speak("What do you think you're doing?");
            getMob().action.clearNonWalkableActions();
            getMob().damage(new Hit(Utility.random(pickpocket.getDamage())));
            getMob().locking.lock(pickpocket.getStun(), LockType.STUN);
            getMob().send(new SendMessage("You failed to pickpocketing the " + npc.getName() + "."));
            cancel();
            return;
        }

        double experience = Area.inSuperDonatorZone(getMob()) || Area.inRegularDonatorZone(getMob()) ? pickpocket.getExperience() * 2 : pickpocket.getExperience();
        getMob().skills.addExperience(Skill.THIEVING, experience * Config.THIEVING_MODIFICATION);
        getMob().send(new SendMessage("You have successfully pickpocket the " + npc.getName() + "."));
        getMob().inventory.add(Utility.randomElement(pickpocket.getLoot()));
        getMob().locking.unlock();
        cancel();
    }

    @Override
    public String getName() {
        return "Thieving pickpocket";
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