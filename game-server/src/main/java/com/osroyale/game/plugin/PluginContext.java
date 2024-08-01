package com.osroyale.game.plugin;

import com.osroyale.Config;
import com.osroyale.content.DropDisplay;
import com.osroyale.content.ProfileViewer;
import com.osroyale.content.achievement.AchievementWriter;
import com.osroyale.content.activity.ActivityType;
import com.osroyale.content.simulator.DropSimulator;
import com.osroyale.content.skill.impl.slayer.SlayerTab;
import com.osroyale.content.tittle.TitleManager;
import com.osroyale.content.writer.InterfaceWriter;
import com.osroyale.content.writer.impl.InformationWriter;
import com.osroyale.game.event.Event;
import com.osroyale.game.event.impl.*;
import com.osroyale.game.event.impl.log.CommandLogEvent;
import com.osroyale.game.event.impl.log.PickupItemLogEvent;
import com.osroyale.game.event.listener.PlayerEventListener;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.entity.mob.player.command.CommandParser;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.net.packet.out.SendURL;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The base class that all plugins should extend.
 * This class allows plugins to have many different behaviors
 * and listen to all types of events.
 *
 * @author nshusa
 */
public class PluginContext implements PlayerEventListener {

    public static final Logger logger = LogManager.getLogger();

    public void onInit() {

    }

    @Override
    public boolean accept(Player player, Event event) {
        try {
            if (event instanceof ButtonClickEvent) {
                return handleButtonClickEvent(player, (ButtonClickEvent) event);
            } else if (event instanceof ItemClickEvent) {
                return handleItemClickEvent(player, (ItemClickEvent) event);
            } else if (event instanceof NpcClickEvent) {
                return handleNpcClickEvent(player, (NpcClickEvent) event);
            } else if (event instanceof ObjectClickEvent) {
                return handleObjectClickEvent(player, (ObjectClickEvent) event);
            } else if (event instanceof ItemOnItemEvent) {
                return handleItemOnItemEvent(player, (ItemOnItemEvent) event);
            } else if (event instanceof ItemOnNpcEvent) {
                return handleItemOnNpcEvent(player, (ItemOnNpcEvent) event);
            } else if (event instanceof ItemOnObjectEvent) {
                return handleItemOnObjectEvent(player, (ItemOnObjectEvent) event);
            } else if (event instanceof ItemOnPlayerEvent) {
                return handleItemOnPlayerEvent(player, (ItemOnPlayerEvent) event);
            } else if (event instanceof CommandEvent) {
                return handleCommandEvent(player, (CommandEvent) event);
            } else if (event instanceof ItemContainerContextMenuEvent) {
                return handleItemContainerContextMenuEvent(player, (ItemContainerContextMenuEvent) event);
            } else if (event instanceof DropItemEvent) {
                return handleDropItemEvent(player, (DropItemEvent) event);
            } else if (event instanceof PickupItemEvent) {
                return handlePickupItemEvent(player, (PickupItemEvent) event);
            } else if (event instanceof MovementEvent) {
                return onMovement(player, (MovementEvent) event);
            }
        } catch (Exception ex) {
            logger.error(String.format("player=%s error while handling event: %s", player.getName(), this.getClass().getSimpleName()), ex);
        }

        return true;
    }

    protected boolean onMovement(Player player, MovementEvent event) {
        return false;
    }

    private boolean handlePickupItemEvent(Player player, PickupItemEvent event) {
        if (onPickupItem(player, event)) {
            logger.log(Level.INFO, String.format("player=%s picked up item=%d amount=%d position=%s", player.getName(), event.getItem().getId(), event.getItem().getAmount(), event.getPosition()));
            if (PlayerRight.isDeveloper(player)) {
                player.send(new SendMessage(String.format("[%s]: item=%d position=%s", this.getClass().getSimpleName(), event.getItem().getId(), event.getPosition().toString())));
            }
            World.getDataBus().publish(new PickupItemLogEvent(player, event.getGroundItem()));
            return true;
        }
        return false;
    }

    protected boolean onPickupItem(Player player, PickupItemEvent event) {
        return false;
    }

