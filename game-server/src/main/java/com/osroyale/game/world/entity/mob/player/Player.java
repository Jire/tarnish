package com.osroyale.game.world.entity.mob.player;

import com.osroyale.Config;
import com.osroyale.content.ActivityLog;
import com.osroyale.content.ActivityLogger;
import com.osroyale.content.achievement.AchievementKey;
import com.osroyale.content.activity.Activity;
import com.osroyale.content.activity.impl.barrows.BrotherData;
import com.osroyale.content.activity.record.PlayerRecord;
import com.osroyale.content.bags.impl.CoalBag;
import com.osroyale.content.bags.impl.GemBag;
import com.osroyale.content.clanchannel.channel.ClanChannel;
import com.osroyale.content.clanchannel.channel.ClanChannelHandler;
import com.osroyale.content.clanchannel.content.ClanViewer;
import com.osroyale.content.collectionlog.CollectionLog;
import com.osroyale.content.collectionlog.CollectionLogData;
import com.osroyale.content.collectionlog.CollectionLogPage;
import com.osroyale.content.collectionlog.CollectionLogSaving;
import com.osroyale.content.combat.Killstreak;
import com.osroyale.content.combat.Skulling;
import com.osroyale.content.dailyeffect.DailyEffect;
import com.osroyale.content.dailyeffect.impl.DailySlayerTaskSkip;
import com.osroyale.content.dailyeffect.impl.DailySlayerTaskTeleport;
import com.osroyale.content.dailyeffect.impl.DailySpellBookSwap;
import com.osroyale.content.dialogue.ChatBoxItemDialogue;
import com.osroyale.content.dialogue.Dialogue;
import com.osroyale.content.dialogue.DialogueFactory;
import com.osroyale.content.dialogue.OptionDialogue;
import com.osroyale.content.donators.Donation;
import com.osroyale.content.emote.EmoteUnlockable;
import com.osroyale.content.event.EventDispatcher;
import com.osroyale.content.event.impl.LogInEvent;
import com.osroyale.content.gambling.GambleManager;
import com.osroyale.content.lms.LMSGame;
import com.osroyale.content.lms.lobby.LMSLobby;
import com.osroyale.content.lms.lobby.LMSLobbyEvent;
import com.osroyale.content.mysterybox.MysteryBoxManager;
import com.osroyale.content.overrides.Overrides;
import com.osroyale.content.pet.PetData;
import com.osroyale.content.pet.Pets;
import com.osroyale.content.preset.PresetManager;
import com.osroyale.content.prestige.Prestige;
import com.osroyale.content.puzzle.PuzzleDisplay;
import com.osroyale.content.skill.impl.construction.House;
import com.osroyale.content.skill.impl.farming.Farming;
import com.osroyale.content.skill.impl.hunter.birdhouse.PlayerBirdHouseData;
import com.osroyale.content.skill.impl.hunter.trap.TrapManager;
import com.osroyale.content.skill.impl.magic.RunePouch;
import com.osroyale.content.skill.impl.magic.Spellbook;
import com.osroyale.content.skill.impl.magic.spell.SpellCasting;
import com.osroyale.content.skill.impl.runecrafting.RunecraftPouch;
import com.osroyale.content.skill.impl.slayer.Slayer;
import com.osroyale.content.store.impl.PersonalStore;
import com.osroyale.content.teleport.Teleport;
import com.osroyale.content.tittle.PlayerTitle;
import com.osroyale.content.tradingpost.TradingPost;
import com.osroyale.game.event.impl.MovementEvent;
import com.osroyale.game.plugin.PluginManager;
import com.osroyale.game.service.Highscores;
import com.osroyale.game.task.impl.PvPTimerTask;
import com.osroyale.game.task.impl.TeleblockTask;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.EntityType;
import com.osroyale.game.world.entity.combat.Combat;
import com.osroyale.game.world.entity.combat.CombatConstants;
import com.osroyale.game.world.entity.combat.attack.FightType;
import com.osroyale.game.world.entity.combat.effect.AntifireDetails;
import com.osroyale.game.world.entity.combat.magic.CombatSpell;
import com.osroyale.game.world.entity.combat.ranged.RangedAmmunition;
import com.osroyale.game.world.entity.combat.ranged.RangedWeaponDefinition;
import com.osroyale.game.world.entity.combat.strategy.CombatStrategy;
import com.osroyale.game.world.entity.combat.strategy.player.special.CombatSpecial;
import com.osroyale.game.world.entity.combat.weapon.WeaponInterface;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.UpdateFlag;
import com.osroyale.game.world.entity.mob.Viewport;
import com.osroyale.game.world.entity.mob.movement.waypoint.PickupWaypoint;
import com.osroyale.game.world.entity.mob.movement.waypoint.Waypoint;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.player.appearance.Appearance;
import com.osroyale.game.world.entity.mob.player.exchange.ExchangeSessionManager;
import com.osroyale.game.world.entity.mob.player.relations.ChatMessage;
import com.osroyale.game.world.entity.mob.player.relations.PlayerRelation;
import com.osroyale.game.world.entity.mob.player.requests.PlayerPunishment;
import com.osroyale.game.world.entity.mob.player.requests.RequestManager;
import com.osroyale.game.world.entity.mob.prayer.Prayer;
import com.osroyale.game.world.entity.mob.prayer.PrayerBook;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.containers.bank.Bank;
import com.osroyale.game.world.items.containers.bank.BankPin;
import com.osroyale.game.world.items.containers.bank.BankVault;
import com.osroyale.game.world.items.containers.bank.DonatorDeposit;
import com.osroyale.game.world.items.containers.equipment.Equipment;
import com.osroyale.game.world.items.containers.impl.LootingBag;
import com.osroyale.game.world.items.containers.impl.LostUntradeables;
import com.osroyale.game.world.items.containers.inventory.Inventory;
import com.osroyale.game.world.items.containers.pricechecker.PriceChecker;
import com.osroyale.game.world.object.CustomGameObject;
import com.osroyale.game.world.object.ObjectDirection;
import com.osroyale.game.world.object.ObjectType;
import com.osroyale.game.world.position.Area;
import com.osroyale.game.world.position.Boundary;
import com.osroyale.game.world.position.Position;
import com.osroyale.game.world.region.Region;
import com.osroyale.net.packet.OutgoingPacket;
import com.osroyale.net.packet.out.*;
import com.osroyale.net.session.GameSession;
import com.osroyale.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jire.tarnishps.event.Events;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * This class represents a character controlled by a player.
 *
 * @author Daniel
 * @author Michael | Chex
 */
