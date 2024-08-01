package com.osroyale.game.world.entity.mob.player;

import com.osroyale.Config;
import com.osroyale.content.achievement.AchievementHandler;
import com.osroyale.content.achievement.AchievementKey;
import com.osroyale.content.activity.Activity;
import com.osroyale.content.activity.ActivityDeathType;
import com.osroyale.content.bot.BotUtility;
import com.osroyale.content.bot.PlayerBot;
import com.osroyale.content.event.EventDispatcher;
import com.osroyale.content.event.impl.OnKillEvent;
import com.osroyale.content.itemaction.impl.CrawsBow;
import com.osroyale.content.itemaction.impl.ThammaronsSceptre;
import com.osroyale.content.itemaction.impl.ViggorasChainmace;
import com.osroyale.content.lms.LMSGame;
import com.osroyale.content.pet.Pets;
import com.osroyale.content.writer.InterfaceWriter;
import com.osroyale.content.writer.impl.InformationWriter;
import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.combat.weapon.WeaponInterface;
import com.osroyale.game.world.entity.mob.Direction;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.MobDeath;
import com.osroyale.game.world.entity.mob.UpdateFlag;
import com.osroyale.game.world.entity.mob.prayer.Prayer;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.ground.GroundItem;
import com.osroyale.game.world.position.Area;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.packet.out.SendCameraReset;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.Utility;
import plugin.click.item.ImbuedHeartPlugin;
import plugin.itemon.item.LootingBagPlugin;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Handles the player death listener.
 *
 * @author Daniel
 */
public final class PlayerDeath extends MobDeath<Player> {
    private boolean safe = false;

    /** Array of all death messages. */
    private static final String[] DEATH_MESSAGES = {
            "You have defeated $VICTIM.",
            "With a crushing blow, you defeat $VICTIM.",
            "It's a humiliating defeat for $VICTIM.",
            "$VICTIM didn't stand a chance against you.",
            "You have defeated $VICTIM.",
            "It's all over for $VICTIM.",
            "$VICTIM regrets the day they met you in combat.",
            "$VICTIM falls before your might.",
            "Can anyone defeat you? Certainly not $VICTIM.",
            "You were clearly a better fighter than $VICTIM."
    };

    /** Creates a new {@link MobDeath}. */
    PlayerDeath(Player mob) {
        super(mob, 2);
    }

    /** The part of the death process where the character is prepared for the rest of the death tick. */
    @Override
    public void preDeath(Mob killer) {
        mob.animate(new Animation(836, UpdatePriority.VERY_HIGH));
        mob.graphic(new Graphic(293, UpdatePriority.VERY_HIGH));
    }

    /** The main part of the death process where the killer is found for the character. */
    @Override
    public void death() {
        Mob killer = mob.getCombat().getDamageCache().calculateProperKiller().orElse(null);

        if (mob.inActivity()) {
            ActivityDeathType deathType = mob.activity.deathType();

            if (PlayerRight.isAdministrator(mob)) {
                safe = true;
                return;
            }

            if (deathType == ActivityDeathType.SAFE) {
                safe = true;
            } else if (deathType == ActivityDeathType.NORMAL) {
                Pets.onDeath(mob);
                calculateDropItems(mob, killer, false);
            } else if (deathType == ActivityDeathType.PURCHASE) {
                calculateDropItems(mob, killer, true);
                mob.message("You have lost your items. Speak to Lord Marshal Brogan at home to claim them!");
            }
            return;
        }
        if(mob.playTime < 6000) {
            System.out.println("Safe death");
            safe = true;
            mob.message("Your death is safe because you have less than 1 hour of playtime.");
            return;
        }
        if (Area.inZulrah(mob)) {
            safe = true;
            return;
        }

      /*  if (Area.inEventArena(mob)) {
            safe = true;
            return;
        }*/

        if(mob.isPlayer() && LMSGame.inGameArea(mob.getPlayer()) && killer != null && killer.isPlayer()) {
            safe = true;
            LMSGame.onKill(killer.getPlayer());
            return;
        }

        if (!PlayerRight.isAdministrator(mob)) {
            Pets.onDeath(mob);
            calculateDropItems(mob, killer, false);
        }

        if (killer == null || !killer.isPlayer())
            return;

        Player playerKiller = killer.getPlayer();

        if (mob.isBot) {
            playerKiller.message("<col=295EFF>You were rewarded with 100 blood money for that bot kill.");
            playerKiller.inventory.addOrDrop(new Item(13307, 100));
            return;
        }

        playerKiller.kill++;
        mob.death++;

        if (!PlayerKilling.contains(playerKiller, mob.lastHost)) {
            AchievementHandler.activate(playerKiller, AchievementKey.KILLER, 1);
            playerKiller.send(new SendMessage(Utility.randomElement(DEATH_MESSAGES).replace("$VICTIM", mob.getName())));
            PlayerKilling.handle(playerKiller, mob);
        } else {
            playerKiller.message("<col=FF0019>You have recently killed " + mob.getName() + " and were not rewarded. You must kill 2 other players to reset this!");
        }

        EventDispatcher.execute(playerKiller, new OnKillEvent(mob));
    }

