package plugin.click.object.birdhouse;

import com.osroyale.content.skill.impl.hunter.birdhouse.BirdhouseData;
import com.osroyale.content.skill.impl.hunter.birdhouse.BirdhouseSeedData;
import com.osroyale.content.skill.impl.hunter.birdhouse.Birdhouses;
import com.osroyale.content.skill.impl.hunter.birdhouse.PlayerBirdHouseData;
import com.osroyale.content.skill.impl.hunter.birdhouse.action.FillBirdhouse;
import com.osroyale.content.skill.impl.hunter.birdhouse.action.PlaceBirdhouse;
import com.osroyale.game.event.impl.ItemOnObjectEvent;
import com.osroyale.game.event.impl.ObjectClickEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.persist.PlayerSerializer;
import com.osroyale.game.world.entity.skill.SkillData;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.object.CustomGameObject;
import com.osroyale.game.world.object.GameObject;
import com.osroyale.game.world.object.ObjectDirection;
import com.osroyale.game.world.object.ObjectType;
import com.osroyale.net.packet.out.SendAddObject;

public class BirdhouseInteractionPlugin extends PluginContext {

    @Override
    protected boolean itemOnObject(Player player, ItemOnObjectEvent event) {
        final GameObject gameObject = event.getObject();
        final Item itemUsed = event.getUsed();

        BirdhouseSeedData seedData = BirdhouseSeedData.forId(itemUsed.getId());
        if(seedData == null) return false;

        if(gameObject.getId() >= 30565 && gameObject.getId() <= 30568) {
            boolean foundData = false;
            PlayerBirdHouseData birdHouseData = null;
            for (PlayerBirdHouseData playerBirdHouseData : player.birdHouseData) {
                if (playerBirdHouseData.birdhousePosition.equals(gameObject.getPosition())) {
                    birdHouseData = playerBirdHouseData;
                    foundData = true;
                    break;
                }
            }

            if (foundData) {
                int requiredAmount = seedData.seedAmount;

                if(birdHouseData.seedAmount >= 10) return false;

                int requiredSeeds = requiredAmount == 5 ? requiredAmount - (birdHouseData.seedAmount / 2) : requiredAmount - birdHouseData.seedAmount;
                int itemAmount = player.inventory.computeAmountForId(itemUsed.getId()) > 10 ? 10 : player.inventory.computeAmountForId(itemUsed.getId());

                if(itemAmount > requiredSeeds)
                    itemAmount = requiredSeeds;

                if(requiredAmount == 5)
                    itemAmount *= 2;

                player.animate(827);
                player.action.execute(new FillBirdhouse(player, birdHouseData, itemUsed, itemAmount));

                return true;
            }
        }

        return false;
    }

    @Override
    protected boolean thirdClickObject(Player player, ObjectClickEvent event) {
        final GameObject gameObject = event.getObject();
        final int objectId = gameObject.getId();

        if(objectId >= 30565 && objectId <= 30568) {

            boolean foundData = false;
            PlayerBirdHouseData birdHouseData = null;
            for(PlayerBirdHouseData playerBirdHouseData : player.birdHouseData) {
                if(playerBirdHouseData.birdhousePosition.equals(gameObject.getPosition())) {
                    birdHouseData = playerBirdHouseData;
                    foundData = true;
                    break;
                }
            }

            if(foundData) {
                if(birdHouseData.seedAmount < 10 || birdHouseData.seedAmount >= 10 && birdHouseData.birdhouseTimer > System.currentTimeMillis()) {
                    PlayerBirdHouseData finalBirdHouseData = birdHouseData;
                    player.dialogueFactory.sendOption("Yes - dismantle and <col=800000>discard the birdhouse and contents</col>.", () -> {
                        player.animate(827);
                        player.send(new SendAddObject(new CustomGameObject(finalBirdHouseData.oldObjectId, finalBirdHouseData.birdhousePosition, ObjectDirection.valueOf(finalBirdHouseData.rotation).get(), ObjectType.valueOf(finalBirdHouseData.type).get())));
                        player.birdHouseData.remove(finalBirdHouseData);
                        PlayerSerializer.save(player);
                    }, "No, leave it be.", player.dialogueFactory::clear).execute();
                    return true;
                } else {
                    Birdhouses.receiveLoot(player, birdHouseData);
                    return true;
                }
            }

        }

        return false;
    }