public class Player extends Mob {

    private final Events events = new Events();

    public List<PlayerBirdHouseData> birdHouseData = new ArrayList<>();
    public int wintertodtPoints;
    public int menuOpened, optionOpened = -1;

    private final GambleManager gamblingManager = new GambleManager();
    public Map<String, Position> waypoints = new HashMap<>();
    public Teleport lastTeleport = null;
    public GambleManager getGambling() {
        return gamblingManager;
    }

    private long lastModification;

    public void setLastModification(long lastModification) {
        this.lastModification = lastModification;
    }

    public long getLastModification() {
        return lastModification;
    }

    private boolean gambleLocked;

    public void setGambleLock(boolean gambleLocked) {
        this.gambleLocked = gambleLocked;
    }

    public boolean isGambleLocked() {
        return gambleLocked;
    }

    /**
     * LMS Variables
     */
    public int lastFogSent;
    public transient long lmsImmunity;
    public long lastLMSRopeCross;
    public List<Integer> unlockedLMSItems = new ArrayList<>();
    public int lmsKills;
    public int lmsDeaths;

    public int answeredTrivias;
    public int lmsPoints;
    public int lmsTotalKills;
    public int lmsTotalDeaths;
    public int lmsWins;

    public GemBag gemBag = new GemBag();

    public CoalBag coalBag = new CoalBag();

    public CollectionLogPage collectionLogPageOpen = null;
    public CollectionLogData collectionLogView = null;

    private CollectionLog collectionLog = new CollectionLog();

    public void setCollectionLog(CollectionLog collectionLog) {
        this.collectionLog = collectionLog;
    }

    public HashMap<WeaponInterface, FightType> fightStyles = new HashMap<>();

    public CollectionLog getCollectionLog() {
        return this.collectionLog;
    }

