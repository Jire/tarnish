package com.osroyale.game.world.entity.mob.npc.drop;

import com.osroyale.Config;
import com.osroyale.content.CrystalChest;
import com.osroyale.content.achievement.AchievementHandler;
import com.osroyale.content.achievement.AchievementKey;
import com.osroyale.content.collectionlog.CollectionLog;
import com.osroyale.content.pet.PetData;
import com.osroyale.content.pet.Pets;
import com.osroyale.content.skill.impl.prayer.AshData;
import com.osroyale.content.skill.impl.prayer.BoneData;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.ground.GroundItem;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.discord.DiscordPlugin;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.net.packet.out.SendScreenshot;
import com.osroyale.util.Items;
import com.osroyale.util.RandomGen;
import com.osroyale.util.Utility;
import plugin.itemon.item.LootingBagPlugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The manager class which holds the static entries of drop tables and has
 * a method to execute a drop table from the specified npc.
 *
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 29-1-2017.
 */
public final class NpcDropManager {

    /**
     * The collection of npc ids by their representative drop tables.
     */
    public static final Map<Integer, NpcDropTable> NPC_DROPS = new HashMap<>();

    /**
     * Alternative drop positions for mobs (i.e kraken, zulrah)
     */
    private static final int[] ALTERNATIVE_POSITION = {2044, 2043, 2042, 494, 8060};

    // use new drop system
    private static final boolean USE_NEW_SYSTEM = true;

    /**
     * Attempts to drop the drop table which belongs to {@code npc#id}.
     */
    public static void drop(Player killer, Npc npc) {
        if (killer == null) {
            return;
        }

        if (npc == null) {
            return;
        }

        if (USE_NEW_SYSTEM) {
            rollDrop(killer, npc);
            AchievementHandler.activate(killer, AchievementKey.NPCS);
            return;
        }

        NpcDropTable table = NPC_DROPS.get(npc.id);

        if (table == null) {
            return;
        }

        RandomGen gen = new RandomGen();
        List<NpcDrop> npc_drops = table.generate(killer, false);
        Position dropPosition = npc.getPosition().copy();

        // special drop positions
        if (checkAlternativePosition(npc.id)) {
            dropPosition = killer.getPosition().copy();
        }

        // crystal key drop
        if (npc.getMaximumHealth() > 50 && Utility.random(150) <= 5) {
            Item crystal_key = Utility.randomElement(CrystalChest.KEY_HALVES);
            GroundItem.create(killer, crystal_key, dropPosition);
            killer.send(new SendMessage("<col=BA383E>Rare Drop Notification: </col>" + crystal_key.getName()));
            AchievementHandler.activate(killer, AchievementKey.RARE_DROPS);
        }

        // casket drop
        if (npc.getMaximumHealth() > 10 && Utility.random(200) <= 10) {
            Item casket = new Item(405);
            GroundItem.create(killer, casket, dropPosition);
            killer.send(new SendMessage("<col=BA383E>Rare Drop Notification: </col>" + casket.getName(), true));
            AchievementHandler.activate(killer, AchievementKey.RARE_DROPS);
        }

        // drop table
        for (NpcDrop drop : npc_drops) {
            Item item = drop.toItem(gen);

            if (item.getId() == 11941 && (killer.playerAssistant.contains(new Item(LootingBagPlugin.OPENED_ID)) || killer.playerAssistant.contains(item))) { // looting bag
                killer.message(true, "You have missed out on " + Utility.getAOrAn(item.getName()) + " " + item.getName() + " since you already have on on your account.");
                continue;
            }

//            if (killer.clanChannel != null && killer.clanChannel.lootshareEnabled()) {
//                killer.forClan(channel -> channel.splitLoot(killer, npc, item));
//                return;
//            }

            if (killer.settings.dropNotification && item.getValue() > 1_000_000) {
                String name = item.getName();
                killer.send(new SendMessage("<col=BA383E>Exotic Drop Notification: </col>" + name + " (" + Utility.formatDigits(item.getValue()) + " coins)"));
                World.sendMessage("<col=BA383E>Tarnish: <col=" + killer.right.getColor() + ">" + killer.getName() + " </col>has just received " + Utility.getAOrAn(name) + " <col=BA383E>" + name + " </col>from <col=BA383E>" + npc.getName() + "</col>!");
                DiscordPlugin.sendSimpleMessage(killer.getName() + " has just received " + Utility.getAOrAn(name) + " " + name + " from " + npc.getName() + "!");
                if (killer.settings.screenshotKill) {
                    killer.getPlayer().send(new SendScreenshot());
                }
            } else if (killer.settings.dropNotification && item.getValue() > 100_000) {
                String name = item.getName();
                killer.send(new SendMessage("<col=BA383E>Rare Drop Notification: </col>" + name + " (" + Utility.formatDigits(item.getValue()) + " coins)"));
                AchievementHandler.activate(killer, AchievementKey.RARE_DROPS);
            } else if (killer.settings.untradeableNotification && !item.isTradeable()) {
                killer.send(new SendMessage("<col=F5424B>Untradeable Drop Notification: </col>" + item.getName()));
            }

            CollectionLog.checkItemDrop(killer, npc.id, item.getId(), item.getAmount());

            if (npc.id == 494 && item.getId() == 11907) {
                tridentRunes(killer, dropPosition);
            }

            if (!item.isStackable()) {
                Item single = item.createWithAmount(1);
                for (int i = 0; i < item.getAmount(); i++)
                    GroundItem.create(killer, single, dropPosition);
            } else {
                GroundItem.create(killer, item, dropPosition);
            }
        }
    }

