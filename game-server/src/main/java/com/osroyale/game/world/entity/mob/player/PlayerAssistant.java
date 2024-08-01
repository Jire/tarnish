package com.osroyale.game.world.entity.mob.player;

import com.osroyale.Config;
import com.osroyale.content.StarterKit;
import com.osroyale.content.ValueIcon;
import com.osroyale.content.achievement.AchievementWriter;
import com.osroyale.content.activity.Activity;
import com.osroyale.content.activity.ActivityType;
import com.osroyale.content.activity.GroupActivity;
import com.osroyale.content.activity.impl.CerberusActivity;
import com.osroyale.content.activity.impl.JailActivity;
import com.osroyale.content.activity.impl.VorkathActivity;
import com.osroyale.content.activity.impl.barrows.Barrows;
import com.osroyale.content.activity.impl.godwars.GodwarsActivity;
import com.osroyale.content.activity.impl.kraken.KrakenActivity;
import com.osroyale.content.activity.impl.warriorguild.WarriorGuild;
import com.osroyale.content.clanchannel.channel.ClanChannelHandler;
import com.osroyale.content.collectionlog.CollectionLogSaving;
import com.osroyale.content.emote.EmoteHandler;
import com.osroyale.content.pet.Pets;
import com.osroyale.content.skill.impl.farming.Farming;
import com.osroyale.content.skill.impl.magic.teleport.TeleportType;
import com.osroyale.content.skillcape.SkillCape;
import com.osroyale.content.writer.InterfaceWriter;
import com.osroyale.content.writer.impl.InformationWriter;
import com.osroyale.game.task.impl.SuperAntipoisonTask;
import com.osroyale.game.task.impl.TeleblockTask;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.combat.CombatConstants;
import com.osroyale.game.world.entity.combat.CombatTarget;
import com.osroyale.game.world.entity.combat.CombatType;
import com.osroyale.game.world.entity.combat.CombatUtil;
import com.osroyale.game.world.entity.combat.attack.FormulaFactory;
import com.osroyale.game.world.entity.combat.attack.listener.other.PrayerListener;
import com.osroyale.game.world.entity.combat.attack.listener.other.VengeanceListener;
import com.osroyale.game.world.entity.combat.effect.CombatEffectType;
import com.osroyale.game.world.entity.combat.strategy.CombatStrategy;
import com.osroyale.game.world.entity.combat.strategy.player.PlayerMagicStrategy;
import com.osroyale.game.world.entity.combat.strategy.player.PlayerMeleeStrategy;
import com.osroyale.game.world.entity.combat.strategy.player.PlayerRangedStrategy;
import com.osroyale.game.world.entity.combat.strategy.player.custom.*;
import com.osroyale.game.world.entity.combat.strategy.player.special.CombatSpecial;
import com.osroyale.game.world.entity.combat.weapon.WeaponInterface;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.UpdateFlag;
import com.osroyale.game.world.entity.mob.npc.definition.NpcDefinition;
import com.osroyale.game.world.entity.mob.prayer.Prayer;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.containers.ItemContainer;
import com.osroyale.game.world.items.containers.equipment.Equipment;
import com.osroyale.game.world.items.containers.pricechecker.PriceType;
import com.osroyale.game.world.position.Area;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.packet.out.*;
import com.osroyale.net.packet.out.SendWidget.WidgetType;
import com.osroyale.util.TinterfaceText;
import com.osroyale.util.Utility;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Method handles small methods for players that do not have any parent class.
 *
 * @author Daniel | Obey
 */
public class PlayerAssistant {

    /** The player instance. */
    private final Player player;

    /** Holds itemcontainer strings. */
    private Map<Integer, TinterfaceText> interfaceText = new HashMap<>();

    /** Creates a new <code>PlayerAssistant<code> */
    PlayerAssistant(Player player) {
        this.player = player;
    }

    /** Handles initializing all the player assistant methods on login. */
    public final void login() {
        reset();
        setAttribute();
        setPrayer();
        initialize();
        setSidebar(false, true);
        setActivity();
        setContextMenu();
        setEffects();
        Pets.onLogin(player);
        EmoteHandler.refresh(player);
        ClanChannelHandler.onLogin(player);
        player.setCollectionLog(CollectionLogSaving.load(player));
        player.runePouch.refresh();
    }