    private boolean handleDropItemEvent(Player player, DropItemEvent event) {
        if (onDropItem(player, event)) {
            logger.log(Level.INFO, String.format("player=%s dropped item=%d amount=%d slot=%d position=%s", player.getName(), event.getItem().getId(), event.getItem().getAmount(), event.getSlot(), event.getPosition()));
            if (PlayerRight.isDeveloper(player)) {
                player.send(new SendMessage(String.format("[%s]: item=%d amount=%d slot=%d position=%s", this.getClass().getSimpleName(), event.getItem().getId(), event.getItem().getAmount(), event.getSlot(), event.getPosition())));
            }
            return true;
        }
        return false;
    }

    protected boolean onDropItem(Player player, DropItemEvent event) {
        return false;
    }

    private boolean handleItemContainerContextMenuEvent(Player player, ItemContainerContextMenuEvent event) {
            switch(event.getType()) {
                case 1:
                    if (firstClickItemContainer(player, event)) {
                        logger.log(Level.INFO, String.format("[%s, type=%d]: first item interfaceId=%d (removeId=%d, removeSlot=%d)", this.getClass().getSimpleName(), event.getType(), event.getInterfaceId(), event.getRemoveId(), event.getRemoveSlot()));
                        if (PlayerRight.isDeveloper(player)) {
                            player.send(new SendMessage(String.format("[%s, type=%d]: interfaceId=%d (removeId=%d, removeSlot=%d)", this.getClass().getSimpleName(), event.getType(), event.getInterfaceId(), event.getRemoveId(), event.getRemoveSlot())));
                        }
                        return true;
                    }
                    break;

                case 2:
                    if (secondClickItemContainer(player, event)) {
                        logger.log(Level.INFO, String.format("[%s, type=%d]: second item interfaceId=%d (removeId=%d, removeSlot=%d)", this.getClass().getSimpleName(), event.getType(), event.getInterfaceId(), event.getRemoveId(), event.getRemoveSlot()));

                        if (PlayerRight.isDeveloper(player)) {
                            player.send(new SendMessage(String.format("[%s, type=%d]: interfaceId=%d (removeId=%d, removeSlot=%d)", this.getClass().getSimpleName(), event.getType(), event.getInterfaceId(), event.getRemoveId(), event.getRemoveSlot())));
                        }
                        return true;
                    }
                    break;

                case 3:
                    if (thirdClickItemContainer(player, event)) {
                        logger.log(Level.INFO, String.format("[%s, type=%d]: third item interfaceId=%d (removeId=%d, removeSlot=%d)", this.getClass().getSimpleName(), event.getType(), event.getInterfaceId(), event.getRemoveId(), event.getRemoveSlot()));

                        if (PlayerRight.isDeveloper(player)) {
                            player.send(new SendMessage(String.format("[%s, type=%d]: interfaceId=%d (removeId=%d, removeSlot=%d)", this.getClass().getSimpleName(), event.getType(), event.getInterfaceId(), event.getRemoveId(), event.getRemoveSlot())));
                        }
                        return true;
                    }
                    break;

                case 4:
                    if (fourthClickItemContainer(player, event)) {
                        logger.log(Level.INFO, String.format("[%s, type=%d]: fourth item interfaceId=%d (removeId=%d, removeSlot=%d)", this.getClass().getSimpleName(), event.getType(), event.getInterfaceId(), event.getRemoveId(), event.getRemoveSlot()));

                        if (PlayerRight.isDeveloper(player)) {
                            player.send(new SendMessage(String.format("[%s, type=%d]: interfaceId=%d (removeId=%d, removeSlot=%d)", this.getClass().getSimpleName(), event.getType(), event.getInterfaceId(), event.getRemoveId(), event.getRemoveSlot())));
                        }
                        return true;
                    }
                    break;

                case 5:
                    if (fifthClickItemContainer(player, event)) {
                        logger.log(Level.INFO, String.format("[%s, type=%d]: fifth item interfaceId=%d (removeId=%d, removeSlot=%d)", this.getClass().getSimpleName(), event.getType(), event.getInterfaceId(), event.getRemoveId(), event.getRemoveSlot()));

                        if (PlayerRight.isDeveloper(player) ) {
                            player.send(new SendMessage(String.format("[%s, type=%d]: interfaceId=%d (removeId=%d, removeSlot=%d)", this.getClass().getSimpleName(), event.getType(), event.getInterfaceId(), event.getRemoveId(), event.getRemoveSlot())));
                        }
                        return true;
                    }
                    break;

                case 6:
                    if (sixthClickItemContainer(player, event)) {
                        logger.log(Level.INFO, String.format("[%s, type=%d]: sixth item interfaceId=%d (removeId=%d, removeSlot=%d)", this.getClass().getSimpleName(), event.getType(), event.getInterfaceId(), event.getRemoveId(), event.getRemoveSlot()));

                        if (PlayerRight.isDeveloper(player) ) {
                            player.send(new SendMessage(String.format("[%s, type=%d]: interfaceId=%d (removeId=%d, removeSlot=%d)", this.getClass().getSimpleName(), event.getType(), event.getInterfaceId(), event.getRemoveId(), event.getRemoveSlot())));
                        }
                        return true;
                    }
                    break;

                case 7:
                    if (allButOneItemContainer(player, event)) {
                        logger.log(Level.INFO, String.format("[%s, type=%d]: seventh item interfaceId=%d (removeId=%d, removeSlot=%d)", this.getClass().getSimpleName(), event.getType(), event.getInterfaceId(), event.getRemoveId(), event.getRemoveSlot()));

                        if (PlayerRight.isDeveloper(player)) {
                            player.send(new SendMessage(String.format("[%s, type=%d]: interfaceId=%d (removeId=%d, removeSlot=%d)", this.getClass().getSimpleName(), event.getType(), event.getInterfaceId(), event.getRemoveId(), event.getRemoveSlot())));
                        }
                        return true;
                    }
                        break;

                case 8:
                    if (modifiableXItemContainer(player, event)) {
                        logger.log(Level.INFO, String.format("[%s, type=%d]: eight item interfaceId=%d (removeId=%d, removeSlot=%d)", this.getClass().getSimpleName(), event.getType(), event.getInterfaceId(), event.getRemoveId(), event.getRemoveSlot()));

                        if (PlayerRight.isDeveloper(player) ) {
                            player.send(new SendMessage(String.format("[%s, type=%d]: interfaceId=%d (removeId=%d, removeSlot=%d)", this.getClass().getSimpleName(), event.getType(), event.getInterfaceId(), event.getRemoveId(), event.getRemoveSlot())));
                        }
                        return true;
                    }
                    break;
            }
            return false;
    }