    private static final Logger logger = LogManager.getLogger();
    private int memberId = -1;
    public final Viewport viewport = new Viewport(this);
    public boolean debug;
    public Npc pet;
    public CombatSpell autocast;
    public CombatSpell singleCast;
    public Appearance appearance = Config.DEFAULT_APPEARANCE;
    public PlayerRight right = PlayerRight.PLAYER;
    public PlayerTitle playerTitle = PlayerTitle.empty();
    public Spellbook spellbook = Spellbook.MODERN;
    public Spellbook spellbook_copy = Spellbook.MODERN;
    public ChatBoxItemDialogue chatBoxItemDialogue;
    private Optional<ChatMessage> chatMessage = Optional.empty();
    public DailyEffect dailySlayerTaskTeleport = new DailySlayerTaskTeleport();
    public DailyEffect dailySlayerTaskSkip = new DailySlayerTaskSkip();
    public DailyEffect dailySpellBookSwap = new DailySpellBookSwap();
    public PrayerBook quickPrayers = new PrayerBook();
    public HashSet<Prayer> unlockedPrayers = new HashSet<>();
    public RangedWeaponDefinition rangedDefinition;
    public RangedAmmunition rangedAmmo;
    private AntifireDetails antifireDetails;
    private WeaponInterface weapon = WeaponInterface.UNARMED;
    private CombatSpecial combatSpecial;
    public Optional<Player> managing;
    public boolean completedMageArena;
    public boolean pvpInstance;
    public boolean hasPvPTimer;
    public Stopwatch pvpTimer = Stopwatch.start();
    public Optional<Dialogue> dialogue = Optional.empty();
    public Optional<OptionDialogue> optionDialogue = Optional.empty();
    public Optional<Consumer<String>> enterInputListener = Optional.empty();
    public boolean[] barrowKills = new boolean[BrotherData.values().length];
    public final PlayerRelation relations = new PlayerRelation(this);
    public final Donation donation = new Donation(this);
    public final LostUntradeables lostUntradeables = new LostUntradeables(this);
    public LinkedList<Item> lostItems = new LinkedList<>();
    public BrotherData hiddenBrother;
    public int barrowsKillCount;
    public int sequence;
    public int playTime;
    public int kill;
    public int death;
    public int shop;
    public int headIcon;
    public int valueIcon = -1;
    public int skillingPoints;
    public int pestPoints;
    public int votePoints;
    public int totalVotes;
    public int[] achievedSkills = new int[7];

    public int mageArenaPoints;
    public int ringOfRecoil = 40;
    public int whipCharges = 2500;
    public int agsCharges = 750;
    public int wilderness;
    public int runEnergy;
    public int energyRate;
    public int glovesTier;
    public int royaltyLevel = 1;
    public int royalty;