    /** The last part of the death process where the character is reset. */
    @Override
    public void postDeath(Mob killer) {
        if (mob.isBot) {
            ((PlayerBot) mob).postDeath();
            return;
        }

        mob.playerAssistant.restore();

        if(mob.isPlayer() && LMSGame.isActivePlayer(mob.getPlayer())) {
            LMSGame.onDeath(mob.getPlayer(), false);
            return;
        }

        if (mob.inActivity()) {
            Activity.forActivity(mob, it -> it.onDeath(mob));
            return;
        }
        if(mob.isPlayer()) {
            ImbuedHeartPlugin.resetCooldown(mob);
        }
        mob.hasPvPTimer = false;
        mob.move(mob.pvpInstance ? new Position(3093, 3494,4) : Config.DEFAULT_POSITION);
        mob.send(new SendMessage("Oh dear, you are dead!"));
        mob.send(new SendCameraReset());
        mob.face(Direction.SOUTH);
        mob.equipment.updateAnimation();
        mob.equipment.refresh();
        mob.animate(Animation.RESET, true);
        mob.graphic(Graphic.RESET, true);
        Pets.onDeath(mob);
        WeaponInterface.execute(mob, mob.equipment.getWeapon());
        InterfaceWriter.write(new InformationWriter(mob));

        if (!safe) {
            if (killer != null && killer.isPlayer() && !mob.equals(killer)) {
                mob.killstreak.end(killer.getPlayer());
            }
            if (mob.right == PlayerRight.HARDCORE_IRONMAN) {
                mob.right = PlayerRight.IRONMAN;
                mob.updateFlags.add(UpdateFlag.APPEARANCE);
                mob.send(new SendMessage("You have lost your hardcore iron man status since you died!"));
                World.sendMessage("<icon=1> <col=FF0000>Tarnish: <col=" + mob.right.getColor() + ">" + mob.getName() + "</col>'s hardcore iron man account was lost!");
            }
        }

        if (mob.presetManager.deathOpen) {
            World.schedule(1, mob.presetManager::open);
        }

        if (!mob.lostItems.isEmpty()) {
            mob.dialogueFactory.sendStatement("There are lost items waiting for you!", "Speak to Lord Marshal Brogan at home to claim them!").execute();
        }
    }