    protected boolean firstClickItemContainer(Player player, ItemContainerContextMenuEvent event) {
        return false;
    }

    protected boolean secondClickItemContainer(Player player, ItemContainerContextMenuEvent event) {
        return false;
    }

    protected boolean thirdClickItemContainer(Player player, ItemContainerContextMenuEvent event) {
        return false;
    }

    protected boolean fourthClickItemContainer(Player player, ItemContainerContextMenuEvent event) {
        return false;
    }

    protected boolean fifthClickItemContainer(Player player, ItemContainerContextMenuEvent event) {
        return false;
    }

    protected boolean sixthClickItemContainer(Player player, ItemContainerContextMenuEvent event) {
        return false;
    }

    protected boolean allButOneItemContainer(Player player, ItemContainerContextMenuEvent event) {
        return false;
    }

    protected boolean modifiableXItemContainer(Player player, ItemContainerContextMenuEvent event) {
        return false;
    }

    private boolean handleCommandEvent(Player player, CommandEvent event) {
        if (handleCommand(player, event.getParser())) {
            if (Config.LOG_PLAYER) {
                World.getDataBus().publish(new CommandLogEvent(player, event.getParser()));
            }
            return true;
        }
        return false;
    }

    protected boolean handleCommand(Player player, CommandParser parser) {
        return false;
    }

