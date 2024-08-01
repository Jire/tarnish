package com.osroyale.content.skill.impl.prayer;

import com.osroyale.Config;
import com.osroyale.content.skillcape.SkillCape;
import com.osroyale.content.activity.randomevent.RandomEventHandler;
import com.osroyale.content.dialogue.ChatBoxItemDialogue;
import com.osroyale.content.event.impl.ItemInteractionEvent;
import com.osroyale.content.event.impl.ItemOnObjectInteractionEvent;
import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.action.Action;
import com.osroyale.game.action.impl.BuryBoneAction;
import com.osroyale.game.action.impl.ScatterAshAction;
import com.osroyale.game.action.policy.WalkablePolicy;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.ItemDefinition;
import com.osroyale.game.world.object.GameObject;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.packet.out.SendInputAmount;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.Utility;

/**
 * Handles sacrificing bones to an altar.
 *
 * @author Daniel
 */
public class BoneSacrifice extends Skill {

    public BoneSacrifice(int level, double experience) {
        super(Skill.PRAYER, level, experience);
    }

    @Override
    protected double modifier() {
        return Config.PRAYER_MODIFICATION;
    }

    @Override
    protected boolean clickItem(Player player, ItemInteractionEvent event) {
        if (event.getOpcode() != 0)
            return false;
        Item item = event.getItem();
        int slot = event.getSlot();
        if (!BoneData.forId(item.getId()).isPresent() && !AshData.forId(item.getId()).isPresent())
            return false;
        if (AshData.forId(item.getId()).isPresent()) {
            AshData ashes = AshData.forId(item.getId()).get();
            new ScatterAshAction(player, ashes, slot).start();
            return true;
        }
        BoneData bone = BoneData.forId(item.getId()).get();
        new BuryBoneAction(player, bone, slot).start();
        return true;
    }

    @Override
    protected boolean useItem(Player player, ItemOnObjectInteractionEvent event) {
        Item item = event.getItem();
        GameObject object = event.getObject();
        if (object.getId() != 409 && object.getId() != 411) {
            return false;
        }
        if (!BoneData.forId(item.getId()).isPresent())
            return false;
        BoneData bone = BoneData.forId(item.getId()).get();

        if (player.inventory.computeAmountForId(bone.getId()) == 0) {
            player.action.execute(sacrifice(player, object.getPosition(), bone, 1), true);
        } else {
            ChatBoxItemDialogue.sendInterface(player, 1746, bone.getId(), 0, -5, 170);
            player.chatBoxItemDialogue = new ChatBoxItemDialogue(player) {
                @Override
                public void firstOption(Player player) {
                    player.action.execute(sacrifice(player, object.getPosition(), bone, 1), true);
                }

                @Override
                public void secondOption(Player player) {
                    player.action.execute(sacrifice(player, object.getPosition(), bone, 5), true);
                }

                @Override
                public void thirdOption(Player player) {
                    player.send(new SendInputAmount("Enter the amount of bones you would like to sacrifice", 10, input -> player.action.execute(sacrifice(player, object.getPosition(), bone, Integer.parseInt(input)), true)));
                }

                @Override
                public void fourthOption(Player player) {
                    player.action.execute(sacrifice(player, object.getPosition(), bone, 28), true);
                }
            };
        }
        return true;
    }

    private Action<Player> sacrifice(Player player, Position position, BoneData bone, int amount) {
        return new Action<Player>(player, 4, true) {
            int ticks = 0;

            @Override
            public void execute() {
                if (!player.inventory.contains(bone.getId())) {
                    cancel();
                    return;
                }
                Graphic graphic = new Graphic(624, true);
                World.sendGraphic(graphic, position, player.instance);
                player.animate(new Animation(645, 5));
                if (player.getPosition().inLocation(Position.create(2946, 3816), Position.create(2959, 3831), true)) {
                    int rnd = Utility.random(100);
                    if (rnd <= 50) {
                        player.send(new SendMessage("You sacrifice the " + ItemDefinition.get(bone.getId()).getName() + " ."));
                        player.inventory.remove(bone.getId(), 1);
                    }
                    if (rnd > 50) {
                        player.message("You've saved a bone due to sacrificing in the wilderness..");
                    }
                } else {
                    player.inventory.remove(bone.getId(), 1);
                }

                double exp = bone.getExperience() * (modifier() * 1.8);
                if (SkillCape.isEquipped(player, SkillCape.PRAYER)) {
                    exp *= 2.0;
                }

                player.skills.addExperience(Skill.PRAYER, exp);
                RandomEventHandler.trigger(player);
                if (++ticks == amount) {
                    cancel();
                }
            }

            @Override
            public String getName() {
                return "Bone sacrifice";
            }

            @Override
            public boolean prioritized() {
                return false;
            }

            @Override
            public WalkablePolicy getWalkablePolicy() {
                return WalkablePolicy.NON_WALKABLE;
            }
        };
    }

}