    /** Sets the effects for the player. */
    private void setEffects() {
        if (player.getPoisonImmunity().get() > 0) {
            World.schedule(new SuperAntipoisonTask(player).attach(player));
        }
        if (player.skulling.getSkullRemoveTask().getSkullTime() > 0) {
            player.skulling.skull();
        }

        if (player.teleblockTimer.get() > 0) {
            player.send(new SendWidget(SendWidget.WidgetType.TELEBLOCK, (int) ((double) player.teleblockTimer.get() / 1000D * 600D)));
            World.schedule(new TeleblockTask(player));
        }

    }

    /** initializes the player's random bs. */
    private void initialize() {
        player.getCombat().resetTimers(-CombatConstants.COMBAT_LOGOUT_COOLDOWN);
        player.send(new SendEntityFeed());
        player.send(new SendSpecialAmount());
        InterfaceWriter.write(new InformationWriter(player));
        InterfaceWriter.write(new AchievementWriter(player));
    }

    /** Handles the method that will occur on sequence. */
    public void sequence() {
        skillRestore();
        updateSpecial();
        prayerDrain();
        run();
        runRestore();
        player.getCombat().tick();
        Activity.forActivity(player, it -> {
            if (!(it instanceof GroupActivity))
                it.sequence();
        });

        CombatTarget.checkAggression(player);

        if (player.sequence % 20 == 0) {
            Farming.tick(player);
        }
    }

    /** Handles getting the combat strategy. */
    public CombatStrategy<Player> getStrategy() {
        if (player.isSingleCast())
            return new PlayerMagicStrategy(player.getSingleCastSpell());
        if (player.isAutocast())
            return new PlayerMagicStrategy(player.getAutocastSpell());
        if (player.isSpecialActivated()) {
            if (player.getCombatSpecial() == null) {
                player.setSpecialActivated(false);
                player.send(new SendSpecialEnabled(0));
            } else {
                return player.getCombatSpecial().getStrategy();
            }
        }
        Item item = player.equipment.get(Equipment.WEAPON_SLOT);
        if (item != null) {
            if(item.getId() == 22325) {
                return ScytheOfViturStrategy.get();
            }
            if (item.getId() == 11907) {
                return TridentOfTheSeasStrategy.get();
            }
            if (item.getId() == 27275) {
                return TumekenStrategy.get();
            }

            if (item.getId() == 12899) {
                return TridentOfTheSwampStrategy.get();
            }

            if (item.getId() == 12926) {
                return ToxicBlowpipeStrategy.get();
            }

            if (item.getRangedDefinition().isPresent()) {
                return PlayerRangedStrategy.get();
            }
        }
        return PlayerMeleeStrategy.get();
    }

    /** Updates the special amount. */
    private void updateSpecial() {
        if (player.getSpecialPercentage().get() < 100 && player.sequence % 60 == 0)
            CombatSpecial.restore(player, 10);
    }

    /** Sends all the side-bar identifications to the {@code Player}'s client. **/
    public final void setSidebar(boolean disabled) {
        setSidebar(disabled, false);
    }

    public final void setSidebar(boolean disabled, boolean login) {
        for (int index = 0; index < Config.SIDEBAR_INTERFACE.length; index++) {
            player.interfaceManager.setSidebar(Config.SIDEBAR_INTERFACE[index][0], disabled ? -1 : Config.SIDEBAR_INTERFACE[index][1]);
        }
        player.interfaceManager.setSidebar(Config.MAGIC_TAB, disabled ? -1 : player.spellbook.getInterfaceId());
        if (!disabled) {
            WeaponInterface.execute(player, player.equipment.getWeapon(), login);
            player.send(new SendConfig(980, 0));
        }
    }

    /** Sets all the player attributes. */
    private void setAttribute() {
        player.attributes.set("OBELISK", -1);
        player.attributes.set("BANK_KEY", false);
        player.attributes.set("TRADE_KEY", false);
        player.attributes.set("DUEL_KEY", false);
        player.attributes.set("RUN_FLAG_KEY", 10);
        player.attributes.set("PLAYER_TITLE_KEY", 0);
        player.attributes.set("FORCE_MOVEMENT", false);
        player.attributes.set("PRELOADING_SLOT_KEY", 0);
        player.attributes.set("PERSONAL_STORE_KEY", null);
        player.attributes.set("PRICE_CHECKER_KEY", false);
        player.attributes.set("DONATOR_DEPOSIT_KEY", false);
        player.attributes.set("TELEPORT_TYPE_KEY", TeleportType.FAVORITES);
    }