    public int smallPouch;
    public int mediumPouch;
    public int largePouch;
    public int giantPouch;
    public final RunecraftPouch runecraftPouch = new RunecraftPouch(this);
    public double experienceRate = Config.COMBAT_MODIFICATION;
    public long usernameLong;
    public boolean idle;
    public boolean resting;
    public boolean newPlayer;
    public boolean needsStarter;
    public boolean venged;
    private boolean specialActivated;
    public boolean warriorGuidTask;
    public boolean isBot;
    public int trapsLaid;
    private String username = "";
    private String password = "";
    public String uuid;
    public String lastHost;
    public String created = Utility.getDate();
    public String lastClan = "";
    public ClanChannel clanChannel;
    public String clan = "";
    public String clanTag = "";
    public String clanTagColor = "";
    public final AtomicBoolean saved = new AtomicBoolean(false);
    public Stopwatch yellDelay = Stopwatch.start();
    public Stopwatch godwarsDelay = Stopwatch.start();
    public Stopwatch buttonDelay = Stopwatch.start();
    public Stopwatch itemDelay = Stopwatch.start();
    public Stopwatch foodDelay = Stopwatch.start();
    public Stopwatch fastFoodDelay = Stopwatch.start();
    public Stopwatch potionDelay = Stopwatch.start();
    public Stopwatch revstele = Stopwatch.start();
    public int cooldown = 7 * 60 * 1000;
    public long lastUsed = 0;
    public Stopwatch restoreDelay = Stopwatch.start();
    public Stopwatch diceDelay = Stopwatch.start();
    public Stopwatch aggressionTimer = Stopwatch.start();
    public Stopwatch databaseRequest = Stopwatch.start();
    public Set<PetData> petInsurance = new HashSet<>();
    public Set<PetData> lostPets = new HashSet<>();
    public final PuzzleDisplay puzzle = new PuzzleDisplay(this);
    public final ClanViewer clanViewer = new ClanViewer(this);
    public final PlayerRecord gameRecord = new PlayerRecord(this);
    public final Farming farming = new Farming(this);
    public final ExchangeSessionManager exchangeSession = new ExchangeSessionManager(this);
    public final Map<Integer, PersonalStore> viewing_shops = new HashMap<>();
    public final PlayerAssistant playerAssistant = new PlayerAssistant(this);
    public final InterfaceManager interfaceManager = new InterfaceManager(this);
    public final RequestManager requestManager = new RequestManager(this);
    public final MysteryBoxManager mysteryBox = new MysteryBoxManager(this);
    public final Settings settings = new Settings(this);
    public final Inventory inventory = new Inventory(this);
    public final Inventory inventory_copy = new Inventory(this);
    public final Bank bank = new Bank(this);
    public final BankVault bankVault = new BankVault(this);
    public final BankPin bankPin = new BankPin(this);
    public final RunePouch runePouch = new RunePouch(this);
    public final RunePouch runePouch_copy = new RunePouch(this);
    public final Killstreak killstreak = new Killstreak(this);
    public final LootingBag lootingBag = new LootingBag(this);
    public final PlayerPunishment punishment = new PlayerPunishment(this);
    public final Equipment equipment = new Equipment(this);
    public final Equipment equipment_copy = new Equipment(this);
    public final PresetManager presetManager = new PresetManager(this);
    public final Prestige prestige = new Prestige(this);
    public final PriceChecker priceChecker = new PriceChecker(this);
    public final DonatorDeposit donatorDeposit = new DonatorDeposit(this);
    public DialogueFactory dialogueFactory = new DialogueFactory(this);
    public final House house = new House(this);
    public Slayer slayer = new Slayer(this);
    public Skulling skulling = new Skulling(this);
    public SpellCasting spellCasting = new SpellCasting(this);
    private final Combat<Player> combat = new Combat<>(this);
    public final ActivityLogger activityLogger = new ActivityLogger(this);
    private final MutableNumber poisonImmunity = new MutableNumber();
    private final MutableNumber venomImmunity = new MutableNumber();
    private final MutableNumber specialPercentage = new MutableNumber();
    public Deque<String> lastKilled = new ArrayDeque<>(3);
    public List<EmoteUnlockable> emoteUnlockable = new LinkedList<>();
    public List<Teleport> favoriteTeleport = new ArrayList<>();
    public final Set<String> hostList = new HashSet<>();
    public final TradingPost tradingPost = new TradingPost(this);
    public final Overrides overrides = new Overrides(this);


    public HashMap<ActivityLog, Integer> loggedActivities = new HashMap<ActivityLog, Integer>(ActivityLog.values().length) {
        {
            for (final ActivityLog achievement : ActivityLog.values())
                put(achievement, 0);
        }
    };

    public HashMap<AchievementKey, Integer> playerAchievements = new HashMap<AchievementKey, Integer>(AchievementKey.values().length) {
        {
            for (final AchievementKey achievement : AchievementKey.values())
                put(achievement, 0);
        }
    };

    public float blowpipeScales;
    public Item blowpipeDarts;


    public int crawsBowCharges;
    public int viggorasChainmaceCharges;
    public int thammoranSceptreCharges;
    public int tridentSeasCharges;
    public int tridentSwampCharges;

    public int celestialRingCharges;

    public int serpentineHelmCharges;
    public int tanzaniteHelmCharges;
    public int magmaHelmCharges;

    public Stopwatch staffOfDeadSpecial = Stopwatch.start().reset(200);
    private Optional<GameSession> session = Optional.empty();
    public int dragonfireCharges;
    public long dragonfireUsed;

    public Player(String username) {
        super(Config.DEFAULT_POSITION, false);
        this.username = username;
        this.usernameLong = Utility.nameToLong(username);
    }

    public Player(String username, Position position) {
        super(Config.DEFAULT_POSITION);
        this.setPosition(position);
        this.setUsername(username);
        this.isBot = true;
    }

