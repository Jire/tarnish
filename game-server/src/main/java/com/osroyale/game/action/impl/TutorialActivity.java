package com.osroyale.game.action.impl;


import com.osroyale.Config;
import com.osroyale.content.StarterKit;
import com.osroyale.content.achievement.AchievementHandler;
import com.osroyale.content.achievement.AchievementKey;
import com.osroyale.content.activity.Activity;
import com.osroyale.content.activity.ActivityType;
import com.osroyale.content.activity.panel.ActivityPanel;
import com.osroyale.content.dialogue.DialogueFactory;
import com.osroyale.content.event.impl.ClickButtonInteractionEvent;
import com.osroyale.content.skill.impl.magic.teleport.TeleportType;
import com.osroyale.content.skill.impl.magic.teleport.Teleportation;
import com.osroyale.content.teleport.TeleportHandler;
import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.world.entity.mob.Direction;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.packet.out.SendConfig;
import com.osroyale.net.packet.out.SendForceTab;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.net.packet.out.SendSpecialAmount;
import com.osroyale.util.Utility;

import static com.osroyale.content.skill.impl.magic.teleport.TeleportationData.MODERN;

/**
 * Handles the tutorial activity.
 *
 * @author Daniel
 */
public class TutorialActivity extends Activity {

    /** The player doing the tutorial activity. */
    private final Player player;

    /** The guide npc for the activity. */
    private Npc guide;

    /** The current activity stage. */
    private int stage;

    /** The dialogue factory for this activity. */
    private final DialogueFactory factory;

    private boolean completed = false;

    /** Constructs a new <code>TutorialActivity</code>. */
    private TutorialActivity(Player player) {
        super(2, player.playerAssistant.instance());
        this.stage = 0;
        this.player = player;
        this.factory = player.dialogueFactory;
    }

    /** Creates the tutorial activity for the player. */
    public static TutorialActivity create(Player player) {
        TutorialActivity activity = new TutorialActivity(player);
        player.runEnergy = 100;
        player.send(new SendSpecialAmount());
        player.movement.setRunningToggled(false);
        player.playerAssistant.setSidebar(true);
        player.setVisible(false);
        player.move(Config.DEFAULT_POSITION);
        activity.cooldown(3);
        activity.add(player);
        player.locking.lock();
        return activity;
    }

    /** Updates the activity panel. */
    private void update(String... strings) {
        ActivityPanel.update(player, (int) Utility.getPercentageAmount(stage, 27), "Tutorial", "Progress", strings);
    }

    @Override
    public void cooldown(int cooldown) {
        super.cooldown(cooldown);
        factory.clear();
    }

    private void next() {
        update();
        cooldown(1);
    }