    /** Sets the activity for the player. */
    private void setActivity() {

        if (player.punishment.isJailed()) {
            JailActivity.create(player);
        } else if (player.needsStarter) {
            StarterKit.open(player);
        } else if (Area.inGodwars(player)) {
            GodwarsActivity.create(player);
        } else if (Area.inWarriorGuild(player)) {
            WarriorGuild.create(player);
        } else if (Area.inBarrows(player)) {
            Barrows.create(player);
        } else if (Area.inVorkath(player)) {
            VorkathActivity.create(player);
        } else if (Area.inKraken(player)) {
            KrakenActivity.create(player);
        } else if (Area.inCerberus(player)) {
            CerberusActivity.create(player);
        } else if (Area.inWilderness(player)) {
            player.valueIcon = getValueIcon(player).getCode();
            player.updateFlags.add(UpdateFlag.APPEARANCE);
        }
    }

    /** Sends the context menu to the {@code Player}'s context. */
    private void setContextMenu() {
        if (Area.inDuelArenaLobby(player)) {
            player.send(new SendPlayerOption(PlayerOption.DUEL_REQUEST, false));
            player.send(new SendPlayerOption(PlayerOption.ATTACK, false, true));
      /*  } else if (Area.inWilderness(player) || Area.inEventArena(player)) {
            player.send(new SendPlayerOption(PlayerOption.ATTACK, true));
            player.send(new SendPlayerOption(PlayerOption.DUEL_REQUEST, false, true));*/
        } else if (Area.inDuelArena(player) || Area.inDuelObsticleArena(player)) {
            player.send(new SendPlayerOption(PlayerOption.ATTACK, true));
            player.send(new SendPlayerOption(PlayerOption.DUEL_REQUEST, false, true));
        } else {
            player.send(new SendPlayerOption(PlayerOption.ATTACK, false, true));
            player.send(new SendPlayerOption(PlayerOption.DUEL_REQUEST, false, true));
        }
        player.send(new SendPlayerOption(PlayerOption.FOLLOW, false));
        player.send(new SendPlayerOption(PlayerOption.TRADE_REQUEST, false));
        player.send(new SendPlayerOption(PlayerOption.VIEW_PROFILE, false));
    }

    /** Sets the prayer book. */
    public void setPrayer() {
        Consumer<Prayer> enable = prayer -> {
            if (player.prayer.anyActive(Prayer.RETRIBUTION, Prayer.REDEMPTION, Prayer.SMITE)) {
                player.getCombat().addListener(PrayerListener.get());
            }
            player.send(new SendConfig(prayer.getConfig(), 1));
            prayer.getListener().ifPresent(listener -> player.getCombat().addListener(listener));
        };
        Consumer<Prayer> disable = prayer -> {
            if (prayer == Prayer.RETRIBUTION || prayer == Prayer.REDEMPTION || prayer == Prayer.SMITE) {
                player.getCombat().removeListener(PrayerListener.get());
            }
            player.send(new SendConfig(prayer.getConfig(), 0));
            prayer.getListener().ifPresent(listener -> player.getCombat().removeListener(listener));
        };
        Consumer<Prayer> overhead = prayer -> {
            player.headIcon = prayer.getHeadIcon();
            player.updateFlags.add(UpdateFlag.APPEARANCE);
        };
        Consumer<Prayer> noOverhead = prayer -> {
            player.headIcon = -1;
            player.updateFlags.add(UpdateFlag.APPEARANCE);
        };
        player.prayer.setOnChange(enable, disable, overhead, noOverhead);
        Consumer<Prayer> qEnable = prayer -> player.send(new SendConfig(prayer.getQConfig(), 0));
        Consumer<Prayer> qDisable = prayer -> player.send(new SendConfig(prayer.getQConfig(), 1));
        player.quickPrayers.setOnChange(qEnable, qDisable, null, null);
        Arrays.stream(Prayer.values()).forEach(prayer -> {
            disable.accept(prayer);

            if (player.quickPrayers.isActive(prayer)) {
                qEnable.accept(prayer);
            } else {
                qDisable.accept(prayer);
            }
        });
    }