    private boolean handleItemOnPlayerEvent(Player player, ItemOnPlayerEvent event) {
        if (itemOnPlayer(player, event)) {
            if (player.debug) {
                player.send(new SendMessage(String.format("[%s]: item=%d slot=%d player=%s", this.getClass().getSimpleName(), event.getUsed().getId(), event.getSlot(), event.getOther().getName())));
            }
            return true;
        }
        return false;
    }

    protected boolean itemOnPlayer(Player player, ItemOnPlayerEvent event) {
        return false;
    }

    private boolean handleItemOnObjectEvent(Player player, ItemOnObjectEvent event) {
        if (itemOnObject(player, event)) {
            logger.log(Level.INFO, String.format("[%s]: item=%d slot=%d object=%d", this.getClass().getSimpleName(), event.getUsed().getId(), event.getSlot(), event.getObject().getId()));
            if (player.debug) {
                player.send(new SendMessage(String.format("[%s]: item=%d slot=%d object=%d", this.getClass().getSimpleName(), event.getUsed().getId(), event.getSlot(), event.getObject().getId())));
            }
            return true;
        }
        return false;
    }

    protected boolean itemOnObject(Player player, ItemOnObjectEvent event) {
        return false;
    }

    private boolean handleItemOnNpcEvent(Player player, ItemOnNpcEvent event) {
        if (itemOnNpc(player, event)) {
            logger.log(Level.INFO, String.format("[%s]: item=%d slot=%d npc=%d", this.getClass().getSimpleName(), event.getUsed().getId(), event.getSlot(), event.getNpc().id));
            if (player.debug) {
                player.send(new SendMessage(String.format("[%s]: item=%d slot=%d npc=%d", this.getClass().getSimpleName(), event.getUsed().getId(), event.getSlot(), event.getNpc().id)));
            }
            return true;
        }
        return false;
    }

    protected boolean itemOnNpc(Player player, ItemOnNpcEvent event) {
        return false;
    }

    private boolean handleItemOnItemEvent(Player player, ItemOnItemEvent event) {
        if (itemOnItem(player, event)) {
            logger.log(Level.INFO, String.format("[%s]: used=%d usedSlot=%d with=%d withSlot=%d", this.getClass().getSimpleName(), event.getUsed().getId(), event.getUsedSlot(), event.getWith().getId(), event.getWithSlot()));
            if (player.debug) {
                player.send(new SendMessage(String.format("[%s]: used=%d usedSlot=%d with=%d withSlot=%d", this.getClass().getSimpleName(), event.getUsed().getId(), event.getUsedSlot(), event.getWith().getId(), event.getWithSlot())));
            }
            return true;
        }
        return false;
    }

    protected boolean itemOnItem(Player player, ItemOnItemEvent event) {
        return false;
    }

    private boolean handleObjectClickEvent(Player player, ObjectClickEvent event) {
        switch(event.getType()) {
                case 1:
                    if (firstClickObject(player, event)) {
                        logger.log(Level.INFO, String.format("[%s, type=%d]:first click object=%d", this.getClass().getSimpleName(), event.getType(), event.getObject().getId()));
                        if (player.debug) {
                            player.send(new SendMessage(String.format("[%s, type=%d]: object=%d", this.getClass().getSimpleName(), event.getType(), event.getObject().getId())));
                        }
                        return true;
                    }
                    break;

                case 2:
                    if (secondClickObject(player, event)) {
                        logger.log(Level.INFO, String.format("[%s, type=%d]:second click object=%d", this.getClass().getSimpleName(), event.getType(), event.getObject().getId()));
                        if (player.debug) {
                            player.send(new SendMessage(String.format("[%s, type=%d]: object=%d", this.getClass().getSimpleName(), event.getType(), event.getObject().getId())));
                        }
                        return true;
                    }
                    break;

                case 3:
                    if (thirdClickObject(player, event)) {
                        logger.log(Level.INFO, String.format("[%s, type=%d]:third click object=%d", this.getClass().getSimpleName(), event.getType(), event.getObject().getId()));
                        if (player.debug) {
                            player.send(new SendMessage(String.format("[%s, type=%d]: object=%d", this.getClass().getSimpleName(), event.getType(), event.getObject().getId())));
                        }
                        return true;
                    }
                    break;
            }
            return false;
    }