    public void chat(ChatMessage chatMessage) {
        if (!chatMessage.isValid()) {
            return;
        }
        this.chatMessage = Optional.of(chatMessage);
        this.updateFlags.add(UpdateFlag.CHAT);
    }

    public void setUsername(String username) {
        this.username = username;
        this.usernameLong = Utility.nameToLong(username);
    }

    public void send(OutgoingPacket encoder) {
        encoder.execute(this);
    }

    private void login() {
        positionChange = true;
        regionChange = true;

        onStep();

        skills.login();

        mobAnimation.reset();

        inventory.refresh();

        CombatSpell autocast = getAutocastSpell();
        equipment.login();

        settings.login();

        relations.onLogin();

        sendInitialPackets();

        playerAssistant.login();


        send(new SendSpecialEnabled(0));
        send(new SendConfig(301, 0));

        setAutocast(autocast);
        if (isAutocast() && equipment.hasWeapon()) {
            send(new SendConfig(43, 3));
            send(new SendConfig(108, 1));
        } else {
            setAutocast(null);
            send(new SendConfig(108, 0));
            send(new SendString("Spell", 18584));
        }
    }

    private void sendInitialPackets() {
        int players = World.getPlayerCount();
        send(new SendRunEnergy());
        send(new SendPlayerDetails());
        send(new SendCameraReset());
        send(new SendExpCounter(skills.getExpCounter()));
        World.sendMessage("<img=15> " +getPlayer().getName()+ " has logged in.");
        message(true, String.format("Welcome to %s. ", Config.SERVER_NAME));
        message(true, "We are currently in early access.");

        if (Config.DOUBLE_EXPERIENCE) {
            message("Double experience is currently active!");
        }

        if (players > GameSaver.MAX_PLAYERS) {
            GameSaver.MAX_PLAYERS = players;
        }
        if (needsStarter) {
            newPlayer = true;
            bank.addAll(Config.STARTER_BANK);
            System.arraycopy(Config.STARTER_BANK_AMOUNT, 0, bank.tabAmounts, 0, Config.STARTER_BANK_AMOUNT.length);
            bank.shift();
        }

        playerAssistant.welcomeScreen();
    }

    private boolean canLogout() {
        if (getCombat().inCombat()) {
            send(new SendMessage("You can not logout whilst in combat!"));
            return false;
        }

        if (!getCombat().hasPassed(CombatConstants.COMBAT_LOGOUT_COOLDOWN)) {
            final long seconds = TimeUnit.SECONDS.convert(
                    CombatConstants.COMBAT_LOGOUT_COOLDOWN - combat.elapsedTime(),
                    TimeUnit.MILLISECONDS);
            send(new SendMessage("You must wait " + seconds + " second" + (seconds > 1 ? "s" : "")
                    + " before you can logout as you were recently in combat."));
            return false;
        }

        if (!interfaceManager.isMainClear()) {
            send(new SendMessage("Please close what you are doing before logging out!"));
            return false;
        }

        return !Activity.evaluate(this, it -> !it.canLogout(this));
    }

    public final Set<PlayerOption> contextMenus = new HashSet<>();

    public final void logout() {
        logout(false);
    }

    public final void logout(boolean force) {
        if (!canLogout() && !force) {
            return;
        }

        send(new SendLogout());
        setVisible(false);
        World.queueLogout(this);
    }

    public void loadRegion() {
        for(PlayerBirdHouseData playerBirdHouseData : birdHouseData) {

            if(Utility.goodDistance(playerBirdHouseData.birdhousePosition.getX(), playerBirdHouseData.birdhousePosition.getY(), getPosition().getX(), getPosition().getY(), 60)) {
                int objectId = playerBirdHouseData.birdhouseData.objectData[playerBirdHouseData.seedAmount >= 10 ? 1 : 0];
                send(new SendAddObject(new CustomGameObject(objectId, playerBirdHouseData.birdhousePosition, ObjectDirection.valueOf(playerBirdHouseData.rotation).get(), ObjectType.valueOf(playerBirdHouseData.type).get())));
            }
        }

        Activity.forActivity(this, minigame -> minigame.onRegionChange(this));

        Region[] surrounding = World.getRegions().getSurroundingRegions(getPosition());

        for (Region region : surrounding) {
            region.sendGroundItems(this);
            region.sendGameObjects(this);

            //Npc Face
            for (Npc npc : region.getNpcs(getHeight())) {
                if (!npc.getCombat().inCombat())
                    npc.face(npc.faceDirection);
            }
        }

        farming.regionChange(this);
        TrapManager.handleRegionChange(this);

        if (debug && PlayerRight.isDeveloper(this)) {
            send(new SendMessage("[REGION] Loaded new region.", MessageColor.DEVELOPER));
        }
    }