    /** Resets the player's variables. */
    public void restore() {
        reset();
        player.runEnergy = 100;
        player.skulling.unskull();
        player.skills.restoreAll();
        player.inventory.refresh();
        player.equipment.login();
        player.action.reset();
        player.interfaceManager.close();
        player.setSpecialActivated(false);
        player.send(new SendSpecialEnabled(0));
        player.getCombat().getDamageCache().clear();
        player.send(new SendRunEnergy());
        player.movement.reset();
        player.teleblockTimer.set(0);
        player.spellCasting.castingDelay.reset();
        player.spellCasting.vengeanceDelay.reset();

        if (player.venged) {
            player.venged = false;
            player.getCombat().removeListener(VengeanceListener.get());
        }

        CombatSpecial.restore(player, 100);
    }

    /** Resets the player's variables. */
    public void reset() {
        player.unpoison();
        player.unvenom();
        resetEffects();
        setWidget();
        player.movement.reset();
        player.getCombat().reset();
        player.prayer.reset();
        player.headIcon = -1;
        player.updateFlags.add(UpdateFlag.APPEARANCE);
    }

    /** Reset's all the player's activated effects. */
    public void resetEffects() {
        CombatUtil.cancelEffect(player, CombatEffectType.TELEBLOCK);
        CombatUtil.cancelEffect(player, CombatEffectType.ANTIFIRE_POTION);
        CombatUtil.cancelEffect(player, CombatEffectType.SKULL);
        CombatUtil.cancelEffect(player, CombatEffectType.VENOM);
    }

    /** Sends all the widget data to the client. */
    public void setWidget() {
        for (WidgetType widget : WidgetType.values()) {
            player.send(new SendWidget(widget, 0));
        }
    }

    /** Handles the running. */
    private void run() {
        if (player.movement.isRunning() && player.movement.isMoving() && !player.isBot && !PlayerRight.isDeveloper(player)) {
            player.runEnergy--;
            if (player.runEnergy < 0)
                player.runEnergy = 0;
            if (player.runEnergy == 0)
                player.movement.setRunningToggled(false);
            player.send(new SendRunEnergy());
        }
    }

    /** Handles restoring the run energy. */
    private void runRestore() {
        if (player.resting && player.sequence % 50 == 0) {
            player.animate(player.right.getRestAnimation());
        }
        if (player.runEnergy < 100) {
            int rate = player.energyRate > 0 ? 2 : player.resting || SkillCape.isEquipped(player, SkillCape.AGILITY) ? 3 : 4;
            if (player.sequence % rate == 0) {
                player.runEnergy++;
                player.send(new SendRunEnergy());
            }
        }
        if (player.energyRate > 0) {
            player.energyRate--;
        }
    }

    public void activateSkilling(int amount) {
        if (Utility.hasOneOutOf(30)) {
            player.skillingPoints += amount;
            player.message("<col=D4379A>You have earned " + amount + " skilling point" + (amount > 1 ? "" : "s") + ". You now have a total of " + Utility.formatDigits(player.skillingPoints) + ".");
        }
    }

    /** Handles restoration of skills. */
    private void skillRestore() {
        if (player.sequence % 120 != 0 && player.sequence % 50 != 0)
            return;
        if (player.sequence % (SkillCape.isEquipped(player, SkillCape.HITPOINTS) ? 50 : 100) == 0) {
            player.skills.regress(Skill.HITPOINTS);
        } else if (player.sequence % 50 == 0 && player.prayer.isActive(Prayer.RAPID_HEAL)) {
            player.skills.regress(Skill.HITPOINTS);
        }
        for (int index = 0; index <= 6; index++) {
            if (index == Skill.HITPOINTS || index == Skill.PRAYER) continue;
            int amount = 100;
            Skill skill = player.skills.get(index);
            if (skill.getLevel() < skill.getMaxLevel() && player.prayer.isActive(Prayer.RAPID_RESTORE))
                amount = 50;
            if (skill.getLevel() > skill.getMaxLevel() && player.prayer.isActive(Prayer.PRESERVE))
                amount = 150;
            if (player.sequence % amount == 0)
                player.skills.regress(index);
        }
    }

    /** Handles draining prayer. */
    private void prayerDrain() {
        if (!player.prayer.isActive()) {
            return;
        }
        int bonus = player.getBonus(Equipment.PRAYER_BONUS);
        int rate = player.prayer.drainAmount(bonus);
        drainPrayer(rate);
    }