    private static void tridentRunes(Player killer, Position dropPosition) {
        List<Item> fullChargeItems = Arrays.asList(
                new Item(Items.DEATH_RUNE, 2500), new Item(Items.CHAOS_RUNE, 2500),
                new Item(Items.FIRE_RUNE, 12500), new Item(Items.COINS, 25000)
        );

        fullChargeItems.forEach(item -> GroundItem.create(killer, item, dropPosition));
    }

    private static void handleBoneCrusher(Player player, BoneData bone) {
        if (bone == null) { return; }

        var exp = (bone.getExperience() * Config.PRAYER_MODIFICATION);
        player.skills.addExperience(Skill.PRAYER, exp);
        AchievementHandler.activate(player, AchievementKey.BURY_BONES, 1);

        if (player.equipment.hasAmulet() && player.equipment.getAmuletSlot().getId() == 22111) {
            int current = player.skills.getLevel(Skill.PRAYER);
            int maximum = player.skills.getMaxLevel(Skill.PRAYER);

            if (current < maximum) {
                int points = bone.getBoneAmulet();

                if (current + points > maximum) {
                    points = maximum - current;
                }

                player.skills.get(Skill.PRAYER).addLevel(points);
                player.skills.refresh(Skill.PRAYER);
                player.message("You feel your " + player.equipment.getAmuletSlot().getName() + " vibrate as you bury the bone.");
            }
        }
    }

    private static void handleAshSanctifier(Player player, AshData ashes) {
        if (ashes == null) { return; }
        var exp = (ashes.getExperience() * Config.PRAYER_MODIFICATION);
        player.skills.addExperience(Skill.PRAYER, exp);
    }

    private static NpcDrop rollDrop(Player player, Npc npc) {
        //   player.send(new SendMessage("Called rollDrop"));
        NpcDropTable table = NPC_DROPS.get(npc.id);
        Position dropPos = npc.getPosition().copy();
        if (checkAlternativePosition(npc.id)) {
            dropPos = player.getPosition().copy();
        }
        if (table == null) {
            return null;
        }
        handleMiscDrops(player, npc);
        NpcDrop[] drops = table.drops;
        for (NpcDrop drop : drops) {
            if (drop.getWeight() == 0) {
                Item item = drop.toItem(new RandomGen());
                if (BoneData.forId(item.getId()).isPresent() && !item.isNoted() && player.inventory.contains(Items.BONECRUSHER)) {
                    handleBoneCrusher(player, BoneData.forId(item.getId()).get());
                    continue;
                }
                if (AshData.forId(item.getId()).isPresent() && !item.isNoted() && player.inventory.contains(25781)) {
                    handleAshSanctifier(player, AshData.forId(item.getId()).get());
                    continue;
                }
                /**
                 * Handle the pet roll if the item is a pet
                 */
                if(PetData.forItem(item.getId()).isPresent()) {
                    Pets.onReward(player, item.getId());
                } else {
                    GroundItem.create(player, item, dropPos);
                }
            }
        }
        WeightedCollection<NpcDrop> rc = new WeightedCollection<>();
        for (NpcDrop drop : drops) {
            double drOffset = 1 + (PlayerRight.getDropRateBonus(player) / 100.0); // dr bonus is in percent, conversion is 1 + (percent / 100.0)
            int weightMultiplier = 1;
            double dropChance = (drop.getWeight() * weightMultiplier) / drOffset;
            if (dropChance < 1) {
                dropChance = 1;
            }
            double weight = 1.0 / dropChance;
            if (drop.getWeight() != 0) {
                rc.add(weight, drop);
            }
        }

        double weightSum = rc.getTotal();

        if (Math.random() <= weightSum) {
            NpcDrop selectedDrop = rc.next();
            Item item = selectedDrop.toItem(new RandomGen());
            handleMessages(player, npc, item);
            /**
             * Handle the pet roll if the item is a pet
             */
            if (npc.id == 494 && item.getId() == 11907) {
                tridentRunes(player, dropPos);
            }
            if(PetData.forItem(item.getId()).isPresent()) {
                Pets.onReward(player, item.getId());
            } else {
                GroundItem.create(player, item, dropPos);
            }
            CollectionLog.checkItemDrop(player, npc.id, item.getId(), item.getAmount());
            return selectedDrop;
        }

        return null;
    }