    protected boolean firstClickObject(Player player, ObjectClickEvent event) {
        logger.log(Level.INFO, String.format("[%s, type=%d]:first click object=%d", this.getClass().getSimpleName(), event.getType(), event.getObject().getId()));
        if (player.debug) {
            player.send(new SendMessage(String.format("[%s, type=%d]: object=%d", this.getClass().getSimpleName(), event.getType(), event.getObject().getId())));
        }
        return false;
    }

    protected boolean secondClickObject(Player player, ObjectClickEvent event) {
        logger.log(Level.INFO, String.format("[%s, type=%d]:second click object=%d", this.getClass().getSimpleName(), event.getType(), event.getObject().getId()));

        if (player.debug) {
            player.send(new SendMessage(String.format("[%s, type=%d]: object=%d", this.getClass().getSimpleName(), event.getType(), event.getObject().getId())));
        }
        return false;
    }

    protected boolean thirdClickObject(Player player, ObjectClickEvent event) {
        logger.log(Level.INFO, String.format("[%s, type=%d]:third click object=%d", this.getClass().getSimpleName(), event.getType(), event.getObject().getId()));

        if (player.debug) {
            player.send(new SendMessage(String.format("[%s, type=%d]: object=%d", this.getClass().getSimpleName(), event.getType(), event.getObject().getId())));
        }
        return false;
    }

    private boolean handleNpcClickEvent(Player player, NpcClickEvent event) {
        switch(event.getType()) {
            case 1:

                if (firstClickNpc(player, event)) {
                    logger.log(Level.INFO, String.format("[%s, type=%d]:first click npc=%d", this.getClass().getSimpleName(), event.getType(), event.getNpc().id));
                    if (player.debug) {
                        player.send(new SendMessage(String.format("[%s, type=%d]: npc=%d", this.getClass().getSimpleName(), event.getType(), event.getNpc().id)));
                    }
                    return true;
                }
                break;

            case 2:
                if (secondClickNpc(player, event)) {
                    logger.log(Level.INFO, String.format("[%s, type=%d]:second click npc=%d", this.getClass().getSimpleName(), event.getType(), event.getNpc().id));
                    if (player.debug) {
                        player.send(new SendMessage(String.format("[%s, type=%d]: npc=%d", this.getClass().getSimpleName(), event.getType(), event.getNpc().id)));
                    }
                    return true;
                }
                break;


            case 3:
                if (thirdClickNpc(player, event)) {
                    logger.log(Level.INFO, String.format("[%s, type=%d]:third click npc=%d", this.getClass().getSimpleName(), event.getType(), event.getNpc().id));
                    if (player.debug) {
                        player.send(new SendMessage(String.format("[%s, type=%d]: npc=%d", this.getClass().getSimpleName(), event.getType(), event.getNpc().id)));
                    }
                    return true;
                }
                break;

            case 4:
                if (fourthClickNpc(player, event)) {
                    logger.log(Level.INFO, String.format("[%s, type=%d]:fourth click npc=%d", this.getClass().getSimpleName(), event.getType(), event.getNpc().id));
                    if (player.debug) {
                        player.send(new SendMessage(String.format("[%s, type=%d]: npc=%d", this.getClass().getSimpleName(), event.getType(), event.getNpc().id)));
                    }
                    return true;
                }
                break;
        }

        return false;
    }

    protected boolean firstClickNpc(Player player, NpcClickEvent event) {
        return false;
    }

    protected boolean secondClickNpc(Player player, NpcClickEvent event) {
        return false;
    }

    protected boolean thirdClickNpc(Player player, NpcClickEvent event) {
        return false;
    }

    protected boolean fourthClickNpc(Player player, NpcClickEvent event) {
        return false;
    }