    @Override
    protected void start() {
//        System.out.println(stage);
        switch (stage) {
            case 0:
                guide = new Npc(306, new Position(3222, 3223));
                add(guide);
                guide.graphic(new Graphic(86, true));
                cooldown(2);
                break;
            case 1:
                guide.face(player.getPosition());
                guide.animate(new Animation(863));
                guide.speak("Hello, " + player.getName() + "!");
                cooldown(2);
                break;
            case 2:
                guide.walk(new Position(3222, 3218));
                cooldown(1);
                break;
            case 3:
                player.face(guide.getPosition());
                factory.sendNpcChat(306, "Welcome to <col=D63E3E>Augury</col>, " + player.getName(),
                        "Would you like a tutorial of our lands?");
                factory.sendOption("Yes", () -> cooldown(1), "No", this::finish);
                factory.execute();
                pause();
                break;
            case 4:
                update("This is the activity panel.", "The activity panel replaces a lot", "of interfaces so be sure",
                        "to get acquainted to it.");
                factory.sendNpcChat(306, "Wise choice my friend! Let's get started.",
                        "The first thing you need to know about is the", "activity panel. When you are doing an activity,",
                        "like this tutorial for example; a panel will be available to you.");
                factory.sendNpcChat(306, "You can access this panel by clicking the red tab icon.",
                        "The activity panel is disabled by default but will", "automatically enable when in a activity.");
                factory.onAction(() -> cooldown(1));
                factory.execute();
                pause();
                break;
            case 5:
                factory.sendNpcChat(306, "Before I show you around I want to show you",
                        "useful content for your adventures.");
                factory.onAction(() -> {
                    player.interfaceManager.setSidebar(Config.QUEST_TAB, 29400);
                    player.send(new SendForceTab(Config.QUEST_TAB));
                    next();
                });
                factory.execute();
                pause();
                break;
            case 6:
                factory.sendNpcChat(306, "This is your quest tab. Inside contains information about",
                        "the world and yourself. On the top right corner of the",
                        "tab you will see a bunch of different buttons.");
                factory.sendNpcChat(306, "The blue button will show you the quest tab",
                        "The green button will provide you with various options.");
                factory.sendNpcChat(306, "Some of these options include - npc drop viwer,",
                        "log drop simulator, log kill logs, title manager and more!");
                factory.sendNpcChat(306, "Lastly, the scroll button will open a menu where you can",
                        "change game related options. For example: welcome screen,",
                        " triviabot, drop notification, and more! These options",
                        "are not to be confused with the client options.");
                factory.onAction(() -> {
                    player.interfaceManager.setSidebar(Config.QUEST_TAB, -1);
                    player.interfaceManager.setSidebar(Config.WRENCH_TAB, 50020);
                    next();
                });
                factory.execute();
                pause();
                break;
            case 7:
                player.send(new SendForceTab(Config.WRENCH_TAB));
                factory.sendNpcChat(306, "This is your settings tab. Here you can change all options",
                        "that are for your client. The interface has different tabs.",
                        "You are currently in the display tab. You can change all", " display settings here.");
                factory.onAction(this::next);
                factory.execute();
                pause();
                break;
            case 8:
                setInstance(Mob.DEFAULT_INSTANCE);
                player.instance = Mob.DEFAULT_INSTANCE;
                player.loadRegion();
                factory.sendNpcChat(306, "Let's move on.");
                factory.sendNpcChat(306, "Edgeville will be your primary home, however you",
                        "can change it later on as you progress", "in the game.").onAction(this::next);
                factory.execute();
                pause();
                break;
            case 9:
                Teleportation.teleport(player, new Position(3089, 3492), MODERN, () -> {
                    player.face(Direction.EAST);
                    next();
                    player.locking.lock();
                });
                pause();
                break;
            case 10:
                factory.sendNpcChat(306, "Throughout your adventures you will find key halves.",
                        "When using the right halves together you will be able",
                        "to form a Crystal key. With that key you can unlock this chest.",
                        "This chest contains various high valued items.").onAction(this::next).execute();
                pause();
                break;
            case 11:
                Teleportation.teleport(player, new Position(3091, 3494), MODERN, () -> {
                    player.face(Direction.WEST);
                    next();
                    player.locking.lock();
                });
                pause();
                break;
            case 12:
                factory.sendNpcChat(306, "This is our market place. You can put any items you",
                                "would like to sell in your store. You can also", "purchase items from other players.")
                        .onAction(this::next);
                factory.execute();
                pause();
                break;
            case 13:
                Teleportation.teleport(player, new Position(3093, 3500), MODERN, () -> {
                    player.face(Direction.EAST);
                    next();
                    player.locking.lock();
                });
                pause();
                break;
            case 14:
                factory.sendNpcChat(306, "These thieving stalls are a very good for making", "quick money.")
                        .onAction(this::next).execute();
                pause();
                break;
            case 15:
                Teleportation.teleport(player, new Position(3080, 3491), MODERN, () -> {
                    player.face(Direction.WEST);
                    next();
                    player.locking.lock();
                });
                pause();
                break;
            case 16:
                factory.sendNpcChat(306, "Here are shops that will sell you all the supplies you may need,",
                        " aswell as gear/food/potions/misc items and more.").onAction(this::next).execute();
                pause();
                break;
            case 17:
                Teleportation.teleport(player, new Position(3080, 3491), MODERN, () -> {
                    player.face(Direction.WEST);
                    next();
                    player.locking.lock();
                });
                pause();
                break;
            case 18:
                factory.sendNpcChat(306, "These npcs can really help you out, anywhere from",
                                "mass decanting potions, changing appearances,", "buying skillcapes and more!").onAction(this::next)
                        .execute();
                pause();
                break;
            case 19:
                Teleportation.teleport(player, new Position(3086, 3500), MODERN, () -> {
                    player.face(Direction.WEST);
                    next();
                    player.locking.lock();
                });
                pause();
                break;
            case 20:
                factory.sendNpcChat(306, "This is the Well of goodwill,",
                        "you will be able to contribute gold for a server wide",
                        "experience bonus that will last 30 Minutes.").onAction(this::next).execute();
                pause();
                break;
            case 21:
                Teleportation.teleport(player, new Position(3078, 3509), MODERN, () -> {
                    player.face(Direction.SOUTH);
                    next();
                    player.locking.lock();
                });
                pause();
                break;
            case 22:
                factory.sendNpcChat(306, "This is the fun pk arena.",
                                "Here you can fight players in multi-combat and lose nothing", "on death!").onAction(this::next)
                        .execute();
                pause();
                break;
            case 23:
                Teleportation.teleport(player, new Position(3090, 3474), MODERN, () -> {
                    player.face(Direction.SOUTH);
                    next();
                    player.onStep();
                    player.locking.lock();
                });
                pause();
                break;
            case 24:
                factory.sendNpcChat(306, "This is the skilling area for members with",
                        "Extreme Donator status.");
                factory.onAction(() -> {
                    player.face(Direction.SOUTH);
                    next();
                    player.locking.lock();
                });
                factory.execute();
                pause();
                break;
            case 25:
                Teleportation.teleport(player, new Position(3097, 3498), MODERN, () -> {
                    player.face(Direction.NORTH);
                    next();
                    player.locking.lock();
                });
                pause();
                break;
            case 26:
                factory.sendNpcChat(1143, "Hello #name.", "I am the clan master.");
                factory.sendNpcChat(1143, "Clans are going to be extremely useful for your adventures.",
                        "Joining a clan can give you extra experience rewards,",
                        "experience and drop modifiers, and much more!", "It is also a great way to make new friends!");
                factory.sendNpcChat(1143, "There are heavily enforced rules for being in a clan.",
                        "Check out the rules on our forums ::clanrules");
                factory.onAction(this::next).execute();
                pause();
                break;
            case 27:
                Teleportation.teleport(player, Config.DEFAULT_POSITION, MODERN, () -> {
                    player.face(Direction.SOUTH);
                    next();
                    player.locking.lock();
                });
                pause();
                break;
            case 28:
                factory.sendNpcChat(306, "That's the end of the tutorial! If you have any more questions",
                                "Check out our forums for detail guides or ask a moderator.", "Good luck #name!")
                        .onAction(this::next).execute();
                pause();
                break;
            case 29:
                completed = true;
                finish();
                break;
        }
        stage++;
    }