    /** Calculates and drops all of the items from {@code character} for {@code killer}. */
    private void calculateDropItems(Player character, Mob killer, boolean purchase) {
        Player theKiller = killer == null || killer.isNpc() ? character : killer.getPlayer();

        if (character.right.equals(PlayerRight.ULTIMATE_IRONMAN)) {
            List<Item> items = new LinkedList<>();
            character.equipment.forEach(items::add);
            character.inventory.forEach(items::add);
            character.lootingBag.forEach(items::add);
            character.equipment.clear();
            character.inventory.clear();
            character.lootingBag.clear();
            items.forEach(item -> {
                if (!item.isTradeable()) {
                    if (!character.lostUntradeables.deposit(item)) {
                        GroundItem.create(character, item);
                    }
                } else {
                    GroundItem.create(theKiller, item, character.getPosition());
                }
            });
            return;
        }

        LinkedList<Item> toDrop = new LinkedList<>();
        List<Item> keep = new LinkedList<>();
        List<Item> items = new LinkedList<>();
        character.equipment.forEach(items::add);
        Item[] lootingBag = character.lootingBag.getDeathItems();
        character.inventory.forEach(item -> {
            if (!LootingBagPlugin.isLootingBag(item)) {
                items.add(item);
            }
        });
        character.equipment.clear();
        character.inventory.clear();

        if (lootingBag != null) {
            items.addAll(Arrays.asList(lootingBag));
            character.lootingBag.clear();
        }

        toDrop.addAll(items);

        toDrop.sort((first, second) -> second.getValue() - first.getValue());

        if (!character.skulling.isSkulled()) {
            keep.add(toDrop.pollFirst());
            keep.add(toDrop.pollFirst());
            keep.add(toDrop.pollFirst());
        }

        if (character.prayer.isActive(Prayer.PROTECT_ITEM)) {
            keep.add(toDrop.pollFirst());
        }

        keep.forEach(item -> {
            if (item == null) {
                return;
            }

            character.inventory.add(new Item(item.getId()));
            if (item.isStackable() && item.getAmount() > 1) {
                toDrop.add(item.createAndDecrement(1));
            }
        });

        if (theKiller.isBot) {
            toDrop.forEach(item -> {
                if (character.runecraftPouch.death(item))
                    return;

                if (character.runePouch.death(item))
                    return;

                if (!item.isTradeable()) {
                    if (!character.lostUntradeables.deposit(item)) {
                        GroundItem.create(character, item);
                    }
                    return;
                }

                if (theKiller.isBot && item.getValue() >= 50_000) {
                    return;
                }

                BotUtility.logLoot(item);
            });

            GroundItem drop = GroundItem.create(theKiller, new Item(526), character.getPosition());
            if (!theKiller.equals(character) && PlayerRight.isIronman(theKiller)) {
                drop.canIronMenPickThisItemUp = false;
            }
            return;
        }

        toDrop.forEach(item -> {
            if (character.runecraftPouch.death(item))
                return;

            if (character.runePouch.death(item))
                return;

            if (purchase) {
                character.lostItems.add(item);
                return;
            }

            if (!item.isTradeable()) {
                if (!character.lostUntradeables.deposit(item)) {
                    GroundItem.create(character, item);
                }
                return;
            }

            if (theKiller.isBot && item.getValue() >= 50_000) {
                return;
            }

            switch(item.getId()) {
                case CrawsBow.CRAWS_CHARGED_ID -> {
                    item.setId(CrawsBow.CRAWS_UNCHARGED_ID);
                    GroundItem drop = GroundItem.create(theKiller, new Item(CrawsBow.ETHER_ID, 1000 + character.crawsBowCharges), character.getPosition());
                    if (!theKiller.equals(character) && PlayerRight.isIronman(theKiller)) {
                        drop.canIronMenPickThisItemUp = false;
                    }
                    character.crawsBowCharges = 0;
                }
                case ViggorasChainmace.VIGGORAS_CHAINMACE_CHARGED_ID -> {
                    item.setId(ViggorasChainmace.VIGGORAS_CHAINMACE_UNCHARGED_ID);
                    GroundItem drop = GroundItem.create(theKiller, new Item(CrawsBow.ETHER_ID, 1000 + character.viggorasChainmaceCharges), character.getPosition());
                    if (!theKiller.equals(character) && PlayerRight.isIronman(theKiller)) {
                        drop.canIronMenPickThisItemUp = false;
                    }
                    character.viggorasChainmaceCharges = 0;
                }
                case ThammaronsSceptre.THAMMARONS_SCEPTRE_CHARGED_ID -> {
                    item.setId(ViggorasChainmace.VIGGORAS_CHAINMACE_UNCHARGED_ID);
                    GroundItem drop = GroundItem.create(theKiller, new Item(CrawsBow.ETHER_ID, 1000 + character.thammoranSceptreCharges), character.getPosition());
                    if (!theKiller.equals(character) && PlayerRight.isIronman(theKiller)) {
                        drop.canIronMenPickThisItemUp = false;
                    }
                    character.thammoranSceptreCharges = 0;
                }
            }

            GroundItem drop = GroundItem.create(theKiller, item, character.getPosition());
            if (!theKiller.equals(character) && PlayerRight.isIronman(theKiller)) {
                drop.canIronMenPickThisItemUp = false;
            }
        });

        GroundItem drop = GroundItem.create(theKiller, new Item(526), character.getPosition());
        if (!theKiller.equals(character) && PlayerRight.isIronman(theKiller)) {
            drop.canIronMenPickThisItemUp = false;
        }
    }

}
