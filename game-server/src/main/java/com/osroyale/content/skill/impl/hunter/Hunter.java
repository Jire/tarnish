package com.osroyale.content.skill.impl.hunter;

import com.osroyale.Config;
import com.osroyale.content.event.impl.ItemInteractionEvent;
import com.osroyale.content.event.impl.NpcInteractionEvent;
import com.osroyale.content.event.impl.ObjectInteractionEvent;
import com.osroyale.content.skill.impl.hunter.net.ImplingReward;
import com.osroyale.content.skill.impl.hunter.net.PuroPuroAction;
import com.osroyale.content.skill.impl.hunter.net.impl.Butterfly;
import com.osroyale.content.skill.impl.hunter.net.impl.Impling;
import com.osroyale.content.skill.impl.hunter.net.Netting;
import com.osroyale.content.skill.impl.hunter.trap.BoxTrap;
import com.osroyale.content.skill.impl.hunter.trap.SnareTrap;
import com.osroyale.content.skill.impl.hunter.trap.Trap;
import com.osroyale.content.skill.impl.hunter.trap.TrapManager;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.object.CustomGameObject;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.Utility;

/**
 * Handles the hunter skill.
 *
 * @author Daniel
 */
public class Hunter extends Skill {

    /** Creates a new <code>Hunter<code> */
    public Hunter(int level, double experience) {
        super(Skill.HUNTER, level, experience);
    }

    @Override
    protected double modifier() {
        return Config.HUNTER_MODIFICATION;
    }

    @Override
    protected boolean clickObject(Player player, ObjectInteractionEvent event) {
        if (event.getOpcode() == 0) {
            switch (event.getObject().getId()) {
                case 25016:
                case 25029:
                    player.action.execute(new PuroPuroAction(player, event.getObject()));
                    return true;
                case 19187:
                case 19175:
                    TrapManager.dismantle(player, event.getObject());
                    return true;
                case 19191:
                case 19189:
                case 19180:
                case 19184:
                case 19182:
                case 19178:
                    TrapManager.lootTrap(player, event.getObject());
                    return true;
            }
        }
        return false;
    }

    @Override
    protected boolean clickNpc(Player player, NpcInteractionEvent event) {
        if (event.getOpcode() != 0) {
            return false;
        }

        Npc npc = event.getNpc();

        if (!npc.isVisible()) {
            return false;
        }

        if (Impling.forId(npc.id).isPresent()) {
            Impling imp = Impling.forId(npc.id).get();
            World.schedule(new Netting(player, npc, imp.experience, imp.level, 11260, imp.reward));
            return true;
        } else if (Butterfly.forId(npc.id).isPresent()) {
            Butterfly fly = Butterfly.forId(npc.id).get();
            World.schedule(new Netting(player, npc, fly.experience, fly.level, 10012, fly.reward));
            return true;
        }

        return false;
    }

    @Override
    protected boolean clickItem(Player player, ItemInteractionEvent event) {
        Item item = event.getItem();

        if (event.getOpcode() == 0) {
            switch (item.getId()) {
                case 10006:
                    TrapManager.layTrap(player, new SnareTrap(new CustomGameObject(19175, new Position(player.getX(), player.getY(), player.getHeight())), Trap.TrapState.SET, 200, player));
                    return true;
                case 10008:
                    TrapManager.layTrap(player, new BoxTrap(new CustomGameObject(19187, new Position(player.getX(), player.getY(), player.getHeight())), Trap.TrapState.SET, 200, player));
                    return true;
            }
        }

        if (event.getOpcode() != 1) {
            return false;
        }

        if (!ImplingReward.forId(item.getId()).isPresent()) {
            return false;
        }

        ImplingReward impling = ImplingReward.forId(item.getId()).get();
        Item reward = Utility.randomElement(impling.getLootation());

        if (player.inventory.getFreeSlots() == 0) {
            player.send(new SendMessage("You do not have enough inventory space to enter this."));
            return true;
        }

        boolean shatter = Utility.random(10) == 1;
        player.inventory.remove(item);

        if (!shatter) {
            player.inventory.add(11260, 1);
            player.send(new SendMessage("You successfully open the jar.", true));
        } else {
            player.send(new SendMessage("The jar breaks as you open the jar, cutting you a bit.", true));
            player.damage(new Hit(Utility.random(3)));
        }

        player.inventory.add(reward);
        return true;
    }
}