    public void drainPrayer(int amount) {
        if (amount > 0) {
            Skill skill = player.skills.get(Skill.PRAYER);
            skill.modifyLevel(level -> level - amount, 0, skill.getLevel());
            player.skills.refresh(Skill.PRAYER);

            if (skill.getLevel() == 0) {
                player.send(new SendMessage("You have run out of prayer points; you must recharge at an altar."));
                player.prayer.reset();
                player.getPlayer().send(new SendConfig(659, 0));
            }
        }
    }

    public void claimIronmanArmour() {
        if (player.right == PlayerRight.IRONMAN) {
            player.inventory.addOrDrop(new Item(12810), new Item(12811), new Item(12812));
        } else if (player.right == PlayerRight.ULTIMATE_IRONMAN) {
            player.inventory.addOrDrop(new Item(12813), new Item(12814), new Item(12815));
        } else if (player.right == PlayerRight.HARDCORE_IRONMAN) {
            player.inventory.addOrDrop(new Item(20792), new Item(20794), new Item(20796));
        }
        else {
            player.message("You must be an ironman to claim this armour.");
        }
    }

    /** Copy's the inventory and equipment of another player. */
    public void copy(Player other) {
        Item[] inventory = other.inventory.toArray();
        Item[] equipment = other.equipment.toArray();
        player.inventory.clear(false);
        player.equipment.clear(false);
        player.inventory.addAll(inventory);
        player.equipment.manualWearAll(equipment);
        player.inventory.refresh();
        player.equipment.login();
    }

    /** Tranforms player into npc. */
    public void transform(int npc) {
        if (npc == -1) {
            player.mobAnimation.reset();
            player.setWidth(1);
            player.setLength(1);
        } else {
            NpcDefinition def = NpcDefinition.get(npc);
            if (def != null) {
                player.mobAnimation.setStand(def.getStand());
                player.mobAnimation.setWalk(def.getWalk());
                player.mobAnimation.setTurn180(def.getTurn180());
                player.mobAnimation.setTurn90CW(def.getTurn90CW());
                player.mobAnimation.setTurn90CCW(def.getTurn90CCW());
                player.setWidth(def.getSize());
                player.setLength(def.getSize());
            }
        }
        player.id = npc;
        player.updateFlags.add(UpdateFlag.APPEARANCE);
    }

    /** Handles displaying the welcome itemcontainer. */
    void welcomeScreen() {
        player.setVisible(true);

//        boolean wants = player.settings.welcomeScreen;
//        if (!wants || Area.inWilderness(player) || player.getCombat().inCombat() || player.newPlayer || player.needsStarter) {
//            player.setVisible(true);
//            player.send(new SendScreenMode(player.settings.clientWidth, player.settings.clientHeight));
//            return;
//        }
//        if (player.settings.clientWidth > 765 || player.settings.clientWidth > 503) {
//            player.settings.clientWidth = 765;
//            player.settings.clientHeight = 503;
//            player.send(new SendScreenMode(765, 503));
//        }
//        for (int index = 0; index < 3; index++) {
//            player.send(new SendString(Config.WELCOME_DIALOGUE[index], 21315 + index));
//        }
//        player.send(new SendInterfaceAnimation(21310, Expression.HAPPY.getId()));
//        player.send(new SendMarquee(21319, Config.WELCOME_MARQUEE));
//        player.send(new SendString("You last logged in <col=EB4646>earlier today</col>.", 21318));//TODO
//        //black marks
//        player.send(new SendString("", 21320));
//        player.send(new SendString("You have 0 black marks!", 21321));
//        player.send(new SendString("Keep up the good work!", 21322));
//        player.send(new SendString("", 21323));
//        //bank pins
//        player.send(new SendString("You do not have a bank", 21324));
//        player.send(new SendString("pin set! Speak to any", 21325));
//        player.send(new SendString("banker to set one.", 21326));
//        player.send(new SendString("Bank pins are security!", 21327));
//        //other
//        player.send(new SendString("Click to vote", 21328));
//        player.send(new SendString("Click to donate", 21329));
//        player.send(new SendString("Click change e-mail", 21330));
//        player.send(new SendString("Click to change pass", 21331));
//        //announcement
//        player.send(new SendString(Config.WELCOME_ANNOUNCEMENT[0], 21332));
//        player.send(new SendString(Config.WELCOME_ANNOUNCEMENT[1], 21333));
//        player.send(new SendString(Config.WELCOME_ANNOUNCEMENT[2], 21334));
//        player.send(new SendString(Config.WELCOME_ANNOUNCEMENT[3], 21335));
//        player.send(new SendString(Config.WELCOME_ANNOUNCEMENT[4], 21336));
//        //update
//        player.send(new SendString(Config.WELCOME_UPDATE[0], 21337));
//        player.send(new SendString(Config.WELCOME_UPDATE[1], 21338));
//        player.send(new SendString(Config.WELCOME_UPDATE[2], 21339));
//        player.send(new SendString(Config.WELCOME_UPDATE[3], 21340));
//        player.send(new SendString(Config.WELCOME_UPDATE[4], 21341));
//        player.interfaceManager.open(450);
    }