    @Override
    protected boolean secondClickObject(Player player, ObjectClickEvent event) {
        final GameObject gameObject = event.getObject();
        final int objectId = gameObject.getId();

        if(objectId >= 30565 && objectId <= 30568) {

            boolean foundData = false;
            PlayerBirdHouseData birdHouseData = null;
            for(PlayerBirdHouseData playerBirdHouseData : player.birdHouseData) {
                if(playerBirdHouseData.birdhousePosition.equals(gameObject.getPosition())) {
                    birdHouseData = playerBirdHouseData;
                    foundData = true;
                    break;
                }
            }

            if(foundData) {
                if(System.currentTimeMillis() > birdHouseData.birdhouseTimer && birdHouseData.birdhouseTimer != 0)
                    player.dialogueFactory.sendStatement("Your birdhouse trap is ready to be collected.").execute();
                else if(birdHouseData.seedAmount < 10)
                    player.dialogueFactory.sendStatement("Your birdhouse seed level is: "+birdHouseData.seedAmount+"/10. <col=800000>It must be full of seed before it", "<col=800000>will start catching birds.").execute();
                else
                    player.dialogueFactory.sendStatement("Your birdhouse trap is set and consuming seed.", "Ready in " + Birdhouses.getTimeLeft(birdHouseData)).execute();
                return true;
            }

        }

        return false;
    }

    @Override
    protected boolean firstClickObject(Player player, ObjectClickEvent event) {
        final GameObject gameObject = event.getObject();
        final int objectId = gameObject.getId();

        if(objectId >= 30565 && objectId <= 30568) {

            boolean foundData = false;
            PlayerBirdHouseData birdHouseData = null;
            for(PlayerBirdHouseData playerBirdHouseData : player.birdHouseData) {
                if(playerBirdHouseData.birdhousePosition.equals(gameObject.getPosition())) {
                    birdHouseData = playerBirdHouseData;
                    foundData = true;
                    break;
                }
            }

            if(foundData) {
                PlayerBirdHouseData finalBirdHouseData = birdHouseData;
                player.dialogueFactory.sendOption("Check seed levels.", () -> {
                    if(System.currentTimeMillis() > finalBirdHouseData.birdhouseTimer && finalBirdHouseData.birdhouseTimer != 0)
                        player.dialogueFactory.sendStatement("Your birdhouse trap is ready to be collected.");
                    else if(finalBirdHouseData.seedAmount < 10)
                        player.dialogueFactory.sendStatement("Your birdhouse seed level is: "+finalBirdHouseData.seedAmount+"/10. <col=800000>It must be full of seed before it", "<col=800000>will start catching birds.");
                    else
                        player.dialogueFactory.sendStatement("Your birdhouse trap is set and consuming seed.", "Ready in " + Birdhouses.getTimeLeft(finalBirdHouseData));
                }, "Dismantle my trap.", () -> {
                    if(finalBirdHouseData.seedAmount < 10 || finalBirdHouseData.seedAmount >= 10 && finalBirdHouseData.birdhouseTimer > System.currentTimeMillis()) {
                        player.dialogueFactory.sendOption("Yes - dismantle and <col=800000>discard the birdhouse and contents</col>.", () -> {
                            player.animate(827);
                            player.send(new SendAddObject(new CustomGameObject(finalBirdHouseData.oldObjectId, finalBirdHouseData.birdhousePosition, ObjectDirection.valueOf(finalBirdHouseData.rotation).get(), ObjectType.valueOf(finalBirdHouseData.type).get())));
                            player.birdHouseData.remove(finalBirdHouseData);
                            PlayerSerializer.save(player);
                        }, "No, leave it be.", player.dialogueFactory::clear);
                    } else
                        Birdhouses.receiveLoot(player, finalBirdHouseData);
                }, "Do nothing.", player.dialogueFactory::clear).execute();
            } else {
                BirdhouseData birdhouseData = null;
                for (int index = BirdhouseData.values().length - 1; index >= 0; index--) {
                    BirdhouseData data = BirdhouseData.values()[index];
                    if (player.inventory.contains(data.birdHouseId) && player.skills.getLevel(SkillData.HUNTER.ordinal()) >= data.hunterData[0]) {
                        birdhouseData = data;
                        break;
                    }
                }
                if (birdhouseData != null) {
                    player.animate(827);
                    player.action.execute(new PlaceBirdhouse(player, birdhouseData, gameObject));
                    return true;
                } else {
                    player.message("You do not have a birdhouse with the required hunter level to build here.");
                    return true;
                }
            }
        }

        return false;
    }

}