    public void pickup(Item item, Position position) {
        Waypoint waypoint = new PickupWaypoint(this, item, position);
        if (cachedWaypoint == null || (!cachedWaypoint.isRunning() || !waypoint.equals(cachedWaypoint))) {
            resetWaypoint();
            action.clearNonWalkableActions();
            movement.reset();
            getCombat().reset();
            World.schedule(cachedWaypoint = waypoint);
        }
    }

    public void forClan(Consumer<ClanChannel> action) {
        if (clanChannel != null)
            action.accept(clanChannel);
    }
    public void message(String message) {
        send(new SendMessage(message));
    }

    @Override
    public void register() {
        if (!isRegistered() && !World.getPlayers().contains(this)) {
            setRegistered(World.getPlayers().add(this));
            setPosition(getPosition());

            login();

            logger.info("[REGISTERED]: " + Utility.formatName(getName()) + " [" + lastHost + "]");
            EventDispatcher.execute(this, new LogInEvent());
        }
    }

    @Override
    public void unregister() {
        if (!isRegistered()) {
            return;
        }

        if (!World.getPlayers().contains(this)) {
            return;
        }
        if (LMSGame.isActivePlayer(this))
            LMSGame.onDeath(this, true);
        World.sendMessage("<img=15> " +getPlayer().getName()+ " has logged out.");
        send(new SendLogout());
        Activity.forActivity(this, minigame -> minigame.onLogout(this));
        relations.updateLists(false);
        TrapManager.handleLogout(this);
        ClanChannelHandler.disconnect(this, true);
        interfaceManager.close();
        new Highscores(this).execute();
        //HighscoreService.saveHighscores(this);
        World.cancelTask(this, true);
        Pets.onLogout(this);
        CollectionLogSaving.save(this);
        World.getPlayers().remove((Player) destroy());
        getGambling().decline(this);
        logger.info(String.format("[UNREGISTERED]: %s [%s]", getName(), lastHost));
    }

    @Override
    public void addToRegion(Region region) {
        region.addPlayer(this);
        aggressionTimer.reset();
    }

    @Override
    public void removeFromRegion(Region region) {
        region.removePlayer(this);
    }

    @Override
    public void sequence() {
        if (!idle) {
            playTime++;
        }

        action.sequence();
        playerAssistant.sequence();
        sequence++;

        if(Area.inLMSLobby(this) || Area.inLMSBuilding(this)) {
            send(new SendString("Next Game: ", 44654));
            send(new SendString("" + ((LMSLobbyEvent.lobbyTicks * 600) / 1000), 44655));
            send(new SendString("" + LMSLobby.currentGameType.getClass().getSimpleName(), 44657));
            send(new SendString("<col="+(LMSLobby.lobbyMembers.size() >= LMSLobby.requiredPlayers ? "65280" : "ff0000")+">" + LMSLobby.lobbyMembers.size() + "/" + LMSLobby.maxPlayers + "</col>", 44659));
        }
    }