    /** Handles sending the destroy item dialogue. */
    public void destroyItem(Item item, int slot) {
        player.send(new SendItemOnInterfaceSlot(14171, item, 0));
        player.send(new SendString("Are you sure you want to destroy this item?", 14174));
        player.send(new SendString("Yes.", 14175));
        player.send(new SendString("No.", 14176));
        player.send(new SendString("", 14177));
        player.send(new SendString(/*item.getDestroyMessage()*/"Doing this action is permanent!", 14182));
        player.send(new SendString("", 14183));
        player.send(new SendString(item.getName(), 14184));
        player.send(new SendChatBoxInterface(14170));
        player.attributes.set("DESTROY_ITEM_KEY", slot);
    }

    /** Handles destroying the item. */
    public void handleDestroyItem() {
        if (player.attributes.has("UNCHARGE_HELM_KEY")) {
            Item item = player.attributes.get("UNCHARGE_HELM_KEY");
            player.dialogueFactory.clear();
            player.inventory.replace(item, new Item(12_934, 20_000), true);
            player.send(new SendMessage("You have dismantled your " + item.getName() + "."));
            player.attributes.remove("UNCHARGE_HELM_KEY");
            return;
        }

        if (player.attributes.has("RESTORE_HELM_KEY")) {
            Item item = player.attributes.get("RESTORE_HELM_KEY");
            player.dialogueFactory.clear();
            if (item.matchesId(13_197)) {
                player.serpentineHelmCharges += player.tanzaniteHelmCharges;
                player.tanzaniteHelmCharges = 0;
                if (player.serpentineHelmCharges > 11_000) {
                    player.inventory.addOrDrop(new Item(12934, player.serpentineHelmCharges - 11_000));
                    player.serpentineHelmCharges = 11_000;
                }
                player.inventory.replace(item, new Item(player.serpentineHelmCharges <= 0 ? 12_929 : 12_931), true);
            } else if (item.matchesId(13_199)) {
                player.serpentineHelmCharges += player.magmaHelmCharges;
                player.magmaHelmCharges = 0;
                if (player.serpentineHelmCharges > 11_000) {
                    player.inventory.addOrDrop(new Item(12934, player.serpentineHelmCharges - 11_000));
                    player.serpentineHelmCharges = 11_000;
                }
            }
            player.inventory.replace(item, new Item(player.serpentineHelmCharges <= 0 ? 12_929 : 12_931), true);
            player.send(new SendMessage("You have restored your " + item.getName() + "."));
            player.attributes.remove("RESTORE_HELM_KEY");
            return;
        }

        int index = player.attributes.get("DESTROY_ITEM_KEY", Integer.class);
        if (index == -1)
            return;
        Item item = player.inventory.get(index);
        if (item == null)
            return;
        player.dialogueFactory.clear();
        player.inventory.remove(item, index);
        player.send(new SendMessage("You have destroyed your " + item.getName() + "."));
        player.attributes.remove("DESTROY_ITEM_KEY");
    }

    public boolean contains(Item item) {
        return player.inventory.contains(item) || player.equipment.contains(item) || player.bank.contains(item);
    }

    /** Gets the KDR of the player. */
    public String kdr() {
        double KDR = (player.kill / (double) player.death);
        return Double.isNaN(KDR) ? "0.0" : String.format("%.2f", KDR);
    }