    private boolean handleItemClickEvent(Player player, ItemClickEvent event) {
        switch(event.getType()) {
            case 1:
                if (firstClickItem(player, event)) {
                    logger.log(Level.INFO, String.format("[%s, type=%d]:first click item=%d", this.getClass().getSimpleName(), event.getType(), event.getItem().getId()));
                    if (player.debug) {
                        player.send(new SendMessage(String.format("[%s, type=%d]: item=%d", this.getClass().getSimpleName(), event.getType(), event.getItem().getId())));
                    }
                    return true;
                }
                break;

            case 2:
                if (secondClickItem(player, event)) {
                    logger.log(Level.INFO, String.format("[%s, type=%d]:second click item=%d", this.getClass().getSimpleName(), event.getType(), event.getItem().getId()));
                    if (player.debug) {
                        player.send(new SendMessage(String.format("[%s, type=%d]: item=%d", this.getClass().getSimpleName(), event.getType(), event.getItem().getId())));
                    }
                    return true;
                }
                break;

            case 3:
                if (thirdClickItem(player, event)) {
                    logger.log(Level.INFO, String.format("[%s, type=%d]:third click item=%d", this.getClass().getSimpleName(), event.getType(), event.getItem().getId()));
                    if (player.debug) {
                        player.send(new SendMessage(String.format("[%s, type=%d]: item=%d", this.getClass().getSimpleName(), event.getType(), event.getItem().getId())));
                    }
                    return true;
                }
                break;

            case 4:
                if (fourthClickItem(player, event)) {
                    logger.log(Level.INFO, String.format("[%s, type=%d]:fourth click item=%d", this.getClass().getSimpleName(), event.getType(), event.getItem().getId()));
                    if (player.debug) {
                        player.send(new SendMessage(String.format("[%s, type=%d]: item=%d", this.getClass().getSimpleName(), event.getType(), event.getItem().getId())));
                    }
                    return true;
                }
                break;
        }
        return false;
    }

    protected boolean firstClickItem(Player player, ItemClickEvent event) {
        return false;
    }

    protected boolean secondClickItem(Player player, ItemClickEvent event) {
        return false;
    }

    protected boolean thirdClickItem(Player player, ItemClickEvent event) {
        return false;
    }

    protected boolean fourthClickItem(Player player, ItemClickEvent event) {
        return false;
    }

    private boolean handleButtonClickEvent(Player player, ButtonClickEvent event) {
        if (onClick(player, event.getButton())) {
            if (player.debug) {
                player.send(new SendMessage(String.format("[%s]: button=%d", this.getClass().getSimpleName(), event.getButton())));
            }
            return true;
        }
         return false;
    }

    protected boolean onClick(final Player player, final int button) {
        switch (button) {
            case 29404:
                player.send(new SendURL("http://www.tarnishps.com/"));
                return true;
            case 29408:
            InterfaceWriter.write(new AchievementWriter(player));
            player.interfaceManager.setSidebar(Config.QUEST_TAB, 35_000);
            return true;

                  case 29411:
                InterfaceWriter.write(new InformationWriter(player));
                return true;
            case -30528:
                InterfaceWriter.write(new InformationWriter(player));
                player.interfaceManager.setSidebar(Config.QUEST_TAB, 29_400);
            return true;
            case -30525:
                player.dialogueFactory.sendOption("Activity Logger", () -> {
                    player.dialogueFactory.onAction(player.activityLogger::open);
                }, "Drop Simulator", () -> {
                    player.dialogueFactory.onAction(() -> DropSimulator.open(player));
               /* }, "Activity Logger", () -> {
                    player.dialogueFactory.onAction(player.activityLogger::open);*/
                }, "Title Manager", () -> {
                    player.dialogueFactory.onAction(() -> TitleManager.open(player));
                }, "Slayer Interface", () -> {
                    player.dialogueFactory.onAction(() -> player.slayer.open(SlayerTab.MAIN));
                }).execute();
                return true;
            case -30085:
                ProfileViewer.open(player, player);
                return true;

            case -30084:
                player.activityLogger.open();
                return true;

            case -30083:
                TitleManager.open(player);
                return true;

            case -30082:
                DropDisplay.open(player);
                return true;

            case -30081:
                DropSimulator.open(player);
                return true;

            case -30080:
                player.gameRecord.display(ActivityType.getFirst());
                return true;

        }
        return false;
    }


}