    @Override
    public void onStep() {
        PluginManager.getDataBus().publish(this, new MovementEvent(getPlayer().getPosition().copy()));
        send(new SendMultiIcon(Area.inMulti(this) ? 1 : -1));

        if (Activity.evaluate(this, activity -> activity.onStep(this))) {
            return;
        }

        // Reset the options
        send(new SendPlayerOption(PlayerOption.GAMBLE_REQUEST, false, true));
        send(new SendPlayerOption(PlayerOption.ATTACK, false, true));
        send(new SendPlayerOption(PlayerOption.DUEL_REQUEST, false, true));
        send(new SendPlayerOption(PlayerOption.FOLLOW, false));
        send(new SendPlayerOption(PlayerOption.TRADE_REQUEST, false));
        send(new SendPlayerOption(PlayerOption.VIEW_PROFILE, false));

        if(Boundary.isIn(this, GambleManager.GAMBLING_ZONE)) {
            //send(new SendPlayerOption(PlayerOption.TRADE_REQUEST, false, true));
            send(new SendPlayerOption(PlayerOption.GAMBLE_REQUEST, false));
        }
        else if(LMSGame.inGameArea(this)){
            send(new SendPlayerOption(PlayerOption.ATTACK, true));
            if (interfaceManager.getWalkable() != 44660)
                interfaceManager.openWalkable(44660);
        }
        else if(Area.inLMSLobby(this) || Area.inLMSBuilding(this)) {
            if (interfaceManager.getWalkable() != 44650)
                interfaceManager.openWalkable(44650);
        }
        // pvp instance
        else if (pvpInstance) {
            if (hasPvPTimer) {
                send(new SendPlayerOption(PlayerOption.ATTACK, true));
                send(new SendPlayerOption(PlayerOption.DUEL_REQUEST, false, true));
            } else if (Area.inPvP(this)) {
                send(new SendString("<col=E68A00>" + playerAssistant.getCombatRange(), 23327));
                send(new SendPlayerOption(PlayerOption.ATTACK, true));
                send(new SendPlayerOption(PlayerOption.DUEL_REQUEST, false, true));
                if (interfaceManager.getWalkable() != 23400) {
                    interfaceManager.openWalkable(23400);
                }
                return;
            } else {
                if (!hasPvPTimer && getCombat().inCombat()) {
                    World.schedule(new PvPTimerTask(this));
                } else {
                    send(new SendString("<col=36CF4D>Safe", 23327));
                    send(new SendPlayerOption(PlayerOption.ATTACK, false, true));
                    send(new SendPlayerOption(PlayerOption.DUEL_REQUEST, false, true));
                }
            }
            if (interfaceManager.getWalkable() != 23410) {
                interfaceManager.openWalkable(23410);
            }

            // wilderness
        } else if (Area.inWilderness(this)) {
            int modY = getPosition().getY() > 6400 ? getPosition().getY() - 6400 : getPosition().getY();
            wilderness = (((modY - 3521) / 8) + 1);
            send(new SendString("Level " + wilderness, 23327));
            send(new SendPlayerOption(PlayerOption.ATTACK, true));
            send(new SendPlayerOption(PlayerOption.DUEL_REQUEST, false, true));
            if (interfaceManager.getWalkable() != 23400)
                interfaceManager.openWalkable(23400);

            // duel arena lobby
        } else if (Area.inDuelArenaLobby(this)) {
            send(new SendPlayerOption(PlayerOption.DUEL_REQUEST, false));
            send(new SendPlayerOption(PlayerOption.ATTACK, false, true));
            if (interfaceManager.getWalkable() != 201)
                interfaceManager.openWalkable(201);
            // duel arena
        } else if (Area.inDuelArena(this) || Area.inDuelObsticleArena(this)) {
            send(new SendPlayerOption(PlayerOption.ATTACK, true));
            send(new SendPlayerOption(PlayerOption.DUEL_REQUEST, false, true));
            if (interfaceManager.getWalkable() != 201)
                interfaceManager.openWalkable(201);

            // event arena
      /*  } else if (Area.inEventArena(this)) {
            send(new SendPlayerOption(PlayerOption.ATTACK, true));
            send(new SendPlayerOption(PlayerOption.DUEL_REQUEST, false, true));
            send(new SendString("Fun PK", 23327));
            if (interfaceManager.getWalkable() != 23400)
                interfaceManager.openWalkable(23400);*/

            //
        } else if (Area.inSuperDonatorZone(this) && (Area.inRegularDonatorZone(this) && !PlayerRight.isDonator(this))) {
            move(Config.DEFAULT_POSITION);
            send(new SendMessage("You're not supposed to be here!"));

            // clear
        } else if (!inActivity()) {
            send(new SendPlayerOption(PlayerOption.GAMBLE_REQUEST, false, true));
            send(new SendPlayerOption(PlayerOption.ATTACK, false, true));
            send(new SendPlayerOption(PlayerOption.DUEL_REQUEST, false, true));
            send(new SendPlayerOption(PlayerOption.FOLLOW, false));
            send(new SendPlayerOption(PlayerOption.TRADE_REQUEST, false));
            send(new SendPlayerOption(PlayerOption.VIEW_PROFILE, false));
//
//            if (!interfaceManager.isClear())
                interfaceManager.close();
            if (wilderness > 0)
                wilderness = 0;
        }
    }

