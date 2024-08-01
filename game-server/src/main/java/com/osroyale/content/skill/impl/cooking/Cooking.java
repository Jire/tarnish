package com.osroyale.content.skill.impl.cooking;

import com.osroyale.Config;
import com.osroyale.content.activity.randomevent.RandomEventHandler;
import com.osroyale.content.dialogue.ChatBoxItemDialogue;
import com.osroyale.content.event.impl.ItemOnObjectInteractionEvent;
import com.osroyale.content.skillcape.SkillCape;
import com.osroyale.game.Animation;
import com.osroyale.game.action.SkillIdAction;
import com.osroyale.game.action.policy.WalkablePolicy;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.ItemDefinition;
import com.osroyale.game.world.object.GameObject;
import com.osroyale.net.packet.out.SendInputAmount;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.Utility;

import java.util.Arrays;

/**
 * Handles the cooking skill.
 * 
 * @author Daniel
 */
public class Cooking extends Skill {

	private transient final String[] objects = { "range", "fire", "oven", "stove", "cooking range", "fireplace" };

	public Cooking(int level, double experience) {
		super(Skill.COOKING, level, experience);
	}

	private boolean cookableObject(GameObject object) {
		String name = object.getDefinition().getName().toLowerCase();
		return Arrays.stream(objects).anyMatch(name::contains);
	}

	private boolean success(Player player, int level, int noBurn) {
		if (SkillCape.isEquipped(player, SkillCape.COOKING) || player.skills.getLevel(Skill.COOKING) >= noBurn || player.equipment.contains(775)) {
			return true;
		}


		int burn_bonus = 3;
		double burn_chance = (45.0 - burn_bonus);
		double cook_level = player.skills.getLevel(Skill.COOKING);
		double multi_a = ((double) noBurn - (double) level);
		double burn_dec = (burn_chance / multi_a);
		double multi_b = (cook_level - (double) level);
		burn_chance -= (multi_b * burn_dec);
		double random_number = Utility.random(100);
		return burn_chance <= random_number;
	}

	@Override
	protected double modifier() {
		return Config.COOKING_MODIFICATION;
	}

	@Override
	protected boolean useItem(Player player, ItemOnObjectInteractionEvent event) {
		Item item = event.getItem();
		GameObject object = event.getObject();

		if (!cookableObject(object)) {
			return false;
		}

		if (!CookData.forId(item.getId()).isPresent()) {
			return false;
		}

		if (player.skills.get(Skill.COOKING).isDoingSkill()) {
			return true;
		}

		CookData cook = CookData.forId(item.getId()).get();

		if (getLevel() < cook.getLevel()) {
			player.dialogueFactory.sendStatement("You need a cooking level of " + cook.getLevel() + " to cook this!").execute();
			return true;
		}


		if (player.inventory.computeAmountForId(item.getId()) == 1) {
			player.action.execute(cook(player, cook, 1), true);
		} else {
			ChatBoxItemDialogue.sendInterface(player, 1746, cook.getCooked(), 0, -5, 170);
			player.chatBoxItemDialogue = new ChatBoxItemDialogue(player) {
				@Override
				public void firstOption(Player player) {
					player.action.execute(cook(player, cook, 1), true);
				}

				@Override
				public void secondOption(Player player) {
					player.action.execute(cook(player, cook, 5), true);
				}

				@Override
				public void thirdOption(Player player) {
					player.send(new SendInputAmount("Enter amount of fish you would like to cook", 10, input -> player.action.execute(cook(player, cook, Integer.parseInt(input)), true)));
				}

				@Override
				public void fourthOption(Player player) {
					player.action.execute(cook(player, cook, 28), true);
				}
			};
		}
		return true;
	}

	private SkillIdAction<Player> cook(Player player, CookData cook, int amount) {
		return new SkillIdAction<>(player, 3, true, Skill.COOKING) {
            int ticks = 0;

            @Override
            public void execute() {
                if (!player.skills.get(Skill.COOKING).isDoingSkill()) {
                    cancel();
                    return;
                }

                if (!player.inventory.contains(cook.getItem())) {
                    cancel();
                    player.send(new SendMessage("<col=369>You have run out of materials."));
                    return;
                }
                player.animate(new Animation(883));
                String name = ItemDefinition.get(cook.getCooked()).getName();
                player.inventory.remove(cook.getItem(), 1);

                if (success(player, cook.getLevel(), cook.getNoBurn())) {
                    player.inventory.add(cook.getCooked(), 1);
                    player.skills.addExperience(Skill.COOKING, cook.getExp() * modifier());
                    player.send(new SendMessage("You successfully cook the " + name + ".", true));
                    player.playerAssistant.activateSkilling(1);
                    RandomEventHandler.trigger(player);
                } else {
                    player.inventory.add(cook.getBurnt(), 1);
                    player.send(new SendMessage("You accidently burn the " + name + ".", true));
                }

                if (++ticks == amount) {
                    cancel();
                }
            }

            @Override
            public void onCancel(boolean logout) {
                player.resetFace();
                player.skills.get(Skill.COOKING).setDoingSkill(false);
            }

            @Override
            public String getName() {
                return "Cooking";
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