    @Override
    public void cleanup() {
        remove(guide);
    }

    @Override
    public void finish() {
        player.setVisible(true);
        player.playerAssistant.setSidebar(false);
        cleanup();
        remove(player);
        factory.clear();
        player.locking.unlock();
        ActivityPanel.clear(player);
        if (completed) {
            AchievementHandler.activate(player, AchievementKey.COMPLETE_TUTORIAL);
        }
        if (player.needsStarter) {
            StarterKit.open(player);
        }

    }

    @Override
    protected boolean clickButton(Player player, ClickButtonInteractionEvent event) {
        int button = event.getButton();
        switch (stage) {
            case 8:
                if (button == -15496) {
                    factory.lock(false);
                    player.send(new SendMessage(":settingupdate:"));
                    player.interfaceManager.open(28_500);
                    start();
                }
                return true;
            case 9:
                if (button == 28502) {
                    factory.lock(false);
                    player.interfaceManager.close();
                    start();
                }
                return true;
            case 10:
                if (button == -15531) {
                    factory.lock(false);
                    player.interfaceManager.setSidebar(Config.WRENCH_TAB, 50300);
                    player.send(new SendConfig(980, 3));
                    next();
                }
                return true;
            case 12:
                if (button == 850) {
                    factory.lock(false);
                    TeleportHandler.open(player, TeleportType.FAVORITES);
                    start();
                }
                return true;
            case 13:
                if (button == -7534) {
                    factory.lock(false);
                    player.interfaceManager.close();
                    start();
                }
                return true;
        }
        return false;
    }

    @Override
    public void onRegionChange(Player player) {
    }

    @Override
    public void onLogout(Player player) {
        cleanup();
        remove(player);
        player.move(Config.DEFAULT_POSITION);

        if (player.needsStarter) {
            player.newPlayer = true;
        }
    }

    @Override
    public ActivityType getType() {
        return ActivityType.TUTORIAL;
    }
}