    @Override
    public Combat<Player> getCombat() {
        return combat;
    }

    @Override
    public CombatStrategy<Player> getStrategy() {
        return playerAssistant.getStrategy();
    }

    @Override
    public void appendDeath() {
        World.schedule(new PlayerDeath(this));
    }

    @Override
    public String getName() {
        return Utility.formatName(username);
    }

    @Override
    public EntityType getType() {
        return EntityType.PLAYER;
    }

    @Override
    public boolean isValid() {
        return (isBot || session != null) && super.isValid();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || !(obj instanceof Player)) {
            return false;
        }

        Player other = (Player) obj;
        return Objects.equals(getIndex(), other.getIndex()) && Objects.equals(username, other.username) && Objects.equals(password, other.password) && Objects.equals(isBot, other.isBot);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIndex(), username);
    }

    @Override
    public String toString() {
        return String.format("Player[index=%d member_id=%d username=%s pos=%s bot=%b]", getIndex(), getMemberId(), getUsername(), getPosition(), isBot);
    }

    public void message(boolean filtered, String... messages) {
        for (String message : messages) {
            send(new SendMessage(message, filtered));
        }
    }

    public void message(String... messages) {
        for (String message : messages) {
            send(new SendMessage(message));
        }
    }

    public boolean isAutoRetaliate() {
        return settings.autoRetaliate;
    }

    public boolean isSpecialActivated() {
        return specialActivated;
    }

    public void setSpecialActivated(boolean activated) {
        this.specialActivated = activated;
    }

    public void setCombatSpecial(CombatSpecial special) {
        this.combatSpecial = special;
    }

    public boolean isSingleCast() {
        return singleCast != null;
    }

    CombatSpell getSingleCastSpell() {
        return singleCast;
    }

    public void setSingleCast(CombatSpell singleCast) {
        this.singleCast = singleCast;
    }

    public boolean isAutocast() {
        return autocast != null;
    }

    CombatSpell getAutocastSpell() {
        return autocast;
    }

    public void setAutocast(CombatSpell autocast) {
        this.autocast = autocast;
    }

    public MutableNumber getSpecialPercentage() {
        return specialPercentage;
    }

    public final AtomicInteger teleblockTimer = new AtomicInteger(0);

    public void teleblock(int time) {
        if (time <= 0 || (teleblockTimer.get() > 0)) {
            return;
        }

        teleblockTimer.set(time);
        send(new SendMessage("A teleblock spell has been casted on you!"));
        send(new SendWidget(SendWidget.WidgetType.TELEBLOCK, (int) ((double) teleblockTimer.get() / 1000D * 600D)));
        World.schedule(new TeleblockTask(this));
    }

    public boolean isTeleblocked() {
        return teleblockTimer.get() > 0;
    }

    public CombatSpecial getCombatSpecial() {
        return combatSpecial;
    }

    public WeaponInterface getWeapon() {
        return weapon;
    }

    public void setWeapon(WeaponInterface weapon) {
        this.weapon = weapon;
    }

    public Optional<AntifireDetails> getAntifireDetails() {
        return Optional.ofNullable(antifireDetails);
    }

    public void setAntifireDetail(AntifireDetails antifireDetails) {
        this.antifireDetails = antifireDetails;
    }

    public final MutableNumber getPoisonImmunity() {
        return poisonImmunity;
    }

    public final MutableNumber getVenomImmunity() {
        return venomImmunity;
    }

    public Optional<ChatMessage> getChatMessage() {
        return chatMessage;
    }

    public String getUsername() {
        return username;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getMemberId() {
        return memberId;
    }

    public Optional<GameSession> getSession() {
        return session;
    }

    public void setSession(GameSession session) {
        this.session = Optional.of(session);

        final String host = session.getHost();
        this.lastHost = host;
        this.hostList.add(host);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public Appearance getAppearance() {
        return appearance;
    }

    public Events getEvents() {
        return events;
    }

}