    private static void handleMiscDrops(Player player, Npc npc) {
        Position dropPosition = npc.getPosition().copy();
        if (checkAlternativePosition(npc.id)) {
            dropPosition = player.getPosition().copy();
        }
        if (npc.getMaximumHealth() > 50 && Utility.random(150) <= 5) {
            Item crystal_key = Utility.randomElement(CrystalChest.KEY_HALVES);
            GroundItem.create(player, crystal_key, dropPosition);
            player.send(new SendMessage("<col=BA383E>Rare Drop Notification: </col>" + crystal_key.getName()));
            AchievementHandler.activate(player, AchievementKey.RARE_DROPS);
        }

        // casket drop
        if (npc.getMaximumHealth() > 10 && Utility.random(200) <= 10) {
            Item casket = new Item(405);
            GroundItem.create(player, casket, dropPosition);
            player.send(new SendMessage("<col=BA383E>Rare Drop Notification: </col>" + casket.getName(), true));
            AchievementHandler.activate(player, AchievementKey.RARE_DROPS);
        }
    }

    private static void handleMessages(Player player, Npc npc, Item item) {
        if (item.getId() == 11941 && (player.playerAssistant.contains(new Item(LootingBagPlugin.OPENED_ID)) || player.playerAssistant.contains(item))) { // looting bag
            player.message("You have missed out on " + Utility.getAOrAn(item.getName()) + " " + item.getName() + " since you already have on on your account.");
            return;
        }

        if (player.settings.dropNotification && item.getValue() > 1_000_000) {
            String name = item.getName();
            player.send(new SendMessage("<col=BA383E>Exotic Drop Notification: </col>" + name + " (" + Utility.formatDigits(item.getValue()) + " coins)"));
            World.sendMessage("<col=BA383E>Tarnish: <col=" + player.right.getColor() + ">" + player.getName() + " </col>has just received " + Utility.getAOrAn(name) + " <col=BA383E>" + name + " </col>from <col=BA383E>" + npc.getName() + "</col>!");
            DiscordPlugin.sendSimpleMessage("Exotic drop: " + player.getName() + " has just received " + Utility.getAOrAn(name) + " " + name + " from " + npc.getName() + "!");
            AchievementHandler.activate(player, AchievementKey.RARE_DROPS);
            if (player.settings.screenshotKill) {
                player.getPlayer().send(new SendScreenshot());
            }
        } else if (player.settings.dropNotification && item.getValue() > 100_000) {
            String name = item.getName();
            player.send(new SendMessage("<col=BA383E>Rare Drop Notification: </col>" + name + " (" + Utility.formatDigits(item.getValue()) + " coins)"));
            AchievementHandler.activate(player, AchievementKey.RARE_DROPS);
        } else if (player.settings.untradeableNotification && !item.isTradeable()) {
            player.send(new SendMessage("<col=F5424B>Untradeable Drop Notification: </col>" + item.getName()));
        }
    }

    private static boolean checkAlternativePosition(int npc) {
        for (int alternative : ALTERNATIVE_POSITION) {
            if (alternative == npc)
                return true;
        }
        return false;
    }
}