    /** Gets the max hit of a combat type. */
    public int getMaxHit(Mob defender, CombatType type) {
        player.getCombat().addFirst(player.getStrategy());
        int max = FormulaFactory.getModifiedMaxHit(player, defender, type);
        player.getCombat().removeFirst();
        return max;
    }

    /** Gets the instanced height for player. */
    public int instance() {
        return player.getIndex() << 2;
    }

    /** Gets the total amount of 99s the player has. */
    public int getMaxSkillCount() {
        int count = 0;
        for (int index = 0; index < Skill.SKILL_COUNT; index++) {
            if (player.skills.getMaxLevel(index) == 99)
                count++;
        }
        return count;
    }

    public long networth() {
        return networth(player.inventory, player.equipment, player.bank);
    }

    /** Gets the net worth of the player. */
    public long networth(ItemContainer... containers) {
        long networth = 0;
        for (ItemContainer container : containers) {
            networth += container.containerValue(PriceType.VALUE);
        }
        return networth;
    }

    /** Gets the total carried weight of the player. */
    public double weight() {
        double weight = player.inventory.getWeight();
        weight += player.equipment.getWeight();
        return weight;
    }

    public ValueIcon getValueIcon(Player player) {
        ValueIcon icon = ValueIcon.NONE;
        long carrying = networth(player.inventory, player.equipment);

        if (carrying < 500_000)
            icon = ValueIcon.BRONZE;
        if (carrying > 500_000 && carrying < 1_500_000)
            icon = ValueIcon.SILVER;
        if (carrying > 1_500_000 && carrying < 5_000_000)
            icon = ValueIcon.GREEN;
        if (carrying > 5_000_000 && carrying < 25_000_000)
            icon = ValueIcon.BLUE;
        if (carrying > 25_000_000)
            icon = ValueIcon.RED;

        return icon;
    }

    public void setValueIcon() {
        ValueIcon icon = getValueIcon(player);

        final int currentIcon = player.valueIcon;

        if (icon.getCode() == currentIcon) {
            return;
        }

        player.valueIcon = icon.getCode();
        player.updateFlags.add(UpdateFlag.APPEARANCE);
    }

    String getCombatRange() {
        return (player.skills.getCombatLevel() - 10 < 3 ? 3 : player.skills.getCombatLevel() - 10) + " - " + (player.skills.getCombatLevel() + 10 > 126 ? 126 : player.skills.getCombatLevel() + 10);
    }

    /** Checks if player is busy. */
    public boolean busy() {
        return !player.interfaceManager.isMainClear() || player.isDead() || player.newPlayer ||
                player.needsStarter || player.locking.locked() || player.inActivity(ActivityType.DUEL_ARENA);
    }

    public void moveCamera(Position position, int resetDelay) {
        player.send(new SendCameraTurn(position.getChunkX(), position.getChunkY(), position.getHeight(), 5, 5));
        World.schedule(resetDelay, () -> player.send(new SendCameraReset()));
    }

    private static final int[] RING_OF_WEALTH = {
            2572, 11988, 11986, 11984, 11982, 11980
    };

    private int getROWIndex(int item) {
        int index = -1;
        for (int amulet = 0; amulet < RING_OF_WEALTH.length; amulet++) {
            if (item == RING_OF_WEALTH[amulet]) {
                return amulet;
            }
        }
        return index;
    }

    public void useROW() {
        for (int row : RING_OF_WEALTH) {
            int index = getROWIndex(row);
            if (player.equipment.contains(row)) {
                player.equipment.set(Equipment.RING_SLOT, new Item(RING_OF_WEALTH[index]), true);
                player.message("<col=7F007F>" + (index == 0 ? "You have used your last charge." : "Your ring of wealth has " + Utility.convertWord(index).toLowerCase() + "charge" + (index == 1 ? "" : "s") + " remaining."));
                break;
            }
        }
    }

    /** Checks if the string is already stored in the list. */
    public boolean checkSendString(String text, int id) {
        if (!interfaceText.containsKey(id)) {
            interfaceText.put(id, new TinterfaceText(text, id));
            return true;
        }
        TinterfaceText t = interfaceText.get(id);
        if (t.currentState.equals(text))
            return false;
        t.currentState = text;
        return true;
    }

    /** Clears the send strings */
    public void clearSendStrings() {
        interfaceText.clear();
    }
}
